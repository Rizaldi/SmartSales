package smartsales.rizaldi.com.smartsales.Report.transaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAdetailtransaction extends RecyclerView.Adapter<RVAdetailtransaction.ViewHolder> {
    List<ModelDetail> list_product;
    public Context context;

    public RVAdetailtransaction(List<ModelDetail> list_product, Context context) {
        this.list_product = list_product;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.id.setText(list_product.get(position).getId());
        holder.productname.setText(list_product.get(position).getProduct());
        holder.orderqty.setText(list_product.get(position).getQty());
        holder.uom.setText(list_product.get(position).getUom());
        holder.unitprice.setText(list_product.get(position).getHarga());
        holder.taxcode.setText(list_product.get(position).getTax_code());
        holder.netamount.setText(list_product.get(position).getNiteamount());
        holder.dnp.setText(list_product.get(position).getDnp());
        holder.tax.setText(list_product.get(position).getTax_val());
        holder.total.setText(list_product.get(position).getTotal_amount());

    }

    @Override
    public int getItemCount() {
        return list_product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView id, productname, orderqty, uom, unitprice, taxcode, netamount, dnp, tax, total;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listtransactiondetail, parent, false));
            cv = (CardView) itemView.findViewById(R.id.cv);
            id = (TextView) itemView.findViewById(R.id.id);
            productname = (TextView) itemView.findViewById(R.id.productname);
            orderqty = (TextView) itemView.findViewById(R.id.orderqty);
            uom = (TextView) itemView.findViewById(R.id.uom);
            total = (TextView) itemView.findViewById(R.id.total);
            unitprice = (TextView) itemView.findViewById(R.id.unitprice);
            taxcode = (TextView) itemView.findViewById(R.id.taxcode);
            netamount = (TextView) itemView.findViewById(R.id.netamount);
            dnp = (TextView) itemView.findViewById(R.id.dnp);
            tax = (TextView) itemView.findViewById(R.id.tax);

        }

    }
}
