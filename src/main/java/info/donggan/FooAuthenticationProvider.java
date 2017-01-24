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
 * Authentication Provider for 'foo'
 *
 * Created by gdong on 2017/1/24.
 */
@Component
public class FooAuthenticationProvider implements AuthenticationProvider {
  private final Logger logger = LoggerFactory
      .getLogger(FooAuthenticationProvider.class);

  @Autowired
  private UserService userService;

  @Override
  public Authentication authenticate(Authentication authentication)
      throws AuthenticationException {
    logger.debug(
        "==== Authenticating using FooAuthenticationProvider: " +
            authentication);

    // here goes username/password authentication for Foo
    Response response = userService
        .authenticateFoo(String.valueOf(authentication.getPrincipal()),
            String.valueOf(authentication.getCredentials()));

    if (response.isOk()) {
      List<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority("FOO_READ"));
      authorities.add(new SimpleGrantedAuthority("FOO_WRITE"));
      return new FooUsernamePasswordAuthenticationToken(
          authentication.getPrincipal(), authentication.getCredentials(),
          authorities);
    } else {
      throw new BadCredentialsException("Authentication failed.");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    logger.info("Checking if foo authentication is applicable");
    return FooUsernamePasswordAuthenticationToken.class
        .isAssignableFrom(authentication);
  }
}
