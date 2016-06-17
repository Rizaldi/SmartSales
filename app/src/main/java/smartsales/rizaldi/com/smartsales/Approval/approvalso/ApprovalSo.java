package smartsales.rizaldi.com.smartsales.Approval.approvalso;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
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
import android.widget.DatePicker;
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

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class ApprovalSo extends AppCompatActivity {
    Spinner salesspinner;
    CardView card;
    TextView tvdate;
    RecyclerView rvsalesrep;
    private List<ModelSalesRep> list;
    private List<ModelApprovalSo> listapproval;
    int year_x, month_x, day_x;
    String idsalesrep = "-1",positionStatus="";
    static final int Dialog_Id = 0;
    SqliteHandler db;
    RVAApproval adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_so);
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
        initialvariable();
        list = new ArrayList<>();
        listapproval=new ArrayList<>();
        db=new SqliteHandler(this);
        HashMap<String,String> user=db.getUserDetails();
        positionStatus=user.get("position_status");
        Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        getDataSalesRep();
        getDataApproval("","");

        showDialogClick();
        rvsalesrep.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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
    private void getDataApproval(String key, String value) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_listapproval+positionStatus+"&"+
                key+"="+value, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code=jsonObject.getString("code");
                    if(code.equals("1")){
                        String data = jsonObject.getString("data");
                        jsonArray = new JSONArray(data);
                        parseDataList(jsonArray);
                    }else{
                        Toast.makeText(ApprovalSo.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                listapproval.clear();
                adapter = new RVAApproval(listapproval, ApprovalSo.this);
                rvsalesrep.setAdapter(adapter);
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
        for (i = 0; i<jsonArray.length(); i++){
            ModelApprovalSo Items = new ModelApprovalSo();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Items.setId(jsonObject.getString("id"));
                Items.setDocnumber(jsonObject.getString("document_number"));
                Items.setCustname(jsonObject.getString("customer_name"));
                Items.setDate(jsonObject.getString("date_transaction"));
                Items.setTime(jsonObject.getString("time_transaction"));
                Items.setStatus(jsonObject.getString("status"));
                Items.setTotal(jsonObject.getString("total"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && listapproval.size() != 0) {
                listapproval.clear();
                listapproval.add(Items);
            } else if (i == 0) {
                listapproval.add(Items);
            } else {
                listapproval.add(Items);
            }
        }
        adapter = new RVAApproval(listapproval,this);
        rvsalesrep.setAdapter(adapter);
    }

    public void showDialogClick() {
        card = (CardView) findViewById(R.id.div);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Dialog_Id);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == Dialog_Id)
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear + 1;
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
            SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
            Date date = new Date(year, monthOfYear, dayOfMonth - 1);
            String dayOfWeek = simpledateformat.format(date);
            String month = new DateFormatSymbols().getMonths()[month_x - 1];
//            tvdate.setText(dayOfWeek + "," + day_x + " " + month + " " + year_x);
            tvdate.setText(year_x + "-" + bulan + "-" + tanggal);
            String tgl=year_x + "-" + bulan + "-" + tanggal;
            getDataApproval("Startdate",tgl);
        }
    };


    private void getDataSalesRep() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_salesrep, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String data = jsonObject.getString("data");
                    jsonArray = new JSONArray(data);
                    parseData(jsonArray);
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

    private void parseData(JSONArray array) {
        placeholderSpinnerSales();
        for (int i = 0; i < array.length(); i++) {
            ModelSalesRep Items = new ModelSalesRep();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setNama(json.getString("text"));
                Items.setId(json.getString("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            list.add(Items);
        }
        salesspinner.setAdapter(new MyAdapter(ApprovalSo.this, R.layout.sales_spinner,
                list));
    }

    private void placeholderSpinnerSales() {
        ModelSalesRep placeholder = new ModelSalesRep();
        placeholder.setNama("Pilih Sales Rep");
        placeholder.setId("-1");
        list.add(placeholder);
    }

    public class MyAdapter extends ArrayAdapter {
        List<ModelSalesRep> listSales;

        public MyAdapter(Context context, int resource, List<ModelSalesRep> listSales) {
            super(context, resource, listSales);
            this.listSales = listSales;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.sales_spinner, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_sales);
            id.setText(listSales.get(position).getId());
            TextView tvsales = (TextView) layout
                    .findViewById(R.id.nama_sales);
            tvsales.setText(listSales.get(position).getNama());
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

    private void initialvariable() {
        salesspinner = (Spinner) findViewById(R.id.salesspinner);
        tvdate = (TextView) findViewById(R.id.date);
        rvsalesrep = (RecyclerView) findViewById(R.id.rvsalesrep);
        rvsalesrep.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rvsalesrep.setLayoutManager(llm);
        rvsalesrep.setFocusable(false);
        salesspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    idsalesrep = ((TextView) view.findViewById(R.id.id_sales)).getText().toString();
                    if(!idsalesrep.equals("-1")){
                        getDataApproval("salesRepID", idsalesrep);
                    }else{
                        getDataApproval("", "");
                    }
                } catch (Exception e) {
                    Log.e("cek id", "tidak ada data");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
