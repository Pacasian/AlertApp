package com.example.alertapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class AfterDelete_IP_module extends AppCompatActivity {
    private TextView ipShow;
    private Button btnIP;
    String st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_delete__i_p_module);
        ipShow = (TextView) findViewById(R.id.ipTXT);
        btnIP = (Button) findViewById(R.id.btnIP);
        //Document doc = Jsoup.parse("https://pacasiancommunity.cf/");
        //st=doc.text();
        btnIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBodyText();
            }
        });
    }

    private void getBodyText() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    String url = "https://railwayserver-58664.firebaseapp.com/";//your website url
                    Document doc = Jsoup.connect(url).get();

                    Element body = doc.body();
                    Element img = doc.select("#retriveServer").first();
                    builder.append(img.text());

                } catch (Exception e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ipShow.setText(builder.toString());
                    }
                });
            }
        }).start();
    }
}

