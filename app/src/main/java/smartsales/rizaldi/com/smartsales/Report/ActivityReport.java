package smartsales.rizaldi.com.smartsales.Report;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Report.goodreturn.ReportGoodReturn;
import smartsales.rizaldi.com.smartsales.Report.topcustomer.Topcustomer;
import smartsales.rizaldi.com.smartsales.Report.transaction.Transactionreport;

public class ActivityReport extends AppCompatActivity implements View.OnClickListener {
    CardView transaction, topcustomer, reportrevil, reportreturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_report);
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
        initial();

    }

    private void initial() {
        transaction = (CardView) findViewById(R.id.transaction);
        transaction.setOnClickListener(this);
        topcustomer = (CardView) findViewById(R.id.topcustomer);
        topcustomer.setOnClickListener(this);
        reportrevil = (CardView) findViewById(R.id.reportrevil);
        reportrevil.setOnClickListener(this);
        reportreturn = (CardView) findViewById(R.id.reportreturn);
        reportreturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.transaction:
                startActivity(new Intent(ActivityReport.this, Transactionreport.class));
                break;
            case R.id.topcustomer:
                startActivity(new Intent(ActivityReport.this, Topcustomer.class));
                break;
            case R.id.reportrevil:

                break;
            case R.id.reportreturn:
                startActivity(new Intent(ActivityReport.this, ReportGoodReturn.class));
                break;


        }
    }
}
