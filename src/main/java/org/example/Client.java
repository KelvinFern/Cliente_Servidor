package org.example;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(serverAddress, port)) {
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            String fileName = "image_received.jpg";
            File file = new File(fileName);

            dataOutputStream.writeUTF(fileName);
            dataOutputStream.writeLong(file.length());

            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            fileInputStream.close();
            outputStream.flush();

            InputStream inputStream = socket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            String serverResponse = dataInputStream.readUTF();
            System.out.println("Servidor respondeu: " + serverResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}