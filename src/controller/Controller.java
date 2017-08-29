package controller;

import tools.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class Controller {

    protected String id;

    protected List<String> abilitys;
    protected Socket socket;


    public Controller(String id, Socket socket) {

        // Exceptions
        if (id == null) throw new NullPointerException("ID was NULL!");
        if (socket == null) throw new NullPointerException("Controller-socket was NULL!");
        if (id.isEmpty()) throw new IllegalArgumentException("ID was empty!");

        this.id = id;
        this.socket = socket;
    }

    private void send(String text) {

        if (this.socket != null) {
            try {

                if (socket.isConnected()) {
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(text.getBytes());
                    outputStream.flush();
                    Log.print("Sending: " + text);

                } else {

                    Log.print("Could not send Message, No connection with socket!");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public String getId() {

        return this.id;
    }

    public Socket getSocket() {

        return this.socket;
    }
}
