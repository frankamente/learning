package learning.domain;

public final class CustomerBuilder {
    private String id = "c1";
    private String name = "John Doe";
    private String email = "john.doe@example.com";
    private String address = "123 Main St, Springfield";

    private CustomerBuilder() {}

    public static CustomerBuilder aCustomer() {
        return new CustomerBuilder();
    }

    public CustomerBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public CustomerBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CustomerBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public CustomerBuilder withAddress(String address) {
        this.address = address;
        return this;
    }

    public Customer build() {
        return new Customer(id, name, email, address);
    }
}
