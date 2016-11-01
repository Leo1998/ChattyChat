package chattychat;

import chattychat.ui.ServerUI;
import clientserver.Server;
import crypto.Crypto;
import crypto.KeyGenerator;
import crypto.KeyPair;

import java.math.BigInteger;
import java.util.ArrayList;

public class ChatServer extends Server {

    private ArrayList<ClientInfo> clients;

    private KeyPair keyPair;

    private ServerUI ui;

    public ChatServer(int pPort, ServerUI ui) {
        super(pPort);

        this.ui = ui;

        this.clients = new ArrayList<>();

        this.keyPair = KeyGenerator.generateKeyPair(512);
    }

    @Override
    public void processNewConnection(String pClientIP, int pClientPort) {
        ClientInfo c = new ClientInfo(pClientIP, pClientPort, "n/a");
        clients.add(c);

        ui.printMessage("Client connected: " + c);

        KeyPair.PublicKey publicKey = keyPair.publicKey;

        this.send(pClientIP, pClientPort, "request#name#key#" + publicKey.n + "#" + publicKey.e);
    }

    @Override
    public void processMessage(String pClientIP, int pClientPort, String pMessage) {
        ClientInfo client = getClientInfo(pClientIP, pClientPort);

        String[] tokens = pMessage.split("#");

        if (tokens[0].equals("input")) {
            if (tokens[1].equals("name")) {
                String name = tokens[2];
                client.setName(name);

                BigInteger n = new BigInteger(tokens[3]);
                BigInteger e = new BigInteger(tokens[4]);
                client.setPublicKey(new KeyPair.PublicKey(n, e));

                onClientUpdate(client);
            }
        } else if (tokens[0].equals("request")) {
            if (tokens[1].equals("clients")) {
                sendClientList(client);
            }
        } else if (tokens[0].equals("send")) {
            String msg = Crypto.decrypt(tokens[1], keyPair.privateKey);

            ui.printMessage(client.getName() + ": " + msg);

            for (int i = 0; i < clients.size(); i++) {
                ClientInfo c = clients.get(i);

                KeyPair.PublicKey clientKey = c.getPublicKey();

                if (c != client) {
                    this.send(c.getIpAddress(), c.getPort(), "sendfrom#" + client.getName() + "#" + Crypto.encrypt(msg, clientKey));
                }
            }
        }
    }

    private void sendClientList(ClientInfo client) {
        StringBuilder builder = new StringBuilder();
        builder.append("input#clients");

        for (int i = 0; i < clients.size(); i++) {
            ClientInfo c = clients.get(i);
            if (!c.equals(client)) {
                builder.append("#");
                builder.append(c.getName() + "#" + c.getPublicKey().n + "#" + c.getPublicKey().e);
            }
        }

        this.send(client.getIpAddress(), client.getPort(), builder.toString());
    }

    private void onClientUpdate(ClientInfo client) {
        for (int i = 0; i < clients.size(); i++) {
            ClientInfo c = clients.get(i);
            if (!c.equals(client)) {
                sendClientList(c);
            }
        }
    }

    @Override
    public void processClosingConnection(String pClientIP, int pClientPort) {
        for (int i = 0; i < clients.size(); i++) {
            ClientInfo c = clients.get(i);
            if (c.getIpAddress().equals(pClientIP) && c.getPort() == pClientPort) {
                clients.remove(c);

                onClientUpdate(c);

                ui.printMessage("Client disconnected: " + c);
            }
        }
    }

    public ClientInfo getClientInfo(String pClientIP, int pClientPort) {
        for (int i = 0; i < clients.size(); i++) {
            ClientInfo c = clients.get(i);
            if (c.getIpAddress().equals(pClientIP) && c.getPort() == pClientPort) {
                return c;
            }
        }

        return null;
    }
}
