package com.modsen.profile.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * @author Alexander Dudkin
 */
public record Profile(
        UUID id,
        UUID userId,
        String email,
        String firstname,
        String lastname,
        LocalDate birthdate,
        Character gender,
        String bio
) implements Serializable {

    public Profile withUserId(UUID userId) {
        return this.userId == userId
                ? this
                : new Profile(this.id, userId, this.email, this.firstname, this.lastname, this.birthdate, this.gender, this.bio);
    }

}
