package smos.storage;

import smos.bean.Insegnamento;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.CampoObbligatorioEccezione;
import smos.storage.connectionManagement.DBConnessione;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;
import smos.utility.Utility;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;
import java.sql.Connection;

/**
 * 
 * Classe manager degli insegnamenti.
 * 
 * @author Giulio D'Amora
 * @version 1.0
 * 
 */
public class ManagerInsegnamento {
	private static ManagerInsegnamento instance;

	/**
	 * Il nome della tabella degli insegnamenti.
	 */
	public static final String TABELLA_INSEGNAMENTO = "teaching";

	/**
	 * Il nome della tabella che modella l'associazione molti a molti tra
	 * indirizzi ed insegnamenti.
	 */
	public static final String TABELLA_INDIRIZZO_INSEGNAMENTI = "address_has_teaching";

	/**
	 * Il nome della tabella che modella l'associazione molti a molti tra utenti
	 * e insegnamenti.
	 */
	public static final String TABELLA_INSEGNANTE_CLASSE = "teacher_has_classroom";

	/**
	 * Il costruttore della classe.
	 */
	private ManagerInsegnamento() {
		super();
	}

	/**
	 * Ritorna la sola istanza dell'insegnamento esistente.
	 * 
	 * @return Ritorna l'istanza della classe.
	 */
	public static synchronized ManagerInsegnamento ottenereIstanza() {
		if (instance == null) {
			instance = new ManagerInsegnamento();
		}
		return instance;
	}

	/**
	 * Verifica l'esistenza di un insegnamento nel database.
	 * 
	 * @param pInsegnamento
	 *            L'insegnamento da controllare.
	 * @return Ritorna true se esiste l'insegnamento passato come parametro,
	 *         false altrimenti.
	 * 
	 * @throws CampoObbligatorioEccezione
	 * @throws SQLException
	 * @throws ConnessioneEccezione
	 */
	public synchronized boolean esiste(Insegnamento pInsegnamento)
			throws CampoObbligatorioEccezione, ConnessioneEccezione, SQLException {

		boolean result = false;
		Connection connect = null;

		if (pInsegnamento.ottenereNome() == null)
			throw new CampoObbligatorioEccezione("Specificare il nome.");
		try {
			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			String sql = "SELECT * FROM " + ManagerInsegnamento.TABELLA_INSEGNAMENTO
					+ " WHERE name = " + Utility.eNull(pInsegnamento.ottenereNome());

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
	 * Inserisce un nuovo insegnamento nella tabella teaching.
	 * 
	 * @param pInsegnamento
	 *            L'insegnamento da inserire.
	 * 
	 * @throws SQLException
	 * @throws ConnessioneEccezione
	 * @throws CampoObbligatorioEccezione
	 * @throws EntitaNonTrovataEccezione
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized void inserire(Insegnamento pInsegnamento)
			throws CampoObbligatorioEccezione, ConnessioneEccezione, SQLException,
			EntitaNonTrovataEccezione, ValoreNonValidoEccezione {
		Connection connect = null;
		try {
			// controllo dei campi obbligatori
			if (pInsegnamento.ottenereNome() == null)
				throw new CampoObbligatorioEccezione("Specificare il campo nome");

			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();
			// Prepariamo la stringa Sql
			String sql = "INSERT INTO " + ManagerInsegnamento.TABELLA_INSEGNAMENTO
					+ " (name) " + "VALUES ("
					+ Utility.eNull(pInsegnamento.ottenereNome()) + ")";

			Utility.eseguireOperazione(connect, sql);

			pInsegnamento.settareId(Utility.ottenereValoreMassimo("id_teaching",
					ManagerInsegnamento.TABELLA_INSEGNAMENTO));

		} finally {
			// rilascia le risorse

			DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Aggiorna un insegnamento presente nella tabella teaching.
	 * 
	 * @param pInsegnamento
	 *            L'insegnamento da modificare
	 * 
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws CampoObbligatorioEccezione
	 */
	public synchronized void aggiornare(Insegnamento pInsegnamento)
			throws ConnessioneEccezione, SQLException, EntitaNonTrovataEccezione,
			CampoObbligatorioEccezione {
		Connection connect = null;

		try {
			if (pInsegnamento.ottenereId() <= 0)
				throw new EntitaNonTrovataEccezione(
						"Impossibile trovare l'insegnamento!");

			if (pInsegnamento.ottenereNome() == null)
				throw new CampoObbligatorioEccezione("Specificare il campo nome");

			// Prepariamo la stringa SQL
			String sql = "UPDATE " + ManagerInsegnamento.TABELLA_INSEGNAMENTO + " SET"
					+ " name = " + Utility.eNull(pInsegnamento.ottenereNome())
					+ " WHERE id_teaching = "
					+ Utility.eNull(pInsegnamento.ottenereId());

			// effettua una nuova connessione e invia la query
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			Utility.eseguireOperazione(connect, sql);
		} finally {
			// rilascia le risorse
			DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Elimina un insegnamento dalla tabella teaching.
	 * 
	 * @param pInsegnamento
	 *            L'insegnamento da eliminare.
	 * 
	 * @throws CampoObbligatorioEccezione
	 * @throws EntitaNonTrovataEccezione
	 * @throws SQLException
	 * @throws ConnessioneEccezione
	 * @throws ValoreNonValidoEccezione
	 * 
	 */
	public synchronized void eliminare(Insegnamento pInsegnamento)
			throws ConnessioneEccezione, SQLException, EntitaNonTrovataEccezione,
			CampoObbligatorioEccezione, ValoreNonValidoEccezione {
		Connection connect = null;

		try {
			// ManagerTeaching.getInstance().teachingOnDeleteCascade(pInsegnamento);
			connect = DBConnessione.ottenereConnesione();
			// Prepariamo la stringa SQL
			String sql = "DELETE FROM " + ManagerInsegnamento.TABELLA_INSEGNAMENTO
					+ " WHERE id_teaching = "
					+ Utility.eNull(pInsegnamento.ottenereId());

			Utility.eseguireOperazione(connect, sql);
		} finally {
			// rilascia le risorse
			DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Ritorna l'id dell'insegnamento passato come parametro.
	 * 
	 * @param pInsegnamento
	 *            L'insegnamento di cui si richiede l'id.
	 * @return Ritorna l'id dell'insegnamento passato come parametro.
	 * 
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 */
	public synchronized int ottenereInsegnamentoId(Insegnamento pInsegnamento)
			throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException {
		int result = 0;
		Connection connect = null;
		try {
			if (pInsegnamento == null)
				throw new EntitaNonTrovataEccezione(
						"Impossibile trovare l'insegnamento!");

			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti all'id dell'insegnamento passato come parametro.
			 */
			String tSql = "SELECT id_teaching FROM "
					+ ManagerInsegnamento.TABELLA_INSEGNAMENTO + " WHERE name = "
					+ Utility.eNull(pInsegnamento.ottenereNome());

			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);

			if (tRs.next())
				result = tRs.getInt("id_teaching");

			return result;
		} finally {
			DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Ritorna il nome dell'insegnamento corrispondente all'id passato come
	 * parametro.
	 * 
	 * @param pId
	 *            L'id dell'insegnamento.
	 * @return Ritorna una stringa contenente il nome dell'insegnamento.
	 * 
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 */
	public synchronized String ottenereInsegnamentoNomePerId(int pId)
			throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException {
		String result;
		Connection connect = null;
		try {
			// Se non e' stato fornito l'id restituiamo un codice di errore
			if (pId <= 0)
				throw new EntitaNonTrovataEccezione(
						"Impossibile trovare l'insegnamento!");

			/*
			 * Prepariamo la stringa SQL per recuperare le informazioni
			 * corrispondenti all'id dell'insegnamento passato come parametro
			 */
			String tSql = "SELECT name FROM " + ManagerInsegnamento.TABELLA_INSEGNAMENTO
					+ " WHERE id_teaching = " + Utility.eNull(pId);

			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			// Inviamo la Query al DataBase
			ResultSet tRs = Utility.queryOperazione(connect, tSql);

			if (tRs.next())
				result = tRs.getString("name");
			else
				throw new EntitaNonTrovataEccezione(
						"Impossibile trovare l'insegnamento!");

			return result;
		} finally {
			DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Ritorna l'insegnamento corrispondente all'id passato come parametro.
	 * 
	 * @param pId
	 *            L'id dell'insegnamento.
	 * @return Ritorna l'insegnamento associato all'id passato come parametro.
	 * 
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized Insegnamento ottenereInsegnamentoPerId(int pId)
			throws ConnessioneEccezione, SQLException, EntitaNonTrovataEccezione,
			ValoreNonValidoEccezione {
		Insegnamento result = null;
		Connection connect = null;
		try {

			if (pId <= 0)
				throw new EntitaNonTrovataEccezione(
						"Impossibile trovare l'insegnamento!");

			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			// Prepariamo la stringa SQL
			String sql = "SELECT * FROM " + ManagerInsegnamento.TABELLA_INSEGNAMENTO
					+ " WHERE id_teaching = " + Utility.eNull(pId);

			// Inviamo la Query al DataBase
			ResultSet pRs = Utility.queryOperazione(connect, sql);

			if (pRs.next())
				result = this.loadRecordFromRs(pRs);
			else
				throw new EntitaNonTrovataEccezione(
						"Impossibile trovare l'insegnamento!");

			return result;
		} finally {
			// rilascia le risorse
			DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Ritorna l'insieme di tutti gli insegnamenti presenti nel database.
	 * 
	 * @return Ritorna una collection di insegnamenti.
	 * 
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 * @throws EntitaNonTrovataEccezione
	 */
	public synchronized Collection<Insegnamento> ottenereInsegnamenti()
			throws ConnessioneEccezione, SQLException, ValoreNonValidoEccezione,
			EntitaNonTrovataEccezione {
		Collection<Insegnamento> result = null;
		Connection connect = null;

		try {
			// Prepariamo la stringa SQL
			String sql = "SELECT * FROM " + ManagerInsegnamento.TABELLA_INSEGNAMENTO
					+ " ORDER BY name";

			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			// Inviamo la Query al DataBase
			ResultSet pRs = Utility.queryOperazione(connect, sql);

			if (pRs.next())
				result = this.loadRecordsFromRs(pRs);

			return result;
		} finally {
			// rilascia le risorse
			DBConnessione.rilasciareConnessione(connect);
		}

	}

	/**
	 * Ritorna l'insieme degli insegnamenti associati all'utente corrispondente
	 * all'id passato come paramentro.
	 * 
	 * @param pId
	 *            L'id dell'utente.
	 * @return Ritorna una collection di insegnamenti.
	 * 
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized Collection<Insegnamento> ottenereInsegnamentiPerUtenteId(int pId)
			throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException,
			ValoreNonValidoEccezione {

		Collection<Insegnamento> result = null;
		Connection connect = null;

		if (pId <= 0)
			throw new EntitaNonTrovataEccezione("specificare l'utente");

		try {
			// Prepariamo la stringa SQL
			String sql = "SELECT " + ManagerInsegnamento.TABELLA_INSEGNAMENTO
					+ ".* FROM " + ManagerInsegnamento.TABELLA_INSEGNANTE_CLASSE
					+ ", " + ManagerInsegnamento.TABELLA_INSEGNAMENTO + " WHERE ("
					+ ManagerInsegnamento.TABELLA_INSEGNANTE_CLASSE
					+ ".id_teaching = " + ManagerInsegnamento.TABELLA_INSEGNAMENTO
					+ ".id_teaching AND "
					+ ManagerInsegnamento.TABELLA_INSEGNANTE_CLASSE + ".id_user = "
					+ Utility.eNull(pId) + ")" + " ORDER BY name";

			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			// Inviamo la Query al DataBase
			ResultSet pRs = Utility.queryOperazione(connect, sql);

			if (pRs.next())
				result = this.loadRecordsFromRs(pRs);

			return result;
		} finally {
			// rilascia le risorse
			DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Ritorna l'insieme degli insegnamenti che il docente insegna nella classe
	 * 
	 * @param pIdInsegnante
	 *            L'id dell'utente.
	 * @param pIdClasse
	 *            l'id della classe
	 * @return Ritorna una collection di insegnamenti.
	 * 
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized Collection<Insegnamento> ottenereInsegnamentiPerUtenteClasseID(int pIdInsegnante,int pIdClasse)
			throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException,
			ValoreNonValidoEccezione {

		Collection<Insegnamento> result = null;
		Connection connect = null;

		if (pIdInsegnante <= 0)
			throw new EntitaNonTrovataEccezione("specificare l'utente");
		if (pIdClasse <= 0)
			throw new EntitaNonTrovataEccezione("specificare la classe");

		try {
			// Prepariamo la stringa SQL
			
			String sql = "SELECT DISTINCT " + ManagerInsegnamento.TABELLA_INSEGNAMENTO
					+ ".* FROM " + ManagerInsegnamento.TABELLA_INSEGNANTE_CLASSE
					+ ", " + ManagerInsegnamento.TABELLA_INSEGNAMENTO + " WHERE ("
					+ ManagerInsegnamento.TABELLA_INSEGNANTE_CLASSE + ".id_user = "
					+ Utility.eNull(pIdInsegnante) +" AND "
					+ ManagerInsegnamento.TABELLA_INSEGNANTE_CLASSE 
					+ ".id_teaching= " + Utility.eNull(pIdClasse)
					+ " AND "
					+ ManagerInsegnamento.TABELLA_INSEGNANTE_CLASSE
					+ ".id_teaching = " + ManagerInsegnamento.TABELLA_INSEGNAMENTO
					+ ".id_teaching "
					+") ORDER BY name";

			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			// Inviamo la Query al DataBase
			ResultSet pRs = Utility.queryOperazione(connect, sql);

			if (pRs.next())
				result = this.loadRecordsFromRs(pRs);

			return result;
		} finally {
			// rilascia le risorse
			DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Verifica se un insegnamento ha un professore assegnato.
	 * 
	 * @param pInsegnamento
	 *            L'insegnamento da controllare.
	 * @return Ritorna true se l'insegnamento ha un professore assegnato, false
	 *         altrimenti.
	 * 
	 * @throws SQLException
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized boolean avereInsegnante(Insegnamento pInsegnamento)
			throws SQLException, EntitaNonTrovataEccezione, ConnessioneEccezione,
			ValoreNonValidoEccezione {
		Connection connect = null;
		boolean result = false;
		if (pInsegnamento.ottenereId() <= 0)
			throw new EntitaNonTrovataEccezione("Specificare l'insegnamento");

		try {
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			// Preparimao la stringa sql
			String sql = "SELECT * FROM "
					+ ManagerInsegnamento.TABELLA_INSEGNANTE_CLASSE
					+ " WHERE id_teaching = "
					+ Utility.eNull(pInsegnamento.ottenereId());
			// Inviamo la Query al database
			ResultSet pRs = Utility.queryOperazione(connect, sql);
			if (pRs.next())
				result = true;

			return result;

		} finally {
			// rilasciamo le risorse
			DBConnessione.rilasciareConnessione(connect);

		}
	}

	/**
	 * Ritorna l'insieme degli insegnamenti associati alla classe specificata
	 * 
	 * @param pId
	 *            L'id della classe.
	 * @return Ritorna una collection di insegnamenti.
	 * 
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized Collection<Insegnamento> ottenereInsegnamentiPerClasseId(int pId)
			throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException,
			ValoreNonValidoEccezione {

		Collection<Insegnamento> result = null;
		Connection connect = null;

		if (pId < 0)
			throw new EntitaNonTrovataEccezione("specificare l'id della Classe!");

		try {
			// Prepariamo la stringa SQL
			String sql = "SELECT " + ManagerInsegnamento.TABELLA_INSEGNAMENTO
					+ ".* FROM " + ManagerClasse.TABELLA_CLASSE + ", "
					+ ManagerInsegnamento.TABELLA_INDIRIZZO_INSEGNAMENTI + ", "
					+ ManagerInsegnamento.TABELLA_INSEGNAMENTO + " WHERE "
					+ ManagerClasse.TABELLA_CLASSE + ".id_classroom = "
					+ Utility.eNull(pId) + " AND "
					+ ManagerClasse.TABELLA_CLASSE + ".id_address = "
					+ ManagerInsegnamento.TABELLA_INDIRIZZO_INSEGNAMENTI
					+ ".id_address AND " + ManagerInsegnamento.TABELLA_INSEGNAMENTO
					+ ".id_teaching= "
					+ ManagerInsegnamento.TABELLA_INDIRIZZO_INSEGNAMENTI + ".id_teaching ";

			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			// Inviamo la Query al DataBase
			ResultSet pRs = Utility.queryOperazione(connect, sql);

			if (pRs.next())
				result = this.loadRecordsFromRs(pRs);

			return result;
		} finally {
			// rilascia le risorse
			DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Ritorna l'insieme degli insegnamenti associati alla classe specificata
	 * 
	 * @param nome
	 *            Il nome della classe.
	 * @return Ritorna una collection di insegnamenti.
	 * 
	 * @throws EntitaNonTrovataEccezione
	 * @throws ConnessioneEccezione
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	public synchronized Collection<Insegnamento> ottenereInsegnamentiPerClasseNome(
			String nome) throws EntitaNonTrovataEccezione, ConnessioneEccezione,
			SQLException, ValoreNonValidoEccezione {

		Collection<Insegnamento> result = null;
		Connection connect = null;

		if ((nome == null) || (nome == ""))
			throw new EntitaNonTrovataEccezione(
					"specificare il nome della Classe!");

		try {
			// Prepariamo la stringa SQL
			String sql = "SELECT " + ManagerInsegnamento.TABELLA_INSEGNAMENTO
					+ ".* FROM " + ManagerClasse.TABELLA_CLASSE + ", "
					+ ManagerInsegnamento.TABELLA_INDIRIZZO_INSEGNAMENTI + ", "
					+ ManagerInsegnamento.TABELLA_INSEGNAMENTO + " WHERE "
					+ ManagerClasse.TABELLA_CLASSE + ".name = "
					+ Utility.eNull(nome) + " AND "
					+ ManagerClasse.TABELLA_CLASSE + ".id_address = "
					+ ManagerInsegnamento.TABELLA_INDIRIZZO_INSEGNAMENTI
					+ ".id_address AND " + ManagerInsegnamento.TABELLA_INSEGNAMENTO
					+ ".id_teaching= "
					+ ManagerInsegnamento.TABELLA_INDIRIZZO_INSEGNAMENTI + ".id_teaching ";

			// Otteniamo una Connessione al DataBase
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			// Inviamo la Query al DataBase
			ResultSet pRs = Utility.queryOperazione(connect, sql);

			if (pRs.next())
				result = this.loadRecordsFromRs(pRs);

			return result;
		} finally {
			// rilascia le risorse
			DBConnessione.rilasciareConnessione(connect);
		}
	}
	
	public synchronized Collection<Insegnamento> ottenereInsegnamentiPerIdUtenteIdClasse(int pUtente, int pClasse) throws SQLException,
	EntitaNonTrovataEccezione, ConnessioneEccezione, ValoreNonValidoEccezione {
		
		
		Collection<Insegnamento> result = null;
		Connection connect = null;
		try {
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			// 	Preparimao la stringa sql
			//select teaching.* from teacher_has_classroom AS THC , teaching where thc.id_user = 54 
			//&& thc.id_classroom = 64 && thc.id_teaching = teaching.id_teaching
			
			String sql = "SELECT DISTINCT "
				+ManagerInsegnamento.TABELLA_INSEGNAMENTO+".*" 
				+" FROM " 
				+ ManagerUtente.TABELLA_INSEGNANTE_CLASSE
				+" , "
				+ManagerInsegnamento.TABELLA_INSEGNAMENTO
				+ " WHERE "
				+ ManagerUtente.TABELLA_INSEGNANTE_CLASSE
				+ ".id_user = "  
				+ Utility.eNull(pUtente)
				+ " AND "
				+ ManagerUtente.TABELLA_INSEGNANTE_CLASSE
				+ ".id_classroom= "
				+ Utility.eNull(pClasse)
				+ " AND "
				+ ManagerUtente.TABELLA_INSEGNANTE_CLASSE
				+".id_teaching ="
				+ManagerInsegnamento.TABELLA_INSEGNAMENTO
				+".id_teaching";
				// Inviamo la Query al database
			connect = DBConnessione.ottenereConnesione();
			if (connect == null)
				throw new ConnessioneEccezione();

			// Inviamo la Query al DataBase
			ResultSet pRs = Utility.queryOperazione(connect, sql);

			if (pRs.next())
				result = this.loadRecordsFromRs(pRs);

			return result;
		} finally {
			// rilascia le risorse
			DBConnessione.rilasciareConnessione(connect);
		}
	}

	/**
	 * Consente la lettura di un record dal ResultSet.
	 * 
	 * @param pRs
	 *            Il risultato della query.
	 * @return Ritorna l'insegnamento letto.
	 * 
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	private Insegnamento loadRecordFromRs(ResultSet pRs) throws SQLException,
			ValoreNonValidoEccezione {
		Insegnamento teaching = new Insegnamento();
		teaching.settareNome(pRs.getString(("name")));
		teaching.settareId(pRs.getInt("id_teaching"));

		return teaching;
	}

	/**
	 * Consente la lettura dei record dal ResultSet.
	 * 
	 * @param pRs
	 *            Il risultato della query.
	 * @return Ritorna la collection di insegnamenti letti.
	 * 
	 * @throws SQLException
	 * @throws ValoreNonValidoEccezione
	 */
	private Collection<Insegnamento> loadRecordsFromRs(ResultSet pRs)
			throws SQLException, ValoreNonValidoEccezione {
		Collection<Insegnamento> result = new Vector<Insegnamento>();
		do {
			result.add(loadRecordFromRs(pRs));
		} while (pRs.next());
		return result;
	}

}
