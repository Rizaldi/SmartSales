package smartsales.rizaldi.com.smartsales.Report.transaction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import smartsales.rizaldi.com.smartsales.R;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAadapter extends RecyclerView.Adapter<RVAadapter.ViewHolder> {
    List<ModelHistory> list_product;
    public Context context;

    public RVAadapter(List<ModelHistory> list_product, Context context) {
        this.list_product = list_product;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.id.setText(list_product.get(position).getId());
        holder.docnumber.setText(list_product.get(position).getDocument_number());
        holder.customer_name.setText(list_product.get(position).getCustomer_name());
        holder.customer_address.setText(list_product.get(position).getCustomer_address());
        holder.total.setText(list_product.get(position).getTotal());
        holder.date.setText(list_product.get(position).getDate_transaction());
        holder.time.setText(list_product.get(position).getTime_transaction());
        holder.status.setText(list_product.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return list_product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView id, docnumber, customer_name,customer_address,total,date,time,status;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listtransactionreport, parent, false));
            cv = (CardView) itemView.findViewById(R.id.cv);
            id = (TextView) itemView.findViewById(R.id.id);
            docnumber = (TextView) itemView.findViewById(R.id.docnumber);
            customer_name = (TextView) itemView.findViewById(R.id.customer_name);
            total = (TextView) itemView.findViewById(R.id.total);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            status = (TextView) itemView.findViewById(R.id.status);
            customer_address = (TextView) itemView.findViewById(R.id.customer_address);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailTransaction.class);
                    Bundle b = new Bundle();
                    b.putString("id",id.getText().toString());
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });
        }
    }
}
