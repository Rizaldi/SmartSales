package smartsales.rizaldi.com.smartsales.Sales.competitor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Report.goodreturn.Modelreturnhistory;
import smartsales.rizaldi.com.smartsales.Report.transaction.DetailTransaction;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAadapter extends RecyclerView.Adapter<RVAadapter.ViewHolder> {
    List<ModelCompetitor> list;
    public Context context;

    public RVAadapter(List<ModelCompetitor> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvdate.setText(list.get(position).getDate());
        holder.tvdescription.setText(list.get(position).getDescription());
        Picasso.with(context).load("http://"+list.get(position).getImage()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvdescription, tvdate;
        ImageView img;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listcompetitor, parent, false));
            tvdescription = (TextView) itemView.findViewById(R.id.tvdescription);
            tvdate = (TextView) itemView.findViewById(R.id.tvdate);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
