package smartsales.rizaldi.com.smartsales.Sales.invoicepayment;

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
    List<Modelcustomerinvoice> list;
    public Context context;

    public RVAadapter(List<Modelcustomerinvoice> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.customer.setText(list.get(position).getCustomer());
        holder.invoice.setText(list.get(position).getTotal());
        holder.payment.setText(list.get(position).getRemaining());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView customer, invoice, payment;
        CardView cv;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_customerinvoice, parent, false));
            customer = (TextView) itemView.findViewById(R.id.customer);
            invoice = (TextView) itemView.findViewById(R.id.invoice);
            payment = (TextView) itemView.findViewById(R.id.payment);
            cv = (CardView) itemView.findViewById(R.id.cv);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putString("id", list.get(getPosition()).getId());
                    b.putString("customer", customer.getText().toString());
                    Intent i = new Intent(context, DetailInvoice.class);
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });
        }
    }
}
