package smartsales.rizaldi.com.smartsales.Report.refilvan;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
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
import java.util.Calendar;
import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;

public class Refillvan extends AppCompatActivity {
    RelativeLayout startdate, enddate;
    RecyclerView rv;
    static final int Dialog_Id = 0, Dialog_Id2 = 1;
    int year_x, month_x, day_x;
    TextView tvstartdate, tvenddate;
    String start = "", end = "";
    private List<ModelRefillvan> list;
    RVAadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refillvan);
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
        Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        initial();
        showDialogStartDate();
        showDialogEndDate();
        list=new ArrayList<>();
        getDataRefill("", "");

    }

    private void getDataRefill(String key, String val) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_reportrefillhistory + "?" +
                key + "=" + val, new Response.Listener<String>() {
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
                        Toast.makeText(Refillvan.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                adapter = new RVAadapter(list, Refillvan.this);
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
            ModelRefillvan Items = new ModelRefillvan();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Items.setId(jsonObject.getString("id"));
                Items.setDocumentname(jsonObject.getString("document_name"));
                Items.setRequestedby(jsonObject.getString("requested_by"));
                Items.setRequeteddate(jsonObject.getString("requested_date"));
                Items.setStatus(jsonObject.getString("status"));
                Items.setItem(jsonObject.getString("item"));
                Items.setStatus(jsonObject.getString("status"));
                Items.setRequestedtime(jsonObject.getString("requested_time"));
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

    private void showDialogEndDate() {
        enddate = (RelativeLayout) findViewById(R.id.enddate);
        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Dialog_Id2);
            }
        });
    }

    private void showDialogStartDate() {
        startdate = (RelativeLayout) findViewById(R.id.startdate);
        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Dialog_Id);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == Dialog_Id) {
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        } else if (id == Dialog_Id2) {
            return new DatePickerDialog(this, dpickerListener2, year_x, month_x, day_x);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear+1;
            day_x = dayOfMonth;
            String bulan, tanggal;
            if (month_x < 10) {
                bulan = "0" + month_x;
            } else {
                bulan = String.valueOf(month_x);
            }
            if (day_x < 10) {
                tanggal = "0" + day_x;
            } else {
                tanggal = String.valueOf(day_x);
            }
            start = year_x + "-" + bulan + "-" + tanggal;
            tvstartdate.setText(start);
            getDataRefill("startDate", start);
        }
    };
    private DatePickerDialog.OnDateSetListener dpickerListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear+1;
            day_x = dayOfMonth;
            String bulan, tanggal;
            if (month_x < 10) {
                bulan = "0" + month_x;
            } else {
                bulan = String.valueOf(month_x);
            }
            if (day_x < 10) {
                tanggal = "0" + day_x;
            } else {
                tanggal = String.valueOf(day_x);
            }
            end = year_x + "-" + bulan + "-" + tanggal;
            tvenddate.setText(end);
            getDataRefill("endDate", end);
        }
    };

    private void initial() {
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setFocusable(false);
        tvstartdate = (TextView) findViewById(R.id.date);
        tvenddate = (TextView) findViewById(R.id.date1);

    }

}
