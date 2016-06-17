package smartsales.rizaldi.com.smartsales.Approval;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
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
import smartsales.rizaldi.com.smartsales.Sales.ListCustomer;
import smartsales.rizaldi.com.smartsales.Sales.ModelCustomerItinerary;
import smartsales.rizaldi.com.smartsales.Sales.ModelCustomerLocation;
import smartsales.rizaldi.com.smartsales.Sales.RVAListCustomer;
import smartsales.rizaldi.com.smartsales.UrlLib;

public class Itinerary extends AppCompatActivity implements View.OnClickListener {
    private List<ModelItinerary> modelItineraries;
    private List<ModelCustomerItinerary> modelCustomerItinaries;
    private List<ModelCustomerLocation> mlocation;


    // statrt   kholil edit
    Spinner spinsales, spincustomer, spinlokasi;
    TextView tvdate, tgl_list;
    int year_x, month_x, day_x;
    String idsales = "-1", namasales = "", idcustomer = "-1", tgl = "", customerLocationId = "-1";
    CardView card;
    static final int Dialog_Id = 0;
    ImageButton ibsave;
    private List<ListCustomer> customerlist;
    RVAListCustomer adapter;
    public RecyclerView rv;
    public static String cek = "";

//    end kholil edit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itenerary);
        initialVariable();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        customerlist = new ArrayList<>();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle(" Sales REP Schedule");

//        kholil edit
        Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        modelItineraries = new ArrayList<>();
        modelCustomerItinaries = new ArrayList<>();
        mlocation = new ArrayList<>();
        getDataSales();
        showDialogClick();
        listCustomer();
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

    //    list customer
    private void dataListCustomer(String paramtgl, String paramidsales, final View v) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_listcustomer + paramtgl + "&salesId=" + paramidsales, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String data = jsonObject.getString("data");
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        jsonArray = new JSONArray(data);
                        parseDataListCustomer(jsonArray);
                    } else {
                        String msg = jsonObject.getString("message");
                        Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show();
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
                customerlist.clear();
                adapter = new RVAListCustomer(customerlist, Itinerary.this);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loading.dismiss();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
        Log.e("e", strReq.toString());
    }

    private void parseDataListCustomer(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            ListCustomer Items = new ListCustomer();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setId(json.getString("id"));
                Items.setName(json.getString("customer"));
                Items.setAddress(json.getString("customer_address"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && customerlist.size() > 0) {
                customerlist.clear();
                customerlist.add(Items);
            } else {
                customerlist.add(Items);
            }
        }

        adapter = new RVAListCustomer(customerlist, this);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void listCustomer() {
        rv = (RecyclerView) findViewById(R.id.rvcustomer);
        tgl_list = (TextView) findViewById(R.id.tgl_list);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setFocusable(false);
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
        modelItineraries.add(placeholder);
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
            modelItineraries.add(Items);
        }
        spinsales.setAdapter(new MyAdapter(Itinerary.this, R.layout.sales_spinner,
                modelItineraries));
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
            String dayOfWeek = simpledateformat.format(date);
            String month = new DateFormatSymbols().getMonths()[month_x - 1];
//            tvdate.setText(dayOfWeek + "," + day_x + " " + month + " " + year_x);
            tvdate.setText(year_x + "-" + bulan + "-" + tanggal);
            tgl_list.setText(dayOfWeek + "," + day_x + " " + month + " " + year_x);
            tgl_list.setTextColor(Color.parseColor("#000000"));
            tgl = year_x + "-" + bulan + "-" + tanggal;
            if (!tgl.isEmpty() && !idsales.isEmpty()) {
                dataListCustomer(tgl, idsales, view);
            }

        }
    };

//    stop calender


    private void initialVariable() {
        spinsales = (Spinner) findViewById(R.id.salesspinner);
        spinsales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idsales = ((TextView) view.findViewById(R.id.id_sales)).getText().toString();
                namasales = ((TextView) view.findViewById(R.id.nama_sales)).getText().toString();
                if (tgl.isEmpty() && !idsales.equals("-1")) {
                    tgl_list.setTextColor(Color.parseColor("#F44336"));
                    tgl_list.setText("You must choose a schedule date!");
                }
                try {
                    getDataCustomer(idsales, view);
                    if (!tgl.isEmpty() && !idsales.isEmpty()) {
                        dataListCustomer(tgl, idsales, view);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spincustomer = (Spinner) findViewById(R.id.spincustomername);
        spinlokasi = (Spinner) findViewById(R.id.customerlocation);
        spincustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    idcustomer = ((TextView) view.findViewById(R.id.id_customer)).getText().toString();
                    getDataCustomerLocation(idcustomer, view);
                } catch (Exception e) {
                    Log.e("cek id", "tidak ada data");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinlokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    customerLocationId = ((TextView) view.findViewById(R.id.id_customerlocation)).getText().toString();
                } catch (Exception e) {
                    Log.e("cek id", "tidak ada data");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvdate = (TextView) findViewById(R.id.date);
        ibsave = (ImageButton) findViewById(R.id.ibsave);
        ibsave.setOnClickListener(this);
    }

    //get data customer
    private void getDataCustomer(final String idsales, final View v) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_customer + idsales, new Response.Listener<String>() {
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
                        parseDataCustomer(jsonArray);
                    } else {
                        String msg = jsonObject.getString("message");
                        Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show();
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
                modelCustomerItinaries.clear();
                CustomerAdapter adapter = new CustomerAdapter(Itinerary.this, R.layout.customer_spinner,
                        modelCustomerItinaries);
                spincustomer.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loading.dismiss();
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
        Log.e("e", strReq.toString());
    }

    //    customer location
    private void getDataCustomerLocation(final String idcustomer, final View v) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_customerlocation + idcustomer, new Response.Listener<String>() {
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
                        parseDataCustomerLocation(jsonArray);
                    } else {
                        String msg = jsonObject.getString("message");
                        Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show();
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
                mlocation.clear();
                CustomerLocation adapter = new CustomerLocation(Itinerary.this, R.layout.customerlocation_spinner,
                        mlocation);
                spinlokasi.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loading.dismiss();
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
        Log.e("e", strReq.toString());
    }

    private void parseDataCustomerLocation(JSONArray array) {
        int i;
        for (i = 0; i < array.length(); i++) {
            ModelCustomerLocation Items = new ModelCustomerLocation();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setIdlokasi(json.getString("id"));
                Items.setLokasi(json.getString("address"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && mlocation.size() != 0) {
                mlocation.clear();
                placeholderLocation();
                mlocation.add(Items);
            } else if (i == 0) {
                placeholderLocation();
                mlocation.add(Items);
            } else {
                mlocation.add(Items);
            }
        }
        CustomerLocation adapter = new CustomerLocation(Itinerary.this, R.layout.customerlocation_spinner,
                mlocation);
        spinlokasi.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void placeholderLocation() {
        ModelCustomerLocation Placeholder = new ModelCustomerLocation();
        Placeholder.setLokasi("Pilih Lokasi");
        Placeholder.setIdlokasi("-1");
        mlocation.add(Placeholder);
    }

    private void placeholderSpinner() {
        ModelCustomerItinerary Placeholder = new ModelCustomerItinerary();
        Placeholder.setNama_customer("Pilih Customer");
        Placeholder.setId_customer("-1");
        modelCustomerItinaries.add(Placeholder);
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
            if (i == 0 && modelCustomerItinaries.size() != 0) {
                modelCustomerItinaries.clear();
                placeholderSpinner();
                modelCustomerItinaries.add(Items);
            } else if (i == 0) {
                placeholderSpinner();
                modelCustomerItinaries.add(Items);
            } else {
                modelCustomerItinaries.add(Items);
            }
        }
        CustomerAdapter adapter = new CustomerAdapter(Itinerary.this, R.layout.customer_spinner,
                modelCustomerItinaries);
        spincustomer.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibsave:
                saveDataItinerary(tgl, idsales, idcustomer, customerLocationId, v);
                break;
        }
    }

    //    save data itinerary
    private void saveDataItinerary(final String paramTgl, final String paramIdsales, String paramIdcustomer, String paramCustomerLocationId, final View v) {

        if (paramTgl.isEmpty() ||
                paramIdsales.isEmpty() ||
                paramIdsales == "-1" ||
                paramIdcustomer.isEmpty() ||
                paramIdcustomer == "-1" ||
                paramCustomerLocationId.isEmpty() ||
                paramCustomerLocationId == "-1"
                ) {
            Snackbar.make(v, "Maaf Data Tidak Boleh Kosong!", Snackbar.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();

        final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_save_itinary + "&tgl=" + paramTgl + "&salesId=" + paramIdsales
                + "&customerId=" + paramIdcustomer + "&customerLocationId=" + paramCustomerLocationId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                Log.e("JSON", s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("message");
                    if (code.equals("1")) {
                        Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show();
                        dataListCustomer(paramTgl, paramIdsales, v);
                    } else {
                        Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show();
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
                loading.dismiss();
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
//        get data customer
        Log.e("e", strReq.toString());
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

    //    customer location
    public class CustomerLocation extends ArrayAdapter {
        List<ModelCustomerLocation> listCustomer;

        public CustomerLocation(Context context, int resource, List<ModelCustomerLocation> listCustomer) {
            super(context, resource, listCustomer);
            this.listCustomer = listCustomer;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.customerlocation_spinner, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_customerlocation);
            id.setText(listCustomer.get(position).getIdlokasi());
            TextView tvcustomer = (TextView) layout
                    .findViewById(R.id.customerlocation);
            tvcustomer.setText(listCustomer.get(position).getLokasi());
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


    //    adapter menu_sales
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
