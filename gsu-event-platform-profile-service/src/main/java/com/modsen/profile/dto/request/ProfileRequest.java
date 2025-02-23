package com.modsen.profile.dto.request;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Alexander Dudkin
 */
public record ProfileRequest(
        String email,
        String firstname,
        String lastname,
        LocalDate birthdate,
        Character gender,
        String bio
) implements Serializable { }
