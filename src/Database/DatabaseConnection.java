package Database;
/**
 * Created by dimas on 10.03.16.
 */

import java.sql.*;

public class DatabaseConnection {


    private String url = null;
    private String user = null;
    private String password = null;
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public DatabaseConnection(String url,  String user, String password) throws SQLException {

        this.url = url;
        this.user = user;
        this.password = password;
        this.connection = connectionGetter();
        this.statement = this.connection.createStatement();
        this.connection.setAutoCommit(true);
        System.out.println("Database Successfully Connected !!!");

    }

    private Connection connectionGetter() throws SQLException {

        return DriverManager.getConnection(this.url, this.user, this.password);

    }

    public void selection() throws SQLException {

        this.resultSet = this.statement.executeQuery("select * from Forex.STOCKS");
        try {
            System.out.println(this.resultSet.getMetaData().getTableName(1));
        } catch(Exception ex) {
            System.out.println(ex);
        }

    }

    public void insertToTable(double bid, double ask, String status, String instrument) throws SQLException {

        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "INSERT INTO Forex.STOCKS(BID, ASK, STATUS, INSTRUMENT, TIME) VALUES(?, ?, ?, ?,NOW())");
            preparedStatement.setDouble(1, bid);
            preparedStatement.setDouble(2, ask);
            if(status.equals("halted")){
                preparedStatement.setByte(3, (byte) 1);
            }else if(status.length() == 0){
                preparedStatement.setByte(3, (byte) 0);
            }else{
                preparedStatement.setByte(3, (byte) 0);
            }
            preparedStatement.setString(4, instrument);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
