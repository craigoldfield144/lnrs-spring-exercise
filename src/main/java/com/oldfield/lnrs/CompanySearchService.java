package com.oldfield.lnrs;

import com.oldfield.lnrs.api.CompaniesResponse;
import com.oldfield.lnrs.api.OfficersResponse;
import com.oldfield.lnrs.api.TruProxyCompany;
import com.oldfield.lnrs.api.TruProxyOfficer;
import com.oldfield.lnrs.resource.Company;
import com.oldfield.lnrs.resource.CompanySearchRequest;
import com.oldfield.lnrs.resource.CompanySearchResponse;
import com.oldfield.lnrs.resource.Officers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.oldfield.lnrs.LnrsSpringExerciseApplication.API_KEY_HEADER;

@Service
public class CompanySearchService {

    private final RestClient restClient;

    public CompanySearchService(@Value("${tru.proxy.api.base.url}") String truProxyApiBaseUrl,
                                RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(truProxyApiBaseUrl).build();
    }

    public CompanySearchResponse search(CompanySearchRequest request, boolean activeOnly, String apiKey) {

        List<Company> companies = new ArrayList<>();

        String query = request.companyNumber() != null ? request.companyNumber() : request.companyName();

        CompaniesResponse companiesResponse = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/Companies/v1/Search").queryParam("Query", query).build())
                .header(API_KEY_HEADER, apiKey)
                .retrieve().body(CompaniesResponse.class);

        for (TruProxyCompany truProxyCompany : companiesResponse.items()) {

            if (!activeOnly || "active".equals(truProxyCompany.companyStatus())) {
                List<Officers> officers = getOfficers(truProxyCompany.companyNumber(), apiKey);

                companies.add(new Company(
                        truProxyCompany.companyNumber(),
                        truProxyCompany.companyType(),
                        truProxyCompany.title(),
                        truProxyCompany.companyStatus(),
                        truProxyCompany.dateOfCreation(),
                        truProxyCompany.address(),
                        officers
                ));
            }
        }

        return new CompanySearchResponse(companies.size(), companies);
    }

    private List<Officers> getOfficers(final String companyNumber, final String apiKey) {
        try {
            List<Officers> officers = new ArrayList<>();
            OfficersResponse officersResponse = restClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/Companies/v1/Officers").queryParam("CompanyNumber", companyNumber).build())
                    .header(API_KEY_HEADER, apiKey)
                    .retrieve().body(OfficersResponse.class);

            for (TruProxyOfficer truProxyOfficer : officersResponse.items()) {
                if (truProxyOfficer.resignedOn() == null) {
                    officers.add(new Officers(
                            truProxyOfficer.name(),
                            truProxyOfficer.officerRole(),
                            truProxyOfficer.appointedOn(),
                            truProxyOfficer.address()));
                }
            }
            return officers;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
