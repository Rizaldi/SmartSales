package smartsales.rizaldi.com.smartsales;

/**
 * Created by spasi on 06/03/2016.
 */
public class UrlLib {
    public static String urlGoogleMaps = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=depok&destinations=jakarta&language=id-ID";
    public static String url_sales="http://119.235.30.119/smartweb/sales/query/";
    public static String url_customer="http://119.235.30.119/smartweb/customers/query/?salesId=";
    public static String url_save_itinary="http://119.235.30.119/smartweb/itinerary/query/?action=save";
    public static String url_listcustomer="http://119.235.30.119/smartweb/itinerary/query/?action=view&tgl=";
    public static String url_deleteList="http://119.235.30.119/smartweb/itinerary/query/?action=deleted&id=";
    public static String url_customerlocation="http://119.235.30.119/smartweb/customerLocation/query/?customerId=";
    public static String URL_LOGIN="http://119.235.30.119/smartweb/login/query/";
    public static String url_jadwal="http://119.235.30.119/smartweb/schedule/query/?tgl=";
    public static String saveCheckIn="http://119.235.30.119/smartweb/checkin/query/";
    public static String saveCheckOut="http://119.235.30.119/smartweb/checkout/query/";
    public static String saveCheckInVan="http://119.235.30.119/smartweb/vansalescheckin/query/";
    public static String saveCheckOutVan="http://119.235.30.119/smartweb/vansalescheckout/query/";
    public static String url_category="http://119.235.30.119/smartweb/customerCategory/query/?organizationId=";
    public static String url_brand="http://119.235.30.119/smartweb/productBrand/query/?organizationId=";
    public static String url_categoryproduct="http://119.235.30.119/smartweb/productCategory/query/?organizationId=";
    public static String url_listdataproduct="http://119.235.30.119/smartweb/product/query/?organizationId=";
    public static String url_addSalesOrder="http://119.235.30.119/smartweb/addSalesOrder/query/";
    public static String url_saveSalesOrder="http://119.235.30.119/smartweb/saveSalesOrder/query/";
    public static String url_deleteSalesOrder="http://119.235.30.119/smartweb/deleteSalesOrder/query/";
    public static String url_listapproval="http://119.235.30.119/smartweb/approvalSalesOrder/query/?positionStatus=";
    public static String url_listapprovalproduct="http://119.235.30.119/smartweb/approvalSalesOrderDetail/query/?idSO=";
    public static String url_approveso="http://119.235.30.119/smartweb/approvalSalesOrderProses/query/";
    public static String url_rejectso="http://119.235.30.119/smartweb/approvalSalesOrderReject/query/";
    public static String url_rejectItem="http://119.235.30.119/smartweb/approvalSalesOrderRejectItem/query/";
    public static String url_editItem="http://119.235.30.119/smartweb/approvalSalesOrderEditItem/query/";
    public static String url_listhistory="http://119.235.30.119/smartweb/transactionHistory/query/";
    public static String url_lisdetail=" http://119.235.30.119/smartweb/transactionHistoryDetail/query/?salesOrderID=";
    public static String url_listprodct="http://119.235.30.119/smartweb/productList/query/?organizationId=";
    public static String url_listopcustomer="http://119.235.30.119/smartweb/top10Customer/query/";
    public static String url_returnhistory="http://119.235.30.119/smartweb/returnHistory/query/";
    public static String url_getcompetitor="http://119.235.30.119/smartweb/competitorData/query/?employeeId=";
    public static String url_addcompetitor="http://119.235.30.119/smartweb/competitorSave/query/";
    public static String url_getsalestarget="http://119.235.30.119/smartweb/salesTarget/query/?month=";
    public static String url_getstockbalance="http://119.235.30.119/smartweb/stockBalance/query/?warehouseId=";
    public static String url_getproposereport="http://119.235.30.119/smartweb/proposeSoReport/query/";
    public static String url_getdetailpropose="http://119.235.30.119/smartweb/proposeSoReportDetail/query/?customerId=";
    public static String url_getdetailgoodreturn="http://119.235.30.119/smartweb/returnHistoryDetail/query/?returnId=";
    public static String url_reportrefill="http://119.235.30.119/smartweb/refillHistoryDetail/query/?refillId=";
    public static String url_reportrefillhistory="http://119.235.30.119/smartweb/refillHistory/query/";
    public static String url_saveProposeSalesOrder="http://119.235.30.119/smartweb/saveProposeSO/query/";
    public static String url_listrefill="http://119.235.30.119/smartweb/approvalRefill/query/";
    public static String url_listapprovalrefill="http://119.235.30.119/smartweb/approvalRefillDetail/query/?refillId=";
    public static String rejectrefill="http://119.235.30.119/smartweb/rejectRefillOrder/query/";
    public static String aproverefill="http://119.235.30.119/smartweb/approveRefillOrder/query/";
    public static String url_listapprovegood="http://119.235.30.119/smartweb/approvalReturn/query/?positionStatus=";
    public static String url_listapprovalreturn="http://119.235.30.119/smartweb/approvalReturnDetail/query/?idReturn=";
    public static String rejectreturn="http://119.235.30.119/smartweb/rejectReturnOrder/query/";
    public static String aprovereturn="http://119.235.30.119/smartweb/approveReturnOrder/query/";
    public static String url_getstockcustomer=" http://119.235.30.119/smartweb/customerStock/query/?employeeId=";
    public static String url_savecustomerstock="http://119.235.30.119/smartweb/customerStockSave/query/";
    public static String url_customeraging="http://119.235.30.119/smartweb/customerAging/query/?positionStatus=";
    public static String url_customeragingdetail="http://119.235.30.119/smartweb/customerAgingDetail/query/?customerId=";
    public static String url_efectivecall="http://119.235.30.119/smartweb/effectiveCalls/query/?startDate=";
    public static String url_getinputso="http://119.235.30.119/smartweb/productDetail/query/?organizationId=";
    public static String url_orderlist="http://119.235.30.119/smartweb/salesOrderdetail/query/?organizationId=";
    public static String url_efectivedetail="http://119.235.30.119/smartweb/effectiveCallsDetail/query/?startDate=";
    public static String url_invoicepayment="http://119.235.30.119/smartweb/invoicePayment/query/?positionStatus=";
    public static String url_detailinvoice="http://119.235.30.119/smartweb/invoicePaymentDetail/query/?customerId=";
    public static String url_saveInvoicePayment="http://119.235.30.119/smartweb/saveInvoicePayment/query/";
    public static String url_tracking="http://119.235.30.119/smartweb/trackingData/query/";
    public static String url_picture="http://119.235.30.119/smartweb/trackingPicture/query/?visitId=";
    public static String url_datatracking="http://119.235.30.119/smartweb/trackingMap/query/?visitId=";
    public static String to_warehouse="http://119.235.30.119/smartweb/customersOrg/query/?organizationId=";
    public static String url_product="http://119.235.30.119/smartweb/productReturn/query/?";
    public static String condition_ofgood="http://119.235.30.119/smartweb/goodsCondition/query/";
    public static String url_save_goodreturn="http://119.235.30.119/smartweb/saveReturnOrder/query/";
    public static String url_save_refillvan="http://119.235.30.119/smartweb/saveRefillOrder/query/";

    public static String MapsURL(Double lat, Double longs)
    {
        return "http://maps.google.com/maps/api/geocode/json?latlng="+lat+","+longs+"&sensor=true";
    }
    public static String productreffill(String organitation_id)
    {
        return "http://119.235.30.119/smartweb/productRefill/query/?organizationId="+organitation_id;
    }
    public static String UOM(String organitation_id)
    {
        return "http://119.235.30.119/smartweb/productUom/query/?productId="+organitation_id;
    }

}
