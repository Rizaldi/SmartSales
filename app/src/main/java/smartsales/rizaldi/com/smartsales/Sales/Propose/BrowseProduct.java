package smartsales.rizaldi.com.smartsales.Sales.Propose;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ImageView;
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

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.customerorder.*;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class BrowseProduct extends AppCompatActivity {
    List<ModelBrand> brandList;
    List<ModelCategory> categoryList;
    List<ModelListProduct> listProducts;
    SqliteHandler db;
    Spinner brandspinner, categoryspinner;
    String organizationId = "", warehouseId = "";
    RecyclerView rvproductlist;
    RVAProduct adapter;
    String idBrand = "", idCategory = "";
    ImageView go;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_product2);
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
        brandList = new ArrayList<>();
        categoryList = new ArrayList<>();
        listProducts = new ArrayList<>();
        db = new SqliteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        organizationId = user.get("organization_id");
        warehouseId = user.get("warehouse_id");
        initialVariable();
        listDataProduct("", "");
        getDataBrand();
        getDataCategory();
        SearchView search = (SearchView) findViewById(R.id.search);
        search.setOnQueryTextListener(listener);
        go=(ImageView) findViewById(R.id.go);
        try {
            if (ParamInput.anydata == false) {
                go.setVisibility(View.GONE);
            }
        } catch (Exception e) {

//            Log.e("message",e.getMessage());
        }
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BrowseProduct.this, OrderedProductList.class);
                Bundle b = new Bundle();
                b.putString("idso", ParamInput.idSO);
                i.putExtras(b);
                startActivity(i);
                finish();
            }
        });
    }

    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String query) {

            query = query.toLowerCase();

            final List<ModelListProduct> filteredList = new ArrayList<>();

            for (int i = 0; i < listProducts.size(); i++) {

                final String text = listProducts.get(i).getNama().toLowerCase();
                if (text.contains(query)) {

                    filteredList.add(listProducts.get(i));
                }
            }

            rvproductlist.setLayoutManager(new LinearLayoutManager(BrowseProduct.this));
            adapter = new RVAProduct(filteredList, BrowseProduct.this);
            rvproductlist.setAdapter(adapter);
            adapter.notifyDataSetChanged();  // data set changed
            return true;
        }

        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    };

    private void listDataProduct(String paramkey, String paramValue) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_listdataproduct + organizationId + "&warehouseId=" + warehouseId
                +"&customerId="+ ParamInput.idcustomer+ "&" + paramkey + "=" + paramValue, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.e("tes", s);
                    String data = jsonObject.getString("data");
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        jsonArray = new JSONArray(data);
                        parseDataListProduct(jsonArray);
                    } else {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
                listProducts.clear();
                adapter = new RVAProduct(listProducts, BrowseProduct.this);
                rvproductlist.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loading.dismiss();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);

    }

    private void parseDataListProduct(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            ModelListProduct Items = new ModelListProduct();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setId(json.getString("id"));
                Items.setNama(json.getString("name"));
                Items.setPrice(json.getString("harga_satuan_besar"));
                String stock = "";
                JSONArray jarray = null;
                jarray = new JSONArray(json.getString("stok"));
                JSONObject jstock = null;
                for (int j = 0; j < jarray.length(); j++) {
                    jstock = jarray.getJSONObject(j);
                    stock += jstock.getString("qty") + " " + jstock.getString("name") + ",";
                }
                stock = stock.substring(0, stock.lastIndexOf(","));
                Items.setStock(stock);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && listProducts.size() > 0) {
                listProducts.clear();
                listProducts.add(Items);
            } else {
                listProducts.add(Items);
            }
        }

        adapter = new RVAProduct(listProducts, this);
        rvproductlist.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void getDataCategory() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();

        final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_categoryproduct + organizationId, new Response.Listener<String>() {
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
                        parseDataCategory(jsonArray);
                    } else {
                        Toast.makeText(BrowseProduct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
    }

    private void parseDataCategory(JSONArray array) {
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
            categoryList.add(Items);
        }
        categoryspinner.setAdapter(new MyAdapterCategory(BrowseProduct.this, R.layout.customer_category,
                categoryList));
    }

    private void getDataBrand() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();

        final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_brand + organizationId, new Response.Listener<String>() {
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
                        parseDataBrand(jsonArray);
                    } else {
                        Toast.makeText(BrowseProduct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
    }

    private void parseDataBrand(JSONArray array) {
        placeholderBrand();
        for (int i = 0; i < array.length(); i++) {
            ModelBrand Items = new ModelBrand();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setNama(json.getString("name"));
                Items.setId(json.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            brandList.add(Items);
        }
        brandspinner.setAdapter(new MyAdapterBrand(BrowseProduct.this, R.layout.customer_category,
                brandList));
    }

    private void placeholderBrand() {
        ModelBrand Placeholder = new ModelBrand();
        Placeholder.setNama("Pilih Brand");
        Placeholder.setId("-1");
        brandList.add(Placeholder);
    }

    private void placeholderCategory() {
        ModelCategory Placeholder = new ModelCategory();
        Placeholder.setNamaCategory("Pilih Category");
        Placeholder.setIdCategory("-1");
        categoryList.add(Placeholder);
    }

    private void initialVariable() {
        brandspinner = (Spinner) findViewById(R.id.productbrand);
        brandspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    idBrand = ((TextView) view.findViewById(R.id.id_category)).getText().toString();
                    if (!idBrand.equals("-1")) {
                        listDataProduct("productBrand", idBrand);
                    }else{
                        listDataProduct("","");
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categoryspinner = (Spinner) findViewById(R.id.productcategory);
        categoryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    idCategory = ((TextView) view.findViewById(R.id.id_category)).getText().toString();
                    if (!idCategory.equals("-1")) {
                        listDataProduct("productCategory", idCategory);
                    }else{
                        listDataProduct("","");
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rvproductlist = (RecyclerView) findViewById(R.id.rvproductlist);
        rvproductlist.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rvproductlist.setLayoutManager(llm);
        rvproductlist.setFocusable(false);
        rvproductlist.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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


    //    adapter
    public class MyAdapterBrand extends ArrayAdapter {
        List<ModelBrand> listBrand;

        public MyAdapterBrand(Context context, int resource, List<ModelBrand> listBrand) {
            super(context, resource, listBrand);
            this.listBrand = listBrand;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.customer_category, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_category);
            id.setText(listBrand.get(position).getId());
            TextView tvsales = (TextView) layout
                    .findViewById(R.id.nama_category);
            tvsales.setText(listBrand.get(position).getNama());
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

}
