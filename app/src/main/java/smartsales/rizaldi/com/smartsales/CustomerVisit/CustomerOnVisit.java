package smartsales.rizaldi.com.smartsales.CustomerVisit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.customerorder.C_Order_form;
import smartsales.rizaldi.com.smartsales.session.AppController;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

/**
 * Created by Toshiba-PC on 4/16/2016.
 */
public class CustomerOnVisit extends Fragment {
    int year_x, month_x, day_x;
    SqliteHandler db;
    FragmentActivity c;
    String sales_id;
    List<ModelListOnVisit> scheduleList;
    String date;
    TextView schedule_id, visiting, customer_id, customer, customer_address;
    ImageButton chekout, view,salesorder;
    String image = "";
    ImageView iv;
    boolean viewImage = true;
    String getdata = "";
    LinearLayout place,nodata;
    public CustomerOnVisit() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //construct a joiner
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.customer_visit, container, false);
        c = getActivity();
        iv = (ImageView) v.findViewById(R.id.img);
        chekout = (ImageButton) v.findViewById(R.id.chekout);
        view = (ImageButton) v.findViewById(R.id.view);
        place=(LinearLayout) v.findViewById(R.id.place);
        nodata=(LinearLayout) v.findViewById(R.id.nodata);
        salesorder=(ImageButton) v.findViewById(R.id.salesorder);
        salesorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(c, C_Order_form.class));
                getActivity().finish();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewImage) {
                    if (getdata.equals("1")) {
                        Picasso.with(getContext()).load("http://" + image).into(iv);
//
                    }
                    iv.setVisibility(View.VISIBLE);
                    view.setImageResource(R.drawable.ic_visibility_off_white_36dp);
                    viewImage = false;
                } else {
                    iv.setVisibility(View.GONE);
                    view.setImageResource(R.drawable.view);
                    viewImage = true;
                }

            }
        });

        chekout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getdata.equals("1")) {
                    Bundle b = new Bundle();
                    b.putString("salesId", sales_id);
                    b.putString("scheduleId", schedule_id.getText().toString());
                    b.putString("customerId", customer_id.getText().toString());
                    b.putString("asal", "out");
                    Intent i = new Intent(c, CheckInOut.class);
                    i.putExtras(b);
                    startActivity(i);
                    getActivity().finish();
                }
            }
        });


        db = new SqliteHandler(c);
        HashMap<String, String> user = db.getUserDetails();
        sales_id = user.get("employee_id");
        scheduleList = new ArrayList<>();
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        String mbulan, mtanggal;
        if (month_x < 10) {
            mbulan = "0" + (month_x + 1);
        } else {
            mbulan = String.valueOf(month_x + 1);
        }
        if (day_x < 10) {
            mtanggal = "0" + day_x;
        } else {
            mtanggal = String.valueOf(day_x);
        }
        date = today.year + "-" + mbulan + "-" + mtanggal;
        schedule_id = (TextView) v.findViewById(R.id.schedule_id);
        visiting = (TextView) v.findViewById(R.id.visiting);
        customer_id = (TextView) v.findViewById(R.id.customer_id);
        customer = (TextView) v.findViewById(R.id.customer);
        customer_address = (TextView) v.findViewById(R.id.customer_address);
        getData(date, sales_id);
        return v;
    }

    private void getData(String date, String sales_id) {
        String tag_string_req = "req_onvisit";
        final ProgressDialog loading = new ProgressDialog(c);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlLib.url_onvisit + date + "&salesId=" + sales_id + "&status=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                try {
                    JSONObject jobj = new JSONObject(s);
                    String code = jobj.getString("code");
                    getdata = code;
                    if (code.equals("1")) {
                        JSONArray jsonArray = jobj.getJSONArray("data");
                        parseData(jsonArray);
                        place.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.GONE);
                    } else {
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
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                customer_id.setText(json.getString("customer_id"));
                schedule_id.setText(json.getString("schedule_id"));
                customer.setText(json.getString("customer"));
                customer_address.setText(json.getString("customer_address"));
                visiting.setText(json.getString("visiting"));
                image = json.getString("url_checkin");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
