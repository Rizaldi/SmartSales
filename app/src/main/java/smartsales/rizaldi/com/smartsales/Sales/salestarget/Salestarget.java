package smartsales.rizaldi.com.smartsales.Sales.salestarget;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import smartsales.rizaldi.com.smartsales.Approval.approvalso.ModelSalesRep;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Sales.Sales;
import smartsales.rizaldi.com.smartsales.Sales.competitor.CompetitorActivity;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.customerorder.ModelBrand;
import smartsales.rizaldi.com.smartsales.customerorder.ModelCategory;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class Salestarget extends AppCompatActivity {
    Spinner salesrep,brandname,month;
    RecyclerView rv;
    private List<Modelsalesrep> listsales;
    private List<Modelbrand> listbrand;
    private List<ModelMonth> listmonth;
    private List<ModelSalestarget> listtarget;
    RVAadapter adapter;
    String id_sales="";
    private String organizationId="";
    SqliteHandler db;
    private String id_brand="";
    private String id_month="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salestarget);
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
        listsales=new ArrayList<>();
        listbrand=new ArrayList<>();
        listmonth=new ArrayList<>();
        listtarget=new ArrayList<>();
        db=new SqliteHandler(this);
        HashMap<String,String> user=db.getUserDetails();
        organizationId=user.get("organization_id");
        getsales();
        getcategory();
        getbrand();
        getbrandname();
        getMonth();
        getselectmonth();


    }

    private void initialUI() {
        salesrep=(Spinner) findViewById(R.id.salesrep);
        brandname=(Spinner) findViewById(R.id.brandname);
        month=(Spinner) findViewById(R.id.month);
        rv=(RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setFocusable(false);
    }

//    get list target
private void getDataTarget(String idmonth,String key, String value) {
    final ProgressDialog loading = new ProgressDialog(this);
    loading.setMessage("please wait...");
    loading.setCancelable(false);
    loading.show();
    StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_getsalestarget +idmonth+ "&" +
            key + "=" + value, new Response.Listener<String>() {
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
                    Toast.makeText(Salestarget.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
            listtarget.clear();
            adapter = new RVAadapter(listtarget, Salestarget.this);
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
            ModelSalestarget Items = new ModelSalestarget();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Items.setBrand(jsonObject.getString("brand"));
                Items.setProductname(jsonObject.getString("product_name"));
                Items.setSalesrep(jsonObject.getString("sales_rep"));
                Items.setTarget(jsonObject.getString("target"));
                Items.setAchieved(jsonObject.getString("achieved"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && listtarget.size() != 0) {
                listtarget.clear();
                listtarget.add(Items);
            } else if (i == 0) {
                listtarget.add(Items);
            } else {
                listtarget.add(Items);
            }
        }
        adapter = new RVAadapter(listtarget, this);
        rv.setAdapter(adapter);
    }
    //    category
    private void getsales() {
        final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_targetsalesrep, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        String data = jsonObject.getString("data");
                        jsonArray = new JSONArray(data);
                        parseData(jsonArray);
                    } else {
                        Toast.makeText(Salestarget.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void placeholderCategory() {
        Modelsalesrep placeholder = new Modelsalesrep();
        placeholder.setText("Chose Sales Representative");
        placeholder.setId("-1");
        listsales.add(placeholder);
    }

    private void parseData(JSONArray array) {
        placeholderCategory();
        for (int i = 0; i < array.length(); i++) {
            Modelsalesrep Items = new Modelsalesrep();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setText(json.getString("text"));
                Items.setId(json.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listsales.add(Items);
        }
        salesrep.setAdapter(new SalesAdapter(Salestarget.this, R.layout.customer_category,
                listsales));
    }
    private void getcategory() {
        salesrep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_sales = ((TextView) view.findViewById(R.id.id_category)).getText().toString();
                if (!id_sales.equals("-1")) {
                    getDataTarget(id_month,"salesRepID",id_sales);
//                    getDataCustomer("customerCategory", idcategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public class SalesAdapter extends ArrayAdapter {
        List<Modelsalesrep> listsales;

        public SalesAdapter(Context context, int resource, List<Modelsalesrep> listsales) {
            super(context, resource, listsales);
            this.listsales = listsales;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.customer_category, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_category);
            id.setText(listsales.get(position).getId());
            TextView tvcustomer = (TextView) layout
                    .findViewById(R.id.nama_category);
            tvcustomer.setText(listsales.get(position).getText());
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
//    get brand
private void getbrand() {
    final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_brand+organizationId, new Response.Listener<String>() {
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
                    Toast.makeText(Salestarget.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void placeholderBrand() {
        Modelbrand placeholder = new Modelbrand();
        placeholder.setNama("Chose Brand Name");
        placeholder.setId("-1");
        listbrand.add(placeholder);
    }

    private void parseDataBrand(JSONArray array) {
        placeholderBrand();
        for (int i = 0; i < array.length(); i++) {
            Modelbrand Items = new Modelbrand();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setNama(json.getString("name"));
                Items.setId(json.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listbrand.add(Items);
        }
        brandname.setAdapter(new BrandAdapter(Salestarget.this, R.layout.customer_category,
                listbrand));
    }
    private void getbrandname() {
        brandname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_brand = ((TextView) view.findViewById(R.id.id_category)).getText().toString();
                if (!id_brand.equals("-1")) {
                    getDataTarget(id_month,"brandId",id_brand);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public class BrandAdapter extends ArrayAdapter {
        List<Modelbrand> listbrand;

        public BrandAdapter(Context context, int resource, List<Modelbrand> listbrand) {
            super(context, resource, listbrand);
            this.listbrand = listbrand;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.customer_category, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_category);
            id.setText(listbrand.get(position).getId());
            TextView tvcustomer = (TextView) layout
                    .findViewById(R.id.nama_category);
            tvcustomer.setText(listbrand.get(position).getNama());
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

    public void getMonth(){
        String[] bulan={"January","February","March","April","May","June","July","August","September","October","November","December"};
        for (int i=0;i<bulan.length;i++){
            ModelMonth items=new ModelMonth();
            items.setId(String.valueOf(i+1));
            items.setMonth(bulan[i]);
            listmonth.add(items);
        }
        month.setAdapter(new MonthAdapter(Salestarget.this, R.layout.customer_category,
                listmonth));
    }
    private void getselectmonth() {
        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_month = ((TextView) view.findViewById(R.id.id_category)).getText().toString();
                if (!id_month.equals("-1")) {
                    getDataTarget(id_month,"", "");
//                    getDataCustomer("customerCategory", idcategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class MonthAdapter extends ArrayAdapter {
        List<ModelMonth> listmonth;

        public MonthAdapter(Context context, int resource, List<ModelMonth> listmonth) {
            super(context, resource, listmonth);
            this.listmonth = listmonth;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.customer_category, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_category);
            id.setText(listmonth.get(position).getId());
            TextView tvcustomer = (TextView) layout
                    .findViewById(R.id.nama_category);
            tvcustomer.setText(listmonth.get(position).getMonth());
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
