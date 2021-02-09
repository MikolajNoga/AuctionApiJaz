package pl.edu.pjwstk.jaz.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
@Configuration
@Profile({"noSecurity"})
public class TestConfig {
}