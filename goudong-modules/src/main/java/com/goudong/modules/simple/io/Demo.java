package com.goudong.modules.simple.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/4/19 11:34
 */
public class Demo {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(8080);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 8080.");
            System.exit(1);
        }

        Socket clientSocket = null;
        try {
            System.out.println("Waiting for connection.....");
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        System.out.println("Connection successful");
        System.out.println("Waiting for input.....");

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
                true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            System.out.println("Server: " + inputLine);
            out.println(inputLine);

            if (inputLine.equals("Bye."))
                break;
        }

        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}
