package de.medieninformatik;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientFX extends Application {
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private TextArea chat;

    @Override
    public void start(Stage stage) throws Exception {
        VBox box = new VBox();
        box.setPrefSize(1000, 750);
        final TextField text = new TextField();
        final Label label = new Label();
        chat = new TextArea();
        chat.setPrefRowCount(700);
        box.getChildren().addAll(text, label, chat);
        stage.setScene(new Scene(box));
        stage.show();

        text.setOnAction(event -> {
            String input = text.getText();
            text.clear();
            System.out.println(input);
            try {
                oos.writeObject(new Message(input, InetAddress.getLocalHost()));
            } catch (IOException e) {
                System.err.println("Error while sending message.");
                System.exit(0);
            }
        });

        connectToServer("localhost", 6000);
    }

    private void connectToServer(String server, int port) {
        Thread t = new Thread(() -> {
            try (Socket socket = new Socket(server, port)) {
                //Streams
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.flush();

                ois = new ObjectInputStream(socket.getInputStream());
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
                                        String msg = message + "\n";
                                        chat.setText(chat.getText() + msg);
                                    } catch (IOException e) {
                                        System.err.println("Host disconnected.");
                                        System.exit(0);
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
                while (true) ;
            } catch (SocketException | UnknownHostException e) {
                System.err.println("Host disconnected.");
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.err.println("Received wrong input.");
            }
            System.err.println("No Host found.");
        }
        );
        t.start();
    }

    public static void main(String[] args) {
        Application.launch(ClientFX.class, args);
    }
}
