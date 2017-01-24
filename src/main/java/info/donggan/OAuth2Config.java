package info.donggan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gdong on 16/9/1.
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

  @Autowired
  @Qualifier("authenticationManagerBean")
  private AuthenticationManager authenticationManager;

  @Autowired
  private HybridUserDetailsService userDetailsService;

  @Override
  public void configure(ClientDetailsServiceConfigurer clients)
      throws Exception {
    clients.inMemory()
        .withClient("foo_app")
        .autoApprove(true)
        .authorities("FOO_READ", "FOO_WRITE")
        .authorizedGrantTypes("authorization_code", "refresh_token", "implicit",
            "password", "client_credentials")
        .scopes("FOO")
        .and()
        .withClient("bar_app")
        .autoApprove(true)
        .authorities("BAR_READ", "BAR_WRITE")
        .authorizedGrantTypes("authorization_code", "refresh_token", "implicit",
            "password", "client_credentials")
        .scopes("BAR");
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints)
      throws Exception {
    endpoints.tokenStore(tokenStore())
        .tokenEnhancer(jwtTokenEnhancer())
        .authenticationManager(authenticationManager)
        .userDetailsService(userDetailsService);

    List<TokenGranter> tokenGranters = new ArrayList<>();
    tokenGranters
        .add(new CustomResourceOwnerPasswordTokenGranter(authenticationManager,
            endpoints.getTokenServices(), endpoints.getClientDetailsService(),
            endpoints.getOAuth2RequestFactory()));
    tokenGranters.add(new RefreshTokenGranter(endpoints.getTokenServices(),
        endpoints.getClientDetailsService(),
        endpoints.getOAuth2RequestFactory()));
    endpoints.tokenGranter(new CompositeTokenGranter(tokenGranters));
  }

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(jwtTokenEnhancer());
  }

  @Bean
  protected JwtAccessTokenConverter jwtTokenEnhancer() {
    KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
        new ClassPathResource("jwt.jks"), "mySecretKey".toCharArray());
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setKeyPair(keyStoreKeyFactory.getKeyPair("jwt"));
    return converter;
  }
}
