package servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {
    private final int PORT = 9999;
    private ServerSocket serverSocket;
    private Socket socket;
    private DataOutputStream streamSender;
    private DataInputStream streamReceiver;
    private BufferedWriter bufferSender;
    ArrayList<Tortugas> listaTortugas = new ArrayList();
    

   public void Server() throws IOException {
        serverSocket = new ServerSocket(PORT);
        socket = new Socket();
    }

    //
    public void startServer() throws IOException {
        String msg;
        /*
        * La variable breaker se encarga de decidir si el bucle while continua repitiendose o no
        * true = el bucle repite el ciclo y el servidor socket sigue en linea
        * false = sale del bucle para proceder a cerrar el socket y finalizar el programa
        * */
        boolean breaker = true;

        //System.out.println("Iniciando servidor");
        try {
            while (breaker) {
                //El servidor se pone a la espera a que un cliente se conecte a el
                System.out.println("Esperando conexión...");
                socket = serverSocket.accept();

                //Se abre el canal de entrada y salida de datos
                streamReceiver = new DataInputStream(socket.getInputStream());
                streamSender = new DataOutputStream(socket.getOutputStream());

                /*
                 * Envia un mensaje al cliente confirmandole que la conexión al  servidor fue
                 * exitosa
                 */
                streamSender.writeUTF("Petición recibida y aceptada");

                //El cliente enviará una respuesta tipo Integer a la función "menu(int i);"
                breaker = menu(Integer.parseInt(streamReceiver.readUTF()));

                streamReceiver.close();
                streamSender.close();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            //Se procede a cerrar el socket
            System.out.println("Fin de la conexión");
            serverSocket.close();
        }
    }

    /*
    * En la función "menu" se llevarán a cabo las opciones que elija el cliente:
    * 1º Añadir una tortuga
    * 2º Borrar una tortuga
    * 3º Listar todas las tortugas almacenadas
    * 4º Inicia la carrera
    * 5º Apaga el servidor
    * La funcion "menu" retornará un valor booleano que será almacenada dentro de la variable breaker
    * de la función "StartServer"
    */
    public boolean menu(int op) throws IOException, InterruptedException {
        boolean breaker = true;
        Tortugas aux = new Tortugas();
        Carrera[] carrera = new Carrera[listaTortugas.size()];

        switch (op) {
            case 1:
                System.out.println("Añadiendo tortuga");
                //El cliente enviará el nombre de la tortuga
                aux.setNombre(streamReceiver.readUTF());

                //El cliente enviará el dorsal de la tortuga
                aux.setDorsal(Integer.parseInt(streamReceiver.readUTF()));

                //La información de la tortuga se almacenará en un arraylist "listatortugas"
                listaTortugas.add(aux);
                System.out.println("Tortuga de nombre:" + aux.getNombre() + " y dorsal:" + aux.getDorsal() +
                        " creado correctamente");
                streamSender.writeUTF("Tortuga de nombre:" + aux.getNombre() + " y dorsal:" + aux.getDorsal() +
                        " creado correctamente");
                break;

            case 2:
                //Borra la tortuga selecionada por el cliente en el arraylist
                int indexDel = Integer.parseInt(streamReceiver.readUTF());
                listaTortugas.remove(indexDel - 1);
                System.out.println("Tortuga eliminada, enviando mensaje al cliente");
                streamSender.writeUTF("La tortuga ha sido eliminada");;
                break;

            case 3:
                /*
                * El servidor envia al cliente un listado completo de las tortugas almacenadas
                * en el arrayList mediante un toString()
                * */
                System.out.println("Enviando tortugas al cliente");

                /*
                * Considero que es más efectivo el uso de un BufferedReader para enviar al cliente la lista
                * completa de tortugas ya que si se usa un DataOutputStream puede llevar a errores
                */
                bufferSender = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                for (int i = 0; i < listaTortugas.size(); i++) {
                    bufferSender.write((i + 1) + ". " + listaTortugas.get(i).toString());
                }
                bufferSender.flush();
                bufferSender.close();
                break;

            case 4:
            	int posicion = 1;
            	bufferSender = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                for(int i = 0; i < listaTortugas.size(); i++) {
                    Thread hilo = new Thread(carrera[i]); 
                    hilo.start();
                    if(!hilo.isAlive()) {
                    	bufferSender.write(posicion + "º " + listaTortugas.toString());
                    	posicion++;
                    }
                }
                bufferSender.flush();
                bufferSender.close();
                //streamSender.writeUTF("El hilo se inició correctamente");
                break;

            case 5:
                /*
                * Si el cliente decide desconectar el servidor la variable booleana "breaker"
                * se modificará su valor true a false
                */
                System.out.println("Saliendo...");
                breaker = false;
                break;

            default:
                break;
        }

        return breaker;
    }
}
