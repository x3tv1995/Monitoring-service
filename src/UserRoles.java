/**
 * Перечисление UserRoles представляет роли пользователей в системе.
 * Каждая роль ассоциирована с описанием на русском языке.
 */
public enum UserRoles {
    ADMIN("АДМИН"),
    USER("ПОЛЬЗОВАТЕЛЬ");

    private String description;

    /**
     * Конструктор принимает описание роли на русском языке и инициализирует поле description.
     *
     * @param description Описание роли на русском языке.
     */
    UserRoles(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}