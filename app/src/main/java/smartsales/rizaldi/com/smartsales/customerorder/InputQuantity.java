package smartsales.rizaldi.com.smartsales.customerorder;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class InputQuantity extends AppCompatActivity {
    TextView id, productname, productprice;
    EditText qty;
    Spinner uom;
    List<ModelUom> uomList;
    String organizationId = "", hargauom = "", uomId = "", productId = "",position_status = "", username = "", idSO = "";
    SqliteHandler db;
    ImageButton ibsave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_quantity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputQuantity.this, BrowseProduct.class));
                finish();
            }
        });
        uomList = new ArrayList<>();
        db = new SqliteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        organizationId = user.get("organization_id");
//        warehouseId = user.get("warehouse_id");
        position_status = user.get("position_status");
        username = user.get("username");

        initialVariable();
        try {
            Bundle b = getIntent().getExtras();
            productId = b.getString("id");
        } catch (Exception e) {
        }
        getData();
        selectUom();
        ibsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!qty.getText().toString().isEmpty()) {
                    saveData(organizationId, ParamInput.idcustomer, productId,
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
                        ParamInput.idSO=idSO;
                        ParamInput.anydata=true;
                        String msg = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        Intent i=new Intent(InputQuantity.this,OrderedProductList.class);
                        Bundle b=new Bundle();
                        b.putString("idso",idSO);
                        i.putExtras(b);
                        startActivity(i);
                        finish();
                    }else if(code.equals("0")) {
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

    private void selectUom() {
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
    }

    private void getData() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_getinputso + organizationId +
                "&customerId=" + ParamInput.idcustomer + "&productId=" + productId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                Log.e("cekkkk", s);
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        String data = jsonObject.getString("data");
                        jsonArray = new JSONArray(data);
                        parseData(jsonArray);
                    } else {
                        Toast.makeText(InputQuantity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void parseData(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                productname.setText(jsonObject.getString("product_name"));
                JSONArray jArray = new JSONArray(jsonObject.getString("uom_list"));
                Log.e("dataaa", jsonObject.getString("uom_list"));
                for (int j = 0; j < jArray.length(); j++) {
                    ModelUom Item = new ModelUom();
                    JSONObject jObj = (JSONObject) jArray.get(j);
                    Item.setId(jObj.getString("uom_id"));
                    Item.setNama(jObj.getString("uom_name"));
                    Item.setHarga(jObj.getString("uom_price"));
                    uomList.add(Item);
                    Toast.makeText(InputQuantity.this, uomList.get(0).getNama(), Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        uom.setAdapter(new MyAdapter(InputQuantity.this, R.layout.spinner_uom,
                uomList));

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

    private void initialVariable() {
        id = (TextView) findViewById(R.id.id);
        productname = (TextView) findViewById(R.id.productname);
        productprice = (TextView) findViewById(R.id.productprice);
        qty = (EditText) findViewById(R.id.qty);
        ibsave = (ImageButton) findViewById(R.id.ibsave);
    }

}
