package com.gminds.auth_server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OAuth2TokenEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturn200WhenSendingRequestToTokenEndpoint() throws Exception {
        mockMvc.perform(post("/oauth2/token")
                        .param("grant_type", "client_credentials")
                        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("client-id:SekretDoTestowaniaMojejAplikacji!".getBytes())))
                .andExpect(status().isOk());
    }
}
