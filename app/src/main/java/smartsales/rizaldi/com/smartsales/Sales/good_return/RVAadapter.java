package smartsales.rizaldi.com.smartsales.Sales.good_return;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Sales.competitor.ModelCompetitor;
import smartsales.rizaldi.com.smartsales.Varglobal;

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
        holder.productname.setText(list.get(position).getProductname());
        holder.qty.setText(list.get(position).getQty());
        holder.uom.setText(list.get(position).getUomname());
        holder.condition.setText(list.get(position).getConditionname());
        holder.expired.setText(list.get(position).getExpireddate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productname, qty,uom,condition,expired;
        ImageView delete;
        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_goodreturn, parent, false));
            productname = (TextView) itemView.findViewById(R.id.productname);
            qty = (TextView) itemView.findViewById(R.id.qty);
            uom = (TextView) itemView.findViewById(R.id.uom);
            condition = (TextView) itemView.findViewById(R.id.condition);
            expired = (TextView) itemView.findViewById(R.id.expired);
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
            Varglobal.arrGoodreturn="";
            for (int i = 0; i < list.size(); i++) {

                if (i == 0) {
                    Varglobal.arrGoodreturn = list.get(i).getGoodreturn() + "|";
                } else {
                    Varglobal.arrGoodreturn = Varglobal.arrGoodreturn + list.get(i).getGoodreturn() + "|";
                }
            }
            Log.e("dataarrayafterremove",Varglobal.arrGoodreturn);
        }

    }
}
