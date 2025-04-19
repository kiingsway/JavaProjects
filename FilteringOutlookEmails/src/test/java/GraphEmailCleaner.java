import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GraphEmailCleaner {

    private final String token;

    public GraphEmailCleaner(String token) {
        this.token = token;
    }

    public void deleteInfoJobsEmails() {

        String email_to_delete = "fastshop@info.fastshop.com.br";


        try {
            String filter = "from/emailAddress/address eq '" + email_to_delete + "'";
            String endpoint = "https://graph.microsoft.com/v1.0/me/messages?$filter=" +
                    java.net.URLEncoder.encode(filter, StandardCharsets.UTF_8) +
                    "&$select=id,from,subject&$top=1000";

            // GET: Buscar mensagens
            HttpURLConnection getConn = (HttpURLConnection) new URL(endpoint).openConnection();
            getConn.setRequestMethod("GET");
            getConn.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = getConn.getResponseCode();
            if (responseCode != 200) {
                JOptionPane.showMessageDialog(null, "Erro ao buscar mensagens: " + responseCode,
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(getConn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray messages = jsonResponse.getJSONArray("value");

            if (messages.isEmpty()) {
                System.out.println("Nenhuma mensagem encontrada.");
                return;
            }

            // Loop: Deletar cada mensagem validada
            for (int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                String id = message.getString("id");
                String subject = message.getString("subject");

                JSONObject fromObj = message.optJSONObject("from");
                String address = fromObj != null
                        ? fromObj.optJSONObject("emailAddress").optString("address", "")
                        : "";

                if (!address.equalsIgnoreCase(email_to_delete)) {
                    System.out.println("❌ Ignorado (remetente diferente): " + address);
                    continue;
                }

                // DELETE: Deletar a mensagem
                String deleteUrl = "https://graph.microsoft.com/v1.0/me/messages/" + id;
                HttpURLConnection deleteConn = (HttpURLConnection) new URL(deleteUrl).openConnection();
                deleteConn.setRequestMethod("DELETE");
                deleteConn.setRequestProperty("Authorization", "Bearer " + token);

                int deleteResponseCode = deleteConn.getResponseCode();
                if (deleteResponseCode < 300) {
                    String txt = String.format("✅ [%d/%d] Deletado: \"%s\"", i + 1, messages.length(), subject);
                    System.out.println(txt);
                } else {

                    System.out.println("❌ Falha ao deletar: \"" + subject + "\" (HTTP " + deleteResponseCode + ") - " + deleteConn.getResponseMessage());
                    // System.out.println(deleteUrl);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Caixa de diálogo para inserir o token
        String token = JOptionPane.showInputDialog(null,
                "Insira o Bearer Token:",
                "Token de Acesso",
                JOptionPane.PLAIN_MESSAGE);

        if (token == null || token.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Token não informado. Encerrando aplicação.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Instancia e executa o GraphEmailCleaner
        GraphEmailCleaner cleaner = new GraphEmailCleaner(token.trim());
        cleaner.deleteInfoJobsEmails();
    }
}
