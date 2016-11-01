package chattychat;

import chattychat.ui.ClientUI;
import chattychat.ui.ServerUI;
import crypto.Crypto;
import crypto.KeyGenerator;
import crypto.KeyPair;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        //testEncryption();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {}

        int selection = -1;

        while(selection != 0 && selection != 1) {
            selection = JOptionPane.showOptionDialog(null, "Launch Client or Server??", "Frage:", 0, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Server", "Client"}, "Client");
        }

        if (selection == 0) {
            int port = 8192;

            final ServerUI ui = new ServerUI();;

            ChatServer server = new ChatServer(port, ui);
        } else {
            String ipAddress = JOptionPane.showInputDialog("Enter ipAddress", "localhost");
            int port = 8192;

            ClientUI ui = new ClientUI();

            ChatClient client = new ChatClient(ipAddress, port);
            ui.setClient(client);
            client.setUi(ui);
        }
    }

    private static void testEncryption() {
        KeyPair keyPair = KeyGenerator.generateKeyPair(128);

        String text = "Hallo Test!§$%&/()=";

        String encrypted = Crypto.encrypt(text, keyPair.publicKey);
        System.out.println(encrypted);

        String original = Crypto.decrypt(encrypted, keyPair.privateKey);
        System.out.println(original);
    }

}
