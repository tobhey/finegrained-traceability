package smos;

/**
 * Classe utilizzata per contenere le variabili d'ambiente di GESA 
 */
public class Ambiente {

    /**
     * Messaggio di errore di default.
     */
    public static String DEFAULT_MESSAGIO_ERRORE = "Un errore si e' verificato durante l'elaborazione della richiesta.<br><br>";

    private static String poolPropertiesPath = "";

    /**
     * @return getPoolPropertiesPath()
     */
    public static String ottenerePoolPropertiesPath() {
        return poolPropertiesPath;
    }
    
    /**
     * @param poolPropertiesPath
     */
    public static void settarePoolPropertiesPath(String poolPropertiesPath) {
        Ambiente.poolPropertiesPath = poolPropertiesPath;
    }
}