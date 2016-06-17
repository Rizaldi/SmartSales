package smartsales.rizaldi.com.smartsales.Report.proposeso;

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
        holder.productname.setText(list.get(position).getProductname());
        holder.qtyproposed.setText(list.get(position).getQtyproposed());
        holder.qtyordered.setText(list.get(position).getQtyordered());
        holder.totalordered.setText(list.get(position).getTotalordered());
        holder.totalproposed.setText(list.get(position).getTotalproposed());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productname, qtyproposed, totalproposed, qtyordered, totalordered;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listdetailpropose, parent, false));
            productname = (TextView) itemView.findViewById(R.id.productname);
            qtyordered = (TextView) itemView.findViewById(R.id.qtyordered);
            totalordered = (TextView) itemView.findViewById(R.id.totalordered);
            qtyproposed = (TextView) itemView.findViewById(R.id.qtyproposed);
            totalproposed = (TextView) itemView.findViewById(R.id.totalproposed);
        }
    }
}
