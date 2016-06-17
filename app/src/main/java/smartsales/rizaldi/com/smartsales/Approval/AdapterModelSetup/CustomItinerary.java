package smartsales.rizaldi.com.smartsales.Approval.AdapterModelSetup;

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
public class CustomItinerary extends RecyclerView.Adapter<CustomItinerary.ViewHolder> {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelItinerary> modelItineraries;
    Context context;

    public CustomItinerary(Context context, List<ModelItinerary> modelItineraries) {
        super();
        this.context = context;
        this.modelItineraries = modelItineraries;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sales_name, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelItinerary modelItinerary = modelItineraries.get(position);

        holder.name_sales.setText(modelItinerary.getName());
        holder.id_sales.setText(modelItinerary.getId_name());
    }

    @Override
    public int getItemCount() {
        return modelItineraries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name_sales, id_sales;
        public CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            name_sales = (TextView) itemView.findViewById(R.id.name_sales);
            id_sales = (TextView) itemView.findViewById(R.id.id_sales);
            card = (CardView) itemView.findViewById(R.id.listDataSales);
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