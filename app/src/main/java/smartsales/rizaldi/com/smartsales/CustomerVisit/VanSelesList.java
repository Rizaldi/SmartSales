package smartsales.rizaldi.com.smartsales.CustomerVisit;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import smartsales.rizaldi.com.smartsales.Approval.Itenerary.List_Schedule;
import smartsales.rizaldi.com.smartsales.DateToday;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.session.AppController;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

/**
 * Created by Toshiba-PC on 4/19/2016.
 */
public class VanSelesList extends Fragment {
    FragmentActivity c;
    SqliteHandler db;
    String sales_id;
    String date;
    List<ListVanSales> scheduleList;
    RecyclerView rv;
    RVAvanSales adapter;


    public VanSelesList() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //construct a joiner
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.vanseleslist, container, false);
        c = getActivity();
        db=new SqliteHandler(c);
        HashMap<String,String> user=db.getUserDetails();
        sales_id=user.get("employee_id");
        scheduleList=new ArrayList<>();
        rv = (RecyclerView)v.findViewById(R.id.listCustomer);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        date= DateToday.dateNow();
        SearchView search = (SearchView) v.findViewById(R.id.search);
        search.setOnQueryTextListener(listener);
        getData(sales_id);
        return v;
    }
    SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextChange(String query) {
            query = query.toLowerCase();

            final List<ListVanSales> filteredList = new ArrayList<>();

            for (int i = 0; i < scheduleList.size(); i++) {

                final String text = scheduleList.get(i).getNama().toLowerCase();
                if (text.contains(query)) {

                    filteredList.add(scheduleList.get(i));
                }
            }

            rv.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new RVAvanSales(filteredList, getContext());
            rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();  // data set changed
            return true;

        }
        public boolean onQueryTextSubmit(String query) {
            return false;
        }
    };

    private void getData(String sales_id) {
        String tag_string_req="req_vansales";
        final ProgressDialog loading=new ProgressDialog(c);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();

        StringRequest strReq=new StringRequest(Request.Method.GET, UrlLib.url_customer+sales_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                try {
                    JSONObject jobj=new JSONObject(s);
                    String code=jobj.getString("code");
                    if(code.equals("1")){
                        JSONArray jsonArray=jobj.getJSONArray("data");
                        parseData(jsonArray);
                    }else{
                        Toast.makeText(getContext(), jobj.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(),
                        volleyError.getMessage(), Toast.LENGTH_LONG).show();
                loading.dismiss();
            }
        }
        );
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void parseData(JSONArray array) {
        for (int i=0;i<array.length();i++){
            ListVanSales Items=new ListVanSales();
            JSONObject json=null;
            try{
                json=array.getJSONObject(i);
                Items.setId(json.getString("id"));
                Items.setNama(json.getString("text"));
                Items.setVisiting(json.getString("visiting"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            scheduleList.add(Items);
        }
        adapter = new RVAvanSales(scheduleList,c);
//        adapter.setTgl(date);
        rv.setAdapter(adapter);
    }

}
