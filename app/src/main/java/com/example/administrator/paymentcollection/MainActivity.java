package com.example.administrator.paymentcollection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {

    private ProgressDialog pDialog;

    JSONParser jParser = new JSONParser();

    private ArrayList<Model> ledgerList;
    ArrayList<HashMap<String, String>> ledger_hash;

    private static final int ACTIVITY_RESULT_QR_DRDROID = 0;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private static String url_ledger_list = "http://mastertechnics.com/liveApp/payment_collection/get_all_ledgers.php";

    TextView tv_customer_id, tv_customer_name;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_LEDGERS = "ledgers";
    private static final String TAG_CUST_ID = "accountcode";
    private static final String TAG_CUST_NAME = "name";
    private static final String TAG_TRDATE = "trdate";
    private static final String TAG_DESC = "gldescription";
    private static final String TAG_DEBIT = "debit";
    private static final String TAG_CREDIT = "credit";

    JSONArray ledgers = null;

    ListView lview;

    public static final String SERVER_URL_PREF = "server_url_pref";
    public static final String SERVER_URL = "server_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences userDetails = getApplicationContext().getSharedPreferences(SERVER_URL_PREF, MODE_PRIVATE);
        String url = userDetails.getString(SERVER_URL, null);

        if (url == null) {
            SharedPreferences.Editor editor = userDetails.edit();
            editor.putString(SERVER_URL, url_ledger_list);
            editor.commit();
        }

        ledgerList = new ArrayList<Model>();

        tv_customer_id = (TextView) findViewById(R.id.txt_customer_id_val);
        tv_customer_name = (TextView) findViewById(R.id.txt_customer_name_val);

        ImageButton btnScan = (ImageButton) findViewById(R.id.img_btn_scan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openScanner();
            }
        });

        Button btn_print = (Button) findViewById(R.id.btn_print);
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        lview = (ListView) findViewById(R.id.listview);

        lview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String Date = ((TextView) view.findViewById(R.id.lDate)).getText().toString();
                String Description = ((TextView) view.findViewById(R.id.lDesc)).getText().toString();
                String Debit = ((TextView) view.findViewById(R.id.lDebit)).getText().toString();
                String Credit = ((TextView) view.findViewById(R.id.lCredit)).getText().toString();

                Toast.makeText(getApplicationContext(), Description + " Chosen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null)

        {
            if (result.getContents() == null) {
                tv_customer_id.setText("OOPS.. You did not scan anything");
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {

                tv_customer_id.setText(result.getContents());
                if (isOnline()) new LoadAllLedgers().execute(tv_customer_id.getText().toString());

                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);

    }

    //product qr code mode
    private void openScanner() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Scan the barcode or QR code to get the data!");
        intentIntegrator.initiateScan();
    }

    class LoadAllLedgers extends AsyncTask<String, String, String> {

        String cust_name;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading ledgers. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user_id", args[0]));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_ledger_list, "GET", params);

            // Check your log cat for JSON reponse
            Log.d("All ledgers: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    ledgers = json.getJSONArray(TAG_LEDGERS);

                    for (int i = 0; i < ledgers.length(); i++) {
                        JSONObject c = ledgers.getJSONObject(i);

                        // Storing each json item in variable
                        String cust_id = c.getString(TAG_CUST_ID);
                        cust_name = c.getString(TAG_CUST_NAME);
                        String tr_date = c.getString(TAG_TRDATE);
                        String tr_desc = c.getString(TAG_DESC);
                        String debit = c.getString(TAG_DEBIT);
                        String credit = c.getString(TAG_CREDIT);

                        Model item1 = new Model(tr_date, tr_desc, debit, credit);
                        ledgerList.add(item1);
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "No ledgers found", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }


        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {

                    tv_customer_name.setText(cust_name);

                    ListviewAdapter adapter = new ListviewAdapter(MainActivity.this, ledgerList);
                    lview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    // Check internet connectivity
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent i = new Intent(getApplicationContext(), Settings.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}