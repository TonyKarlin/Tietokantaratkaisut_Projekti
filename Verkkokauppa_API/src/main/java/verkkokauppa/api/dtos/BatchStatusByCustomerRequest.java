package verkkokauppa.api.dtos;

public class BatchStatusByCustomerRequest {
    private Integer customerId;
    private String fromStatus;
    private String toStatus;

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }
    public String getFromStatus() { return fromStatus; }
    public void setFromStatus(String fromStatus) { this.fromStatus = fromStatus; }
    public String getToStatus() { return toStatus; }
    public void setToStatus(String toStatus) { this.toStatus = toStatus; }
}
