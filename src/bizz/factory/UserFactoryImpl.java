package bizz.factory;

import bizz.UserImpl;
import bizz.dto.UserDTO;

/**
 * This class is one implementation of the UserFactory, this is the implementation that should be
 * used in production.
 */
public class UserFactoryImpl implements UserFactory {
  /**
   * {@inheritDoc}
   */
  @Override
  public UserDTO createUser(String login, String password) {
    return new UserImpl(login, password);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserDTO createUser() {
    return new UserImpl();
  }
}
