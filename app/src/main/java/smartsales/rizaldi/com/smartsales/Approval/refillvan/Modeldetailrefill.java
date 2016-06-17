package smartsales.rizaldi.com.smartsales.Approval.refillvan;

/**
 * Created by Toshiba-PC on 6/1/2016.
 */
public class Modeldetailrefill {
    String id,productid,uomid,productname,orderedqty,uom,warehousestock;

    public String getId() {
        return id;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getUomid() {
        return uomid;
    }

    public void setUomid(String uomid) {
        this.uomid = uomid;
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

    public String getWarehousestock() {
        return warehousestock;
    }

    public void setWarehousestock(String warehousestock) {
        this.warehousestock = warehousestock;
    }
}
