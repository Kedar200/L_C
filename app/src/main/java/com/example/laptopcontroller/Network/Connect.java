package com.example.laptopcontroller.Network;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connect implements Runnable {
    private static Socket socket;
    private String ip;
    private int SERVER_PORT;
    private ConnectListener listener;

    public void setListener(ConnectListener listener) {
        this.listener = listener;
    }

    public Connect(String ip, int SERVER_PORT, ConnectListener listener) {
        this.ip = ip;
        this.SERVER_PORT = SERVER_PORT;
        this.listener = listener;
    }

    public void run() {
        try {
            socket = new Socket(ip, SERVER_PORT);
            socket.setTcpNoDelay(true);
            listener.onConnected();

            // Start the message listening thread
            Thread messageThread = new Thread(new MessageListener());
            messageThread.start();


            while (!Thread.currentThread().isInterrupted()) {
            }
        } catch (IOException e) {
            e.printStackTrace();
            listener.onDisconnected();
        }
    }

    public interface ConnectListener {
        void onConnected();

        void onDisconnected();

        void onMessageReceived(String message);
    }

    public class MessageListener implements Runnable {
        private BufferedReader input;

        @Override
        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while (true) {
                    // Notify the listener about the received message
                    message = input.readLine();
                    listener.onMessageReceived(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                listener.onDisconnected();
            } finally {
                // Close the input stream and socket when done

                try {
                    if (input != null)
                        input.close();
                    if (socket != null)
                        socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public static class Sendmsg implements Runnable {
        private String type = "Initial";
        private JSONObject data;
        private PrintWriter output;

        public Sendmsg(String type, JSONObject data) {
            this.type = type;
            this.data = data;
        }

        @Override
        public void run() {
            JSONObject object = new JSONObject();

            try {
                output = new PrintWriter(socket.getOutputStream());
                object.put("type", type);
                object.put("data", data);
                output.write(object.toString() + "\n");
                output.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}