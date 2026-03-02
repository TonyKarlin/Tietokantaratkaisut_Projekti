package verkkokauppa.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name="customeraddresses")
public class CustomerAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne()
    @JoinColumn(name = "customer_id", nullable = false)
    @MapsId
    private Customer customer;

    @Column()
    private String streetAddress;

    @Column(length = 5)
    private String postalCode;

    @Column
    private String city;

    @Column
    private String country;

    public CustomerAddress() {}

    public CustomerAddress(Customer customer, String street, String post, String city, String country) {
        this.customer = customer;
        this.streetAddress = street;
        this.postalCode = post;
        this.city = city;
        this.country = country;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customerId) {
        this.customer = customerId;
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
