package smartsales.rizaldi.com.smartsales.Approval.AdapterModelApproval;

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

import java.util.List;

import smartsales.rizaldi.com.smartsales.Approval.Itinerary;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Sales.Tracking.Tracking;

/**
 * Created by spasi on 07/03/2016.
 */
public class CustomApproval extends RecyclerView.Adapter<CustomApproval.ViewHolder> {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelApprovalMenu> modelApprovalMenus;
    Context context;

    public CustomApproval(Context context, List<ModelApprovalMenu> modelApprovalMenus) {
        super();
        this.context = context;
        this.modelApprovalMenus = modelApprovalMenus;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_menu_approval, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelApprovalMenu modelApprovalMenu = modelApprovalMenus.get(position);

        holder.name_approval.setText(modelApprovalMenu.getApproval_menu());
        holder.id_approval.setText(modelApprovalMenu.getId_approval());
    }

    @Override
    public int getItemCount() {
        return modelApprovalMenus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name_approval, id_approval;
        public CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            name_approval = (TextView) itemView.findViewById(R.id.menu_approval);
            id_approval = (TextView) itemView.findViewById(R.id.id_approval);
            card = (CardView) itemView.findViewById(R.id.listDataApprovalMenu);
            card.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String id_app = id_approval.getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString("buat_id_approval", id_app);
//            String no = no_telp.getText().toString();
            if (id_app.equals("1")) {
                Intent i = new Intent(context, Itinerary.class);
                i.putExtras(bundle);
                context.startActivity(i);
            }
            if (id_app.equals("21")) {
                Intent i = new Intent(context, Tracking.class);
                i.putExtras(bundle);
                context.startActivity(i);
            }
        }
    }
}