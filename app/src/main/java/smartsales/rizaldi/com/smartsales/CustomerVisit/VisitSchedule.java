package smartsales.rizaldi.com.smartsales.CustomerVisit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartsales.rizaldi.com.smartsales.Approval.Itenerary.List_Schedule;
import smartsales.rizaldi.com.smartsales.DateToday;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.session.AppController;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

/**
 * Created by Toshiba-PC on 4/15/2016.
 */
public class VisitSchedule extends Fragment {
    TextView tgl;
    String tanggal;
    String bulan;
    String tahun,date;
    int year_x, month_x, day_x;
    static final int Dialog_Id = 0;
    FragmentActivity c;
    SqliteHandler db;
    String sales_id;
    List<List_Schedule> scheduleList;
    RecyclerView rv;
    RVAadapter adapter;
    public VisitSchedule() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //construct a joiner
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.visit_schedule, container, false);
        c = getActivity();
        db=new SqliteHandler(c);
        HashMap<String,String> user=db.getUserDetails();
        sales_id=user.get("employee_id");
        scheduleList=new ArrayList<>();
        rv = (RecyclerView)v.findViewById(R.id.listCustomer);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        String weekday;
        String month;
        weekday = new DateFormatSymbols().getWeekdays()[today.weekDay + 1];
        month = new DateFormatSymbols().getMonths()[today.month];
        tgl = (TextView) v.findViewById(R.id.tvdate);
        Toast.makeText(getContext(),date,Toast.LENGTH_LONG).show();
        date= DateToday.dateNow();
        tgl.setText(weekday + ", " + today.monthDay + " " + month + " " + today.year);

        try {
            Bundle b = c.getIntent().getExtras();
            tanggal = b.getString("tanggal");
            bulan=b.getString("bulan");
            tahun=b.getString("tahun");
            String tbulan="", ttanggal="";
            if (Integer.parseInt(bulan) < 10 ) {
                tbulan = "0" + (Integer.parseInt(bulan)+1);
            } else {
                tbulan = String.valueOf(Integer.parseInt(bulan)+1);
            }
            if (Integer.parseInt(tanggal) < 10) {
                ttanggal = "0" + tanggal;
            } else {
                ttanggal = tanggal;
            }

            if (!tanggal.isEmpty()) {
                date=tahun+"-"+tbulan+"-"+ttanggal;
                tgl.setText(date);
            }
        } catch (Exception e) {

        }
//            getlist data

        getData(date,sales_id);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fabcalender);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(c, SelectDate.class);
                c.startActivity(i);
                getActivity().finish();
            }
        });
        return v;
    }

    private void getData(String date, String sales_id) {
        String tag_string_req="req_jadwal";
        final ProgressDialog loading=new ProgressDialog(c);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();

        StringRequest strReq=new StringRequest(Request.Method.POST, UrlLib.url_jadwal+date+"&salesId="+sales_id, new Response.Listener<String>() {
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
                        Toast.makeText(getContext(),jobj.getString("message"),Toast.LENGTH_LONG).show();
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
            List_Schedule Items=new List_Schedule();
            JSONObject json=null;
            try{
                json=array.getJSONObject(i);
                Items.setCustomer_id(json.getString("customer_id"));
                Items.setSchedule_id(json.getString("schedule_id"));
                Items.setCustomer(json.getString("customer"));
                Items.setCustomer_address(json.getString("customer_address"));
                Items.setVisiting(json.getString("visiting"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            scheduleList.add(Items);
        }
        adapter = new RVAadapter(scheduleList,c);
        adapter.setTgl(date);
        rv.setAdapter(adapter);
    }


}
