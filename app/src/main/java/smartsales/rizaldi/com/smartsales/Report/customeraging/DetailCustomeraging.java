package smartsales.rizaldi.com.smartsales.Report.customeraging;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;

public class DetailCustomeraging extends AppCompatActivity {
    RecyclerView rv;
    String customerId="";
    private List<ModelListDetail> list;
    RVAadapterDetail adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_customeraging);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        list=new ArrayList<>();
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setFocusable(false);
        Bundle b=getIntent().getExtras();
        customerId=b.getString("id");
        getDataListDetail();

    }

    private void getDataListDetail() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_customeragingdetail +customerId,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        String data = jsonObject.getString("data");
                        jsonArray = new JSONArray(data);
                        parseDataList(jsonArray);
                    } else {
                        Toast.makeText(DetailCustomeraging.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),
                        "No Data Found!", Toast.LENGTH_LONG).show();
                list.clear();
                adapter = new RVAadapterDetail(list, DetailCustomeraging.this);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loading.dismiss();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void parseDataList(JSONArray jsonArray) {
        int i;
        for (i = 0; i < jsonArray.length(); i++) {
            ModelListDetail Items = new ModelListDetail();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Items.setDocument_number(jsonObject.getString("document_number"));
                Items.setType(jsonObject.getString("type"));
                Items.setDatetransaction(jsonObject.getString("date_transaction"));
                Items.setTotalamount(jsonObject.getString("total_amount"));
                Items.setTerms(jsonObject.getString("terms"));
                Items.setDuedate(jsonObject.getString("due_date"));
                Items.setPdreceipt(jsonObject.getString("pd_receipt"));
                Items.setOutstanding(jsonObject.getString("outstanding"));
                Items.setCurentmonth(jsonObject.getString("current_month"));
                Items.setLastmonth(jsonObject.getString("lastMonth"));
                Items.setLast2month(jsonObject.getString("last2Month"));
                Items.setLast3month(jsonObject.getString("last3Month"));
                Items.setLast3month(jsonObject.getString("last3Month"));
                Items.setLast4month(jsonObject.getString("last4Month"));
                Items.setLast5month(jsonObject.getString("last5Month"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && list.size() != 0) {
                list.clear();
                list.add(Items);
            } else if (i == 0) {
                list.add(Items);
            } else {
                list.add(Items);
            }
        }
        adapter = new RVAadapterDetail(list, this);
        rv.setAdapter(adapter);
    }


}
