public enum UserRoles {
    ADMIN("АДМИН"), USER("ПОЛЬЗОВАТЕЛь");


    UserRoles(String description) {
        this.description = description;
    }

    private final String description;
}
