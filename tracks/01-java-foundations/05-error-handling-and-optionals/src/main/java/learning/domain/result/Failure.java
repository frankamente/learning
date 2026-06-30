package learning.domain.result;

public record Failure<T>(String errorMessage, Throwable cause) implements Result<T> {
}
