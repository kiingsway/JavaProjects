package org.example.model;

import org.example.Constants;
import org.example.model.interfaces.Me;
import org.example.model.interfaces.Message;
import org.json.JSONObject;

import javax.swing.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class GraphUser {

    private static final String graphUrl = "https://graph.microsoft.com/v1.0";

    private String token;
    private final List<Message> messages = new ArrayList<>();

    public GraphUser(String token) {this.token = token;}

    public Me getMe() throws Exception {
        if (token.isEmpty()) {
            String title = Constants.APP_TITLE + " - No Access Token";
            JOptionPane.showMessageDialog(null, "No Access Token", title, JOptionPane.ERROR_MESSAGE);
        }

        JSONObject json = Constants.GET_REQUEST(graphUrl + "/me?$select=displayName,mail", token);
        return new Me(json.getString("displayName"), json.getString("mail"));
    }

    public void getMessagesAsync(Runnable onPageLoaded, Runnable onFinish, int maxMessages) {
        this.messages.clear();
        SwingWorker<Void, Message> worker = new SwingWorker<>() {
            private int totalMessagesLoaded = 0;

            @Override
            protected Void doInBackground() {
                String endpoint = graphUrl + "/me/messages?$orderby=receivedDateTime%20desc&$select=subject,bodyPreview,from,receivedDateTime,webLink&$top=5000";

                try {
                    while (endpoint != null && totalMessagesLoaded < maxMessages) {
                        JSONObject json = Constants.GET_REQUEST(endpoint, token);

                        List<Message> pageMessages = parseMessagesFromJson(json);
                        for (Message msg : pageMessages) {
                            if (totalMessagesLoaded >= maxMessages) break;
                            publish(msg);
                            totalMessagesLoaded++;
                        }

                        endpoint = json.has("@odata.nextLink") ? json.getString("@odata.nextLink") : null;
                    }

                } catch (Exception e) {
                    Constants.SHOW_ERROR_DIALOG(null, e);
                }
                return null;
            }

            @Override
            protected void process(List<Message> chunks) {
                chunks.removeIf(Objects::isNull);
                messages.addAll(chunks);
                if (onPageLoaded != null) onPageLoaded.run();
            }

            @Override
            protected void done() {
                if (onFinish != null) onFinish.run();
            }
        };

        worker.execute();
    }

    private List<Message> parseMessagesFromJson(JSONObject json) {
        var messagesArray = json.getJSONArray("value");
        List<Message> result = new ArrayList<>();

        for (int i = 0; i < messagesArray.length(); i++) {
            JSONObject messageJson = messagesArray.getJSONObject(i);

            String id = messageJson.optString("id");
            String bodyPreview = cleanBodyPreview(messageJson.optString("bodyPreview"));
            String subject = messageJson.optString("subject");
            String webLink = messageJson.optString("webLink");
            String receivedDateTime = messageJson.optString("receivedDateTime");
            String from = Optional.ofNullable(messageJson.optJSONObject("from"))
                    .map(f -> f.optJSONObject("emailAddress"))
                    .map(e -> e.optString("address"))
                    .orElse("");


            result.add(new Message(id, subject, bodyPreview, from, receivedDateTime, webLink));
        }
        return result;
    }

    private String cleanBodyPreview(String bodyPreview) {
        String cleanedBody = bodyPreview.replaceAll("\\r\\n|\\r|\\n", " ")
                .replaceAll("\\s{2,}", " ")
                .replaceAll("(?i)\\bhttps?://\\S+", "");
        if (cleanedBody.length() > 200) {
            cleanedBody = cleanedBody.substring(0, 200) + "...";
        }
        return cleanedBody.trim();
    }

    public void setToken(String newToken) {
        if (!newToken.isEmpty() && !Objects.equals(this.token, newToken)) this.token = newToken;
    }

    public void deleteMessage(Message msg) throws Exception {
        try {
            Constants.DELETE_REQUEST(graphUrl + "/me/messages/" + msg.id(), token);
            this.messages.remove(msg);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<Message> messages() {return messages;}
}
