package kata.tkachev.configuration;

import kata.tkachev.controller.UserController;
import kata.tkachev.model.Role;
import kata.tkachev.model.User;
import kata.tkachev.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(UserController.class)
@Import({AppSecurityConfig.class, LoginSuccessHandler.class})
class SecurityAccessTest {
    @Autowired MockMvc mvc;
    @MockBean UserService userService;

    private User account(String authority) {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("encoded");
        user.setRoles(Set.of(new Role(authority)));
        return user;
    }

    @Test
    void anonymousIsSentToLogin() throws Exception {
        mvc.perform(get("/admin")).andExpect(status().is3xxRedirection());
    }

    @Test
    void userCannotOpenAdminPage() throws Exception {
        mvc.perform(get("/admin").with(user(account("ROLE_USER"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanOpenCrudPage() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());
        mvc.perform(get("/admin").with(user(account("ROLE_ADMIN"))))
                .andExpect(status().isOk()).andExpect(view().name("users"));
    }

    @Test
    void userCanOpenOwnPage() throws Exception {
        User account = account("ROLE_USER");
        when(userService.getUserById(anyLong())).thenReturn(account);
        mvc.perform(get("/user").with(user(account)))
                .andExpect(status().isOk()).andExpect(view().name("user"));
    }

    @Test
    void userCanOpenOwnPageWithBothRoles() throws Exception {
        User account = account("ROLE_USER");
        account.setRoles(Set.of(new Role("ROLE_USER"), new Role("ROLE_ADMIN")));
        when(userService.getUserById(anyLong())).thenReturn(account);
        mvc.perform(get("/user").with(user(account)))
                .andExpect(status().isOk()).andExpect(view().name("user"));
    }

    @Test
    void loginSuccessGoesToAdminWhenUserHasBothRoles() throws Exception {
        LoginSuccessHandler handler = new LoginSuccessHandler();
        org.springframework.mock.web.MockHttpServletRequest request = new org.springframework.mock.web.MockHttpServletRequest();
        org.springframework.mock.web.MockHttpServletResponse response = new org.springframework.mock.web.MockHttpServletResponse();
        User account = account("ROLE_USER");
        account.setRoles(Set.of(new Role("ROLE_USER"), new Role("ROLE_ADMIN")));
        handler.onAuthenticationSuccess(request, response,
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        account, null, account.getAuthorities()));
        org.junit.jupiter.api.Assertions.assertEquals("/admin", response.getRedirectedUrl());
    }
}
