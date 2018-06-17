package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

import config.Configuration;
import exception.FatalException;

/**
 * This class is the implementation of DALServices and DALBackendServices used in production.
 */
public class DALServicesImpl implements DALServices, DALBackendServices {

  private BasicDataSource connectionPool;
  private ThreadLocal<Connection> threadMap;

  private static Logger logger = Logger.getLogger(DALServicesImpl.class);
  private final String dbDriver = Configuration.properties.getProperty("dbdriver");
  private final int dbConnPoolSize =
      Integer.parseInt(Configuration.properties.getProperty("dbconnpoolsize"));
  private final String dbHost = Configuration.properties.getProperty("dbhost");
  private final String dbPort = Configuration.properties.getProperty("dbport");
  private final String dbName = Configuration.properties.getProperty("dbname");
  private final String dbUser = Configuration.properties.getProperty("dbuser");
  private final String dbPassword = Configuration.properties.getProperty("dbpassword");

  {
    try {
      Class.forName(Configuration.properties.getProperty("dbdriver"));
    } catch (ClassNotFoundException exc) {
      logger.fatal("Missing PostgreSQL driver");
      throw new FatalException(exc);
    }
  }

  public DALServicesImpl() {
    this.threadMap = new ThreadLocal<Connection>();
    this.connectionPool = new BasicDataSource();
    String url = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
    this.connectionPool.setUrl(url);
    this.connectionPool.setUsername(dbUser);
    this.connectionPool.setPassword(dbPassword);
    this.connectionPool.setDriverClassName(dbDriver);
    this.connectionPool.setInitialSize(dbConnPoolSize);
  }

  /**
   * {@inheritDoc}
   */
  private void openConnection() {
    Connection conn = this.threadMap.get();
    if (conn == null) {
      logger.debug("Opening a connection (threadId=" + Thread.currentThread().getId() + ")");
      try {
        conn = this.connectionPool.getConnection();
      } catch (SQLException exc) {
        logger.fatal("Couldn't establish a connection to the database", exc);
        throw new FatalException(exc);
      }
      this.threadMap.set(conn);
    }
  }

  /**
   * {@inheritDoc}
   */
  private void closeConnection() {
    Connection conn = this.threadMap.get();
    if (conn != null) {
      threadMap.remove();
      try {
        conn.close();
      } catch (SQLException exc) {
        logger.fatal("Couldn't close a connection to the database", exc);
        throw new FatalException(exc);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PreparedStatement prepareStatement(String query) throws SQLException {
    this.openConnection();
    return this.threadMap.get().prepareStatement(query);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void startTransaction() {
    this.openConnection();
    try {
      this.threadMap.get().setAutoCommit(false);
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void commit() {
    try {
      this.threadMap.get().commit();
      this.threadMap.get().setAutoCommit(true);
      this.closeConnection();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void rollback() {
    try {
      this.threadMap.get().rollback();
      this.threadMap.get().setAutoCommit(true);
      this.closeConnection();
    } catch (SQLException exc) {
      logger.error("Unexpected error", exc);
    }
  }
}
