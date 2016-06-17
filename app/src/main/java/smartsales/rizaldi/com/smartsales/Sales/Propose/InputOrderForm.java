package smartsales.rizaldi.com.smartsales.Sales.Propose;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import smartsales.rizaldi.com.smartsales.customerorder.ModelUom;
import smartsales.rizaldi.com.smartsales.customerorder.ParamInput;
import smartsales.rizaldi.com.smartsales.salesform.ListSalesForm;
import smartsales.rizaldi.com.smartsales.salesform.RVAsalesForm;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class InputOrderForm extends AppCompatActivity implements View.OnClickListener {
    TextView id, productname, productprice;
    EditText qty;
    Spinner uom;
    String organizationId = "", warehouseId = "", hargauom = "", uomId = "", position_status = "", username = "", idSO = "";
    SqliteHandler db;
    List<ModelUom> uomList;
    ImageButton ibsave;
    RVAsalesForm adapter;
    RecyclerView rv;
    List<ListSalesForm> listproduct;
    LinearLayout inputquantyty, salesform;
    ImageButton remove, add, payment;
    double Total = 0;
    String amounttotal = "";
    static TextView totalharga;
    public static TextView getTextView(){
        return totalharga;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_order_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputOrderForm.this, BrowseProduct.class));
                finish();
            }
        });
        uomList = new ArrayList<>();
        listproduct = new ArrayList<>();
        db = new SqliteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        organizationId = user.get("organization_id");
        warehouseId = user.get("warehouse_id");
        position_status = user.get("position_status");
        username = user.get("username");
        initialVariable();
        try{
            Bundle b = getIntent().getExtras();
            id.setText(b.getString("id"));
            productname.setText(b.getString("product"));
            productprice.setText(b.getString("price"));
            getDataUom("productId", b.getString("id"));

        }catch (Exception e){

        }
        ibsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), ParamInput.idSO, Toast.LENGTH_LONG).show();
                if (!qty.getText().toString().isEmpty()) {
                    saveData(organizationId, ParamInput.idcustomer, id.getText().toString(),
                            uomId, productprice.getText().toString(), qty.getText().toString(),
                            position_status, username);
                } else {
                    Snackbar.make(v, "input Qty", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    private void saveData(final String organizationIdparam, final String idcustomerparam, final String idparam, final String uomIdparam, final String priceparam,
                          final String qtyparam, final String position_statusparam, final String usernameparam) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST, UrlLib.url_addSalesOrder, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                Log.e("JSON", s);
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        idSO = jsonObject.getString("idSO");
                        String data = jsonObject.getString("data");
                        jsonArray = new JSONArray(data);
                        setTitle("Sales Form");
                        inputquantyty.setVisibility(View.GONE);
                        salesform.setVisibility(View.VISIBLE);
                        parseDataCustomer(jsonArray);
                    }
                    if(code.equals("0")) {
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
                params.put("organizationId", organizationIdparam);
                params.put("customerId", idcustomerparam);
                params.put("productId", idparam);
                params.put("uomId", uomIdparam);
                params.put("harga", priceparam);
                params.put("qty", qtyparam);
                params.put("positionStatus", position_statusparam);
                params.put("username", usernameparam);
                if (!ParamInput.idSO.isEmpty()) {
                    params.put("idSO", ParamInput.idSO);
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);

    }

    private void parseDataCustomer(JSONArray array) {
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
                        Items.setDiscount1(jdiscount.getString("discount_amount"));
                    }
                    if (j == 1) {
                        JSONObject jdiscount = jsonArray.getJSONObject(1);
                        Items.setName2(jdiscount.getString("discount_name"));
                        Items.setDiscount2(jdiscount.getString("discount_amount"));
                    }
                    if (j == 2) {
                        JSONObject jdiscount = jsonArray.getJSONObject(2);
                        Items.setName3(jdiscount.getString("discount_name"));
                        Items.setDiscount3(jdiscount.getString("discount_amount"));
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
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
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

    private void deleteData(final String id,final  String idso,final String organizationId,final String customerId){
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
                        String data = jsonObject.getString("data");
                        jsonArray = new JSONArray(data);
                        setTitle("Sales Form");
                        inputquantyty.setVisibility(View.GONE);
                        salesform.setVisibility(View.VISIBLE);
                        DataAfterDelete(jsonArray);
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
                        Items.setDiscount1(jdiscount.getString("discount_amount"));
                    }
                    if (j == 1) {
                        JSONObject jdiscount = jsonArray.getJSONObject(1);
                        Items.setName2(jdiscount.getString("discount_name"));
                        Items.setDiscount2(jdiscount.getString("discount_amount"));
                    }
                    if (j == 2) {
                        JSONObject jdiscount = jsonArray.getJSONObject(2);
                        Items.setName3(jdiscount.getString("discount_name"));
                        Items.setDiscount3(jdiscount.getString("discount_amount"));
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
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
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

    private void initialVariable() {
        totalharga = (TextView) findViewById(R.id.totalharga);
        inputquantyty = (LinearLayout) findViewById(R.id.inputquantyty);
        salesform = (LinearLayout) findViewById(R.id.salesform);
        remove = (ImageButton) findViewById(R.id.remove);
        add = (ImageButton) findViewById(R.id.add);
        payment = (ImageButton) findViewById(R.id.payment);
        remove.setOnClickListener(this);
        add.setOnClickListener(this);
        payment.setOnClickListener(this);
        id = (TextView) findViewById(R.id.id);
        productname = (TextView) findViewById(R.id.productname);
        productprice = (TextView) findViewById(R.id.productprice);
        qty = (EditText) findViewById(R.id.qty);
        ibsave = (ImageButton) findViewById(R.id.ibsave);

        uom = (Spinner) findViewById(R.id.uom);
        uom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    uomId = ((TextView) view.findViewById(R.id.id)).getText().toString();
                    hargauom = ((TextView) view.findViewById(R.id.harga)).getText().toString();
                    productprice.setText(hargauom);
                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    private void getDataUom(String paramkey, String paramValue) {
        final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_listdataproduct +
                organizationId + "&warehouseId=" + warehouseId + "&customerId=" + ParamInput.idcustomer
                + "&" + paramkey + "=" + paramValue, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        String data = jsonObject.getString("data");
                        jsonArray = new JSONArray(data);
                        parseDataBrand(jsonArray);
                    } else {
                        Toast.makeText(InputOrderForm.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }
//    private void placeholderBrand() {
//        ModelBrand Placeholder = new ModelBrand();
//        Placeholder.setNama("Pilih Brand");
//        Placeholder.setId("-1");
//        brandList.add(Placeholder);
//    }

    private void parseDataBrand(JSONArray array) {
//        placeholder();
        for (int i = 0; i < array.length(); i++) {
            ModelUom Items = new ModelUom();
            JSONObject json = null;
            JSONArray jsonArray = null;
            JSONObject jobj = null;
            try {
                json = array.getJSONObject(i);
                jsonArray = new JSONArray(json.getString("stok"));
                for (int j = 0; j < jsonArray.length(); j++) {
                    jobj = jsonArray.getJSONObject(j);
                    Items.setId(jobj.getString("uom_id"));
                    Items.setNama(jobj.getString("name"));
                    Items.setHarga(jobj.getString("harga_uom"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            uomList.add(Items);
        }
        uom.setAdapter(new MyAdapter(InputOrderForm.this, R.layout.spinner_uom,
                uomList));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remove:
                ParamInput.idSO = "";
                ParamInput.idcustomer = "";
                ParamInput.customerLocationId = "";

                startActivity(new Intent(InputOrderForm.this, ProposeSalesOrder.class));
                finish();
                break;
            case R.id.add:
                ParamInput.idSO = idSO;
                startActivity(new Intent(InputOrderForm.this, BrowseProduct.class));
                finish();
                break;
            case R.id.payment:
                Bundle b = new Bundle();
                b.putString("total", String.valueOf(Total));
                b.putString("idSo", idSO);
                Intent i = new Intent(InputOrderForm.this, Payment.class);
                i.putExtras(b);
                startActivity(i);
                finish();
                break;
        }
    }

    public class MyAdapter extends ArrayAdapter {
        List<ModelUom> listUom;

        public MyAdapter(Context context, int resource, List<ModelUom> listUom) {
            super(context, resource, listUom);
            this.listUom = listUom;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.spinner_uom, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id);
            id.setText(listUom.get(position).getId());
            TextView tvsales = (TextView) layout
                    .findViewById(R.id.nama);
            tvsales.setText(listUom.get(position).getNama());
            TextView tvharga = (TextView) layout
                    .findViewById(R.id.harga);
            tvharga.setText(listUom.get(position).getHarga());
            return layout;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
    }

}
