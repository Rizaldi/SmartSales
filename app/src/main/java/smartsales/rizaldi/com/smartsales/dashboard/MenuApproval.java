package smartsales.rizaldi.com.smartsales.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;

import smartsales.rizaldi.com.smartsales.Approval.Itenerary.Itinerary;
import smartsales.rizaldi.com.smartsales.Approval.approvalso.ApprovalSo;
import smartsales.rizaldi.com.smartsales.Approval.goodreturn.Goodreturnapprove;
import smartsales.rizaldi.com.smartsales.Approval.refillvan.RefillRequest;
import smartsales.rizaldi.com.smartsales.R;

public class MenuApproval extends AppCompatActivity implements View.OnClickListener {
    LinearLayout itinerary,approvalso,approvalrefill,approvalreturn;
    TextView gettime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_approval);
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
        variable();
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        String weekday = new DateFormatSymbols().getWeekdays()[today.weekDay + 1];
        String month = new DateFormatSymbols().getMonths()[today.month];
        gettime = (TextView) findViewById(R.id.gettime);
        gettime.setText("Today-" + weekday + ", " + today.monthDay + " " + month + " " + today.year);

    }

    private void variable() {
        itinerary=(LinearLayout) findViewById(R.id.itinerary);
        approvalso=(LinearLayout) findViewById(R.id.approvalso);
        approvalrefill=(LinearLayout) findViewById(R.id.approvalrefill);
        approvalreturn=(LinearLayout) findViewById(R.id.approvalreturn);
        itinerary.setOnClickListener(this);
        approvalso.setOnClickListener(this);
        approvalrefill.setOnClickListener(this);
        approvalreturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.itinerary:
                startActivity(new Intent(MenuApproval.this, Itinerary.class));
                break;
            case R.id.approvalso:
                startActivity(new Intent(MenuApproval.this, ApprovalSo.class));
                break;
            case R.id.approvalrefill:
                startActivity(new Intent(MenuApproval.this, RefillRequest.class));
                break;
            case R.id.approvalreturn:
                startActivity(new Intent(MenuApproval.this, Goodreturnapprove.class));
                break;
        }
    }
}
