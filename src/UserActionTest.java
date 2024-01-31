import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserActionTest {

    @Test
    void testUserRegisteredLogging() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        UserAction action = UserAction.User_registered;
        User user = new User("testUser", "password", UserRoles.USER);

        action.log(user);

        assertEquals("User testUser Пользователь зарегестрирован\n", outContent.toString());


        System.setOut(System.out);
    }
}