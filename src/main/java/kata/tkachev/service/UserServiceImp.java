package kata.tkachev.service;

import kata.tkachev.dao.RoleDao;
import kata.tkachev.dao.UserDao;
import kata.tkachev.model.Role;
import kata.tkachev.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImp implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void saveUser(User user, List<Long> roleIds) {
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        assignRoles(user, roleIds);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.saveUser(user);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    @Transactional
    public void updateUser(User user, List<Long> roleIds) {
        User existing = userDao.getUserById(user.getId());
        if (existing == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            user.setPassword(existing.getPassword());
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        assignRoles(user, roleIds);
        userDao.updateUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDao.findAll();
    }

    @Override
    @Transactional
    public void initializeDefaultRolesAndAdmin(String email, String password) {
        Role admin = getOrCreateRole("ROLE_ADMIN");
        Role regular = getOrCreateRole("ROLE_USER");
        if (!email.isBlank() && !password.isBlank() && userDao.getUserByEmail(email) == null) {
            User user = new User();
            user.setName("Administrator");
            user.setAge(0);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRoles(Set.of(admin, regular));
            userDao.saveUser(user);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }
        return user;
    }

    private void assignRoles(User user, List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("Select at least one role");
        }
        Set<Long> uniqueIds = new HashSet<>(roleIds);
        List<Role> selectedRoles = roleDao.findAllById(uniqueIds);
        if (selectedRoles.size() != uniqueIds.size()) {
            throw new IllegalArgumentException("Some roles do not exist");
        }
        user.setRoles(new HashSet<>(selectedRoles));
    }

    private Role getOrCreateRole(String name) {
        Role role = roleDao.findByName(name);
        return role == null ? roleDao.save(new Role(name)) : role;
    }
}
