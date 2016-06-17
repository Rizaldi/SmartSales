package smartsales.rizaldi.com.smartsales;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.HashMap;

import smartsales.rizaldi.com.smartsales.Approval.Approval;
import smartsales.rizaldi.com.smartsales.CustomerVisit.CustomerVisit;
import smartsales.rizaldi.com.smartsales.CustomerVisit.VisitVanSeles;
import smartsales.rizaldi.com.smartsales.Report.ActivityReport;
import smartsales.rizaldi.com.smartsales.Sales.Sales;
import smartsales.rizaldi.com.smartsales.session.SessionManager;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button approval, sales, stock, report, customer_visit;
    UrlLib urlLib;
    private SessionManager session;
    SqliteHandler db;
    ImageButton add;
    String position_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(" Smart Sales");
        getSupportActionBar().setIcon(R.drawable.tas);

        approval = (Button) findViewById(R.id.approval);
        sales = (Button) findViewById(R.id.sales);
        stock = (Button) findViewById(R.id.stock);
        report = (Button) findViewById(R.id.report);
        customer_visit = (Button) findViewById(R.id.customer_visit);
        approval.setOnClickListener(this);
        sales.setOnClickListener(this);
        stock.setOnClickListener(this);
        report.setOnClickListener(this);
        customer_visit.setOnClickListener(this);
        session = new SessionManager(getApplicationContext());
        db = new SqliteHandler(getApplicationContext());
        HashMap<String,String> user=db.getUserDetails();
        position_status=user.get("position_status");
//        Log.e("cekdata", position_status);
//        Toast.makeText(MainActivity.this, position_status, Toast.LENGTH_SHORT).show();
        if (!session.isLoggedIn()) {
            logoutUser();
        }
        add=(ImageButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUser();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == approval) {
//            Bundle
            Intent i = new Intent(getApplicationContext(), Approval.class);
            Bundle bundle = new Bundle();
            bundle.putString("id_sub_menu", "1");
            i.putExtras(bundle);
            startActivity(i);
        }
        if (v == sales) {
            Intent i = new Intent(getApplicationContext(), Sales.class);
            Bundle bundle = new Bundle();
            bundle.putString("id_sub_menu", "2");
            i.putExtras(bundle);
            startActivity(i);
        }
        if (v == stock) {
            Intent i = new Intent(getApplicationContext(), Approval.class);
            Bundle bundle = new Bundle();
            bundle.putString("id_sub_menu", "3");
            i.putExtras(bundle);
            startActivity(i);
        }
        if (v == report) {
            Intent i = new Intent(getApplicationContext(), ActivityReport.class);
            startActivity(i);
        }
        if (v == customer_visit) {
            if(position_status.equals("1")){
                Intent i = new Intent(getApplicationContext(), CustomerVisit.class);
                startActivity(i);
            }else if(position_status.equals("2")){
                Intent i = new Intent(getApplicationContext(), VisitVanSeles.class);
                startActivity(i);
            }

        }
    }
}
