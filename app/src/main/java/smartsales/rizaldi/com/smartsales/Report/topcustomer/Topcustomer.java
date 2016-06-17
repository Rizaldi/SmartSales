package smartsales.rizaldi.com.smartsales.Report.topcustomer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
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

import smartsales.rizaldi.com.smartsales.Approval.AdapterModelSetup.ModelItinerary;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class Topcustomer extends AppCompatActivity {
    private List<ModelItinerary> listSales;
    private List<ModelProduct> listproduct;
    private List<ModelCustomerReport> listproductcustomer;
    Spinner spinsales, productname;
    String idsales, idproduct;
    SqliteHandler db;
    String organizationId;
    RecyclerView rv;
    RVAlistProduct adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topcustomer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        listSales = new ArrayList<>();
        listproduct = new ArrayList<>();
        listproductcustomer = new ArrayList<>();
        db = new SqliteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        organizationId = user.get("organization_id");
        setSupportActionBar(toolbar);
        spinsales = (Spinner) findViewById(R.id.salesspinner);
        productname = (Spinner) findViewById(R.id.productname);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setFocusable(false);

        getDataSales();
        getProductname(organizationId);
        selectSpinner();
        getDataList("", "");
    }

    private void selectSpinner() {
        spinsales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    idsales = ((TextView) view.findViewById(R.id.id_sales)).getText().toString();
                    if (!idsales.equals("-1")) {
                        getDataList("salesRep", idsales);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        productname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    idproduct = ((TextView) view.findViewById(R.id.id_customerlocation)).getText().toString();
                    if (!idproduct.equals("-1")) {
                        getDataList("product", idproduct);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getDataList(String key, String value) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_listopcustomer + "?" + key + "=" + value
                , new Response.Listener<String>() {
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
                        Toast.makeText(Topcustomer.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                listproductcustomer.clear();
                adapter = new RVAlistProduct(listproductcustomer, Topcustomer.this);
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
            ModelCustomerReport Items = new ModelCustomerReport();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Items.setCustomer(jsonObject.getString("customer_name"));
                Items.setAddress(jsonObject.getString("customer_address"));
                Items.setSales(jsonObject.getString("salesman"));
                Items.setTotal(jsonObject.getString("total_order"));
                Items.setTotal_amount(jsonObject.getString("total_amount"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && listproductcustomer.size() != 0) {
                listproductcustomer.clear();
                listproductcustomer.add(Items);
            } else if (i == 0) {
                listproductcustomer.add(Items);
            } else {
                listproductcustomer.add(Items);
            }
        }
        adapter = new RVAlistProduct(listproductcustomer, this);
        rv.setAdapter(adapter);
    }

    private void getDataSales() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_sales, new Response.Listener<String>() {
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

    private void placeholderSpinnerSales() {
        ModelItinerary placeholder = new ModelItinerary();
        placeholder.setName("Pilih Sales");
        placeholder.setId_name("-1");
        listSales.add(placeholder);
    }

    private void parseData(JSONArray array) {
        placeholderSpinnerSales();
        for (int i = 0; i < array.length(); i++) {
            ModelItinerary Items = new ModelItinerary();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setName(json.getString("text"));
                Items.setId_name(json.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listSales.add(Items);
        }
        spinsales.setAdapter(new MyAdapter(Topcustomer.this, R.layout.sales_spinner,
                listSales));
    }

    private void getProductname(final String organizationId) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_listprodct + organizationId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                Log.e("JSON", s);
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String data = jsonObject.getString("data");
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        jsonArray = new JSONArray(data);
                        parseDataProduct(jsonArray);
                    } else {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(Topcustomer.this, msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.e("error GC", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NetworkResponse networkResponse = volleyError.networkResponse;
                Toast.makeText(getApplicationContext(),
                        volleyError.getMessage(), Toast.LENGTH_LONG).show();
                listproduct.clear();
                AdapterProduct adapter = new AdapterProduct(Topcustomer.this, R.layout.customerlocation_spinner,
                        listproduct);
                productname.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loading.dismiss();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
        Log.e("e", strReq.toString());
    }

    private void parseDataProduct(JSONArray array) {
        int i;
        for (i = 0; i < array.length(); i++) {
            ModelProduct Items = new ModelProduct();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setId(json.getString("id"));
                Items.setNama(json.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && listproduct.size() != 0) {
                listproduct.clear();
                placeholderProduct();
                listproduct.add(Items);
            } else if (i == 0) {
                placeholderProduct();
                listproduct.add(Items);
            } else {
                listproduct.add(Items);
            }
        }
        AdapterProduct adapter = new AdapterProduct(Topcustomer.this, R.layout.customerlocation_spinner,
                listproduct);
        productname.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void placeholderProduct() {
        ModelProduct placeholder = new ModelProduct();
        placeholder.setNama("Pilih Product");
        placeholder.setId("-1");
        listproduct.add(placeholder);
    }

    public class AdapterProduct extends ArrayAdapter {
        List<ModelProduct> listProduct;

        public AdapterProduct(Context context, int resource, List<ModelProduct> listProduct) {
            super(context, resource, listProduct);
            this.listProduct = listProduct;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.customerlocation_spinner, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_customerlocation);
            id.setText(listProduct.get(position).getId());
            TextView tvcustomer = (TextView) layout
                    .findViewById(R.id.customerlocation);
            tvcustomer.setText(listProduct.get(position).getNama());
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

    public class MyAdapter extends ArrayAdapter {
        List<ModelItinerary> listSales;

        public MyAdapter(Context context, int resource, List<ModelItinerary> listSales) {
            super(context, resource, listSales);
            this.listSales = listSales;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.sales_spinner, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_sales);
            id.setText(listSales.get(position).getId_name());
            TextView tvsales = (TextView) layout
                    .findViewById(R.id.nama_sales);
            tvsales.setText(listSales.get(position).getName());
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
