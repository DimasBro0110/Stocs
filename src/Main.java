import Database.DatabaseConnection;
import ForexAPI.ConnectionHandler;
import com.sun.java.util.jar.pack.*;

import java.util.*;
import java.text.SimpleDateFormat;
import java.sql.SQLException;

public class Main {

    public static void main(String args[]){

          Thread t = null;

          try{
              Class.forName("com.mysql.jdbc.Driver");
              DatabaseConnection db = new DatabaseConnection("jdbc:mysql://localhost/Forex", "Dimas", "Dimas");
              t = new Thread(new ConnectionHandler("EUR_USD", db));
              t.start();
          }catch(Exception ex){
              System.out.println(ex.toString());
              if(t != null){
                  try {
                      t.join();
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
          }

    }

}
