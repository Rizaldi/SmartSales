package smartsales.rizaldi.com.smartsales.customerorder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.BasicNetwork;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class Payment extends AppCompatActivity implements View.OnClickListener {
    SqliteHandler db;
    EditText qtypayment;
    TextView total, change;
    private String organizationId = "", warehouseId = "", position_status = "", username = "", employee_id = "",idSo="";
    ImageButton ibsave;
    Double kembali=0.0,totalPayment=0.0,payment=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        qtypayment = (EditText) findViewById(R.id.qtypayment);
        total = (TextView) findViewById(R.id.total);
        change = (TextView) findViewById(R.id.change);
        ibsave = (ImageButton) findViewById(R.id.ibsave);
        ibsave.setOnClickListener(this);
        Bundle b = getIntent().getExtras();
        total.setText(b.getString("total"));
        idSo=b.getString("idSo");
        db = new SqliteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        organizationId = user.get("organization_id");
        warehouseId = user.get("warehouse_id");
        position_status = user.get("position_status");
        username = user.get("username");
        employee_id = user.get("employee_id");
        if(position_status.equals("1")){
            qtypayment.setText(b.getString("total"));
            qtypayment.setFocusable(false);
            change.setText("0");
        }
//        qtypayment.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                kembali=Integer.parseInt(total.getText().toString())-Integer.parseInt(String.valueOf(s));
//                change.setText(kembali);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibsave:
                if(!qtypayment.getText().toString().isEmpty()){
                    payment=Double.parseDouble(qtypayment.getText().toString());
                    totalPayment=Double.parseDouble(total.getText().toString());
                    if(payment>=totalPayment){
                        saveSalesOrder();
                    }else{
                        Snackbar.make(v,"check payment",Snackbar.LENGTH_LONG).show();
                    }
                }else{
                    Snackbar.make(v,"fill payment",Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void saveSalesOrder() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        final StringRequest strReq = new StringRequest(Request.Method.POST, UrlLib.url_saveSalesOrder, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                JSONArray jsonArray = null;
                try {
                    Log.e("Json",s);
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    String msg = jsonObject.getString("message");
                    if (code.equals("1")) {
                        ParamInput.idcustomer="";
                        ParamInput.customerLocationId="";
                        ParamInput.idSO="";
                        ParamInput.anydata=false;
                        ParamInput.deliveristatus="";
                        ParamInput.DeliveryDate="";
                        ParamInput.PoDate="";
                        ParamInput.TotalHarga=0.0;
                        ParamInput.PoNumber="";
//                        kembali=payment-totalPayment;
//                        change.setText(String.valueOf(kembali));
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Payment.this,C_Order_form.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e("error GC", e.getMessage());
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
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("organizationId", organizationId);
                params.put("warehouseId", warehouseId);
                params.put("customerId", ParamInput.idcustomer);
                params.put("customerLocationId", ParamInput.customerLocationId);
                params.put("employeeId", employee_id);
                params.put("positionStatus", position_status);
                params.put("username", username);
                params.put("idSO",idSo );
                params.put("poNumber",ParamInput.PoNumber );
                params.put("poDate",ParamInput.PoDate );
                params.put("deliveryStatus",ParamInput.deliveristatus );
                params.put("deliveryDate",ParamInput.DeliveryDate );
                params.put("payment",qtypayment.getText().toString() );
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);

    }
}
