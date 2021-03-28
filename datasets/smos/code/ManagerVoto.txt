package smos.storage;

import smos.bean.Insegnamento;
import smos.bean.VoceElencoUtenti;
import smos.bean.Voto;
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
 * Classe manager dei voti.
 * 
 * @author Luigi Colangelo
 * @version 1.0
 * 
 * 
 */
public class ManagerVoto {
    private static ManagerVoto instance;

    /**
     * Il nome della tabella dei voti.
     */
    public static final String TABELLA_VOTO = "votes";

    /**
     * Il costruttore della classe.
     */
    public ManagerVoto() {
        super();
    }

    /**
     * Ritorna la sola istanza del voto esistente.
     * 
     * @return Ritorna l'istanza della classe.
     */
    public static synchronized ManagerVoto ottenereIstanza() {
        if (instance == null) {
            instance = new ManagerVoto();
        }
        return instance;
    }

    /**
     * Verifica l'esistenza di voto nel database.
     * 
     * @param pVoto il voto da controllare.
     * @return Ritorna true se esiste il voto passato come parametro, false
     *         altrimenti.
     * 
     * @throws CampoObbligatorioEccezione
     * @throws SQLException
     * @throws ConnessioneEccezione
     */
    public synchronized boolean esiste(Voto pVoto) throws CampoObbligatorioEccezione, ConnessioneEccezione, SQLException {

        boolean result = false;
        Connection connect = null;

        if (pVoto.ottenereId_voti() == 0)
            throw new CampoObbligatorioEccezione("Specificare l'id.");
        try {
            // Otteniamo una Connessione al DataBase
            connect = DBConnessione.ottenereConnesione();
            if (connect == null)
                throw new ConnessioneEccezione();

            String sql = "SELECT * FROM " + ManagerVoto.TABELLA_VOTO + " WHERE id_votes = "
                    + Utility.eNull(pVoto.ottenereId_voti());

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
     * Inserisce un nuovo voto nella tabella Votes.
     * 
     * @param pVoto il voto da inserire.
     * 
     * @throws SQLException
     * @throws ConnessioneEccezione
     * @throws CampoObbligatorioEccezione
     * @throws EntitaNonTrovataEccezione
     * @throws ValoreNonValidoEccezione
     */
    public synchronized void inserire(Voto pVoto) throws CampoObbligatorioEccezione, ConnessioneEccezione, SQLException,
            EntitaNonTrovataEccezione, ValoreNonValidoEccezione {
        Connection connect = null;
        try {

            connect = DBConnessione.ottenereConnesione();
            if (connect == null)
                throw new ConnessioneEccezione();
            // Prepariamo la stringa Sql
            String sql = "INSERT INTO " + ManagerVoto.TABELLA_VOTO
                    + " (id_user, id_teaching, written, oral, laboratory, AccademicYear, turn) " + "VALUES ("
                    + Utility.eNull(pVoto.ottenereId_utente()) + ", " + Utility.eNull(pVoto.ottenereInsegnamento()) + ", "
                    + Utility.eNull(pVoto.ottenereScritto()) + ", " + Utility.eNull(pVoto.ottenereOrale()) + ", "
                    + Utility.eNull(pVoto.ottenereLaboratorio()) + ", " + Utility.eNull(pVoto.ottenereAnnoAccademico()) + ", "
                    + Utility.eNull(pVoto.ottenereTurno()) + " )";

            Utility.eseguireOperazione(connect, sql);

            pVoto.settareId_voti(Utility.ottenereValoreMassimo("id_votes", ManagerVoto.TABELLA_VOTO));

        } finally {
            // rilascia le risorse

            DBConnessione.rilasciareConnessione(connect);
        }
    }

    /**
     * Aggiorna un voto presente nella tabella votes.
     * 
     * @param pVoto Un voto da modificare
     * 
     * @throws ConnessioneEccezione
     * @throws SQLException
     * @throws EntitaNonTrovataEccezione
     * @throws CampoObbligatorioEccezione
     */
    public synchronized void aggiornare(Voto pVoto)
            throws ConnessioneEccezione, SQLException, EntitaNonTrovataEccezione, CampoObbligatorioEccezione {
        Connection connect = null;

        try {
            if (pVoto.ottenereId_voti() <= 0)
                throw new EntitaNonTrovataEccezione("Impossibile trovare il voto!");

            if (pVoto.ottenereId_utente() <= 0)
                throw new CampoObbligatorioEccezione("Specificare l'user del voto");
            if (pVoto.ottenereInsegnamento() <= 0)
                throw new CampoObbligatorioEccezione("Specificare l'insegnamento del voto");
            if (pVoto.ottenereAnnoAccademico() <= 0)
                throw new CampoObbligatorioEccezione("Specificare l'anno accademico");
            if (pVoto.ottenereTurno() < 0)
                throw new CampoObbligatorioEccezione("Specificare il semestre ");
            // Prepariamo la stringa SQL
            String sql = "UPDATE " + ManagerVoto.TABELLA_VOTO + " SET" + " id_user = "
                    + Utility.eNull(pVoto.ottenereId_utente()) + "," + " id_teaching= " + Utility.eNull(pVoto.ottenereInsegnamento())
                    + "," + " written= " + Utility.eNull(pVoto.ottenereScritto()) + "," + " oral= "
                    + Utility.eNull(pVoto.ottenereOrale()) + "," + " laboratory= " + Utility.eNull(pVoto.ottenereLaboratorio())
                    + "," + " accademicYear= " + Utility.eNull(pVoto.ottenereAnnoAccademico()) + "," + " turn="
                    + Utility.eNull(pVoto.ottenereTurno()) + " WHERE id_votes = " + Utility.eNull(pVoto.ottenereId_voti());

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
     * Verifica se uno studente passato come parametro ha un voto assegnato
     * nell'insegnamento passato come parametro nell'anno passato come parametro e
     * nel quadrimestre passato come parametro
     * 
     * 
     * @param pInsegnamento L'insegnamento da controllare.
     * @param pUserListItem Lo studente da controllare
     * 
     * @return Ritorna l'id del voto -1 altrimenti
     * 
     * @throws SQLException
     * @throws EntitaNonTrovataEccezione
     * @throws ConnessioneEccezione
     * @throws ValoreNonValidoEccezione
     */
    public synchronized int ottenereIdVoto(Insegnamento pInsegnamento, int annoAccademico, int turno,
            VoceElencoUtenti pUtente)
            throws SQLException, EntitaNonTrovataEccezione, ConnessioneEccezione, ValoreNonValidoEccezione {
        Connection connect = null;
        int result = -1;
        Voto v = null;
        if (pInsegnamento.ottenereId() <= 0)
            throw new EntitaNonTrovataEccezione("Specificare l'insegnamento");
        if (pUtente.ottenereId() <= 0)
            throw new EntitaNonTrovataEccezione("Specificare l'utente");
        try {
            connect = DBConnessione.ottenereConnesione();
            if (connect == null)
                throw new ConnessioneEccezione();

            // Preparimao la stringa sql
            String sql = "SELECT * FROM " + ManagerVoto.TABELLA_VOTO + " WHERE id_teaching = "
                    + Utility.eNull(pInsegnamento.ottenereId()) + " AND " + ManagerVoto.TABELLA_VOTO + ".AccademicYear= "
                    + Utility.eNull(annoAccademico) + " AND " + ManagerVoto.TABELLA_VOTO + ".turn= "
                    + Utility.eNull(turno) + " AND " + ManagerVoto.TABELLA_VOTO + ".id_user= "
                    + Utility.eNull(pUtente.ottenereId());
            // Inviamo la Query al database
            ResultSet pRs = Utility.queryOperazione(connect, sql);
            if (pRs.next()) {
                v = this.loadRecordFromRs(pRs);
                result = v.ottenereId_voti();

            }

            return result;

        } finally {
            // rilasciamo le risorse
            DBConnessione.rilasciareConnessione(connect);

        }
    }

    /**
     * Elimina un voto dalla tabella votes.
     * 
     * @param pVoto Il voto da eliminare.
     * 
     * @throws CampoObbligatorioEccezione
     * @throws EntitaNonTrovataEccezione
     * @throws SQLException
     * @throws ConnessioneEccezione
     * @throws ValoreNonValidoEccezione
     * 
     */
    public synchronized void eliminare(Voto pVoto) throws ConnessioneEccezione, SQLException, EntitaNonTrovataEccezione,
            CampoObbligatorioEccezione, ValoreNonValidoEccezione {
        Connection connect = null;

        try {
            // ManagerTeaching.getInstance().teachingOnDeleteCascade(pInsegnamento);
            connect = DBConnessione.ottenereConnesione();
            // Prepariamo la stringa SQL
            String sql = "DELETE FROM " + ManagerVoto.TABELLA_VOTO + " WHERE id_votes = "
                    + Utility.eNull(pVoto.ottenereId_voti());

            Utility.eseguireOperazione(connect, sql);
        } finally {
            // rilascia le risorse
            DBConnessione.rilasciareConnessione(connect);
        }
    }

    /**
     * Ritorna l'insegnamento corrispondente all'id passato come parametro.
     * 
     * @param pId L'id dell'insegnamento.
     * @return Ritorna l'insegnamento associato all'id passato come parametro.
     * 
     * @throws ConnessioneEccezione
     * @throws SQLException
     * @throws EntitaNonTrovataEccezione
     * @throws ValoreNonValidoEccezione
     */
    public synchronized Voto ottenereVotoPerId(int pId)
            throws ConnessioneEccezione, SQLException, EntitaNonTrovataEccezione, ValoreNonValidoEccezione {
        Voto result = null;
        Connection connect = null;
        try {

            if (pId <= 0)
                throw new EntitaNonTrovataEccezione("Impossibile trovare il voto!");

            // Otteniamo una Connessione al DataBase
            connect = DBConnessione.ottenereConnesione();
            if (connect == null)
                throw new ConnessioneEccezione();

            // Prepariamo la stringa SQL
            String sql = "SELECT * FROM " + ManagerVoto.TABELLA_VOTO + " WHERE id_votes = " + Utility.eNull(pId);

            // Inviamo la Query al DataBase
            ResultSet pRs = Utility.queryOperazione(connect, sql);

            if (pRs.next())
                result = this.loadRecordFromRs(pRs);
            else
                throw new EntitaNonTrovataEccezione("Impossibile trovare l'insegnamento!");

            return result;
        } finally {
            // rilascia le risorse
            DBConnessione.rilasciareConnessione(connect);
        }
    }

    /**
     * Ritorna l'id dell'insegnamento corrispondente all'id del voto passato come
     * parametro.
     * 
     * @param pId L'id del voto.
     * @return Ritorna l'id dell' insegnamento.
     * 
     * @throws EntitaNonTrovataEccezione
     * @throws ConnectionException
     * @throws SQLException
     */
    public synchronized String ottenereInsegnamentoIdPerVotoId(int pId)
            throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException {
        String result;
        Connection connect = null;
        try {
            // Se non e' stato fornito l'id restituiamo un codice di errore
            if (pId <= 0)
                throw new EntitaNonTrovataEccezione("Impossibile trovare il voto!");

            /*
             * Prepariamo la stringa SQL per recuperare le informazioni corrispondenti
             * all'id dell'insegnamento passato come parametro
             */
            String tSql = "SELECT id_teaching FROM " + ManagerVoto.TABELLA_VOTO

                    + " WHERE id_votes = " + Utility.eNull(pId);

            // Otteniamo una Connessione al DataBase
            connect = DBConnessione.ottenereConnesione();
            if (connect == null)
                throw new ConnessioneEccezione();

            // Inviamo la Query al DataBase
            ResultSet tRs = Utility.queryOperazione(connect, tSql);

            if (tRs.next())
                result = tRs.getString("id_teaching");
            else
                throw new EntitaNonTrovataEccezione("Impossibile trovare il voto!");

            return result;
        } finally {
            DBConnessione.rilasciareConnessione(connect);
        }
    }

    /**
     * Ritorna l'insieme di tutti i voti presenti nel database.
     * 
     * @return Ritorna una collection di voti.
     * 
     * @throws ConnessioneEccezione
     * @throws SQLException
     * @throws ValoreNonValidoEccezione
     * @throws EntitaNonTrovataEccezione
     */
    public synchronized Collection<Voto> ottenereVoto()
            throws ConnessioneEccezione, SQLException, ValoreNonValidoEccezione, EntitaNonTrovataEccezione {
        Collection<Voto> result = null;
        Connection connect = null;

        try {
            // Prepariamo la stringa SQL
            String sql = "SELECT * FROM " + ManagerVoto.TABELLA_VOTO + " ORDER BY id_votes";

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
     * Ritorna l'insieme dei voti associati all'utente corrispondente all'id passato
     * come paramentro.
     * 
     * @param pId L'id dell'utente.
     * @return Ritorna una collection di voti.
     * 
     * @throws EntitaNonTrovataEccezione
     * @throws ConnessioneEccezione
     * @throws SQLException
     * @throws ValoreNonValidoEccezione
     */
    public synchronized Collection<Voto> ottenereVotoPerUtenteId(int pId)
            throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione {

        Collection<Voto> result = null;
        Connection connect = null;

        if (pId <= 0)
            throw new EntitaNonTrovataEccezione("specificare l'utente");

        try {
            // Prepariamo la stringa SQL
            String sql = "SELECT " + ManagerVoto.TABELLA_VOTO + ".* FROM " + ManagerVoto.TABELLA_VOTO + " WHERE ("
                    + ManagerVoto.TABELLA_VOTO + ".id_user = " + Utility.eNull(pId) + ")" + " ORDER BY id_user";

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
     * Ritorna l'insieme dei voti associati all'utente corrispondente all'id passato
     * come paramentro.
     * 
     * @param pId L'id dell'utente.
     * @return Ritorna una collection di voti.
     * 
     * @throws EntitaNonTrovataEccezione
     * @throws ConnessioneEccezione
     * @throws SQLException
     * @throws ValoreNonValidoEccezione
     */
    public synchronized Collection<Voto> ottenereVotoPerUtenteIdAnnoTurno(int pId, int pAnno, int pTurno)
            throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione {

        Collection<Voto> result = null;
        Connection connect = null;

        if (pId <= 0)
            throw new EntitaNonTrovataEccezione("specificare l'utente");

        try {
            // Prepariamo la stringa SQL
            String sql = "SELECT " + ManagerVoto.TABELLA_VOTO + ".* FROM " + ManagerVoto.TABELLA_VOTO + " WHERE ("
                    + ManagerVoto.TABELLA_VOTO + ".id_user = " + Utility.eNull(pId) + " AND "
                    + ManagerVoto.TABELLA_VOTO + ".accademicYear = " + Utility.eNull(pAnno) + " AND "
                    + ManagerVoto.TABELLA_VOTO + ".turn = " + Utility.eNull(pTurno) + ")" + " ORDER BY id_user";

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

    public synchronized void eliminareVotoPerUtenteIdAnnoTurno(int pId, int pAnno, int pTurno)
            throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione {

        Connection connect = null;

        if (pId <= 0)
            throw new EntitaNonTrovataEccezione("specificare l'utente");

        try {
            // Prepariamo la stringa SQL
            String sql = "DELETE " + ManagerVoto.TABELLA_VOTO + " FROM " + ManagerVoto.TABELLA_VOTO + " WHERE ("
                    + ManagerVoto.TABELLA_VOTO + ".id_user=" + Utility.eNull(pId) + " AND " + ManagerVoto.TABELLA_VOTO
                    + ".AccademicYear=" + Utility.eNull(pAnno) + " AND " + ManagerVoto.TABELLA_VOTO + ".turn="
                    + Utility.eNull(pTurno) + ")";

            // Otteniamo una Connessione al DataBase
            connect = DBConnessione.ottenereConnesione();
            if (connect == null)
                throw new ConnessioneEccezione();

            // Inviamo la Query al DataBase
            Utility.eseguireOperazione(connect, sql);

        } finally {
            // rilascia le risorse
            DBConnessione.rilasciareConnessione(connect);
        }
    }

    /**
     * Consente la lettura di un record dal ResultSet.
     * 
     * @param pRs Il risultato della query.
     * @return Ritorna il voto letto.
     * 
     * @throws SQLException
     * @throws ValoreNonValidoEccezione
     */
    private Voto loadRecordFromRs(ResultSet pRs) throws SQLException, ValoreNonValidoEccezione {
        Voto votes = new Voto();
        votes.settareId_voti(pRs.getInt(("id_votes")));
        votes.settareId_utente(pRs.getInt("id_user"));
        votes.settareInsegnamento(pRs.getInt("id_teaching"));
        votes.settareScritto(pRs.getInt("written"));
        votes.settareOrale(pRs.getInt("oral"));
        votes.settareLaboratorio(pRs.getInt("laboratory"));
        votes.settareAnnoAccademico(pRs.getInt("AccademicYear"));
        votes.settareTurno(pRs.getInt("turn"));

        return votes;
    }

    /**
     * Consente la lettura dei record dal ResultSet.
     * 
     * @param pRs Il risultato della query.
     * @return Ritorna la collection di insegnamenti letti.
     * 
     * @throws SQLException
     * @throws ValoreNonValidoEccezione
     */
    private Collection<Voto> loadRecordsFromRs(ResultSet pRs) throws SQLException, ValoreNonValidoEccezione {
        Collection<Voto> result = new Vector<Voto>();
        do {
            result.add(loadRecordFromRs(pRs));
        } while (pRs.next());
        return result;
    }

}
