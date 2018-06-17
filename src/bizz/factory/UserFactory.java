package bizz.factory;

import bizz.dto.UserDTO;

/**
 * 
 * This interface is used by the Servlet and the persistance package, it's purpose is to give them
 * the UserDTO they need.
 *
 */
public interface UserFactory {
  /**
   * Returns an object implementing UserDTO and that only has a login and a password set.
   * 
   * @param login the user's login
   * @param password the user's password
   * @return an object implementing UserDTO whose login and password fields are set
   */
  UserDTO createUser(String login, String password);

  /**
   * Returns an empty object implementing UserDTO
   * 
   * @return an object implementing UserDTO whose fields are all empty
   */
  UserDTO createUser();
}
