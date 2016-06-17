package smartsales.rizaldi.com.smartsales.Approval;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;

import smartsales.rizaldi.com.smartsales.R;

/**
 * Created by spasi on 13/03/2016.
 */
public class IteneraryInput extends AppCompatActivity{
    CalendarView calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_itenerary_input);


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
        getSupportActionBar().setTitle(" Select date..");




        initializeCalendar();
//        calendar_itenerary.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
//
//            }
//        });
    }

    public void initializeCalendar() {
        calendar = (CalendarView) findViewById(R.id.calendarView1);

//        calendar.setShowWeekNumber(false);
//        calendar.setFirstDayOfWeek(2);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                Intent o = new Intent(getApplicationContext(),ItineraryAdd.class);
                startActivity(o);
//                Toast.makeText(getBaseContext(), "Selected Date is\n\n"
//                                + dayOfMonth + " : " + month + " : " + year,
//                        Toast.LENGTH_LONG).show();
            }
        });
    }
}