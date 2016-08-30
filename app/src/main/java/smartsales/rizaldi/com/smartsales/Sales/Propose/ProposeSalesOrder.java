package smartsales.rizaldi.com.smartsales.Sales.Propose;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
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
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.customerorder.ParamInput;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class ProposeSalesOrder extends AppCompatActivity implements View.OnClickListener {
    Spinner category,customername,address;
    RecyclerView rv;
    private List<Modelcategory> list;
    private List<Modelcustomer> listcustomer;
    private List<Modeladddress> listaddress;
    RVAadapter adapter;
    String id_category="";
    private String organizationId="",salesId="";
    SqliteHandler db;
    private String id_customer="";
    private String id_addres="",brn="",gstverified="",gstnumber="";
    TextView tvbrn,tvgstverified,tvgstnumber;
    ImageButton next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propose_sales_order);
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
        list=new ArrayList<>();
        listcustomer=new ArrayList<>();
        listaddress=new ArrayList<>();
        db=new SqliteHandler(this);
        HashMap<String,String> user=db.getUserDetails();
        organizationId=user.get("organization_id");
        salesId=user.get("employee_id");
        getCategory();
        selectCategory();
        getCustomer("","");
        selectCustomername();
        selectAddress();
    }

    private void selectCategory() {
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_category = ((TextView) view.findViewById(R.id.id_category)).getText().toString();
                if (!id_category.equals("-1")) {
                    getCustomer("customerCategory",id_category);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void parseCategory(JSONArray array) {
        placeholderCategory();
        for (int i = 0; i < array.length(); i++) {
            Modelcategory Items = new Modelcategory();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setName(json.getString("name"));
                Items.setId(json.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            list.add(Items);
        }
        category.setAdapter(new CategoryAdapter(ProposeSalesOrder.this, R.layout.customer_category,
                list));
    }

    @Override
    public void onClick(View v) {
        if(v==next){
            ParamInput.idcustomer=id_customer;
            ParamInput.customerLocationId=id_addres;
            Intent i=new Intent(ProposeSalesOrder.this,BrowseProductPropose.class);
            startActivity(i);
        }
    }

    public class CategoryAdapter extends ArrayAdapter {
        List<Modelcategory> listcategory;

        public CategoryAdapter(Context context, int resource, List<Modelcategory> listcategory) {
            super(context, resource, listcategory);
            this.listcategory = listcategory;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.customer_category, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_category);
            id.setText(list.get(position).getId());
            TextView tvcustomer = (TextView) layout
                    .findViewById(R.id.nama_category);
            tvcustomer.setText(list.get(position).getName());
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
    private void getCategory() {
        final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_category+organizationId+"&salesId="+salesId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        String data = jsonObject.getString("data");
                        jsonArray = new JSONArray(data);
                        parseCategory(jsonArray);
                    } else {
                        Toast.makeText(ProposeSalesOrder.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),
                        "check Internet connection", Toast.LENGTH_LONG).show();
                listcustomer.clear();
                customername.setAdapter(new CustomerAdapter(ProposeSalesOrder.this, R.layout.spinnercustomername,
                        listcustomer));
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }


    private void placeholderCategory() {
        Modelcategory placeholder = new Modelcategory();
        placeholder.setName("Choose Category");
        placeholder.setId("-1");
        list.add(placeholder);
    }

//    customername
private void selectCustomername() {
    customername.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            id_customer = ((TextView) view.findViewById(R.id.id_category)).getText().toString();
            brn = ((TextView) view.findViewById(R.id.brn)).getText().toString();
            gstverified = ((TextView) view.findViewById(R.id.gst_reg_date)).getText().toString();
            gstnumber = ((TextView) view.findViewById(R.id.gst_number)).getText().toString();
            tvbrn.setText(brn);
            tvgstnumber.setText(gstnumber);
            tvgstverified.setText(gstverified);
            if (!id_customer.equals("-1")) {
//                    getDataTarget(id_month,"brandId",id_brand);
                getAddress(id_customer);

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });

}

    private void parsecustomer(JSONArray array) {
//        placeholderCategory();
        for (int i = 0; i < array.length(); i++) {
            Modelcustomer Items = new Modelcustomer();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setName(json.getString("text"));
                Items.setId(json.getString("id"));
                Items.setBrn(json.getString("brn"));
                Items.setGst_reg_date(json.getString("gst_reg_date"));
                Items.setGst_number(json.getString("gst_number"));
                Items.setVisiting(json.getString("visiting"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && listcustomer.size() != 0) {
                listcustomer.clear();
                listcustomer.add(Items);
            } else if (i == 0) {
                listcustomer.add(Items);
            } else {
                listcustomer.add(Items);
            }
//            listcustomer.add(Items);
        }
        customername.setAdapter(new CustomerAdapter(ProposeSalesOrder.this, R.layout.spinnercustomername,
                listcustomer));
    }
    public class CustomerAdapter extends ArrayAdapter {
        List<Modelcustomer> listcustomer;

        public CustomerAdapter(Context context, int resource, List<Modelcustomer> listcustomer) {
            super(context, resource, listcustomer);
            this.listcustomer = listcustomer;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.spinnercustomername, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_category);
            id.setText(listcustomer.get(position).getId());
            TextView tvcustomer = (TextView) layout
                    .findViewById(R.id.nama_category);
            tvcustomer.setText(listcustomer.get(position).getName());
            TextView brn = (TextView) layout
                    .findViewById(R.id.brn);
            brn.setText(listcustomer.get(position).getBrn());
            TextView gst_reg_date = (TextView) layout
                    .findViewById(R.id.gst_reg_date);
            gst_reg_date.setText(listcustomer.get(position).getGst_reg_date());
            TextView gst_number = (TextView) layout
                    .findViewById(R.id.gst_number);
            gst_number.setText(listcustomer.get(position).getGst_number());
            TextView visiting = (TextView) layout
                    .findViewById(R.id.visiting);
            visiting.setText(listcustomer.get(position).getVisiting());
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
    private void getCustomer(String key,String value) {
        final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_customer+salesId+"&"+key+"="+value, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        String data = jsonObject.getString("data");
                        jsonArray = new JSONArray(data);
                        parsecustomer(jsonArray);
                    } else {
                        Toast.makeText(ProposeSalesOrder.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

//        address
private void selectAddress() {
    address.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            id_addres = ((TextView) view.findViewById(R.id.id_category)).getText().toString();
            if (!id_addres.equals("-1")) {
//                getCustomer("customerCategory",id_category);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    });

}

    private void parseAddress(JSONArray array) {
//        placeholderCategory();
        for (int i = 0; i < array.length(); i++) {
            Modeladddress Items = new Modeladddress();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setName(json.getString("address"));
                Items.setId(json.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && listaddress.size() != 0) {
                listaddress.clear();
                listaddress.add(Items);
            } else if (i == 0) {
                listaddress.add(Items);
            } else {
                listaddress.add(Items);
            }

            listaddress.add(Items);
        }
        address.setAdapter(new AddressAdapter(ProposeSalesOrder.this, R.layout.customer_category,
                listaddress));
    }
    public class AddressAdapter extends ArrayAdapter {
        List<Modeladddress> listaddress;

        public AddressAdapter(Context context, int resource, List<Modeladddress> listaddress) {
            super(context, resource, listaddress);
            this.listaddress = listaddress;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.customer_category, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_category);
            id.setText(listaddress.get(position).getId());
            TextView tvcustomer = (TextView) layout
                    .findViewById(R.id.nama_category);
            tvcustomer.setText(listaddress.get(position).getName());
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
    private void getAddress(String customerId) {
        final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_customerlocation+customerId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        String data = jsonObject.getString("data");
                        jsonArray = new JSONArray(data);
                        parseAddress(jsonArray);
                    } else {
                        Toast.makeText(ProposeSalesOrder.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),
                        "check Internet connection", Toast.LENGTH_LONG).show();
                listaddress.clear();
                address.setAdapter(new AddressAdapter(ProposeSalesOrder.this, R.layout.customer_category,
                        listaddress));
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void initialUI() {
        category=(Spinner) findViewById(R.id.category);
        customername=(Spinner) findViewById(R.id.customername);
        address=(Spinner) findViewById(R.id.address);
//        rv=(RecyclerView) findViewById(R.id.rv);
//        rv.setHasFixedSize(true);
//        rv.setNestedScrollingEnabled(false);
//        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
//        rv.setLayoutManager(llm);
//        rv.setFocusable(false);
        tvbrn=(TextView) findViewById(R.id.tvbrn);
        tvgstverified=(TextView) findViewById(R.id.tvgstverified);
        tvgstnumber=(TextView) findViewById(R.id.tvgstnumber);
        next=(ImageButton) findViewById(R.id.next);
        next.setOnClickListener(this);
    }

}
