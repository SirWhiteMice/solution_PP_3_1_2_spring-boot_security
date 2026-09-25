package kata.tkachev.service;

import kata.tkachev.model.Role;
import kata.tkachev.model.User;

import java.util.List;

public interface UserService {
    void saveUser(User user, List<Long> roleIds);
    User getUserById(Long id);
    void updateUser(User user, List<Long> roleIds);
    void deleteUser(Long id);
    List<User> getAllUsers();
    List<Role> getAllRoles();
    void initializeDefaultRolesAndAdmin(String email, String password);
}
