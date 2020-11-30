package com.learn.pkg.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.pkg.exception.CustomerControllerAdvice;
import com.learn.pkg.model.JwtRequest;
import com.learn.pkg.service.AuthService;
import com.learn.pkg.service.PKUserDetailsService;
import com.learn.pkg.util.JwtTokenUtil;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
  private static final Logger logger = LoggerFactory.getLogger(AuthControllerTest.class);

  @InjectMocks private AuthController authController = new AuthController();

  @Mock private AuthenticationManager authenticationManager;

  private MockMvc mockMvc;
  private JwtTokenUtil jwtTokenUtil;
  private AuthService authService;

  @BeforeEach
  public void init() {
    jwtTokenUtil = new JwtTokenUtil();
    authService = new AuthService();
    ReflectionTestUtils.setField(jwtTokenUtil, "secret", "pkglobal");
    ReflectionTestUtils.setField(authService, "authenticationManager", authenticationManager);

    ReflectionTestUtils.setField(authController, "userDetailsService", new PKUserDetailsService());
    ReflectionTestUtils.setField(authController, "authService", authService);
    ReflectionTestUtils.setField(authController, "jwtTokenUtil", jwtTokenUtil);

    mockMvc =
        MockMvcBuilders.standaloneSetup(authController)
            .setControllerAdvice(new CustomerControllerAdvice())
            .build();
  }

  @Test
  public void testCreateAuthenticationToken() throws Exception {
    MvcResult result =
        mockMvc
            .perform(
                post("/authenticate")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(getAuthData()))
            .andExpect(MockMvcResultMatchers.status().is(200))
            .andReturn();
    logger.info("Result is:", result.getResponse().getContentAsString());
    assertNotNull(result.getResponse());
  }

  @Test
  public void testCreateAuthenticationTokenFailure() throws Exception {
    Mockito.when(authenticationManager.authenticate(Mockito.any()))
        .thenThrow(BadCredentialsException.class);

    MvcResult result =
        mockMvc
            .perform(
                post("/authenticate")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(getAuthData()))
            .andExpect(MockMvcResultMatchers.status().is(401))
            .andReturn();
    logger.info("The result is:", result.getResponse().getContentAsString());
    assertNotNull(result.getResponse());
  }

  private String getAuthData() throws JsonProcessingException {
    JwtRequest request = new JwtRequest();
    request.setUsername("pkuser");
    request.setPassword("password");

    return mapToJson(request);
  }

  protected String mapToJson(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }
}
