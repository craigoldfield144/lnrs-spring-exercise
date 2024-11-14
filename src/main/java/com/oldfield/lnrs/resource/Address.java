package com.oldfield.lnrs.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonPropertyOrder({"locality", "postal_code", "premises", "address_line_1", "country"})
public record Address(
        String locality,
        String postalCode,
        String premises,
        @JsonProperty("address_line_1") String addressLine1,
        String country
) {
}
