package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.*;

public class VediAddebito extends Frame
{
   	TextField num_stanza, desc, quantita, tot, tot_addebiti;
   	Label etich1, etich2, etich3, etich4, etich5, etich6;
   	Panel panel1, panel2;
   	Button Annulla;
   	GridBagLayout gridbag = new GridBagLayout();
   	Frame padre = new Frame();
	Addebito add;
  	float addebiti_totali; 	
   	public VediAddebito(Frame p, Addebito a, float tot)
   	{
      	super("Visualizzazione dei dati dell'addebito scelto");
      	padre = p;
      	add = a;
      	addebiti_totali = tot;
      	padre.setEnabled(false);
      	setupPanels();
      	inizializza();
      	pack();
      	setVisible(true);
   	}

   	void setupPanels()
   	{
	    this.setFont(ConfigurazioneSistema.font_base);
      	//Creo le etichette
      	etich1 = new Label("Dati dell'addebito");
      	etich1.setFont(ConfigurazioneSistema.font_titolo);
      	etich2 = new Label("Numero Stanza");
      	etich3 = new Label("Descrizione");
      	etich4 = new Label("Quantita");
      	etich5 = new Label("Addebito relativo");
      	etich6 = new Label("Totale addebiti sulla stanza "+add.getNumStanza());
      	etich6.setFont(ConfigurazioneSistema.font_titolo);
      
      	//Creo i TextField e ne rendo  alcuni non editabili
      	num_stanza = new TextField("",4);
      	desc = new TextField("",32);
     	quantita = new TextField("",6);
      	tot = new TextField("",8);
      	tot_addebiti = new TextField("",10);
      	
      	//Disabilita i TextField
      	num_stanza.setEditable(false);
      	desc.setEditable(false);
     	quantita.setEditable(false);
      	tot.setEditable(false);
      	tot_addebiti.setEditable(false);

      	//Creo i pulsanti
      	Annulla = new Button(" Indietro ");

      	//Creo il pannello
      	panel1 = new Panel();
      	panel1.setLayout(gridbag);
      	Utils.constrain(panel1, etich1, 0, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0); 
      	Utils.constrain(panel1, etich2, 0, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
 	    Utils.constrain(panel1,num_stanza, 1, 1, 1, 1, GridBagConstraints.NONE,
                    GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1, etich3, 0, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1,desc, 1, 2, 2, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1, etich4, 0, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1,quantita , 1, 3, 1, 1, GridBagConstraints.NONE,
                    GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1, etich5, 2, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1,tot , 3, 3, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1, etich6, 0, 4, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 15, 5, 5, 5);
      	Utils.constrain(panel1, tot_addebiti, 2, 4, 1, 1, GridBagConstraints.NONE,
                    GridBagConstraints.NORTHWEST, 1.0, 0.0, 15, 5, 5, 5);

      	panel2 = new Panel();
     	panel2.setLayout(gridbag);
      	Utils.constrain(panel2, Annulla, 0, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 0, 0, 0, 15);
		
      	//Attacco i pannelli al frame
      	this.setLayout(gridbag);
      	Utils.constrain(this, panel1, 0, 0, 6, 5, GridBagConstraints.NONE,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(this, panel2, 0, 5, 6, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5);
   }

   public void inizializza()
   {
      writeDatiTel();
      
      Annulla.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
			dispose();
			padre.setEnabled(true);
         }
      });
   }

	void writeDatiTel()
	{
      	String d = new String("");
      	BeneServizio extra = (Principale.db).readBeneServizio(add.getCodExtra());
		if (extra == null)
			d = new String("");
		else
		d = new String(extra.getDescrizione());	
      	num_stanza.setText(add.getNumStanza());
      	desc.setText(d);
     	quantita.setText(""+add.getQuantita());
      	tot.setText(""+add.getTotAddebito());
      	tot_addebiti.setText(""+addebiti_totali);
	}
}
