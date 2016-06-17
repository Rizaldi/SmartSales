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
import smartsales.rizaldi.com.smartsales.Sales.Propose.ProposeSalesOrder;
import smartsales.rizaldi.com.smartsales.Sales.competitor.CompetitorActivity;
import smartsales.rizaldi.com.smartsales.Sales.salestarget.Salestarget;
import smartsales.rizaldi.com.smartsales.customerorder.C_Order_form;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class MenuSales extends AppCompatActivity implements View.OnClickListener {
    TextView gettime;
    SqliteHandler db;
    String position_status = "";
    LinearLayout so, pso, payment, goods, competitor, target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SqliteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        position_status = user.get("position_status");
        if (position_status.equals("1")) {
            setContentView(R.layout.activity_menu_sales);
        } else {
            setContentView(R.layout.activity_menu_vansales);
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
        if (position_status.equals("1")) {
            pso = (LinearLayout) findViewById(R.id.pso);
            payment = (LinearLayout) findViewById(R.id.payment);
            pso.setOnClickListener(this);
            payment.setOnClickListener(this);

        }
        so = (LinearLayout) findViewById(R.id.so);
        goods = (LinearLayout) findViewById(R.id.goods);
        competitor = (LinearLayout) findViewById(R.id.competitor);
        target = (LinearLayout) findViewById(R.id.target);
        so.setOnClickListener(this);
        goods.setOnClickListener(this);
        competitor.setOnClickListener(this);
        target.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.so:
                startActivity(new Intent(MenuSales.this, C_Order_form.class));
                break;
            case R.id.competitor:
                startActivity(new Intent(MenuSales.this, CompetitorActivity.class));
                break;
            case R.id.target:
                startActivity(new Intent(MenuSales.this, Salestarget.class));
                break;
            case R.id.pso:
                startActivity(new Intent(MenuSales.this, ProposeSalesOrder.class));
                break;
        }
    }
}
