package smartsales.rizaldi.com.smartsales.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.HashMap;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Report.customeraging.CustomerAging;
import smartsales.rizaldi.com.smartsales.Report.efectivecall.EffectiveCalls;
import smartsales.rizaldi.com.smartsales.Report.goodreturn.ReportGoodReturn;
import smartsales.rizaldi.com.smartsales.Report.proposeso.ProposeSo;
import smartsales.rizaldi.com.smartsales.Report.refilvan.Refillvan;
import smartsales.rizaldi.com.smartsales.Report.topcustomer.Topcustomer;
import smartsales.rizaldi.com.smartsales.Report.transaction.Transactionreport;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class MenuReport extends AppCompatActivity implements View.OnClickListener {
    TextView gettime;
    SqliteHandler db;
    String position_status;
    LinearLayout efectivecall,transaction,goodreturn,top10,customeraging,psoreport,refillvan;
//
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new SqliteHandler(this);
        HashMap<String,String> user=db.getUserDetails();
        position_status=user.get("position_status");
        if(position_status.equals("1")){
            setContentView(R.layout.activity_menu_report);
            customeraging=(LinearLayout) findViewById(R.id.customeraging);
            customeraging.setOnClickListener(this);
        }else{
            setContentView(R.layout.activity_menu_reportvansales);
            psoreport=(LinearLayout) findViewById(R.id.psoreport);
            refillvan=(LinearLayout) findViewById(R.id.refillvan);
            psoreport.setOnClickListener(this);
            refillvan.setOnClickListener(this);
        }
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

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        String weekday = new DateFormatSymbols().getWeekdays()[today.weekDay + 1];
        String month = new DateFormatSymbols().getMonths()[today.month];
        gettime = (TextView) findViewById(R.id.gettime);
        gettime.setText("Today-" + weekday + ", " + today.monthDay + " " + month + " " + today.year);
        variable();

    }

    private void variable() {
        efectivecall=(LinearLayout) findViewById(R.id.efectivecall);
        transaction=(LinearLayout) findViewById(R.id.transaction);
        goodreturn=(LinearLayout) findViewById(R.id.goodreturn);
        top10=(LinearLayout) findViewById(R.id.top10);
        efectivecall.setOnClickListener(this);
        transaction.setOnClickListener(this);
        goodreturn.setOnClickListener(this);
        top10.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.transaction:
                startActivity(new Intent(MenuReport.this, Transactionreport.class));
                break;
            case R.id.goodreturn:
                startActivity(new Intent(MenuReport.this, ReportGoodReturn.class));
                break;
            case R.id.top10:
                startActivity(new Intent(MenuReport.this, Topcustomer.class));
                break;
            case R.id.psoreport:
                startActivity(new Intent(MenuReport.this, ProposeSo.class));
                break;
            case R.id.refillvan:
                startActivity(new Intent(MenuReport.this, Refillvan.class));
                break;
            case R.id.customeraging:
                startActivity(new Intent(MenuReport.this, CustomerAging.class));
                break;
            case R.id.efectivecall:
                startActivity(new Intent(MenuReport.this, EffectiveCalls.class));
                break;
        }
    }
}
