package verkkokauppa.api.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(
            name = "product_suppliers",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "supplier_id")
    )
    private Set<Supplier> suppliers = new HashSet<>();

    @OneToOne(mappedBy = "product")
    private OrderItem orderItem;

    // Vanha supplier_id-sarake säilytetään taaksepäin yhteensopivuuden vuoksi
    // insertable=false, updatable=false: JPA ei käytä tätä tallennuksessa
    @Column(name = "supplier_id", insertable = false, updatable = false)
    private Integer supplierId;

    public Product() {

    }

    // Vanha konstruktori säilytetään taaksepäin yhteensopivuuden vuoksi
    // categoryId-parametri jätetään pois, kategoria asetetaan setCategory-metodilla
    public Product(String name, String description, BigDecimal price, Integer stockQuantity, Integer supplierId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.supplierId = supplierId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public Set<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public void addSupplier(Supplier supplier) {
        this.suppliers.add(supplier);
        supplier.getProducts().add(this);
    }

    public void removeSupplier(Supplier supplier) {
        this.suppliers.remove(supplier);
        supplier.getProducts().remove(this);
    }

    /**
     * @deprecated Käytä getCategory().getId() sen sijaan. Tämä metodi
     * poistetaan tulevissa versioissa.
     */
    @Deprecated(since = "1.0", forRemoval = true)
    public Integer getCategoryId() {
        return category != null ? category.getId() : null;
    }

    /**
     * @deprecated Käytä setCategory(ProductCategory) sen sijaan. Tämä metodi
     * poistetaan tulevissa versioissa.
     */
    @Deprecated(since = "1.0", forRemoval = true)
    public void setCategoryId(Integer categoryId) {
        // Tämä metodi säilytetään taaksepäin yhteensopivuuden vuoksi
        // Todellinen kategoria-objekti asetetaan erikseen
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
