package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import java.util.Date;
import moduli.*;

public class CancellaPrenotazione extends MascheraPrenotazione
{  
	Prenotazione p;	   
   	RicercaPrenotazione parent;
   
   	public CancellaPrenotazione(Prenotazione pren, Stanza room, RicercaPrenotazione papa)
    {
        super(room,"","","Cancellazione della prenotazione","Cancella",3, pren.getPensionamento());
        p = pren;
        parent = papa;
        init();
    }
        
    public void init()
    {
    	writeDatiPren(p);
    	for(int i=0; i<11; i++)
    		testo[i].setEditable(false);
    	myCheckbox.setEnabled(false);
    	myCheckbox1.setEnabled(false);
    	for (int i=0; i<checkboxes.length; i++)
    		checkboxes[i].setEnabled(false);
    	testo[9].setText(DateUtils.parseDate(DateUtils.giveStringOfDate(p.getDataPren())));	    
     
    
    	Azione.addActionListener(new ActionListener()
    	{
            public void actionPerformed(ActionEvent e)
            {
				startCancella();
            }       
    	});

    } 

	void startCancella()
	{
		Frame msg;
		(Principale.db).delPrenotazione(p.getIdPrenotazione());	
       	aggiornaDisp(p, Flag.DISPONIBILE);
       	Utils.restoreCommissioni(p.getNumStanza());
       	parent.creaLista(1);
        dispose();
        padre.setEnabled(true);
	} 
}
