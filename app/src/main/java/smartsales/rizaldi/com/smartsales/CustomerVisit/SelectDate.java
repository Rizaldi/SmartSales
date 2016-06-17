package smartsales.rizaldi.com.smartsales.CustomerVisit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

import smartsales.rizaldi.com.smartsales.R;

/**
 * Created by Toshiba-PC on 4/15/2016.
 */
public class SelectDate extends AppCompatActivity {
    CalendarView calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_customer_visit);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        initializeCalendar();
    }
    public void initializeCalendar() {
        calendar = (CalendarView) findViewById(R.id.calendarViewCustomerVisit);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Intent o = new Intent(getApplicationContext(), CustomerVisit.class);
                Bundle bundle = new Bundle();
                bundle.putString("tanggal", String.valueOf(dayOfMonth));
                bundle.putString("bulan",String.valueOf(month));
                bundle.putString("tahun", String.valueOf(year));
//                bundle.putString("date", year + "-" + month + "-" + dayOfMonth);
                o.putExtras(bundle);
                startActivity(o);
                finish();
            }
        });
    }


}
