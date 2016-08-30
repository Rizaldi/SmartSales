package smartsales.rizaldi.com.smartsales.Report.tracking;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;

public class Picture extends AppCompatActivity {
    ImageView imgcheckin,imgcheckout;
    TextView datecheckin,datecheckout,timecheckin,timecheckout,customername,address;
    String visitId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initialUI();
        Bundle b=getIntent().getExtras();
        visitId=b.getString("visitId");
        getData();

    }

    private void getData() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_picture+visitId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String data = jsonObject.getString("data");
                    jsonArray = new JSONArray(data);
                    parseData(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),
                        volleyError.getMessage(), Toast.LENGTH_LONG).show();
                loading.dismiss();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                customername.setText(json.getString("customer_name"));
                address.setText(json.getString("customer_address"));
                if(json.getString("checkin_picture").isEmpty()){
                    imgcheckin.setVisibility(View.GONE);
                }
                if(json.getString("checkout_picture").isEmpty()){
                    imgcheckout.setVisibility(View.GONE);
                }
                Picasso.with(this).load("http://"+json.getString("checkin_picture")).into(imgcheckin);
                Picasso.with(this).load("http://"+json.getString("checkout_picture")).into(imgcheckout);
                datecheckin.setText(json.getString("checkin_date").isEmpty()?"-":json.getString("checkin_date"));
                datecheckout.setText(json.getString("checkout_date").isEmpty()?"-":json.getString("checkout_date"));
                timecheckout.setText(json.getString("checkout_time").isEmpty()?"-":json.getString("checkout_time"));
                timecheckin.setText(json.getString("checkin_time").isEmpty()?"-":json.getString("checkin_time"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void initialUI() {
        imgcheckin= (ImageView) findViewById(R.id.imgcheckin);
        imgcheckout= (ImageView) findViewById(R.id.imgcheckout);
        datecheckin= (TextView) findViewById(R.id.datecheckin);
        datecheckout= (TextView) findViewById(R.id.datecheckout);
        timecheckin= (TextView) findViewById(R.id.timecheckin);
        timecheckout= (TextView) findViewById(R.id.timecheckout);
        customername= (TextView) findViewById(R.id.customername);
        address= (TextView) findViewById(R.id.address);
    }

}
