package smartsales.rizaldi.com.smartsales.Sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import smartsales.rizaldi.com.smartsales.Approval.Itenerary.Itinerary;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.CustomerVisit.CheckInOut;
import smartsales.rizaldi.com.smartsales.Sales.good_return.GoodsReturn;
import smartsales.rizaldi.com.smartsales.Sales.reffill_van.RefillVan;

public class Sales extends AppCompatActivity implements View.OnClickListener {
    Button btnItenerary,btnCheckInOut,btnRefill,btnGoodreturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnItenerary = (Button) findViewById(R.id.btnItenerary);
        btnCheckInOut = (Button) findViewById(R.id.btnCheckInOut);
        btnRefill = (Button) findViewById(R.id.btnRefill);
        btnGoodreturn = (Button) findViewById(R.id.btnGoodreturn);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnItenerary.setOnClickListener(this);
        btnCheckInOut.setOnClickListener(this);
        btnRefill.setOnClickListener(this);
        btnGoodreturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnItenerary){
            Intent i = new Intent(getApplicationContext(), Itinerary.class);
            startActivity(i);
        }
        if (v == btnCheckInOut){
            Intent i = new Intent(getApplicationContext(), CheckInOut.class);
            startActivity(i);
        }
        if (v == btnRefill){
            Intent i = new Intent(getApplicationContext(), RefillVan.class);
            startActivity(i);
        }
        if (v == btnGoodreturn){
            Intent i = new Intent(getApplicationContext(), GoodsReturn.class);
            startActivity(i);
        }
    }
}
