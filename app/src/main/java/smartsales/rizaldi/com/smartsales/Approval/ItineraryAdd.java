package smartsales.rizaldi.com.smartsales.Approval;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import smartsales.rizaldi.com.smartsales.Approval.Itenerary.IteneraryAddCustom;
import smartsales.rizaldi.com.smartsales.Approval.Itenerary.IteneraryModel;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;

/**
 * Created by spasi on 13/03/2016.
 */
public class ItineraryAdd extends AppCompatActivity{
    private List<IteneraryModel> iteneraryModels;
    private IteneraryAddCustom iteneraryAddCustom;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    UrlLib urlLib;

    Spinner customer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itenerary_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Add customer");
        recyclerView = (RecyclerView) findViewById(R.id.customer_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        iteneraryModels= new ArrayList<>();



        customer = (Spinner) findViewById(R.id.customer);
        addItemSpinner();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlLib.urlItinerary,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        parseData(jsonArray);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
//        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void parseData(JSONArray jsonArray) {
        for (int i = 0; i<jsonArray.length(); i++){
            IteneraryModel iteneraryModel = new IteneraryModel();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                iteneraryModel.setId_name(jsonObject.getString("id_category_lagu"));
                iteneraryModel.setName(jsonObject.getString("category_lagu"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            iteneraryModels.add(iteneraryModel);
        }
        adapter = new IteneraryAddCustom(this,iteneraryModels);
        recyclerView.setAdapter(adapter);
    }

    public void addItemSpinner() {
        List<String> list = new ArrayList<String>();
        list.add("DANIEL SIN");
        list.add("ADA 11 SDN BHD");
        list.add("GCH RETAIL (MALAYSIA) SDN BHD - BINTANG SELA");
        list.add("GCH RETAIL (MALAYSIA) S/B - BINTANG SUPER BBA");
        list.add("GCH RETAIL (MALAYSIA) SDN BHD - BINTANG SUPER CHERAS");
        list.add("GCH RETAIL (MALAYSIA) SDN BHD (ULU KELANG)");
        list.add("ONG TAI KIM SUPERMARKET SDN BHD (GOMBAK)");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, list);
        customer.setAdapter(dataAdapter);
    }
}
