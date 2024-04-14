package model;

public class inStateCustomer extends customer {

    private String distance;
    private String inStateDiscount;

    public inStateCustomer(int customerId, String customerName, String address, String postalCode, String phone,
                           String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, int divisionId,
                           String distance, String inStateDiscount) {
        super(customerId, customerName, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
        this.distance = distance;
        this.inStateDiscount = inStateDiscount;
    }

    public void displayDetails() {
        super.displayDetails();
        System.out.println("Distance: " + getDistance());
        System.out.println("In-state discount: " + getInStateDiscount());
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getInStateDiscount() {
        return inStateDiscount;
    }

    public void setInStateDiscount(String inStateDiscount) {
        this.inStateDiscount = inStateDiscount;
    }
}