package smartsales.rizaldi.com.smartsales.Report.goodreturn;

/**
 * Created by Toshiba-PC on 5/28/2016.
 */
public class ModelProductlist {
    String product,quanity,uom,condition,expired;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQuanity() {
        return quanity;
    }

   public void setQuanity(String quanity) {
        this.quanity = quanity;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }
}
