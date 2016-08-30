package smartsales.rizaldi.com.smartsales.CustomerVisit;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Sales.ListCustomer;

/**
 * Created by Toshiba-PC on 8/7/2016.
 */
public class AdapterAddItinerary extends RecyclerView.Adapter<AdapterAddItinerary.ViewHolder>  {
    List<ListCustomer> customerList;
    Context context;

    public AdapterAddItinerary(List<ListCustomer> customerList, Context context) {
        this.customerList = customerList;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.id.setText(customerList.get(position).getId());
        holder.name.setText(customerList.get(position).getName());
        holder.address.setText(customerList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cv;
        TextView id,name,address;
        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_addjadwal,parent,false));
            id=(TextView) itemView.findViewById(R.id.customer_list_id);
            name=(TextView) itemView.findViewById(R.id.customer_list_name);
            address=(TextView)itemView.findViewById(R.id.customer_list_address);
            cv=(CardView) itemView.findViewById(R.id.cv);

        }
    }

}
