package smartsales.rizaldi.com.smartsales.Sales.invoicepayment;

/**
 * Created by Toshiba-PC on 6/30/2016.
 */
public class Modelcustomerinvoice {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String customer;
    String total;
    String remaining;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }
}
