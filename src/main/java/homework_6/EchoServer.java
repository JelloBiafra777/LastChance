package homework_6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {

    private static Socket socket;
    private static DataInputStream in;
    private static DataOutputStream out;

    static Thread thread1;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(8189)) {

            System.out.println("Server is running. Waiting for client response... ");
            socket = serverSocket.accept();
            System.out.println("Client is connected!");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());


            thread1 = new Thread(() -> {
                try {
                    while (true) {

                        String str = in.readUTF();

                        if (str.equals("/end")) {
                            SendMessage("/end");
                            break;
                        }
                        System.out.println("Message from client " + str);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    CloseConnection();
                }
            });

            thread1.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Thread enterTextThread = new Thread(() -> {

            while (true) {
                Scanner scanner = new Scanner(System.in);
                String a = scanner.nextLine();
                SendMessage(a);
                if (a.equals("/end")) {
                    break;
                }
            }

        });

        enterTextThread.setDaemon(true);
        enterTextThread.start();

    }



    private static void SendMessage(String message) {

        try {
            out.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void CloseConnection() {

        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
