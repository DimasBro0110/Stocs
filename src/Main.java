import Database.DatabaseConnection;
import ForexAPI.ConnectionHandler;
import Server_TCP_Analysis.ServerRun;
import com.sun.java.util.jar.pack.*;

import java.util.*;
import java.text.SimpleDateFormat;
import java.sql.SQLException;

public class Main {

    public static void main(String args[]){

        Thread t_forex = null;

          try{
              Class.forName("com.mysql.jdbc.Driver");
              DatabaseConnection db = new DatabaseConnection("jdbc:mysql://localhost/Forex", "Dimas", "Dimas");
              t_forex = new Thread(new ConnectionHandler("EUR_USD", db));
              t_forex.start();
              ServerRun serv = new ServerRun();
          }catch(Exception ex){
              System.out.println(ex.toString());
          }

    }

}
