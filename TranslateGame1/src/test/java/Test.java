import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class Test {
  public static void main(String[] args) {

    JFrame frame = new JFrame("Exemplo de JTree");

    // Criando nós da árvore
    DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("Animais");
    DefaultMutableTreeNode mamiferos = new DefaultMutableTreeNode("Mamíferos");
    DefaultMutableTreeNode aves = new DefaultMutableTreeNode("Aves");

    DefaultMutableTreeNode cachorro = new DefaultMutableTreeNode("Cachorro");
    DefaultMutableTreeNode gato = new DefaultMutableTreeNode("Gato");

    DefaultMutableTreeNode papagaio = new DefaultMutableTreeNode("Papagaio");
    DefaultMutableTreeNode aguia = new DefaultMutableTreeNode("Águia");

    // Adicionando nós
    raiz.add(mamiferos);
    raiz.add(aves);
    mamiferos.add(cachorro);
    mamiferos.add(gato);
    aves.add(papagaio);
    aves.add(aguia);

    // Criando a árvore
    JTree tree = new JTree(raiz);
    JScrollPane scrollPane = new JScrollPane(tree);

    tree.setRootVisible(false);

    // Configuração da janela
    frame.add(scrollPane);
    frame.setSize(300, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}
