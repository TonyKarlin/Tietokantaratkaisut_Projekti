package verkkokauppa.api.utility.assemblers;

import org.jspecify.annotations.NullMarked;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import verkkokauppa.api.controller.ProductCategoryController;
import verkkokauppa.api.dtos.ProductCategoryDTO;
import verkkokauppa.api.entity.ProductCategory;

@NullMarked
@Component
public class ProductCategoryModelAssembler implements RepresentationModelAssembler<ProductCategory, EntityModel<ProductCategoryDTO>> {

    @Override
    public EntityModel<ProductCategoryDTO> toModel(ProductCategory productCategory) {
        ProductCategoryDTO dto = new ModelMapper().map(productCategory, ProductCategoryDTO.class);
        return EntityModel.of(
                dto,
                linkTo(methodOn(ProductCategoryController.class).getProductCategoryById(productCategory.getId())).withSelfRel(),
                linkTo(methodOn(ProductCategoryController.class).getAllProductCategories(Pageable.unpaged())).withRel("productcategories")
        );
    }
}
