package smartsales.rizaldi.com.smartsales.Report.topcustomer;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Report.transaction.ModelDetail;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAlistProduct extends RecyclerView.Adapter<RVAlistProduct.ViewHolder> {
    List<ModelCustomerReport> list_product;
    public Context context;

    public RVAlistProduct(List<ModelCustomerReport> list_product, Context context) {
        this.list_product = list_product;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.customer_name.setText(list_product.get(position).getCustomer());
        holder.customer_address.setText(list_product.get(position).getAddress());
        holder.orderqty.setText(list_product.get(position).getTotal());
        holder.salesman.setText(list_product.get(position).getSales());
        holder.total.setText(list_product.get(position).getTotal_amount());

    }

    @Override
    public int getItemCount() {
        return list_product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView customer_name, customer_address, salesman, orderqty, total;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listcustomerproduct, parent, false));
            cv = (CardView) itemView.findViewById(R.id.cv);
            customer_name = (TextView) itemView.findViewById(R.id.customer_name);
            customer_address = (TextView) itemView.findViewById(R.id.customer_address);
            orderqty = (TextView) itemView.findViewById(R.id.orderqty);
            salesman = (TextView) itemView.findViewById(R.id.salesman);
            total = (TextView) itemView.findViewById(R.id.total);
        }

    }
}
