package verkkokauppa.api.dtos;


import verkkokauppa.api.entity.CustomerAddress;

public class AddressDTO {
    private Integer id;
    private Integer customerId;
    private String streetAddress;
    private String postalCode;
    private String city;
    private String country;

    public AddressDTO(){}

    public AddressDTO(CustomerAddress customerAddress) {
        this.id = customerAddress.getId();
        this.customerId = customerAddress.getCustomer().getId();
        this.streetAddress = customerAddress.getStreetAddress();
        this.postalCode = customerAddress.getPostalCode();
        this.city = customerAddress.getCity();
        this.country = customerAddress.getCountry();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
