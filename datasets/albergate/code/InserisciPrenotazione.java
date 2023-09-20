package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import java.util.Date;
import moduli.*;

public class InserisciPrenotazione extends MascheraPrenotazione
{  
    MascheraRicercaStanza mask_ricerca = new MascheraRicercaStanza("",2); 
    String agenzia;
    public InserisciPrenotazione(Stanza room, String data_i , String data_f, String nome_ag)
    {
        super(room, data_i, data_f, "Inserimento dei dati della prenotazione", "Inserisci",1, Flag.PENSIONE_COMPLETA);
        agenzia = new String(nome_ag);
        init(room, data_i, data_f);
    }
                     
	public void init(Stanza room, String data_i, String data_f)
 	{     	
        testo[0].setText(room.getNumStanza());
        testo[0].setEditable(false);
        testo[4].setText(DateUtils.parseDate(data_i));
        testo[5].setText(DateUtils.parseDate(data_f));
        testo[4].setEditable(false);
        testo[5].setEditable(false);
        testo[7].setText(room.getDescrizione());
    	testo[9].setText(DateUtils.parseDate(DateUtils.giveStringOfDate(new Date())));    
		testo[8].setText(agenzia);	      	
		testo[8].setEditable(false);
		if (!agenzia.equals(""))
		{
			testo[6].setEditable(false);
			myCheckbox1.setState(true);
		}
		      	
      	Annulla.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
				dispose();
				mask_ricerca.setEnabled(true);
			}
      });

      Azione.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
				startAzione();	
      		}
      });
   }

	void startAzione()
	{
		Frame msg;
		AskDialog ask;
		int answer = 0;
		
		if ( !errori() )
		{
			Prenotazione pren = readDatiPren();
			pren.setDataPren(new Date());
			int j;
			if ( (j = (Principale.db).newIdPrenotazione()) == DataBase.OK )
			{
				(Principale.config).updateIdPrenotazione();
				if ((j = (Principale.db).writePrenotazione(pren)) == DataBase.OK)
				{
					if (!myCheckbox1.getState())
						ask = new AskDialog(this, " Assegnare la stanza o Bloccarla fino al ricevimento della caparra? ", " Assegnare ", " Bloccare ", pren);
					else
			            startAggiornaDisp(pren, Flag.ASSEGNATA);
				}
				else
				 	msg = new MessageDialog(this, "Problemi con il database: "+DataBase.strErrore(j) );
			}
			else
				msg = new MessageDialog(this, "Problemi con il database: "+DataBase.strErrore(j) );
		}
	}
	
	void startAggiornaDisp(Prenotazione p, char tipo)
	{
		aggiornaDisp(p, tipo);
		dispose();
		mask_ricerca.restartAvvio();
		mask_ricerca.setEnabled(true);
	} 
}
