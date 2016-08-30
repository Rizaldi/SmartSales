package smartsales.rizaldi.com.smartsales.Sales.invoicepayment;

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

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Sales.ModelCustomerItinerary;
import smartsales.rizaldi.com.smartsales.Sales.competitor.CompetitorActivity;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class InvoicePayment extends AppCompatActivity {
    Spinner customername;
    private List<ModelCustomerItinerary> modelCustomer;
    SqliteHandler db;
    String salesId,positionstatus;
    RVAadapter adapter;
    private List<Modelcustomerinvoice> list;
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_payment);
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
        rv=(RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        modelCustomer = new ArrayList<>();
        list=new ArrayList<>();
        db = new SqliteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        salesId = user.get("employee_id");
        positionstatus=user.get("position_status");
        getDataCustomer();
        initialUI();
        getDataInvoice();
    }

    private void getDataInvoice() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_invoicepayment +positionstatus+"&employeeId="+
                salesId, new Response.Listener<String>() {
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
                        Toast.makeText(InvoicePayment.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                adapter = new RVAadapter(list, InvoicePayment.this);
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
            Modelcustomerinvoice Items = new Modelcustomerinvoice();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Items.setId(jsonObject.getString("customerId"));
                Items.setCustomer(jsonObject.getString("customerName"));
                Items.setRemaining(jsonObject.getString("remainingPayment"));
                Items.setTotal(jsonObject.getString("totalInvoice"));
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

    private void initialUI() {
        customername=(Spinner)findViewById(R.id.customername);

    }

    private void getDataCustomer() {
        final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_customer + salesId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        String data = jsonObject.getString("data");
                        jsonArray = new JSONArray(data);
                        parseDataCustomer(jsonArray);
                    } else {
                        Toast.makeText(InvoicePayment.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                CustomerAdapter adapter = new CustomerAdapter(InvoicePayment.this, R.layout.customer_spinner,
                        modelCustomer);
                customername.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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
        CustomerAdapter adapter = new CustomerAdapter(InvoicePayment.this, R.layout.customer_spinner,
                modelCustomer);
        customername.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void placeholderSpinner() {
        ModelCustomerItinerary Placeholder = new ModelCustomerItinerary();
        Placeholder.setNama_customer("choose customername");
        Placeholder.setId_customer("-1");
        modelCustomer.add(Placeholder);
    }
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

}
