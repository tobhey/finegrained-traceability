package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.*;

public class ModificaStanze extends DescrittoreStanze
{
	public ModificaStanze()
	{
		super("Modifica dei dati delle stanze", "Modifica", 2);
		init();
	}

	public void init()
	{
		testo1.setEditable(true);
		testo2.setEditable(false);
		testo3.setEditable(false);
		testo4.setEditable(false);
		mycheckboxes.setEnabled(false);
		Azione.setEnabled(false);
		
		Azione.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				startAzione();
			}
		});
      
		testo1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				startTesto1();
			}			      	
		});
		
		Cerca.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				startTesto1();
			}			      	
		});
    } // init
	
	void startAzione()
	{
		MessageDialog msg;
		
		if (!errori())
		{
			Stanza room = readDatiStanza();					
			int j;
			if( (j = (Principale.db).changeStanza(room.getNumStanza(), room.getPostiLetto(), 
							room.getDispLettoAgg(), room.getPxBase(), 
							room.getDescrizione())) == DataBase.OK)
			{
				cleanFields();
				testo1.setEditable(true);
				testo2.setEditable(false);
				testo3.setEditable(false);
				testo4.setEditable(false);
				mycheckboxes.setEnabled(false);
				Azione.setEnabled(false);
			}
			else
				msg = new MessageDialog(this, "Problemi con il data base: "+DataBase.strErrore(j));
		}
	}

	void startTesto1()
	{
		MessageDialog msg;

		Stanza room;
		if ((room = (Principale.db).readStanza(testo1.getText())) != null)
		{
			writeDatiStanza(room);
			testo1.setEditable(false);
			testo2.setEditable(true);
			testo3.setEditable(true);
			testo4.setEditable(true);
			mycheckboxes.setEnabled(true);
			Azione.setEnabled(true);
		}
		else
			msg = new MessageDialog(this, "Stanza inesistente!");
	}
}
