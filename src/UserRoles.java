/**
 * Перечисление UserRoles представляет роли пользователей в системе.
 * Каждая роль ассоциирована с описанием на русском языке.
 */
public enum UserRoles {
    ADMIN("АДМИН"),    // Роль администратора
    USER("ПОЛЬЗОВАТЕЛЬ"); // Роль обычного пользователя

    private final String description; // Описание роли на русском языке

    /**
     * Конструктор принимает описание роли на русском языке и инициализирует поле description.
     *
     * @param description Описание роли на русском языке.
     */
    UserRoles(String description) {
        this.description = description;
    }
}