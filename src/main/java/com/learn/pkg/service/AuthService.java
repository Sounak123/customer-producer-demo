package com.learn.pkg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.learn.pkg.exception.InvalidAuthenticationException;

@Service
public class AuthService {
  @Autowired private AuthenticationManager authenticationManager;

  public void authenticate(String username, String password) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new InvalidAuthenticationException("USER_DISABLED: please contact administrator");
    } catch (BadCredentialsException e) {
      throw new InvalidAuthenticationException("INVALID_CREDENTIALS");
    }
  }
}
