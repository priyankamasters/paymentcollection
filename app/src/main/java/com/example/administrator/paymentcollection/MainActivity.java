package com.example.administrator.paymentcollection;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends Activity {

	private ArrayList<Model> ledgerList;

	private static final int ACTIVITY_RESULT_QR_DRDROID = 0;
	static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

	TextView tv_customer_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tv_customer_id = (TextView) findViewById(R.id.txt_customer_id_val);

		ImageButton btnScan = (ImageButton) findViewById(R.id.img_btn_scan);
		btnScan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			openScanner();
			}
		});

		ledgerList = new ArrayList<Model>();
		ListView lview = (ListView) findViewById(R.id.listview);
		ListviewAdapter adapter = new ListviewAdapter(this, ledgerList);
		lview.setAdapter(adapter);

		populateList();
		adapter.notifyDataSetChanged();

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data){

		Uri uri;

	IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
	if(result!=null)

	{
		if (result.getContents() == null) {
			tv_customer_id.setText("OOPS.. You did not scan anything");
			Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
		} else {
			Log.d("ScanActivity", "Scanned");
			tv_customer_id.setText(result.getContents());

//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
		}
	}

	else
			super.

	onActivityResult(requestCode, resultCode, data);

}




	private void populateList() {

		Model item1, item2, item3, item4, item5;

		item1 = new Model("1", "Apple (Northern Spy)", "Fruits", "₹. 200");
		ledgerList.add(item1);

		item2 = new Model("2", "Orange (Sunkist navel)", "Fruits", "₹. 100");
		ledgerList.add(item2);

		item3 = new Model("3", "Tomato", "Vegetable", "₹. 50");
		ledgerList.add(item3);

		item4 = new Model("4", "Carrot", "Vegetable", "₹. 80");
		ledgerList.add(item4);

		item5 = new Model("5", "Banana (Cavendish)", "Fruits", "₹. 100");
		ledgerList.add(item5);
	}

	//product qr code mode

	private void openScanner() {
		IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
		intentIntegrator.setBeepEnabled(true);
		intentIntegrator.setOrientationLocked(false);
		intentIntegrator.setPrompt("Scan the barcode or QR code to get the data!");
		intentIntegrator.initiateScan();
	}
}