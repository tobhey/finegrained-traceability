package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.*;

public class CancellaStanze extends DescrittoreStanze
{
	public CancellaStanze()
	{
		super("Cancellazione delle stanze", "Cancella", 3);
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
		
		int j;
		if ( (j = (Principale.db).delStanza(testo1.getText())) != DataBase.OK )
			msg = new MessageDialog(this, "Problemi con il data base: "+DataBase.strErrore(j));
		else
		{
			cleanFields();
			Azione.setEnabled(false);
		}
		testo1.setEditable(true);
	}
	
	void startTesto1()
	{
		MessageDialog msg;
		
		Stanza room;
		if ((room = (Principale.db).readStanza(testo1.getText())) != null)
		{
			writeDatiStanza(room);
			testo1.setEditable(false);
			Azione.setEnabled(true);
		}
		else
			msg = new MessageDialog(this, "Stanza inesistente!");
	}
}
