package Server_TCP_Analysis;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by dimas on 24.04.16.
 */

public class ServerRun{

    private ServerSocket serverSocket = null;
    private boolean isRun = true;

    public ServerRun(){

        try {

            this.serverSocket = new ServerSocket(8998);

            while (this.isRun){

                Socket incomingSocket = this.serverSocket.accept();
                Runnable runnable = new TCPServer(incomingSocket);
                Thread t = new Thread(runnable);
                t.start();

            }

        }catch (Exception ex){

            isRunServer(false);
            ex.printStackTrace();

        }
    }

    public void isRunServer(boolean flag){

        this.isRun = false;

    }

}