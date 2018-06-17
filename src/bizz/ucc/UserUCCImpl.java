package bizz.ucc;

import org.apache.log4j.Logger;

import bizz.User;
import bizz.dto.UserDTO;
import exception.BizzException;
import persistence.DALServices;
import persistence.dao.DAO;
import persistence.dao.UserDAO;
import util.Util;

/**
 * This class is the implementation of UserUCC, this implementation is meant to be used in
 * production.
 */
public class UserUCCImpl implements UserUCC {
  DALServices dalServices;
  DAO<UserDTO> userDAO;

  private static Logger logger = Logger.getLogger(UserUCCImpl.class);

  /**
   * Class constructor.
   * 
   * @param dalServices The interface used to operate the database. Typically used to start
   *        transactions and commit modifications.
   * @param userDAO The user DAO (Data Access Object) used to perform operations on the database
   *        regarding users.
   */
  public UserUCCImpl(DALServices dalServices, DAO<UserDTO> userDAO) {
    this.dalServices = dalServices;
    this.userDAO = userDAO;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public UserDTO login(UserDTO userDTO) {
    if (Util.anyNull(userDTO)) {
      throw new BizzException("Erreur, utilisateur inexistant.");
    }
    ((User) userDTO).checkLogin();
    dalServices.startTransaction();
    UserDTO userDb = ((UserDAO) userDAO).findByLogin(userDTO);
    dalServices.commit();
    if (userDb == null) {
      throw new BizzException("Erreur, pas d'utilisateur avec ce pseudo.");

    }
    // A BizzException will be thrown if passwords don't match
    ((User) userDb).checkPassword(userDTO.getPassword());
    return userDb;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public UserDTO register(UserDTO userDTO) {
    UserDTO userReturn = null;
    // Checks if fields are valid. A BizzException will be thrown if not.
    ((User) userDTO).checkRegistration();

    // Checks if email and login are each unique
    dalServices.startTransaction();
    UserDTO tmp1 = ((UserDAO) userDAO).findByEmail(userDTO);
    UserDTO tmp2 = ((UserDAO) userDAO).findByLogin(userDTO);
    if (tmp1 != null) {
      throw new BizzException("Email déjà utilisé.");
    } else if (tmp2 != null) {
      throw new BizzException("Pseudo déjà pris.");
    }

    // Everything is OK, we can create the user
    userDTO.setPassword(((User) userDTO).hashedPassword());
    Long newID = userDAO.create(userDTO);
    if (newID == null) {
      // An error has occurred -> rollback
      dalServices.rollback();
      throw new BizzException("Utilisateur n'a pas été créer.");
    } else {
      logger.debug("userID=" + newID);
      dalServices.commit();
      userDTO.setUserID(newID);
      userReturn = userDAO.find(userDTO);
    }
    return userReturn;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public UserDTO getUserByLogin(UserDTO userDTO) {
    dalServices.startTransaction();
    UserDTO user = ((UserDAO) userDAO).findByLogin(userDTO);
    dalServices.commit();
    if (user == null) {
      throw new BizzException("Aucun utilisateur avec ce pseudo");
    }
    return user;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public UserDTO getUserByEmail(UserDTO userDTO) {
    dalServices.startTransaction();
    UserDTO user = ((UserDAO) userDAO).findByEmail(userDTO);
    dalServices.commit();
    if (user == null) {
      throw new BizzException("Aucun utilisateur avec cet email");
    }
    return user;
  }
}
