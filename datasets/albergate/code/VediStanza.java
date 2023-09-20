package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.*;

public class VediStanza extends DescrittoreStanze
{
	public VediStanza()
	{
		super("Visualizzazione dei dati delle stanze", "Cerca",4);
		init();
	}

	public void init()
	{
		testo1.setEditable(true);
		testo2.setEditable(false);
		testo3.setEditable(false);
		testo4.setEditable(false);
		mycheckboxes.setEnabled(false);

		
		
		Azione.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				startCerca();
			}
		});
		
		testo1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				startCerca();
			}
		});
	} // init
	
	void startCerca()
	{
		MessageDialog msg;
		
		Stanza room;
		if ((room = (Principale.db).readStanza(testo1.getText())) != null)
		{
			writeDatiStanza(room);
			Azione.setEnabled(true);
		}
		else
			msg = new MessageDialog(this, "Stanza inesistente!");
	}
}
