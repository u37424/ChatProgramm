package de.medieninformatik;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientFX extends Application {
    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        connectToServer("localhost", 6000);
    }

    private void connectToServer(String server, int port) {
        try (Socket socket = new Socket(server, port)) {
            //Streams
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            boolean suc = (Boolean) ois.readObject();
            if (!suc) {
                System.err.println("Host denied access.");
                return;
            }
            System.out.println("Connected to " + socket.getInetAddress().getHostName());

            Service<Void> service = new Service<>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<>() {
                        @Override
                        protected Void call() {
                            do {
                                try {
                                    Message message = (Message) ois.readObject();
                                    System.out.println(message);
                                } catch (IOException e) {
                                    System.err.println("Host disconnected.");
                                    break;
                                } catch (ClassNotFoundException e) {
                                    System.err.println("Unknown Input received.");
                                    break;
                                }
                            } while (true);
                            return null;
                        }
                    };
                }
            };

            service.start();

            //Eingabe für Client
            Scanner scanner = new Scanner(System.in);

            //Schreiben
            do {
                Message message = new Message(scanner.nextLine());
                oos.writeObject(message);
            } while (true);
        } catch (SocketException | UnknownHostException e) {
            System.err.println("Host disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Received wrong input.");
        }
    }
}
