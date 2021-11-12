package smos.storage;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import smos.bean.Classe;
import smos.bean.Utente;

import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.CampoObbligatorioEccezione;
import smos.storage.connectionManagement.DBConnessione;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
import smos.utility.Utility;
/**
 * Classe che gestiste le classi dell'istituto 
 * @author Nicola Pisanti
 * @version 1.0
 */
public class ManagerClasse  {
	
	
	private static ManagerClasse instance;
	
	public static final String TABELLA_CLASSE = "classroom";
	public static final String TABELLA_INDIRIZZO ="address";
	public static final String TABLE_INSEGNANTE_AVERE_CLASSE = "teacher_has_classroom";
	public static final String TABLE_STUDENTE_AVERE_CLASSE = "student_has_classroom";
	
	private ManagerClasse(){
		super();
	}
	
	
	
	/**
	 * Ritorna la sola istanza della classe esistente.
	 * 
	 * @return Ritorna l'istanza della classe.
	 */
	public static synchronized ManagerClasse ottenereIstanza(){
		if(instance==null){
			instance = new ManagerClasse();
		}
		return instance;
	}
	
	
	
	
	
	/**
	 * Verifica se la classe data in input e nel database
	 * @param La classe di cui bisogna verificare l'esistenza
	 * @return true se la classe e nel database, altrimenti false
	 * @throws CampoObbligatorioEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 */
	public synchronized boolean esiste (Classe pClasse) throws CampoObbligatorioEccezione, ConnessioneEccezione, SQLException {
		
		boolean result = false;
		Connection connect = null;

		if (pClasse.ottenereNome() == null)
			throw new CampoObbligatorioEccezione("Specificare il nome della classe.");
		if (pClasse.ottenereAnnoAccademico() <=1970)
			throw new CampoObbligatorioEccezione("Specificare l'anno accademico");
		if (pClasse.ottenereIdIndirizzo()<=0){
			throw new CampoObbligatorioEccezione("Specificare l'indirizzo");
			//l'utente inserisce l'indirizzo, viene convertito in idAddress
		}
		
		try {
			//Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			String sql = "SELECT * FROM " 
				+ ManagerClasse.TABELLA_CLASSE
				+ " WHERE name = " 
				+ Utility.eNull(pClasse.ottenereNome()) 
				+ " AND accademic_year = "
				+ Utility.eNull(pClasse.ottenereAnnoAccademico()
				+ " AND id_address = "
				+ Utility.eNull(pClasse.ottenereIdIndirizzo())
				
				);

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
	 * Inserisce l'oggetto di tipo classe nel database
	 * @param la classe da inserire nel database
	 * @throws CampoObbligatorioEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized void inserire(Classe pClasse) throws CampoObbligatorioEccezione, 
		ConnessioneEccezione, SQLException, EntitaNonTrovataEccezione, ValoreNonValidoEccezione{
		
		Connection connect= null;
		try{
			// controllo dei campi obbligatori
			if (pClasse.ottenereNome() == null)
				throw new CampoObbligatorioEccezione("Specificare il nome della classe.");
			if (pClasse.ottenereAnnoAccademico() <=1970)
				throw new CampoObbligatorioEccezione("Specificare l'anno accademico");
			if (pClasse.ottenereIdIndirizzo()<=0){
				throw new CampoObbligatorioEccezione("Specificare l'indirizzo");
				//l'utente inserisce l'indirizzo, viene convertito in idAddress
			}
			
			connect = DBConnessione.ottenereConnesione();
			if (connect==null)
				throw new ConnessioneEccezione();
			//Prepariamo la stringa Sql
			String sql =
				"INSERT INTO " 
				+ ManagerClasse.TABELLA_CLASSE 
				+ " (id_address, name, accademic_year) " 
				+ "VALUES (" 
				+ Utility.eNull(pClasse.ottenereIdIndirizzo()) 
				+ "," 
				+ Utility.eNull(pClasse.ottenereNome()) 
				+ "," 
				+ Utility.eNull(pClasse.ottenereAnnoAccademico())
				+ ")";
		
			Utility.eseguireOperazione(connect,sql);
		
			pClasse.settareIdClasse((Utility.ottenereValoreMassimo("id_classroom",ManagerClasse.TABELLA_CLASSE)));
		
		}finally {
		//rilascia le risorse
		
		DBConnessione.rilasciareConnessione(connect);
		}
	}
	
	/**
	 * Aggiorna le statistiche di una classe
	 * @param La classe con le statistiche aggiornate (ma ID identico)
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws CampoObbligatorioEccezione
	 */
	public synchronized void aggiornare (Classe pClasse) throws ConnessioneEccezione,
	SQLException, EntitaNonTrovataEccezione, CampoObbligatorioEccezione{
		Connection connect= null;
		
		try{
			if (pClasse.ottenereIdClasse()<=0)
				throw new EntitaNonTrovataEccezione("Impossibile trovare la classe!");
			
			if (pClasse.ottenereNome() == null)
				throw new CampoObbligatorioEccezione("Specificare il nome della classe.");
			if (pClasse.ottenereAnnoAccademico() <=1970)
				throw new CampoObbligatorioEccezione("Specificare l'anno accademico");
			if (pClasse.ottenereIdIndirizzo()<=0){
				throw new CampoObbligatorioEccezione("Specificare l'indirizzo");
				//l'utente inserisce l'indirizzo, viene convertito in idAddress
			}
			//Prepariamo la stringa SQL
			String sql=
				"UPDATE " 
				+	ManagerClasse.TABELLA_CLASSE 
				+ " SET" 
				+ " id_address = " 
				+ Utility.eNull(pClasse.ottenereIdIndirizzo()) 
				+ ", name = " 
				+ Utility.eNull(pClasse.ottenereNome()) 
				+ ", accademic_year = " 
				+ Utility.eNull(pClasse.ottenereAnnoAccademico())  
				+ " WHERE id_classroom = " 
				+ Utility.eNull(pClasse.ottenereIdClasse());
			
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
	 * Cancella una classe dal database
	 * @param La classe da cancellare
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws CampoObbligatorioEccezione
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized void eliminare (Classe pClasse) throws ConnessioneEccezione, 
			SQLException, EntitaNonTrovataEccezione, CampoObbligatorioEccezione, ValoreNonValidoEccezione {
		Connection connect = null;
		
		
		try {
			//ManagerUser.getInstance().userOnDeleteCascade(pUtente);
			connect = DBConnessione.ottenereConnesione();
				//Prepariamo la stringa SQL
				String sql = "DELETE FROM " 
							+ ManagerClasse.TABELLA_CLASSE 
							+ " WHERE id_classroom = "
							+ Utility.eNull(pClasse.ottenereIdClasse());
			
				Utility.eseguireOperazione(connect, sql);
		}finally {
			//rilascia le risorse
			DBConnessione.rilasciareConnessione(connect);
		}
	}
	
	public synchronized Collection<Classe> ottenereClassePerStudente(Utente pUtente) throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione, CampoObbligatorioEccezione{
		Collection<Classe> result=null;
		Connection connect = null;
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		try
		{
			// Se non esiste l'utente
			if (!managerUser.esiste(pUtente))
					throw new EntitaNonTrovataEccezione("L'utente non esiste!!!");
			if(!managerUser.eStudente(pUtente))
					throw new ValoreNonValidoEccezione("L'utente non e uno studente!");
			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti alla classe dell'id passato
			 */
			int iduser=managerUser.ottenereUtenteId(pUtente);
			String tSql = 
				
				"SELECT " 
				+ ManagerClasse.TABELLA_CLASSE 
				+".* FROM " 
				+ ManagerClasse.TABLE_STUDENTE_AVERE_CLASSE 
				+ ", "
				+ ManagerClasse.TABELLA_CLASSE 
				+ " WHERE "
				+ ManagerClasse.TABLE_STUDENTE_AVERE_CLASSE
				+ ".id_user = "  
				+ Utility.eNull(iduser)
				+" AND "
				+ ManagerClasse.TABELLA_CLASSE 
				+".id_classroom = "
				+ ManagerClasse.TABLE_STUDENTE_AVERE_CLASSE 
				+".id_classroom";
				
				
			
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null) 
				throw new ConnessioneEccezione();
			
			
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);
			
				result = this.loadRecordsFromRs(tRs);
				
			if(result.isEmpty()) 
				throw new EntitaNonTrovataEccezione("Impossibile Trovare Classi per l'utente inserito");
			
			return result;
		}finally{
			DBConnessione.rilasciareConnessione(connect);
		}
	}
	
	/**
	 * Restituisce la classe che ha l'ID passato 
	 * @param L'ID della classe cercata
	 * @return una stringa che rappresenta la classe con l'ID fornito
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized Classe ottenereClassePerID(int pId) throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione{
		Classe result=null;
		Connection connect = null;
		try
		{
			// Se non e stato fornito l'id restituiamo un codice di errore
			if (pId <= 0)
				throw new EntitaNonTrovataEccezione("Impossibile trovare la classe!");
			
			
			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti alla classe dell'id passato
			 */
			String tSql = 
				"SELECT * FROM " 
				+ ManagerClasse.TABELLA_CLASSE 
				+ " WHERE id_classroom = " 
				+ Utility.eNull(pId) ;
			
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null) 
				throw new ConnessioneEccezione();
			
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);
			
			if (tRs.next()) 
				result = this.loadRecordFromRs(tRs);
			else 
				throw new EntitaNonTrovataEccezione("Impossibile trovare l'utente!");
			
			return result;
		}finally{
			DBConnessione.rilasciareConnessione(connect);
		}
	}
	
	
	/**
	 * Restituisce una collezione di classi dello stesso anno accademico
	 */
	public synchronized Collection<Classe> ottenereClassePerAnnoAccademico(int pAnnoAccademico) throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione{
		Collection<Classe> result=null;
		Connection connect = null;
		try
		{
			// Se non e stato fornito l'id restituiamo un codice di errore
			if (pAnnoAccademico <= 1970)
				throw new EntitaNonTrovataEccezione("Data troppo vecchia");
			
			
			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti alla classe dell'id passato
			 */
			String tSql = 
				"SELECT * FROM " 
				+ ManagerClasse.TABELLA_CLASSE 
				+ " WHERE accademic_year = " 
				+ Utility.eNull(pAnnoAccademico) ;
			
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null) 
				throw new ConnessioneEccezione();
			
			
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);
			
				result = this.loadRecordsFromRs(tRs);
				
			if(result.isEmpty()) 
				throw new EntitaNonTrovataEccezione("Impossibile Trovare Classi per la data inserita");
			
			return result;
		}finally{
			DBConnessione.rilasciareConnessione(connect);
		}
	}
		
	public synchronized Collection<Integer> ottenereAnnoAccademicoElenco() throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione{
		Collection<Integer> result=null;
		Connection connect = null;
		try
		{	
			
			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti alla classe dell'id passato
			 */
			String tSql = 
				"SELECT DISTINCT accademic_year FROM " 
				+ ManagerClasse.TABELLA_CLASSE
				+ " order by accademic_year ";
			
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null) 
				throw new ConnessioneEccezione();
			
			
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);
			
				result = this.loadIntegersFromRs(tRs);
				
			if(result.isEmpty()) 
				throw new EntitaNonTrovataEccezione("Impossibile Trovare Classi per la data inserita");
			
			return result;
		}finally{
			DBConnessione.rilasciareConnessione(connect);
		}
	}
	
	
	public synchronized Classe ottenereClassePerUtenteAnnoAccademico(Utente pUtente, int pAnnoAccademico) throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione, CampoObbligatorioEccezione{
		Classe result = null;
		Classe temp = null;
		ManagerClasse managerClassroom = ManagerClasse.ottenereIstanza();
		Collection<Classe> list = null;
		list = managerClassroom.ottenereClassePerStudente(pUtente);
		Iterator<Classe> it = list.iterator();
		while(it.hasNext()){
			temp = it.next();
			if(temp.ottenereAnnoAccademico()==pAnnoAccademico){
				result = temp;
				break;
			}
		}
		return result;
	}
	public synchronized Collection<Classe> ottenereClassePerInsegnanteAnnoAccademico(Utente pUtente, int pAnnoAccademico) throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione, CampoObbligatorioEccezione{
		Collection<Classe> result = null;
		Connection connect = null;
		int idUser = pUtente.ottenereId();
		try
		{
			// Se non e stato fornito l'id restituiamo un codice di errore
			if (pAnnoAccademico <= 1970)
				throw new EntitaNonTrovataEccezione("Data troppo vecchia");
			
			
			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * 
			 */
			String tSql = 
				"SELECT DISTINCT " 
				+ ManagerClasse.TABELLA_CLASSE +".* FROM "  
				+ ManagerClasse.TABELLA_CLASSE + ", "
				+ ManagerClasse.TABLE_INSEGNANTE_AVERE_CLASSE 
				+ " WHERE  "
				+ ManagerClasse.TABELLA_CLASSE + ".id_classroom = "
				+ ManagerClasse.TABLE_INSEGNANTE_AVERE_CLASSE 
				+ ".id_classroom  AND "
				+ ManagerClasse.TABELLA_CLASSE + ".accademic_year = "
				+ Utility.eNull(pAnnoAccademico)
				+ " AND "
				+ ManagerClasse.TABLE_INSEGNANTE_AVERE_CLASSE + ".id_user = "
				+ Utility.eNull(idUser)
				;
			
			
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null) 
				throw new ConnessioneEccezione();
			
			
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);
			
				result = this.loadRecordsFromRs(tRs);
				
			if(result.isEmpty()) 
				throw new EntitaNonTrovataEccezione("Impossibile Trovare Classi per l'utente e l'anno inseriti");
			
			return result;
		}finally{
			DBConnessione.rilasciareConnessione(connect);
		}
	
	}
	public synchronized Collection<Classe> ottenereClassePerInsegnante(Utente pUtente) 
	throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione, CampoObbligatorioEccezione{
		Collection<Classe> result=null;
		Connection connect = null;
		ManagerUtente managerUser = ManagerUtente.ottenereIstanza();
		try
		{
			// Se non esiste l'utente
			if (!managerUser.esiste(pUtente))
					throw new EntitaNonTrovataEccezione("L'utente non esiste!!!");
			if(!managerUser.eInsegnante(pUtente))
					throw new ValoreNonValidoEccezione("L'utente non e uno studente!");
			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti alla classe dell'id passato
			 */
			int iduser=managerUser.ottenereUtenteId(pUtente);
			String tSql = 
				
				"SELECT DISTINCT " 
				+ ManagerClasse.TABELLA_CLASSE 
				+".* FROM " 
				+ ManagerClasse.TABLE_INSEGNANTE_AVERE_CLASSE 
				+ ", "
				+ ManagerClasse.TABELLA_CLASSE 
				+ " WHERE "
				+ ManagerClasse.TABLE_INSEGNANTE_AVERE_CLASSE
				+ ".id_user = "  
				+ Utility.eNull(iduser)
				+" AND "
				+ ManagerClasse.TABELLA_CLASSE 
				+".id_classroom = "
				+ ManagerClasse.TABLE_INSEGNANTE_AVERE_CLASSE 
				+".id_classroom";
				
				
			
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null) 
				throw new ConnessioneEccezione();
			
			
			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);
			
				result = this.loadRecordsFromRs(tRs);
				
			if(result.isEmpty()) {
				
				throw new EntitaNonTrovataEccezione("Impossibile Trovare Classi per l'utente inserito");
			}
			
			return result;
		}finally{
			DBConnessione.rilasciareConnessione(connect);
		}
	}
	/** COnsente la lettura di un intero dal recod resultSet
	 * 
	 * @param pRs
	 * 		resultSet
	 * @return
	 * 	collection<Integer>
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	private Collection<Integer> loadIntegersFromRs(ResultSet pRs) throws SQLException, ValoreNonValidoEccezione{
		Collection<Integer> result = new Vector<Integer>();
		while(pRs.next())  {
			result.add(pRs.getInt("accademic_year"));
		} 
		return result;
	}



	/**
	 * Consente la lettura di un solo record dal Result Set
	 * @param Il result set da cui estrarre l'oggetto Classroom
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	private Classe loadRecordFromRs(ResultSet pRs) throws SQLException, ValoreNonValidoEccezione{
		Classe classroom = new Classe();
		classroom.settareNome(pRs.getString("name"));
		classroom.settareAnnoAccademico(pRs.getInt("accademic_year"));
		classroom.settareIdClasse(pRs.getInt("id_classroom"));
		classroom.settareIdIndirizzo(pRs.getInt("id_address"));
		return classroom;
	}

	/**
	 * Consente la lettura di un piu record dal Result Set
	 * @param Il result set da cui estrarre l'oggetto Classroom
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	private Collection<Classe> loadRecordsFromRs(ResultSet pRs) throws SQLException, ValoreNonValidoEccezione{
		Collection<Classe> result = new Vector<Classe>();
		while(pRs.next())  {
			result.add(loadRecordFromRs(pRs));
		} 
		return result;
	}

}
