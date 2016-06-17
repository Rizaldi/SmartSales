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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.HashMap;

import smartsales.rizaldi.com.smartsales.LoginActivity;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Sales.salestarget.Salestarget;
import smartsales.rizaldi.com.smartsales.session.SessionManager;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class MenuManager extends AppCompatActivity implements View.OnClickListener {
    LinearLayout approval,salestarget,report;
    SqliteHandler db;
    String position_status;
    private SessionManager session;
    TextView gettime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        String weekday = new DateFormatSymbols().getWeekdays()[today.weekDay + 1];
        String month = new DateFormatSymbols().getMonths()[today.month];
        gettime = (TextView) findViewById(R.id.gettime);
        gettime.setText("Today-" + weekday + ", " + today.monthDay + " " + month + " " + today.year);
        setTitle("Menu Manager");
        session = new SessionManager(getApplicationContext());
        db = new SqliteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        position_status = user.get("position_status");
        if (!session.isLoggedIn()) {
            logoutUser();
        }
        variable();
    }

    private void variable() {
        approval=(LinearLayout) findViewById(R.id.approval);
        salestarget=(LinearLayout) findViewById(R.id.salestarget);
        report=(LinearLayout) findViewById(R.id.report);
        approval.setOnClickListener(this);
        salestarget.setOnClickListener(this);
        report.setOnClickListener(this);
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUser();
        Intent intent = new Intent(MenuManager.this, LoginActivity.class);
        startActivity(intent);
        finish();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.approval:
                startActivity(new Intent(MenuManager.this,MenuApproval.class));
                break;
            case R.id.salestarget:
                startActivity(new Intent(MenuManager.this,Salestarget.class));
                break;
            case R.id.report:
                startActivity(new Intent(MenuManager.this,MenuReportManager.class));
                break;
        }
    }
}
