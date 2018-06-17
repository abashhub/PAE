package persistence.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import bizz.dto.UserDTO;
import bizz.factory.UserFactory;
import persistence.DALBackendServices;
import persistence.DALServices;
import util.Util;

/**
 * This class is the implementation of UserDAO used in production. Since UserDAO extends DAO, this
 * implementation contains the basic CRUD as well.
 */
public class UserDAOImpl implements UserDAO {

  private DALServices dalServices;
  private UserFactory userFactory;

  private static Logger logger = Logger.getLogger(UserDAOImpl.class);
  private static final String SQL_SELECT_BY_ID =
      "SELECT * FROM PAE.utilisateurs u WHERE u.id_utilisateur = ?";
  private static final String SQL_SELECT_BY_LOGIN =
      "SELECT * FROM PAE.utilisateurs u WHERE lower(u.pseudo) = lower(?)";
  private static final String SQL_SELECT_BY_EMAIL =
      "SELECT * FROM PAE.utilisateurs u WHERE lower(u.email) = lower(?)";
  private static final String SQL_INSERT_USER =
      "INSERT INTO PAE.utilisateurs VALUES (DEFAULT, ?, ?, ?, ?, ?, ?) RETURNING id_utilisateur";

  /**
   * Class constructor.
   * 
   * @param dalServices the DALServices used to retrieve prepared statements.
   * @param userFactory the factory used to get user DTOs (Data Transfer Objects).
   */
  public UserDAOImpl(DALServices dalServices, UserFactory userFactory) {
    super();
    this.dalServices = dalServices;
    this.userFactory = userFactory;
  }

  /**
   * Builds a UserDTO given a ResultSet.
   * 
   * @param rs the ResultSet obtained through a previous query to the database.
   * @return a UserDTO filled with all the fields stored in the ResultSet.
   * @throws SQLException if the ResultSet can't be used.
   */
  private UserDTO buildUser(ResultSet rs) throws SQLException {
    UserDTO usr = this.userFactory.createUser();
    usr.setUserID(rs.getLong("id_utilisateur"));
    usr.setLogin(rs.getString("pseudo"));
    usr.setFirstName(rs.getString("prenom"));
    usr.setLastName(rs.getString("nom"));
    usr.setEmail(rs.getString("email"));
    usr.setManager(rs.getBoolean("est_responsable"));
    usr.setPassword(rs.getString("mot_de_passe"));
    return usr;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public UserDTO findByLogin(UserDTO userDTO) {
    UserDTO user = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_SELECT_BY_LOGIN);
      ps.setString(1, userDTO.getLogin());
      rs = ps.executeQuery();
      while (rs.next()) {
        user = this.buildUser(rs);
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return user;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public UserDTO findByEmail(UserDTO userDTO) {
    UserDTO user = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_SELECT_BY_EMAIL);
      ps.setString(1, userDTO.getEmail());
      rs = ps.executeQuery();
      while (rs.next()) {
        user = this.buildUser(rs);
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return user;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public UserDTO find(UserDTO userDTO) {
    UserDTO user = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_SELECT_BY_ID);
      ps.setLong(1, userDTO.getUserID());
      rs = ps.executeQuery();
      while (rs.next()) {
        user = this.buildUser(rs);
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return user;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public Long create(UserDTO userDTO) {
    Long insertedID = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = ((DALBackendServices) this.dalServices).prepareStatement(SQL_INSERT_USER);
      ps.setString(1, userDTO.getLogin());
      ps.setString(2, userDTO.getFirstName());
      ps.setString(3, userDTO.getLastName());
      ps.setString(4, userDTO.getEmail());
      ps.setBoolean(5, userDTO.isManager());
      ps.setString(6, userDTO.getPassword());
      rs = ps.executeQuery();
      while (rs.next()) {
        insertedID = rs.getLong(1);
      }
      rs.close();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    } finally {
      Util.closeQuietly(rs, ps);
    }
    return insertedID;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public Long update(UserDTO obj) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * {@inheritDoc}.
   */
  @Override
  public void delete(UserDTO obj) {
    // TODO Auto-generated method stub
  }
}
