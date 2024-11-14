package com.oldfield.lnrs.api;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.oldfield.lnrs.resource.Address;

import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TruProxyCompany(
        String companyStatus,
        LocalDate dateOfCreation,
        String companyNumber,
        String title,
        String companyType,
        Address address
) {
}
