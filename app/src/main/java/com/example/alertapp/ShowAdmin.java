package com.example.alertapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static android.widget.Toast.LENGTH_LONG;

public class ShowAdmin extends AppCompatActivity {
    String position = "1";
    int pos;
    Button btnOpenMap;
    TextView eidWhat, eidWhere, eidAddMatter;
Connection con;
    String[] myList;
    String[] splitSt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_admin);
        position = (Integer.parseInt(getIntent().getExtras().getString("Pos").trim()) + 1) + "";
        eidWhat = findViewById(R.id.eidWhat);
        eidWhere = findViewById(R.id.eidLocation);
        eidAddMatter = findViewById(R.id.AdditionalMatter);
        btnOpenMap = findViewById(R.id.btnMapOpen);
        //btnOpenMap.setEnabled(false);
        Button btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShowAdmin.this,AlertActivity.class));
                finish();
            }
        });
        con=DatabaseConnection.ConnectDB();
        if(con!=null)
        {

        }
        // Setting up the function when button login is clicked
        CheckLogin checkLogin = new CheckLogin();// ShowAdmin.this is the Asynctask, which is used to process in background to reduce load on app process
        checkLogin.execute("");
        btnOpenMap.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                locationFinder();

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
            Toast.makeText(ShowAdmin.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess)
            {
                Toast.makeText(ShowAdmin.this , "Login Successful" , LENGTH_LONG).show();

            }
        }
        @Override
        protected String doInBackground(String... params)
        {
           // @SuppressLint("WrongThread") String usernam = userTv.getText().toString();
            //@SuppressLint("WrongThread") String passwordd = passwordTv.getText().toString();

            try {
                con = DatabaseConnection.ConnectDB(); // Connect to database
                if (con == null) {
                    z = "Check Your Internet Access!";
                } else {
                    String query = "select * from AlertInfo where ID ="+position+";";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    /**  while (rs.next()) {
                     edInfo.setText("hai");
                     String col1 = rs.getString("what");
                     String col2 = rs.getString("location");
                     String col3 = rs.getString("additional");
                     edInfo.setText(col1);
                     z = "Login successful";
                     }

                     rs.getString("what")

                    if (rs.next()) {
                        int i=1;
                        while (i<=2) {
                            eidWhat.setText(rs.getString("what"));
                            eidWhere.setText(rs.getString("ID"));
                        }
                        z = "Login successful";
                        //edInfo.setText((CharSequence) rs.getArray("what"));
                        //Toast.makeText(AlertAdmin.this, "Successful", Toast.LENGTH_SHORT).show();
                        isSuccess = true;
                        con.close();
                    } else {
                        z = "Invalid Credentials!";
                        //Toast.makeText(AlertAdmin.this, "Failed", Toast.LENGTH_SHORT).show();
                        isSuccess = false;
                    }
                     **/
                    if (rs.next()){
                         myList = new String[]{rs.getString("what"),rs.getString("additional"),rs.getString("location")};
                        //a1.add("Zara");
                        //a1.add("Mahnaz");

                        //a1.add("Ayan");
                        //eidAddMatter.setText( rs.getString("what")+" "+rs.getString("additional")+"baldh    "+rs.getString("location"));
                        //eidAddMatter.setText(myList[1]+" "+myList[2]+" "+myList[0]);
                        rs.getString("what");
                        //eidWhere.setText(rs.getString("location"));
                        eidWhat.setText(myList[0]);
                        eidWhere.setText(myList[2]);
                        eidAddMatter.setText(myList[1]);
                        //spliting the string, here, it seperate the latitiude and longitude

                    }else{
                        // you have no record

                    }

                }
            } catch (Exception ex) {
                isSuccess = false;
                z = ex.getMessage();
            }

            return z;


        }

    }
    public void locationFinder(){
        //String[] latlang = {"-33.8666","151.1957"};
        String[] latlang = myList[2].split("\\s+");
        Uri gmmIntentUri = Uri.parse("geo:"+latlang[0]+","+latlang[1]+"");
        //Uri gmmIntentUri = Uri.parse("geo:0,0?q="+myList[2]);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

}