package de.medieninformatik;

import javafx.scene.paint.Color;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Message implements Serializable {
    private String text;
    private String alias;
    private  Color color;

    final static long serialVersionUID = 123456789L;

    public Message(String text) {
        this.text = text;
        try {
            this.alias = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
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
}
