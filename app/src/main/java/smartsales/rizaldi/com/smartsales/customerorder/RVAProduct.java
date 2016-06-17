package smartsales.rizaldi.com.smartsales.customerorder;

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

import java.util.HashMap;
import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAProduct extends RecyclerView.Adapter<RVAProduct.ViewHolder> {
    List<ModelListProduct> list_product;
    public Context context;

    public RVAProduct(List<ModelListProduct> list_product, Context context) {
        this.list_product = list_product;
        this.context = context;
    }

    @Override
    public RVAProduct.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RVAProduct.ViewHolder holder, int position) {
        holder.id.setText(list_product.get(position).getId());
        holder.productname.setText(list_product.get(position).getNama());
        holder.price.setText(list_product.get(position).getPrice());
        holder.stock.setText(list_product.get(position).getStock());
    }

    @Override
    public int getItemCount() {
        return list_product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView id, productname, price, stock;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product, parent, false));
            cv = (CardView) itemView.findViewById(R.id.cv);
            id = (TextView) itemView.findViewById(R.id.id);
            productname = (TextView) itemView.findViewById(R.id.tvproduct);
            price = (TextView) itemView.findViewById(R.id.tvprice);
            stock = (TextView) itemView.findViewById(R.id.tvstock);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, InputQuantity.class);
                    Bundle b=new Bundle();
                    b.putString("id",id.getText().toString());
                    b.putString("product",productname.getText().toString());
                    b.putString("price",price.getText().toString());
                    i.putExtras(b);
                    context.startActivity(i);
                    ((Activity)context).finish();
                }
            });
        }
    }
}
