package kata.tkachev.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kata.tkachev.model.Role;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class пшеRoleDaoImp implements RoleDao {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Role save(Role role) {
        manager.persist(role);
        return role;
    }

    @Override
    public Role findByName(String name) {
        return manager.createQuery("FROM Role r WHERE r.name = :name", Role.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Role> findAll() {
        return manager.createQuery("FROM Role", Role.class).getResultList();
    }

    @Override
    public List<Role> findAllById(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return manager.createQuery("FROM Role r WHERE r.id IN :ids", Role.class)
                .setParameter("ids", ids)
                .getResultList();
    }
}
