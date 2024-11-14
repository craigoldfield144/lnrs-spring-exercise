package com.oldfield.lnrs;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.oldfield.lnrs.resource.CompanySearchRequest;
import com.oldfield.lnrs.resource.CompanySearchResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CompanySearchControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final WireMockServer wireMockServer = new WireMockServer();

    @BeforeAll
    public static void setUp() throws Exception {
        wireMockServer.start();
        configureFor("localhost", 8080);
        stubFor(get(urlEqualTo("/TruProxyAPI/rest/Companies/v1/Search?Query=06500244"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withBody(readFile("company_06500244.json"))));

        stubFor(get(urlEqualTo("/TruProxyAPI/rest/Companies/v1/Search?Query=BBC"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withBody(readFile("company_inactive.json"))));

        stubFor(get(urlEqualTo("/TruProxyAPI/rest/Companies/v1/Officers?CompanyNumber=06500244"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withBody(readFile("officers_06500244.json"))));

        stubFor(get(urlEqualTo("/TruProxyAPI/rest/Companies/v1/Officers?CompanyNumber=111"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withBody(readFile("officers_inactive.json"))));
    }

    @AfterAll
    public static void shutDown() {
        wireMockServer.stop();
    }

    @Test
    void responseShouldBeJson() throws Exception {
        String expected = "{\"total_results\":1,\"items\":[{\"company_number\":\"06500244\",\"company_type\":\"ltd\",\"title\":\"BBC LIMITED\",\"company_status\":\"active\",\"date_of_creation\":\"2008-02-11\",\"address\":{\"locality\":\"Retford\",\"postal_code\":\"DN22 0AD\",\"premises\":\"Boswell Cottage Main Street\",\"address_line_1\":\"North Leverton\",\"country\":\"England\"},\"officers\":[{\"name\":\"BOXALL, Sarah Victoria\",\"officer_role\":\"secretary\",\"appointed_on\":\"2008-02-11\",\"address\":{\"locality\":\"Leeds\",\"postal_code\":\"LS2 7JF\",\"premises\":\"The Leeming Building\",\"address_line_1\":\"Vicar Lane\",\"country\":\"England\"}}]}]}";

        CompanySearchRequest companySearchRequest = new CompanySearchRequest(null, "06500244");

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "a-test-api-key");

        HttpEntity<CompanySearchRequest> request = new HttpEntity<>(companySearchRequest, headers);
        String url = "http://localhost:" + port + "/companysearch";
        ResponseEntity<String> jsonResponse = restTemplate.postForEntity(url, request, String.class);

        assertEquals(expected, jsonResponse.getBody());
    }

    @Test
    void onlyActiveCompaniesAreReturned() throws Exception {

        CompanySearchRequest companySearchRequest = new CompanySearchRequest("BBC", null);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "a-test-api-key");

        HttpEntity<CompanySearchRequest> request = new HttpEntity<>(companySearchRequest, headers);

        String url = "http://localhost:" + port + "/companysearch?activeOnly=true";
        ResponseEntity<String> jsonResponse = restTemplate.postForEntity(url, request, String.class);

        assertNotNull(jsonResponse.getBody());
        assertTrue(jsonResponse.getBody().contains("\"company_number\":\"111\""));
        assertFalse(jsonResponse.getBody().contains("\"company_number\":\"222\""));
    }

    @Test
    void inactiveCompaniesAreReturned() throws Exception {
        String expected = "{\"total_results\":1,\"items\":[{\"company_number\":\"06500244\",\"company_type\":\"ltd\",\"title\":\"BBC LIMITED\",\"company_status\":\"active\",\"date_of_creation\":\"2008-02-11\",\"address\":{\"locality\":\"Retford\",\"postal_code\":\"DN22 0AD\",\"premises\":\"Boswell Cottage Main Street\",\"address_line_1\":\"North Leverton\",\"country\":\"England\"},\"officers\":[{\"name\":\"BOXALL, Sarah Victoria\",\"officer_role\":\"secretary\",\"appointed_on\":\"2008-02-11\",\"address\":{\"locality\":\"Leeds\",\"postal_code\":\"LS2 7JF\",\"premises\":\"The Leeming Building\",\"address_line_1\":\"Vicar Lane\",\"country\":\"England\"}}]}]}";

        CompanySearchRequest companySearchRequest = new CompanySearchRequest("BBC", null);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "a-test-api-key");

        HttpEntity<CompanySearchRequest> request = new HttpEntity<>(companySearchRequest, headers);
        String url = "http://localhost:" + port + "/companysearch?activeOnly=false";
        ResponseEntity<String> jsonResponse = restTemplate.postForEntity(url, request, String.class);

        assertNotNull(jsonResponse.getBody());
        assertTrue(jsonResponse.getBody().contains("\"company_number\":\"111\""));
        assertTrue(jsonResponse.getBody().contains("\"company_number\":\"222\""));
    }

    @Test
    void onlyActiveOfficersAreReturned() throws Exception {

        CompanySearchRequest companySearchRequest = new CompanySearchRequest("BBC", null);

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "a-test-api-key");

        HttpEntity<CompanySearchRequest> request = new HttpEntity<>(companySearchRequest, headers);
        String url = "http://localhost:" + port + "/companysearch?activeOnly=true";
        ResponseEntity<String> jsonResponse = restTemplate.postForEntity(url, request, String.class);

        assertNotNull(jsonResponse.getBody());
        assertTrue(jsonResponse.getBody().contains("\"name\":\"BOXALL, Sarah Victoria\""));
        assertFalse(jsonResponse.getBody().contains("\"name\":\"INACTIVE, I AM\""));
    }

    @Test
    void apiKeyHeaderShouldBeIncluded() throws Exception {
        CompanySearchRequest companySearchRequest = new CompanySearchRequest(null, "06500244");

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "a-test-api-key");

        HttpEntity<CompanySearchRequest> request = new HttpEntity<>(companySearchRequest, headers);
        String url = "http://localhost:" + port + "/companysearch";
        restTemplate.postForObject(url, request, CompanySearchResponse.class);
        verify(getRequestedFor(urlEqualTo("/TruProxyAPI/rest/Companies/v1/Search?Query=06500244"))
                .withHeader("x-api-key", equalTo("a-test-api-key")));
        verify(getRequestedFor(urlEqualTo("/TruProxyAPI/rest/Companies/v1/Officers?CompanyNumber=06500244"))
                .withHeader("x-api-key", equalTo("a-test-api-key")));
    }

    @Test
    void companyNumberIsUsedIfSuppliedWithName() throws Exception {

        CompanySearchRequest companySearchRequest = new CompanySearchRequest("ITV", "06500244");

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", "a-test-api-key");

        HttpEntity<CompanySearchRequest> request = new HttpEntity<>(companySearchRequest, headers);
        String url = "http://localhost:" + port + "/companysearch";
        ResponseEntity<String> jsonResponse = restTemplate.postForEntity(url, request, String.class);

        assertNotNull(jsonResponse.getBody());
        assertTrue(jsonResponse.getBody().contains("\"company_number\":\"06500244\""));
    }

    private static String readFile(String filePath) throws IOException {
        File resource = new ClassPathResource(filePath).getFile();
        byte[] byteArray = Files.readAllBytes(resource.toPath());
        return new String(byteArray);
    }

}