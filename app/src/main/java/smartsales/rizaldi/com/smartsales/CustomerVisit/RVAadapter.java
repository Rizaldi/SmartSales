package smartsales.rizaldi.com.smartsales.CustomerVisit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import smartsales.rizaldi.com.smartsales.Approval.Itenerary.List_Schedule;
import smartsales.rizaldi.com.smartsales.DateToday;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

/**
 * Created by Toshiba-PC on 4/16/2016.
 */
public class RVAadapter extends RecyclerView.Adapter<RVAadapter.ViewHolder> {
    List<List_Schedule> list;
    public Context context;
    SqliteHandler db;
    String salesId = "";
    String tgl, date;

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getTgl() {
        return tgl;
    }

    public RVAadapter(List<List_Schedule> list, Context context) {
        this.list = list;
        this.context = context;
        db = new SqliteHandler(context);
        HashMap<String, String> user = db.getUserDetails();
        salesId = user.get("employee_id");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.customer_id.setText(list.get(position).getCustomer_id());
        holder.schedule_id.setText(list.get(position).getSchedule_id());
        holder.visiting.setText(list.get(position).getVisiting());
        holder.customer_name.setText(list.get(position).getCustomer());
        holder.customer_address.setText(list.get(position).getCustomer_address());
        if (list.get(position).getVisiting().equals("1")) {
            holder.cIn.setVisibility(View.VISIBLE);
            holder.cOut.setVisibility(View.GONE);
        }
        if (list.get(position).getVisiting().equals("2")) {
            holder.cIn.setVisibility(View.GONE);
            holder.cOut.setVisibility(View.VISIBLE);
            holder.bglist.setBackgroundColor(Color.parseColor("#efefef"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        TextView schedule_id, visiting, customer_id, customer_name, customer_address;
        ImageView cIn, cOut;
        RelativeLayout bglist;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_schedule, parent, false));
            schedule_id = (TextView) itemView.findViewById(R.id.schedule_id);
            visiting = (TextView) itemView.findViewById(R.id.visiting);
            customer_id = (TextView) itemView.findViewById(R.id.customer_id);
            customer_name = (TextView) itemView.findViewById(R.id.customer_name);
            customer_address = (TextView) itemView.findViewById(R.id.customer_address);
            cIn = (ImageView) itemView.findViewById(R.id.cIn);
            cOut = (ImageView) itemView.findViewById(R.id.cOut);
            cv = (CardView) itemView.findViewById(R.id.cv);
            bglist = (RelativeLayout) itemView.findViewById(R.id.bglist);
            date = DateToday.dateNow();

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getTgl().equals(date)) {
                        if (list.get(getPosition()).getVisiting().equals("0") && !Ischekin.chekin) {
                            Bundle b = new Bundle();
                            b.putString("salesId", salesId);
                            b.putString("scheduleId", schedule_id.getText().toString());
                            b.putString("customerId", customer_id.getText().toString());
                            b.putString("asal", "in");
                            Intent i = new Intent(context, CheckInOut.class);
                            i.putExtras(b);
                            context.startActivity(i);
                            ((Activity) context).finish();

                        } else if (list.get(getPosition()).getVisiting().equals("1")) {
                            Snackbar.make(v, "check out terlebih dahulu", Snackbar.LENGTH_LONG).show();
                        } else if (list.get(getPosition()).getVisiting().equals("0") && Ischekin.chekin) {
                            Snackbar.make(v, "Check out terlebih dahulu", Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(v, "Customer sudah checkin dan check out", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        Snackbar.make(v, "check tanggal visit atu masih dalam keadaan checkin", Snackbar.LENGTH_LONG).show();
                    }
                }
            });

        }
    }
}
