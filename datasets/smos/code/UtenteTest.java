package smos.storage;

import java.sql.SQLException;
import java.util.Collection;

import smos.bean.Indirizzo;
import smos.bean.Insegnamento;
import smos.bean.VoceElencoUtenti;
import smos.bean.Voto;
import smos.exception.EntitaNonTrovataEccezione;
import smos.exception.ValoreNonValidoEccezione;
import smos.exception.CampoObbligatorioEccezione;
import smos.storage.connectionManagement.exception.ConnessioneEccezione;

public class UtenteTest {

    /**
     * @param args
     * @throws ValoreNonValidoEccezione
     * @throws ConnessioneEccezione
     * @throws EntitaNonTrovataEccezione
     * @throws SQLException
     * @throws CampoObbligatorioEccezione
     */
    public static void main(String[] args) throws SQLException, EntitaNonTrovataEccezione, ConnessioneEccezione,
            ValoreNonValidoEccezione, CampoObbligatorioEccezione {

        VoceElencoUtenti temp = new VoceElencoUtenti();
        temp.settareId(5);
        Insegnamento teaching = new Insegnamento();
        teaching.settareId(3);
        ManagerVoto mv = ManagerVoto.ottenereIstanza();
        Voto ghh = new Voto();
        ghh.settareAnnoAccademico(2134);
        ghh.settareId_utente(88);
        ghh.settareLaboratorio(3);
        ghh.settareOrale(4);
        ghh.settareInsegnamento(9);
        ghh.settareTurno(2);
        ghh.settareScritto(3);
        mv.inserire(ghh);
    }
}
