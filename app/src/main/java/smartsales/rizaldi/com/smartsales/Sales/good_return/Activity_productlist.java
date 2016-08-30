package smartsales.rizaldi.com.smartsales.Sales.good_return;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.SpinnerModel;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.Varglobal;
import smartsales.rizaldi.com.smartsales.session.SessionManager;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class Activity_productlist extends AppCompatActivity implements View.OnClickListener {
    private List<SpinnerModel> listProduct;
    private List<SpinnerModel> listUom;
    private List<SpinnerModel> listCondition;
    private List<ModelList> list;
    SqliteHandler db;
    String idcustomer = "", idlocation = "", colectstatus = "", returnstatus = "", productId = "", uomId = "", conditionId = "";
    String productname = "", uomname = "", conditionname = "";
    Spinner spinproductname, spinneruom, spinnercondition;
    ProgressDialog pDialog;
    private String organizationId = "", warehouseId = "", employeId = "", username = "", position_status = "";
    EditText month, year, etqty;
    RVAadapter adapter;
    RecyclerView rv;
    ImageView add, csfsave;
    boolean errmonth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_goodreturn);
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

        listProduct = new ArrayList<>();
        listUom = new ArrayList<>();
        listCondition = new ArrayList<>();
        list = new ArrayList<>();
        Bundle b = getIntent().getExtras();
        idcustomer = b.getString("idcustomer");
        idlocation = b.getString("idlocation");
        colectstatus = b.getString("colecstatus");
        returnstatus = b.getString("returnstatus");
        db = new SqliteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        organizationId = user.get("organization_id");
        warehouseId = user.get("warehouse_id");
        employeId = user.get("employee_id");
        username = user.get("username");
        position_status = user.get("position_status");
        initialUI();
        getSpinProduct();
    }

    private void initialUI() {
        spinproductname = (Spinner) findViewById(R.id.productname);
        spinproductname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                productId = ((TextView) view.findViewById(R.id.id_grcustname)).getText().toString();
                productname = ((TextView) view.findViewById(R.id.grcustname)).getText().toString();
                if (!productId.equals("-1")) {
                    getSpinUom(productId);
                    getSpinOfCondition();
                } else {
                    listUom.clear();
                    listCondition.clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinneruom = (Spinner) findViewById(R.id.uom);
        spinneruom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                uomId = ((TextView) view.findViewById(R.id.id_grcustname)).getText().toString();
                uomname = ((TextView) view.findViewById(R.id.grcustname)).getText().toString();

                Log.e("uomid", uomId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnercondition = (Spinner) findViewById(R.id.conditionofgood);
        spinnercondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                conditionId = ((TextView) view.findViewById(R.id.id_grcustname)).getText().toString();
                conditionname = ((TextView) view.findViewById(R.id.grcustname)).getText().toString();
                Log.e("conditionId", conditionId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        month = (EditText) findViewById(R.id.month);
        month.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    if (Integer.parseInt(String.valueOf(s)) > 12) {
                        Toast.makeText(Activity_productlist.this, "wrong format month", Toast.LENGTH_SHORT).show();
                        errmonth = true;
                    } else {
                        errmonth = false;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        year = (EditText) findViewById(R.id.year);
        etqty = (EditText) findViewById(R.id.qty);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setFocusable(false);
        add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(this);
        csfsave = (ImageView) findViewById(R.id.csfsave);
        csfsave.setOnClickListener(this);
    }

    private void getSpinProduct() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlLib.url_product
                + "organizationId=" + organizationId + "&warehouseId=" + warehouseId + "&returnStatus=" + returnstatus + "&collectStatus=" + colectstatus,
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
                String text = jsonObject.getString("name");

                spinnerModel.setId(id);
                spinnerModel.setName(text);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && listProduct.size() != 0) {
                listProduct.clear();
                placeholderSpinnerProduct();
                listProduct.add(spinnerModel);
            } else if (i == 0) {
                placeholderSpinnerProduct();
                listProduct.add(spinnerModel);
            } else {
                listProduct.add(spinnerModel);
            }
        }
        spinproductname.setAdapter(new ProductAdapter(Activity_productlist.this, R.layout.grcustname_spinner, listProduct));
    }

    private void placeholderSpinnerProduct() {
        SpinnerModel spinnerModel = new SpinnerModel();
        spinnerModel.setName("Choose Customer Name");
        spinnerModel.setId("-1");
        listProduct.add(spinnerModel);
    }

    @Override
    public void onClick(View v) {
        if (v == add) {
            if (!productId.equals("-1") && !conditionId.equals("-1") && !uomId.equals("-1") && !etqty.getText().toString().isEmpty()
                    && !month.getText().toString().isEmpty() && !year.getText().toString().isEmpty()) {
                if (errmonth) {
                    Toast.makeText(Activity_productlist.this, "wrong format month", Toast.LENGTH_SHORT).show();
                    month.setText("");
                    month.requestFocus();
                    return;
                }
                if (year.getText().toString().length() < 4) {
                    year.setText("");
                    year.requestFocus();
                    Toast.makeText(Activity_productlist.this, "wrong format year", Toast.LENGTH_SHORT).show();
                    return;
                }
                ModelList items = new ModelList();
                items.setProdutId(productId);
                items.setProductname(productname);
                items.setConditionId(conditionId);
                items.setConditionname(conditionname);
                items.setQty(etqty.getText().toString());
                items.setUomId(uomId);
                items.setUomname(uomname);
                String m = (Integer.parseInt(month.getText().toString()) < 10 && month.getText().length() == 1) ? "0" + month.getText().toString() : month.getText().toString();
                items.setExpireddate(m + "/" + year.getText().toString());
                items.setGoodreturn(productId + ","
                        + etqty.getText().toString() + "," +
                        uomId + "," + conditionId + "," + m + "/" + year.getText().toString());
                items.setPosition(list.size());
                list.add(items);
                adapter = new RVAadapter(list, Activity_productlist.this);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                for (int i = 0; i < list.size(); i++) {

                    if (i == 0) {
                        Varglobal.arrGoodreturn = list.get(i).getGoodreturn() + "|";
                    } else {
                        Varglobal.arrGoodreturn = Varglobal.arrGoodreturn + list.get(i).getGoodreturn() + "|";
                    }
                }
                spinproductname.setSelection(0);
                month.setText("");
                year.setText("");
                etqty.setText("");
            } else {
                Toast.makeText(Activity_productlist.this, "fill content", Toast.LENGTH_SHORT).show();
            }
        }

        if (v == csfsave) {
            if (list.size() != 0) {
                saveGoodreturn(v);
            } else
                Toast.makeText(Activity_productlist.this, "nothing product in list", Toast.LENGTH_LONG).show();

        }
    }

    //    save data
    private void saveGoodreturn(final View v) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();

        final StringRequest strReq = new StringRequest(Request.Method.POST, UrlLib.url_save_goodreturn, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("message");
                    if (code.equals("1")) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(Activity_productlist.this);
                        builder.setTitle("Message").setMessage(msg).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                Varglobal.arrGoodreturn="";
                            }
                        });
                        builder.show();
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
                params.put("organizationId", organizationId);
                params.put("warehouseId", warehouseId);
                params.put("customerId", idcustomer);
                params.put("customerLocationId", idlocation);
                params.put("employeeId", employeId);
                params.put("username", username);
                params.put("collectionStatus", colectstatus);
                String returnProductOrder = Varglobal.arrGoodreturn.substring(0, Varglobal.arrGoodreturn.length() - 1);
                params.put("returnProductOrder", returnProductOrder);
                params.put("positionStatus", position_status);
                params.put("returnType", returnstatus);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }


    public class ProductAdapter extends ArrayAdapter {
        List<SpinnerModel> spinnerModels;

        public ProductAdapter(Context context, int resource, List<SpinnerModel> spinnerModels) {
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


    //    get uom
    private void getSpinUom(String productId) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlLib.UOM(productId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String data = jsonObject.getString("data");

                            JSONArray jsonArray = new JSONArray(data);
                            parseDataUom(jsonArray);
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

    private void parseDataUom(JSONArray jsonArray) {
        int i = 0;
        for (i = 0; i < jsonArray.length(); i++) {
            SpinnerModel spinnerModel = new SpinnerModel();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String id = jsonObject.getString("uom_id");
                String text = jsonObject.getString("name");

                spinnerModel.setId(id);
                spinnerModel.setName(text);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && listUom.size() != 0) {
                listUom.clear();
                placeholderSpinnerUom();
                listUom.add(spinnerModel);
            } else if (i == 0) {
                placeholderSpinnerUom();
                listUom.add(spinnerModel);
            } else {
                listUom.add(spinnerModel);
            }
        }
        spinneruom.setAdapter(new UomAdapter(Activity_productlist.this, R.layout.grcustname_spinner, listUom));
    }

    private void placeholderSpinnerUom() {
        SpinnerModel spinnerModel = new SpinnerModel();
        spinnerModel.setName("Choose Uom");
        spinnerModel.setId("-1");
        listUom.add(spinnerModel);
    }

    public class UomAdapter extends ArrayAdapter {
        List<SpinnerModel> spinnerModels;

        public UomAdapter(Context context, int resource, List<SpinnerModel> spinnerModels) {
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

    //    get condition of good
    private void getSpinOfCondition() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlLib.condition_ofgood,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String data = jsonObject.getString("data");

                            JSONArray jsonArray = new JSONArray(data);
                            parseDataCondition(jsonArray);
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

    private void parseDataCondition(JSONArray jsonArray) {
        int i;
        for (i = 0; i < jsonArray.length(); i++) {
            SpinnerModel spinnerModel = new SpinnerModel();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String id = jsonObject.getString("id");
                String text = jsonObject.getString("name");

                spinnerModel.setId(id);
                spinnerModel.setName(text);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && listCondition.size() != 0) {
                listCondition.clear();
                placeholderSpinnerCondition();
                listCondition.add(spinnerModel);
            } else if (i == 0) {
                placeholderSpinnerCondition();
                listCondition.add(spinnerModel);
            } else {
                listCondition.add(spinnerModel);
            }
        }
        spinnercondition.setAdapter(new UomAdapter(Activity_productlist.this, R.layout.grcustname_spinner, listCondition));
    }

    private void placeholderSpinnerCondition() {
        SpinnerModel spinnerModel = new SpinnerModel();
        spinnerModel.setName("Choose Condition of goods");
        spinnerModel.setId("-1");
        listCondition.add(spinnerModel);
    }

}
