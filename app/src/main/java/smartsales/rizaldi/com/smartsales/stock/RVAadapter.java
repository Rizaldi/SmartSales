package smartsales.rizaldi.com.smartsales.stock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Sales.competitor.ModelCompetitor;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAadapter extends RecyclerView.Adapter<RVAadapter.ViewHolder> {
    List<ModelStock> list;
    public Context context;

    public RVAadapter(List<ModelStock> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productname.setText(list.get(position).getProduct());
        holder.category.setText(list.get(position).getCategory());
        holder.qty.setText(list.get(position).getQty());
        holder.uom.setText(list.get(position).getUom());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productname, category,qty,uom;
        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.liststockbalance, parent, false));
            productname = (TextView) itemView.findViewById(R.id.productname);
            category = (TextView) itemView.findViewById(R.id.category);
            qty = (TextView) itemView.findViewById(R.id.qty);
            uom = (TextView) itemView.findViewById(R.id.uom);
        }
    }
}
