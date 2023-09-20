package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.*;
import moduli.Commissionamento;

public class VediCommissione extends MascheraCommissioni
{
	Commissionamento commissione;
   	
   	public VediCommissione()
   	{
      	super("","","Visualizzazione dati delle commissioni"," Cerca ",4);
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
      	
		lista.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
         		commissione = L.getCommissionamento((lista.getSelectedIndexes())[0]+1);
				writeDatiComm(commissione);
				Azione.setEnabled(true);
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
				creaLista();
			}
		});	
	}
}
