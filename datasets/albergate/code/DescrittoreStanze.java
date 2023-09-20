package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.*;

public class DescrittoreStanze extends Frame
{
	//Dichiaro gli oggetti che utilizzero' per costruire la finestra
	Label etichetta, label1, label2, label3, label4;
	TextField testo1, testo2, testo3, testo4;
	Button Annulla, Azione, Cerca;
	Panel panel1, panel2;
	GridBagLayout gridbag = new GridBagLayout();
	Checkbox mycheckboxes;
	SubStanze stanze = new SubStanze();		
    int caller;  
	public DescrittoreStanze(String title, String caption, int c)
	{
		super(title);
		caller = c;
		setupPanels(caption);
		inizializza();
		pack();
	}

	void setupPanels(String caption)
	{
		this.setFont(ConfigurazioneSistema.font_base);
		//Creo  le etichette
		etichetta=new Label("Informazioni sulle stanze");
		etichetta.setFont(ConfigurazioneSistema.font_titolo);
		label1 = new Label("Numero di stanza");
		label2 = new Label("Numero di posti letto");
		label3 = new Label("Prezzo base della stanza "+(Principale.config).getValuta()+".");
		label4 = new Label("Descrizione delle caratteristiche peculiari della stanza");
    
		//Creo i TextField
		testo1 = new TextField("", 4);
		testo2 = new TextField("", 4);
		testo3 = new TextField("", 10);
		testo4 = new TextField("", 50);
   
		//Creo i bottoni
		Annulla = new Button(" Fine ");
		Azione = new Button(caption);
		Cerca = new Button(" Cerca ");
		mycheckboxes = new Checkbox(" Letto aggiuntivo", null, false);

		//Creo il pannello in alto contenente i dati del cliente
		panel1 = new Panel();
		panel1.setLayout(gridbag);
		Utils.constrain(panel1, etichetta, 0, 0, 2, 1, GridBagConstraints.NONE,
					GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
		Utils.constrain(panel1, label1, 0, 1, 1, 1, GridBagConstraints.NONE,
					GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
		Utils.constrain(panel1, testo1, 1, 1, 1, 1, GridBagConstraints.NONE,
					GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
		Utils.constrain(panel1, label2, 2, 1, 1, 1, GridBagConstraints.NONE,
					GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 25);
		Utils.constrain(panel1, testo2, 3, 1, 1, 1, GridBagConstraints.NONE,
					GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
		Utils.constrain(panel1, label3, 0, 2, 1 ,1, GridBagConstraints.NONE,
					GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
		Utils.constrain(panel1, testo3, 1, 2, 1, 1, GridBagConstraints.NONE,
					GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
		Utils.constrain(panel1, mycheckboxes, 2, 2, 1, 1, GridBagConstraints.NONE,
					GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
		Utils.constrain(panel1, label4, 0, 3, 2, 1, GridBagConstraints.NONE,
					GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
		Utils.constrain(panel1, testo4, 2, 3, 3, 1, GridBagConstraints.NONE,
					GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);

		//Creo il pannello in basso con due pulsanti
		panel2 = new Panel();
		panel2.setLayout(gridbag);
		Utils.constrain(panel2, Annulla, 0, 0, 1, 1, GridBagConstraints.NONE,
					GridBagConstraints.CENTER, 0, 0.0, 5, 5, 5, 5);
		Utils.constrain(panel2, Azione, 1, 0, 1, 1, GridBagConstraints.NONE,
					GridBagConstraints.CENTER, 0, 0.0, 5, 5, 5, 5);
		if ((caller == 2) || (caller==3))
		Utils.constrain(panel2, Cerca, 2, 0, 1, 1, GridBagConstraints.NONE,
					GridBagConstraints.CENTER, 0, 0.0, 5, 5, 5, 5);			

		
		//Attacco i pannelli al frame
		this.setLayout(gridbag);
		Utils.constrain(this, panel1, 0, 0, 4, 4, GridBagConstraints.HORIZONTAL,
					GridBagConstraints.NORTH, 1.0, 1.0, 5, 5, 5, 5);
		Utils.constrain(this,panel2, 0, 4, 4, 1, GridBagConstraints.HORIZONTAL,
					GridBagConstraints.CENTER, 1.0, 0.0, 5, 5, 5, 5);
	}

	public void inizializza()
	{
		Annulla.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
				stanze.setEnabled(true);
			}
		});
	}
	
	protected Stanza readDatiStanza()
	{
		Stanza room = new Stanza();
		room.setNumStanza(testo1.getText());
		room.setPostiLetto(Integer.parseInt(testo2.getText()));
		Float f = Float.valueOf(testo3.getText());
		room.setPxBase(f.floatValue());
		room.setDescrizione(testo4.getText());
		if 	(mycheckboxes.getState())
			room.setDispLettoAgg(Const.SI);
		return room;	 
	}

	protected void writeDatiStanza(Stanza room)
	{
		testo1.setText(room.getNumStanza());
		testo2.setText(""+room.getPostiLetto());
		testo3.setText(""+room.getPxBase());
		testo4.setText(room.getDescrizione());    	
		if ( room.getDispLettoAgg() == Const.SI)
			mycheckboxes.setState(true);
		else
			mycheckboxes.setState(false);	
	}
    
	protected void cleanFields()
	{
		testo1.setText("");
		testo2.setText("");
		testo3.setText("");
		testo4.setText("");
		mycheckboxes.setState(false);	
	}
	
	protected boolean errori()
	{
		Frame msg;
		
		if ( !((testo1.getText()).length() <= 4) )
		{
			msg = new MessageDialog(this, " Il numero della stanza deve essere composto da al piu' 4 caratteri! ");
			return true;	
		}
		if ( ((testo1.getText()).equals("")) )
		{
			msg = new MessageDialog(this, " Manca il numero della stanza! ");
			return true;	
		}
		if ( ((testo2.getText()).equals("")) )
		{
			msg = new MessageDialog(this, " Manca il numero di posti letto! ");
			return true;	
		}
		if ( !Utils.isIntPos(testo2.getText()) )
		{
			msg = new MessageDialog(this, " Il numero di posti deve essere un numero positivo! ");
			return true;	
		}
		if ( ((testo3.getText()).equals("")) )
		{
			msg = new MessageDialog(this, " Manca il prezzo della stanza! ");
			return true;	
		}
		if ( !Utils.isFloatPos(testo3.getText()) )
		{
			msg = new MessageDialog(this, " Il prezzo della stanza deve essere un numero positivo! ");
			return true;	
		}
		if ( !((testo4.getText()).length() <= 52) )
		{
			msg = new MessageDialog(this, " La descrizione deve essere composta da al piu' 52 caratteri! ");
			return true;	
		}
		return false;
	}
}
