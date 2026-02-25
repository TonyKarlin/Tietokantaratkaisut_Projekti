package verkkokauppa.api.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "productcategories")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    // 1:M relaatio - yhdellä kategorialla voi olla monta tuotetta
    // LAZY loading: tuotteet ladataan vain kun niitä tarvitaan
    // mappedBy kertoo, että Product-entiteetin 'category' kenttä hallinnoi relaatiota
    // @JsonManagedReference estää ympyräviittauksen JSON-serialisoinnissa
    @JsonManagedReference
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    public ProductCategory() {

    }

    public ProductCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }

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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    // Helper-metodi tuotteen lisäämiseen kategoriaan
    public void addProduct(Product product) {
        products.add(product);
        product.setCategory(this);
    }

    // Helper-metodi tuotteen poistamiseen kategoriasta
    public void removeProduct(Product product) {
        products.remove(product);
        product.setCategory(null);
    }
}
