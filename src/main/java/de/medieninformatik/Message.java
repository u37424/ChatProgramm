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

    final static long serialVersionUID = 123456789L;

    public Message(String text) {
        this.text = text;
        try {
            this.alias = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Random r = new Random();
        int n = r.nextInt(1,7);
        switch (n){
            case 1: this.colorUni = "\u001B[41m"; break;
            case 2: this.colorUni = "\u001B[42m";break;
            case 3: this.colorUni = "\u001B[43m";break;
            case 4: this.colorUni = "\u001B[44m";break;
            case 5: this.colorUni = "\u001B[45m";break;
            case 6: this.colorUni = "\u001B[46m";break;
            case 7: this.colorUni = "\u001B[47m";break;
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
}
