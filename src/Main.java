import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        // Запуск сервера і клієнта в окремих потоках
        Thread serverThread = new Thread(() -> runServer());
        Thread clientThread = new Thread(() -> runClient());

        serverThread.start();
        clientThread.start();
    }

    private static void runServer() {
        try {
            // Створення серверного сокету на порту 7777
            ServerSocket serverSocket = new ServerSocket(7777);
            System.out.println("Сервер запущено. Очікування підключення...");

            // Очікування підключення клієнта
            Socket clientSocket = serverSocket.accept();
            System.out.println("Клієнт підключено.");

            // Отримання потоків вводу-виводу
            InputStream inputStream = clientSocket.getInputStream();
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            // Читання повідомлень від клієнта
            for (int i = 0; i < 100; i++) {
                int messageLength = dataInputStream.readInt(); // Читання довжини повідомлення
                byte[] messageBytes = new byte[messageLength];
                dataInputStream.readFully(messageBytes); // Читання самого повідомлення

                // Обробка отриманого повідомлення
                String receivedMessage = new String(messageBytes);
                System.out.println("Сервер отримав повідомлення: " + receivedMessage);
            }

            // Закриття з'єднання
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runClient() {
        try {
            // Затримка перед спробою підключення до сервера
            Thread.sleep(1000);

            // Підключення до сервера за допомогою сокету
            Socket socket = new Socket("localhost", 7777);
            System.out.println("Підключено до сервера.");

            // Отримання потоків вводу-виводу
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            // Надсилання 100 повідомлень серверу
            for (int i = 0; i < 100; i++) {
                String message = "Повідомлення " + (i + 1);
                byte[] messageBytes = message.getBytes();
                dataOutputStream.writeInt(messageBytes.length); // Відправка довжини повідомлення
                dataOutputStream.write(messageBytes); // Відправка самого повідомлення
                dataOutputStream.flush();
                System.out.println("Клієнт відправив повідомлення: " + message);

                // Затримка між відправкою повідомлень
                Thread.sleep(100);
            }

            // Закриття з'єднання
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
