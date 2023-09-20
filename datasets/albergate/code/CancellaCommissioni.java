package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.*;
import moduli.Commissionamento;

public class CancellaCommissioni extends MascheraCommissioni
{
	Commissionamento commissione;
   	
   	public CancellaCommissioni()
   	{
      	super("","","Cancella dati delle commissioni","Cancella", 3);
      	init();
   	}

 	public void init()
 	{     	
      	nome_agenzia.setEditable(true);
      	num_stanza.setEditable(false);
      	num_tel_agenzia.setEditable(false);
        inizio_comm.setEditable(false);
        fine_comm.setEditable(false);
        scadenza_comm.setEditable(false); 
       	num_stanza.setEditable(false);
       	Azione.setEnabled(false);
      	
		lista.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
         		commissione = L.getCommissionamento((lista.getSelectedIndexes())[0]+1);
				writeDatiComm(commissione);
				Azione.setEnabled(true);
				panel5.setVisible(false);
				pack();
         	}
      	});
   
		nome_agenzia.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				creaLista();
			}			      	
		});
		
		Cerca.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				creaLista();
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
			int j;
			if ( (j = (Principale.db).delCommissionamento(commissione.getIdCommissionamento())) == DataBase.OK)
			{	
				aggiornaDisp(commissione, Flag.DISPONIBILE);
				num_tel_agenzia.setEditable(false);
				scadenza_comm.setEditable(false);
				Azione.setEnabled(false);						
				cleanFields();
				nome_agenzia.setEditable(true);
			}
			else
				msg = new MessageDialog(this, "Problemi con il database: "+DataBase.strErrore(j));
		}
	}
}
