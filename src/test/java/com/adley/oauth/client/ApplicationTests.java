package com.adley.oauth.client;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Oauth2ClientApp.class})
public class ApplicationTests {
    @LocalServerPort
    private int port;

    @Autowired
    private AuthorizationServerTokenServices tokenServices;

    @Test
    public void tokenStoreIsJwt() {
        assertTrue("Wrong token store type: " + tokenServices, ReflectionTestUtils.getField(tokenServices,"tokenStore") instanceof JwtTokenStore);
    }

    @Test
    public void tokenKeyEndpointWithSecret() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("username","user3");
        map.add("password","pwd2");
        map.add("grant_type","password");
        map.add("client_id","blogClientId");

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map,headers);

        log.info("result = {}",
            new TestRestTemplate().withBasicAuth("blogClientId","secret")
            .postForEntity("http://localhost:"+port+"/oauth/token", request, String.class));
    }
}
