package smartsales.rizaldi.com.smartsales.Report.customeraging;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SearchView;
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

import smartsales.rizaldi.com.smartsales.Approval.AdapterModelSetup.ModelItinerary;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class CustomerAging extends AppCompatActivity {
    private List<ModelItinerary> listSales;
    private List<ModelList> list;
    Spinner spinsales;
    RecyclerView rv;
    String idsales = "", positionstatus = "";
    SqliteHandler db;
    CardView manager;
    private String employeeId = "";
    RVAadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_aging);
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
        listSales = new ArrayList<>();
        list=new ArrayList<>();
        initialUi();
        db = new SqliteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        positionstatus = user.get("position_status");
        if (positionstatus.equals("1")) {
            manager.setVisibility(View.GONE);
            employeeId = user.get("employee_id");
        } else {
            getDataSales();
            selectSpinner();
        }
        getDataList("","");
        SearchView search = (SearchView) findViewById(R.id.search);

        search.setOnQueryTextListener(listener);
    }
    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String query) {
            query = query.toLowerCase();

            final List<ModelList> filteredList = new ArrayList<>();

            for (int i = 0; i < list.size(); i++) {

                final String text = list.get(i).getCustomer_name().toLowerCase();

                if (text.contains(query)) {
                    filteredList.add(list.get(i));
                }
            }

            rv.setLayoutManager(new LinearLayoutManager(CustomerAging.this));
            adapter = new RVAadapter(filteredList, CustomerAging.this);
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();  // data set changed
//            getDataHistory("customerName", query);
            return true;

        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    };


    private void getDataList(String key, String value) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_customeraging +positionstatus+
                "&employeeId=" +employeeId+"&"+
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
                        Toast.makeText(CustomerAging.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                adapter = new RVAadapter(list, CustomerAging.this);
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
            ModelList Items = new ModelList();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Items.setCustomer_id(jsonObject.getString("customer_id"));
                Items.setCustomer_name(jsonObject.getString("customer_name"));
                Items.setSalesman(jsonObject.getString("salesman"));
                Items.setTotalAmount(jsonObject.getString("totalAmount"));
                Items.setCurrentMonth(jsonObject.getString("currentMonth"));
                Items.setLastMonth(jsonObject.getString("lastMonth"));
                Items.setLast2Month(jsonObject.getString("last2Month"));
                Items.setLast3Month(jsonObject.getString("last3Month"));
                Items.setLast4Month(jsonObject.getString("last4Month"));
                Items.setLast5Month(jsonObject.getString("last5Month"));
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
        adapter = new RVAadapter(list, this);
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
        placeholder.setName("choose Sales");
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
        spinsales.setAdapter(new MyAdapter(CustomerAging.this, R.layout.sales_spinner,
                listSales));
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

    private void initialUi() {
        spinsales = (Spinner) findViewById(R.id.salesrep);
        manager = (CardView) findViewById(R.id.manager);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setFocusable(false);


    }

    private void selectSpinner() {
        spinsales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    idsales = ((TextView) view.findViewById(R.id.id_sales)).getText().toString();
                    if (!idsales.equals("-1")) {
                        getDataList("salesId",idsales);
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


}
