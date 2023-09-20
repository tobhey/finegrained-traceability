package Servlet;

import Bean.*;
import Manager.*;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * The ServletChargePractice class loads Citizen and IdentityCard objects into a session The class
 * depends on DbCitizen and DbIdentityCard
 *
 * @author Christian Ronca
 */

public class ServletChargePractice extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    int user_id = Integer.parseInt(request.getParameter("user_id"));

    CitizenManager cm = new CitizenManager();
    Citizen citizen = cm.getCitizenById(user_id);
    HttpSession session = request.getSession();
    CIManager cim = new CIManager();
    IdentityCard ci = cim.getCardByIdCStri(user_id);

    session.setAttribute("c", citizen);
    session.setAttribute("ci", ci);

    String url = "/myDoc/workers/index.jsp?func=pra&page=modulone";
    response.sendRedirect(url);
  }

}
