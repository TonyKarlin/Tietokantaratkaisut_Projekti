package verkkokauppa.api.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplierAddress> addresses = new ArrayList<>();

    @ManyToMany(
            mappedBy = "suppliers",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private Set<Product> products = new HashSet<>() >();

    public Supplier() {
    }

    public Supplier(String name, String contactName, String phone, String email) {
        this.name = name;
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    /**
     * Helper-metodi tuotteen lisäämiseen. Huolehtii kahdensuuntaisen suhteen
     * ylläpidosta.
     */
    public void addProduct(Product product) {
        this.products.add(product);
        product.getSuppliers().add(this);
    }

    /**
     * Helper-metodi tuotteen poistamiseen. Huolehtii kahdensuuntaisen suhteen
     * ylläpidosta.
     */
    public void removeProduct(Product product) {
        this.products.remove(product);
        product.getSuppliers().remove(this);
    }

    public List<SupplierAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<SupplierAddress> addresses) {
        this.addresses = addresses;
    }
}
