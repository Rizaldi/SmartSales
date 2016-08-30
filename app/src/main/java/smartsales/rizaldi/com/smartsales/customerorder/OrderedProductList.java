package smartsales.rizaldi.com.smartsales.customerorder;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.salesform.ListSalesForm;
import smartsales.rizaldi.com.smartsales.salesform.RVAsalesForm;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class OrderedProductList extends AppCompatActivity implements View.OnClickListener {
    RVAsalesForm adapter;
    RecyclerView rv;
    List<ListSalesForm> listproduct;
    ImageButton remove, add, payment;
    double Total = 0;
    String amounttotal = "", idSO = "";
    static TextView totalharga;

    public static TextView getTextView() {
        return totalharga;
    }

    SqliteHandler db;
    String organizationId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_product_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderedProductList.this, BrowseProduct.class));
                finish();
            }
        });
        Bundle b = getIntent().getExtras();
        idSO = b.getString("idso");
        listproduct = new ArrayList<>();
        db = new SqliteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        organizationId = user.get("organization_id");
        initialUI();
        getDataList();
    }

    private void getDataList() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST, UrlLib.url_orderlist + organizationId
                + "&customerId=" + ParamInput.idcustomer + "&idSO=" + idSO, new Response.Listener<String>() {
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
                        parseDataCustomer(jsonArray);
                    } else if (code.equals("0")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e("error GC", e.getMessage());
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

    private void parseDataCustomer(JSONArray jsonArray) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        for (int i = 0; i < jsonArray.length(); i++) {
            ListSalesForm Items = new ListSalesForm();
            JSONObject json = null;
            try {
                json = jsonArray.getJSONObject(i);
                Items.setId(json.getString("id"));
                Items.setName(json.getString("name"));
                Items.setQuantyty(json.getString("ordered_quantity"));
                Items.setUomname(json.getString("uom_name"));
                Items.setHarga(json.getString("harga"));
                Items.setTax_code(json.getString("tax_code"));
                Items.setSubtotal(json.getString("line_net_amount"));
                Items.setTax(json.getString("tax_amount"));
                JSONArray jArray = new JSONArray(json.getString("discount"));
                Log.e("datadiscount", json.getString("discount"));
                for (int j = 0; j < jArray.length(); j++) {
                    if (j == 0) {
                        JSONObject jdiscount = jsonArray.getJSONObject(0);
                        Items.setName1(jdiscount.getString("discount_name"));
                        Items.setDiscount1(nf.format(Double.parseDouble(jdiscount.getString("discount_amount"))));
                    }
                    if (j == 1) {
                        JSONObject jdiscount = jsonArray.getJSONObject(1);
                        Items.setName2(jdiscount.getString("discount_name"));
                        Items.setDiscount2(nf.format(Double.parseDouble(jdiscount.getString("discount_amount"))));
                    }
                    if (j == 2) {
                        JSONObject jdiscount = jsonArray.getJSONObject(2);
                        Items.setName3(jdiscount.getString("discount_name"));
                        Items.setDiscount3(nf.format(Double.parseDouble(jdiscount.getString("discount_amount"))));
                    }
                }
                Items.setTotal_amount(json.getString("total_amount"));
                amounttotal = json.getString("total_amount");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Total = Total + Double.parseDouble(amounttotal);
            if (i == 0 && listproduct.size() > 0) {
                listproduct.clear();
                listproduct.add(Items);
            } else {
                listproduct.add(Items);
            }
//            listproduct.add(Items);
        }
//        NumberFormat nf = NumberFormat.getInstance();
//        nf.setMaximumFractionDigits(2);
        ParamInput.TotalHarga = Total;
        totalharga.setText(String.valueOf(nf.format(Total)));
        adapter = new RVAsalesForm(listproduct, this, new RVAsalesForm.BtnClickListener() {
            @Override
            public void onBtnClick(int position) {
                deleteData(adapter.idDelete, ParamInput.idSO, organizationId, ParamInput.idcustomer);
            }
        });
        ParamInput.idSO = idSO;
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void deleteData(final String id, final String idso, final String organizationId, final String customerId) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST, UrlLib.url_deleteSalesOrder, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                Log.e("JSON", s);
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        String idSO = jsonObject.getString("idSO");
                        String msg = jsonObject.getString("message");
                        AlertDialog.Builder builder=new AlertDialog.Builder(OrderedProductList.this);
                        builder.setTitle("Message").setMessage(msg).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i=new Intent(OrderedProductList.this,OrderedProductList.class);
                                Bundle b=new Bundle();
                                b.putString("idso",ParamInput.idSO);
                                i.putExtras(b);
                                startActivity(i);
                                finish();
                            }
                        });
                        builder.show();
                    } else {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e("error GC", e.getMessage());
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("organizationId", organizationId);
                params.put("customerId", customerId);
                params.put("idSO", idso);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);

    }
    private void DataAfterDelete(JSONArray array) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        for (int i = 0; i < array.length(); i++) {
            ListSalesForm Items = new ListSalesForm();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setId(json.getString("id"));
                Items.setName(json.getString("name"));
                Items.setQuantyty(json.getString("ordered_quantity"));
                Items.setUomname(json.getString("uom_name"));
                Items.setHarga(json.getString("harga"));
                Items.setTax_code(json.getString("tax_code"));
                Items.setSubtotal(json.getString("line_net_amount"));
                Items.setTax(json.getString("tax_amount"));
                JSONArray jsonArray = new JSONArray(json.getString("discount"));
                Log.e("datadiscount",json.getString("discount"));
                for (int j = 0; j < jsonArray.length(); j++) {
                    if (j == 0) {
                        JSONObject jdiscount = jsonArray.getJSONObject(0);
                        Items.setName1(jdiscount.getString("discount_name"));
                        Items.setDiscount1(nf.format(Double.parseDouble(jdiscount.getString("discount_amount"))));
                    }
                    if (j == 1) {
                        JSONObject jdiscount = jsonArray.getJSONObject(1);
                        Items.setName2(jdiscount.getString("discount_name"));
                        Items.setDiscount2(nf.format(Double.parseDouble(jdiscount.getString("discount_amount"))));
                    }
                    if (j == 2) {
                        JSONObject jdiscount = jsonArray.getJSONObject(2);
                        Items.setName3(jdiscount.getString("discount_name"));
                        Items.setDiscount3(nf.format(Double.parseDouble(jdiscount.getString("discount_amount"))));
                    }
                }
                Items.setTotal_amount(json.getString("total_amount"));
                amounttotal = json.getString("total_amount");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Total = Total + Double.parseDouble(amounttotal);
            if (i == 0 && listproduct.size() > 0) {
                listproduct.clear();
                listproduct.add(Items);
            } else {
                listproduct.add(Items);
            }
//            listproduct.add(Items);
        }

        ParamInput.TotalHarga= Total;
        totalharga.setText(String.valueOf(nf.format(Total)));
        adapter = new RVAsalesForm(listproduct, this, new RVAsalesForm.BtnClickListener() {
            @Override
            public void onBtnClick(int position) {
                deleteData(adapter.idDelete, ParamInput.idSO, organizationId, ParamInput.idcustomer);
            }
        });
        ParamInput.idSO = idSO;
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initialUI() {
        totalharga = (TextView) findViewById(R.id.totalharga);
        remove = (ImageButton) findViewById(R.id.remove);
        add = (ImageButton) findViewById(R.id.add);
        payment = (ImageButton) findViewById(R.id.payment);
        remove.setOnClickListener(this);
        add.setOnClickListener(this);
        payment.setOnClickListener(this);
        rv = (RecyclerView) findViewById(R.id.rvproduct);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remove:
                ParamInput.anydata=false;
                ParamInput.idSO = "";
                ParamInput.PoNumber = "";
                ParamInput.idcustomer = "";
                ParamInput.customerLocationId = "";
                ParamInput.deliveristatus = "";
                ParamInput.DeliveryDate = "";
                ParamInput.PoDate = "";
                startActivity(new Intent(OrderedProductList.this, C_Order_form.class));
                finish();
                break;
            case R.id.add:
                ParamInput.idSO = idSO;
                startActivity(new Intent(OrderedProductList.this, BrowseProduct.class));
                finish();
                break;
            case R.id.payment:
                Bundle b = new Bundle();
                b.putString("total", String.valueOf(Total));
                b.putString("idSo", idSO);
                Intent i = new Intent(OrderedProductList.this, Payment.class);
                i.putExtras(b);
                startActivity(i);
                finish();
                break;
        }

    }


}
