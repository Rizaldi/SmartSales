package smartsales.rizaldi.com.smartsales.Sales;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
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

import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;

public class RVAListCustomer extends RecyclerView.Adapter<RVAListCustomer.ViewHolder> {
    List<ListCustomer> customerList;
    Context context;

    public RVAListCustomer(List<ListCustomer> customerList, Context context) {
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
        ImageView btnhps;
        TextView id,name,address;
        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_customer,parent,false));
            id=(TextView) itemView.findViewById(R.id.customer_list_id);
            name=(TextView) itemView.findViewById(R.id.customer_list_name);
            address=(TextView)itemView.findViewById(R.id.customer_list_address);
            btnhps=(ImageView) itemView.findViewById(R.id.btnhps);
            cv=(CardView) itemView.findViewById(R.id.cv);
            btnhps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteList(id.getText().toString(), v,getPosition());

                }
            });
        }
    }

    private void DeleteList(String s, final View v, final int position) {
        final ProgressDialog loading = new ProgressDialog(context);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_deleteList + s , new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String data = jsonObject.getString("code");
                    if(data.equals("1")){
                        removeAt(position);
                    }
                    Snackbar.make(v,jsonObject.getString("message"),Snackbar.LENGTH_LONG).show();
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
        );
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(strReq);
    }
    public void removeAt(int position) {
        customerList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, customerList.size());
    }
}
