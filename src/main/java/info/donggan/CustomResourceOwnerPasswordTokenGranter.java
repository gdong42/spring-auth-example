package info.donggan;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Date: 16/7/19 Time: AM12:35
 *
 * @author gan
 */
public class CustomResourceOwnerPasswordTokenGranter extends
    AbstractTokenGranter {

  private static final String GRANT_TYPE = "password";

  private final AuthenticationManager authenticationManager;

  public CustomResourceOwnerPasswordTokenGranter(
      AuthenticationManager authenticationManager,
      AuthorizationServerTokenServices tokenServices,
      ClientDetailsService clientDetailsService,
      OAuth2RequestFactory requestFactory) {
    this(authenticationManager, tokenServices, clientDetailsService,
        requestFactory, GRANT_TYPE);
  }

  protected CustomResourceOwnerPasswordTokenGranter(
      AuthenticationManager authenticationManager,
      AuthorizationServerTokenServices tokenServices,
      ClientDetailsService clientDetailsService,
      OAuth2RequestFactory requestFactory, String grantType) {
    super(tokenServices, clientDetailsService, requestFactory, grantType);
    this.authenticationManager = authenticationManager;
  }

  @Override
  protected OAuth2Authentication getOAuth2Authentication(ClientDetails client,
      TokenRequest tokenRequest) {

    Map<String, String> parameters = new LinkedHashMap<String, String>(
        tokenRequest.getRequestParameters());
    String username = parameters.get("username");
    String password = parameters.get("password");
    String clientId = client.getClientId();
    // Protect from downstream leaks of password
    parameters.remove("password");

    Authentication userAuth;
    if ("foo_app".equalsIgnoreCase(clientId)) {
      userAuth = new FooUsernamePasswordAuthenticationToken(username,
          password);
    } else if ("bar_app".equalsIgnoreCase(clientId)) {
      userAuth = new BarUsernamePasswordAuthenticationToken(username,
          password);
    } else {
      throw new InvalidGrantException("Unknown client: " + clientId);
    }

    ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
    try {
      userAuth = authenticationManager.authenticate(userAuth);
    } catch (AccountStatusException ase) {
      //covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
      throw new InvalidGrantException(ase.getMessage());
    } catch (BadCredentialsException e) {
      // If the username/password are wrong the spec says we should send 400/invalid grant
      throw new InvalidGrantException(e.getMessage());
    }
    if (userAuth == null || !userAuth.isAuthenticated()) {
      throw new InvalidGrantException(
          "Could not authenticate user: " + username);
    }

    OAuth2Request storedOAuth2Request = getRequestFactory()
        .createOAuth2Request(client, tokenRequest);
    return new OAuth2Authentication(storedOAuth2Request, userAuth);
  }
}
