package DB;

import Bean.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The DbRequest class takes care of managing the connections to the db
 *
 * @author Antonio Leone
 * @version 1.0
 */

public class DbRequest {

  private Connection connection;

  public DbRequest() throws DbException {
    try {
      connection = DbConnection.getConnection();
    } catch (SQLException e) {
      throw new DbException("Error: Connection failed");
    }
  }

  /**
   * Method that inserts a request into the db
   *
   * @param ric
   *     object of type Request
   * @return True if an entry has been made in the db, False otherwise
   * @throws DbException
   */

  public boolean insertRequest(Request ric) throws DbException {
    int ret = 0;
    PreparedStatement statement = null;
    String type = ric.getType();
    String data = ric.getData();
    int applicant = ric.getApplicant();
    String state = ric.getState();
    String document = ric.getDocument();
    try {
      statement = connection.prepareStatement("INSERT INTO request VALUES (?, ?, ?, ?, ?, ?)");
      statement.setInt(1, 0);
      statement.setString(2, type);
      statement.setString(3, data);
      statement.setInt(4, applicant);
      statement.setString(5, state);
      statement.setString(6, document);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: Request insertion failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: Request insertion failed");
      }
    }
  }

  /**
   * Method that delete a request from the db
   *
   * @param id
   *     the integer that is used as the request id
   * @return True if a delete has been made in the db, False otherwise
   * @throws DbException
   */

  public boolean deleteRequest(int id) throws DbException {
    PreparedStatement statement = null;
    int ret = 0;
    try {
      statement = connection.prepareStatement("DELETE FROM request WHERE idRequest =?");
      statement.setInt(1, id);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: request deletion failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: request deletion failed");
      }
    }
  }

  /**
   * Method that returns a request
   *
   * @param id
   *     the integer that is used as the request id
   * @return Returns an object of type Request
   * @throws DbException
   */

  public Request getRequestById(int id) throws DbException {
    PreparedStatement statement = null;
    ResultSet rs = null;
    Request ret = null;
    try {
      statement = connection.prepareStatement("SELECT* FROM request WHERE idRequest = ?");
      statement.setInt(1, id);
      rs = statement.executeQuery();
      if (!rs.next())
        return ret;
      int idR = rs.getInt("idRequest");
      String type = rs.getString("type");
      String data = rs.getString("data");
      int applicant = rs.getInt("applicant");
      String state = rs.getString("state");
      String document = rs.getString("document");
      ret = new Request(idR, type, data, applicant, state, document);
      return ret;
    } catch (SQLException e) {
      throw new DbException("Error: search by request id failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: search by request id failed");
      }
    }
  }

  /**
   * Method that returns a set of requests
   *
   * @param idR
   *     the integer which is used as the requester id
   * @return Returns a Collection of Requests
   * @throws DbException
   */

  public Collection<Request> getRequestByApplicant(int idR) throws DbException {
    ArrayList<Request> ret = new ArrayList<Request>();
    PreparedStatement statement = null;
    ResultSet rs = null;
    try {
      statement = connection.prepareStatement(
          "SELECT * FROM request WHERE applicant = ? ORDER BY  data desc");
      statement.setInt(1, idR);
      rs = statement.executeQuery();
      while (rs.next()) {
        int idRic = rs.getInt("idRequest");
        String type = rs.getString("type");
        String data = rs.getString("data");
        int applicant = rs.getInt("applicant");
        String stateR = rs.getString("state");
        String document = rs.getString("document");
        ret.add(new Request(idRic, type, data, applicant, stateR, document));
      }
      if (ret.isEmpty())
        return null;
      else
        return ret;
    } catch (SQLException e) {
      throw new DbException("Error: Search requests via applicant id failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: Search requests via applicant id failed");
      }
    }
  }

  /**
   * Method that returns a set of requests
   *
   * @param idR
   *     the integer that is used as the applicant's id
   * @param state
   *     the string that is used as the state of the request
   * @return Returns a Collection of type Request
   * @throws DbException
   */

  public Collection<Request> getRequestByState(int idR, String state) throws DbException {
    ArrayList<Request> ret = new ArrayList<Request>();
    PreparedStatement statement = null;
    ResultSet rs = null;
    try {
      statement = connection.prepareStatement(
          "SELECT * FROM request WHERE applicant =? and state =? ORDER BY  data desc");
      statement.setInt(1, idR);
      statement.setString(2, state);
      rs = statement.executeQuery();
      while (rs.next()) {
        int idRic = rs.getInt("idRequest");
        String type = rs.getString("type");
        String data = rs.getString("data");
        int applicant = rs.getInt("applicant");
        String stateR = rs.getString("state");
        String document = rs.getString("document");
        ret.add(new Request(idRic, type, data, applicant, stateR, document));
      }
      if (ret.isEmpty())
        return null;
      else
        return ret;
    } catch (SQLException e) {
      throw new DbException("Error: search requests via applicant id and state request failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: search requests via applicant id and state request failed ");
      }
    }
  }

  /**
   * Method that returns a set of requests
   *
   * @param idR
   *     the integer that is used as the applicant's id
   * @param type
   *     the string that is used as the typelogy of the request
   * @return Returns a Collection of type Request
   * @throws DbException
   */

  public Collection<Request> getRequestByType(int idR, String type) throws DbException {
    ArrayList<Request> ret = new ArrayList<Request>();
    PreparedStatement statement = null;
    ResultSet rs = null;
    try {
      statement = connection.prepareStatement(
          "SELECT * FROM request WHERE applicant =? and type =? ORDER BY  data desc");
      statement.setInt(1, idR);
      statement.setString(2, type);
      rs = statement.executeQuery();
      while (rs.next()) {
        int idRic = rs.getInt("idRequest");
        String typeR = rs.getString("type");
        String data = rs.getString("data");
        int applicant = rs.getInt("applicant");
        String stateR = rs.getString("state");
        String document = rs.getString("document");
        ret.add(new Request(idRic, typeR, data, applicant, stateR, document));
      }
      if (ret.isEmpty())
        return null;
      else
        return ret;
    } catch (SQLException e) {
      throw new DbException("Error: search requests via applicant id and type request failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: search via applicant id and type request failed");
      }
    }
  }

  /**
   * Method that modify the state of a request
   *
   * @param idR
   *     the integer that is used as the request id
   * @param state
   *     the string that is used as the state of the request
   * @return True if the modify was successful, False otherwise
   * @throws SQLException
   */

  public boolean setStateRequest(int idR, String state) throws DbException {
    PreparedStatement statement = null;
    int ret = 0;
    try {
      statement = connection.prepareStatement("UPDATE request SET state = ? WHERE idRequest = ?");
      statement.setString(1, state);
      statement.setInt(2, idR);
      ret = statement.executeUpdate();
      return (ret == 1);
    } catch (SQLException e) {
      throw new DbException("Error: modify state request failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
      } catch (SQLException e) {
        throw new DbException("Error: modify state request failed");
      }
    }
  }

  /**
   * Method that returns a set of requests
   *
   * @return Returns a Collection of Requests
   * @throws DbException
   */

  public Collection<Request> getRequests() throws DbException {
    ArrayList<Request> ret = new ArrayList<Request>();
    PreparedStatement statement = null;
    ResultSet rs = null;
    try {
      statement = connection.prepareStatement("SELECT * FROM request ORDER BY data desc");
      rs = statement.executeQuery();
      while (rs.next()) {
        int idRic = rs.getInt("idRequest");
        String type = rs.getString("type");
        String data = rs.getString("data");
        int applicant = rs.getInt("applicant");
        String stateR = rs.getString("state");
        String document = rs.getString("document");
        ret.add(new Request(idRic, type, data, applicant, stateR, document));
      }
      if (ret.isEmpty())
        return null;
      else
        return ret;
    } catch (SQLException e) {
      throw new DbException("Error: search requests failed");
    } finally {
      try {
        if (statement != null)
          statement.close();
        if (rs != null)
          rs.close();
      } catch (SQLException e) {
        throw new DbException("Error: Search requests via applicant id failed");
      }
    }
  }
}