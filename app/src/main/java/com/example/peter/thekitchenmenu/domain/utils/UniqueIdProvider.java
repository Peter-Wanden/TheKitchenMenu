package com.example.peter.thekitchenmenu.domain.utils;

import java.util.UUID;

public class UniqueIdProvider {
    public String getUId() {
        return String.valueOf(UUID.randomUUID());
    }
}
