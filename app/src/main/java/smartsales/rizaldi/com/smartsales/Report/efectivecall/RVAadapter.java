package smartsales.rizaldi.com.smartsales.Report.efectivecall;

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
import smartsales.rizaldi.com.smartsales.Report.customeraging.DetailCustomeraging;

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
        holder.salesman.setText(list.get(position).getSalesman());
        holder.targetvisit.setText(list.get(position).getTargetVisit());
        holder.totalvisit.setText(list.get(position).getTotalVisit());
        holder.effectivecall.setText(list.get(position).getEffectiveCall());
        holder.totaltransaction.setText(list.get(position).getTotalTransaction());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView salesman,targetvisit,totalvisit,effectivecall,totaltransaction;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listefectivecall, parent, false));
            cv = (CardView) itemView.findViewById(R.id.cv);
            salesman = (TextView) itemView.findViewById(R.id.salesman);
            targetvisit = (TextView) itemView.findViewById(R.id.targetvisit);
            totalvisit = (TextView) itemView.findViewById(R.id.totalvisit);
            effectivecall = (TextView) itemView.findViewById(R.id.effectivecall);
            totaltransaction = (TextView) itemView.findViewById(R.id.totaltransaction);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, EfectiveDetail.class);
                    Bundle b = new Bundle();
                    b.putString("id", list.get(getPosition()).getSalesrepId());
                    b.putString("start", EffectiveCalls.start);
                    b.putString("end", EffectiveCalls.end);
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });
        }
    }
}
