package com.example.alertapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import androidx.core.app.ActivityCompat;


public class alert extends AppCompatActivity implements LocationListener {
    String latitude,longitude;
    TextView actionTittle;
    ImageView actionIcon;
    Button btnAlert, btnGps, btnMention,button2;
    EditText edWhat, edMention, edAdd;
    String loco = "";
    //Connection con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.RED);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.NavGrey));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout);
        actionTittle = findViewById(R.id.actionTitle);
        actionTittle.setText("Alert");
        actionTittle.setTextColor(getResources().getColor(R.color.Red));
        actionIcon = findViewById(R.id.actionIcon);
        actionIcon.setImageResource(R.drawable.alert);

        setContentView(R.layout.alert);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);


        edWhat = (EditText) findViewById(R.id.idWhat);
        edMention = (EditText) findViewById(R.id.idMention);
        edAdd = (EditText) findViewById(R.id.idAdd);
        btnGps = (Button) findViewById(R.id.btnGps);
        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(alert.this, "hai", Toast.LENGTH_SHORT).show();
                btnMention.setEnabled(false);

                ActivityCompat.requestPermissions(alert.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                if (ActivityCompat.checkSelfPermission(alert.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
        /*
        tvLatitud.setText("No se tienen permisos");
        ...
         */
                    Toast.makeText(alert.this, "no", Toast.LENGTH_SHORT).show();

                    return;
                }else
                {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                     latitude = location.getLatitude()+" ";
                     longitude =  location.getLongitude()+" ";
                }
                Toast.makeText(alert.this, latitude+"", Toast.LENGTH_SHORT).show();
                loco=latitude+" "+longitude;
                edMention.setText("lat "+ latitude+" "+" long "+longitude);
                edMention.setEnabled(false);
            }
        });
        btnMention = (Button) findViewById(R.id.btnManual);
        btnMention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAlert.setEnabled(false);
                String localVal = edMention.getText().toString();
                loco=localVal;
            }
        });
        btnAlert = (Button) findViewById(R.id.btnAlert);

        /**con = DatabaseConnection.ConnectDB();
        if (con != null) {
            Toast.makeText(this, "Connection valid", Toast.LENGTH_SHORT).show();
        }**/
        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDatabase AlertDB = new AlertDatabase();
                AlertDB.execute("");
            }
        });
        button2=findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(alert.this,AlertActivity.class));
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class AlertDatabase extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(alert.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess) {
                Toast.makeText(alert.this, "Login Successful", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected String doInBackground(String... params) {
           /** @SuppressLint("WrongThread") String sendWhat = edWhat.getText().toString();
            @SuppressLint("WrongThread") String sendWhere = "10250540";
            @SuppressLint("WrongThread") String sendMore = "Nothing";

            if (sendWhat.trim().equals("") || sendWhere.trim().equals(""))
                z = "Please enter Username and Password";
            else {
                try {
                    con = DatabaseConnection.ConnectDB(); // Connect to database
                    if (con == null) {
                        z = "Check Your Internet Access!";
                    } else {
                        String query = "insert into AlertInfo (what,location,additional) values ('" + sendWhat + "','" + sendWhere + "','" + sendMore + "');";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        Toast.makeText(alert.this, rs+"", Toast.LENGTH_SHORT).show();
                        if (rs.next()) {
                            z = "Login successful";
                            Toast.makeText(alert.this, "Successful", Toast.LENGTH_SHORT).show();
                            isSuccess = true;
                            con.close();
                        } else {
                            z = "Invalid Credentials!";
                            Toast.makeText(alert.this, "Failed", Toast.LENGTH_SHORT).show();
                            isSuccess = false;
                        }
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = ex.getMessage();
                }
            }
            return z;

        } **/

            try
            {

                Connection con = new ConnectionClass().CONN();
                @SuppressLint("WrongThread") String sendWhat = edWhat.getText().toString();
                @SuppressLint("WrongThread") String sendWhere = loco;
                @SuppressLint("WrongThread") String sendMore = edAdd.getText().toString();
                String query = "insert into AlertInfo (what,location,additional) values ('" + sendWhat + "','" + sendWhere + "','" + sendMore + "');";
                Statement stmt = con.createStatement();
                stmt.executeUpdate(query);
            }
            catch (SQLException se)
            {
                Log.e("ERROR", se.getMessage());
            }
return "suuss";
        }
    }
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.alert_menu, menu);
            return true;
        }

        public void onGroupItemClickAlert(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.alertCall:
                    Intent intentCall = new Intent(Intent.ACTION_CALL);

                    intentCall.setData(Uri.parse("tel:919952050439"));

                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Please grant permission", Toast.LENGTH_SHORT).show();
                        requestPermission();
                    } else {
                        startActivity(intentCall);
                    }
                    break;
            }
        }

        private void requestPermission() {
            ActivityCompat.requestPermissions(alert.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }

}


