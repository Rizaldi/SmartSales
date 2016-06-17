package smartsales.rizaldi.com.smartsales.Approval;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;

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
import java.util.Map;

import smartsales.rizaldi.com.smartsales.Approval.AdapterModelApproval.CustomApproval;
import smartsales.rizaldi.com.smartsales.Approval.AdapterModelApproval.ModelApprovalMenu;
import smartsales.rizaldi.com.smartsales.Approval.AdapterModelSetup.CustomItinerary;
import smartsales.rizaldi.com.smartsales.Approval.approvalso.ApprovalSo;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;

public class Approval extends AppCompatActivity implements View.OnClickListener {
    CardView itinerary,approvalso,approvalrevil,approvalreturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
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
        getSupportActionBar().setTitle(" Select Menu..");
        initial();
    }

    private void initial() {
        itinerary=(CardView) findViewById(R.id.itinerary);
        itinerary.setOnClickListener(this);
        approvalso=(CardView) findViewById(R.id.approvalso);
        approvalso.setOnClickListener(this);
        approvalrevil=(CardView) findViewById(R.id.approvalrevil);
        approvalrevil.setOnClickListener(this);
        approvalreturn=(CardView) findViewById(R.id.approvalreturn);
        approvalreturn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.itinerary:

                break;
            case R.id.approvalso:
                startActivity(new Intent(Approval.this, ApprovalSo.class));
                break;
            case R.id.approvalrevil:

                break;
            case R.id.approvalreturn:

                break;


        }
    }
}
