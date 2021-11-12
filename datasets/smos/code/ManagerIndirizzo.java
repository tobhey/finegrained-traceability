package smos.storage;
import smos.bean.Indirizzo;
import smos.bean.Insegnamento;
import smos.exception.EntitaDuplicataEccezione;
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
* Classe manager degli indirizzi 
*
*/
public class ManagerIndirizzo {

    private static ManagerIndirizzo instance;

    /**
     * Il nome della tabella degli indirizzi
     */
    public static final String TABELLA_INDIRIZZO = "address";
    public static final String TABELLA_INDIRIZZO_AVERE_INSEGNAMENTO = "address_has_teaching";

    private ManagerIndirizzo() {
        super();
    }

    /**
     * Ritorna la sola istanza della classe esistente.
     * 
     * @return Ritorna l'istanza della classe.
     */
    public static synchronized ManagerIndirizzo ottenereIstanza() {
        if (instance == null) {
            instance = new ManagerIndirizzo();
        }
        return instance;
    }

    /**
     * Verifica l'esistenza di un indirizzo nel database.
     * 
     * @param pIndirizzo L'indirizzo da controllare.
     * @return Ritorna true se esiste gia l'indirizzo passato come parametro, false
     *         altrimenti.
     * 
     * @throws CampoObbligatorioEccezione
     * @throws SQLException
     * @throws CampoObbligatorioEccezione
     * @throws ConnessioneEccezione
     * @throws ConnessioneEccezione
     * @throws SQLException
     */
    public synchronized boolean avereInsegnamento(Insegnamento pInsegnamento, Indirizzo pIndirizzo)
            throws SQLException, EntitaNonTrovataEccezione, ConnessioneEccezione, ValoreNonValidoEccezione {
        Connection connect = null;
        boolean result = false;
        if (pInsegnamento.ottenereId() <= 0)
            throw new EntitaNonTrovataEccezione("Specificare l'insegnamento");

        try {
            connect = DBConnessione.ottenereConnesione();
            if (connect == null)
                throw new ConnessioneEccezione();

            // Preparimao la stringa sql
            String sql = "SELECT * FROM " + ManagerInsegnamento.TABELLA_INDIRIZZO_INSEGNAMENTI + " WHERE id_teaching = "
                    + Utility.eNull(pInsegnamento.ottenereId()) + " AND id_address = "
                    + Utility.eNull(pIndirizzo.ottenereIdIndirizzo());
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

    public synchronized boolean esiste(Indirizzo pIndirizzo)
            throws CampoObbligatorioEccezione, ConnessioneEccezione, SQLException {
        boolean result = false;
        Connection connect = null;

        if (pIndirizzo.ottenereNome() == null)
            throw new CampoObbligatorioEccezione("Specificare il nome.");
        try {
            // Otteniamo la connessione al database
            connect = DBConnessione.ottenereConnesione();

            if (connect == null)
                throw new ConnessioneEccezione();

            String sql = " SELECT * FROM " + ManagerIndirizzo.TABELLA_INDIRIZZO + " WHERE name = "
                    + Utility.eNull(pIndirizzo.ottenereNome());

            ResultSet tRs = Utility.queryOperazione(connect, sql);

            if (tRs.next())
                result = true;

            return result;

        } finally {
            DBConnessione.rilasciareConnessione(connect);
        }
    }

    /**
     * Inserisce un nuovo indirizzo nella tabella address.
     * 
     * @param pIndirizzo L'indirizzo da inserire.
     * 
     * @throws SQLException
     * @throws ConnessioneEccezione
     * @throws CampoObbligatorioEccezione
     * @throws EntitaNonTrovataEccezione
     * @throws ValoreNonValidoEccezione
     */
    public synchronized void inserire(Indirizzo pIndirizzo) throws CampoObbligatorioEccezione, ConnessioneEccezione,
            SQLException, EntitaNonTrovataEccezione, ValoreNonValidoEccezione {
        Connection connect = null;
        try {
// controllo dei campi obbligatori
            if (pIndirizzo.ottenereNome() == null)
                throw new CampoObbligatorioEccezione("Specificare il campo nome");

            connect = DBConnessione.ottenereConnesione();
            if (connect == null)
                throw new ConnessioneEccezione();
            // Prepariamo la stringa Sql
            String sql = "INSERT INTO " + ManagerIndirizzo.TABELLA_INDIRIZZO + " (name) " + "VALUES ("
                    + Utility.eNull(pIndirizzo.ottenereNome()) + ")";

            Utility.eseguireOperazione(connect, sql);

            pIndirizzo.settareIdIndirizzo(Utility.ottenereValoreMassimo("id_address", ManagerIndirizzo.TABELLA_INDIRIZZO));

        } finally {
            // rilascia le risorse

            DBConnessione.rilasciareConnessione(connect);
        }
    }

    /**
     * Elimina un indirizzo dalla tabella address.
     * 
     * @param pIndirizzo L'indirizzo da eliminare.
     * 
     * @throws CampoObbligatorioEccezione
     * @throws EntitaNonTrovataEccezione
     * @throws SQLException
     * @throws ConnessioneEccezione
     * @throws ValoreNonValidoEccezione
     * 
     */
    public synchronized void elimina(Indirizzo pIndirizzo) throws ConnessioneEccezione, SQLException,
            EntitaNonTrovataEccezione, CampoObbligatorioEccezione, ValoreNonValidoEccezione {
        Connection connect = null;

        try {
            // ManagerAddress.getInstance().AddressOnDeleteCascade(pIndirizzo);
            connect = DBConnessione.ottenereConnesione();
            // Prepariamo la stringa SQL
            String sql = "DELETE FROM " + ManagerIndirizzo.TABELLA_INDIRIZZO + " WHERE id_address = "
                    + Utility.eNull(pIndirizzo.ottenereIdIndirizzo());

            Utility.eseguireOperazione(connect, sql);
        } finally {
            // rilascia le risorse
            DBConnessione.rilasciareConnessione(connect);
        }
    }

    public synchronized void assegnareInsegnamentoComeIndirizzo(Indirizzo pIndirizzo, Insegnamento pInsegnamento)
            throws ConnessioneEccezione, SQLException, EntitaNonTrovataEccezione, CampoObbligatorioEccezione,
            ValoreNonValidoEccezione, EntitaDuplicataEccezione {
        Connection connect = null;
        ManagerIndirizzo managerAddress = ManagerIndirizzo.ottenereIstanza();
        if (managerAddress.avereInsegnamento(pInsegnamento, pIndirizzo))
            throw new EntitaDuplicataEccezione("Questo indirizzo ha gia quest'insegnamento associato");

        try {
            // ManagerAddress.getInstance().AddressOnDeleteCascade(pIndirizzo);
            connect = DBConnessione.ottenereConnesione();
            // Prepariamo la stringa SQL
            String sql = "INSERT INTO " + ManagerIndirizzo.TABELLA_INDIRIZZO_AVERE_INSEGNAMENTO + " (id_address, id_teaching) "
                    + " VALUES( " + Utility.eNull(pIndirizzo.ottenereIdIndirizzo()) + " , "
                    + Utility.eNull(pInsegnamento.ottenereId()) + " )";

            Utility.eseguireOperazione(connect, sql);
        } finally {
            // rilascia le risorse
            DBConnessione.rilasciareConnessione(connect);
        }
    }

    public synchronized void rimuovereInsegnamentoComeIndirizzo(Indirizzo pIndirizzo, Insegnamento pInsegnamento)
            throws ConnessioneEccezione, SQLException, EntitaNonTrovataEccezione, CampoObbligatorioEccezione,
            ValoreNonValidoEccezione {
        Connection connect = null;
        ManagerIndirizzo managerAddress = ManagerIndirizzo.ottenereIstanza();
        if (!managerAddress.avereInsegnamento(pInsegnamento, pIndirizzo))
            throw new EntitaNonTrovataEccezione("Questo indirizzo non contiene l'insegnamento da rimuovere");

        try {
            // ManagerAddress.getInstance().AddressOnDeleteCascade(pIndirizzo);
            connect = DBConnessione.ottenereConnesione();
            // Prepariamo la stringa SQL
            String sql = "DELETE FROM " + ManagerIndirizzo.TABELLA_INDIRIZZO_AVERE_INSEGNAMENTO + " WHERE id_address= "
                    + Utility.eNull(pIndirizzo.ottenereIdIndirizzo()) + " AND id_teaching = "
                    + Utility.eNull(pInsegnamento.ottenereId());

            Utility.eseguireOperazione(connect, sql);
        } finally {
            // rilascia le risorse
            DBConnessione.rilasciareConnessione(connect);
        }
    }

    /**
     * Ritorna l'id dell'indirizzo passato come parametro.
     * 
     * @param pIndirizzo L'indirizzo di cui si richiede l'id.
     * @return Ritorna l'id dell'indirizzo passato come parametro.
     * 
     * @throws EntitaNonTrovataEccezione
     * @throws ConnessioneEccezione
     * @throws SQLException
     */
    public synchronized int ottenereIndirizzoId(Indirizzo pIndirizzo)
            throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException {
        int result = 0;
        Connection connect = null;
        try {
            if (pIndirizzo == null)
                throw new EntitaNonTrovataEccezione("Impossibile trovare l'indirizzo!");

            /*
             * Prepariamo la stringa SQL per recuperare le informazioni corrispondenti
             * all'id dell'indirizzo passato come parametro.
             */
            String tSql = "SELECT id_address FROM " + ManagerIndirizzo.TABELLA_INDIRIZZO + " WHERE name = "
                    + Utility.eNull(pIndirizzo.ottenereNome());

            // Otteniamo una Connessione al DataBase
            connect = DBConnessione.ottenereConnesione();
            if (connect == null)
                throw new ConnessioneEccezione();

            // Inviamo la Query al DataBase
            ResultSet tRs = Utility.queryOperazione(connect, tSql);

            if (tRs.next())
                result = tRs.getInt("id_address");

            return result;
        } finally {
            DBConnessione.rilasciareConnessione(connect);
        }
    }

    /**
     * Ritorna l'indirizzo corrispondente all'id passato come parametro.
     * 
     * @param pIdIndirizzo L'id dell'indirizzo.
     * @return Ritorna l'indirizzo associato all'id passato come parametro.
     * 
     * @throws ConnessioneEccezione
     * @throws SQLException
     * @throws EntitaNonTrovataEccezione
     * @throws ValoreNonValidoEccezione
     */
    public synchronized Indirizzo ottenereIndirizzoPerId(int pIdIndirizzo)
            throws ConnessioneEccezione, SQLException, EntitaNonTrovataEccezione, ValoreNonValidoEccezione {
        Indirizzo result = null;
        Connection connect = null;
        try {

            if (pIdIndirizzo <= 0)
                throw new EntitaNonTrovataEccezione("Impossibile trovare l'indirizzo!");

            // Otteniamo una Connessione al DataBase
            connect = DBConnessione.ottenereConnesione();
            if (connect == null)
                throw new ConnessioneEccezione();

            // Prepariamo la stringa SQL
            String sql = "SELECT * FROM " + ManagerIndirizzo.TABELLA_INDIRIZZO + " WHERE id_address = "
                    + Utility.eNull(pIdIndirizzo);

            // Inviamo la Query al DataBase
            ResultSet pRs = Utility.queryOperazione(connect, sql);

            if (pRs.next())
                result = this.loadRecordFromRs(pRs);
            else
                throw new EntitaNonTrovataEccezione("Impossibile trovare l'utente!");

            return result;
        } finally {
            // rilascia le risorse
            DBConnessione.rilasciareConnessione(connect);
        }
    }

    /**
     * Ritorna l'insieme di tutti gli indirizzi presenti nel database.
     * 
     * @return Ritorna una collection di indirizzi.
     * 
     * @throws ConnessioneEccezione
     * @throws EntitaNonTrovataEccezione
     * @throws SQLException
     * @throws ValoreNonValidoEccezione
     */
    public synchronized Collection<Indirizzo> ottenereIndirizzoElenco()
            throws ConnessioneEccezione, EntitaNonTrovataEccezione, SQLException, ValoreNonValidoEccezione {
        Connection connect = null;
        Collection<Indirizzo> result = new Vector<Indirizzo>();
        ;

        try {
            connect = DBConnessione.ottenereConnesione();
            if (connect == null)
                throw new ConnessioneEccezione();

            // Prepariamo la stringa sql
            String sql = "SELECT * " + " FROM " + ManagerIndirizzo.TABELLA_INDIRIZZO + " ORDER BY id_address";

            // Inviamo la Query al DataBase
            ResultSet tRs = Utility.queryOperazione(connect, sql);

            if (tRs.next())
                result = this.loadRecordsFromRs(tRs);
            return result;
        } finally {
            // rilascia le risorse
            DBConnessione.rilasciareConnessione(connect);
        }
    }

    /**
     * Ritorna il nome dell'indirizzo corrispondente all'id passato come parametro.
     * 
     * @param pIdIndirizzo L'id dell'indirizzo.
     * @return Ritorna una stringa contenente il nome dell'indirizzo.
     * 
     * @throws EntitaNonTrovataEccezione
     * @throws ConnessioneEccezione
     * @throws SQLException
     */
    public synchronized String ottenereNomeIndirizzoPerId(int pIdIndirizzo)
            throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException {
        String result;
        Connection connect = null;
        try {
            // Se non e' stato fornito l'id restituiamo un codice di errore
            if (pIdIndirizzo <= 0)
                throw new EntitaNonTrovataEccezione("Impossibile trovare l'indirizzo!");

            /*
             * Prepariamo la stringa SQL per recuperare le informazioni corrispondenti
             * all'id dell'utente passato come parametro
             */
            String tSql = "SELECT name FROM " + ManagerIndirizzo.TABELLA_INDIRIZZO + " WHERE id_address = "
                    + Utility.eNull(pIdIndirizzo);

            // Otteniamo una Connessione al DataBase
            connect = DBConnessione.ottenereConnesione();
            if (connect == null)
                throw new ConnessioneEccezione();

            // Inviamo la Query al DataBase
            ResultSet tRs = Utility.queryOperazione(connect, tSql);

            if (tRs.next())
                result = tRs.getString("name");
            else
                throw new EntitaNonTrovataEccezione("Impossibile trovare l'indirizzo!");

            return result;
        } finally {
            DBConnessione.rilasciareConnessione(connect);
        }
    }

    /**
     * Ritorna una collection con gli id degli insegnamenti associati all'id passato
     * come parametro.
     * 
     * @param pIdIndirizzo L'id dell'indirizzo.
     * @return Ritorna una collection con di int
     * 
     * @throws EntitaNonTrovataEccezione
     * @throws ConnessioneEccezione
     * @throws SQLException
     * @throws ValoreNonValidoEccezione
     */
    public synchronized Collection<Integer> ottenereInsegnamentiIndirizzo(int pIdIndirizzo)
            throws EntitaNonTrovataEccezione, ConnessioneEccezione, SQLException, ValoreNonValidoEccezione {
        Collection<Integer> result;
        Connection connect = null;
        try {
            // Se non e' stato fornito l'id restituiamo un codice di errore
            if (pIdIndirizzo <= 0)
                throw new EntitaNonTrovataEccezione("Impossibile trovare l'indirizzo!");

            /*
             * Prepariamo la stringa SQL per recuperare le informazioni corrispondenti
             * all'id dell'utente passato come parametro
             */
            String tSql = "SELECT id_teaching FROM " + ManagerIndirizzo.TABELLA_INDIRIZZO_AVERE_INSEGNAMENTO
                    + " WHERE id_address = " + Utility.eNull(pIdIndirizzo);

            // Otteniamo una Connessione al DataBase
            connect = DBConnessione.ottenereConnesione();
            if (connect == null)
                throw new ConnessioneEccezione();

            // Inviamo la Query al DataBase
            ResultSet tRs = Utility.queryOperazione(connect, tSql);

            result = this.loadIntegersFromRs(tRs);
            return result;
        } finally {
            DBConnessione.rilasciareConnessione(connect);
        }
    }

    /*
     * Consente la lettura di un record dal ResultSet.
     */
    private Indirizzo loadRecordFromRs(ResultSet pRs) throws SQLException, ValoreNonValidoEccezione {
        Indirizzo address = new Indirizzo();
        address.settareNome(pRs.getString("name"));
        address.settareIdIndirizzo(pRs.getInt("id_address"));
        return address;
    }

    /*
     * Consente la lettura dei record dal ResultSet.
     */
    private Collection<Indirizzo> loadRecordsFromRs(ResultSet pRs) throws SQLException, ValoreNonValidoEccezione {
        Collection<Indirizzo> result = new Vector<Indirizzo>();
        do {
            result.add(loadRecordFromRs(pRs));
        } while (pRs.next());
        return result;
    }

    private Collection<Integer> loadIntegersFromRs(ResultSet pRs) throws SQLException, ValoreNonValidoEccezione {
        Collection<Integer> result = new Vector<Integer>();
        while (pRs.next()) {
            result.add(pRs.getInt("id_teaching"));
        }
        return result;
    }

}
