package learning.domain.result;

public sealed interface Result<T> permits Success, Failure {
}
