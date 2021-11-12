package smos.utility;

import smos.exception.EntitaNonTrovataEccezione;
import smos.storage.connectionManagement.DBConnessione;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.sql.*;
import java.text.DateFormat;

/**
 * Questa classe contiene un insieme di utility per la gestione del sistema.
 */
public class Utility {

	/**
	 * Costanti
	 */
	public static final char SLASH = (char) 47;// carattere '/' in ascii

	/**
	 * 
	 */
	public static final char BACKSLASH = (char) 92;// carattere '\' in ascii

	/**
	 * 
	 */
	public static final String[] giorno = { "lunedi", "martedi", "mercoledi",
			"giovedi", "venerdi", "sabato"};
	
	/**
	 * 
	 */
	public static final String[] oraValida = { "08:00", "08:30", "09:00",
		"09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30",
		"13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00",
		"16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30",
		"20:00" };
	
	public static final String[] mese = {"gen","feb","mar","apr","mag","giu","lug","ago","set","ott","nov","dic"};

	/**
	 * 
	 */
	public static final String[] richiestaStato = { "effettuata", "rifiutata",
			"accettata", "eliminata" };
	
	//Variabili da impostare all'avvio di Tomcat
	
	//Variabili per l'aspetto grafico del programma
	private static String imageHeaderPath = "";
	private static String imageHeaderLogoPath = "";
	private static String imageLeftColumn = "";
	private static String textFooter = "";
	//server smtp per invio e-mail
	private static String serverSmtp = "smtp.tele2.it";
	//Giorni minimi di distanza dall'esame per sottoscrivere o eliminare una prenotazione
	private static int needDayBeforeExam = 0;
	//Giorni massimi di distanza dall'esame per sottoscrivere una prenotazione
	private static int maxDayBeforeExam = 0;
	//pdf path per il percorso di memorizzazione dei file pdf
	private static String pdfPath = "";
	// upload path per il percorso di memorizzazione dei file per l'importazione dei dati
	private static String uploadPath = "";
	//Variabili per la connessione al database
	private static String driverMySql = "com.mysql.jdbc.Driver";
	private static String fullPathDatabase = "jdbc:mysql://localhost:3306/smos";
	private static String utenteNome = "root";
	private static String password = "";
	private static int dimensioneMassimaPool = 200;
	private static int waitTimeout = 120000;
	private static int attivoTimeout = 600000;
	private static int poolTimeout = 27000000;	
	
	

	/**
	 * Ritorna, dati un campo e una tabella, il valore massimo del campo nella
	 * tabella.
	 * 
	 * @param pField
	 *            Il campo di cui vogliamo recuperare il valore massimo.
	 * @param pTabello
	 *            La tabella in cui ricercare le informazioni richieste.
	 * @return Ritorna il valore massimo, del campo pField, passato come
	 *         parametro, presente nella tabella pTabello, passata come parametro.
	 * 
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 */
	synchronized static public int ottenereValoreMassimo(String pField, String pTabello)
			throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException {

		int value = 0;
		Connection connect = null;
		try {
			/*
			 * Se non sono stati forniti il campo e la tabella restituiamo un
			 * codice di errore
			 */
			if (pField.equals(""))
				throw new EntitaNonTrovataEccezione();
			if (pTabello.equals(""))
				throw new EntitaNonTrovataEccezione();

			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * richieste
			 */
			String tSql = "SELECT max(" + pField + ") as new_field FROM "
					+ pTabello;

			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);

			if (tRs.next())
				value = tRs.getInt("new_field");
			else
				throw new EntitaNonTrovataEccezione();

			return value;
		} finally {
			DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Converte una data in una stringa formattata per il database.
	 * 
	 * @param pData
	 *            La data da convertire.
	 * @return Ritorna la data formattata per il database.
	 */
	static public String date2SQLString(java.util.Date pData, boolean pOra) {
		
		TimeZone tz = TimeZone.getDefault();
		Calendar calendar = Calendar.getInstance(tz);
		calendar.setTime(pData);
		
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		
		String result = year + "-" + month + "-" + day ;
		
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		int sec = calendar.get(Calendar.SECOND);
		
		if (pOra){
			result = result + " " + hour + ":" + min + ":" + sec; 
		}
						

		return result;
	}

	/**
	 * Controlla se la stringa passata come parametro e' null.
	 * 
	 * @param pStr
	 *            La stringa da controllare.
	 * 
	 * @return <code>null</code> se la stringa e' null Altrimenti la stringa e'
	 *         passata al metodo <code>Replace</code>
	 * 
	 * @see #Replace
	 */
	static public String eNull(String pStr) {
		String tTmp;

		if (pStr == null) 
			tTmp = "null";

		else {
			pStr = sostituireTuttoStrings(pStr, "'", "\\'");
			pStr = sostituireTuttoStrings(pStr, "\"", "\\" + "\"");
			tTmp = "'" + pStr + "'";
		}
		return tTmp;
	}

	/**
	 * Controlla se un dato intero � null.
	 * 
	 * @param pInt
	 *            Il dato intero da controllare.
	 * @return Ritorna il dato in formato stringa se il dato � diverso da zero,
	 *         altrimenti la stringa null.
	 */
	static public String eNull(Integer pInt) {
		String tIntString;

		if (pInt == null)
			tIntString = "null";
		else
			tIntString = pInt.toString();
		return tIntString;
	}

	/**
	 * Controlla se una data e' null.
	 * 
	 * @param pData
	 *            la data da controllare.
	 * 
	 * @return Ritorna la stringa che rappresenta la data passata in input,
	 *         formattata per il database.
	 */
	static public String eNull(java.util.Date pData) {

		return "'" + date2SQLString(pData,false) + "'";
	}

	static public String chiarire(String pString){
		int start = 0;
		int start2 = 0;
		int end = 0;
		int end2 = 0;
		String result = "";
		String result2 = "";
			
		while (end >= 0){
		end = pString.indexOf("'", start);
		
		if (end >= 0){
		
			result= result +pString.substring(start, end);
			result = result + "\\'";
			start = end+1;
			
		} else 
			result = result + pString.substring(start);
		}
		
		while (end2 >= 0){
			end2 = result.indexOf("\n", start2);
			
			if (end2 >= 0){
				
				result2= result2 +result.substring(start2, end2-1);
				result2 = result2 + " ";
				start2 = end2+1;
				
			} else 
				result2 = result2 + result.substring(start2);
			}
		return(result2);	
	}
	
	/**
	 * Converte una String in Integer.
	 * 
	 * @param pStr
	 *            La String da convertire.
	 * 
	 * @return L'Integer contenuto nella String <code>0</code> se non �
	 *         possibile la conversione.
	 * 
	 */
	static public Integer String2Integer(String pStr) {
		Integer tInteger;

		if ((pStr == null) || (pStr.compareTo("") == 0))
			tInteger = new Integer(0);
		else
			try {
				tInteger = Integer.decode(pStr);
			} catch (Exception e) {
				tInteger = new Integer(0);
			}

		return tInteger;
	}

	/**
	 * Sostituisce i caratteri ' e \ con '' nella stringa passata come 
	 * parametro.
	 * 
	 * @param pStr
	 *            La stringa da trasformare.
	 * 
	 * @return La tringa trasformata.
	 */
	static public String sostituire(String pStr) {
		String tRis;

		tRis = pStr.replaceAll("\"", "'");

		tRis = tRis.replaceAll("'", "\\'");

		return tRis;
	}

	/**
	 * Sostituisce il carattere ' con la stringa " " nella stringa passata come
	 * parametro.
	 * 
	 * @param pStr
	 *            La stringa da trasformare.
	 * 
	 * @return La tringa trasformata.
	 */
	static public String sostituireVirgoletta(String pStr) {
		String tRis;

		tRis = pStr.replaceAll("'", " ");
		return tRis;
	}

	/**
	 * Esegue un'operazione sul database tramite una stringa SQL.
	 * 
	 * @param pConnessione
	 *            La connessione al database.
	 * @param pSql
	 *            La stringa SQL.
	 * 
	 * @return Il numero di record coinvolti nell'operazione.
	 * 
	 * @throws SQLException
	 */
	static public int eseguireOperazione(Connection pConnessione, String pSql)
			throws SQLException {
		Statement stmt = pConnessione.createStatement();
		int tResult = stmt.executeUpdate(pSql);
		stmt.close();
		return tResult;
	}

	/**
	 * Esegue una Query SQL sul database.
	 * 
	 * @param pConnect
	 *            La connessione al database.
	 * @param pSql
	 *            La stringa SQL.
	 * 
	 * @return Il numero di record coinvolti nell'operazione.
	 * 
	 * @throws SQLException
	 */
	static public ResultSet queryOperazione(Connection pConnect, String pSql)
			throws SQLException {

		Statement stmt = pConnect.createStatement();
		return stmt.executeQuery(pSql);
	}

	/**
	 * Converte un dato booleano in intero.
	 * 
	 * @param pBol
	 *            Il valore booleano da convertire in intero.
	 * 
	 * @return Il valore intero corrispondente al valore booleano passato come
	 *         parametro.
	 */
	static public int BooleanToInt(boolean pBol) {
		if (pBol == true)
			return 1;
		else
			return 0;
	}

	/**
	 * Converte un dato intero in booleano.
	 * 
	 * @param pInt
	 *            Il valore intero da convertire in booleano.
	 * 
	 * @return Il valore booleano corrispondente al valore intero passato come
	 *         parametro.
	 */
	static public boolean IntToBoolean(int pInt) {
		if (pInt == 1)
			return true;
		else
			return false;
	}

	/**
	 * Restituisce la data corrente.
	 * 
	 * @return La data corrente.
	 */
	static public java.util.Date oggi() {
		Calendar calendar = Calendar.getInstance();
		java.util.Date creationDate = calendar.getTime();
		return creationDate;
		// java.util.Date creationDate = new java.util.Date();
		// Timestamp timeStamp = new Timestamp(creationDate.getTime());
		// return (java.util.Date) timeStamp;
	}

	/**
	 * Restituisce la data contenuta nella stringa in input.
	 * 
	 * @param pData
	 *            La stringa di cui effettuare il parse in un data.
	 * 
	 * @return La data convertita.
	 */
	static public java.util.Date String2Date(String pData) {
		try {
			DateFormat dfDate = DateFormat.getDateInstance();
			java.util.Date tDate = dfDate.parse(pData,
					new java.text.ParsePosition(0));
			java.sql.Timestamp timeStamp = new java.sql.Timestamp(tDate
					.getTime());

			return (java.util.Date) timeStamp;
		} catch (Exception e) {
			try {
				DateFormat dfDate = DateFormat.getDateInstance(
						DateFormat.SHORT, java.util.Locale.ITALY);
				java.util.Date tDate = dfDate.parse(pData,
						new java.text.ParsePosition(0));
				java.sql.Timestamp timeStamp = new java.sql.Timestamp(tDate
						.getTime());

				return (java.util.Date) timeStamp;
			} catch (Exception e2) {
				return null;
			}
		}
	}

	/**
	 * Restituisce la data in input in formato String.
	 * 
	 * @param pData
	 *            La Data da convertire.
	 * @param pOra
	 * 
	 * @return La data convertita.
	 */
	static public String Date2String(java.util.Date pData, boolean pOra) {
		try {
			DateFormat dfDate = DateFormat.getDateInstance(DateFormat.SHORT);
			DateFormat dfTime = DateFormat.getTimeInstance(DateFormat.SHORT);
			if (pOra)
				return dfDate.format(pData) + " " + dfTime.format(pData);
			else
				return dfDate.format(pData);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @param origineStr
	 * @param cercare
	 * @param sostituirloCon
	 * @return la stringa corrispondente a searchBuffer
	 */
	public static String sostituireTutto(String origineStr, String cercare,
			String sostituirloCon) {
		StringBuffer searchBuffer = new StringBuffer(origineStr);
		int hits = 0;

		while (searchBuffer.toString().toUpperCase().indexOf(
				cercare.toUpperCase(), hits) >= 0) {
			int newIndex = searchBuffer.toString().toUpperCase().indexOf(
					cercare.toUpperCase(), hits);
			searchBuffer.replace(newIndex, newIndex + cercare.length(),
					sostituirloCon);
			hits++;
		}

		return searchBuffer.toString();
	}

	// La funzione precedente va in loop se sostituisco "\" con "\\"
	// usiamo allora due variabile la prima viene "consumata" ad ogni occorrenza
	// del pattern
	/**
	 * @param origineStr
	 * @param cercare
	 * @param sostituirloCon
	 * @return newStringBuffer
	 */
	public static String sostituireTuttoStrings(String origineStr, String cercare,
			String sostituirloCon) {
		StringBuffer searchBuffer = new StringBuffer(origineStr);
		StringBuffer newStringBuffer = new StringBuffer("");

		while (searchBuffer.toString().toUpperCase().indexOf(
				cercare.toUpperCase()) >= 0) {
			int newIndex = searchBuffer.toString().toUpperCase().indexOf(
					cercare.toUpperCase());
			newStringBuffer.append(searchBuffer.substring(0, newIndex));
			newStringBuffer.append(sostituirloCon);
			searchBuffer = new StringBuffer(searchBuffer.substring(newIndex
					+ cercare.length(), searchBuffer.length()));
		}

		newStringBuffer.append(searchBuffer);
		return newStringBuffer.toString();
	}

	/**
	 * @param origineStr
	 * @param eliminareStr
	 */
	public static void eliminareTutto(String origineStr, String eliminareStr) {
		int nextOccurence;

		while (origineStr.toString().toUpperCase().indexOf(
				eliminareStr.toUpperCase()) >= 0) {
			nextOccurence = origineStr.toString().toUpperCase().indexOf(
					eliminareStr.toUpperCase());
			origineStr = origineStr.substring(0, nextOccurence)
					+ origineStr.substring(nextOccurence + eliminareStr.length(),
							origineStr.length());
		}
	}
	
	
	/**
	 * Calcola la distanza in giorni tra 2 date passate.
	 */
	@SuppressWarnings("deprecation")
	public static int giornoTra(Date oggi, Date dataDiPrenotazione){
		int daysBetween = 0;
		long millisecBetween = 0;
		final int millisecInADay = 86400000;
		
		GregorianCalendar firstDate = new GregorianCalendar();
		GregorianCalendar secondDate = new GregorianCalendar();
		
		firstDate.set(oggi.getYear()+1900, oggi.getMonth(), oggi.getDate());
		secondDate.set(dataDiPrenotazione.getYear()+1900, dataDiPrenotazione.getMonth(), dataDiPrenotazione.getDate());
		
		millisecBetween = secondDate.getTimeInMillis() - firstDate.getTimeInMillis();
		daysBetween = (int) (millisecBetween/millisecInADay);
		
		return (daysBetween);
	}

	/**
	 * @return Il percorso impostato per i pdf.
	 */
	@SuppressWarnings("static-access")
	public static String ottenerePdfPath() {
		return Utility.pdfPath;
	}

	/**
	 * @param pPdfPath Il path da settare per i pdf.
	 */
	public static void settarePdfPath(String pPdfPath) {
		Utility.pdfPath = pPdfPath;
	}
	
	/**
	 * @return Il percorso impostato per i file utilizzati 
	 * durante l'importazione dei dati.
	 */
	@SuppressWarnings("static-access")
	public static String ottenereUploadPath() {
		return Utility.uploadPath;
	}

	/**
	 * @param pUploadPath Il path da settare per i file utilizzati 
	 * durante l'importazione dei dati.
	 */
	public static void settareUploadPath(String pUploadPath) {
		Utility.uploadPath = pUploadPath;
	}

	/**
	 * @return Il server smtp da utilizzare per l'invio
	 * 		   automatizzato delle e-mail.
	 */
	@SuppressWarnings("static-access")
	public static String ottenereServerSmtp() {
		return Utility.serverSmtp;
	}
	
	/**
	 * @param pServerSmtp il server smtp da settare.
	 */
	public static void settareServerSmtp(String pServerSmtp) {
		Utility.serverSmtp = pServerSmtp;
	}

	/**
	 * @return the activeTimeout
	 */
	public static int ottenereAttivoTimeout() {
		return Utility.attivoTimeout;
	}

	/**
	 * @param attivoTimeout the activeTimeout to set
	 */
	public static void settareAttivoTimeout(int pAttivoTimeout) {
		Utility.attivoTimeout = pAttivoTimeout;
	}

	/**
	 * @return the driverMySql
	 */
	public static String ottenereDriverMySql() {
		return Utility.driverMySql;
	}

	/**
	 * @param driverMySql the driverMySql to set
	 */
	public static void settareDriverMySql(String pDriverMySql) {
		Utility.driverMySql = pDriverMySql;
	}

	/**
	 * @return the fullPathDatabase
	 */
	public static String ottenereCompletoPathDatabase() {
		return Utility.fullPathDatabase;
	}

	/**
	 * @param fullPathDatabase the fullPathDatabase to set
	 */
	public static void settareCompletoPathDatabase(String pCompletoPathDatabase) {
		Utility.fullPathDatabase = pCompletoPathDatabase;
	}

	/**
	 * @return the maxPoolSize
	 */
	public static int ottenereDimensioneMassimaPool() {
		return Utility.dimensioneMassimaPool;
	}

	/**
	 * @param dimensioneMassimaPool the maxPoolSize to set
	 */
	public static void settareDimensioneMassimaPool(int pDimensioneMassimaPool) {
		Utility.dimensioneMassimaPool = pDimensioneMassimaPool;
	}

	/**
	 * @return the password
	 */
	public static String ottenerePassword() {
		return Utility.password;
	}

	/**
	 * @param password the password to set
	 */
	public static void settarePassword(String pPassword) {
		Utility.password = pPassword;
	}


	/**
	 * @return the poolTimeout
	 */
	public static int ottenerePoolTimeout() {
		return Utility.poolTimeout;
	}

	/**
	 * @param poolTimeout the poolTimeout to set
	 */
	public static void settarePoolTimeout(int pPoolTimeout) {
		Utility.poolTimeout = pPoolTimeout;
	}

	/**
	 * @return the userName
	 */
	public static String ottenereUtenteNome() {
		return Utility.utenteNome;
	}

	/**
	 * @param utenteNome the userName to set
	 */
	public static void settareUtenteNome(String pUtenteNome) {
		Utility.utenteNome = pUtenteNome;
	}

	/**
	 * @return the waitTimeout
	 */
	public static int ottenereAspettareTimeout() {
		return Utility.waitTimeout;
	}

	/**
	 * @param waitTimeout the waitTimeout to set
	 */
	public static void settareAspettareTimeout(int pAspettareTimeout) {
		Utility.waitTimeout = pAspettareTimeout;
	}

	/**
	 * @return the imageHeaderPath
	 */
	public static String ottenereImmagineHeaderPath() {
		return Utility.imageHeaderPath;
	}

	/**
	 * @param imageHeaderPath the imageHeaderPath to set
	 */
	public static void settareImmagineHeaderPath(String pImmagineHeaderPath) {
		Utility.imageHeaderPath = pImmagineHeaderPath;
	}

	/**
	 * @return the testoAPieDiPagina
	 */
	public static String ottenereTestoAPieDiPagina() {
		return Utility.textFooter;
	}

	/**
	 * @param testoAPieDiPagina the textFooter to set
	 */
	public static void settareTestoAPieDiPagina(String pTestoAPieDiPagina) {
		Utility.textFooter = pTestoAPieDiPagina;
	}

	/**
	 * @return the imageLeftColumn
	 */
	public static String ottenereImmagineColonnaASinistra() {
		return Utility.imageLeftColumn;
	}

	/**
	 * @param imageLeftColumn the imageLeftColumn to set
	 */
	public static void settareImmagineColonnaASinistra(String pImmagineColonnaASinistra) {
		Utility.imageLeftColumn = pImmagineColonnaASinistra;
	}
	
	public static String ottenereImmagineLogoHeaderPath() {
		return Utility.imageHeaderLogoPath;
	}

	public static void settareImmagineLogoHeaderPath(String pImmagineHeaderLogoPath) {
		Utility.imageHeaderLogoPath = pImmagineHeaderLogoPath;
	}

	/**
	 * @return the needDayBeforeExam
	 */
	public static int ottenereBisognoGiornoPrimaDellEsame() {
		return Utility.needDayBeforeExam;
	}

	/**
	 * @param needDayBeforeExam the needDayBeforeExam to set
	 */
	public static void settareBisognoGiornoPrimaDellEsame(int pBisognoGiornoPrimaDellEsame) {
		Utility.needDayBeforeExam = pBisognoGiornoPrimaDellEsame;
	}

	/**
	 * @return the maxDayBeforeExam
	 */
	public static int ottenereMassimoGiornoPrimaDellEsame() {
		return Utility.maxDayBeforeExam;
	}

	/**
	 * @param maxDayBeforeExam the maxDayBeforeExam to set
	 */
	public static void settareMassimoGiornoPrimaDellEsame(int pMassimoGiornoPrimaDellEsame) {
		Utility.maxDayBeforeExam = pMassimoGiornoPrimaDellEsame;
	}
	
	public static String ottenereDataEffettiva() {
		GregorianCalendar gc = new GregorianCalendar();

		String date="";
		int year=gc.get(GregorianCalendar.YEAR);
		
		int month=gc.get(GregorianCalendar.MONTH)+1;
		String months="";
		if(month<10){
			months="0"+month;
		}else{
			months= months+month;
		}
		
		int day=gc.get(GregorianCalendar.DAY_OF_MONTH);
		String days="";
		if(day<10){
			days="0"+day;
		}else{
			days= days+day;
		}
		
		date= date + days+"/"+months+"/"+year;
	
		return date;
		
	}
}
