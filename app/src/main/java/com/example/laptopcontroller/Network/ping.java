package com.example.laptopcontroller.Network;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ping implements Runnable {
    private String ipAddress;
    private int port;
    private PortPingListener listener;

    public ping(String ipAddress, int port, PortPingListener listener) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.listener = listener;
    }

    @Override
    public void run() {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ipAddress, port), 2000); // Timeout set to 0.5 seconds
            listener.onPortPingResult(true); // Port is open

            socket.close();
        } catch (IOException e) {
            listener.onPortPingResult(false); // Port is closed or unreachable
        }


    }

    public interface PortPingListener {
        void onPortPingResult(boolean isPortOpen);
    }
}
