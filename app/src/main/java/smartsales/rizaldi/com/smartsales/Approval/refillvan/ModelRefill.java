package smartsales.rizaldi.com.smartsales.Approval.refillvan;

/**
 * Created by Toshiba-PC on 5/31/2016.
 */
public class ModelRefill {
    String id,document_number,date_transaction,time_transaction,sales_representative_id,sales_name,status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocument_number() {
        return document_number;
    }

    public void setDocument_number(String document_number) {
        this.document_number = document_number;
    }

    public String getDate_transaction() {
        return date_transaction;
    }

    public void setDate_transaction(String date_transaction) {
        this.date_transaction = date_transaction;
    }

    public String getTime_transaction() {
        return time_transaction;
    }

    public void setTime_transaction(String time_transaction) {
        this.time_transaction = time_transaction;
    }

    public String getSales_representative_id() {
        return sales_representative_id;
    }

    public void setSales_representative_id(String sales_representative_id) {
        this.sales_representative_id = sales_representative_id;
    }

    public String getSales_name() {
        return sales_name;
    }

    public void setSales_name(String sales_name) {
        this.sales_name = sales_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
