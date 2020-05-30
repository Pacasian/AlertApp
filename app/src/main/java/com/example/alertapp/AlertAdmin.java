package com.example.alertapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AlertAdmin extends AppCompatActivity {
    Button btnShowData;
    TextView edInfo;
    Connection con;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_admin);
        btnShowData = findViewById(R.id.btnShowData);
        edInfo = findViewById(R.id.edShowData);
        con = DatabaseConnection.ConnectDB();
        if (con != null) {
            Toast.makeText(this, "Connection valid", Toast.LENGTH_SHORT).show();
        }
        btnShowData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDatabase1 AlertDB = new AlertDatabase1();
                AlertDB.execute("");
            }
        });
    }

    public class AlertDatabase1 extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(AlertAdmin.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(AlertAdmin.this, "Login Successful", Toast.LENGTH_LONG).show();

            }
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {
            /**@SuppressLint("WrongThread") String sendWhat = edWhat.getText().toString();
             @SuppressLint("WrongThread") String sendWhere = "10250540";
             @SuppressLint("WrongThread") String sendMore = "Nothing";
             **/

            try {
                con = DatabaseConnection.ConnectDB(); // Connect to database
                if (con == null) {
                    z = "Check Your Internet Access!";
                } else {
                    String query = "select * from AlertInfo where ID =4;";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    String st=rs.getString("ID");
                    /**  while (rs.next()) {
                        edInfo.setText("hai");
                        String col1 = rs.getString("what");
                        String col2 = rs.getString("location");
                        String col3 = rs.getString("additional");
                       edInfo.setText(col1);
                        z = "Login successful";
                    }

                     rs.getString("what")
                     **/
                    if (rs.next()) {
                        z = "Login successful";
                        edInfo.setText((CharSequence) rs.getArray("what"));
                        //Toast.makeText(AlertAdmin.this, "Successful", Toast.LENGTH_SHORT).show();
                        isSuccess = true;
                        con.close();
                    } else {
                        z = "Invalid Credentials!";
                        //Toast.makeText(AlertAdmin.this, "Failed", Toast.LENGTH_SHORT).show();
                        isSuccess = false;
                    }

                }
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage();
            }

            return z;

        }
    }
}
