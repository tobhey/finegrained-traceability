package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import java.util.Date;
import moduli.*;

public class VediPrenotazione extends MascheraPrenotazione
{  
	Prenotazione p;	   
   	RicercaPrenotazione parent;
    char flag;
    
   	public VediPrenotazione(Prenotazione pren, Stanza room, RicercaPrenotazione papa)
    {
        super(room,"","","Visualizzazione dei dati della prenotazione","",4, pren.getPensionamento());
        p = pren;
        parent = papa;
        init();
    }
        
    public void init()
    {
    	Frame msg;
    	
    	writeDatiPren(p);
    	for(int i=0; i<11; i++)
    		testo[i].setEditable(false);
    	myCheckbox.setEnabled(false);
    	myCheckbox1.setEnabled(false);
    	for (int i=0; i<checkboxes.length; i++)
    		checkboxes[i].setEnabled(false);
    	testo[9].setText(DateUtils.parseDate(DateUtils.giveStringOfDate(p.getDataPren())));	    
     	panel2.remove(Azione);
    } 
}
