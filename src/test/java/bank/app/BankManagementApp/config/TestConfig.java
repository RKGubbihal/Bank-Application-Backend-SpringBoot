package bank.app.BankManagementApp.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
public class TestConfig {
    
    // This configuration class can be used to define test-specific beans
    // and configurations if needed in the future
    
}
