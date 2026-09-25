package kata.tkachev.configuration;

import kata.tkachev.service.UserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialData {
    @Bean
    public ApplicationRunner initialUsers(UserService users) {
        return args -> users.initializeDefaultUsers();
    }
}
