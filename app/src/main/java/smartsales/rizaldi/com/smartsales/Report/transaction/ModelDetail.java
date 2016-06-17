package smartsales.rizaldi.com.smartsales.Report.transaction;

/**
 * Created by Toshiba-PC on 5/13/2016.
 */
public class ModelDetail {
    String id,product,qty,uom,harga,tax_code,tax_val,niteamount,dnp,total_amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getTax_code() {
        return tax_code;
    }

    public void setTax_code(String tax_code) {
        this.tax_code = tax_code;
    }

    public String getTax_val() {
        return tax_val;
    }

    public void setTax_val(String tax_val) {
        this.tax_val = tax_val;
    }

    public String getNiteamount() {
        return niteamount;
    }

    public void setNiteamount(String niteamount) {
        this.niteamount = niteamount;
    }

    public String getDnp() {
        return dnp;
    }

    public void setDnp(String dnp) {
        this.dnp = dnp;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }
}
