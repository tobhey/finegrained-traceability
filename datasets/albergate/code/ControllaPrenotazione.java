package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import java.util.Date;
import moduli.*;

public class ControllaPrenotazione extends MascheraPrenotazione
{  
	Prenotazione p;	   
   	PrenotazioniScadute parent;
   
   	public ControllaPrenotazione(Prenotazione pren, Stanza room, PrenotazioniScadute papa)
    {
        super(room,"","","Controlla dei dati della prenotazione"," Rinnova ",5,pren.getPensionamento());
        p = pren;
        parent = papa;
        init();
    }
        
    public void init()
    {
    	writeDatiPren(p);
    	for(int i=0; i<11; i++)
    		testo[i].setEditable(false);
    	testo[9].setText(DateUtils.parseDate(DateUtils.giveStringOfDate(p.getDataPren())));	    
     
    
    	Azione.addActionListener(new ActionListener()
    	{
            public void actionPerformed(ActionEvent e)
            {
				startRinnova();
            	dispose();
            	parent.dispose();
            	startUpdate();

            }       
    	});
    	
    	Cancella.addActionListener(new ActionListener()
    	{
            public void actionPerformed(ActionEvent e)
            {
				startCancella();
				dispose();
            	parent.dispose();
            	startUpdate();

            }       
    	});

    } 

	void startUpdate()
	{
          	Frame supp = (parent.padre).p;
           	((Principale) supp).updateBloccate();
	}

	void startCancella()
	{
		Frame msg;
		ListaDisponibilita elenco_disp = (Principale.db).elencoDisponibilita();
		if (elenco_disp != null)
		{
			(Principale.db).delPrenotazione(p.getIdPrenotazione());
    	  	Utils.aggiornaDisp(elenco_disp, p.getNumStanza(), p.getInizioSogg(), p.getFineSogg(), Flag.DISPONIBILE, Flag.BLOCCATA, true);
       		if (p.getTramiteAgenzia() == Const.SI)
	       		Utils.restoreCommissioni(p.getNumStanza());
    	   	dispose();
       		padre.setEnabled(true);
       	}
       	else
       		msg = new MessageDialog(this, " Problemi con il database nella lettura delle disponibilita! ");
	} 
	
	void startRinnova()
	{
		p.setDataPren(new Date());
		(Principale.db).changePrenotazione(p.getIdPrenotazione(), p.getNumStanza(), 
														p.getNumPers(),p.getNome(),p.getCognome(), 
														p.getNumTel(), p.getInizioSogg(), p.getFineSogg(), p.getDataPren(),
														p.getCaparra(), p.getRichLettoAgg(), p.getPensionamento(),
														p.getTramiteAgenzia(), p.getNomeAgenzia(), p.getRichParticolari());	
		dispose();
		parent.setEnabled(true);		
	
	}
}
