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
 * The ServletCreatePdfCitizen class a PDF file based on the data received from a JSP page The class
 * has no dependencies
 *
 * @author Christian Ronca
 */

public class ServletCreatePdfCitizen extends HttpServlet {
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
      String name = request.getParameter("name").toUpperCase();
      String surname = request.getParameter("surname").toUpperCase();
      String name_citya = request.getParameter("citya").toUpperCase();
      String gg_date = request.getParameter("gg");
      String mm_date = request.getParameter("mm");
      String aa_date = request.getParameter("aa");
      String newmunicipality = request.getParameter("newmunicipality").toUpperCase();
      String indir = request.getParameter("street").toUpperCase();
      String number_civic = request.getParameter("civic");
      String indirnew = request.getParameter("newstreet").toUpperCase();
      String num_civnew = request.getParameter("num");
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
      String streetAddress = request.getParameter("streetAddress").toUpperCase();
      String civicNumber1 = request.getParameter("civicNumber1");
      String prec_res = request.getParameter("prec_res").toUpperCase();
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

      cntlr = "stesso";
      if (cntlr.equals("stesso")) {
        inout = "interno";
      } else {
        inout = "altro";
      }

      Document document = new Document(PageSize.A4);
      PdfWriter.getInstance(document, response.getOutputStream());
      FileOutputStream fout = new FileOutputStream(
          "webapps//myDocs//docs//" + now + "_2345_" + inout + ".pdf");
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

      Paragraph sdata = new Paragraph("Data: " + gg + "/" + mm + "/" + year,
          new Font(Font.HELVETICA, 12, Font.NORMAL));
      sdata.setAlignment(Element.ALIGN_LEFT);
      document.add(sdata);

      Paragraph firma_dichiarante = new Paragraph("_________________\nFirma del dichiarante\n\n",
          new Font(Font.HELVETICA, 12, Font.NORMAL));
      firma_dichiarante.setAlignment(Element.ALIGN_RIGHT);
      document.add(firma_dichiarante);

      document.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}