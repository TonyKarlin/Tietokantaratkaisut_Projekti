package verkkokauppa.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name="customeraddresses")
public class CustomerAddress extends Address{
    @OneToOne()
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public CustomerAddress() {}

    public CustomerAddress(Customer customer, String street, String post, String city, String country) {
        this.customer = customer;
        this.setStreetAddress(street);
        this.setPostalCode(post);
        this.setCity(city);
        this.setCountry(country);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customerId) {
        this.customer = customerId;
    }
}
