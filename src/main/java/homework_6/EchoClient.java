package homework_6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    private Thread thread;

    public static void main(String[] args) {
        new EchoClient().start();
    }

    private void start() {

        try {
            openConnection();
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

    private void SendMessage(String message) {

        try {
            out.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openConnection() throws IOException {

        socket = new Socket("127.0.0.1", 8189);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        thread = new Thread(() -> {
            try {
                while (true) {

                    String message = in.readUTF();

                    if (message.equals("/end")) {
                        SendMessage("/end");
                        break;
                    }
                    System.out.println("Message from server " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                CloseConnection();
            }
        });

        thread.start();

    }

    private void CloseConnection() {


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

            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }
}

