package smartsales.rizaldi.com.smartsales.Report.proposeso;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Sales.Propose.Propose;
import smartsales.rizaldi.com.smartsales.UrlLib;

public class ProposeSo extends AppCompatActivity {
    Spinner salesrep,customername,month,year;
    RecyclerView rv;
    private List<Modelsalesrep> listsales;
    private List<ModelCustomer> listcustomer;
    private List<ModelMonth> listmonth;
    private List<ModelYear> listyear;
    private List<ModelProposeList> listpropose;
    RVAadapter adapter;
    String id_sales="",id_customer="",id_month="",id_year="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propose_so);
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
        listcustomer=new ArrayList<>();
        listpropose=new ArrayList<>();
        listmonth=new ArrayList<>();
        listyear=new ArrayList<>();
        getsales();
        getcategory();
        getCustomer("");
        getcustomername();
        getMonth();
        getselectmonth();
        getYear();
        getselectyear();
        getData("","");
    }

    private void initialUI() {
        salesrep=(Spinner) findViewById(R.id.salesrep);
        customername=(Spinner)findViewById(R.id.customername);
        month=(Spinner)findViewById(R.id.month);
        year=(Spinner)findViewById(R.id.year);
        rv=(RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setFocusable(false);
    }

//    getdata
private void getData(String key, String value) {
    final ProgressDialog loading = new ProgressDialog(this);
    loading.setMessage("please wait...");
    loading.setCancelable(false);
    loading.show();
    StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_getproposereport +"?" +
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
                    Toast.makeText(ProposeSo.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
            listpropose.clear();
            adapter = new RVAadapter(listpropose, ProposeSo.this);
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
            ModelProposeList Items = new ModelProposeList();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Items.setId(jsonObject.getString("customer_id"));
                Items.setCustomername(jsonObject.getString("customer_name"));
                Items.setSales(jsonObject.getString("salesman"));
                Items.setProposedso(jsonObject.getString("proposed_so"));
                Items.setTotal(jsonObject.getString("total"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && listpropose.size() != 0) {
                listpropose.clear();
                listpropose.add(Items);
            } else if (i == 0) {
                listpropose.add(Items);
            } else {
                listpropose.add(Items);
            }
        }
        adapter = new RVAadapter(listpropose, this);
        rv.setAdapter(adapter);
    }

//    getsales
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
                    Toast.makeText(ProposeSo.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
        salesrep.setAdapter(new SalesAdapter(ProposeSo.this, R.layout.customer_category,
                listsales));
    }
    private void getcategory() {
        salesrep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_sales = ((TextView) view.findViewById(R.id.id_category)).getText().toString();
                if (!id_sales.equals("-1")) {
                    getCustomer(id_sales);
                    getData("salesRepID",id_sales);
                } else {
                    getCustomer("");
                    getData("","");

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
//customername
private void getCustomer(String value) {
    final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_customer+value, new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            JSONArray jsonArray = null;
            try {
                JSONObject jsonObject = new JSONObject(s);
                String code = jsonObject.getString("code");
                if (code.equals("1")) {
                    String data = jsonObject.getString("data");
                    jsonArray = new JSONArray(data);
                    parseDatacustomer(jsonArray);
                } else {
                    Toast.makeText(ProposeSo.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(getApplicationContext(),
                    "no data found", Toast.LENGTH_LONG).show();
            listcustomer.clear();
            customername.setAdapter(new CustomerAdapter(ProposeSo.this, R.layout.customer_category,
                    listcustomer));
        }
    }
    );
    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(strReq);
}

    private void placeholderCustomer() {
        ModelCustomer placeholder = new ModelCustomer();
        placeholder.setName("Chose Customer Name");
        placeholder.setId("-1");
        listcustomer.add(placeholder);
    }

    private void parseDatacustomer(JSONArray array) {

        int i;
        for (i = 0; i < array.length(); i++) {
            ModelCustomer Items = new ModelCustomer();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setName(json.getString("text"));
                Items.setId(json.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && listcustomer.size() != 0) {
                listcustomer.clear();
                placeholderCustomer();
                listcustomer.add(Items);
            } else if (i == 0) {
                placeholderCustomer();
                listcustomer.add(Items);
            } else {
                listcustomer.add(Items);
            }
        }
        customername.setAdapter(new CustomerAdapter(ProposeSo.this, R.layout.customer_category,
                listcustomer));
    }
    private void getcustomername() {
        customername.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_customer = ((TextView) view.findViewById(R.id.id_category)).getText().toString();
                if (!id_customer.equals("-1")) {
                    getData("customerId",id_customer);
                }else{
                    getData("","");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public class CustomerAdapter extends ArrayAdapter {
        List<ModelCustomer> listcustomer;

        public CustomerAdapter(Context context, int resource, List<ModelCustomer> listcustomer) {
            super(context, resource, listcustomer);
            this.listcustomer = listcustomer;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.customer_category, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_category);
            id.setText(listcustomer.get(position).getId());
            TextView tvcustomer = (TextView) layout
                    .findViewById(R.id.nama_category);
            tvcustomer.setText(listcustomer.get(position).getName());
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
//getmonth
private void placeholderMonth() {
    ModelMonth placeholder = new ModelMonth();
    placeholder.setMonth("Chose Month");
    placeholder.setId("-1");
    listmonth.add(placeholder);
}

    public void getMonth(){
    String[] bulan={"January","February","March","April","May","June","July","August","September","October","November","December"};
    placeholderMonth();
    for (int i=0;i<bulan.length;i++){
        ModelMonth items=new ModelMonth();
        items.setId(String.valueOf(i+1));
        items.setMonth(bulan[i]);
        listmonth.add(items);
    }
    month.setAdapter(new MonthAdapter(ProposeSo.this, R.layout.customer_category,
            listmonth));
}
    private void getselectmonth() {
        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_month = ((TextView) view.findViewById(R.id.id_category)).getText().toString();
                if (!id_month.equals("-1")) {
                    getData("month",id_month);
                }else{
                    getData("","");
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

//    getyear
private void placeholderYear() {
    ModelYear placeholder = new ModelYear();
    placeholder.setYear("Chose Year");
    placeholder.setId("-1");
    listyear.add(placeholder);
}

    public void getYear(){
    String[] tahun={"2012","2103","2014","2015","2016"};
    placeholderYear();
    for (int i=0;i<tahun.length;i++){
        ModelYear items=new ModelYear();
        items.setId(String.valueOf(i+1));
        items.setYear(tahun[i]);
        listyear.add(items);
    }
    year.setAdapter(new YearAdapter(ProposeSo.this, R.layout.customer_category,
            listyear));
}
    private void getselectyear() {
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              String namatahun="";
                id_year = ((TextView) view.findViewById(R.id.id_category)).getText().toString();
                namatahun = ((TextView) view.findViewById(R.id.nama_category)).getText().toString();
                if (!id_year.equals("-1")) {
                    getData("year",namatahun);
                }else{
                    getData("","");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class YearAdapter extends ArrayAdapter {
        List<ModelYear> listyear;

        public YearAdapter(Context context, int resource, List<ModelYear> listyear) {
            super(context, resource, listyear);
            this.listyear = listyear;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.customer_category, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_category);
            id.setText(listyear.get(position).getId());
            TextView tvcustomer = (TextView) layout
                    .findViewById(R.id.nama_category);
            tvcustomer.setText(listyear.get(position).getYear());
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
