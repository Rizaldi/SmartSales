package smartsales.rizaldi.com.smartsales.Approval.goodreturn;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import smartsales.rizaldi.com.smartsales.Approval.refillvan.Modelapproval;
import smartsales.rizaldi.com.smartsales.Approval.refillvan.Modeldetailrefill;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Varglobal;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAApprovalreturn extends RecyclerView.Adapter<RVAApprovalreturn.ViewHolder> {
    List<Modeldetailgood> list_product;
    public Context context;
    List<Modelapproval> list;
    static String approvalList="";

    public RVAApprovalreturn(List<Modeldetailgood> list_product, Context context) {
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
//        holder.productname.setText(list_product.get(position).getProduct_id());
        holder.orderqty.setText(list_product.get(position).getQty());
//        holder.uom.setText(list_product.get(position).get());
        holder.reason.setText(list_product.get(position).getCondition());
        holder.expdate.setText(list_product.get(position).getExpireddate());
    }

    @Override
    public int getItemCount() {
        return list_product.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText approvedqty;
        TextView id, productname, orderqty, uom, reason,expdate;
        CheckBox checkbox;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.listdetailapprovereturn, parent, false));
            id = (TextView) itemView.findViewById(R.id.id);
            approvedqty = (EditText) itemView.findViewById(R.id.approvedqty);
            productname = (TextView) itemView.findViewById(R.id.productname);
            orderqty = (TextView) itemView.findViewById(R.id.orderqty);
            uom = (TextView) itemView.findViewById(R.id.uom);
            reason = (TextView) itemView.findViewById(R.id.reason);
            expdate = (TextView) itemView.findViewById(R.id.expdate);
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
                                       +list_product.get(getPosition()).getProduct_id()+","+
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
