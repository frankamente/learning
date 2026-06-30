package learning.domain;

public record Customer(String id, String name, String email, String address) {
    public Customer {
        java.util.Objects.requireNonNull(id, "ID cannot be null");
        java.util.Objects.requireNonNull(name, "Name cannot be null");
        java.util.Objects.requireNonNull(email, "Email cannot be null");
        java.util.Objects.requireNonNull(address, "Address cannot be null");
    }

    public boolean hasValidEmail() {
        return email.contains("@") && email.contains(".");
    }
}
