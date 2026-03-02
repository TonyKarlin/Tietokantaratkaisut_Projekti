package verkkokauppa.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "supplieraddresses")
public class SupplierAddress extends Address {
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    public SupplierAddress() {
    }

    public SupplierAddress(Supplier supplier, String streetAddress, String postalCode, String city, String country) {
        this.supplier = supplier;
        this.setStreetAddress(streetAddress);
        this.setPostalCode(postalCode);
        this.setCity(city);
        this.setCountry(country);
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}
