package smos.storage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import smos.bean.Assenza;
import smos.bean.Ritardo;
import smos.bean.Giustificare;
import smos.bean.Nota;
import smos.bean.RegistratiLinea;
import smos.bean.VoceElencoUtenti;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.CampoObbligatorioEccezione;
import smos.storage.connectionManagement.DBConnessione;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
import smos.utility.Utility;
/**
 * Classe che gestisce il Registro Digitale
 * @author Nicola Pisanti
 * @version 1.0
 */
public class ManagerRegistrati {
	
	private static ManagerRegistrati instance;
	
	public final static String TABELLA_ASSENZA="absence";
	public final static String TABELLA_RITARDO="delay";
	public final static String TABELLAE_GIUSTIFICARE="justify";
	public final static String TABELLA_NOTA="note";
	
	
	private ManagerRegistrati(){
		super();
	}
	
	
	/**
	 * Ritorna la sola istanza della classe esistente.
	 * 
	 * @return Ritorna l'istanza della classe.
	 */
	public static synchronized ManagerRegistrati ottenereIstanza(){
		if(instance==null){
			instance = new ManagerRegistrati();
		}
		return instance;
	}
	
	/**
	 * Verifica se la classe data in input e nel database
	 * @param pAssenza
	 * 		La classe di cui bisogna verificare l'esistenza
	 * @return true se la classe e nel database, altrimenti false
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 */
	public synchronized boolean esiste(Assenza pAssenza) throws ConnessioneEccezione, SQLException {
		
		boolean result = false;
		Connection connect = null;

		try {
			//Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			String sql = "SELECT * FROM " 
				+ ManagerRegistrati.TABELLA_ASSENZA
				+ " WHERE id_absence = "
				+ Utility.eNull(pAssenza.ottenereIdAssenza());

			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, sql);

			if (tRs.next()){
				result = true;
			}
			
			return result;
			
		} finally {
			DBConnessione.rilasciareConnessione(connect);
		}
	}
	
	/**
	 * Verifica se la classe data in input e nel database
	 * @param pRitardo
	 * 		La classe di cui bisogna verificare l'esistenza
	 * @return true se la classe e nel database, altrimenti false
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 */
	public synchronized boolean esiste(Ritardo pRitardo) throws ConnessioneEccezione, SQLException {
		
		boolean result = false;
		Connection connect = null;

		try {
			//Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			String sql = "SELECT * FROM " 
				+ ManagerRegistrati.TABELLA_ASSENZA
				+ " WHERE id_delay = "
				+ Utility.eNull(pRitardo.ottenereIdRitardo());
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, sql);

			if (tRs.next())
				result = true;

			return result;
			
		} finally {
			DBConnessione.rilasciareConnessione(connect);
		}
	}
	/**
	 * Verifica se la classe data in input e nel database
	 * @param pRitardo
	 * 		La classe di cui bisogna verificare l'esistenza
	 * @return true se la classe e nel database, altrimenti false
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 */
	public synchronized boolean esiste(Giustificare pGiustificare) throws ConnessioneEccezione, SQLException {
		
		boolean result = false;
		Connection connect = null;

		try {
			//Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			String sql = "SELECT * FROM " 
				+ ManagerRegistrati.TABELLAE_GIUSTIFICARE
				+ " WHERE  id_justify = "
				+ Utility.eNull(pGiustificare.ottenereIdGiustificare());

			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, sql);

			if (tRs.next())
				result = true;

			return result;
			
		} finally {
			DBConnessione.rilasciareConnessione(connect);
		}
	}
	/**
	 * Inserisce un assenza nel database
	 * @param pAssenza
	 * 		un oggetto di tipo Absence da inserire nel database
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized void inserireAssenza(Assenza pAssenza) throws  
		ConnessioneEccezione, SQLException, EntitaNonTrovataEccezione, ValoreNonValidoEccezione{
		
		Connection connect= null;
		try{
			
			connect = DBConnessione.ottenereConnesione();
			if (connect==null)
				throw new ConnessioneEccezione();
			//Prepariamo la stringa Sql
			String sql =
				"INSERT INTO " 
				+ ManagerRegistrati.TABELLA_ASSENZA 
				+ " (id_user, date_absence, id_justify, accademic_year) " 
				+ "VALUES (" 
				+ Utility.eNull(pAssenza.ottenereIdUtente()) 
				+ "," 
				+ Utility.eNull(pAssenza.ottenereDataAssenza()) 
				+ "," 
				+ Utility.eNull(pAssenza.ottenereIdGiustificare()) 
				+ "," 
				+ Utility.eNull(pAssenza.ottenereAnnoAccademico())
				+ ")";
		
			Utility.eseguireOperazione(connect,sql);
		
			pAssenza.settareIdAssenza((Utility.ottenereValoreMassimo("id_absence",ManagerRegistrati.TABELLA_ASSENZA)));
		
		}finally {
		//rilascia le risorse
		
		DBConnessione.rilasciareConnessione(connect);
		}
	}
	

	/**
	 * Inserisce un ritardo nel database
	 * @param pRitardo
	 * 		un oggetto di tipo Delay da inserire nel database
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized void inserireRitardo(Ritardo pRitardo) throws  
		ConnessioneEccezione, SQLException, EntitaNonTrovataEccezione, ValoreNonValidoEccezione{
		
		Connection connect= null;
		try{
			
			connect = DBConnessione.ottenereConnesione();
			if (connect==null)
				throw new ConnessioneEccezione();
			//Prepariamo la stringa Sql
			String sql =
				"INSERT INTO " 
				+ ManagerRegistrati.TABELLA_RITARDO 
				+ " (id_user, date_delay, time_delay, accademic_year) " 
				+ "VALUES (" 
				+ Utility.eNull(pRitardo.ottenereIdUtente()) 
				+ "," 
				+ Utility.eNull(pRitardo.ottenereDataRitardo()) 
				+ "," 
				+ Utility.eNull(pRitardo.ottenereTempoRitardo()) 
				+ "," 
				+ Utility.eNull(pRitardo.ottenereAnnoAccademico())
				+ ")";
		
			Utility.eseguireOperazione(connect,sql);
		
			pRitardo.settareIdRitardo((Utility.ottenereValoreMassimo("id_delay",ManagerRegistrati.TABELLA_RITARDO)));
		
		}finally {
		//rilascia le risorse
		
		DBConnessione.rilasciareConnessione(connect);
		}
	}
	


	/**
	 * Inserisce una nota nel database
	 * @param pNota
	 * 		un oggetto di tipo Note da inserire nel database
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized void inserireNota(Nota pNota) throws CampoObbligatorioEccezione,  
		ConnessioneEccezione, SQLException, EntitaNonTrovataEccezione, ValoreNonValidoEccezione{
		
		Connection connect= null;
		try{
			if (pNota.ottenereDescrizione() == null || pNota.ottenereDescrizione().equals(""))
				throw new CampoObbligatorioEccezione("Inserire il testo della nota");
			
			if (pNota.ottenereInsegnante() == null || pNota.ottenereInsegnante().equals("") )
				throw new CampoObbligatorioEccezione("Inserire l'insegnante");
			
			connect = DBConnessione.ottenereConnesione();
			if (connect==null)
				throw new ConnessioneEccezione();
			//Prepariamo la stringa Sql
			String sql =
				"INSERT INTO " 
				+ ManagerRegistrati.TABELLA_NOTA 
				+ " (id_user, date_note, description, teacher, accademic_year) " 
				+ "VALUES (" 
				+ Utility.eNull(pNota.ottenereIdUtente()) 
				+ "," 
				+ Utility.eNull(pNota.ottenereDataNota()) 
				+ "," 
				+ Utility.eNull(pNota.ottenereDescrizione()) 
				+ "," 
				+ Utility.eNull(pNota.ottenereInsegnante()) 
				+ "," 
				+ Utility.eNull(pNota.ottenereAnnoAccademico())
				+ ")";
		
			Utility.eseguireOperazione(connect,sql);
		
			pNota.settareIdNota((Utility.ottenereValoreMassimo("id_note",ManagerRegistrati.TABELLA_NOTA)));
		
		}finally {
		//rilascia le risorse
		
		DBConnessione.rilasciareConnessione(connect);
		}
	}
	

	/**
	 * Inserisce una giustifica nel database
	 * @param pGiustificare 
	 * 		un oggetto di tipo Justify da inserire nel database
	 * @param pAssenza
	 * 		un oggetto di tipo Absence che rappresenta l'assenza giustificata
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized void inserireGiustificare(Giustificare pGiustificare, Assenza pAssenza) throws   
		ConnessioneEccezione, SQLException, EntitaNonTrovataEccezione, ValoreNonValidoEccezione{
		
		Connection connect= null;
		try{
			
			
			connect = DBConnessione.ottenereConnesione();
			if (connect==null)
				throw new ConnessioneEccezione();
			//Prepariamo la stringa Sql
			String sql =
				"INSERT INTO " 
				+ ManagerRegistrati.TABELLAE_GIUSTIFICARE 
				+ " (id_user, date_justify, accademic_year) " 
				+ "VALUES (" 
				+ Utility.eNull(pGiustificare.ottenereIdUtente()) 
				+ "," 
				+ Utility.eNull(pGiustificare.ottenereDataGiustificare()) 
				+ "," 
				+ Utility.eNull(pGiustificare.ottenereAnnoAccademico())
				+ ")";
		
			Utility.eseguireOperazione(connect,sql);
		
			pGiustificare.settareIdGiustificare((Utility.ottenereValoreMassimo("id_justify",ManagerRegistrati.TABELLAE_GIUSTIFICARE)));
			
			pAssenza.settareIdGiustificare(pGiustificare.ottenereIdGiustificare());
			this.aggiornareAssenza(pAssenza);
		
		}finally {
		//rilascia le risorse
		
		DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Cancella un'assenza dal database
	 * @param pAssenza
	 * 		l'assenza da cancellare
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized void eliminareAssenza (Assenza pAssenza) throws ConnessioneEccezione, 
			SQLException, EntitaNonTrovataEccezione, CampoObbligatorioEccezione, ValoreNonValidoEccezione {
		Connection connect = null;
		
		
		try {
			connect = DBConnessione.ottenereConnesione();
				//Prepariamo la stringa SQL
				String sql = "DELETE FROM " 
							+ ManagerRegistrati.TABELLA_ASSENZA 
							+ " WHERE id_absence = "
							+ Utility.eNull(pAssenza.ottenereIdAssenza());
			
				Utility.eseguireOperazione(connect, sql);
				
				if (!(pAssenza.ottenereIdGiustificare()==null)){
					eliminareGiustificare(pAssenza.ottenereIdGiustificare());
				}
		}finally {
			//rilascia le risorse
			DBConnessione.rilasciareConnessione(connect);
		}
	}


	/**
	 * Cancella un ritardo dal database
	 * @param pRitardo
	 * 		il ritardo da cancellare
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized void eliminareRitardo (Ritardo pRitardo) throws ConnessioneEccezione, 
			SQLException, EntitaNonTrovataEccezione, CampoObbligatorioEccezione, ValoreNonValidoEccezione {
		Connection connect = null;
		
		
		try {
			connect = DBConnessione.ottenereConnesione();
				//Prepariamo la stringa SQL
				String sql = "DELETE FROM " 
							+ ManagerRegistrati.TABELLA_RITARDO 
							+ " WHERE id_delay = "
							+ Utility.eNull(pRitardo.ottenereIdRitardo());
			
				Utility.eseguireOperazione(connect, sql);
		}finally {
			//rilascia le risorse
			DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Cancella una nota dal database
	 * @param pNota
	 * 		la nota da cancellare
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized void eliminareNota (Nota pNota) throws ConnessioneEccezione, 
			SQLException, EntitaNonTrovataEccezione, ValoreNonValidoEccezione {
		Connection connect = null;
		
		
		try {
			connect = DBConnessione.ottenereConnesione();
				//Prepariamo la stringa SQL
				String sql = "DELETE FROM " 
							+ ManagerRegistrati.TABELLA_NOTA 
							+ " WHERE id_note = "
							+ Utility.eNull(pNota.ottenereIdNota());
			
				Utility.eseguireOperazione(connect, sql);
		}finally {
			//rilascia le risorse
			DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Cancella una nota dal database
	 * @param pJIDustify
	 * 		l'ID della nota da cancellare
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized void eliminareGiustificare (int pIDGiustificare) throws ConnessioneEccezione, 
			SQLException, EntitaNonTrovataEccezione, CampoObbligatorioEccezione, ValoreNonValidoEccezione {
		Connection connect = null;
		
		
		try {
			connect = DBConnessione.ottenereConnesione();
				//Prepariamo la stringa SQL
				String sql = "DELETE FROM " 
							+ ManagerRegistrati.TABELLAE_GIUSTIFICARE 
							+ " WHERE id_justify = "
							+ Utility.eNull(pIDGiustificare);
			
				Utility.eseguireOperazione(connect, sql);
				
				try{
					Assenza temp= ottenereAssenzaPerIdGiustificare(pIDGiustificare);
					temp.settareIdGiustificare(0);
					aggiornareAssenza(temp);
				}catch(Exception e){
					// e normale se un exception viene generata
					// dato che puo essere che stiamo cancellando una giustifica
					//di cui abbiamo appena cancellato l'assenza 
				}
				
				
		}finally {
			//rilascia le risorse
			DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Aggiorna le statistiche di un'assenza
	 * @param pAssenza
	 * 		L'assenza con le statistiche aggiornate (ma ID identico)
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws CampoObbligatorioEccezione
	 */
	public synchronized void aggiornareAssenza (Assenza pAssenza) throws ConnessioneEccezione,
	SQLException, EntitaNonTrovataEccezione{
		Connection connect= null;
		
		try{

			
			//Prepariamo la stringa SQL
			String sql=
				"UPDATE " 
				+	ManagerRegistrati.TABELLA_ASSENZA 
				+ " SET" 
				+ " id_user = " 
				+ Utility.eNull(pAssenza.ottenereIdUtente()) 
				+ ", date_absence = " 
				+ Utility.eNull(pAssenza.ottenereDataAssenza()) 
				+ ", id_justify = " 
				+ Utility.eNull(pAssenza.ottenereIdGiustificare())  
				+ ", accademic_year = " 
				+ Utility.eNull(pAssenza.ottenereAnnoAccademico())  
				+ " WHERE id_absence = " 
				+ Utility.eNull(pAssenza.ottenereIdAssenza());
			
			//effettua una nuova connessione e invia la query
			connect = DBConnessione.ottenereConnesione();
			if (connect==null)
				throw new ConnessioneEccezione();
			
			Utility.eseguireOperazione(connect, sql);
		}finally {
		//rilascia le risorse
		DBConnessione.rilasciareConnessione(connect);
		}
	}
	/**
	 * Aggiorna le statistiche di un ritardo
	 * @param pRitardo
	 * 		Il ritardo con le statistiche aggiornate (ma ID identico)
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws CampoObbligatorioEccezione
	 */
	public synchronized void aggiornareRitardo (Ritardo pRitardo) throws ConnessioneEccezione,
	SQLException, EntitaNonTrovataEccezione, CampoObbligatorioEccezione{
		Connection connect= null;
		
		try{

			
			//Prepariamo la stringa SQL
			String sql=
				"UPDATE " 
				+	ManagerRegistrati.TABELLA_RITARDO 
				+ " SET" 
				+ " id_user = " 
				+ Utility.eNull(pRitardo.ottenereIdUtente()) 
				+ ", date_delay = " 
				+ Utility.eNull(pRitardo.ottenereDataRitardo()) 
				+ ", time_delay = " 
				+ Utility.eNull(pRitardo.ottenereTempoRitardo())  
				+ ", accademic_year = " 
				+ Utility.eNull(pRitardo.ottenereAnnoAccademico())  
				+ " WHERE id_delay = " 
				+ Utility.eNull(pRitardo.ottenereIdRitardo());
			
			//effettua una nuova connessione e invia la query
			connect = DBConnessione.ottenereConnesione();
			if (connect==null)
				throw new ConnessioneEccezione();
			
			Utility.eseguireOperazione(connect, sql);
		}finally {
		//rilascia le risorse
		DBConnessione.rilasciareConnessione(connect);
		}
	}
	
	/**
	 * Metodo che aggiorna le statistiche di una Nota
	 * @param pNota
	 * 		un oggetto di tipo Note con le statistiche aggiornate ma id identico
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws CampoObbligatorioEccezione
	 */
	public synchronized void aggiornareNota (Nota pNota) throws ConnessioneEccezione,
	SQLException, EntitaNonTrovataEccezione, CampoObbligatorioEccezione{
		Connection connect= null;
		
		try{
			if (pNota.ottenereDescrizione() == null || pNota.ottenereDescrizione().equals(""))
				throw new CampoObbligatorioEccezione("Inserire il testo della nota");
			
			if (pNota.ottenereInsegnante() == null || pNota.ottenereInsegnante().equals("") )
				throw new CampoObbligatorioEccezione("Inserire l'insegnante");
			//Prepariamo la stringa SQL
			String sql=
				"UPDATE " 
				+	ManagerRegistrati.TABELLA_NOTA
				+ " SET" 
				+ " id_user = " 
				+ Utility.eNull(pNota.ottenereIdUtente()) 
				+ ", date_note = " 
				+ Utility.eNull(pNota.ottenereDataNota())   
				+ ", description = " 
				+ Utility.eNull(pNota.ottenereDescrizione())   
				+ ", teacher = " 
				+ Utility.eNull(pNota.ottenereInsegnante())   
				+ ", accademic_year = " 
				+ Utility.eNull(pNota.ottenereAnnoAccademico())  
				+ " WHERE id_note = " 
				+ Utility.eNull(pNota.ottenereIdNota());
			
			//effettua una nuova connessione e invia la query
			connect = DBConnessione.ottenereConnesione();
			if (connect==null)
				throw new ConnessioneEccezione();
			
			Utility.eseguireOperazione(connect, sql);
		}finally {
		//rilascia le risorse
		DBConnessione.rilasciareConnessione(connect);
		}
	}
	
	/**
	 * Aggiorna le statistiche di una giustifica 
	 * @param pGiustificare
	 * 		la giustifica con le statistiche aggiornate (ma ID identico)
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws CampoObbligatorioEccezione
	 */
	public synchronized void aggiornareGiustificare (Giustificare pGiustificare) throws ConnessioneEccezione,
	SQLException, EntitaNonTrovataEccezione, CampoObbligatorioEccezione{
		Connection connect= null;
		
		try{

			
			//Prepariamo la stringa SQL
			String sql=
				"UPDATE " 
				+	ManagerRegistrati.TABELLAE_GIUSTIFICARE
				+ " SET" 
				+ " id_user = " 
				+ Utility.eNull(pGiustificare.ottenereIdUtente()) 
				+ ", date_justify = " 
				+ Utility.eNull(pGiustificare.ottenereDataGiustificare())   
				+ ", accademic_year = " 
				+ Utility.eNull(pGiustificare.ottenereAnnoAccademico())  
				+ " WHERE id_justify = " 
				+ Utility.eNull(pGiustificare.ottenereIdGiustificare());
			
			//effettua una nuova connessione e invia la query
			connect = DBConnessione.ottenereConnesione();
			if (connect==null)
				throw new ConnessioneEccezione();
			
			Utility.eseguireOperazione(connect, sql);
		}finally {
		//rilascia le risorse
		DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Metodo che restituisce una nota dato l'id della note stessa
	 * @param pIDGiustificare
	 * 		un intero che rappresenta l'id della nota  
	 * @return un oggetto di tipo Note che rappresenta la nota
	 * @throws ValoreNonValidoEccezione
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 */
	public synchronized Nota ottenereNotaPerId( int pIDNota)throws ValoreNonValidoEccezione,
			EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException{
		Nota result=null;
		Connection connect = null;
		try
		{
			// Se non e stato fornito l'id restituiamo un codice di errore
			if (pIDNota<=0)
				throw new EntitaNonTrovataEccezione("Impossibile trovare la nota");
			
			
			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti alla classe dell'id passato
			 */
			String tSql = 
				"SELECT * FROM " 
				+ ManagerRegistrati.TABELLA_NOTA 
				+ " WHERE id_note = " 
				+ Utility.eNull(pIDNota) ;
			
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null) 
				throw new ConnessioneEccezione();
			
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);
			
			if (tRs.next()) 
				result = this.loadNoteFromRs(tRs);
			else 
				throw new EntitaNonTrovataEccezione("Impossibile trovare la nota!");
			
			return result;
		}finally{
			DBConnessione.rilasciareConnessione(connect);
		}
	}	
	
	
	
	
	
	
	
	/**
	 * Metodo che restituisce un assenza dato l'id della giustifca associata a tale assenza
	 * @param pIDGiustificare
	 * 		un intero che rappresenta l'id della giustifica  
	 * @return un oggetto di tipo Absence che rappresenta l'assenza giustificata
	 * @throws ValoreNonValidoEccezione
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 */
	public synchronized Assenza ottenereAssenzaPerIdGiustificare( int pIDGiustificare)throws ValoreNonValidoEccezione,
			EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException{
		Assenza result=null;
		Connection connect = null;
		try
		{
			// Se non e stato fornito l'id restituiamo un codice di errore
			if (pIDGiustificare<=0)
				throw new EntitaNonTrovataEccezione("Impossibile trovare la l'assenza");
			
			
			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti alla classe dell'id passato
			 */
			String tSql = 
				"SELECT * FROM " 
				+ ManagerRegistrati.TABELLA_ASSENZA 
				+ " WHERE id_justify = " 
				+ Utility.eNull(pIDGiustificare) ;
			
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null) 
				throw new ConnessioneEccezione();
			
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);
			
			if (tRs.next()) 
				result = this.loadAbsenceFromRs(tRs);
			else 
				throw new EntitaNonTrovataEccezione("Impossibile trovare l'assenza!");
			
			return result;
		}finally{
			DBConnessione.rilasciareConnessione(connect);
		}
	}
	/**
	 * Metodo che restituisce un assenza dato l'id di questa
	 * @param pIDAssenza
	 * 		un intero che rappresenta l'id dell'assenza  
	 * @return un oggetto di tipo Absence che rappresenta l'assenza 
	 * @throws ValoreNonValidoEccezione
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 */
	public synchronized Assenza ottenereAssenzaPerIdAssenza( int pIDAssenza)throws ValoreNonValidoEccezione,
			EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException{
		Assenza result=null;
		Connection connect = null;
		try
		{
			// Se non e stato fornito l'id restituiamo un codice di errore
			if (pIDAssenza<=0)
				throw new EntitaNonTrovataEccezione("Impossibile trovare l' assenza");
			
			
			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti alla classe dell'id passato
			 */
			String tSql = 
				"SELECT * FROM " 
				+ ManagerRegistrati.TABELLA_ASSENZA 
				+ " WHERE id_absence = " 
				+ Utility.eNull(pIDAssenza) ;
			
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null) 
				throw new ConnessioneEccezione();
			
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);
			
			if (tRs.next()) 
				result = this.loadAbsenceFromRs(tRs);
			else 
				throw new EntitaNonTrovataEccezione("Impossibile trovare l'assenza!");
			
			return result;
		}finally{
			DBConnessione.rilasciareConnessione(connect);
		}
	}
	
	
	/**
	 * Metodo che restituisce un ritardo dato l'id di questo
	 * @param pIDRitardo
	 * 		un intero che rappresenta l'id del ritardo  
	 * @return un oggetto di tipo Delay che rappresenta il ritardo
	 * @throws ValoreNonValidoEccezione
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 */
	public synchronized Ritardo ottenereRitardoPerId( int pIDRitardo)throws ValoreNonValidoEccezione,
			EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException{
		Ritardo result=null;
		Connection connect = null;
		try
		{
			// Se non e stato fornito l'id restituiamo un codice di errore
			if (pIDRitardo<=0)
				throw new EntitaNonTrovataEccezione("Impossibile trovare il ritardo");
			
			
			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti alla classe dell'id passato
			 */
			String tSql = 
				"SELECT * FROM " 
				+ ManagerRegistrati.TABELLA_RITARDO
				+ " WHERE id_delay = " 
				+ Utility.eNull(pIDRitardo) ;
			
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null) 
				throw new ConnessioneEccezione();
			
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);
			
			if (tRs.next()) 
				result = this.loadDelayFromRs(tRs);
			else 
				throw new EntitaNonTrovataEccezione("Impossibile trovare l'assenza!");
			
			return result;
		}finally{
			DBConnessione.rilasciareConnessione(connect);
		}
	}
	/**
	 * Metodo che restituisce un assenza dato l'id di questa
	 * @param pIDAssenza
	 * 		un intero che rappresenta l'id dell'assenza  
	 * @return un oggetto di tipo Absence che rappresenta l'assenza 
	 * @throws ValoreNonValidoEccezione
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 */
	public synchronized Giustificare ottenereGiustificarePerIdGiustificare( int pIDGiustificare)throws ValoreNonValidoEccezione,
			EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException{
		Giustificare result=null;
		Connection connect = null;
		try
		{
			// Se non e stato fornito l'id restituiamo un codice di errore
			if (pIDGiustificare<=0)
				throw new EntitaNonTrovataEccezione("Impossibile trovare la giustifica");
			
			
			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti alla classe dell'id passato
			 */
			String tSql = 
				"SELECT * FROM " 
				+ ManagerRegistrati.TABELLAE_GIUSTIFICARE 
				+ " WHERE id_justify = " 
				+ Utility.eNull(pIDGiustificare) ;
			
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null) 
				throw new ConnessioneEccezione();
			
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);
			
			if (tRs.next()) 
				
				result= this.loadJustifyFromRs(tRs);
			else 
				throw new EntitaNonTrovataEccezione("Impossibile trovare la giustifica!");
			
			return result;
		}finally{
			DBConnessione.rilasciareConnessione(connect);
		}
	}
	/**
	 * Metodo che restituisce true se l'assenza data in input ha una giustifica assegnata
	 * @param pAssenza
	 * 		un oggetto di valore Absence di cui bisogna controllare se ha giustifica
	 * @return true se l'assenza e giustificata, false altrimenti
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 */
	public synchronized boolean avereGiustificare(Assenza pAssenza)throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException{
		if(!esiste(pAssenza)) throw new EntitaNonTrovataEccezione("Assenza non presente in database");
		if(pAssenza.ottenereIdGiustificare()==null) return false;
		return true;
	}
	
	/**
	 * Metodo che restituisce la giustifica legata a una data assenza
	 * @param pAssenza
	 * 		un oggetto di tipo Absence che rappresenta l'assenza
	 * @return	un oggetto di tipo Justify, oppure null se l'assenza non ha giustifica
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized Giustificare ottenereGiustificarePerAssenza(Assenza pAssenza)throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione{
		if(!esiste(pAssenza)) throw new EntitaNonTrovataEccezione("Assenza non presente in database");
		if(pAssenza.ottenereIdGiustificare()==null) return null;
		
		Giustificare result=null;
		Connection connect = null;
		try{
			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti alla classe dell'id passato
			 */
			String tSql = 
				"SELECT * FROM " 
				+ ManagerRegistrati.TABELLAE_GIUSTIFICARE 
				+ " WHERE id_justify = " 
				+ Utility.eNull(pAssenza.ottenereIdGiustificare()) ;
			
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null) 
				throw new ConnessioneEccezione();
			
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);
			
			if (tRs.next()) 
				result = this.loadJustifyFromRs(tRs);
			else 
				throw new EntitaNonTrovataEccezione("Impossibile trovare la giustifica!");
			
			return result;
		}finally{
			DBConnessione.rilasciareConnessione(connect);
		}
		
		
	}

	/**
	 * Metodo che restituisce le assenze preso un dato anno scolastico e utente in input
	 * @param pIdUtente
	 * 		un intero che rappresenta l'id dell'utente
	 * @param pAnnoAccademico
	 * 		un intero che rappresenta l'anno accademico 
	 * @return una colleczione di assenze (vuota se l'utente non ha avuto assenze ) 
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized Collection<Assenza> ottenereAssenzaPerIDUtenteEAnnoAccademico(int pIdUtente, int pAnnoAccademico) throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione{
		Collection<Assenza> result=new Vector<Assenza>();
		Connection connect = null;
		try
		{
			// Se non e stato fornito l'id restituiamo un codice di errore
			if (pAnnoAccademico <= 1970)
				throw new EntitaNonTrovataEccezione("Data troppo vecchia");

			// idem per l'id user
			if (pIdUtente<=0)
				throw new EntitaNonTrovataEccezione("Utente non trovato");
			
			
			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti alla classe dell'id passato
			 */
			String tSql = 
				"SELECT * FROM " 
				+ ManagerRegistrati.TABELLA_ASSENZA 
				+ " WHERE accademic_year = " 
				+ Utility.eNull(pAnnoAccademico) 
				+ " AND id_user = "
				+ Utility.eNull(pIdUtente);
			
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null) 
				throw new ConnessioneEccezione();
			
			
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);
			
			while(tRs.next())  {
				result.add(loadAbsenceFromRs(tRs));
			} 
				
			return result;
		}finally{
			DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Metodo che restituisce una collezione di note per un dato utente ed un dato anno scolastico
	 * @param pIdUtente
	 * 		un intero che rappresenta l'id dell'utente
	 * @param pAnnoAccademico
	 * 		un intero che rappresenta l'anno accademico 
	 * @return una collezione di note, vuota se l'utente non ne ha ricevute
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized Collection<Nota> ottenereNotaPerIDUtenteEAnnoAccademico(int pIdUtente, int pAnnoAccademico) throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione{
		Collection<Nota> result=new Vector<Nota>();
		Connection connect = null;
		try
		{
			// Se non e stato fornito l'id restituiamo un codice di errore
			if (pAnnoAccademico <= 1970)
				throw new EntitaNonTrovataEccezione("Data troppo vecchia");

			// idem per l'id user
			if (pIdUtente<=0)
				throw new EntitaNonTrovataEccezione("Utente non trovato");
			
			
			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti alla classe dell'id passato
			 */
			String tSql = 
				"SELECT * FROM " 
				+ ManagerRegistrati.TABELLA_NOTA 
				+ " WHERE accademic_year = " 
				+ Utility.eNull(pAnnoAccademico) 
				+ " AND id_user = "
				+ Utility.eNull(pIdUtente);
			
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null) 
				throw new ConnessioneEccezione();
			
			
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);
			
			while(tRs.next())  {
				result.add(loadNoteFromRs(tRs));
			} 
				
			return result;
		}finally{
			DBConnessione.rilasciareConnessione(connect);
		}
	}
	
	/**
	 * Metodo che restituisce l'assenza di una dato studente in un dato giorno 
	 * @param pIdUtente
	 * 		un intero che rappresenta l'id dello studente
	 * @param pData
	 * 		una stringa che rappresenta la data formattata per il database
	 * @return un oggetto di tipo Absence, oppure null se lo studente era presente
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized Assenza ottenereAssenzaPerIDUtenteEData(int pIdUtente, Date pData) throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione{
		Assenza result=new Assenza();
		Connection connect = null;
		try
		{
			//TODO controlli sulla formattazione della stringa
			
			
			// idem per l'id user
			if (pIdUtente<=0)
				throw new EntitaNonTrovataEccezione("Utente non trovato");
			
			
			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti alla classe dell'id passato
			 */
			String tSql = 
				"SELECT * FROM " 
				+ ManagerRegistrati.TABELLA_ASSENZA 
				+ " WHERE date_absence = " 
				+ Utility.eNull(pData) 
				+ " AND id_user = "
				+ Utility.eNull(pIdUtente);
			
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null) 
				throw new ConnessioneEccezione();
			
			
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);
			
			if(tRs.next())  {
				result=loadAbsenceFromRs(tRs);
			}else {
				result=null;
			}
				
			return result;
		}finally{
			DBConnessione.rilasciareConnessione(connect);
		}
	}
	
	
	/**
	 * Metodo che restituisce il ritardo di una dato studente in un dato giorno 
	 * @param pIdUtente
	 * 		un intero che rappresenta l'id dello studente
	 * @param pData
	 * 		una stringa che rappresenta la data formattata per il database
	 * @return un oggetto di tipo Delay, oppure null se lo studente era in orario o assente
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized Ritardo ottenereRitardoPerIDUtenteEData(int pIdUtente, Date pData) throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione{
		Ritardo result=new Ritardo();
		Connection connect = null;
		try
		{
			//TODO controlli sulla formattazione della stringa
			
			
			// idem per l'id user
			if (pIdUtente<=0)
				throw new EntitaNonTrovataEccezione("Utente non trovato");
			
			
			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti alla classe dell'id passato
			 */
			String tSql = 
				"SELECT * FROM " 
				+ ManagerRegistrati.TABELLA_RITARDO 
				+ " WHERE date_delay = " 
				+ Utility.eNull(pData) 
				+ " AND id_user = "
				+ Utility.eNull(pIdUtente);
			
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null) 
				throw new ConnessioneEccezione();
			
			
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);
			
			if(tRs.next())  {
				result=loadDelayFromRs(tRs);
			}else {
				result=null;
			}
				
			return result;
		}finally{
			DBConnessione.rilasciareConnessione(connect);
		}
	}
	
	
	public synchronized Collection<RegistratiLinea> ottenereRegistratiPerClasseIDEData(int pClasseID, Date pData) throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione{
		
		Collection<RegistratiLinea> result = new Vector<RegistratiLinea>();
		ManagerUtente mg = ManagerUtente.ottenereIstanza();
		
		Collection<VoceElencoUtenti> students = mg.ottenereStudentePerClasseId(pClasseID);
		
		
		for (VoceElencoUtenti x : students){
			RegistratiLinea temp = new RegistratiLinea();
			temp.settareStudente(x);
			temp.settareAssenza(this.ottenereAssenzaPerIDUtenteEData(x.ottenereId(), pData));
			temp.settareRitardo(this.ottenereRitardoPerIDUtenteEData(x.ottenereId(), pData));
			result.add(temp);
		}
		
		return result;
	}

	/**
	 * Metodo che verifica se c'e un'assenza in una linea del registro 
	 * @param pRegistratiLinea
	 * 		un oggetto di tipo RegisterLine
	 * @return	true se c'e un'assenza nella linea di registro passata, altrimenti false
	 */
	public boolean avereAssenza(RegistratiLinea pRegistratiLinea){
		if(pRegistratiLinea.ottenereAssenza()==null)return false;
		return true;
	}

	/**
	 * Metodo che verifica se c'e un ritardo in una linea del registro 
	 * @param pRegistratiLinea
	 * 		un oggetto di tipo RegisterLine
	 * @return	true se c'e un ritardo nella linea di registro passata, altrimenti false
	 */
	public boolean avereRitardo(RegistratiLinea pRegistratiLinea){
		if(pRegistratiLinea.ottenereRitardo()==null)return false;
		return true;
	}

	/**
	 * Consente la lettura di un solo record dal Result Set
	 * @param pRs
	 * 		Il result set da cui estrarre l'oggetto Absence
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	private Assenza loadAbsenceFromRs(ResultSet pRs) throws SQLException, ValoreNonValidoEccezione{
		Assenza absence = new Assenza();
		
		absence.settareIdAssenza(pRs.getInt("id_absence"));
		absence.settareIdUtente(pRs.getInt("id_user"));
		absence.settareDataAssenza((Date)pRs.getDate("date_absence"));
		absence.settareIdGiustificare(pRs.getInt("id_justify"));
		absence.settareAnnoAccademico(pRs.getInt("accademic_year"));
		
		return absence;
	}
	
	/**
	 * Consente la lettura di un solo record dal Result Set
	 * @param pRs
	 * 		Il result set da cui estrarre l'oggetto Justify
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	private Giustificare loadJustifyFromRs(ResultSet pRs) throws SQLException, ValoreNonValidoEccezione{
		Giustificare justify = new Giustificare();
		
		justify.settareIdGiustificare(pRs.getInt("id_justify"));
		justify.settareIdUtente(pRs.getInt("id_user"));
		justify.settareDataGiustificare((Date)pRs.getDate("date_justify"));
		justify.settareAnnoAccademico(pRs.getInt("accademic_year"));
		
		return justify;
	}
	/**
	 * Consente la lettura di un solo record dal Result Set
	 * @param pRs
	 * 		Il result set da cui estrarre l'oggetto Note
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	
	private Nota loadNoteFromRs(ResultSet pRs) throws SQLException, ValoreNonValidoEccezione{
		Nota note= new Nota();
		
		note.settareIdNota(pRs.getInt("id_note"));
		note.settareIdUtente(pRs.getInt("id_user"));
		note.settareDataNota((Date)pRs.getDate("date_note"));
		note.settareDescrizione(pRs.getString("description"));
		note.settareInsegnante(pRs.getString("teacher"));	
		note.settareAnnoAccademico(pRs.getInt("accademic_year"));
	
		return note;
	}

	/**
	 * Consente la lettura di un solo record dal Result Set
	 * @param pRs
	 * 		Il result set da cui estrarre l'oggetto Delay
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	private Ritardo loadDelayFromRs(ResultSet pRs) throws SQLException, ValoreNonValidoEccezione{
		Ritardo delay = new Ritardo();
				
		delay.settareIdRitardo(pRs.getInt("id_delay"));
		delay.settareIdUtente(pRs.getInt("id_user"));
		delay.settareDataRitardo((Date)pRs.getDate("date_delay"));
		delay.settareTempoRitardo(pRs.getString("time_delay"));
		delay.settareAnnoAccademico(pRs.getInt("accademic_year"));
	
		return delay;
	}
}
