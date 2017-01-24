package info.donggan;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Date: 16/7/19 Time: AM11:59
 *
 * @author gan
 */
public class FooUsernamePasswordAuthenticationToken extends
    UsernamePasswordAuthenticationToken {

  public FooUsernamePasswordAuthenticationToken(Object principal,
      Object credentials) {
    super(principal, credentials);
  }

  public FooUsernamePasswordAuthenticationToken(Object principal,
      Object credentials,
      Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
  }

}
