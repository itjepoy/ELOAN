package com.cremcash.eloan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserLoginActivity extends AppCompatActivity {
    TextInputEditText usernamelogin,passwordlogin;
    Button loginbtn;
    Connection con;

    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        usernamelogin = findViewById(R.id.username);
        passwordlogin = findViewById(R.id.password);
        loginbtn = findViewById(R.id.btn_login);

        usernamelogin.setFilters(new InputFilter[]{
                new InputFilter.AllCaps()
        });

        loginbtn.setOnClickListener(v -> {
            login_check();
        });

        db = new SQLiteHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        usernamelogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(usernamelogin.getText().toString().length() <=0){
                    usernamelogin.setError("Enter Employee ID");
                }else {
                    usernamelogin.setError(null);
                }
            }
        });

        passwordlogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(passwordlogin.getText().toString().length() <=0){
                    passwordlogin.setError("Enter Password");
                }else {
                    passwordlogin.setError(null);
                }
            }
        });

    }

    public static String md5(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

//    @SuppressLint("StaticFieldLeak")
//    public class login_check extends AsyncTask<String,String,String>{
//        String z = null;
//        Boolean isSuccess = null;
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try {
//                con = connectionClass(ConnectionClass.un, ConnectionClass.pass, ConnectionClass.dbCAS, ConnectionClass.ip);
//                if(con == null){
//                    runOnUiThread(() -> {
//                        Toast.makeText(UserLoginActivity.this,"You are not connected to network. Offline mode will activate",Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    });
//                    Log.d("Network Error","Not Connected to Server");
//                }else {
//                    String hashedPassword = md5(passwordlogin.getText().toString());
//
//                    String sql = "SELECT * FROM UserAccounts WHERE Employee_ID = ? AND Password = ?";
//                    PreparedStatement stmt = con.prepareStatement(sql);
//                    stmt.setString(1, usernamelogin.getText().toString());
//                    stmt.setString(2, hashedPassword);
//
//                    ResultSet rs = stmt.executeQuery();
//
//                    if (rs.next()) {
//                        runOnUiThread(() -> Toast.makeText(UserLoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show());
//                        Log.d("Network Connected","Login Success");
//                        session.setLogin(true);
//
//                        Date c = Calendar.getInstance().getTime();
//                        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("MMddyyyy");
//
//                        String uid = rs.getString("Employee_ID");
//                        String username = rs.getString("Employee_ID");
//                        String name = rs.getString("Employee_ID");
//                        String created_at = df.format(c);
//                        session.setUserId(uid);
//                        db.addUser(name, username, uid, created_at);
//                        Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//
//                    } else {
//                        runOnUiThread(() -> Toast.makeText(UserLoginActivity.this, "Check username or password", Toast.LENGTH_LONG).show());
//
//                        usernamelogin.setText("");
//                        passwordlogin.setText("");
//                    }
//                }
//            }catch (Exception e){
//                isSuccess = false;
//                Log.e("SQL Error : ", e.getMessage());
//            }
//            return z;
//        }
//    }

    private void login_check() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                con = connectionClass(ConnectionClass.un, ConnectionClass.pass, ConnectionClass.dbCAS, ConnectionClass.ip);
                if (con == null) {
                    handler.post(() -> {
                        Toast.makeText(UserLoginActivity.this, "You are not connected to network. Offline mode will activate", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(UserLoginActivity.this, MainActivity.class));
                        finish();
                    });
                    Log.d("Network Error", "Not Connected to Server");
                } else {
                    String hashedPassword = md5(passwordlogin.getText().toString());

                    String sql = "SELECT * FROM UserAccounts WHERE Employee_ID = ? AND Password = ?";
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setString(1, usernamelogin.getText().toString());
                    stmt.setString(2, hashedPassword);

                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        handler.post(() -> Toast.makeText(UserLoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show());
                        Log.d("Network Connected", "Login Success");
                        session.setLogin(true);

                        Date c = Calendar.getInstance().getTime();
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("MMddyyyy");

                        String uid = rs.getString("Employee_ID");
                        String username = rs.getString("Employee_ID");
                        String name = rs.getString("Employee_ID");
                        String created_at = df.format(c);

                        session.setUserId(uid);
                        db.addUser(name, username, uid, created_at);

                        handler.post(() -> {
                            startActivity(new Intent(UserLoginActivity.this, MainActivity.class));
                            finish();
                        });
                    } else {
                        handler.post(() -> {
                            Toast.makeText(UserLoginActivity.this, "Check username or password", Toast.LENGTH_LONG).show();
                            usernamelogin.setText("");
                            passwordlogin.setText("");
                        });
                    }
                }
            } catch (Exception e) {
                Log.e("SQL Error : ", e.getMessage());
            }
        });
    }

    public void checkLogin () {
        con = connectionClass(ConnectionClass.un, ConnectionClass.pass, ConnectionClass.db, ConnectionClass.ip);
        if(con == null){
            runOnUiThread(() -> {
                Toast.makeText(UserLoginActivity.this,"You are not connected to network. Offline mode will activate",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
            Log.d("Network Error","Not Connected to Server");
        } else {
            try {
                String sql = "SELECT Staff_IDNo, Staff_Username, Staff_Mixname FROM StaffMember WHERE Staff_Username = '" + usernamelogin.getText() + "' AND Staff_Password = '" + passwordlogin.getText() + "' ";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                if (rs.next()) {
                    runOnUiThread(() -> Toast.makeText(UserLoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show());
                    Log.d("Network Connected","Login Success");
                    //session.setLogin(true);

//                    Date c = Calendar.getInstance().getTime();
//                    @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("MMddyyyy");
//
//                    JSONObject user = new JSONObject();
//                    try {
//                        user.put("uid", rs.getString("Staff_IDNo"));
//                        user.put("username", rs.getString("Staff_Username"));
//                        user.put("name", rs.getString("Staff_Mixname"));
//                        user.put("created_at", df.format(c));
//
//                    } catch (JSONException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//
//                    JSONArray jsonArray = new JSONArray();
//                    jsonArray.put(user);
//
//                    for(int i=0; i < jsonArray.length(); i++) {
//                        JSONObject jsonobject = jsonArray.getJSONObject(i);
//                        String uid = jsonobject.getString("uid");
//                        String username = jsonobject.getString("username");
//                        String name = jsonobject.getString("name");
//                        String created_at = jsonobject.getString("created_at");
//                        db.addUser(name, username, uid, created_at);
//
//                        Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
//                        intent.putExtra("key", rs.getString(name));
//                        startActivity(intent);
//                        finish();
//                    }

                    Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                    intent.putExtra("key", rs.getString(Objects.requireNonNull(usernamelogin.getText()).toString()));
                    startActivity(intent);
                    finish();

                } else {
                    runOnUiThread(() -> Toast.makeText(UserLoginActivity.this, "Check username or password", Toast.LENGTH_LONG).show());

                    usernamelogin.setText("");
                    passwordlogin.setText("");
                }
            } catch (Exception e) {
                Log.e("SQL Error : ", e.getMessage());
            }
        }
    }

    @SuppressLint("NewApi")
    public Connection connectionClass(String user, String password, String database, String server){
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