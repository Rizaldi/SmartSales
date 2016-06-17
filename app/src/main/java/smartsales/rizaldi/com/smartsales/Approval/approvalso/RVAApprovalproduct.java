package smartsales.rizaldi.com.smartsales.Approval.approvalso;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
public class RVAApprovalproduct extends RecyclerView.Adapter<RVAApprovalproduct.ViewHolder> {
    List<ModelProductList> list_product;
    public Context context;
    String warehouseId;
    SqliteHandler db;
    public interface BtnClickListener {
        public abstract void onBtnClick(int position);
    }

    //    private BtnClickListener mClickListener = null;
    public RVAApprovalproduct(List<ModelProductList> list_product, Context context) {
        this.list_product = list_product;
        this.context = context;
        db = new SqliteHandler(context);
        HashMap<String, String> user = db.getUserDetails();
        warehouseId = user.get("warehouse_id");
    }
//    public RVAApprovalproduct(List<ModelProductList> list_product, Context context, BtnClickListener listener) {
//        this.list_product = list_product;
//        this.context = context;
//        mClickListener=listener;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.id.setText(list_product.get(position).getId());
        holder.productname.setText(list_product.get(position).getProductname());
        holder.orderqty.setText(list_product.get(position).getOrderedqty());
        holder.uom.setText(list_product.get(position).getUom());
        holder.unitprice.setText(list_product.get(position).getUnitprice());
        holder.taxcode.setText(list_product.get(position).getTaxcode());
        holder.netamount.setText(list_product.get(position).getNetamount());
        holder.dnp.setText(list_product.get(position).getDnp());
        holder.tax.setText(list_product.get(position).getTax());
        holder.total.setText(list_product.get(position).getTotal());
        holder.stock.setText(list_product.get(position).getWarehousestock());
        holder.approvedqty.setText(list_product.get(position).getApprovedqty());

    }

    @Override
    public int getItemCount() {
        return list_product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        EditText approvedqty;
        public ImageView dislike, editquantity;
        TextView id, productname, orderqty, uom, unitprice, taxcode, netamount, dnp, tax, total, stock;
        LinearLayout placesend;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listproductapprovalso, parent, false));
            cv = (CardView) itemView.findViewById(R.id.cv);
            id = (TextView) itemView.findViewById(R.id.id);
            approvedqty = (EditText) itemView.findViewById(R.id.approvedqty);
            productname = (TextView) itemView.findViewById(R.id.productname);
            orderqty = (TextView) itemView.findViewById(R.id.orderqty);
            uom = (TextView) itemView.findViewById(R.id.uom);
            total = (TextView) itemView.findViewById(R.id.total);
            unitprice = (TextView) itemView.findViewById(R.id.unitprice);
            taxcode = (TextView) itemView.findViewById(R.id.taxcode);
            netamount = (TextView) itemView.findViewById(R.id.netamount);
            dnp = (TextView) itemView.findViewById(R.id.dnp);
            tax = (TextView) itemView.findViewById(R.id.tax);
            stock = (TextView) itemView.findViewById(R.id.stock);
            dislike = (ImageView) itemView.findViewById(R.id.dislike);
            placesend = (LinearLayout) itemView.findViewById(R.id.placesend);
            dislike.setTag(getPosition());
            editquantity = (ImageView) itemView.findViewById(R.id.editquantity);
            editquantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditItem(id.getText().toString(), approvedqty.getText().toString());
                }
            });
            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RejectItem(id.getText().toString(), getPosition());
                }
            });

            approvedqty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        private void RejectItem(final String id, final int position) {
            final ProgressDialog loading = new ProgressDialog(context);
            loading.setMessage("please wait...");
            loading.setCancelable(false);
            loading.show();
            StringRequest strReq = new StringRequest(Request.Method.POST, UrlLib.url_rejectItem, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    loading.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String code = jsonObject.getString("code");
                        if (code.equals("1")) {
                            removeAt(position);
//                            Intent i=new Intent(context, ListApprovalSales.class);
//                            Bundle b = new Bundle();
//                            b.putString("idSO",ListApprovalSales.idSO);
//                            i.putExtras(b);
//                            context.startActivity(i);
//                            ((Activity)context).finish();
                        } else {
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(context,
                            volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    loading.dismiss();
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", id);
                    params.put("idSO", ListApprovalSales.idSO);
                    params.put("warehouseId", warehouseId);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(strReq);
        }

        public void removeAt(int position) {
            list_product.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list_product.size());
        }

        private void EditItem(final String id, final String qty) {
            final ProgressDialog loading = new ProgressDialog(context);
            loading.setMessage("please wait...");
            loading.setCancelable(false);
            loading.show();
            StringRequest strReq = new StringRequest(Request.Method.POST, UrlLib.url_editItem, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Log.e("JSON", s);
                    loading.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String code = jsonObject.getString("code");
                        if (code.equals("1")) {
                            Bundle b = new Bundle();
                            b.putString("idSO", ListApprovalSales.idSO);
                            Toast.makeText(context, "data berhasil di edit", Toast.LENGTH_SHORT).show();
//                            Intent i=new Intent(context, ListApprovalSales.class);
//                            i.putExtras(b);
//                            context.startActivity(i);
//                            ((Activity)context).finish();
                        } else {
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(context,
                            volleyError.getMessage(), Toast.LENGTH_LONG).show();
                    loading.dismiss();
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", id);
                    params.put("idSO", ListApprovalSales.idSO);
                    params.put("approvedQty", qty);
                    params.put("warehouseId", warehouseId);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(strReq);
            Log.e("tesdata", strReq.toString());
        }
    }
}
