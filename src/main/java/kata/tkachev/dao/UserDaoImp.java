package kata.tkachev.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kata.tkachev.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public void saveUser(User user) {
        manager.persist(user);
    }

    @Override
    public User getUserById(Long id) {
        return manager.createQuery(
                        "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = :id", User.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateUser(User user) {
        manager.merge(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = manager.find(User.class, id);
        if (user != null) {
            manager.remove(user);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return manager.createQuery(
                "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles", User.class).getResultList();
    }

    @Override
    public User getUserByEmail(String email) {
        return manager.createQuery(
                        "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream().findFirst().orElse(null);
    }
}
