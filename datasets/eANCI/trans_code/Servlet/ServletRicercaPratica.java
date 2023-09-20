package Servlet;

import Bean.*;
import DB.*;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The ServletSearchPractice class returns a specified practice in the search engine The class
 * depends on the DbRequests class
 *
 * @author Christian Ronca
 */

public class ServletSearchPractice extends HttpServlet {

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    int cod = Integer.parseInt(request.getParameter("codice"));
    String value = request.getParameter("valore");

    int idp = 0, idr = 0;
    String idr_attesa = "", idr_lavorazione = "", idr_completato = "";

    ArrayList<Request> arrayList;
    Request ric = null;
    DbRequest dbric = null;

    try {
      dbric = new DbRequest();
      System.out.println("ok");

      if (value.equals("idp")) {
        ric = dbric.getRequestById(cod);
        request.setAttribute("ris", ric);
      } else if (value.equals("idr")) {
        arrayList = (ArrayList<Request>) dbric.getRequestByApplicant(cod);
        request.setAttribute("ris", arrayList);
      } else if ((value.equals("accettata")) || value.equals("rifiutata")) {
        arrayList = (ArrayList<Request>) dbric.getRequestByState(cod, value);
        request.setAttribute("ris", arrayList);
      } else {
        arrayList = null;
      }

    } catch (Exception e) {
      //e.getMessage().toString();
      e.printStackTrace();
    }

    ServletContext sc = getServletContext();
    RequestDispatcher rd = sc.getRequestDispatcher(
        "/workers/index.jsp?func=visualizza&page=DatiPratica");
    rd.forward(request, response);
  }
}
