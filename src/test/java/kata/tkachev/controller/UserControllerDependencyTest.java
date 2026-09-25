package kata.tkachev.controller;

import kata.tkachev.service.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class UserControllerDependencyTest {
    @Test
    void controllerDependsOnlyOnUserService() {
        assertArrayEquals(new Class<?>[]{UserService.class},
                UserController.class.getConstructors()[0].getParameterTypes());
    }
}
