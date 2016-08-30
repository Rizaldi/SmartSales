package smartsales.rizaldi.com.smartsales.Report.tracking;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartsales.rizaldi.com.smartsales.Approval.AdapterModelSetup.ModelItinerary;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Sales.ModelCustomerItinerary;
import smartsales.rizaldi.com.smartsales.UrlLib;

public class Tracking extends AppCompatActivity {
    private List<ModelItinerary> listSales;
    private List<ModelCustomerItinerary> listcustomer;
    private List<ModelTrackingList> list;
    Spinner spinsales,spincustomer;
    String idsales="-1",idcustomer="-1";
    TextView tvdate;
    int year_x, month_x, day_x;
    CardView card;
    static final int Dialog_Id = 0;
    public RecyclerView rv;
    RVAadapterTrack adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking2);
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
        setTitle("Tracking");
        listSales=new ArrayList<>();
        listcustomer=new ArrayList<>();
        list=new ArrayList<>();
        initialUI();
        getDataSales();
        getDataCustomer("");

//        calendar
        Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        showDialogClick();
    }

    //   start calender
    public void showDialogClick() {
        card = (CardView) findViewById(R.id.div);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Dialog_Id);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == Dialog_Id)
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear + 1;
            day_x = dayOfMonth;
            String bulan, tanggal;
            if (month_x < 10) {
                bulan = "0" + month_x;
            } else {
                bulan = String.valueOf(month_x);
            }
            if (day_x < 10) {
                tanggal = "0" + day_x;
            } else {
                tanggal = String.valueOf(day_x);
            }
            SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
            Date date = new Date(year, monthOfYear, dayOfMonth - 1);
//            String dayOfWeek = simpledateformat.format(date);
//            String month = new DateFormatSymbols().getMonths()[month_x - 1];
            tvdate.setText(year_x + "-" + bulan + "-" + tanggal);


        }
    };

//    stop calender

    private void initialUI() {
        tvdate= (TextView) findViewById(R.id.date);
        spinsales = (Spinner) findViewById(R.id.salesrep);
        spincustomer = (Spinner) findViewById(R.id.customer);
        spinsales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idsales = ((TextView) view.findViewById(R.id.id_sales)).getText().toString();
                try {
                    if (!idsales.equals("-1")) {
                        getDataCustomer(idsales);
                        getDataTracking("salesRepID",idsales);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
    }

    private void getDataTracking(String key, String value) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_tracking+"?"+key+"="+value, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String data = jsonObject.getString("data");
                    jsonArray = new JSONArray(data);
                    parseDataTracking(jsonArray);
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

    private void parseDataTracking(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            ModelTrackingList Items = new ModelTrackingList();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setId(json.getString("visit_id"));
                Items.setCustomer(json.getString("customer"));
                Items.setAddress(json.getString("customer_address"));
                Items.setDate(json.getString("visitdate"));
                Items.setTime(json.getString("visittime"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && list.size() > 0) {
                list.clear();
                list.add(Items);
            } else {
                list.add(Items);
            }
        }

        adapter = new RVAadapterTrack(list, this);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //TODO data sales
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
        spinsales.setAdapter(new MyAdapter(Tracking.this, R.layout.sales_spinner,
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

    //TODO data customer
    //get data customer
    private void getDataCustomer(final String idsales) {
        final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_customer + idsales, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String data = jsonObject.getString("data");
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        jsonArray = new JSONArray(data);
                        parseDataCustomer(jsonArray);
                    } else {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(Tracking.this, msg, Toast.LENGTH_SHORT).show();
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
                listcustomer.clear();
                CustomerAdapter adapter = new CustomerAdapter(Tracking.this, R.layout.customer_spinner,
                        listcustomer);
                spincustomer.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("salesId", idsales);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void parseDataCustomer(JSONArray array) {
        int i;
        for (i = 0; i < array.length(); i++) {
            ModelCustomerItinerary Items = new ModelCustomerItinerary();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setId_customer(json.getString("id"));
                Items.setNama_customer(json.getString("text"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && listcustomer.size() != 0) {
                listcustomer.clear();
                placeholderSpinner();
                listcustomer.add(Items);
            } else if (i == 0) {
                placeholderSpinner();
                listcustomer.add(Items);
            } else {
                listcustomer.add(Items);
            }
        }
        CustomerAdapter adapter = new CustomerAdapter(Tracking.this, R.layout.customer_spinner,
                listcustomer);
        spincustomer.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void placeholderSpinner() {
        ModelCustomerItinerary Placeholder = new ModelCustomerItinerary();
        Placeholder.setNama_customer("Choose Customer");
        Placeholder.setId_customer("-1");
        listcustomer.add(Placeholder);
    }

    //    adapater customer
    public class CustomerAdapter extends ArrayAdapter {
        List<ModelCustomerItinerary> listCustomer;

        public CustomerAdapter(Context context, int resource, List<ModelCustomerItinerary> listCustomer) {
            super(context, resource, listCustomer);
            this.listCustomer = listCustomer;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.customer_spinner, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_customer);
            id.setText(listCustomer.get(position).getId_customer());
            TextView tvcustomer = (TextView) layout
                    .findViewById(R.id.nama_customer);
            tvcustomer.setText(listCustomer.get(position).getNama_customer());
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
