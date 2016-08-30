package smartsales.rizaldi.com.smartsales.Sales.good_return;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
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
    private List<GRcustomerAddress> listcustomeraddress;
    ProgressDialog pDialog;
    private SessionManager session;
    ImageView cfsave;
    RadioButton rb, rbcoll;
    SqliteHandler db;
    RadioButton f_customer, t_warehouse, self, driver;
    String group, sales, organitation;
    RadioGroup GRreturnstatus, GRcollectstatus;
    Spinner GRcustomername, spincaddress;
    String idgcustomername = "-1", grcustomer, idgrcl = "-1", grcl = "", organizationId = "";
    String returnstatus = "", colectstatus = "";
    private String position_status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_return);
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

        session = new SessionManager(getApplicationContext());
        db = new SqliteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        group = user.get("position_name");
        sales = user.get("employee_id");
        organizationId = user.get("organization_id");
        position_status = user.get("position_status");
        if (position_status.equals("1")) {
            returnstatus = "0";
            getDataGRcustomername(UrlLib.url_customer, sales);
        } else {
            colectstatus = "0";
        }
        initializeVariable();
        spinnerModels = new ArrayList<>();
        listcustomeraddress = new ArrayList<>();
    }

    private void initializeVariable() {
        GRreturnstatus = (RadioGroup) findViewById(R.id.GRreturnstatus);
        f_customer = (RadioButton) findViewById(R.id.f_customer);
        t_warehouse = (RadioButton) findViewById(R.id.t_warehouse);
        GRcollectstatus = (RadioGroup) findViewById(R.id.GRcollectstatus);
        self = (RadioButton) findViewById(R.id.self);
        driver = (RadioButton) findViewById(R.id.driver);
        GRcustomername = (Spinner) findViewById(R.id.GRcustomername);
        spincaddress = (Spinner) findViewById(R.id.GRcustomeraddress);
        cfsave = (ImageView) findViewById(R.id.csfsave);
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
        spincaddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idgrcl = ((TextView) view.findViewById(R.id.id_grcl)).getText().toString();
                grcl = ((TextView) view.findViewById(R.id.grcl)).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (!group.equals("Van Sales")) {
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
                if (rbcoll.getText().equals("Self Collect")) {
                    colectstatus = "0";
                } else {
                    colectstatus = "1";
                }
            }
        });
        GRreturnstatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rb = (RadioButton) findViewById(checkedId);
                if (rb.getText().equals("To Warehouse")) {
                    getDataGRcustomername(UrlLib.to_warehouse, organizationId);
                    returnstatus = "1";
                }
                if (rb.getText().equals("From Customer")) {
                    getDataGRcustomername(UrlLib.url_customer, sales);
                    returnstatus = "0";
                }
            }
        });
    }

    private void getDataCL(String idgcustomername, View view) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlLib.url_customerlocation + idgcustomername,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
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
                }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseDataGRcl(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
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
            if (i == 0 && listcustomeraddress.size() != 0) {
                listcustomeraddress.clear();
                listcustomeraddress.add(gRcustomeraddress);
            } else if (i == 0) {
                listcustomeraddress.add(gRcustomeraddress);
            } else {
                listcustomeraddress.add(gRcustomeraddress);
            }
        }
        spincaddress.setAdapter(new GRclAdapter(GoodsReturn.this, R.layout.grcl_spinner, listcustomeraddress));
    }

    private void getDataGRcustomername(String url, String value) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + value,
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
                }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void parseDataGRcustname(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.length(); i++) {
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
        spinnerModel.setName("Choose Customer Name");
        spinnerModel.setId("-1");
        spinnerModels.add(spinnerModel);
    }

    @Override
    public void onClick(View v) {
        if (v == cfsave) {
            if (!idgcustomername.equals("-1") && !idgrcl.equals("-1") && !colectstatus.isEmpty() && !returnstatus.isEmpty()) {
                Bundle b = new Bundle();
                Intent i = new Intent(GoodsReturn.this, Activity_productlist.class);
                b.putString("idcustomer", idgcustomername);
                b.putString("idlocation", idgrcl);
                b.putString("colecstatus", colectstatus);
                b.putString("returnstatus", returnstatus);
                i.putExtras(b);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(GoodsReturn.this, "fill all data", Toast.LENGTH_SHORT).show();
            }
        }
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