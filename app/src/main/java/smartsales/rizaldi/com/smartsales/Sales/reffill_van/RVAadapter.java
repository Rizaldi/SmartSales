package smartsales.rizaldi.com.smartsales.Sales.reffill_van;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Sales.good_return.ModelList;
import smartsales.rizaldi.com.smartsales.Varglobal;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAadapter extends RecyclerView.Adapter<RVAadapter.ViewHolder> {
    List<ModelDataRefill> list;
    public Context context;

    public RVAadapter(List<ModelDataRefill> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.productname.setText(list.get(position).getProductname());
        holder.qty.setText(list.get(position).getQty());
        holder.uom.setText(list.get(position).getUomname());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productname, qty,uom;
        ImageView delete;
        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_refillvan, parent, false));
            productname = (TextView) itemView.findViewById(R.id.productname);
            qty = (TextView) itemView.findViewById(R.id.qty);
            uom = (TextView) itemView.findViewById(R.id.uom);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAt(getPosition());
                }
            });
        }

        public void removeAt(int position) {
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
            Varglobal.arrRefillVan="";
            for (int i = 0; i < list.size(); i++) {

                if (i == 0) {
                    Varglobal.arrRefillVan = list.get(i).getRefilvan() + "|";
                } else {
                    Varglobal.arrRefillVan = Varglobal.arrRefillVan + list.get(i).getRefilvan() + "|";
                }
            }
            Log.e("dataarrayafterremove",Varglobal.arrRefillVan);
        }

    }
}
