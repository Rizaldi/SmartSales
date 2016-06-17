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
import smartsales.rizaldi.com.smartsales.Sales.salestarget.ModelSalestarget;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAadapter extends RecyclerView.Adapter<RVAadapter.ViewHolder> {
    List<ModelProposeList> list;
    public Context context;

    public RVAadapter(List<ModelProposeList> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.customer.setText(list.get(position).getCustomername());
        holder.proposedso.setText(list.get(position).getProposedso());
        holder.salesrep.setText(list.get(position).getSales());
        holder.total.setText(list.get(position).getTotal());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView customer, salesrep,proposedso,total;
        CardView cv;
        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listproposereport, parent, false));
            customer = (TextView) itemView.findViewById(R.id.customer);
            proposedso = (TextView) itemView.findViewById(R.id.proposedso);
            salesrep = (TextView) itemView.findViewById(R.id.salesrep);
            total = (TextView) itemView.findViewById(R.id.total);
            cv = (CardView) itemView.findViewById(R.id.cv);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b=new Bundle();
                    b.putString("id",list.get(getPosition()).getId());
                    Intent i=new Intent(context,DetailPropose.class);
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });
        }
    }
}
