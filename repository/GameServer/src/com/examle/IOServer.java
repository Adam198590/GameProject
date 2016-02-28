package com.examle;

import java.net.*;
import java.io.*;

public class IOServer {
    int port                = 1024;
    Socket socket           = null;
    DataInputStream in      = null;
    DataOutputStream out    = null;

    public void start() {
        try {
            ServerSocket ss = new ServerSocket(port);

            System.out.println("Waiting for a client...");
            socket = ss.accept();
            System.out.println("\nGot a client\n");

            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            String line;
            while (true) {
                System.out.println("TRY READ!!!");
                line = in.readUTF();

                System.out.println("sent me this line : " + line);
                System.out.println("sending it back...");

                out.writeUTF(line.toUpperCase());
                out.flush();

                System.out.println("Waiting for the next line...");
            }
        } catch (IOException x) {
            System.out.println("connect lost...\n");
            x.printStackTrace();

            try {
                in.close();
                out.close();
                socket.close();

                System.out.println("connect was closed...");
            } catch (IOException ex) {
                System.out.println("CRASH CLOSE!!!");
                ex.printStackTrace();
            } catch (Exception ex) {
                System.out.println("Unknown Exception...");
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new IOServer().start();
    }
}