package smartsales.rizaldi.com.smartsales.CustomerVisit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

/**
 * Created by Toshiba-PC on 4/19/2016.
 */
public class RVAvanSales extends RecyclerView.Adapter<RVAvanSales.ViewHolder> {
    List<ListVanSales> list;
    public Context context;
    SqliteHandler db;
    String salesId = "";

    public RVAvanSales(List<ListVanSales> list, Context context) {
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
        holder.customer_id.setText(list.get(position).getId());
        holder.customer_name.setText(list.get(position).getNama());
        holder.visiting.setText(list.get(position).getVisiting());
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
        TextView customer_id, customer_name, visiting;
        ImageView cIn, cOut;
        RelativeLayout bglist;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_schedule, parent, false));
            customer_id = (TextView) itemView.findViewById(R.id.customer_id);
            customer_name = (TextView) itemView.findViewById(R.id.customer_name);
            visiting = (TextView) itemView.findViewById(R.id.visiting);
            cIn = (ImageView) itemView.findViewById(R.id.cIn);
            cOut = (ImageView) itemView.findViewById(R.id.cOut);
            cv = (CardView) itemView.findViewById(R.id.cv);
            bglist = (RelativeLayout) itemView.findViewById(R.id.bglist);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(getPosition()).getVisiting().equals("0") && !Ischekin.checkinvansales) {
                        Bundle b = new Bundle();
                        b.putString("salesId", salesId);
                        b.putString("customerId", customer_id.getText().toString());
                        b.putString("asal", "in");
                        Intent i = new Intent(context, CheckInOut.class);
                        i.putExtras(b);
                        context.startActivity(i);
                        ((Activity) context).finish();

                    } else if (list.get(getPosition()).getVisiting().equals("1")) {
                        Snackbar.make(v, "check out terlebih dahulu", Snackbar.LENGTH_LONG).show();
                    } else if (list.get(getPosition()).getVisiting().equals("0") && Ischekin.checkinvansales) {
                        Snackbar.make(v, "Check out terlebih dahulu", Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(v, "Customer sudah checkin dan check out", Snackbar.LENGTH_LONG).show();
                    }

                }
            });

        }
    }
}
