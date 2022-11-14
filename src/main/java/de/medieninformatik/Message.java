package de.medieninformatik;

import javafx.scene.paint.Color;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class Message implements Serializable {
    private String text;
    private String alias;
    private String colorUni;
    private InetAddress sender;

    final static long serialVersionUID = 123456789L;

    public Message(String text, InetAddress sender) {
        this.text = text;
        this.sender = sender;
        try {
            this.alias = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Random r = new Random();
        int n = r.nextInt(1,6);
        switch (n){
            case 1: this.colorUni = "\u001B[0m"; break;
            case 2: this.colorUni = "\u001B[31m";break;
            case 3: this.colorUni = "\u001B[32m";break;
            case 4: this.colorUni = "\u001B[33m";break;
            case 5: this.colorUni = "\u001B[34m";break;
            case 6: this.colorUni = "\u001B[35m";break;
        }
    }

    public String getText() {
        return text;
    }

    public String getAlias() {
        return this.alias;
    }

    @Override
    public String toString() {
        return colorUni+alias+": "+text;
    }

    public InetAddress getSender() {
        return sender;
    }
}
