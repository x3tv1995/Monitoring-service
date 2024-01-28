import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {
    private UserManager userManager;

    @BeforeEach
    void setUp() {
        userManager = new UserManager();
    }

    @Test
    void registerUser_ValidUser_Success() {
        User user = new User("testUser", "password", UserRoles.USER);
        userManager.registerUser(user);

        List<User> users = userManager.getUsers();
        assertTrue(users.contains(user));
    }

    @Test
    void isUserExists_ExistingUser_ReturnsTrue() {
        User existingUser = new User("existingUser", "password", UserRoles.USER);
        userManager.registerUser(existingUser);

        assertTrue(userManager.isUserExists("existingUser"));
    }

    @Test
    void isUserExists_NonExistingUser_ReturnsFalse() {
        assertFalse(userManager.isUserExists("nonExistingUser"));
    }
}