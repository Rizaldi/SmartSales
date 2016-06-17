package smartsales.rizaldi.com.smartsales.Approval.AdapterModelApproval;

/**
 * Created by spasi on 08/03/2016.
 */
public class ModelApprovalMenu {
    String approval_menu;
    String id_approval;

    public ModelApprovalMenu(){

    }
    public ModelApprovalMenu(String approval_menu, String id_approval){
        this.approval_menu = approval_menu;
        this.id_approval = id_approval;
    }
    public String getId_approval() {
        return id_approval;
    }

    public void setId_approval(String id_approval) {
        this.id_approval = id_approval;
    }

    public String getApproval_menu() {
        return approval_menu;
    }

    public void setApproval_menu(String approval_menu) {
        this.approval_menu = approval_menu;
    }

}
