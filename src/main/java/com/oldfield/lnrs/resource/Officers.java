package com.oldfield.lnrs.resource;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Officers(
        String name,
        String officerRole,
        LocalDate appointedOn,
        Address address
) {
}

