package smartsales.rizaldi.com.smartsales.Report.customeraging;

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
    List<ModelList> list;
    public Context context;

    public RVAadapter(List<ModelList> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.outstanding.setText(list.get(position).getOutstanding());
        holder.thismonth.setText(list.get(position).getCurrentMonth());
        holder.lastmonth.setText(list.get(position).getLastMonth());
        holder.last2month.setText(list.get(position).getLast2Month());
        holder.last3month.setText(list.get(position).getLast3Month());
        holder.last4month.setText(list.get(position).getLast4Month());
        holder.customer.setText(list.get(position).getCustomer_name());
        holder.salesrep.setText(list.get(position).getSalesman());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView customer,salesrep,thismonth,lastmonth,last2month,last3month,last4month;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listcustomeraging, parent, false));
            cv = (CardView) itemView.findViewById(R.id.cv);
//            outstanding = (TextView) itemView.findViewById(R.id.outstanding);
            thismonth = (TextView) itemView.findViewById(R.id.thismonth);
            customer = (TextView) itemView.findViewById(R.id.customer);
            lastmonth = (TextView) itemView.findViewById(R.id.lastmonth);
            last2month = (TextView) itemView.findViewById(R.id.last2month);
            last3month = (TextView) itemView.findViewById(R.id.last3month);
            last4month = (TextView) itemView.findViewById(R.id.last4month);
            salesrep = (TextView) itemView.findViewById(R.id.salesrep);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailCustomeraging.class);
                    Bundle b = new Bundle();
                    b.putString("id", list.get(getPosition()).getCustomer_id());
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });
        }
    }
}
