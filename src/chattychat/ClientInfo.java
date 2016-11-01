package chattychat;

import crypto.KeyPair;

public class ClientInfo {

    private String ipAddress;
    private int port;
    private String name;
    private KeyPair.PublicKey publicKey;

    public ClientInfo(String ipAddress, int port, String name) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public KeyPair.PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(KeyPair.PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "ClientInfo{" +
                "ipAddress='" + ipAddress + '\'' +
                ", port=" + port + '\'' +
                "name='" + name +
                '}';
    }
}
