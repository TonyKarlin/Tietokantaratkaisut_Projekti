package verkkokauppa.api.utility.assemblers;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import verkkokauppa.api.controller.ProductController;
import verkkokauppa.api.dtos.productDTOs.ProductDTO;
import verkkokauppa.api.entity.Product;

@NullMarked
@Component
public class ProductModelAssembler implements RepresentationModelAssembler<Product, EntityModel<ProductDTO>> {

    @Override
    public EntityModel<ProductDTO> toModel(Product product) {
        // Käytetään suoraan konstruktoria ModelMapperin sijaan
        ProductDTO dto = new ProductDTO(product);
        return EntityModel.of(
                dto,
                linkTo(methodOn(ProductController.class).getProductById(product.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).getAllProducts(Pageable.unpaged())).withRel("products")
        );
    }
}
