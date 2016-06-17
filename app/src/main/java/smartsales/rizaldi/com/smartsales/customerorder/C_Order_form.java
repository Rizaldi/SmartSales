package smartsales.rizaldi.com.smartsales.customerorder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
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
import org.w3c.dom.Text;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartsales.rizaldi.com.smartsales.Approval.AdapterModelSetup.ModelItinerary;
import smartsales.rizaldi.com.smartsales.DateToday;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Sales.ListCustomer;
import smartsales.rizaldi.com.smartsales.Sales.ModelCustomerItinerary;
import smartsales.rizaldi.com.smartsales.Sales.ModelCustomerLocation;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class C_Order_form extends AppCompatActivity {
    Spinner category, customername, address;
    private List<ModelCustomerItinerary> modelCustomer;
    private List<ModelCategory> listcategory;
    private List<ModelCustomerLocation> mlocation;
    String idcategory = "-1", namacategory = "", idcustomer = "-1", DeliveryDate = "", PoDate = "", customerLocationId = "-1";
    SqliteHandler db;
    String organizationId = "", salesId = "", brn = "", gstreg = "", gstnumber = "", deliveristatus = "";
    String position_status;
    TextView tvbrn, tvgstreg, tvgstnmuber, tvdeliverdate, tvpodate;
    RadioButton ontime, before;
    CardView deliverdate, podate;
    static final int Dialog_Id = 0, Dialog_Id2 = 1;
    int year_x, month_x, day_x;
    EditText etponumber;
    ImageButton ibnext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c__order_form);
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
        Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        modelCustomer = new ArrayList<>();
        listcategory = new ArrayList<>();
        mlocation = new ArrayList<>();
        db = new SqliteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        organizationId = user.get("organization_id");
        salesId = user.get("employee_id");
        position_status=user.get("position_status");
        iniitialVariable();
        category.setFocusable(true);
//        etponumber.setFocusable(false);
        getDataCustomer("","");

        getDataCategory();
        getcategory();
        getCustomer();
        showDialogClick();
        showDialogClickPOdate();
        if(position_status.equals("2")){
            etponumber.setFocusable(false);
            tvdeliverdate.setText(DateToday.dateNowSlash());
            tvpodate.setText(DateToday.dateNowSlash());
            ontime.setSelected(true);
            deliveristatus = "0";
        }
    }

    private void showDialogClickPOdate() {
        podate = (CardView) findViewById(R.id.podate);
        podate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Dialog_Id2);
            }
        });
    }

    private void showDialogClick() {
        deliverdate = (CardView) findViewById(R.id.deliverdate);
        deliverdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Dialog_Id);
//                etponumber.setFocusable(true);
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == Dialog_Id) {
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        } else if (id == Dialog_Id2) {
            return new DatePickerDialog(this, dpickerListener2, year_x, month_x, day_x);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear+1;
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
            DeliveryDate = tanggal + "/" + bulan + "/" + year_x;
            tvdeliverdate.setText(DeliveryDate);
        }
    };
    private DatePickerDialog.OnDateSetListener dpickerListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear+1;
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
            PoDate = tanggal + "/" + bulan + "/" + year_x;
            tvpodate.setText(PoDate);
        }
    };

    private void iniitialVariable() {
        category = (Spinner) findViewById(R.id.category);
        customername = (Spinner) findViewById(R.id.customername);
        address = (Spinner) findViewById(R.id.address);
        tvbrn = (TextView) findViewById(R.id.brn);
        tvgstreg = (TextView) findViewById(R.id.verified);
        tvgstnmuber = (TextView) findViewById(R.id.gstnumber);
        ontime = (RadioButton) findViewById(R.id.ontime);
        before = (RadioButton) findViewById(R.id.before);
        ontime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveristatus = "0";
            }
        });
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveristatus = "1";
            }
        });
        tvdeliverdate = (TextView) findViewById(R.id.date);
        etponumber = (EditText) findViewById(R.id.etponumber);
        tvpodate = (TextView) findViewById(R.id.date1);
        ibnext = (ImageButton) findViewById(R.id.ibnext);
        ibnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!idcustomer.equals("-1") && !customerLocationId.equals("-1")
                        && !deliveristatus.isEmpty() && !DeliveryDate.isEmpty() && !PoDate.isEmpty()) {
                    ParamInput.idcustomer = idcustomer;
                    ParamInput.customerLocationId = customerLocationId;
                    ParamInput.deliveristatus = deliveristatus;
                    ParamInput.DeliveryDate = DeliveryDate;
                    ParamInput.PoDate = PoDate;
                    ParamInput.PoNumber = etponumber.getText().toString();
                    startActivity(new Intent(C_Order_form.this, BrowseProduct.class));
                    finish();
                } else {
                    Snackbar.make(v, "Lengkapi Data Terlebih Dahulu", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    //    category
    private void getDataCategory() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_category + organizationId + "&salesId=" + salesId, new Response.Listener<String>() {
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
                        parseData(jsonArray);
                    } else {
                        Toast.makeText(C_Order_form.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
        Log.e("e", strReq.toString());
    }

    private void placeholderCategory() {
        ModelCategory placeholder = new ModelCategory();
        placeholder.setNamaCategory("Pilih Category");
        placeholder.setIdCategory("-1");
        listcategory.add(placeholder);
    }

    private void parseData(JSONArray array) {
        placeholderCategory();
        for (int i = 0; i < array.length(); i++) {
            ModelCategory Items = new ModelCategory();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setNamaCategory(json.getString("name"));
                Items.setIdCategory(json.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listcategory.add(Items);
        }
        category.setAdapter(new MyAdapterCategory(C_Order_form.this, R.layout.customer_category,
                listcategory));
    }

    private void getcategory() {
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idcategory = ((TextView) view.findViewById(R.id.id_category)).getText().toString();
                namacategory = ((TextView) view.findViewById(R.id.nama_category)).getText().toString();
                if (!idcategory.equals("-1")) {
                    getDataCustomer("customerCategory", idcategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //    getcustomer

    private void getDataCustomer(String keyparam,String valparam) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_customer + salesId + "&"+keyparam+"=" + valparam, new Response.Listener<String>() {
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
                        parseDataCustomer(jsonArray);
                    } else {
                        Toast.makeText(C_Order_form.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                modelCustomer.clear();
                CustomerAdapter adapter = new CustomerAdapter(C_Order_form.this, R.layout.customer_spinner,
                        modelCustomer);
                customername.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loading.dismiss();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
        Log.e("e", strReq.toString());
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
                Items.setBrn(json.getString("brn"));
                Items.setGstReg(json.getString("gst_reg_date"));
                Items.setGstNumber(json.getString("gst_number"));
//                brn = json.getString("brn");
//                gstreg = json.getString("gst_reg_date");
//                gstnumber = json.getString("gst_number");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && modelCustomer.size() != 0) {
                modelCustomer.clear();
                placeholderSpinner();
                modelCustomer.add(Items);
            } else if (i == 0) {
                placeholderSpinner();
                modelCustomer.add(Items);
            } else {
                modelCustomer.add(Items);
            }
        }
        CustomerAdapter adapter = new CustomerAdapter(C_Order_form.this, R.layout.customer_spinner,
                modelCustomer);
        customername.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void placeholderSpinner() {
        ModelCustomerItinerary Placeholder = new ModelCustomerItinerary();
        Placeholder.setNama_customer("Pilih Customer");
        Placeholder.setId_customer("-1");
        modelCustomer.add(Placeholder);
    }

    private void getCustomer() {
        customername.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    idcustomer = ((TextView) view.findViewById(R.id.id_customer)).getText().toString();
                    brn=((TextView) view.findViewById(R.id.brn)).getText().toString();
                    gstreg=((TextView) view.findViewById(R.id.gstreg)).getText().toString();
                    gstnumber=((TextView) view.findViewById(R.id.gstnumber)).getText().toString();
                    tvbrn.setText(brn);
                    tvgstreg.setText(gstreg);
                    tvgstnmuber.setText(gstnumber);
                    getDataCustomerLocation(idcustomer, view);

                } catch (Exception e) {
                    Log.e("cek id", "tidak ada data");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                        getCustomerLocation();
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
                CustomerLocation adapter = new CustomerLocation(C_Order_form.this, R.layout.customerlocation_spinner,
                        mlocation);
                address.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loading.dismiss();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
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
        CustomerLocation adapter = new CustomerLocation(C_Order_form.this, R.layout.customerlocation_spinner,
                mlocation);
        address.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void placeholderLocation() {
        ModelCustomerLocation Placeholder = new ModelCustomerLocation();
        Placeholder.setLokasi("Pilih Lokasi");
        Placeholder.setIdlokasi("-1");
        mlocation.add(Placeholder);
    }

    private void getCustomerLocation() {
        address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    customerLocationId = ((TextView) view.findViewById(R.id.id_customerlocation)).getText().toString();
                }catch (Exception e){

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //    adapter
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
            TextView brn=(TextView) layout.findViewById(R.id.brn);
            brn.setText(listCustomer.get(position).getBrn());
            TextView gstreg=(TextView) layout.findViewById(R.id.gstreg);
            gstreg.setText(listCustomer.get(position).getGstReg());
            TextView gstnumber=(TextView) layout.findViewById(R.id.gstnumber);
            gstnumber.setText(listCustomer.get(position).getGstNumber());
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

    public class MyAdapterCategory extends ArrayAdapter {
        List<ModelCategory> listCategory;

        public MyAdapterCategory(Context context, int resource, List<ModelCategory> listCategory) {
            super(context, resource, listCategory);
            this.listCategory = listCategory;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.customer_category, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_category);
            id.setText(listCategory.get(position).getIdCategory());
            TextView tvsales = (TextView) layout
                    .findViewById(R.id.nama_category);
            tvsales.setText(listCategory.get(position).getNamaCategory());
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
}
