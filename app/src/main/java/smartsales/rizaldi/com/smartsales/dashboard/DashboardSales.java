package smartsales.rizaldi.com.smartsales.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.HashMap;

import smartsales.rizaldi.com.smartsales.CustomerVisit.CustomerVisit;
import smartsales.rizaldi.com.smartsales.CustomerVisit.VisitVanSeles;
import smartsales.rizaldi.com.smartsales.LoginActivity;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Report.ActivityReport;
import smartsales.rizaldi.com.smartsales.Sales.Sales;
import smartsales.rizaldi.com.smartsales.session.SessionManager;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class DashboardSales extends AppCompatActivity implements View.OnClickListener {
    LinearLayout customer_visit, sales, stock, report;
    SqliteHandler db;
    String position_status;
    private SessionManager session;
    TextView gettime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_sales);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        String weekday = new DateFormatSymbols().getWeekdays()[today.weekDay + 1];
        String month = new DateFormatSymbols().getMonths()[today.month];
        gettime = (TextView) findViewById(R.id.gettime);
        gettime.setText("Today-" + weekday + ", " + today.monthDay + " " + month + " " + today.year);

        customer_visit = (LinearLayout) findViewById(R.id.customer_visit);
        sales = (LinearLayout) findViewById(R.id.sales);
        stock = (LinearLayout) findViewById(R.id.stock);
        report = (LinearLayout) findViewById(R.id.report);
        customer_visit.setOnClickListener(this);
        sales.setOnClickListener(this);
        stock.setOnClickListener(this);
        report.setOnClickListener(this);
        session = new SessionManager(getApplicationContext());
        db = new SqliteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        position_status = user.get("position_status");
        if (!session.isLoggedIn()) {
            logoutUser();
        }

    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUser();
        Intent intent = new Intent(DashboardSales.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customer_visit:
                if (position_status.equals("1")) {
                    Intent i = new Intent(getApplicationContext(), CustomerVisit.class);
                    startActivity(i);
                } else if (position_status.equals("2")) {
                    Intent i = new Intent(getApplicationContext(), VisitVanSeles.class);
                    startActivity(i);
                }
                break;
            case R.id.sales:
                Intent i = new Intent(getApplicationContext(), MenuSales.class);
                startActivity(i);
                break;
            case R.id.stock:
                startActivity(new Intent(getApplicationContext(), MenuStock.class));
                break;
            case R.id.report:
                Intent in = new Intent(getApplicationContext(), MenuReport.class);
                startActivity(in);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logoutUser();
        }

        return super.onOptionsItemSelected(item);
    }

}
