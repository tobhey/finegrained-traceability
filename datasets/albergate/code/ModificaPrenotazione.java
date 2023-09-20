package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import java.util.Date;
import moduli.*;

public class ModificaPrenotazione extends MascheraPrenotazione
{  
    Prenotazione p;
    RicercaPrenotazione parent;
	    
    public ModificaPrenotazione(Prenotazione pren, Stanza room, RicercaPrenotazione papa)
    {
        super(room,"","","Modifica dei dati della prenotazione","Modifica",2, pren.getPensionamento());
        p= pren;
        parent = papa;
        init();
    }
                     
    public void init()
    {
		Frame msg;
		writeDatiPren(p);
		testo[0].setEditable(false);
   		testo[4].setEditable(false);
   		testo[5].setEditable(false);
		testo[9].setText(DateUtils.parseDate(DateUtils.giveStringOfDate(p.getDataPren())));
		if (!p.getNomeAgenzia().equals(""))
		{
			testo[6].setEditable(false);
			myCheckbox1.setState(true);
		}

    	Azione.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
				if ( !errori() )
				{
					Prenotazione pren = readDatiPren();
					pren.setIdPrenotazione(p.getIdPrenotazione());
					pren.setDataPren(p.getDataPren());
					int j ;
					j = (Principale.db).changePrenotazione(pren.getIdPrenotazione(), pren.getNumStanza(), 
														pren.getNumPers(),pren.getNome(),pren.getCognome(), 
														pren.getNumTel(), pren.getInizioSogg(), pren.getFineSogg(), pren.getDataPren(),
														pren.getCaparra(), pren.getRichLettoAgg(), pren.getPensionamento(),
														pren.getTramiteAgenzia(), pren.getNomeAgenzia(), pren.getRichParticolari());	
					dispose();
					parent.creaLista(1);
					parent.setEnabled(true);
				}
			}
      });
      
      Assegna.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
				startAssegnazione();
			}
      	});	

    } 
	
	void startAssegnazione()
	{
		Frame msg;
		ListaDisponibilita elenco_disp = (Principale.db).elencoDisponibilita();
		if (elenco_disp == null)
		{
			msg = new MessageDialog(this, " Problemi con il database nella lettura delle disponibilita'! ");
			return;
		}
		Utils.aggiornaDisp(elenco_disp,p.getNumStanza(),p.getInizioSogg(),p.getFineSogg(),Flag.ASSEGNATA,Flag.BLOCCATA,true);
		msg = new AvvisoDialog(this, " Assegnata la stanza "+p.getNumStanza()+" alla prenotazione ");
	}
}
