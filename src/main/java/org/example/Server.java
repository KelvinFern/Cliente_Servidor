package org.example;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 12345;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor aguardando conexão...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Cliente conectado: " + clientSocket);

                    InputStream inputStream = clientSocket.getInputStream();
                    DataInputStream dataInputStream = new DataInputStream(inputStream);

                    String fileName = dataInputStream.readUTF();
                    long fileSize = dataInputStream.readLong();

                    FileOutputStream fileOutputStream = new FileOutputStream(fileName);
                    byte[] buffer = new byte[1024];

                    while (fileSize > 0 && (inputStream.read(buffer, 0, (int)Math.min(buffer.length, fileSize))) != -1) {
                        fileOutputStream.write(buffer);
                        fileSize -= buffer.length;
                    }

                    fileOutputStream.close();
                    System.out.println("Imagem recebida e salva em: " + fileName);

                    DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                    dataOutputStream.writeUTF(fileName);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}