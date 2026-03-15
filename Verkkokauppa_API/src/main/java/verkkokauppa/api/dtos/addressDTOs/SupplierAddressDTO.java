package verkkokauppa.api.dtos.addressDTOs;

import verkkokauppa.api.entity.SupplierAddress;

public class SupplierAddressDTO {

    private Integer id;
    private Integer supplierId;
    private String streetAddress;
    private String postalCode;
    private String city;
    private String country;

    public SupplierAddressDTO() {
    }

    public SupplierAddressDTO(SupplierAddress address) {
        this.id = address.getId();
        this.supplierId = address.getSupplier() != null ? address.getSupplier().getId() : null;
        this.streetAddress = address.getStreetAddress();
        this.postalCode = address.getPostalCode();
        this.city = address.getCity();
        this.country = address.getCountry();
    }

    public SupplierAddressDTO(Integer id, Integer supplierId, String streetAddress,
            String postalCode, String city, String country) {
        this.id = id;
        this.supplierId = supplierId;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
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
