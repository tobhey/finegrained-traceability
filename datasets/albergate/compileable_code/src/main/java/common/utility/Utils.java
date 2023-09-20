package interfacce.common.utility;

import interfacce.DefinizioneStagionalita;
import interfacce.MascheraRicercaStanza;
import moduli.Disponibilita;
import moduli.Etichetta;
import moduli.Immagine;
import moduli.ListaDisponibilita;

import java.awt.*;
import java.util.Date;

public class Utils {
  public static void constrain(Container panel1, Component message, int i, int i1, int i2, int i3, int none,
      int northwest, double v, double v1, int i4, int i5, int i6, int i7) {
  }

  public static void restoreCommissioni(Object numStanza) {

  }

  public static void aggiornaDisp(ListaDisponibilita elenco_disp, Object numStanza, Object inizioSogg, Object fineSogg, char disponibile, char bloccata, boolean b) {
  }

  public static boolean dateEPeriodoCorretti(DefinizioneStagionalita definizioneStagionalita, String text, String text1) {
    return false;
  }

  public static String scanVector(char altaStagione, char[] conf_stag_corr, int anno_corr) {
    return null;
  }

  public static boolean isIntPos(String text) {
    return false;
  }

  public static boolean isFloatPos(String text) {
    return false;
  }

  public static void constrain(Panel panel1, Label etich1, int i, int i1, int i2, int i3) {

  }

  public static void constrain(Panel panel1, Label etich2, int i, int i1, int i2, int i3, int i4,
      int i5, int i6, int i7) {

  }

  public static String completaStringa(Object descrizione, int i) {
    return null;
  }

  public static boolean data1MaggioreData2(Date fine_sogg, Date data_fine_stanza) {
    return false;
  }

  public static boolean data1MinoreData2(Date inizio_sogg, Date data_inizio_stanza) {
    return false;
  }

  public static String[] parseStanze(String text) {
    return new String[0];
  }

  public static Disponibilita getDispOfRoom(ListaDisponibilita elenco_disp, String text,
      boolean b) {
    return null;
  }

  public static void constrain(Panel panel2, TextField extra_add, int i, int i1, int i2, int i3, int i4,
      int i5, int i6, int i7) {
  }

  public static boolean dateEPeriodoCorretti(MascheraRicercaStanza mascheraRicercaStanza, String text, String text1) {
    return false;
  }

  public static void constrain(Panel panel0, Etichetta etich, int i, int i1, int i2, int i3, int vertical,
      int center, double v, double v1, int i4, int i5, int i6, int i7) {

  }

  public static void constrain(Panel panel0, Immagine figura, int i, int i1, int i2, int i3, int vertical,
      int center, double v, double v1, int i4, int i5, int i6, int i7) {
    
  }

  public static char[] scanVectorForDisp(Object dispAnnoCorr, int anno_corr, char[] stagAnnoCorr) {
    return new char[0];
  }
}
