package smartsales.rizaldi.com.smartsales.Report.goodreturn;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Report.transaction.DetailTransaction;
import smartsales.rizaldi.com.smartsales.Report.transaction.ModelHistory;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAadapter extends RecyclerView.Adapter<RVAadapter.ViewHolder> {
    List<Modelreturnhistory> list_product;
    public Context context;

    public RVAadapter(List<Modelreturnhistory> list_product, Context context) {
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
        holder.docnumber.setText(list_product.get(position).getDoc_no());
        holder.customer.setText(list_product.get(position).getCustomer());
        holder.salesrep.setText(list_product.get(position).getSalesman());
        holder.date.setText(list_product.get(position).getCreated_on());
        holder.item.setText(list_product.get(position).getItem());
        holder.status.setText(list_product.get(position).getStatus());

    }

    @Override
    public int getItemCount() {
        return list_product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView id, docnumber, customer, salesrep, item, date, status;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listhistorygoodreturn, parent, false));
            cv = (CardView) itemView.findViewById(R.id.cv);
            id = (TextView) itemView.findViewById(R.id.id);
            docnumber = (TextView) itemView.findViewById(R.id.docnumber);
            customer = (TextView) itemView.findViewById(R.id.customer);
            item = (TextView) itemView.findViewById(R.id.item);
            date = (TextView) itemView.findViewById(R.id.date);
            status = (TextView) itemView.findViewById(R.id.status);
            salesrep = (TextView) itemView.findViewById(R.id.salesrep);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, Productlist.class);
                    Bundle b = new Bundle();
                    b.putString("id", id.getText().toString());
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });
        }
    }
}
