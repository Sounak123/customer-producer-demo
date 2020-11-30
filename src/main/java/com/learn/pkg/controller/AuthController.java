package com.learn.pkg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.learn.pkg.model.JwtRequest;
import com.learn.pkg.service.AuthService;
import com.learn.pkg.service.PKUserDetailsService;
import com.learn.pkg.util.JwtTokenUtil;

@RestController
public class AuthController {

  @Autowired private JwtTokenUtil jwtTokenUtil;

  @Autowired private PKUserDetailsService userDetailsService;

  @Autowired private AuthService authservice;

  @PostMapping(value = "/authenticate")
  public ResponseEntity<String> createAuthenticationToken(
      @RequestBody JwtRequest authenticationRequest) {

    authservice.authenticate(
        authenticationRequest.getUsername(), authenticationRequest.getPassword());

    final UserDetails userDetails =
        userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

    final String token = jwtTokenUtil.generateToken(userDetails);

    return ResponseEntity.ok(token);
  }
}
