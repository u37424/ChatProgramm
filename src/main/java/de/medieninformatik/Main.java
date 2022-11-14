package de.medieninformatik;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        (new Server(6000)).start();
        //Application.launch(ClientFX.class, args);
    }
}
