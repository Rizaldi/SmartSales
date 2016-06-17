package smartsales.rizaldi.com.smartsales.Approval.refillvan;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartsales.rizaldi.com.smartsales.Approval.approvalso.ListApprovalSales;
import smartsales.rizaldi.com.smartsales.Approval.approvalso.ModelProductList;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.Varglobal;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAApprovalrefill extends RecyclerView.Adapter<RVAApprovalrefill.ViewHolder> {
    List<Modeldetailrefill> list_product;
    public Context context;
    List<Modelapproval> list;
    static String approvalList="";

    public RVAApprovalrefill(List<Modeldetailrefill> list_product, Context context) {
        this.list_product = list_product;
        this.context = context;
        list=new ArrayList<>();
    }

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
        holder.stock.setText(list_product.get(position).getWarehousestock());

    }

    @Override
    public int getItemCount() {
        return list_product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText approvedqty;
        TextView id, productname, orderqty, uom, stock;
        CheckBox checkbox;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listdetailapproverefill, parent, false));
            id = (TextView) itemView.findViewById(R.id.id);
            approvedqty = (EditText) itemView.findViewById(R.id.approvedqty);
            productname = (TextView) itemView.findViewById(R.id.productname);
            orderqty = (TextView) itemView.findViewById(R.id.orderqty);
            uom = (TextView) itemView.findViewById(R.id.uom);
            stock = (TextView) itemView.findViewById(R.id.stock);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (approvedqty.getText().toString().isEmpty()) {
                            Toast.makeText(context, "fill data approved qty", Toast.LENGTH_SHORT).show();
                            checkbox.setChecked(false);
                        } else {
                               Modelapproval items=new Modelapproval();
                               items.setApprovalList(id.getText().toString()+","
                                       +list_product.get(getPosition()).getProductid()+","+
                                       orderqty.getText()+","+approvedqty.getText()
                                       );
                            list.add(items);
                            for (int i=0;i<list.size();i++){
//                                if(i==0 && list.size()==1){
//                                    Varglobal.approvallist=list.get(i).getApprovalList();
//                                }
                                if(i==0){
                                    Varglobal.approvallist=list.get(i).getApprovalList()+"|";
                                }else{
                                    Varglobal.approvallist=Varglobal.approvallist+list.get(i).getApprovalList()+"|";
                                }
                            }
                        }
                    }else{
                        list.clear();
                        Varglobal.approvallist="";
                    }

                }
            });

        }
    }
}
