package smartsales.rizaldi.com.smartsales.Sales.reffill_van;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import smartsales.rizaldi.com.smartsales.session.SessionManager;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class RefillVan extends AppCompatActivity {
    private List<spRefillModel> spRefillModels;
    private List<UOMRefillModel> uomRefillModels;
    EditText qty;
    ImageButton ibsave;
    private SessionManager session;
    SqliteHandler db;
    Spinner productspinner,uomspinner;
    String organization;
    String idreffillvan ="-1",product_refillvan="",iduom="-1",uom="";

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reffil_van);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        initializeVariable();
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
        HashMap<String,String> user=db.getUserDetails();
        organization = user.get("organization_id");

        Toast.makeText(RefillVan.this, organization, Toast.LENGTH_SHORT).show();

        spRefillModels = new ArrayList<>();
        uomRefillModels = new ArrayList<>();

        addItemSpinner();
    }

    private void initializeVariable() {
        productspinner = (Spinner) findViewById(R.id.productspinner);
        productspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idreffillvan = ((TextView) view.findViewById(R.id.id_product_refill)).getText().toString();
                product_refillvan = ((TextView) view.findViewById(R.id.product_refill)).getText().toString();

                try {
                    getDataUOM(idreffillvan , view);
                }
                catch (Exception e){
                    Toast.makeText(RefillVan.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        uomspinner = (Spinner) findViewById(R.id.UOMspinner);
        uomspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iduom = ((TextView) view.findViewById(R.id.id_uom)).getText().toString();
                uom = ((TextView) view.findViewById(R.id.uom)).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        qty = (EditText)findViewById(R.id.qty);

        ibsave = (ImageButton) findViewById(R.id.cfsave);
        ibsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDataRVan(v);
            }
        });
    }

    private void addDataRVan(View v) {
        final String a = qty.getText().toString();

        if (iduom.equals("-1") || qty.equals("") || idreffillvan.equals("-1")){
            Snackbar.make(v, "Maaf Data Tidak Boleh Kosong!", Snackbar.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        Toast.makeText(RefillVan.this, idreffillvan+iduom+a, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
//                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("id_refill_van",idreffillvan);
                params.put("id_uom",iduom);
                params.put("qty",a);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getDataUOM(String idreffillvan, View view) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlLib.UOM(idreffillvan),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String data = jsonObject.getString("data");

                            JSONArray jsonArray = new JSONArray(data);
                            parseDataUOM(jsonArray);
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

    private void parseDataUOM(JSONArray jsonArray) {
        for (int i = 0; i<jsonArray.length(); i++){
            UOMRefillModel uomRefillModel = new UOMRefillModel();
            try {
                JSONObject a = (JSONObject) jsonArray.get(i);
                String uom_id = a.getString("uom_id");
                String uom = a.getString("name");
                uomRefillModel.setId_uom(uom_id);
                uomRefillModel.setUom(uom);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && uomRefillModels.size() != 0) {
                uomRefillModels.clear();
                placeholderSpinnerUOM();
                uomRefillModels.add(uomRefillModel);
            } else if (i == 0) {
                placeholderSpinnerUOM();
                uomRefillModels.add(uomRefillModel);
            } else {
                uomRefillModels.add(uomRefillModel);
            }
        }
        uomspinner.setAdapter(new UOMAdapter(RefillVan.this, R.layout.uom_spinner, uomRefillModels));
    }

    private void placeholderSpinnerUOM() {
        UOMRefillModel uomRefillModel = new UOMRefillModel();
        uomRefillModel.setUom("Pilih UOM");
        uomRefillModel.setId_uom("-1");
        uomRefillModels.add(uomRefillModel);
    }

    private void addItemSpinner() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlLib.productreffill(organization),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String data = jsonObject.getString("data");

                            JSONArray jsonArray = new JSONArray(data);
                            parseDataspReffill(jsonArray);
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

    private void parseDataspReffill(JSONArray jsonArray) {
        placeholderSpinnerproductReffill();
        for (int i = 0; i<jsonArray.length(); i++){
            spRefillModel spRefillModel = new spRefillModel();
            JSONObject jsonObject1 = null;
            try {
                jsonObject1 = (JSONObject) jsonArray.get(i);
                String id = jsonObject1.getString("id");
                String name = jsonObject1.getString("name");
                spRefillModel.setId_sp_reffil(id);
                spRefillModel.setProduct_reffil(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            spRefillModels.add(spRefillModel);
        }

        productspinner.setAdapter(new MyAdapter(RefillVan.this, R.layout.spinner, spRefillModels));
    }

    private void placeholderSpinnerproductReffill() {
        spRefillModel place = new spRefillModel();
        place.setProduct_reffil("Pilih Product");
        place.setId_sp_reffil("-1");
        spRefillModels.add(place);
    }

    public class MyAdapter extends ArrayAdapter {
        List<spRefillModel> spRefillModels;

        public MyAdapter(Context context, int resource, List<spRefillModel> spRefillModels) {
            super(context, resource, spRefillModels);
            this.spRefillModels = spRefillModels;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.spinner, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_product_refill);
            id.setText(spRefillModels.get(position).getId_sp_reffil());
            TextView tvcuti = (TextView) layout
                    .findViewById(R.id.product_refill);
            tvcuti.setText(spRefillModels.get(position).getProduct_reffil());
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
    public class UOMAdapter extends ArrayAdapter {
        List<UOMRefillModel> uomRefillModels;

        public UOMAdapter(Context context, int resource, List<UOMRefillModel> uomRefillModels) {
            super(context, resource, uomRefillModels);
            this.uomRefillModels = uomRefillModels;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.uom_spinner, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_uom);
            id.setText(uomRefillModels.get(position).getId_uom());
            TextView tvcuti = (TextView) layout
                    .findViewById(R.id.uom);
            tvcuti.setText(uomRefillModels.get(position).getUom());
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
