package com.oldfield.lnrs;

import com.oldfield.lnrs.resource.CompanySearchRequest;
import com.oldfield.lnrs.resource.CompanySearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.oldfield.lnrs.LnrsSpringExerciseApplication.API_KEY_HEADER;

@RestController
public class CompanySearchController {

    @Autowired
    CompanySearchService companySearchService;

    @PostMapping("/companysearch")
    public CompanySearchResponse companySearch(@RequestBody CompanySearchRequest request,
                                               @RequestParam(value = "activeOnly", defaultValue = "false") boolean activeOnly,
                                               @RequestHeader(API_KEY_HEADER) String apiKey) {
        return companySearchService.search(request, activeOnly, apiKey);
    }
}
