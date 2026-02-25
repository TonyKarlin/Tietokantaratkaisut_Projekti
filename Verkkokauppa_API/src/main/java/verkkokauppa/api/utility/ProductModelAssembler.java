package verkkokauppa.api.utility;

import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import verkkokauppa.api.controller.ProductController;
import verkkokauppa.api.entity.Product;

@NullMarked
@Component
public class ProductModelAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {

    @Override
    public EntityModel<Product> toModel(Product product) {
        return EntityModel.of(
                product,
                linkTo(methodOn(ProductController.class).getProductById(product.getId())).withSelfRel(),
                linkTo(methodOn(ProductController.class).getAllProducts(Pageable.unpaged())).withRel("products")
        );
    }
}
