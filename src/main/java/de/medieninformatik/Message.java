package de.medieninformatik;

import javafx.scene.paint.Color;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class Message implements Serializable {
    private String text;
    private String alias;
    private Color colorUni;
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
            case 1: this.colorUni = Color.RED;
            case 2: this.colorUni = Color.RED;
            case 3: this.colorUni = Color.RED;
            case 4: this.colorUni = Color.RED;
            case 5: this.colorUni = Color.RED;
            case 6: this.colorUni = Color.RED;
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
        return alias+": "+text;
    }

    public InetAddress getSender() {
        return sender;
    }

    public Color getColorUni() {
        return colorUni;
    }
}
