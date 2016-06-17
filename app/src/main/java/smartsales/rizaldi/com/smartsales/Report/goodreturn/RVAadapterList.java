package smartsales.rizaldi.com.smartsales.Report.goodreturn;

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
import smartsales.rizaldi.com.smartsales.Report.transaction.DetailTransaction;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAadapterList extends RecyclerView.Adapter<RVAadapterList.ViewHolder> {
    List<ModelProductlist> list_product;
    public Context context;

    public RVAadapterList(List<ModelProductlist> list_product, Context context) {
        this.list_product = list_product;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.product.setText(list_product.get(position).getProduct());
        holder.qty.setText(list_product.get(position).getQuanity());
        holder.uom.setText(list_product.get(position).getUom());
        holder.condition.setText(list_product.get(position).getCondition());
        holder.expired.setText(list_product.get(position).getExpired());
    }

    @Override
    public int getItemCount() {
        return list_product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView product, qty, uom,condition,expired;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listgoodreturn, parent, false));
            product = (TextView) itemView.findViewById(R.id.product);
            qty = (TextView) itemView.findViewById(R.id.qty);
            uom = (TextView) itemView.findViewById(R.id.uom);
            condition = (TextView) itemView.findViewById(R.id.condition);
            expired = (TextView) itemView.findViewById(R.id.expired);
        }
    }
}
