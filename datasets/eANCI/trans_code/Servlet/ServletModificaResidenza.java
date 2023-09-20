package Servlet;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

import DB.DbIdentityCard;
import DB.DbException;

public class ServletEditResidence extends HttpServlet {

  public void init(ServletConfig conf) throws ServletException {
    super.init(conf);
  }

  /**
   * method that searches for files containing requests for change of residence by the cityadins
   * saved on the server where the employee can access it to retrieve information useful for filling
   * out the form which must be registered internally of the municipal archive
   */
  public class SearchFiles {
    //contatore usato per numerare i file ottenuti
    private int count = 0;

    private File to;
    private String internal;

    private File from;

    public SearchFiles() {
      //costruttore di default
    }

    public String main(int id_citizen, String date) {
      // in the case of the employee the path is c: \\ RequestsCambioResidence
      // directory to search for files with certain extensions
      from = new File("C: \\ RequestsCambioResidence");

      // name of the file to search for. For example I am looking for a file named foo will insert foo
      // if not specified will search for all files with any name
      // I save myself as a string for the citizen id to then compare it with
      // the id of the citizen that comes to me from the database to look for the
      // change file associated with that citizen
      String id_city = String.valueOf(id_citizen);
      String dataric = date;

      String param = "internal";
      String param1 = "external";
      // name of the file to search for
      String name = dataric + "_" + id_city + "_".concat(param);
      String namefile2 = dataric + "_" + id_city + "_".concat(param1);

      String[] splitter = name.split("_");

      // extension of the file to search for. For example * .mp3 I will insert .mp3
      // if not specified it will search for all files with any extension
      String type = ".pdf";

      File newfile = exploreFile(from, name, type);
      if (newfile != null) {
        internal = "internal";
      } else {
        newfile = exploreFile(from, namefile2, type);
        if (newfile != null) {
          // external file
          internal = "external";
        } else {
          internal = null; // file not present
        }
      }
      return internal;
    }

    /**
     * from � la cartella in cui cercare il file di name "name" e di type ".pdf"
     *
     * @param from
     * @param name
     * @param type
     */

    protected File exploreFile(File from, String name, String type) {
      //utilizziamo per la search un filtro
      File[] files = from.listFiles(new Filter(name, type));
      //ordiniamo i file nella lista secondo la data
      if (files.length > 1 || files.length == 0) {
        return null;
      } else {
        return files[0];

      }

    }

    class Filter implements FilenameFilter {
      //estensione del file
      private String type, name;

      public Filter(String name, String type) {
        this.name = name;
        this.type = type;
      }

      //accettiamo tutti i file con estensione s e le directory che non siano
      //nascoste
      public boolean accept(File dir, String file) {
        File f = new File(dir, file);

        //controllo sul type.
        //Ad esempio se cerco \"pippo.txt\" la indexOf(.txt) � uguale a 6 ed � uguale
        // ed � uguale a 10(\"pippo.txt\".length()) - 4 (\".txt\".length)

        boolean flag1 = true;
        if (type != null && type != "\\")
          flag1 = (file.indexOf(type) == file.length() - type.length());

        //controllo sul name
        //flag2 � true se il file contiene la parola cercata

        boolean flag2 = true;
        if (name != null && name != "\\")
          flag2 = file.toUpperCase().indexOf(name.toUpperCase()) != -1;
        //ritorno i file che passano il controllo del type e del name le directory

        return ((flag1 && flag2) || f.isDirectory()) && !f.isHidden();
      }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

      //innanzitutto mi recupero la sessione di lavoro del citizen
      HttpSession session = request.getSession();
      int idCitizen;
      String idtrovato;
      /**
       * se la sessione � stata creata correttamente
       * all'access dell'employee, viene mandato in exe
       * il metodo che controlla se sono presenti file pdf
       * riguardanti il cambio di residence interno o
       * esterno richiesti dai cityadini
       */
      if (session != null) {
        ServletContext sc = getServletContext();
        RequestDispatcher rd = null;
        String ris;
        try {
          /**
           * (1)	cosa deve fare la servlet:
           * 		deve prendere l'id del citizen
           * 		di cui ha inserito il codice della carta di identit�
           * 		dal form e lo passa al metodo main
           * 		per cercare il file corrispondente
           */
          /**
           * (2)	il codice della carta di identit� e la data della request
           * 		li prende con request.getParameter
           * 		salvandoli nelle apposite variabili,
           * 		dopodich� viene chiamato il metodo che restituisce
           * 		l'id di un citizen dopo avergli
           * 		passato name, surname e codice_fiscale di
           * 		quest'ultimo.
           */
          String cardCode = request.getParameter("cardCode");
          String data = request.getParameter("year")
              .concat(request.getParameter("month").concat(request.getParameter("day")));
          /**
           * una volta salvati i parametri, li passo
           * al metodo che restituisce l'id di questo
           * particolare citizen che ha instreetto la
           * request di cambio di residence e di cui
           * se ne vuole cercare il file
           */
          DbIdentityCard dbcarta = new DbIdentityCard();
          idCitizen = dbcarta.searchIdentityCardByNumber(cardCode).id();
          String risp;
          if (idCitizen == -1) {
            risp = "Non � possibile recuperare l'id del citizen all'interno del database";
            response.sendRedirect(
                "http://localhost:8080/E_ANCI/index.jsp?error=e");//da modifyre i collegamenti
            request.setAttribute("risp", risp);
          } else {
            /**
             * una volta conosciuto l'id del citizen
             * lo passo al metodo di search del file
             * ad esso corrispondente
             */
            SearchFiles search = new SearchFiles();
            idtrovato = search.main(idCitizen, data);
            /**
             * ora nella variabile idtrovato c'� l'esito della search
             * del file relativa ad un determinato citizen
             * che ha instreetto la request di cambio di residence.
             * tale valore andr� controllato, se � true si lancia
             * all'employee il modulo da compilare per mantenere traccia
             * della request dopo aver effettuato i controlli necessari
             */
            if (idtrovato == null) {
              ris = "File della request di cambio di residence non trovato";
              request.setAttribute("ris", ris);
              rd = sc.getRequestDispatcher(
                  "/workers/index.jsp?func=operazione&page=fallita");//da modifyre i collegamenti
            } else if (idtrovato.equals("interno")) {
              rd = sc.getRequestDispatcher(
                  "/workers/index.jsp?func=pra&page=modulone");//da modifyre i collegamenti
            } else if (idtrovato.equals("esterno")) {
              rd = sc.getRequestDispatcher(
                  "/workers/index.jsp?func=pra&page=modulone");//da modifyre i collegamenti
            }
            rd.forward(request, response);
          }
        } catch (DbException e) {
          ris = e.getMessage();
          request.setAttribute("ris", ris);
          rd = sc.getRequestDispatcher(
              "/workers/index.jsp?func=operazione&page=fallita");//da modifyre i collegamenti
          rd.forward(request, response);
        }

      } else {
        String url = "/workers/Access.jsp";//da modifyre i collegamenti
        response.sendRedirect(url);
      }
    }
  }
}