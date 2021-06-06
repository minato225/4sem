import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IWorkplace {
    String PREFIX = "localhost";

    interface IChatClient extends Remote {
        String getName() throws RemoteException;
        void receiveNewMessage(String message) throws IOException;
    }

    interface IChatServer extends Remote {
        void sendMessage(IChatClient client, String message) throws RemoteException;
        void connect(IChatClient client) throws  RemoteException;
        void disconnect(IChatClient client) throws  RemoteException;
    }
}
