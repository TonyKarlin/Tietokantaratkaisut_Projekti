package verkkokauppa.api.dtos;

import verkkokauppa.api.entity.Order;
import verkkokauppa.api.entity.OrderItem;
import verkkokauppa.api.entity.Product;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Integer id;

    private Integer quantity;
    private BigDecimal unitPrice;

    private Integer orderId;
    private Integer productId;

    public OrderItemDTO() {}

    public OrderItemDTO(OrderItem oI) {
        this.id = oI.getId();
        this.quantity = oI.getQuantity();
        this.unitPrice = oI.getUnitPrice();
        this. orderId = oI.getOrder().getId();
        this.productId = oI.getProduct().getId();
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
}
