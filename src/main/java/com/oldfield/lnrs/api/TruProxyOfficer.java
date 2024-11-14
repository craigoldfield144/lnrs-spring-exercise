package com.oldfield.lnrs.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oldfield.lnrs.resource.Address;

import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TruProxyOfficer(
        String name,
        String officerRole,
        LocalDate appointedOn,
        LocalDate resignedOn,
        Address address
) {
}
