package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Account;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class AccountService {


    public static final String API_BASE_URL = "http://localhost:8080";
    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Account[] getAllAccounts(int id) {
        Account[] accounts = null;
        try {
            ResponseEntity<Account[]> response =
                    restTemplate.exchange(API_BASE_URL + "/accounts/" + id + "/balance", HttpMethod.GET, makeAuthEntity(), Account[].class);
            accounts = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return accounts;
    }

    public Account getAccount(int id) {
        Account accounts = null;
        try {
            ResponseEntity<Account> response =
                    restTemplate.exchange(API_BASE_URL + "/tenmo/accounts/" + id + "/balance", HttpMethod.GET, makeAuthEntity(), Account.class);
            accounts = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return accounts;

    }

    private HttpEntity<Account> makeAuctionEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(account, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }



}
