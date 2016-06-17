package smartsales.rizaldi.com.smartsales.Report.refilvan;

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
    List<ModelDetail> list_product;
    public Context context;

    public RVAadapterDetail(List<ModelDetail> list_product, Context context) {
        this.list_product = list_product;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(list_product.get(position).getProduct());
        holder.qty.setText(list_product.get(position).getQuantity());
        holder.uom.setText(list_product.get(position).getUom());
    }

    @Override
    public int getItemCount() {
        return list_product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,qty,uom;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.reportlistdetailrefill, parent, false));
            name = (TextView) itemView.findViewById(R.id.name);
            qty = (TextView) itemView.findViewById(R.id.qty);
            uom = (TextView) itemView.findViewById(R.id.uom);
        }
    }
}
