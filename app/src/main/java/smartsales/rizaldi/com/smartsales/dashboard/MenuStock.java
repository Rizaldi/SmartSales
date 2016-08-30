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

import smartsales.rizaldi.com.smartsales.CustomerVisit.CustomerVisit;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Sales.reffill_van.RefillVan;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;
import smartsales.rizaldi.com.smartsales.stock.CustomerStock;
import smartsales.rizaldi.com.smartsales.stock.StockBalance;

public class MenuStock extends AppCompatActivity implements View.OnClickListener {
    LinearLayout stockbalance, refillvan, customerstock;
    SqliteHandler db;
    String position_status;
    TextView gettime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_stock);
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
        db = new SqliteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        position_status = user.get("position_status");
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        String weekday = new DateFormatSymbols().getWeekdays()[today.weekDay + 1];
        String month = new DateFormatSymbols().getMonths()[today.month];
        gettime = (TextView) findViewById(R.id.gettime);
        gettime.setText("Today-" + weekday + ", " + today.monthDay + " " + month + " " + today.year);

        variable();
    }

    private void variable() {
        stockbalance = (LinearLayout) findViewById(R.id.stockbalance);
        refillvan = (LinearLayout) findViewById(R.id.refillvan);
        customerstock = (LinearLayout) findViewById(R.id.customerstock);
        stockbalance.setOnClickListener(this);
        customerstock.setOnClickListener(this);
        refillvan.setOnClickListener(this);
        stockbalance.setOnClickListener(this);
        if (position_status.equals("1")) {
            refillvan.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v==stockbalance){
            startActivity(new Intent(MenuStock.this, StockBalance.class));
        }
        if(v==customerstock){
            startActivity(new Intent(MenuStock.this, CustomerStock.class));
        }
        if(v==refillvan){
            startActivity(new Intent(MenuStock.this, RefillVan.class));
        }
    }
}
