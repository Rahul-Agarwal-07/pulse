package com.rahul.pulse.auth.domain.model;

import com.rahul.pulse.auth.domain.exception.InvalidPasswordHashException;

import java.util.Objects;
import java.util.UUID;

public record RefreshTokenId(
        UUID id
) {

   public RefreshTokenId {
       Objects.requireNonNull(
               id,
               "Refresh Token Id cannot be null"
       );
   }


}
