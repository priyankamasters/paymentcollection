package com.example.administrator.paymentcollection;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 16/Feb/17.
 */

public class Settings extends Activity {

    public static final String SERVER_URL_PREF = "server_url_pref" ;
    public static final String SERVER_URL = "server_url";

    SharedPreferences sharedpreferences;

    EditText url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        sharedpreferences = getSharedPreferences(SERVER_URL_PREF, Context.MODE_PRIVATE);

        url= (EditText) findViewById(R.id.et_url);
        url.setText(sharedpreferences.getString(SERVER_URL,""));

        Button save= (Button) findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s_url=url.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(SERVER_URL, s_url);
                editor.commit();

                Toast.makeText(getApplicationContext(),"URL updated sucessfully",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
