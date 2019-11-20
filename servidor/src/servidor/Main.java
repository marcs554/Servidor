package servidor;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        try {
            servidor.Server();
            servidor.startServer();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}