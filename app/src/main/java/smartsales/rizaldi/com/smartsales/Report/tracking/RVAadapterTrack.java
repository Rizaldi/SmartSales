package smartsales.rizaldi.com.smartsales.Report.tracking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Report.refilvan.Detailrefill;
import smartsales.rizaldi.com.smartsales.Report.refilvan.ModelRefillvan;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAadapterTrack extends RecyclerView.Adapter<RVAadapterTrack.ViewHolder> {
    List<ModelTrackingList> list;
    public Context context;

    public RVAadapterTrack(List<ModelTrackingList> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.customername.setText(list.get(position).getCustomer());
        holder.address.setText(list.get(position).getAddress());
        holder.date.setText(list.get(position).getDate());
        holder.time.setText(list.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView customername,address,date,time;
        ImageView maps,picture;
        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tracking, parent, false));
            cv = (CardView) itemView.findViewById(R.id.cv);
            customername = (TextView) itemView.findViewById(R.id.customername);
            address = (TextView) itemView.findViewById(R.id.address);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            maps = (ImageView) itemView.findViewById(R.id.maps);
            picture = (ImageView) itemView.findViewById(R.id.picture);
            picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b=new Bundle();
                    b.putString("visitId",list.get(getPosition()).getId());
                    Intent i=new Intent(context,Picture.class);
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });
            maps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b=new Bundle();
                    b.putString("visitId",list.get(getPosition()).getId());
                    Intent i=new Intent(context,MapsActivity.class);
                    i.putExtras(b);
                    context.startActivity(i);
                }
            });
        }
    }
}
