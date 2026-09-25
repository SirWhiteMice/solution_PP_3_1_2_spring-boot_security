package kata.tkachev.model;

import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ModelMappingTest {
    @Test
    void rolesAreLoadedLazily() throws NoSuchFieldException {
        ManyToMany mapping = User.class.getDeclaredField("roles").getAnnotation(ManyToMany.class);

        assertEquals(FetchType.LAZY, mapping.fetch());
    }

    @Test
    void userTableDoesNotUseReservedWord() {
        assertEquals("users", User.class.getAnnotation(Table.class).name());
    }

    @Test
    void equalRolesDoNotDuplicateInSet() {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_USER"));
        roles.add(new Role("ROLE_USER"));

        assertEquals(1, roles.size());
    }
}
