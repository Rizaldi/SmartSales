package smartsales.rizaldi.com.smartsales.Sales.good_return;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.view.LayoutInflater;

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

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Sales.reffill_van.UOMRefillModel;
import smartsales.rizaldi.com.smartsales.Sales.reffill_van.spRefillModel;
import smartsales.rizaldi.com.smartsales.SpinnerModel;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.session.SessionManager;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class GoodsReturn extends AppCompatActivity implements View.OnClickListener {
    private List<SpinnerModel> spinnerModels;
    private List<GRcustomerAddress> gRcustomerAddresses;
    ProgressDialog pDialog;
    private SessionManager session;
    Button cfsave;
    RadioButton rb,rbcoll;
    SqliteHandler db;
    RadioButton f_customer,t_warehouse,self,driver;
    String group,sales,organitation;
    RadioGroup GRreturnstatus,GRcollectstatus;
    Spinner GRcustomername,GRcustomeraddressssssss;
    String idgcustomername = "-1",grcustomer,idgrcl = "-1", grcl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_return);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());
        db = new SqliteHandler(getApplicationContext());
        HashMap<String,String> user=db.getUserDetails();

        group = user.get("position_name");
        sales = user.get("employee_id");
        initializeVariable();
        spinnerModels = new ArrayList<>();
        gRcustomerAddresses = new ArrayList<>();
    }

    private void initializeVariable() {
        GRreturnstatus = (RadioGroup) findViewById(R.id.GRreturnstatus);
        f_customer = (RadioButton) findViewById(R.id.f_customer);
        t_warehouse = (RadioButton) findViewById(R.id.t_warehouse);
        GRcollectstatus = (RadioGroup) findViewById(R.id.GRcollectstatus);
        self = (RadioButton) findViewById(R.id.self);
        driver = (RadioButton) findViewById(R.id.driver);
        GRcustomername = (Spinner) findViewById(R.id.GRcustomername);
        GRcustomeraddressssssss = (Spinner) findViewById(R.id.GRcustomeraddress);
        cfsave = (Button) findViewById(R.id.csfsave);
        cfsave.setOnClickListener(this);
        GRcustomername.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idgcustomername = ((TextView) view.findViewById(R.id.id_grcustname)).getText().toString();
                grcustomer = ((TextView) view.findViewById(R.id.grcustname)).getText().toString();

                try {
                    getDataCL(idgcustomername, view);
                } catch (Exception e) {
                    Toast.makeText(GoodsReturn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        GRcustomeraddressssssss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idgrcl = ((TextView) view.findViewById(R.id.id_grcl)).getText().toString();
                grcl = ((TextView) view.findViewById(R.id.grcl)).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (!group.equals("Van Sales")){
            TextView tvRs = (TextView) findViewById(R.id.tvRs);
            tvRs.setVisibility(View.GONE);
            GRreturnstatus.setVisibility(View.GONE);
            TextView textView5 = (TextView) findViewById(R.id.textView5);
            textView5.setVisibility(View.VISIBLE);
            GRcollectstatus.setVisibility(View.VISIBLE);
        }
        GRcollectstatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbcoll = (RadioButton) findViewById(checkedId);
            }
        });
        GRreturnstatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = (RadioButton) findViewById(checkedId);
                if (rb.getText().equals("To Warehouse")) {
                    getDataGRcustomername();
                }
            }
        });
    }

    private void getDataCL(String idgcustomername, View view) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlLib.GRcustCL(idgcustomername),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

//                        Toast.makeText(GoodsReturn.this, s, Toast.LENGTH_SHORT).show();
//                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String data = jsonObject.getString("data");

                            JSONArray jsonArray = new JSONArray(data);
                            parseDataGRcl(jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseDataGRcl(JSONArray jsonArray) {
//        placeholderSpinnerGRcl();
        for (int i = 0; i<jsonArray.length(); i++){
            GRcustomerAddress gRcustomeraddress = new GRcustomerAddress();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String id = jsonObject.getString("id");
                String text = jsonObject.getString("address");

                gRcustomeraddress.setId_grcust(id);
                gRcustomeraddress.setGrcust(text);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && gRcustomerAddresses.size() != 0) {
                gRcustomerAddresses.clear();
                placeholderSpinnerGRcustname();
                gRcustomerAddresses.add(gRcustomeraddress);
            } else if (i == 0) {
                placeholderSpinnerGRcustname();
                gRcustomerAddresses.add(gRcustomeraddress);
            } else {
                gRcustomerAddresses.add(gRcustomeraddress);
            }
        }
        GRcustomeraddressssssss.setAdapter(new GRclAdapter(GoodsReturn.this, R.layout.grcl_spinner, gRcustomerAddresses));
    }

    private void placeholderSpinnerGRcl() {
        GRcustomerAddress gRcustomerAddress = new GRcustomerAddress();
        gRcustomerAddress.setId_grcust("-1");
        gRcustomerAddress.setGrcust("Select Address");
    }

    private void getDataGRcustomername() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlLib.GRcustname(sales),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String data = jsonObject.getString("data");

                            JSONArray jsonArray = new JSONArray(data);
                            parseDataGRcustname(jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseDataGRcustname(JSONArray jsonArray) {
        placeholderSpinnerGRcustname();
        for (int i = 0; i<jsonArray.length(); i++){
            SpinnerModel spinnerModel = new SpinnerModel();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String id = jsonObject.getString("id");
                String text = jsonObject.getString("text");

                spinnerModel.setId(id);
                spinnerModel.setName(text);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && spinnerModels.size() != 0) {
                spinnerModels.clear();
                placeholderSpinnerGRcustname();
                spinnerModels.add(spinnerModel);
            } else if (i == 0) {
                placeholderSpinnerGRcustname();
                spinnerModels.add(spinnerModel);
            } else {
                spinnerModels.add(spinnerModel);
            }
        }
        GRcustomername.setAdapter(new GRcustnameAdapter(GoodsReturn.this, R.layout.grcustname_spinner, spinnerModels));
    }

    private void placeholderSpinnerGRcustname() {
        SpinnerModel spinnerModel = new SpinnerModel();
        spinnerModel.setName("Pilih Customer Name");
        spinnerModel.setId("-1");
        spinnerModels.add(spinnerModel);
    }

    @Override
    public void onClick(View v) {
        if (v == cfsave){
            int selectedId = GRreturnstatus.getCheckedRadioButtonId();
            int a = 0;
            if(selectedId == f_customer.getId()) {
                if (f_customer.getText().equals("From Customer")){
                    a += 1;
                }
            } else if(selectedId == t_warehouse.getId()) {
                if (t_warehouse.getText().equals("To Warehouse")){
                    a += 2;
                }
            } else {
                a += 0;
            }

            int idGRCollect = GRcollectstatus.getCheckedRadioButtonId();
            int b = 0;

            if(idGRCollect == self.getId()) {
                if (self.getText().equals("Self Collect")){
                    a += 1;
                }
            } else if(idGRCollect == driver.getId()) {
                if (driver.getText().equals("Driver Collect")){
                    a += 2;
                }
            } else {
                a += 0;
            }

            Toast.makeText(GoodsReturn.this, a + " " + idgcustomername + " " + idgrcl + " " + b, Toast.LENGTH_SHORT).show();

//            next();
        }
    }

    private void next() {
        final String rbutton = f_customer.getText().toString();
        final String t_wh = t_warehouse.getText().toString();
        final String rcollbutton = rbcoll.getText().toString();



    }

    public class GRcustnameAdapter extends ArrayAdapter {
        List<SpinnerModel> spinnerModels;

        public GRcustnameAdapter(Context context, int resource, List<SpinnerModel> spinnerModels) {
            super(context, resource, spinnerModels);
            this.spinnerModels = spinnerModels;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.grcustname_spinner, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_grcustname);
            id.setText(spinnerModels.get(position).getId());
            TextView tvcuti = (TextView) layout
                    .findViewById(R.id.grcustname);
            tvcuti.setText(spinnerModels.get(position).getName());
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
    public class GRclAdapter extends ArrayAdapter {
        List<GRcustomerAddress> gRcustomerAddresses;

        public GRclAdapter(Context context, int resource, List<GRcustomerAddress> gRcustomerAddresses) {
            super(context, resource, gRcustomerAddresses);
            this.gRcustomerAddresses = gRcustomerAddresses;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.grcl_spinner, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_grcl);
            id.setText(gRcustomerAddresses.get(position).getId_grcust());
            TextView tvcuti = (TextView) layout
                    .findViewById(R.id.grcl);
            tvcuti.setText(gRcustomerAddresses.get(position).getGrcust());
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