package persistence;

/**
 * This interface is part of the DAL (Data Access Layer). It is exposed to the UCCs (Use-case
 * Controllers) to manage the connections to the database.
 */
public interface DALServices {
  /**
   * Opens a connection and starts a transaction.
   */
  void startTransaction();

  /**
   * Commits all operations performed on the database and closes the connection associated to the
   * current thread.
   */
  void commit();

  /**
   * Rolls back all operations performed on the database and closes the connection associated to the
   * current thread.
   */
  void rollback();
}
