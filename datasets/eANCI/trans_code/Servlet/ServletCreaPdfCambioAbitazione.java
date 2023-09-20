package Servlet;

import Bean.IdentityCard;
import Bean.Citizen;
import DB.*;
import Manager.AccessManager;
import Manager.CitizenManager;
import Manager.FamilyManager;

import java.io.*;
import java.util.GregorianCalendar;
import javax.servlet.*;
import javax.servlet.http.*;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * The ServletCreatePdfHomeChange class is a PDF file based on data received from a JSP page The
 * class has no dependencies
 *
 * @author Christian Ronca
 */
public class ServletCreatePdfHomeChange extends HttpServlet {
  private static final long serialVersionUID = -168526506138896791L;
  private HttpSession session;

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    session = request.getSession();

    try {
      GregorianCalendar gc = new GregorianCalendar();
      int gg = gc.get(GregorianCalendar.DATE);
      int mm = gc.get(GregorianCalendar.MONTH) + 1;
      int year = gc.get(GregorianCalendar.YEAR);
      String now = "" + year + mm + gg;
      String inout = "";

      String name_municipality = request.getParameter("municipalityname").toUpperCase();
      //String numb_document		= request.getParameter("numberdocument");
      String name = request.getParameter("name").toUpperCase();
      String surname = request.getParameter("surname").toUpperCase();
      String name_citya = request.getParameter("citya").toUpperCase();
      String gg_date = request.getParameter("gg");
      String mm_date = request.getParameter("mm");
      String aa_date = request.getParameter("aa");
      String newmunicipality = request.getParameter("newmunicipality").toUpperCase();
      String indir = request.getParameter("street").toUpperCase();
      String number_civic = request.getParameter("civic");
      //String interno				= request.getParameter("interno");
      String indirnew = request.getParameter("newstreet").toUpperCase();
      String num_civnew = request.getParameter("num");
      //String interno_new			= request.getParameter("interno1");
      String pref = request.getParameter("pref");
      String tel = request.getParameter("tel");
      String cntlr = request.getParameter("radio");
      String check = request.getParameter("check");

      String parent1 = request.getParameter("parent1").toUpperCase();
      String surname1 = request.getParameter("surname1").toUpperCase();
      String name1 = request.getParameter("name1").toUpperCase();
      String place1 = request.getParameter("place1").toUpperCase();
      String gg1 = request.getParameter("gg1");
      String mm1 = request.getParameter("mm1");
      String aa1 = request.getParameter("aa1");
      String parent2 = request.getParameter("parent2").toUpperCase();
      String surname2 = request.getParameter("surname2").toUpperCase();
      String name2 = request.getParameter("name2").toUpperCase();
      String place2 = request.getParameter("place2").toUpperCase();
      String gg2 = request.getParameter("gg2");
      String mm2 = request.getParameter("mm2");
      String aa2 = request.getParameter("aa2");
      String parent3 = request.getParameter("parent3").toUpperCase();
      String surname3 = request.getParameter("surname3").toUpperCase();
      String name3 = request.getParameter("name3").toUpperCase();
      String place3 = request.getParameter("place3").toUpperCase();
      String gg3 = request.getParameter("gg3");
      String mm3 = request.getParameter("mm3");
      String aa3 = request.getParameter("aa3");
      String parent4 = request.getParameter("parent4").toUpperCase();
      String surname4 = request.getParameter("surname4").toUpperCase();
      String name4 = request.getParameter("name4").toUpperCase();
      String place4 = request.getParameter("place4").toUpperCase();
      String gg4 = request.getParameter("gg4");
      String mm4 = request.getParameter("mm4");
      String aa4 = request.getParameter("aa4");
      String parent5 = request.getParameter("parent5").toUpperCase();
      String surname5 = request.getParameter("surname5").toUpperCase();
      String name5 = request.getParameter("name5").toUpperCase();
      String place5 = request.getParameter("place5").toUpperCase();
      String gg5 = request.getParameter("gg5");
      String mm5 = request.getParameter("mm5");
      String aa5 = request.getParameter("aa5");
      String parent6 = request.getParameter("parent6").toUpperCase();
      String surname6 = request.getParameter("surname6").toUpperCase();
      String name6 = request.getParameter("name6").toUpperCase();
      String place6 = request.getParameter("place6").toUpperCase();
      String gg6 = request.getParameter("gg6");
      String mm6 = request.getParameter("mm6");
      String aa6 = request.getParameter("aa6");

      String name11 = request.getParameter("name11").toUpperCase();
      String name12 = request.getParameter("name12").toUpperCase();
      String relationship = request.getParameter("relationship").toUpperCase();
      //String data_gg				= request.getParameter("data_gg");
      //String data_mm				= request.getParameter("data_mm");
      //String data_aa				= request.getParameter("data_aa");
      //String identita1			= request.getParameter("identita1").toUpperCase();
      //String identita2			= request.getParameter("identita2").toUpperCase();
      //String cod_doc				= request.getParameter("cod_document");
      String streetAddress = request.getParameter("streetAddress").toUpperCase();
      String civicNumber1 = request.getParameter("civicNumber1");
      //String interno1				= request.getParameter("interno1");
      String prec_res = request.getParameter("prec_res").toUpperCase();
      //String cc_date				= request.getParameter("cc_date");
      //String cc_month				= request.getParameter("cc_month");
      //String cc_year				= request.getParameter("cc_year");
      String ab_libera = request.getParameter("home");
      String abilita_cod_doc = request.getParameter("cod_doc");
      String cod_document = request.getParameter("cod_document");
      String nametab1 = request.getParameter("nametab1").toUpperCase();
      String surnametab1 = request.getParameter("surnametab1").toUpperCase();
      String nametab2 = request.getParameter("nametab2").toUpperCase();
      String surnametab2 = request.getParameter("surnametab2").toUpperCase();
      String nametab3 = request.getParameter("nametab3").toUpperCase();
      String surnametab3 = request.getParameter("surnametab3").toUpperCase();
      String nametab4 = request.getParameter("nametab4").toUpperCase();
      String surnametab4 = request.getParameter("surnametab4").toUpperCase();
      String nametab5 = request.getParameter("nametab5").toUpperCase();
      String surnametab5 = request.getParameter("surnametab5").toUpperCase();
      String nametab6 = request.getParameter("nametab6").toUpperCase();
      String surnametab6 = request.getParameter("surnametab6").toUpperCase();
      String nametab7 = request.getParameter("nametab7").toUpperCase();
      String surnametab7 = request.getParameter("surnametab7").toUpperCase();
      //String nametab8				= request.getParameter("nametab8").toUpperCase();
      //String surnametab8			= request.getParameter("surnametab8").toUpperCase();

      //String qualifica_vigile 	= request.getParameter("qualifica_vigile"); //maresciallo
      String dati_vigile =
          request.getParameter("name_vigile") + " " + request.getParameter("surname_vigile");
      String disposes = request.getParameter("official_text");
      String accert = request.getParameter("text_area");
      String streetAddress_esatto_d = request.getParameter("radio1");
      String streetAddress_esatto = request.getParameter("indix"); // indix
      //String ab_effettivamente	= request.getParameter("check");
      String alloggio_occupato = request.getParameter("check1");
      String cp1 = request.getParameter("surname_pers");
      String np1 = request.getParameter("name_pers");
      String pp1 = request.getParameter("parent");
      String cp2 = request.getParameter("surname_pers1");
      String np2 = request.getParameter("name_pers1");
      String pp2 = request.getParameter("parent1");
      String cp3 = request.getParameter("surname_pers2");
      String np3 = request.getParameter("name_pers2");
      String pp3 = request.getParameter("parent2");
      String proprieta_componenti = request.getParameter("check2");
      String titolo_di_possesso = request.getParameter("locazione");
      //String extra_possesso		= request.getParameter("specification");
      String type_di_alloggio = request.getParameter("check3_si");
      String osser_homeione = request.getParameter("textarea");
      String mot = request.getParameter("rad");
      String mot_causa = request.getParameter("text_trasfert");
      String profession = request.getParameter("prof1");
      String ind_lavoro = request.getParameter("all");
      String transfer = request.getParameter("indiconiuge");
      String circostanze = request.getParameter("circos");
      String oss_finali = request.getParameter("osservtext");

      cntlr = "stesso";
      if (cntlr.equals("stesso")) {
        inout = "interno";
      } else {
        inout = "altro";
      }

      Document document = new Document(PageSize.A4);
      PdfWriter.getInstance(document, response.getOutputStream());
      FileOutputStream fout = new FileOutputStream(
          "webapps//myDoc//workers//pratiche_complete//" + now + "_2345_" + inout + ".pdf");
      PdfWriter.getInstance(document, fout);
      response.setContentType("application/pdf");
      document.open();

      Paragraph spazio = new Paragraph("\n");
      Paragraph anagrafe_municipality = new Paragraph(
          "Anagrafe del municipality di " + name_municipality,
          new Font(Font.HELVETICA, 10, Font.BOLD));
      anagrafe_municipality.setAlignment(Element.ALIGN_RIGHT);
      document.add(anagrafe_municipality);

      Paragraph oggetto = new Paragraph("OGGETTO: DICHIARAZIONE DI CAMBIAMENTO DI ABITAZIONE",
          new Font(Font.HELVETICA, 12, Font.BOLD));
      oggetto.setAlignment(Element.ALIGN_CENTER);
      document.add(oggetto);

      Paragraph sottoscritto = new Paragraph(
          "   Io sottoscritto/a " + surname + " " + name + " nato/a in " + name_citya + " il "
              + gg_date + "/" + mm_date + "/" + aa_date + " gi� residente in street " + indir + " "
              + number_civic
              + " dichiaro, ai sensi e per gli effetti del combinato disposto dagli articoli 10,"
              + " lettera a) e 13 del D.P.R. 30/05/1989, n 223 di essermi trasferito nel municipality di "
              + newmunicipality + " in street " + indirnew + " " + num_civnew + " tel: " + pref
              + " " + tel + " unitamente ai seguenti familiari/conviventi:\n ");
      sottoscritto.setAlignment(Element.ALIGN_JUSTIFIED);
      document.add(sottoscritto);

      //tabella state di famiglia
      PdfPTable sf = new PdfPTable(5);
      sf.setWidthPercentage(100);
      sf.addCell(new Paragraph("PARENTELA"));
      sf.addCell(new Paragraph("NOME"));
      sf.addCell(new Paragraph("COGNOME"));
      sf.addCell(new Paragraph("LUOGO DI NASCITA"));
      sf.addCell(new Paragraph("DATA"));
      sf.addCell(new Paragraph(parent1));
      sf.addCell(new Paragraph(surname1));
      sf.addCell(new Paragraph(name1));
      sf.addCell(new Paragraph(place1));
      sf.addCell(new Paragraph(gg1 + "/" + mm1 + "/" + aa1));
      sf.addCell(new Paragraph(parent2));
      sf.addCell(new Paragraph(surname2));
      sf.addCell(new Paragraph(name2));
      sf.addCell(new Paragraph(place2));
      sf.addCell(new Paragraph(gg2 + "/" + mm2 + "/" + aa2));
      document.add(sf);

      Paragraph mendace = new Paragraph(
          "Dichiaro, ai sensi e per gli effetti di cui art. 46 e 47 del DPR 445/00 e pienamente consapevole "
              + "delle responsabilit� civili e penali previste in caso di dichiarazione mendace che:\n\n");
      mendace.setAlignment(Element.ALIGN_JUSTIFIED);
      document.add(mendace);

      if (ab_libera.equals("si")) {
        Paragraph ab = new Paragraph(
            " - l'homeione nella quale mi sono trasferito/a � libera da persone e/o cose;\n\n\n");
        document.add(ab);
      } else {
        Paragraph occ = new Paragraph(" - � occupata da terze parti, sotto riportate: \n\n");
        document.add(occ);
        PdfPTable altri_occupanti = new PdfPTable(2);
        altri_occupanti.setWidthPercentage(100);
        altri_occupanti.addCell(new Paragraph("NOME"));
        altri_occupanti.addCell(new Paragraph("COGNOME"));
        altri_occupanti.addCell(new Paragraph(nametab1));
        altri_occupanti.addCell(new Paragraph(surnametab1));
        altri_occupanti.addCell(new Paragraph(nametab2));
        altri_occupanti.addCell(new Paragraph(surnametab2));
        altri_occupanti.addCell(new Paragraph(nametab3));
        altri_occupanti.addCell(new Paragraph(surnametab3));
        altri_occupanti.addCell(new Paragraph(nametab4));
        altri_occupanti.addCell(new Paragraph(surnametab4));
        altri_occupanti.addCell(new Paragraph(nametab5));
        altri_occupanti.addCell(new Paragraph(surnametab5));
        altri_occupanti.addCell(new Paragraph(nametab6));
        altri_occupanti.addCell(new Paragraph(surnametab6));
        altri_occupanti.addCell(new Paragraph(nametab7));
        altri_occupanti.addCell(new Paragraph(surnametab7));
        document.add(altri_occupanti);

        if (check != null) {
          Paragraph p = new Paragraph(
              "\nche tra il/la Sig./ra " + name11 + " ed il/la Sig./ra " + name12
                  + " sussiste il seguente relationship di: " + relationship + "\n\n");
          document.add(p);
        } else {
          Paragraph p1 = new Paragraph(
              "\nche non sussiste nessun relationship di parent con le persoche che occupano gi� l'alloggio e quelle che vi sono trasferite\n\n");
          document.add(p1);
        }
      }
      //inserire le altri info in caso di abilitazione

      Paragraph sdata = new Paragraph("Data: " + gg + "/" + mm + "/" + year,
          new Font(Font.HELVETICA, 12, Font.NORMAL));
      sdata.setAlignment(Element.ALIGN_LEFT);
      document.add(sdata);

      Paragraph firma_dichiarante = new Paragraph("_________________\nFirma del dichiarante\n\n",
          new Font(Font.HELVETICA, 12, Font.NORMAL));
      firma_dichiarante.setAlignment(Element.ALIGN_RIGHT);
      document.add(firma_dichiarante);

      if (abilita_cod_doc != null) {
        PdfPTable tabella_estremi = new PdfPTable(1);
        tabella_estremi.setWidthPercentage(100);
        PdfPCell cella = new PdfPCell();
        Paragraph livello = new Paragraph("(1) " + cod_document);
        Paragraph livello2 = new Paragraph(
            "In questo spazio riportare gli estremi del document qualora la persona non sia conosciuta dall'ufficiale d'anagrafe.",
            new Font(Font.HELVETICA, 8));
        cella.addElement(livello);
        cella.addElement(livello2);
        tabella_estremi.addCell(cella);
        document.add(tabella_estremi);
      }

      Paragraph polizia = new Paragraph("\nAL COMANDO DI POLIZIA MUNICIPALE - SEDE",
          new Font(Font.HELVETICA, 12, Font.BOLD));
      polizia.setAlignment(Element.ALIGN_RIGHT);
      document.add(polizia);

      Paragraph alcomando_testo = new Paragraph(
          "\n          A norma dell'art. 4 della legge n. 1228 e dell'art. 18, 1� comma del regolamento di esecuzione (DPR 30 maggio 1989, n. 223), pregasi assumere,"
              + " tutte le informazioni riferite alle sopra elencate persone, rispondendo a tutte le domande indicate all' interno del presente foglio.\nDimora abituale dichiata in street "
              + streetAddress + " " + civicNumber1 + "\nPrecedente residence " + prec_res + "\n\n");
      alcomando_testo.setAlignment(Element.ALIGN_JUSTIFIED);
      document.add(alcomando_testo);
      document.add(sdata);

      Paragraph firma_impiegato = new Paragraph(
          "_________________\nL'ufficiale di anagrafe responsabile\n\n",
          new Font(Font.HELVETICA, 12, Font.NORMAL));
      firma_impiegato.setAlignment(Element.ALIGN_RIGHT);
      document.add(firma_impiegato);

      Paragraph ufficiale_anagrafe = new Paragraph("L'UFFICIALE DI ANAGRAFE",
          new Font(Font.HELVETICA, 12, Font.BOLD));
      ufficiale_anagrafe.setAlignment(Element.ALIGN_CENTER);
      document.add(ufficiale_anagrafe);

      Paragraph ufficiale_testo = new Paragraph(
          "Visto la relazione del Comando di Polizia Municipale\nA norma di: " + dati_vigile);
      document.add(ufficiale_testo);

      Paragraph disp = new Paragraph("DISPONE: " + disposes);
      document.add(disp);

      Paragraph testo_disp = new Paragraph("\n");
      testo_disp.setAlignment(Element.ALIGN_JUSTIFIED);
      document.add(testo_disp);
      document.add(sdata);
      document.add(firma_impiegato);

      Paragraph accertamento = new Paragraph("Accertamento del " + accert + "\n",
          new Font(Font.HELVETICA, 12, Font.BOLD));
      document.add(accertamento);
      Paragraph streetAddress_dic = new Paragraph(
          " - L'streetAddress dichiarato � esatto? " + streetAddress_esatto_d.toUpperCase() + "\n",
          new Font(Font.HELVETICA, 12, Font.COURIER));
      document.add(streetAddress_dic);
      if (streetAddress_esatto_d.equals("no")) {
        Paragraph streetAddress_es = new Paragraph(
            " - L'streetAddress esatto �: " + streetAddress_esatto.toUpperCase() + "\n");
        document.add(streetAddress_es);
      }

      Paragraph homeione_eff = new Paragraph(
          " - La persona o le persone sopra citate abitano effettivamente all'streetAddress dichiarato? "
              + streetAddress_esatto_d.toUpperCase() + "\n");
      document.add(homeione_eff);
      Paragraph alloggio_occ = new Paragraph(
          " - L'alloggio � occupato anche da altre persone residenti oltre quelle sopra citatate? "
              + alloggio_occupato.toUpperCase() + "\n");
      document.add(alloggio_occ);
      if (alloggio_occupato.equals("si")) {
        document.add(spazio);
        PdfPTable tabella_famiglia = new PdfPTable(3);
        tabella_famiglia.addCell(new Paragraph("COGNOME"));
        tabella_famiglia.addCell(new Paragraph("NOME"));
        tabella_famiglia.addCell(new Paragraph("RELAZIONE DI PARENTELA"));
        tabella_famiglia.addCell(new Paragraph(cp1));
        tabella_famiglia.addCell(new Paragraph(np1));
        tabella_famiglia.addCell(new Paragraph(pp1));
        tabella_famiglia.addCell(new Paragraph(cp2));
        tabella_famiglia.addCell(new Paragraph(np2));
        tabella_famiglia.addCell(new Paragraph(pp2));
        tabella_famiglia.addCell(new Paragraph(cp3));
        tabella_famiglia.addCell(new Paragraph(np3));
        tabella_famiglia.addCell(new Paragraph(pp3));
        tabella_famiglia.setWidthPercentage(100);
        document.add(tabella_famiglia);
      }

      Paragraph titolo = new Paragraph(" - L'alloggio � di propriet� di uno dei componenti? "
          + proprieta_componenti.toUpperCase() + "\n");
      document.add(titolo);
      if (proprieta_componenti.equals("no")) {
        Paragraph poss = new Paragraph(
            " - Titolo di possesso: " + titolo_di_possesso.toUpperCase() + "\n");
        document.add(poss);
      }

      Paragraph type_alloggio = new Paragraph(
          " - Type di alloggio: " + type_di_alloggio.toUpperCase()
              + "\n"); // inserire il type di alloggio
      document.add(type_alloggio);
      if (type_di_alloggio.equals("altro")) {
        Paragraph osservazioni_type = new Paragraph(
            " - Osservazioni sul type di alloggio: " + osser_homeione + "\n");
        document.add(osservazioni_type);
      }
      Paragraph motivo = new Paragraph(" - Motivo del trasferimento: " + mot.toUpperCase() + "\n");
      document.add(motivo);
      if (mot.equals("altro")) {
        Paragraph motivo_causa = new Paragraph(" > Causa: " + mot_causa.toUpperCase() + "\n\n");
        document.add(motivo_causa);
      }

      document.add(spazio);
      PdfPTable tabella_profession = new PdfPTable(2);
      tabella_profession.addCell(
          new Paragraph("Profession o condizione non professionale dei componenti"));
      tabella_profession.addCell(
          new Paragraph("Indicare il place di lavoro o l'streetAddress della scuola frequentata"));
      tabella_profession.addCell(new Paragraph(profession));
      tabella_profession.addCell(new Paragraph(ind_lavoro));
      tabella_profession.setWidthPercentage(100);
      document.add(tabella_profession);

      if (transfer.length() != 0) {
        Paragraph altro_coniuge = new Paragraph(
            " - Quando il trasferimento si riferisce ad un solo coniuge (con o senza familiari) indicare l'streetAddress: "
                + transfer.toUpperCase() + "\n");
        document.add(altro_coniuge);
      }
      Paragraph desume = new Paragraph(
          " - Dalle informazioni sopra indicate si desume che sussista la dimora abituale? "
              + circostanze.toUpperCase() + "\n");
      document.add(desume);
      Paragraph osservazioni_finali = new Paragraph(" - Osservazioni: " + oss_finali + "\n\n\n");
      document.add(osservazioni_finali);
      document.add(sdata);
      document.add(firma_dichiarante);

      document.close();
      fout.close();

      if (cntlr.equals("stesso")) {
        updateDB(indirnew, num_civnew);
      } else {
        deleteDB(request, response);
      }

    } catch (DocumentException e) {
      e.printStackTrace();
    }
  }

  private void updateDB(String indirnew, String num_civnew) {
    DbCambioResidence cr = new DbCambioResidence();
    IdentityCard ci = (IdentityCard) session.getAttribute("ci");
    cr.changeResidenceIn(ci.getNumber(), indirnew, Integer.parseInt(num_civnew));
  }

  private void deleteDB(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    AccessManager AM = new AccessManager();
    CitizenManager CM = new CitizenManager();
    Citizen c = (Citizen) session.getAttribute("c");

    if (AM.deleteAccess(c.getLogin()) && CM.deleteCitizen(
        c.getIdCitizen())) {  //delete il citizen e l'access
      //controllando che l'esito sia positivo
      FamilyManager NFM = new FamilyManager();
      NFM.getNCoreComponents(c.getFamily());
      String ris = "ok";
      ServletContext sc = getServletContext();
      request.setAttribute("ris", ris);
      RequestDispatcher rd = sc.getRequestDispatcher(
          "/workers/index.jsp?func=operazione&page=riuscita");
      rd.forward(request, response);
    }
  }
}
