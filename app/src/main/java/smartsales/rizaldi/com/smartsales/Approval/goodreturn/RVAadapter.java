package smartsales.rizaldi.com.smartsales.Approval.goodreturn;

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

import smartsales.rizaldi.com.smartsales.Approval.refillvan.ApproveDetailrefill;
import smartsales.rizaldi.com.smartsales.Approval.refillvan.ModelRefill;
import smartsales.rizaldi.com.smartsales.R;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAadapter extends RecyclerView.Adapter<RVAadapter.ViewHolder> {
    List<Modelgood> list_product;
    public Context context;

    public RVAadapter(List<Modelgood> list_product, Context context) {
        this.list_product = list_product;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.docnumber.setText(list_product.get(position).getDoc_no());
        holder.customer.setText(list_product.get(position).getCustomer_name());
        holder.date.setText(list_product.get(position).getCreated_on());
        holder.status.setText(list_product.get(position).getApproval_status());
        holder.total.setText(list_product.get(position).getTotal_return());
    }

    @Override
    public int getItemCount() {
        return list_product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView docnumber, customer,  date, status,total;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listaprovegood, parent, false));
            cv = (CardView) itemView.findViewById(R.id.cv);
            docnumber = (TextView) itemView.findViewById(R.id.docnumber);
            customer = (TextView) itemView.findViewById(R.id.customer);
            total = (TextView) itemView.findViewById(R.id.total);
            date = (TextView) itemView.findViewById(R.id.date);
//            time = (TextView) itemView.findViewById(R.id.time);
            status = (TextView) itemView.findViewById(R.id.status);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, GoodreturnDetail.class);
                    Bundle b = new Bundle();
                    b.putString("id",list_product.get(getPosition()).getId());
                    i.putExtras(b);
                    context.startActivity(i);
                    ((Activity) context).finish();
                }
            });
        }
    }
}
