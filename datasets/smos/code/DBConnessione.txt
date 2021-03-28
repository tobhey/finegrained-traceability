/*
 * DBConnection
 *
 */

package smos.storage.connectionManagement;

import smos.Ambiente;
import smos.utility.Utility;

import java.sql.*;
import java.util.*;
import java.io.*;


/**
 * Classe che s'interfaccia con il pool di connessioni. In particolare crea un
 * unico oggetto ConnectionPoolDataSource (il pool di connessioni) ed ottiene i
 * suoi parametri di configurazione dal file di properties
 * connection.properties.
 */
public class DBConnessione {

    private static int ACTIVE_TIMEOUT;

    private static String DRIVER_MYSQL = "";

    private static String FULL_PATH_DATABASE = "";

    private static ControlloConnessione linker = null;

    private static PoolDiConnessioneDataSorgente manager = null;

    private static int MAX_POOL_SIZE;

    private static String PASSWORD = "";

    private static int POOL_TIMEOUT;

    private static Properties properties = null;

    private static String USER_NAME = "";

    private static int WAIT_TIMEOUT;

    
   
    
    
    /**
     * Blocco d'inizializzazione statico che si occupa di generare il pool nel
     * momento in cui ci sara una prima invocazione del metodo getConnection()
     */
    static {
        try {
            properties = new Properties();
            File fileProp = new File(Ambiente.ottenerePoolPropertiesPath());

            if (fileProp.exists()) {
                properties.load(new FileInputStream(fileProp));

                DRIVER_MYSQL = properties.getProperty("connection.jdbc.name");
                if (DRIVER_MYSQL.equals("")) {
                    DRIVER_MYSQL = Utility.ottenereDriverMySql();
                }

                FULL_PATH_DATABASE = properties
                        .getProperty("connection.jdbc.fullPath");

                if (FULL_PATH_DATABASE.equals("")) {
                    FULL_PATH_DATABASE = Utility.ottenereCompletoPathDatabase();
                }

                USER_NAME = properties.getProperty("connection.username");
                if (USER_NAME.equals("")) {
                    USER_NAME = Utility.ottenereUtenteNome();
                }

                PASSWORD = properties.getProperty("connection.password");
                if (PASSWORD.equals("")) {
                    PASSWORD = Utility.ottenerePassword();
                }

                try {
                    MAX_POOL_SIZE = Integer.parseInt(properties
                            .getProperty("connection.maxPoolSize"));
                } catch (Exception ex) {
                    MAX_POOL_SIZE = Utility.ottenereDimensioneMassimaPool();
                }

                try {
                    WAIT_TIMEOUT = Integer.parseInt(properties
                            .getProperty("connection.waitTimeout"));
                } catch (Exception ex) {
                    WAIT_TIMEOUT = Utility.ottenereAspettareTimeout();
                }

                try {
                    ACTIVE_TIMEOUT = Integer.parseInt(properties
                            .getProperty("connection.activeTimeout"));
                } catch (Exception ex) {
                    ACTIVE_TIMEOUT = Utility.ottenereAttivoTimeout();
                }

                try {
                    POOL_TIMEOUT = Integer.parseInt(properties
                            .getProperty("connection.poolTimeout"));
                } catch (Exception ex) {
                    POOL_TIMEOUT = Utility.ottenerePoolTimeout();
                }
            } else {
                /* Se il file di properties non esiste carica valori di default */

                DRIVER_MYSQL = Utility.ottenereDriverMySql();
                FULL_PATH_DATABASE = Utility.ottenereCompletoPathDatabase();
                USER_NAME = Utility.ottenereUtenteNome();
                PASSWORD = Utility.ottenerePassword();
                MAX_POOL_SIZE = Utility.ottenereDimensioneMassimaPool();
                WAIT_TIMEOUT = Utility.ottenereAspettareTimeout();
                ACTIVE_TIMEOUT = Utility.ottenereAttivoTimeout();
                POOL_TIMEOUT = Utility.ottenerePoolTimeout();
            }

            loadPool(); // Crea il manager e prepara il pool di connessioni

        } catch (Exception e) {
            /* Se un'eccezione viene generata in precedenza */

        	DRIVER_MYSQL = Utility.ottenereDriverMySql();
            FULL_PATH_DATABASE = Utility.ottenereCompletoPathDatabase();
            USER_NAME = Utility.ottenereUtenteNome();
            PASSWORD = Utility.ottenerePassword();
            MAX_POOL_SIZE = 100;
            WAIT_TIMEOUT = 2000;
            ACTIVE_TIMEOUT = 240000;
            POOL_TIMEOUT = 300000;
            loadPool(); // Crea il manager e prepara il pool di connessioni
        }

    }

    /**
     * Restituisce una connessione dal pool.
     * 
     * @return la connessione se possibile null altrimenti
     */
    public static Connection ottenereConnesione() {
        try {
            return manager.getConnection();
        } catch (SQLException e) {
            System.out.println("Eccezione generata"
                    + "in DBConnection.getConnection() " + e);
            return null;
        }
    }

    /**
     * Creazione effettiva del pool di connessione.
     * 
     */
    private static void loadPool() {
        try {
            manager = new PoolDiConnessioneDataSorgente(DRIVER_MYSQL,
                    FULL_PATH_DATABASE, USER_NAME, PASSWORD, MAX_POOL_SIZE,
                    POOL_TIMEOUT);
            manager.settareAttivoTimeout(ACTIVE_TIMEOUT);
            linker = new ControlloConnessione(manager, WAIT_TIMEOUT);
            linker.start();
        } catch (Exception e) {
            System.out.println("Impossibile creare il pool"
                    + "di connessioni in DBConnection:" + e);
        }
    }

    /**
     * Restituisce una connessione al pool che sara inserita nella lista delle
     * connesioni pool, ossia quelle riutilizzabili in seguito.
     * 
     * @param pConnessione
     *            la connessione non piu attiva.
     */
    public static void rilasciareConnessione(Connection pConnessione) {
        manager.rilasciare(pConnessione);
    }

}
