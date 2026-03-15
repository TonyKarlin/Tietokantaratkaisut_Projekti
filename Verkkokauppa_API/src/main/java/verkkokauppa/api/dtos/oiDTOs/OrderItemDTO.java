package verkkokauppa.api.dtos.oiDTOs;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import verkkokauppa.api.entity.OrderItem;

import java.math.BigDecimal;

@JsonPropertyOrder({"orderId", "productId", "quantity", "unitPrice", "discountedPrice"})
public class OrderItemDTO {
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountedPrice;

    private Integer orderId;
    private Integer productId;

    public OrderItemDTO() {
    }

    public OrderItemDTO(OrderItem oI) {
        this.quantity = oI.getQuantity();
        this.unitPrice = oI.getUnitPrice();
        this.orderId = oI.getOrder().getId();
        this.productId = oI.getProduct().getId();
        this.discountedPrice = oI.getDiscountedPrice();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public BigDecimal getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(BigDecimal discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
}
