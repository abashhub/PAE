package persistence.dao;

import bizz.dto.UserDTO;

/**
 * This interface extends DAO and adds specific methods related to Users like findByLogin() and
 * findByEmail().
 */
public interface UserDAO extends DAO<UserDTO> {

  /**
   * Find a user by its login. userDTO is expected to contain the user's login.
   * 
   * @param userDTO the user Data Transfer Object.
   * @return a UserDTO containing all the info stored in DB.
   */
  UserDTO findByLogin(UserDTO userDTO);

  /**
   * Find a user by its email. userDTO is expected to contain the user's email.
   * 
   * @param userDTO the user Data Transfer Object.
   * @return a UserDTO containing all the info stored in DB.
   */
  UserDTO findByEmail(UserDTO userDTO);
}
