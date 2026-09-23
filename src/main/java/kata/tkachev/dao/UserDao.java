package kata.tkachev.dao;

import kata.tkachev.model.User;

import java.util.List;

public interface UserDao {
    void saveUser(User user);
    User getUserById(Long id);
    void updateUser(User user);
    void deleteUser(Long id);
    List<User> getAllUsers();
    User getUserByEmail(String email);
}
