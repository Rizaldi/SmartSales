package smartsales.rizaldi.com.smartsales.Approval.Itenerary;

/**
 * Created by spasi on 07/03/2016.
 */
public class IteneraryModel {
    String name,id_name;
    public IteneraryModel(){

    }
    public IteneraryModel(String name,String id_name){
        this.name = name;
        this.id_name = id_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }


}
