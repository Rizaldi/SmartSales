package smartsales.rizaldi.com.smartsales.Approval.goodreturn;

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

public class GoodreturnDetail extends AppCompatActivity implements View.OnClickListener {
    private List<Modeldetailgood> list;
    ImageButton approve, reject, close;
    RecyclerView rv;
    String idreturn = "";
    private String username = "", positionstatus = "";
    SqliteHandler db;
    RVAApprovalreturn adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodreturn_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GoodreturnDetail.this, Goodreturnapprove.class));
                finish();
            }
        });
        Varglobal.approvallist="";
        db = new SqliteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        username = user.get("username");
        positionstatus = user.get("position_status");
        Bundle b = getIntent().getExtras();
        idreturn = b.getString("id");
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
        getData(idreturn);

    }

    private void getData(String idreturn) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_listapprovalreturn + idreturn,
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
                                Toast.makeText(GoodreturnDetail.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                adapter = new RVAApprovalreturn(list, GoodreturnDetail.this);
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
            Modeldetailgood Items = new Modeldetailgood();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Items.setId(jsonObject.getString("id"));
                Items.setProduct_id(jsonObject.getString("product_id"));
                Items.setQty(jsonObject.getString("qty"));
                Items.setUomId(jsonObject.getString("uom_id"));
                Items.setCondition(jsonObject.getString("condition"));
                Items.setExpireddate(jsonObject.getString("expired_date"));
                Items.setStatus(jsonObject.getString("status"));
                Items.setReturnprice(jsonObject.getString("return_price"));
                Items.setTotalreturn(jsonObject.getString("total_return"));
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
        adapter = new RVAApprovalreturn(list, this);
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

    @Override
    public void onClick(View v) {
        if(v==close){
            startActivity(new Intent(GoodreturnDetail.this,Goodreturnapprove.class));
            finish();
        }
        if(v==reject){
            Rejectreturn();
        }
        if(v==approve){
            if(!Varglobal.approvallist.isEmpty()){
                String approve=Varglobal.approvallist.substring(0,Varglobal.approvallist.length()-1);
                Approvereturn(approve);
            }else{
                Toast.makeText(GoodreturnDetail.this, "Not selected item", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void Approvereturn(final String approve) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlLib.aprovereturn, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if(code.equals("1")){
                        Toast.makeText(GoodreturnDetail.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(GoodreturnDetail.this,Goodreturnapprove.class));
                        finish();
                    }else{
                        Toast.makeText(GoodreturnDetail.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("idReturn", idreturn);
                params.put("username", username);
                params.put("positionStatus", positionstatus);
                params.put("approvalList", approve);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void Rejectreturn() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlLib.rejectreturn, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("JSON", s);
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if(code.equals("1")){
                        Toast.makeText(GoodreturnDetail.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(GoodreturnDetail.this,Goodreturnapprove.class));
                        finish();
                    }else{
                        Toast.makeText(GoodreturnDetail.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("idReturn", idreturn);
                params.put("username", username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);

    }
}
