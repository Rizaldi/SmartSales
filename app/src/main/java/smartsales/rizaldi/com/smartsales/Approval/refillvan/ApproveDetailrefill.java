package smartsales.rizaldi.com.smartsales.Approval.refillvan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.Varglobal;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class ApproveDetailrefill extends AppCompatActivity implements View.OnClickListener {
    private List<Modeldetailrefill> list;
    ImageButton approve, reject, close;
    RecyclerView rv;
    String idrefill = "";
    RVAApprovalrefill adapter;
    private String username="",warehouseId="";
    SqliteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_detailrefill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ApproveDetailrefill.this, RefillRequest.class));
                finish();
            }
        });
        Varglobal.approvallist="";
        db=new SqliteHandler(this);
        HashMap<String,String> user=db.getUserDetails();
        username=user.get("username");
        warehouseId=user.get("warehouse_id");
        Bundle b = getIntent().getExtras();
        idrefill = b.getString("id");
        list = new ArrayList<>();
        initial();

        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
        getData(idrefill);

    }

    private void getData(String idrefill) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_listapprovalrefill + idrefill
                +"&warehouseId="+warehouseId,
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
                                Toast.makeText(ApproveDetailrefill.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                adapter = new RVAApprovalrefill(list, ApproveDetailrefill.this);
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
            Modeldetailrefill Items = new Modeldetailrefill();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Items.setId(jsonObject.getString("id"));
                Items.setProductname(jsonObject.getString("name"));
                Items.setProductid(jsonObject.getString("product_id"));
                Items.setUomid(jsonObject.getString("uom_id"));
                Items.setUom(jsonObject.getString("uom_name"));
                Items.setOrderedqty(jsonObject.getString("qty"));
                Items.setWarehousestock(jsonObject.getString("stok"));
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
        adapter = new RVAApprovalrefill(list, this);
        rv.setAdapter(adapter);
    }

    private void initial() {
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setFocusable(false);

        approve = (ImageButton) findViewById(R.id.approve);
        reject = (ImageButton) findViewById(R.id.reject);
        close = (ImageButton) findViewById(R.id.close);
        approve.setOnClickListener(this);
        reject.setOnClickListener(this);
        close.setOnClickListener(this);
    }
    private void Rejectrefill() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlLib.rejectrefill, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("JSON",s);
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if(code.equals("1")){
                        Toast.makeText(ApproveDetailrefill.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ApproveDetailrefill.this,RefillRequest.class));
                        finish();
                    }else{
                        Toast.makeText(ApproveDetailrefill.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
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
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idRefill", idrefill);
                params.put("username", username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void Approverefill(final String approve) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlLib.aproverefill, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if(code.equals("1")){
                        Toast.makeText(ApproveDetailrefill.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ApproveDetailrefill.this,RefillRequest.class));
                        finish();
                    }else{
                        Toast.makeText(ApproveDetailrefill.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
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
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idRefill", idrefill);
                params.put("username", username);
                params.put("approvalList", approve);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
    @Override
    public void onClick(View v) {
        if(v==close){
            startActivity(new Intent(ApproveDetailrefill.this,RefillRequest.class));
            finish();
        }
        if(v==reject){
            Rejectrefill();
        }
        if(v==approve){

            if(!Varglobal.approvallist.isEmpty()){
                String approve=Varglobal.approvallist.substring(0,Varglobal.approvallist.length()-1);
                Approverefill(approve);
            }else{
                Toast.makeText(ApproveDetailrefill.this, "Not selected item", Toast.LENGTH_SHORT).show();
            }
//
        }
    }
}
