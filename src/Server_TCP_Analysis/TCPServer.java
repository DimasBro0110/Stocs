package Server_TCP_Analysis;

/**
 * Created by dimas on 24.04.16.
 */

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


public class TCPServer implements Runnable{

    private Socket clientSocket = null;
    private boolean isRun = true;

    public TCPServer(Socket socket){

        try{
            this.clientSocket = socket;
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public void isRunSetter(boolean flag){

        this.isRun = flag;

    }

    @Override
    public void run() {

        try {

            while (isRun) {

                InputStream inputStream = this.clientSocket.getInputStream();
                OutputStream outputStream = this.clientSocket.getOutputStream();
                Scanner scanner = new Scanner(inputStream);
                PrintWriter printWriter = new PrintWriter(outputStream, true);
                printWriter.println("go");
                boolean isDone = false;

                while(!isDone && scanner.hasNextLine()){

                    String line = scanner.nextLine();
                    printWriter.println("From server: " + line);
                    printWriter.flush();

                    if (line.trim().equals("status 0")) {
                        isDone = true;
                        this.clientSocket.shutdownOutput();
                        this.clientSocket.close();
                    }

                }

            }

        } catch (Exception ex){
            isRunSetter(false);
            ex.printStackTrace();
        } finally {
            try {
                this.clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
