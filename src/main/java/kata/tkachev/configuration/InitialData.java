package kata.tkachev.configuration;

import kata.tkachev.dao.RoleRepository;
import kata.tkachev.model.Role;
import kata.tkachev.model.User;
import kata.tkachev.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class InitialData {
    @Bean
    public ApplicationRunner initialUsers(RoleRepository roles, UserService users,
                                          @Value("${app.bootstrap-admin.email:}") String email,
                                          @Value("${app.bootstrap-admin.password:}") String password) {
        return args -> {
            Role admin = roles.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roles.save(new Role("ROLE_ADMIN")));
            Role regular = roles.findByName("ROLE_USER")
                    .orElseGet(() -> roles.save(new Role("ROLE_USER")));
            if (!email.isBlank() && !password.isBlank()
                    && users.getAllUsers().stream().noneMatch(user -> email.equals(user.getEmail()))) {
                User user = new User();
                user.setName("Administrator");
                user.setAge(0);
                user.setEmail(email);
                user.setPassword(password);
                user.setRoles(Set.of(admin, regular));
                users.saveUser(user);
            }
        };
    }
}
