package smartsales.rizaldi.com.smartsales.Approval.Itenerary;

import android.app.Activity;
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

import smartsales.rizaldi.com.smartsales.Approval.IteneraryInput;
import smartsales.rizaldi.com.smartsales.R;

/**
 * Created by spasi on 07/03/2016.
 */
public class IteneraryAddCustom extends RecyclerView.Adapter<IteneraryAddCustom.ViewHolder> {
    private Activity activity;
    private LayoutInflater inflater;
    private List<IteneraryModel> iteneraryModels;
    Context context;

    public IteneraryAddCustom(Context context, List<IteneraryModel> iteneraryModels) {
        super();
        this.context = context;
        this.iteneraryModels = iteneraryModels;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_customer, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IteneraryModel iteneraryModel = iteneraryModels.get(position);

        holder.name_sales.setText(iteneraryModel.getName());
        holder.id_sales.setText(iteneraryModel.getId_name());
    }

    @Override
    public int getItemCount() {
        return iteneraryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name_sales, id_sales;
        public CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            name_sales = (TextView) itemView.findViewById(R.id.title_customer_list);
            id_sales = (TextView) itemView.findViewById(R.id.content_customer_list);
            card = (CardView) itemView.findViewById(R.id.listDataCustomer);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            String no = no_telp.getText().toString();
            String id = id_sales.getText().toString();
//            if (v == hpcontact) {
            Intent i = new Intent(context, IteneraryInput.class);
            Bundle bundle = new Bundle();
            bundle.putString("buat_id", id);
            i.putExtras(bundle);
            context.startActivity(i);
//            }
        }
    }
}