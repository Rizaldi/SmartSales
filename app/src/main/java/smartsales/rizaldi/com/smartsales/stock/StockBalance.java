package smartsales.rizaldi.com.smartsales.stock;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SearchView;
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
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class StockBalance extends AppCompatActivity {
    private List<ModelStock> liststock;
    SqliteHandler db;
    String warehouseId;
    RecyclerView rv;
    RVAadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_balance);
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
        liststock=new ArrayList<>();
        rv=(RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setFocusable(false);
        db=new SqliteHandler(this);
        HashMap<String,String> user=db.getUserDetails();
        warehouseId=user.get("warehouse_id");
        SearchView search = (SearchView) findViewById(R.id.search);
        search.setOnQueryTextListener(listener);
        getDataStock();
    }
    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String query) {
            query = query.toLowerCase();

            final List<ModelStock> filteredList = new ArrayList<>();

            for (int i = 0; i < liststock.size(); i++) {

                final String text = liststock.get(i).getProduct().toLowerCase();
                if (text.contains(query)) {

                    filteredList.add(liststock.get(i));
                }
            }

            rv.setLayoutManager(new LinearLayoutManager(StockBalance.this));
            adapter = new RVAadapter(filteredList, StockBalance.this);
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();  // data set changed
            return true;

        }
        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    };

    private void getDataStock() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_getstockbalance +warehouseId, new Response.Listener<String>() {
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
                        Toast.makeText(StockBalance.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                liststock.clear();
                adapter = new RVAadapter(liststock, StockBalance.this);
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
            ModelStock Items = new ModelStock();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Items.setProduct(jsonObject.getString("product_name"));
                Items.setCategory(jsonObject.getString("category"));
                String stock=jsonObject.getString("stock");
                JSONArray jsonstock=new JSONArray(stock);
                for (int j=0;j<jsonstock.length();j++){
                    JSONObject jstock=(JSONObject) jsonstock.get(j);
                    Items.setQty(jstock.getString("qty"));
                    Items.setUom(jstock.getString("uom"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && liststock.size() != 0) {
                liststock.clear();
                liststock.add(Items);
            } else if (i == 0) {
                liststock.add(Items);
            } else {
                liststock.add(Items);
            }
        }
        adapter = new RVAadapter(liststock, this);
        rv.setAdapter(adapter);
    }

}
