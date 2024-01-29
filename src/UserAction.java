import java.io.Serializable;
import java.util.logging.Logger;

public enum UserAction implements Serializable {

    REGISTER("REGISTER"),
    NULL("null"),
    ADD_NEW_POKAZANIYA("добавил показания"),
    DROP_PROFILE("удалил профиль"),

    User_registered("Пользователь зарегестрирован");
    private static final Logger LOGGER = Logger.getLogger(UserAction.class.getName());


    private final String description;

    UserAction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static UserAction getByDescription(String description) {
        for (UserAction action : values()) {
            if (action.description.equals(description)) {
                return action;
            }
        }
        return NULL;
    }
    public void log(User user) {
        LOGGER.info("User " + user.getUsername() + " " + getDescription());
    }
}
