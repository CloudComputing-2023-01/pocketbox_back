package store.pocketbox.app.domain.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    BLOCKED("Blocked");

    private final String statusName;

    UserStatus(String statusName) {
        this.statusName = statusName;
    }
}