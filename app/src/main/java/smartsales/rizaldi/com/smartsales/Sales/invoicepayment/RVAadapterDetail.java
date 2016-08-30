package smartsales.rizaldi.com.smartsales.Sales.invoicepayment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Varglobal;
import smartsales.rizaldi.com.smartsales.customerorder.Payment;

/**
 * Created by Toshiba-PC on 4/23/2016.
 */
public class RVAadapterDetail extends RecyclerView.Adapter<RVAadapterDetail.ViewHolder> {
    List<ModelDetail> list;
    List<ModelInvoice> arrPayment;
    public Context context;

    public RVAadapterDetail(List<ModelDetail> list, Context context) {
        this.list = list;
        this.context = context;
        arrPayment = new ArrayList<>();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.date.setText(list.get(position).getDate());
        holder.invoice.setText(list.get(position).getInvoice());
        holder.payment.setText(list.get(position).getPayment());
        holder.time.setText(list.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView invoice, date, time, payment;
        CheckBox checkbox;
        EditText etpayment;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detailinvoice, parent, false));
            date = (TextView) itemView.findViewById(R.id.date);
            invoice = (TextView) itemView.findViewById(R.id.invoice);
            payment = (TextView) itemView.findViewById(R.id.payment);
            time = (TextView) itemView.findViewById(R.id.time);
            etpayment = (EditText) itemView.findViewById(R.id.etpayment);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            etpayment.setEnabled(false);
            checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {

                        if (Varglobal.payment.isEmpty()) {
                            Toast.makeText(context, "fill total payment", Toast.LENGTH_SHORT).show();
                            checkbox.setChecked(false);
                        } else {
                            if ((Double.parseDouble(Varglobal.payment) <= Double.parseDouble(payment.getText().toString())) && !Varglobal.payment.equals("0")) {
                                etpayment.setText(Varglobal.payment);
                                Varglobal.payment = "0";
                            } else if (Double.parseDouble(Varglobal.payment) > Double.parseDouble(payment.getText().toString())) {
                                Varglobal.payment = String.valueOf(Double.parseDouble(Varglobal.payment) - Double.parseDouble(payment.getText().toString()));
                                etpayment.setText(payment.getText().toString());
                            } else {
                                checkbox.setChecked(false);
                                Toast.makeText(context, "can't check again because payment is use up", Toast.LENGTH_SHORT).show();
                            }
                        }

                        ModelInvoice items = new ModelInvoice();
                        items.setInvoice(invoice.getText().toString() + ","
                                + payment.getText().toString() + "," +
                                etpayment.getText().toString()
                        );
                        items.setPosition(getPosition());
                        arrPayment.add(items);
                        for (int i = 0; i < arrPayment.size(); i++) {

                            if (i == 0) {
                                Varglobal.arrPayment = arrPayment.get(i).getInvoice() + "|";
                            } else {
                                Varglobal.arrPayment = Varglobal.arrPayment + arrPayment.get(i).getInvoice() + "|";
                            }
                        }
                    } else {

                        for (int i = 0; i < arrPayment.size(); i++) {
                            if (arrPayment.get(i).getPosition() == getPosition()) {
                                arrPayment.remove(i);
                                Varglobal.arrPayment = "";
                            }
                        }
                        for (int j = 0; j < arrPayment.size(); j++) {

                            if (j == 0) {
                                Varglobal.arrPayment = arrPayment.get(j).getInvoice() + "|";
                            } else {
                                Varglobal.arrPayment = Varglobal.arrPayment + arrPayment.get(j).getInvoice() + "|";
                            }
                        }

                        Varglobal.payment = String.valueOf(Double.parseDouble(Varglobal.payment) + Double.parseDouble(etpayment.getText().toString()));
                        etpayment.setText("");
                    }
                }
            });
        }
    }
}
