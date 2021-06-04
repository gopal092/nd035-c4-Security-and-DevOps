package com.example.demo.security;

import com.example.demo.model.persistence.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JWTTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUserAuthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/item")).andExpect(status().isUnauthorized());
    }
    @Test
    public void testNonExistentUser() throws Exception {

        String body = "{\"username\":\"gopal\", \"password\":\"password\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/gopal")
                .content(body))
                .andExpect(status().isUnauthorized()).andReturn();
    }
    @Test
    public void testUserNotFound() throws Exception {
        String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJnb3BhbCIsImV4cCI6MTYyMzM4MjU1MH0.kUQ1LpPD1Iy2b5eF_weWcIVWrutaUDSU_jCdYJEGijT_Oyoyy5xuYtZ5dOtXmMz0OyANt8oq-6b-kOcp0ukPWg";

        Assert.assertNotNull(token);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/testUser").header("Authorization", token)).andExpect(status().isNotFound());
    }
    @Test
    public void testLoginFailed() throws Exception {
        String credentials = "{\"username\":\"gopal\", \"password\": \"password\"}";
        mockMvc.perform(MockMvcRequestBuilders.post(
                "/login")
                .content(credentials)
        ).andExpect(status().isUnauthorized());
    }
    @Test
    public void testLoginSuccessful() throws Exception{
        User user = new User();
        user.setUsername("gopal");
        user.setPassword("password");

        String newUserData = "{\"username\":\"gopal\", \"password\": \"password\", \"confirmPassword\":\"password\"}";
        String credentials = "{\"username\":\"gopal\", \"password\": \"password\"}";
        mockMvc.perform(MockMvcRequestBuilders
                .post(
                "/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserData)
        ).andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post(
                "/login")
                .content(credentials)
        ).andExpect(status().isOk());
    }
}
