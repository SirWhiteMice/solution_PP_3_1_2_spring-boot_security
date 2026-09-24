package kata.tkachev.dao;

import kata.tkachev.model.Role;
import java.util.Collection;
import java.util.List;

public interface RoleDao {
    Role save(Role role);
    Role findByName(String name);
    List<Role> findAll();
    List<Role> findAllById(Collection<Long> ids);
}
