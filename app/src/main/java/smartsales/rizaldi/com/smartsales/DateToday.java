package smartsales.rizaldi.com.smartsales;

import android.text.format.Time;

import java.util.Calendar;

/**
 * Created by Toshiba-PC on 4/19/2016.
 */
public class DateToday {
    public static String dateNow(){
        String date="";
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        int year_x, month_x, day_x;
        Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        String mbulan, mtanggal;
        if (month_x < 10) {
            mbulan = "0" + (month_x+1);
        } else {
            mbulan = String.valueOf(month_x+1);
        }
        if (day_x < 10) {
            mtanggal = "0" + day_x;
        } else {
            mtanggal = String.valueOf(day_x);
        }
        date=today.year+"-"+mbulan+"-"+mtanggal;
        return date;
    }
    public static String dateNowSlash(){
        String date="";
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        int year_x, month_x, day_x;
        Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        String mbulan, mtanggal;
        if (month_x < 10) {
            mbulan = "0" + (month_x+1);
        } else {
            mbulan = String.valueOf(month_x+1);
        }
        if (day_x < 10) {
            mtanggal = "0" + day_x;
        } else {
            mtanggal = String.valueOf(day_x);
        }
        date=mtanggal+"/"+mbulan+"/"+today.year;
        return date;
    }

}
