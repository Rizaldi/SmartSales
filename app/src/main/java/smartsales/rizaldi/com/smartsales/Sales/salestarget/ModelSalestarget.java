package smartsales.rizaldi.com.smartsales.Sales.salestarget;

/**
 * Created by Toshiba-PC on 5/27/2016.
 */
public class ModelSalestarget {
    String brand,productname,salesrep,target,achieved,uom;

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getSalesrep() {
        return salesrep;
    }

    public void setSalesrep(String salesrep) {
        this.salesrep = salesrep;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getAchieved() {
        return achieved;
    }

    public void setAchieved(String achieved) {
        this.achieved = achieved;
    }
}
