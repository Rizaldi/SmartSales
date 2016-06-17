package smartsales.rizaldi.com.smartsales.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Report.customeraging.CustomerAging;
import smartsales.rizaldi.com.smartsales.Report.efectivecall.EffectiveCalls;
import smartsales.rizaldi.com.smartsales.Report.goodreturn.ReportGoodReturn;
import smartsales.rizaldi.com.smartsales.Report.proposeso.ProposeSo;
import smartsales.rizaldi.com.smartsales.Report.refilvan.Refillvan;
import smartsales.rizaldi.com.smartsales.Report.topcustomer.Topcustomer;
import smartsales.rizaldi.com.smartsales.Report.transaction.Transactionreport;
import smartsales.rizaldi.com.smartsales.Sales.Propose.Propose;

public class MenuReportManager extends AppCompatActivity implements View.OnClickListener {
    LinearLayout efectivecall,psoreport,transaction,goodreturn,refillvan,customeraging,tracking,top10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_report_manager);
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
        initUI();
    }

    private void initUI() {
        efectivecall=(LinearLayout)findViewById(R.id.efectivecall);
        psoreport=(LinearLayout)findViewById(R.id.psoreport);
        transaction=(LinearLayout)findViewById(R.id.transaction);
        goodreturn=(LinearLayout)findViewById(R.id.goodreturn);
        refillvan=(LinearLayout)findViewById(R.id.refillvan);
        customeraging=(LinearLayout)findViewById(R.id.customeraging);
        tracking=(LinearLayout)findViewById(R.id.tracking);
        top10=(LinearLayout)findViewById(R.id.top10);
        efectivecall.setOnClickListener(this);
        psoreport.setOnClickListener(this);
        transaction.setOnClickListener(this);
        goodreturn.setOnClickListener(this);
        refillvan.setOnClickListener(this);
        customeraging.setOnClickListener(this);
        tracking.setOnClickListener(this);
        top10.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==efectivecall){
            startActivity(new Intent(MenuReportManager.this, EffectiveCalls.class));
        }
        if (v==psoreport){
            startActivity(new Intent(MenuReportManager.this, ProposeSo.class));
        }
        if (v==transaction){
            startActivity(new Intent(MenuReportManager.this, Transactionreport.class));
        }
        if (v==goodreturn){
            startActivity(new Intent(MenuReportManager.this, ReportGoodReturn.class));
        }
        if (v==refillvan){
            startActivity(new Intent(MenuReportManager.this, Refillvan.class));
        }
        if (v==customeraging){
            startActivity(new Intent(MenuReportManager.this, CustomerAging.class));
        }
        if(v==tracking){

        }
        if(v==top10){
            startActivity(new Intent(MenuReportManager.this, Topcustomer.class));
        }
    }
}
