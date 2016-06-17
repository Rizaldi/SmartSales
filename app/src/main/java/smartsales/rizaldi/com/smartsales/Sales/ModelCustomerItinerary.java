package smartsales.rizaldi.com.smartsales.Sales;

import java.util.ArrayList;

/**
 * Created by spasi on 09/04/2016.
 */
public class ModelCustomerItinerary {
    String id_customer,nama_customer,brn,gstReg,gstNumber;

    public String getBrn() {
        return brn;
    }

    public void setBrn(String brn) {
        this.brn = brn;
    }

    public String getGstReg() {
        return gstReg;
    }

    public void setGstReg(String gstReg) {
        this.gstReg = gstReg;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getId_customer() {
        return id_customer;
    }

    public void setId_customer(String id_customer) {
        this.id_customer = id_customer;
    }

    public String getNama_customer() {
        return nama_customer;
    }

    public void setNama_customer(String nama_customer) {
        this.nama_customer = nama_customer;
    }
}
