package smartsales.rizaldi.com.smartsales.Sales.salestarget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import smartsales.rizaldi.com.smartsales.R;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAadapter extends RecyclerView.Adapter<RVAadapter.ViewHolder> {
    List<ModelSalestarget> list;
    public Context context;

    public RVAadapter(List<ModelSalestarget> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.brand.setText(list.get(position).getBrand());
        holder.productname.setText(list.get(position).getProductname());
        holder.salesrep.setText(list.get(position).getSalesrep());
        holder.target.setText(list.get(position).getTarget());
        holder.achieved.setText(list.get(position).getAchieved());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView brand, productname,salesrep,target,achieved;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listsalestarget, parent, false));
            brand = (TextView) itemView.findViewById(R.id.brand);
            productname = (TextView) itemView.findViewById(R.id.productname);
            salesrep = (TextView) itemView.findViewById(R.id.salesrep);
            target = (TextView) itemView.findViewById(R.id.target);
            achieved = (TextView) itemView.findViewById(R.id.achieved);
        }
    }
}
