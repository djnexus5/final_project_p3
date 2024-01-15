package org.example;

import java.time.LocalDate;

public interface Expirable {
    boolean isExpired();
    LocalDate getExpiryDate();
}
