package Manager;

import java.util.Collection;

import Bean.Employee;
import DB.DbException;
import DB.DbEmployee;

/**
 * The EmployeeManager class interacts with the database management classes The EmployeeManager
 * class has no dependencies
 *
 * @author Federico Cinque
 */
public class EmployeeManager {
  private DbEmployee dbEmployee;

  /**
   * Default constructor of the EmployeeManager class
   */
  public EmployeeManager() {
    dbEmployee = new DbEmployee();
  }

  /**
   * Method that modify an employee invoking the relative method of class db
   *
   * @param number
   *     the string that identifies the employee
   * @param newEmployee
   *     employee with updated data
   * @return True if an entry has been made in the db, False otherwise
   */
  public boolean modifyEmployee(String number, Employee newEmployee) throws DbException {
    return dbEmployee.modifyEmployee(number, newEmployee);
  }

  /**
   * Method that returns an employee invoking the relative method of class db
   *
   * @param number
   *     string that is used as the employee number
   * @return Returns an object of type Employee
   * @throws DbException
   */
  public Employee searchEmployeeByNumber(String number) throws DbException {
    return dbEmployee.getEmployeeByNumber(number);
  }

  /**
   * Method that inserts an employee inside the db invoking the relative method of class db
   *
   * @param newEmployee
   *     Object of type Employee
   * @return True if an entry has been made in the db, False otherwise
   * @throws DbException
   */
  public boolean insertEmployee(Employee newEmployee) throws DbException {
    return dbEmployee.insertEmployee(newEmployee);
  }

  /**
   * Method that delete an employee from the db invoking the relative method of class db
   *
   * @param number
   *     the string that is used as number
   * @return True if a delete has been made in the db, False otherwise
   * @throws DbException
   */
  public boolean deleteEmployee(String number) throws DbException {
    return dbEmployee.deleteEmployee(number);
  }

  /**
   * Method that returns an employee invoking the relative method of class db
   *
   * @param login
   *     string that is used as the employee login
   * @return Returns an object of type employee
   * @throws DbException
   */
  public Employee getEmployeeByLogin(String login) throws DbException {
    return dbEmployee.getEmployeeByLogin(login);
  }

  /**
   * Method that returns a collection of employees invoking the relative method of class db
   *
   * @param nameImp
   *     string that is used as the employee name
   * @param surnameImp
   *     string that is used as the employee's surname
   * @return Returns a Collection of Employees
   * @throws DbException
   */
  public Collection<Employee> getEmployeeByName(String nameImp, String surnameImp)
      throws DbException {
    return dbEmployee.getEmployeeByName(nameImp, surnameImp);
  }

  /**
   * Method that returns all stored employees invoking the relative method of class db
   *
   * @return Returns a collection of employees
   * @throws DbException
   */
  public Collection<Employee> getEmployees() throws DbException {
    return dbEmployee.getEmployees();
  }
}
