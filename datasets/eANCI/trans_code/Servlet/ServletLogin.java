package Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Manager.AccessManager;
import Manager.AdminManager;
import Manager.CIManager;
import Bean.Access;
import Bean.Administrator;
import Bean.IdentityCard;
import Bean.Citizen;
import Manager.CitizenManager;
import Bean.Employee;
import DB.DbException;
import Manager.EmployeeManager;

/**
 * The ServletLogin class performs the authentication operation of a user on the system The
 * ServletLogin class has no dependencies
 *
 * @author Federico Cinque
 */
public class ServletLogin extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int flag = -1;
    String login = request.getParameter("login");
    String password = request.getParameter("password");
    String type = request.getParameter("type");

    HttpSession session = request.getSession(true);
    try {
      AccessManager AM = new AccessManager();
      String url;

      Access ac = AM.getAccess(login);

      if (type != null) {  //Se type � diverso da null la servlet � stata invocata dal lato citizen
        flag = 0;
        if (AM.checkAccess(login, password) && ac.getType().equals("Citizen")) {

          CitizenManager CM = new CitizenManager();
          Citizen c = CM.getCitizenByLogin(login);
          CIManager ciM = new CIManager();
          IdentityCard ci = ciM.getCardByIdCStri(c.getIdCitizen());
          session.setAttribute("c", c);
          session.setAttribute("ci", ci);
          url = "/myDoc/user/home.jsp";
        } else
          url = "/myDoc/user/home.jsp?error=e";
      } else {  //Se type � null la servlet � stata invocata dal lato administrator/employee
        flag = 1;
        if (AM.checkAccess(login, password) && !ac.getType().equals("Citizen")) {
          session.setAttribute("login", ac.getLogin());
          session.setAttribute("type", ac.getType());
          if (ac.getType().equals("Employee")) {
            EmployeeManager IM = new EmployeeManager();
            Employee imp = IM.getEmployeeByLogin(login);
            session.setAttribute("imp", imp);
          } else if (ac.getType().equals("Administrator")) {
            AdminManager AdM = new AdminManager();
            Administrator am = AdM.getAdministratorByLogin(login);
            session.setAttribute("am", am);
          }
          url = "/myDoc/workers/index.jsp";
        } else
          url = "/myDoc/workers/Access.jsp?error=e";
      }
      response.sendRedirect(url);
    } catch (DbException e) {
      String url;
      if (flag == 1)
        url = "/myDoc/workers/Access.jsp?error=" + e.getMessage();
      else
        url = "/myDoc/user/home.jsp?error=" + e.getMessage();
      response.sendRedirect(url);
    }
  }
}