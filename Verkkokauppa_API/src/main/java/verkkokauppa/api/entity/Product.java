package verkkokauppa.api.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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

    // M:1 relaatio - monta tuotetta voi kuulua yhteen kategoriaan
    // EAGER loading: kategoria ladataan aina tuotteen mukana (pieni objekti)
    // JoinColumn määrittää tietokannan sarakkeen nimen
    // @JsonBackReference estää ympyräviittauksen JSON-serialisoinnissa (ei serialisoida takaisin parent-objektia)
    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @Column(name = "supplier_id")
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
}
