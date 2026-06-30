package learning.domain.result;

public record Success<T>(T value) implements Result<T> {
}
