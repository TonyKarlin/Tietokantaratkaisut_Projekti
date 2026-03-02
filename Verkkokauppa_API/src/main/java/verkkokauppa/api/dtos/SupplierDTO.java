package verkkokauppa.api.dtos;

import verkkokauppa.api.entity.Supplier;

public class SupplierDTO {

    private Integer id;
    private String name;
    private String contactName;
    private String phone;
    private String email;

    public SupplierDTO() {
    }

    public SupplierDTO(Supplier supplier) {
        this.id = supplier.getId();
        this.name = supplier.getName();
        this.contactName = supplier.getContactName();
        this.phone = supplier.getPhone();
        this.email = supplier.getEmail();
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
