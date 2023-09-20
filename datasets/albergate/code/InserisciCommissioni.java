package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.*;

public class InserisciCommissioni extends MascheraCommissioni
{
	MascheraRicercaStanza mask_ricerca = new MascheraRicercaStanza("",1); 
   	
   	public InserisciCommissioni(Stanza stanza, String data1, String data2)
   	{
      	super(data1, data2, "Inserimento dei dati delle commissioni","Inserisci",1);
      	init(stanza, data1, data2);
   	}

 	public void init(Stanza stanza, String data1, String data2)
 	{     	
      	num_stanza.setText(stanza.getNumStanza());
        inizio_comm.setText(data1);
        fine_comm.setText(data2);
        num_stanza.setEditable(false);
        inizio_comm.setEditable(false);
        fine_comm.setEditable(false);
		
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
		
		if ( !errori() )
		{
			Commissionamento comm = readDatiComm();
			int j;
			if ( (j = (Principale.db).newIdCommissionamento()) == DataBase.OK )
			{
				(Principale.config).updateIdCommissionamento();
				if ((j = (Principale.db).writeCommissionamento(comm)) == DataBase.OK)
				{
					aggiornaDisp(comm, Flag.COMMISSIONATA);
					dispose();
					mask_ricerca.restartAvvio();
					mask_ricerca.setEnabled(true);
				}
				else
					msg = new MessageDialog(this, "Problemi con il database: "+DataBase.strErrore(j) );
			}
			else
				msg = new MessageDialog(this, "Problemi con il database: "+DataBase.strErrore(j) );
		}
	}
}
