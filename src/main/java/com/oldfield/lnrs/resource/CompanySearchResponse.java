package com.oldfield.lnrs.resource;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CompanySearchResponse(
        int totalResults,
        List<Company> items
) {
}