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
import smartsales.rizaldi.com.smartsales.Report.goodreturn.Modelreturnhistory;
import smartsales.rizaldi.com.smartsales.Report.goodreturn.Productlist;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAadapter extends RecyclerView.Adapter<RVAadapter.ViewHolder> {
    List<ModelRefillvan> list_product;
    public Context context;

    public RVAadapter(List<ModelRefillvan> list_product, Context context) {
        this.list_product = list_product;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.refillno.setText(list_product.get(position).getDocumentname());
        holder.reqdate.setText(list_product.get(position).getRequeteddate());
        holder.item.setText(list_product.get(position).getItem());
        holder.status.setText(list_product.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return list_product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView refillno,item,reqdate,status;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listrefilvan, parent, false));
            cv = (CardView) itemView.findViewById(R.id.cv);
            refillno = (TextView) itemView.findViewById(R.id.refillno);
            reqdate = (TextView) itemView.findViewById(R.id.reqdate);
            item = (TextView) itemView.findViewById(R.id.item);
            status = (TextView) itemView.findViewById(R.id.status);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, Detailrefill.class);
                    Bundle b = new Bundle();
                    b.putString("id", list_product.get(getPosition()).getId());
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });
        }
    }
}
