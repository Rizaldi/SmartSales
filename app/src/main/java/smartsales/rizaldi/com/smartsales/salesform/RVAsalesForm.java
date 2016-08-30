package smartsales.rizaldi.com.smartsales.salesform;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.customerorder.OrderedProductList;
import smartsales.rizaldi.com.smartsales.customerorder.ParamInput;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

/**
 * Created by Toshiba-PC on 4/28/2016.
 */
public class RVAsalesForm extends RecyclerView.Adapter<RVAsalesForm.ViewHder> {
    List<ListSalesForm> list;
    Context context;
    public String idSO = "";
    public String idDelete="";
    SqliteHandler db;
    String organizationId = "";

    public interface BtnClickListener {
        public abstract void onBtnClick(int position);
    }
    private BtnClickListener mClickListener = null;
    public RVAsalesForm(List<ListSalesForm> list, Context context) {
        this.list = list;
        this.context = context;
        db = new SqliteHandler(context);
        HashMap<String, String> user = db.getUserDetails();
        organizationId = user.get("organization_id");

    }
    public RVAsalesForm(List<ListSalesForm> list, Context context,BtnClickListener listener) {
        this.list = list;
        this.context = context;
        db = new SqliteHandler(context);
        HashMap<String, String> user = db.getUserDetails();
        organizationId = user.get("organization_id");
        mClickListener = listener;
    }

    @Override

    public ViewHder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHder holder, int position) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        holder.id.setText(list.get(position).getId());
        holder.name.setText(list.get(position).getName());
        holder.quantyty.setText(list.get(position).getQuantyty());
        holder.uom.setText(list.get(position).getUomname());
        holder.harga.setText(list.get(position).getHarga());
        holder.taxcode.setText(list.get(position).getTax_code());
        holder.subtotal.setText(list.get(position).getSubtotal());
            holder.discount1.setText(list.get(position).getDiscount1());
            holder.discount2.setText(list.get(position).getDiscount2());
            holder.discount3.setText(list.get(position).getDiscount3());
        holder.name1.setText(list.get(position).getName1());
        holder.name2.setText(list.get(position).getName2());
        holder.name3.setText(list.get(position).getName3());
        holder.tax.setText(nf.format(Double.parseDouble(list.get(position).getTax())));
        holder.total.setText(list.get(position).getTotal_amount());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHder extends RecyclerView.ViewHolder {
        TextView id, name, quantyty, uom, harga, taxcode, subtotal, discount1, discount2, discount3, tax, total, name1, name2, name3;
        ImageView btnhps;

        public ViewHder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_salesform, parent, false));
            btnhps = (ImageView) itemView.findViewById(R.id.btnhps);
            id = (TextView) itemView.findViewById(R.id.id);
            name = (TextView) itemView.findViewById(R.id.name);
            quantyty = (TextView) itemView.findViewById(R.id.quantity);
            uom = (TextView) itemView.findViewById(R.id.uom);
            harga = (TextView) itemView.findViewById(R.id.harga);
            taxcode = (TextView) itemView.findViewById(R.id.tax_code);
            subtotal = (TextView) itemView.findViewById(R.id.subtotal);
            discount1 = (TextView) itemView.findViewById(R.id.discount1);
            discount2 = (TextView) itemView.findViewById(R.id.discount2);
            discount3 = (TextView) itemView.findViewById(R.id.discount3);
            name1 = (TextView) itemView.findViewById(R.id.name1);
            name2 = (TextView) itemView.findViewById(R.id.name2);
            name3 = (TextView) itemView.findViewById(R.id.name3);
            tax = (TextView) itemView.findViewById(R.id.tax);
            total = (TextView) itemView.findViewById(R.id.total_amount);
            btnhps.setTag(getPosition());
            btnhps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    DeleteList(id.getText().toString(), v, getPosition(),total);
                    idDelete=id.getText().toString();
                    if(mClickListener != null)
                        mClickListener.onBtnClick((Integer) v.getTag());
                }
            });
        }
    }

    private void DeleteList(final String idparam, final View v, final int position, final TextView tot) {
        final ProgressDialog loading = new ProgressDialog(context);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.POST, UrlLib.url_deleteSalesOrder, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String data = jsonObject.getString("code");
                    if (data.equals("1")) {
                        NumberFormat nf = NumberFormat.getInstance();
                        nf.setMaximumFractionDigits(2);
                        ParamInput.TotalHarga=ParamInput.TotalHarga-Double.parseDouble(tot.getText().toString());
                        OrderedProductList.getTextView().setText(nf.format(ParamInput.TotalHarga));
                        removeAt(position);
                    }
                    Snackbar.make(v, jsonObject.getString("message"), Snackbar.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                View v;
                Toast.makeText(context,
                        volleyError.getMessage(), Toast.LENGTH_LONG).show();
                loading.dismiss();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idparam);
                params.put("organizationId", organizationId);
                params.put("customerId", ParamInput.idcustomer);
                params.put("idSO", ParamInput.idSO);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(strReq);
    }

    public void removeAt(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }
}
