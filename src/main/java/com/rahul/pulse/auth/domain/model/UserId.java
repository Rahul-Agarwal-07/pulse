package com.rahul.pulse.auth.domain.model;
import java.util.UUID;

public class UserId {

    private final UUID value;

    public UserId(UUID value) {
        this.value = value;
    }

    public UUID value() {
        return value;
    }
}