import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private UserManager userManager;

    @BeforeEach
    void setUp() {
        userManager = new UserManager();
    }

    @Test
    void testUserCreation() {
        User user = new User("testUser", "testPassword", UserRoles.USER);

        assertEquals("testUser", user.getUsername());
        assertEquals("testPassword", user.getPassword());
        assertEquals(UserRoles.USER, user.getRole());
        assertNotNull(user.getSubmittedReadings());
    }

    @Test
    void testInputUserAuthorization() throws IOException {
        String input = "testUser\n" + "testPassword\n";
        BufferedReader reader = new BufferedReader(new StringReader(input));

        userManager.registerUser(new User("testUser", "testPassword", UserRoles.USER));

        User authorizedUser = User.inputUserAuthorization(reader, userManager);

        assertNotNull(authorizedUser);
        assertEquals("testUser", authorizedUser.getUsername());
    }

    @Test
    void testInputUserRegistration() throws IOException {
        String input = "newUser\n" + "newPassword\n";
        BufferedReader reader = new BufferedReader(new StringReader(input));

        assertNull(User.inputUserRegistration(reader, userManager));

        userManager.registerUser(new User("existingUser", "existingPassword", UserRoles.USER));

        User newUser = User.inputUserRegistration(reader, userManager);

        assertNotNull(newUser);
        assertEquals("newUser", newUser.getUsername());
    }

    @Test
    void testHasSubmittedReadingForMonth() {
        User user = new User("testUser", "testPassword", UserRoles.USER);
        user.addSubmittedReading(new Reading(1, 1, 0.0, 0.0));

        assertTrue(user.hasSubmittedReadingForMonth(1));
        assertFalse(user.hasSubmittedReadingForMonth(2));
    }
}