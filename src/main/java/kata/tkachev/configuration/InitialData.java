package kata.tkachev.configuration;

import kata.tkachev.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialData {
    @Bean
    public ApplicationRunner initialUsers(UserService users,
                                          @Value("${app.bootstrap-admin.email:}") String email,
                                          @Value("${app.bootstrap-admin.password:}") String password) {
        return args -> users.initializeDefaultRolesAndAdmin(email, password);
    }
}
