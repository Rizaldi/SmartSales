package smartsales.rizaldi.com.smartsales.stock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

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
public class RVAadapterStock extends RecyclerView.Adapter<RVAadapterStock.ViewHolder> {
    List<Modelcustomerstock> list;
    LayoutInflater mLayoutInflater;
    public Context context;
    SqliteHandler db;
    public  String employeeId,productId,username;
    public RVAadapterStock(List<Modelcustomerstock> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public interface BtnClickListener {
        public abstract void onBtnClick(int position);
    }
    private BtnClickListener mClickListener = null;
    public RVAadapterStock(Context context, List<Modelcustomerstock> list, BtnClickListener listener) {
        this.list = list;
        this.context = context;
        mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mClickListener = listener;
        db=new SqliteHandler(context);
        HashMap<String,String> user=db.getUserDetails();
        employeeId=user.get("employee_id");
        username=user.get("username");
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.product.setText(list.get(position).getProductname());
        holder.addphoto.setTag(position);
        if(list.get(position).getSt().equals("0")){
            holder.etqtyrack.setVisibility(View.VISIBLE);
            holder.etqtywarehouse.setVisibility(View.VISIBLE);
            holder.save.setVisibility(View.VISIBLE);
            holder.addphoto.setVisibility(View.VISIBLE);
            holder.placename.setVisibility(View.VISIBLE);
            holder.filename.setText(list.get(position).getFilename());
        }else{
            holder.photo.setVisibility(View.VISIBLE);
            holder.tvqtywarehouse.setVisibility(View.VISIBLE);
            holder.tvqtyrack.setVisibility(View.VISIBLE);
            holder.tvqtyrack.setText(list.get(position).getQtyrack());
            holder.tvqtywarehouse.setText(list.get(position).getQtywarehouse());
            Picasso.with(context).load("http://" + list.get(position).getUrlimage()).into(holder.photo);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView product, tvqtyrack,tvqtywarehouse,filename;
        EditText  etqtyrack,etqtywarehouse;
        ImageView photo,addphoto,save;
        LinearLayout placename;
        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listcustomerstock, parent, false));
            product = (TextView) itemView.findViewById(R.id.product);
            placename = (LinearLayout) itemView.findViewById(R.id.placename);
            tvqtyrack = (TextView) itemView.findViewById(R.id.tvqtyrack);
            tvqtywarehouse = (TextView) itemView.findViewById(R.id.tvqtywarehouse);
            etqtyrack = (EditText) itemView.findViewById(R.id.etqtyrack);
            etqtywarehouse = (EditText) itemView.findViewById(R.id.etqtywarehouse);
            photo = (ImageView) itemView.findViewById(R.id.photo);
            addphoto = (ImageView) itemView.findViewById(R.id.addphoto);
            filename=(TextView) itemView.findViewById(R.id.filename);
            save = (ImageView) itemView.findViewById(R.id.save);
            addphoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mClickListener != null)
                        mClickListener.onBtnClick((Integer) v.getTag());
                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!etqtyrack.getText().toString().isEmpty() &&
                        !etqtywarehouse.getText().toString().isEmpty()
                        && !filename.getText().toString().isEmpty()    ){
                        saveData();
//                        Toast.makeText(context, employeeId+"-"+list.get(getPosition()).getProductId()
//                                +"-"+CustomerStock.start+"-"+etqtyrack.getText().toString()
//                                +"-"+etqtywarehouse.getText().toString()+""
//                                +filename.getText().toString()+"-"+username, Toast.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(v,"lengkapi data",Snackbar.LENGTH_LONG).show();
                    }
                }
            });
        }

        private void saveData() {
            final ProgressDialog loading = new ProgressDialog(context);
            loading.setMessage("please wait...");
            loading.setCancelable(false);
            loading.show();
            StringRequest strReq = new StringRequest(Request.Method.POST, UrlLib.url_savecustomerstock, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    loading.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String code = jsonObject.getString("code");
                        if(code.equals("1")){
                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, CustomerStock.class));
                            ((Activity)context).finish();
                        }else{
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
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("image", CustomerStock.image);
                    params.put("customerId", CustomerStock.id_customer);
                    params.put("employeeId", employeeId);
                    params.put("productId", list.get(getPosition()).getProductId());
                    params.put("date", CustomerStock.start);
                    params.put("qtyRack", etqtyrack.getText().toString());
                    params.put("qtyWarehouse", etqtywarehouse.getText().toString());
                    params.put("filename", filename.getText().toString());
                    params.put("username", username);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(strReq);
        }


    }
}
