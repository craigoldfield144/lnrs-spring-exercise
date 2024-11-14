package com.oldfield.lnrs.resource;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Company(
        String companyNumber,
        String companyType,
        String title,
        String companyStatus,
        LocalDate dateOfCreation,
        Address address,
        List<Officers> officers

) {
}





