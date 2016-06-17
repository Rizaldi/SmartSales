package smartsales.rizaldi.com.smartsales.Approval.approvalso;

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
public class RVAApproval extends RecyclerView.Adapter<RVAApproval.ViewHolder> {
    List<ModelApprovalSo> list_product;
    public Context context;

    public RVAApproval(List<ModelApprovalSo> list_product, Context context) {
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
        holder.docnumber.setText(list_product.get(position).getDocnumber());
        holder.customer.setText(list_product.get(position).getCustname());
        holder.total.setText(list_product.get(position).getTotal());
        holder.date.setText(list_product.get(position).getDate());
        holder.time.setText(list_product.get(position).getTime());
        holder.status.setText(list_product.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return list_product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView id, docnumber, customer, total, date, time, status;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listapprovalso, parent, false));
            cv = (CardView) itemView.findViewById(R.id.cv);
            id = (TextView) itemView.findViewById(R.id.id);
            docnumber = (TextView) itemView.findViewById(R.id.docnumber);
            customer = (TextView) itemView.findViewById(R.id.customer);
            total = (TextView) itemView.findViewById(R.id.total);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            status = (TextView) itemView.findViewById(R.id.status);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, ListApprovalSales.class);
                    Bundle b = new Bundle();
                    b.putString("idSO",id.getText().toString());
                    i.putExtras(b);
                    context.startActivity(i);
                    ((Activity) context).finish();
                }
            });
        }
    }
}
