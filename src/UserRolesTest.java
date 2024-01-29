import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRolesTest {

    @Test
    void adminEnumDescription() {
        assertEquals("АДМИН", UserRoles.ADMIN.getDescription());
    }

    @Test
    void userEnumDescription() {
        assertEquals("ПОЛЬЗОВАТЕЛь", UserRoles.USER.getDescription());
    }
}