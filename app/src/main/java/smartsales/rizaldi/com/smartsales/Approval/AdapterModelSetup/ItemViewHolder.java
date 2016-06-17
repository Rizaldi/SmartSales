package smartsales.rizaldi.com.smartsales.Approval.AdapterModelSetup;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import smartsales.rizaldi.com.smartsales.R;

/**
 * Created by spasi on 07/03/2016.
 */
public class ItemViewHolder extends RecyclerView.ViewHolder {

    public TextView name_sales;
    public TextView id_sales;

    public ItemViewHolder(View itemView) {
        super(itemView);

        name_sales = (TextView) itemView.findViewById(R.id.name_sales);
        id_sales = (TextView) itemView.findViewById(R.id.id_sales);
    }

    public void bind(ModelItinerary modelItinerary) {
        name_sales.setText(modelItinerary.getName());
        id_sales.setText(modelItinerary.getId_name());
    }
}