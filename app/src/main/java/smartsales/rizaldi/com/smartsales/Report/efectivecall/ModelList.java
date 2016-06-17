package smartsales.rizaldi.com.smartsales.Report.efectivecall;

/**
 * Created by Toshiba-PC on 6/13/2016.
 */
public class ModelList {
    public String getSalesman() {
        return Salesman;
    }

    public void setSalesman(String salesman) {
        Salesman = salesman;
    }

    public String getTargetVisit() {
        return TargetVisit;
    }

    public void setTargetVisit(String targetVisit) {
        TargetVisit = targetVisit;
    }

    public String getTotalVisit() {
        return TotalVisit;
    }

    public void setTotalVisit(String totalVisit) {
        TotalVisit = totalVisit;
    }

    public String getEffectiveCall() {
        return EffectiveCall;
    }

    public void setEffectiveCall(String effectiveCall) {
        EffectiveCall = effectiveCall;
    }

    public String getTotalTransaction() {
        return TotalTransaction;
    }

    public void setTotalTransaction(String totalTransaction) {
        TotalTransaction = totalTransaction;
    }

    String salesrepId;

    public String getSalesrepId() {
        return salesrepId;
    }

    public void setSalesrepId(String salesrepId) {
        this.salesrepId = salesrepId;
    }

    String Salesman;
    String TargetVisit;
    String TotalVisit;
    String EffectiveCall;
    String TotalTransaction;

}
