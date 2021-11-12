package smos.storage.connectionManagement;


import java.sql.*;

/**
 * Implementazione del thread in grado di mantenere la connessione al database
 * Mysql qualora non vi siano piu connessioni attive (risoluzione del problema
 * dell'autoreconnect di Mysql). Tale classe si occupa anche di richimare il
 * metodo di rilascio delle connessioni attive che non hanno eseguito operazioni
 * in un certo intervallo di tempo.
 */
public class ControlloConnessione extends Thread {
    private static PoolDiConnessioneDataSorgente manager = null;

    private static int waitTimeout;

    /**
     * Crea una nuova istanza del Thread.
     * 
     * @author Di Giorgio Domenico, Cris Malinconico
     * @param pManager
     *            il pool delle connessioni attulamente in esecuzione.
     */
    public ControlloConnessione(PoolDiConnessioneDataSorgente pManager) {
        ControlloConnessione.manager = pManager;
    }

    /**
     * Crea una nuova istanza del Thread.
     * 
     * @param pManager
     *            il pool delle connessioni attulamente in esecuzione.
     * @param pTempo
     *            Il tempo entro cui ristabilire la connessione con mysql prima
     *            che scada.Tale valore dovra essere necessariamente minore del
     *            valore della variabile globale wait_timeout di Mysql.
     */
    public ControlloConnessione(PoolDiConnessioneDataSorgente pManager, int pTempo) {
        ControlloConnessione.waitTimeout = pTempo;
        ControlloConnessione.manager = pManager;
    }

    /**
     * Il thread non fa altro che dormire quando ci sono utenti attivi e
     * mantenere aperta la connessione con MySQL altrimenti.
     * 
     */
    public void run() {
        try {
            while (true) {
                if (manager.dimensioneAttiva() > 0) {
                    this.setPriority(Thread.MAX_PRIORITY);
                    manager.clearActive();
                    this.setPriority(Thread.NORM_PRIORITY);
                }
                if (manager.dimensioneAttiva() == 0) {
                    while (true) {
                        try {
                            manager.chiudiTutteLeConnessioniInPool();
                            Connection con = null;
                            con = manager.getConnection();
                            Statement st = con.createStatement();
                            st.executeQuery("show tables");
                            manager.rilasciare(con);
                            break;
                        } catch (Exception e) {
                            System.out.println("Eccezione geneata "
                                    + "nel Thread ControlConnection:" + e);
                        }
                    }
                    Thread.sleep(waitTimeout);
                } else {
                    Thread.sleep(waitTimeout);
                }
            }
        } catch (InterruptedException ex) {
            System.out.println("Thread ControlConnection interrotto:" + ex);
        }
    }
}
