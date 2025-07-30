package com.cremcash.eloan;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {
    //public static String ip ="26.108.247.37"; // SQL Server IP Address
    public static String ip ="10.10.0.113"; // SQL Server CASHMA and CREMLIC IP Address
    //public static String ip ="10.10.0.73"; // SQL Server CAMFIN IP Address
    public static String un = "sa"; // SQL Server User name
    public static String pass = "g@t3k33p3R2024"; // SQL Server Password
    //public static String db = "ExclusiveDataInfoCLI"; // CAMFIN SQL Server Database
    public static String db = "ExclusiveDataInfoCASH"; // CREMLIC SQL Server Database
    //public static String db = "ExclusiveDataInfoCASH"; // CASHMA SQL Server Database
    public static String dbCAS = "CAS";

    public static Connection getConnection(String user, String password, String database, String server){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionURL = null;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL = "jdbc:jtds:sqlserver://" + server+"/" + database + ";user=" + user + ";password=" + password + ";";
            connection = DriverManager.getConnection(connectionURL);
        }catch (Exception e){
            Log.e("SQL Connection Error : ", e.getMessage());
        }

        return connection;
    }
}
