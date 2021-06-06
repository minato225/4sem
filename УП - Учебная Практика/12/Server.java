import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server extends UnicastRemoteObject implements IWorkplace.IChatServer {
    private static final List<IWorkplace.IChatClient> clients = new CopyOnWriteArrayList<>();
    private static final Map<IWorkplace.IChatClient, Boolean> areWorking = new ConcurrentHashMap<>();
    static final String SERVER_NAME = "Server";
    private final static int PORT = 8080;

    public Server() throws RemoteException, MalformedURLException, InterruptedException {
        LocateRegistry.createRegistry(PORT);
        System.setProperty("java.rmi.server.hostname", "localhost");
        Naming.rebind("rmi://" + IWorkplace.PREFIX + ":" + PORT + "/" + SERVER_NAME, this);
        System.out.println("Server started");
        var thread = new Thread(() -> {
            try {
                checkClients();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
        thread.join();
    }

    private void checkClients() throws IOException, InterruptedException {
        while (true) {
            for (var client : clients) client.receiveNewMessage("Введите любое сообщение");

            Thread.sleep(5000);
            for (int i = 0; i < clients.size(); ) {
                var client = clients.get(i);
                if (!areWorking.get(client)) {
                    client.receiveNewMessage("Вы отключены от сервера");
                    disconnect(client);
                } else {
                    areWorking.put(client, false);
                    i++;
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Server();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appendLog(String msg) {
        try {
            if (Files.exists(Path.of("log.txt"))) {
                Files.write(Paths.get("log.txt"), msg.getBytes(), StandardOpenOption.APPEND);
            } else {
                Files.write(Paths.get("log.txt"), msg.getBytes(), StandardOpenOption.CREATE_NEW);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void sendMessage(IWorkplace.IChatClient sender, String message) throws RemoteException {
        var msg ="\nlog: Message by " + sender.getName() + " received\n";
        System.out.println(msg);
        appendLog(msg);
        areWorking.put(sender, true);
    }


    @Override
    public synchronized void connect(IWorkplace.IChatClient client) throws RemoteException {
        var msg = "\nlog: New Client connected: " + client.getName() + "\n";
        System.out.println(msg);
        appendLog(msg);
        clients.add(client);
        areWorking.put(client, true);
    }

    @Override
    public synchronized void disconnect(IWorkplace.IChatClient client) throws RemoteException {
        var msg = "\nlog: Client disconnected: " + client.getName() + "\n";
        System.out.println(msg);
        appendLog(msg);
        areWorking.remove(client);
        clients.remove(client);
    }
}