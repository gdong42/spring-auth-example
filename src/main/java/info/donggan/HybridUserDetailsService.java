package info.donggan;

import info.donggan.model.Response;
import info.donggan.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a workaround to Spring Security OAuth2 issue:
 * https://github.com/spring-projects/spring-security-oauth/issues/685
 * <p/>
 * It queries the underlying authentication service based on its client_id.
 * <p/>
 * Date: 16/8/2 Time: PM1:42
 *
 * @author Gan Dong
 */
@Service
public class HybridUserDetailsService implements UserDetailsService {

  private final Log logger = LogFactory.getLog(HybridUserDetailsService.class);

  @Autowired
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    if (username == null || username.isEmpty()) {
      throw new UsernameNotFoundException("Username is empty");
    }

    Authentication authentication = SecurityContextHolder.getContext()
        .getAuthentication();

    Object clientPrincipal = authentication.getPrincipal();

    if (clientPrincipal instanceof User) {
      if ("foo_app".equalsIgnoreCase(((User) clientPrincipal).getUsername())) {
        // foo client
        UserDetails user = loadFooUserDetails(username);
        if (user != null)
          return user;
      } else if ("bar_app".equalsIgnoreCase(((User) clientPrincipal).getUsername())) {
        // bar client
        UserDetails user = loadBarUserDetails(username);
        if (user != null)
          return user;
      }
    }

    throw new UsernameNotFoundException(
        "Unauthorized client_id or username not found: " + username);
  }

  private UserDetails loadFooUserDetails(String username) {
    Response response = userService.loadFooUser(username);
    if (logger.isDebugEnabled())
      logger.debug("Loaded from foo details: " + response);
    if (response.isOk()) {
      List<GrantedAuthority> authorities = new ArrayList<>();
      authorities.add(new SimpleGrantedAuthority("FOO_READ"));
      authorities.add(new SimpleGrantedAuthority("FOO_WRITE"));
      return new User(username, "", authorities);
    }
    return null;
  }

  private UserDetails loadBarUserDetails(String username) {
    Response response = userService.loadBarUser(username);
    if (logger.isDebugEnabled())
      logger.debug("Loaded from bar details: " + response);
    if (response.isOk()) {
      List<GrantedAuthority> authorities = new ArrayList<>();

      authorities.add(new SimpleGrantedAuthority("BAR_READ"));
      authorities.add(new SimpleGrantedAuthority("BAR_WRITE"));
      return new User(username, "", authorities);
    }
    return null;
  }
}
