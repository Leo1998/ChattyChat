package chattychat;

import chattychat.ui.ClientUI;
import clientserver.Client;
import clientserver.List;
import crypto.Crypto;
import crypto.KeyGenerator;
import crypto.KeyPair;

import javax.swing.*;
import java.math.BigInteger;
import java.util.ArrayList;

public class ChatClient extends Client {

    private ArrayList<ClientInfo> clients;

    private KeyPair keyPair;
    private KeyPair.PublicKey serverKey;

    private ClientUI ui;

    public ChatClient(String pServerIP, int pServerPort) {
        super(pServerIP, pServerPort);

        this.clients = new ArrayList<>();

        this.keyPair = KeyGenerator.generateKeyPair(512);
    }

    public ClientUI getUi() {
        return ui;
    }

    public void setUi(ClientUI ui) {
        this.ui = ui;
    }

    public void sendMessage(String msg) {
        this.send("send#" + Crypto.encrypt(msg, serverKey));
    }

    @Override
    public void processMessage(String pMessage) {
        String[] tokens = pMessage.split("#");

        if (tokens[0].equals("request")) {
            if (tokens[1].equals("name")) {
                if (tokens[2].equals("key")) {
                    BigInteger n = new BigInteger(tokens[3]);
                    BigInteger e = new BigInteger(tokens[4]);
                    this.serverKey = new KeyPair.PublicKey(n, e);

                    String name = JOptionPane.showInputDialog("Please enter your Name");
                    KeyPair.PublicKey publicKey = keyPair.publicKey;

                    this.send("input#name#" + name + "#" + publicKey.n + "#" + publicKey.e);

                    this.send("request#clients");
                }
            }
        } else if (tokens[0].equals("input")) {
            if(tokens[1].equals("clients")) {
                clients.clear();

                for (int i = 2; i < tokens.length; i += 3) {
                    ClientInfo c = new ClientInfo("n/a", -1, tokens[i]);
                    c.setPublicKey(new KeyPair.PublicKey(new BigInteger(tokens[i + 1]), new BigInteger(tokens[i + 2])));

                    System.out.println(c);

                    clients.add(c);
                }
            }
        } else if (tokens[0].equals("sendfrom")) {
            String from = tokens[1];
            String msg = Crypto.decrypt(tokens[2], keyPair.privateKey);

            ui.printMessage(from + ": " + msg);
        }
    }
}
