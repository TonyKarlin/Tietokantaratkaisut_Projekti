package verkkokauppa.api.dtos;

import verkkokauppa.api.entity.ProductCategory;

public class ProductCategoryDTO {

    private Integer id;
    private String name;
    private String description;

    public ProductCategoryDTO() {
    }

    public ProductCategoryDTO(ProductCategory productCategory) {
        this.id = productCategory.getId();
        this.name = productCategory.getName();
        this.description = productCategory.getDescription();

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
}
