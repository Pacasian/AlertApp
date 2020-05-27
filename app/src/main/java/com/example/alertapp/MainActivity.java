package com.example.alertapp;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import androidx.core.app.ActivityCompat;

    /*
        This source code could be used for academic purposes only. Posting on other websites or blogs is only allowed with a dofollow link to the orignal content.
    */

public class MainActivity extends Activity
{
    // Declaring layout button, edit texts
    Button loginBtn;
    EditText userTv,passwordTv;
    ProgressBar progressBar;
    Connection con;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        loginBtn = (Button) findViewById(R.id.button);
        userTv = (EditText) findViewById(R.id.editText);
        passwordTv = (EditText) findViewById(R.id.editText2);
        textView=findViewById(R.id.textView2);
        progressBar =findViewById(R.id.progressBar);

        con=DatabaseConnection.ConnectDB();
        if(con!=null)
        {
            progressBar.setVisibility(View.GONE);
        }
        // Setting up the function when button login is clicked
        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckLogin checkLogin = new CheckLogin();// this is the Asynctask, which is used to process in background to reduce load on app process
                checkLogin.execute("");
            }
        });
    }

    public class CheckLogin extends AsyncTask<String,String,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {

        }

        @Override
        protected void onPostExecute(String r)
        {
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(MainActivity.this , "Login Successful" , Toast.LENGTH_LONG).show();

            }
        }
        @Override
        protected String doInBackground(String... params)
        {
            @SuppressLint("WrongThread") String usernam = userTv.getText().toString();
            @SuppressLint("WrongThread") String passwordd = passwordTv.getText().toString();

            if(usernam.trim().equals("")|| passwordd.trim().equals(""))
                z = "Please enter Username and Password";
            else
            {
                try
                {
                    con = DatabaseConnection.ConnectDB(); // Connect to database
                    if (con == null)
                    {
                        z = "Check Your Internet Access!";
                    }
                    else
                    {
                        String query = "select * from AlertTable where ID= " + usernam + " and password = '"+ passwordd +"';";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        if(rs.next())
                        {
                            z = "Login successful";
                            startActivity(new Intent(MainActivity.this,alert.class));
                            isSuccess=true;
                            con.close();
                        }
                        else
                        {
                            z = "Invalid Credentials!";
                            isSuccess = false;
                        }
                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = ex.getMessage();
                }
            }
            return z;
        }
    }
}