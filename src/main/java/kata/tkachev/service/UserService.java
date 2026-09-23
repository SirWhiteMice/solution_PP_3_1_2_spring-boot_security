package kata.tkachev.service;

import kata.tkachev.model.User;

import java.util.List;

public interface UserService {
    void saveUser(User user);
    User getUserById(Long id);
    void updateUser(User user);
    void deleteUser(Long id);
    List<User> getAllUsers();
}
