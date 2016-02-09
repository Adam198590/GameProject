package com.examle;

import com.server.classload.ClassLoadManager;
import com.server.classload.PacketFactory;
import com.server.classload.packetproto.PacketSet;

import java.net.*;
import java.io.*;

public class IOClient {
//    int serverPort = 20302;               //remote port
//    String address = "31.220.48.105";     //remote IP
    int serverPort = 1024;                  //local port
    String address = "127.0.0.1";           //local IP

    public void goString() throws Exception {
        InetAddress ipAddress = InetAddress.getByName(address);
        System.out.println("IP address " + address + " and port " + serverPort);
        Socket socket = new Socket(ipAddress, serverPort);
        System.out.println("Yes! I just got hold of the program.");

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        String line;
        System.out.println("Type in something and press enter...");

        while (true) {
            line = keyboard.readLine();
            System.out.println("Sending this line to the server...");
            out.writeUTF(line);
            out.flush();

            line = in.readUTF();
            System.out.println("It sent me this : " + line);
        }
    }

    public void goPacket() throws Exception {
        InetAddress ipAddress = InetAddress.getByName(address);
        System.out.println("IP address " + address + " and port " + serverPort);
        Socket socket = new Socket(ipAddress, serverPort);
        System.out.println("Yes! I just got hold of the program.");

        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        ClassLoadManager classLoadManager   = new ClassLoadManager();
        classLoadManager.load();

        PacketSet.NamePacket namePacket = PacketSet.NamePacket.newBuilder().setName("ASDSF").build();
        byte[] bytes = PacketFactory.getInstance().buildBasePacket(namePacket);

        out.write(bytes);

        int length = readInt(in);
        System.out.println("length - " + length);
        bytes = new byte[length];
        in.read(bytes, 0, bytes.length);

//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte buffer[] = new byte[1024];
//        for (int s; (s = in.read(buffer)) != -1;) {
//            baos.write(buffer, 0, s);
//        }
//        bytes = baos.toByteArray();

        PacketSet.BasePacket basePacket = PacketSet.BasePacket.parseFrom(bytes);
        namePacket = PacketSet.NamePacket.parseFrom(basePacket.getPacketData());
        System.out.println("namePacket - " + namePacket);

        in.close();
        out.close();
        socket.close();
    }

    public final int readInt(InputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    public static void main(String[] args) {
        try {
//            new IOClient().goString();
            new IOClient().goPacket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}