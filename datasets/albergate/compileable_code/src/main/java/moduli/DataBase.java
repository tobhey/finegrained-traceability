package moduli;

import java.util.Date;

public class DataBase {
  public static final int OK = 1;
  public static final int DONT_EXISTS_STANZA = 1;

  public static String strErrore(int j) {
    return null;
  }

  public static void visErrore(int j) {

  }

  public int writeConfigurazione(Configurazione this_config) {
    return 0;
  }

  public int startCambioValuta() {
    return 0;
  }

  public Configurazione readConfigurazione() {
    return null;
  }

  public int changeBeneServizio(String codExtra, String text, float floatValue) {
    return 0;
  }

  public int newIdBeneservizio() {
    return 0;
  }

  public int writeBeneServizio(BeneServizio extra) {
    return 0;
  }

  public ListaStanze elencoStanze() {
    return null;
  }

  public int writeStanza(Stanza room) {
    return 0;
  }

  public int delBeneServizio(String codExtra) {
    return 0;
  }

  public int changeStanza(String numStanza, int postiLetto, int dispLettoAgg, String pxBase,
      String descrizione) {
    return 0;
  }

  public Stanza readStanza(String text) {
    return null;
  }

  public int delStanza(String text) {
    return 0;
  }

  public ListaCommissionamenti foundCommissionamenti(String s) {
    return null;
  }

  public ListaDisponibilita elencoDisponibilita() {
    return null;
  }

  public void changeDisponibilita(Object numStanza, Object dispAnnoCorr, Object dispAnnoProx) {

  }

  public ListaBeniServizi elencoBeniServizi(String s) {
    return null;
  }

  public int delCommissionamento(Object idCommissionamento) {
    return 0;
  }

  public ListaCommissionamenti foundCommissionamentiSenzaLike(String text) {
    return null;
  }

  public int changeCommissionamento(Object idCommissionamento, String numStanza, String nomeAgenzia,
      String numTel, Date inizioComm, Date fineComm, Object scadenzaComm, Object scaduto) {
    return 0;
  }

  public int newIdPrenotazione() {
    return 0;
  }

  public int writePrenotazione(Prenotazione pren) {
    return 0;
  }

  public int newIdCommissionamento() {
    return 0;
  }

  public int writeCommissionamento(Commissionamento comm) {
    return 0;
  }

  public ListaPrenotazioni foundPrenotazioni(String text, String text1) {
    return null;
  }

  public int changeStanzaSoggiornanti(String text, String text1) {
    return 0;
  }

  public int changeStanzaAddebiti(String text, String text1) {
    return 0;
  }

  public int changeStanzaTelefonate(String text, String text1) {
    return 0;
  }

  public ListaSoggiornanti foundSoggiornanti(String text, boolean b) {
    return null;
  }

  public Soggiornante readSoggiornante(String text, int i) {
    return null;
  }

  public Prenotazione readPrenotazione(Object idPrenotazione) {
    return null;
  }

  public int writeAddebito(Addebito da_addebitare) {
    return 0;
  }

  public ListaClienti foundStorico(String text) {
    return null;
  }

  public ListaClienti foundStorico(String text, String text1) {
    return null;
  }

  public ListaClienti foundStorico(Date convertDate) {
    return null;
  }

  public Disponibilita readDisponibilita(String numStanza) {
    return null;
  }

  public ListaSoggiornanti foundSoggiornanti(String idPrenotazione) {
    return null;
  }

  public int changePrenotazione(String idPrenotazione, String numStanza, int numPers, String nome,
      String cognome, String numTel, Date inizioSogg, Date fineSogg, Date dataPren, String caparra,
      Object richLettoAgg, char pensionamento, int tramiteAgenzia, String nomeAgenzia,
      String richParticolari) {
    return 0;
  }

  public void delPrenotazione(long id) {

  }

  public int writeStorico(Object toCliente) {
    return 0;
  }

  public void delSoggiornante(String stanza, Object idSoggiornante) {

  }

  public int delAddebito(String stanza) {

    return 0;
  }

  public void delSuppRid(String stanza) {

  }

  public void delTelefonate(String stanza) {

  }

  public void anticipaFineSogg(String numStanza, Object idSoggiornante, Date fine_sogg_anticipato) {

  }

  public void reversePagato(String numStanza, Object idSoggiornante) {

  }

  public Soggiornante foundSoggiornante(String text, String text1, String text2) {
    return null;
  }

  public ListaBeniServizi menuRistorante() {
    return null;
  }

  public ListaBeniServizi elencoSenzaSuppRid() {
    return null;
  }

  public ListaAddebiti foundAddebiti(String text) {
    return null;
  }

  public BeneServizio readBeneServizio(Object codExtra) {
    return null;
  }

  public int addAddebito(String stanza, Object codExtra, int i) {
    return 0;
  }

  public int delAddebito(String stanza, Object codExtra) {
    return 0;
  }

  public ListaTelefonate foundTelefonate(String text) {
    return null;
  }

  public int writeSoggiornante(Soggiornante sogg) {
    return 0;
  }
}
