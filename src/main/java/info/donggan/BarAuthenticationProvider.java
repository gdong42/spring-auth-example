package info.donggan;

import info.donggan.model.Response;
import info.donggan.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Authentication Provider for 'bar'
 *
 * Created by gdong on 2017/1/24.
 */
@Component
public class BarAuthenticationProvider implements AuthenticationProvider {
  private final Logger logger = LoggerFactory
      .getLogger(BarAuthenticationProvider.class);

  @Autowired
  private UserService userService;

  @Override
  public Authentication authenticate(Authentication authentication)
      throws AuthenticationException {
    logger.debug(
        "==== Authenticating using BarAuthenticationProvider: " +
            authentication);

    // here goes username/password authentication for Foo
    Response response = userService
        .authenticateBar(String.valueOf(authentication.getPrincipal()),
            String.valueOf(authentication.getCredentials()));

    if (response.isOk()) {
      List<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority("BAR_READ"));
      authorities.add(new SimpleGrantedAuthority("BAR_WRITE"));
      return new BarUsernamePasswordAuthenticationToken(
          authentication.getPrincipal(), authentication.getCredentials(),
          authorities);
    } else {
      throw new BadCredentialsException("Authentication failed.");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    logger.info("Checking if bar authentication is applicable");
    return BarUsernamePasswordAuthenticationToken.class
        .isAssignableFrom(authentication);
  }
}
