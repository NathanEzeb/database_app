package model;

public class outStateCustomer extends customer {

    private String distance;
    private String upcharge;

    public outStateCustomer(int customerId, String customerName, String address, String postalCode, String phone,
                            String createDate, String createdBy, String lastUpdate, String lastUpdatedBy, int divisionId,
                            String distance, String upcharge) {
        super(customerId, customerName, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
        this.distance = distance;
        this.upcharge = upcharge;
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("Distance: " + getDistance());
        System.out.println("Upcharge: " + getUpcharge());
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getUpcharge() {
        return upcharge;
    }

    public void setUpcharge(String upcharge) {
        this.upcharge = upcharge;
    }
}