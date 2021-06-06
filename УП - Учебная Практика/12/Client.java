import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements IWorkplace.IChatClient, Serializable {
    transient BufferedReader in;
    transient final IWorkplace.IChatServer server;
    transient static int nextPort = 8080;
    static final String CLIENT_NAME = "Client";
    int port;

    String name;

    @Override
    public String getName() {
        return name;
    }

    public Client(InputStream inputStream) throws IOException, NotBoundException {
        in = new BufferedReader(new InputStreamReader(inputStream));
        String hostname = "localhost";
        String serverName = "Server";

        boolean success = false;
        while (!success) {
            port = nextPort;
            try {
                LocateRegistry.createRegistry(nextPort);
                Naming.rebind("rmi://" + IWorkplace.PREFIX + ":" + port + "/" + CLIENT_NAME + port, this);
            } catch (ExportException e) {
                nextPort++;
            }
            success = true;
        }


        System.out.println("Введите свой никнейм");
        this.name = in.readLine();

        server = (IWorkplace.IChatServer) Naming.lookup("rmi://" + hostname + ":" + port + "/" + serverName);
        server.connect(this);
        System.out.println("Подключён к серверу");
    }

    public static void main(String[] args) {
        try {
            new Client(System.in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveNewMessage(String message) {
        System.out.println(message);
        new Thread( () -> {
            try {
                var cmd = in.readLine();
                server.sendMessage(this, cmd);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
