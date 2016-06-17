package smartsales.rizaldi.com.smartsales.Report.customeraging;

import android.content.Context;
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
    List<ModelListDetail> list;
    public Context context;

    public RVAadapterDetail(List<ModelListDetail> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.outstanding.setText(list.get(position).getOutstanding());
        holder.thismonth.setText(list.get(position).getCurentmonth());
        holder.lastmonth.setText(list.get(position).getLastmonth());
        holder.last2month.setText(list.get(position).getLast2month());
        holder.last3month.setText(list.get(position).getLast3month());
        holder.last4month.setText(list.get(position).getLast4month());
        holder.docno.setText(list.get(position).getDocument_number());
        holder.date.setText(list.get(position).getDatetransaction());
        holder.amount.setText(list.get(position).getTotalamount());
        holder.terms.setText(list.get(position).getTerms());
        holder.duedate.setText(list.get(position).getDuedate());
        holder.pdreceipt.setText(list.get(position).getPdreceipt());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView docno, date, amount, terms, duedate, pdreceipt, outstanding, thismonth, lastmonth, last2month, last3month, last4month;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listcustomeragingdetail, parent, false));

            outstanding = (TextView) itemView.findViewById(R.id.outstanding);
            docno = (TextView) itemView.findViewById(R.id.docno);
            date = (TextView) itemView.findViewById(R.id.date);
            amount = (TextView) itemView.findViewById(R.id.amount);
            terms = (TextView) itemView.findViewById(R.id.terms);
            duedate = (TextView) itemView.findViewById(R.id.duedate);
            pdreceipt = (TextView) itemView.findViewById(R.id.pdreceipt);
            thismonth = (TextView) itemView.findViewById(R.id.thismonth);
            lastmonth = (TextView) itemView.findViewById(R.id.lastmonth);
            last2month = (TextView) itemView.findViewById(R.id.last2month);
            last3month = (TextView) itemView.findViewById(R.id.last3month);
            last4month = (TextView) itemView.findViewById(R.id.last4month);
        }
    }
}
