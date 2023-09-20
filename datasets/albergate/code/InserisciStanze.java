package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.*;

public class InserisciStanze extends DescrittoreStanze
{
	public InserisciStanze()
	{
		super("Inserimento dei dati delle stanze", "Inserisci",1);
		init();
	}
	
	public void init()
	{
		Cerca.setEnabled(false);
		
		Azione.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				startAzione();
			}
		});
	} // init

	void startAzione()
	{
		Frame msg;
		ListaStanze L_st = (Principale.db).elencoStanze();
		if ( L_st.length() < (Principale.config).getNumStanze() ) 
		{	
			if ( !errori() )
			{
				Stanza room = readDatiStanza();
				int j;
				if ((j = (Principale.db).writeStanza(room)) == DataBase.OK)
				{
					cleanFields();
				}
				else
					msg = new MessageDialog(this, "Problemi con il data base: "+DataBase.strErrore(j));
			}
		}
		else
			msg = new AvvisoDialog(this, "Impossibile inserire la stanza: e' stato raggiunto il numero massimo di stanze gestibile");			
	}
}
