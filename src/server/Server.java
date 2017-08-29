package server;

import com.sun.media.sound.InvalidFormatException;
import controller.Controller;
import tools.DataConverter;
import tools.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Handler;

public class Server {

    public static final String REGISTER_CONTROLLER = "REGCON";

    private Handler handler;
    private Thread server;

    private Map<String, Controller> listController;

    public Server() {

    }

    public Server(Handler handler) {

        // Exceptions
        if (handler == null) throw new NullPointerException("Handler to set was NULL!");

        this.handler = handler;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Start Server
    ///////////////////////////////////////////////////////////////////////////
    public void start() {

        this.server = new Thread(() -> {

            ServerSocket serverSocket;

            try {

                serverSocket = new ServerSocket(ServerInformation.PORT);
                Socket socket = serverSocket.accept();
                socket.setKeepAlive(true);

                new Thread(() -> {

                    while (true) {
                        try {

                            String new_request = getRequestStringFromSocket(new BufferedReader(new InputStreamReader(socket.getInputStream())));
                            processRequest(new_request, socket);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.server.start();
        Log.print("Server started...");
    }

    public void stop() {

        this.server.interrupt();
    }

    private String getRequestStringFromSocket(BufferedReader bufferedReader) throws IOException {

        char[] length_in_char_array = new char[ServerInformation.DEFAULT_PACKAGE_LENGTH_LENGTH];
        bufferedReader.read(length_in_char_array, 0, ServerInformation.DEFAULT_PACKAGE_LENGTH_LENGTH);
        int expInput_length = Integer.parseInt(new String(length_in_char_array, 0, ServerInformation.DEFAULT_PACKAGE_LENGTH_LENGTH));
        String input = "";

        char[] buffer = new char[ServerInformation.BUFFER_SIZE];

        while (input.length() < expInput_length && input.length() != -1) {
            int cur_length = bufferedReader.read(buffer, 0, ServerInformation.BUFFER_SIZE);
            input += new String(buffer, 0, cur_length);
        }

        return input;
    }



    private void processRequest(String wholeRequest, Socket socket) throws Exception {

        // Split into tag and
        String tag, value;
        String[] values;
        if (wholeRequest.length() > ServerInformation.DEFAULT_INPUT_TAG_LENGTH) {

            tag = wholeRequest.substring(0, ServerInformation.DEFAULT_INPUT_TAG_LENGTH);
            value = wholeRequest.substring(ServerInformation.DEFAULT_INPUT_TAG_LENGTH, wholeRequest.length());
            values = DataConverter.splitValues(value);

        } else throw new InvalidFormatException("Invalid server request! Request was: " + wholeRequest);

        Log.print("SERVER: got request: " + tag + "   " + value);

        if (tag.equals(REGISTER_CONTROLLER)) {

            // Create new controller and add to list
            Controller controller = new Controller(value, socket);
            this.listController.put(controller.getId(), controller);

            System.out.println(values[0] + " " + values[1]);
            DataConverter.formatForOutput("REG", "CONFIRMED");
        }

    }

    private void send(Socket socket, String text) {

        if (socket != null) {
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


}
