package info.donggan;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Date: 16/7/19 Time: AM11:59
 *
 * @author gan
 */
public class BarUsernamePasswordAuthenticationToken extends
    UsernamePasswordAuthenticationToken {

  public BarUsernamePasswordAuthenticationToken(Object principal,
      Object credentials) {
    super(principal, credentials);
  }

  public BarUsernamePasswordAuthenticationToken(Object principal,
      Object credentials,
      Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
  }

}
