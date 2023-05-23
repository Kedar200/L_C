package com.example.laptopcontroller;
import android.view.View;

import java.io.*;
import java.net.Socket;

public class So {
    static BufferedReader in;
    static BufferedWriter out;
    public static void connect(View v) throws IOException {
            Socket s;
            System.out.println("hello");
            s = new Socket("192.168.1.65",1989);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            out.write("hello");
    }
//    public static void send(String a) throws IOException {
//        out.write(a);
//    }

}
