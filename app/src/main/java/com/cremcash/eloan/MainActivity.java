package com.cremcash.eloan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cremcash.eloan.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.DriverManager;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    public static String load_selector;
    Connection con;

    public com.google.android.material.bottomappbar.BottomAppBar getBottomAppBar() {
        return binding.bottomAppBar;  // assuming the ID in the layout is `bottomAppBar`
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_applications, R.id.navigation_saved, R.id.navigation_info)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        FloatingActionButton fab = findViewById(R.id.fab);

//        fab.setOnClickListener(view -> {
//            con = connectionClass(ConnectionClass.un, ConnectionClass.pass, ConnectionClass.db, ConnectionClass.ip);
//            Intent intent = new Intent(MainActivity.this, ClientInformation.class);
//            if (con != null){
//                load_selector = "default";
//                Bundle bundle = new Bundle();
//                intent.putExtras(bundle);
//                intent.putExtra("mode", "online");
//            }else{
//                load_selector = "offline";
//                Bundle bundle = new Bundle();
//                intent.putExtras(bundle);
//                intent.putExtra("mode", "offline");
//            }
//            startActivity(intent);
//        });
        fab.setOnClickListener(view -> {
            ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Connecting to server...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            new AsyncTask<Void, Void, Boolean>() {
                Connection testCon;

                @Override
                protected Boolean doInBackground(Void... voids) {
                    testCon = connectionClass(ConnectionClass.un, ConnectionClass.pass, ConnectionClass.db, ConnectionClass.ip);
                    return testCon != null;
                }

                @Override
                protected void onPostExecute(Boolean isConnected) {
                    progressDialog.dismiss();
                    con = testCon;

                    String mode = isConnected ? "online" : "offline";
                    load_selector = mode;

                    if (isConnected) {
                        Intent intent = new Intent(MainActivity.this, ClientInformation.class);
                        intent.putExtra("status", mode);
                        startActivity(intent);
                    } else {
                        // Show alert dialog if connection fails
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Offline Mode")
                                .setMessage("Could not connect to the server.\nProceed in offline mode?")
                                .setCancelable(false)
                                .setPositiveButton("Proceed", (dialog, which) -> {
                                    Intent intent = new Intent(MainActivity.this, ClientInformation.class);
                                    intent.putExtra("status", mode);
                                    startActivity(intent);
                                })
                                .setNegativeButton("Cancel", null)
                                .show();
                    }
                }
            }.execute();
        });
    }

    @SuppressLint("NewApi")
    public Connection connectionClass(String user, String password, String database, String server){
        Connection connection = null;
        String connectionURL;

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL = "jdbc:jtds:sqlserver://" + server + "/" + database +
                    ";user=" + user +
                    ";password=" + password +
                    ";loginTimeout=3;" +
                    "socketTimeout=5;";

            connection = DriverManager.getConnection(connectionURL);
        } catch (Exception e) {
            Log.e("SQL Connection Error", e.getMessage());
        }

        return connection;
    }

}
