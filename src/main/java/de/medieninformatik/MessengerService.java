package de.medieninformatik;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

public class MessengerService implements Runnable {
    private Socket socket;
    private HashMap<String, ObjectOutputStream> clients;
    private String user;

    public MessengerService(Socket socket) {
        this.socket = socket;
        this.clients = Server.getClients();
        this.user = socket.getInetAddress().getHostAddress();
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        //Verbinden
        System.out.println("Verbunden mit " + user);


        try {
            //Streams
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream is = new ObjectInputStream(socket.getInputStream());

            //Anmelden
            connect(user, oos);

            //Annehmen und weiterleiten
            do {
                Message message = (Message) is.readObject();
                if (message == null) {
                    socket.close();
                    System.err.println("User " + user + " disconnected from your channel.");
                    if (disconnect(user))
                        System.err.println(user + " abgemeldet.");
                    break;
                }
                System.out.println(message);
                clients.forEach((u, o) -> {
                    try {
                        if (!o.equals(oos)) o.writeObject(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } while (true);


        } catch (IOException e) {
            System.err.println("User " + user + " disconnected from your Channel.");
            if (disconnect(user))
                System.err.println(user + " abgemeldet.");
        } catch (ClassNotFoundException e) {
            System.err.println("Received wrong input.");
        }
    }

    private boolean connect(String user, ObjectOutputStream oos) {
        if (clients.containsKey(user)) return false;
        clients.put(user, oos);
        try {
            oos.writeObject(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean disconnect(String user) {
        if (clients.containsKey(user)) {
            clients.remove(user);
            return true;
        }
        return false;
    }
}
