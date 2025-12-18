import java.io.Serializable;

public class Drug implements Serializable {
    private static final long serialVersionUID = 1L;
    private String drugstoreNum;
    private String drugName;
    private int packagesAvailable;
    private double price;
    private String receiptDate;
    private String expirationDate;

    public Drug(String drugstoreNum,String drugName, int packagesAvailable,double price,String receiptDate, String expirationDate){
        this.drugstoreNum=drugstoreNum;
        this.drugName = drugName;
        this.packagesAvailable = packagesAvailable;
        this.price = price;
        this.receiptDate = receiptDate;
        this.expirationDate = expirationDate;
    }
    public String getDrugstoreNum() { return drugstoreNum; }
    @Override
    public String toString() {
        return String.format("%-12s | %-15s | %-8d | %-8.2f | %-12s | %-12s",
                drugstoreNum, drugName, packagesAvailable, price, receiptDate, expirationDate);
    }

    public String getDrugName() {
        return drugName;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }
}
