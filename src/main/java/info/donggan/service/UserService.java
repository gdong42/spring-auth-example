package info.donggan.service;

import info.donggan.model.Response;
import org.springframework.stereotype.Service;

/**
 *
 * a user service that can verify from two different data source
 *
 * Created by gdong on 2017/1/24.
 */
@Service
public class UserService {

  /**
   * authenticates from 'Foo' data source
   *
   * @param username
   * @param password
   * @return
   */
  public Response authenticateFoo(String username, String password) {
    // here goes your actual username && password verification logic
    return "gan".equals(username) && "foo".equals(password) ?
        Response.ok() :
        Response.fail();
  }

  public Response loadFooUser(String username) {
    return "gan".equals(username) ? Response.ok() : Response.fail();
  }

  /**
   * authenticates from 'Bar' data source
   *
   * @param username
   * @param password
   * @return
   */
  public Response authenticateBar(String username, String password) {
    // here goes your actual username && password verification logic
    return "dong".equals(username) && "bar".equals(password) ?
        Response.ok() :
        Response.fail();
  }

  public Response loadBarUser(String username) {
    return "dong".equals(username) ? Response.ok() : Response.fail();
  }

}
