package DB;

import Bean.*;

/**
 * The DbException class is thrown when a db-related exception occurs
 *
 * @author Antonio Leone
 * @version 1.0
 */
public class DbException extends RuntimeException {

  private static final long serialVersionUID = -6403170047487930045L;

  public DbException() {
  }

  public DbException(String message) {
    super(message);
  }
}