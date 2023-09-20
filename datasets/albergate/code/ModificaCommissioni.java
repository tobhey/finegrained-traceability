package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.*;
import moduli.Commissionamento;

public class ModificaCommissioni extends MascheraCommissioni
{
	Commissionamento commissione;
   	
   	public ModificaCommissioni()
   	{
      	super("", "", "Modifica i dati delle commissioni", "Modifica", 2);
      	nome_agenzia.setEditable(true);
      	deabilita();
      	init();
   	}

 	public void init()
 	{     	
		lista.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
         		commissione = L.getCommissionamento(lista.getSelectedIndex()+1);
				writeDatiComm(commissione);
				abilita();
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

    	Azione.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
         		startAzione();
			}
      	});
      
     	Cerca.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
         		creaLista();
			}
      	});	
	}
	
	void deabilita()
	{
      	num_stanza.setEditable(false);
      	num_tel_agenzia.setEditable(false);
        inizio_comm.setEditable(false);
        fine_comm.setEditable(false);
        scadenza_comm.setEditable(false); 
       	num_stanza.setEditable(false);
       	Azione.setEnabled(false);
	}
	
	void abilita()
	{
      	num_tel_agenzia.setEditable(true);
        scadenza_comm.setEditable(true); 
       	Azione.setEnabled(true);
	}
	
	void startAzione()
	{
		Frame msg;
		
		if ( !errori() )
		{
			Commissionamento comm = readDatiComm();
			int j;
			if (( j = (Principale.db).changeCommissionamento(commissione.getIdCommissionamento(), comm.getNumStanza(), comm.getNomeAgenzia(),comm.getNumTel(),
									 		comm.getInizioComm(), comm.getFineComm(), comm.getScadenzaComm(), comm.getScaduto())) == DataBase.OK)
			{						
				cleanFields();
				deabilita();
			}
			else
				msg = new MessageDialog(this, "Problemi con il database: "+DataBase.strErrore(j));
		}
	}
}
