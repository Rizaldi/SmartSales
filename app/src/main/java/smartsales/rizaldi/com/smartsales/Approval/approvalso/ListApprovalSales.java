package smartsales.rizaldi.com.smartsales.Approval.approvalso;

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
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class ListApprovalSales extends AppCompatActivity implements View.OnClickListener {
    List<ModelProductList> list;
    SqliteHandler db;
    String positionStatus, username, warehouseId;
    public static String idSO="";
    RVAApprovalproduct adapter;
    RecyclerView rv;
    ImageButton approve, reject, close;
    String url="",userId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_approval_sales);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListApprovalSales.this,ApprovalSo.class));
                finish();
            }
        });
        list = new ArrayList<>();
        db = new SqliteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        positionStatus = user.get("position_status");
        username = user.get("username");
        warehouseId = user.get("warehouse_id");
        userId=user.get("user_id");
        Bundle b = getIntent().getExtras();
        idSO = b.getString("idSO");
        initial();
        getDataProductlist();

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

    private void getDataProductlist() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_listapprovalproduct + idSO
                + "&positionStatus=" + positionStatus + "&username=" + username + "&warehouseId=" + warehouseId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                        Log.e("JSON", s);
                        JSONArray jsonArray = null;
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String code = jsonObject.getString("code");
                            if (code.equals("1")) {
                                String data = jsonObject.getString("data");
                                jsonArray = new JSONArray(data);
                                parseDataList(jsonArray);
                            } else {
                                Toast.makeText(ListApprovalSales.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                adapter = new RVAApprovalproduct(list, ListApprovalSales.this);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                loading.dismiss();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
        Log.e("tesda", strReq.toString());
    }


    private void parseDataList(JSONArray jsonArray) {
        int i;
        for (i = 0; i < jsonArray.length(); i++) {
            ModelProductList Items = new ModelProductList();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Items.setId(jsonObject.getString("id"));
                Items.setProductname(jsonObject.getString("name"));
                Items.setOrderedqty(jsonObject.getString("ordered_quantity"));
                Items.setUom(jsonObject.getString("uom_name"));
                Items.setUnitprice(jsonObject.getString("harga"));
                Items.setTaxcode(jsonObject.getString("tax_code"));
                Items.setNetamount(jsonObject.getString("line_net_amount"));
                Items.setTax(jsonObject.getString("tax_amount"));
                JSONArray jdiskon = new JSONArray(jsonObject.getString("discount"));
                String diskon = "(";
                for (int j = 0; j < jdiskon.length(); j++) {
                    JSONObject jobjdiskon = (JSONObject) jdiskon.get(j);
                    diskon = diskon + jobjdiskon.getString("discount_amount") + "|";
                }

                if (diskon.endsWith("|")) {
                    diskon = diskon.substring(0, diskon.length() - 1);
                }

                Items.setDnp(diskon + ")");
                Items.setWarehousestock(jsonObject.getString("stok"));
                Items.setTotal(jsonObject.getString("total_amount"));
                Items.setApprovedqty(jsonObject.getString("approved_quantity"));
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
        adapter = new RVAApprovalproduct(list, this);
        rv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.approve:
                Toast.makeText(ListApprovalSales.this, positionStatus, Toast.LENGTH_SHORT).show();
                url=UrlLib.url_approveso;
                approveRejectSo();
                break;
            case R.id.reject:
                url=UrlLib.url_rejectso;
                approveRejectSo();

                break;
            case R.id.close:
                startActivity(new Intent(ListApprovalSales.this,ApprovalSo.class));
                finish();
                break;
        }
    }

    private void approveRejectSo() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("JSON",s);
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if(code.equals("1")){
                        Toast.makeText(ListApprovalSales.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ListApprovalSales.this,ApprovalSo.class));
                        finish();
                    }else{
                        Toast.makeText(ListApprovalSales.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("idSO", idSO);
                params.put("positionStatus", positionStatus);
                params.put("username", username);
                params.put("userId",userId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
        Log.e("sss",strReq.toString());
    }
}
