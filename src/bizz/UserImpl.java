package bizz;

import org.mindrot.jbcrypt.BCrypt;

import exception.BizzException;
import util.Util;

/**
 * This class is one implementation of the User interface and the UserDTO interface, this is the
 * implementation that should be used in production.
 */
public class UserImpl implements User {
  private Long userID;
  private int numVersion;
  private String login;
  private String firstName;
  private String lastName;
  private String email;
  private boolean manager;
  private String password;

  /**
   * Class constructor specifying every attribute.
   */
  public UserImpl(Long userID, String login, String firstName, String lastName, String email,
      boolean manager, String password) {
    super();
    this.userID = userID;
    this.login = login;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.manager = manager;
    this.password = password;
  }

  /**
   * Class constructor specifying the login and the password. Other attributes are expected to be
   * set through setters.
   */
  public UserImpl(String login, String password) {
    super();
    this.login = login;
    this.password = password;
  }

  /**
   * Empty class constructor. Attributes are expected to be set through setters.
   */
  public UserImpl() {}

  // -------------------------- Business methods -------------------------- //

  @Override
  public void checkPassword(String plainPassword) {
    if (!BCrypt.checkpw(plainPassword, this.password)) {
      throw new BizzException("Les mots de passe ne correspondent pas.");
    }
  }

  @Override
  public void checkLogin() {
    if (Util.anyNull(login) || Util.anyEmptyString(login)) {
      throw new BizzException("Le pseudo doit être spécifié.");
    }
    if (Util.anyNull(password) || Util.anyEmptyString(password)) {
      throw new BizzException("Le mot de passe doit être spécifié.");
    }
  }

  @Override
  public void checkRegistration() {
    if (Util.anyNull(login, firstName, lastName, email, password)) {
      throw new BizzException("Tous les champs doivent être remplis.");
    }
    if (Util.anyEmptyString(login, firstName, lastName, email, password)) {
      throw new BizzException("Tous les champs doivent être remplis.");
    }
    if (login.length() < User.MINIMUM_USERNAME_LENGTH) {
      throw new BizzException(
          "Le pseudo doit comporter au moins " + User.MINIMUM_USERNAME_LENGTH + " caractères.");
    }
    if (login.length() > User.MAXIMUM_USERNAME_LENGTH) {
      throw new BizzException(
          "Le pseudo ne peut pas dépasser " + User.MAXIMUM_USERNAME_LENGTH + " caractères.");
    }
    if (!Util.isValidUsername(login)) {
      throw new BizzException("Le pseudo n'a pas un format valide.");
    }
    if (!Util.isValidEmail(email)) {
      throw new BizzException("L'email n'a pas un format valide.");
    }
    if (password.length() < User.MINIMUM_PASSWORD_LENGTH) {
      throw new BizzException("Le mot de passe doit comporter au moins "
          + User.MINIMUM_PASSWORD_LENGTH + " caractères.");
    }
    if (password.length() > User.MAXIMUM_PASSWORD_LENGTH) {
      throw new BizzException(
          "Le mot de passe ne peut pas dépasser " + User.MAXIMUM_PASSWORD_LENGTH + " caractères.");
    }
  }

  @Override
  public String hashedPassword() {
    return BCrypt.hashpw(this.password, BCrypt.gensalt());
  }

  // -------------------------- Getters/setters --------------------------- //

  public Long getUserID() {
    return userID;
  }

  public void setUserID(Long userID) {
    this.userID = userID;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isManager() {
    return manager;
  }

  public void setManager(boolean manager) {
    this.manager = manager;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getNumVersion() {
    return numVersion;
  }

  public void setNumVersion(int numVersion) {
    this.numVersion = numVersion;
  }
}
