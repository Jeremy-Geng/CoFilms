package com.cognidius.cofilms.database.Azure;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AzureConnection {
    private static java.sql.Connection connection = null;
    public static ArrayList<String> usernameList;


    @SuppressLint("AuthLeak")
    public static void connect() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String url = "jdbc:jtds:sqlserver://cofilmsserver.database.windows.net:1433;DatabaseName=datacofilms;user=jeremygeng@cofilmsserver;password=3458Geng;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(url);
            System.out.println("Connection Succeed");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println(" erroe code" + e.getErrorCode());
            System.err.println("Connection Failed");
        }
    }

    public static PreparedStatement insert(String table) {
        PreparedStatement insertStatement = null;

        try {
            switch (table) {
                case "Userinfo":
                    insertStatement = connection.prepareStatement("insert into userinfo values(?,?,?,?,?,?,?)");
                    break;
                default:

            }
        } catch (SQLException e) {
            e.getErrorCode();
        }
        return insertStatement;
    }

    public static ResultSet execute(String query){
        PreparedStatement readStatement = null;
        ResultSet resultSet = null;
        try {
            readStatement = connection.prepareStatement(query);
            resultSet = readStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public static void getUsernameList(){
        usernameList = new ArrayList<>();
        ResultSet resultSet = execute("select username from userinfo");

        try{
            while(resultSet.next()){
                usernameList.add(resultSet.getString(1));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean checkPassWord(String username, String password){
        boolean rst = false;
        String query = "select password from userinfo where username = '" + username + "'";
        ResultSet resultSet = execute(query);
        try{
            if(resultSet.next() && resultSet.getString(1).equals(password)){
                    rst = true;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return rst;
        }
        return rst;
    }

    public static void disconnect() throws SQLException {
        connection.close();
    }

}
