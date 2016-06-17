package smartsales.rizaldi.com.smartsales.Approval.approvalso;

/**
 * Created by Toshiba-PC on 5/12/2016.
 */
public class ModelProductList {
    String id,productname,orderedqty,uom,unitprice,taxcode,netamount,dnp,tax,total,warehousestock,approvedqty;

    public String getApprovedqty() {
        return approvedqty;
    }

    public void setApprovedqty(String approvedqty) {
        this.approvedqty = approvedqty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getOrderedqty() {
        return orderedqty;
    }

    public void setOrderedqty(String orderedqty) {
        this.orderedqty = orderedqty;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    public String getTaxcode() {
        return taxcode;
    }

    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }

    public String getNetamount() {
        return netamount;
    }

    public void setNetamount(String netamount) {
        this.netamount = netamount;
    }

    public String getDnp() {
        return dnp;
    }

    public void setDnp(String dnp) {
        this.dnp = dnp;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getWarehousestock() {
        return warehousestock;
    }

    public void setWarehousestock(String warehousestock) {
        this.warehousestock = warehousestock;
    }
}
