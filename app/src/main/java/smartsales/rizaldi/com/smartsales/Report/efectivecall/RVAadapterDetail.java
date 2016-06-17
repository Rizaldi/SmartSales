package smartsales.rizaldi.com.smartsales.Report.efectivecall;

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
public class RVAadapterDetail extends RecyclerView.Adapter<RVAadapterDetail.ViewHolder> {
    List<ModelDetail> list;
    public Context context;

    public RVAadapterDetail(List<ModelDetail> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.customer.setText(list.get(position).getCustoomer_name());
        holder.address.setText(list.get(position).getAddress());
        holder.status.setText(list.get(position).getStatus());
        holder.date.setText(list.get(position).getDate());
        holder.checkin.setText(list.get(position).getCheckin());
        holder.checkout.setText(list.get(position).getCheckout());
        holder.transaction.setText(list.get(position).getTransaction());
        holder.total.setText(list.get(position).getTotal());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView customer,address,status,date,checkin,checkout,transaction,total;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listefectivedetail, parent, false));
            customer = (TextView) itemView.findViewById(R.id.customer);
            address = (TextView) itemView.findViewById(R.id.address);
            status = (TextView) itemView.findViewById(R.id.status);
            date = (TextView) itemView.findViewById(R.id.date);
            checkin = (TextView) itemView.findViewById(R.id.checkin);
            checkout = (TextView) itemView.findViewById(R.id.checkout);
            transaction = (TextView) itemView.findViewById(R.id.transaction);
            total = (TextView) itemView.findViewById(R.id.total);
        }
    }
}
