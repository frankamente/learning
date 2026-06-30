package learning;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class VerifyTest {
    @Test
    void shouldRunOnJava25() {
        String javaVersion = System.getProperty("java.version");
        System.out.println("Running on Java version: " + javaVersion);
        assertThat(javaVersion).startsWith("25");
    }
}
