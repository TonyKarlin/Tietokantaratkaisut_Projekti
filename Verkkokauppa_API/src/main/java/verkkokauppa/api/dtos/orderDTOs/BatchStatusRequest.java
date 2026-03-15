package verkkokauppa.api.dtos.orderDTOs;

import java.util.List;

public class BatchStatusRequest {
    private List<Integer> orderIds;
    private String fromStatus;
    private String toStatus;

    // getters and setters
    public List<Integer> getOrderIds() { return orderIds; }
    public void setOrderIds(List<Integer> orderIds) { this.orderIds = orderIds; }
    public String getFromStatus() { return fromStatus; }
    public void setFromStatus(String fromStatus) { this.fromStatus = fromStatus; }
    public String getToStatus() { return toStatus; }
    public void setToStatus(String toStatus) { this.toStatus = toStatus; }
}

