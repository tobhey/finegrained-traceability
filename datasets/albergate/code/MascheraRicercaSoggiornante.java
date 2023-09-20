package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.ListaSoggiornanti;

public class MascheraRicercaSoggiornante extends Frame
{
   Button Annulla, Conferma;
   TextField testo[];
   Label etichetta1, etichetta2, etichetta3, etichetta4, etichetta5, label1, label2,
         label3, label5;
   Panel panel[];
   CheckboxGroup checkbox_group;
   Checkbox[] checkboxes;
   GridBagLayout gridbag = new GridBagLayout();
   List list_soggiornanti;
   Frame padre, figlio;
   
   // la seguente variabile serve per tenere in memoria la lista dei clienti
   // cercati nello storico
   ListaSoggiornanti elenco_soggiornanti;
   	
   public MascheraRicercaSoggiornante(Frame parent)
   {
      super("Ricerca soggiornante");
      padre = parent;
      padre.setEnabled(false);
      setupPanels();
      pack();
      init();
      this.setVisible(true);
   }

   void setupPanels()
   {
	  this.setFont(ConfigurazioneSistema.font_base);
      //Creo i pulsanti
      Annulla = new Button("Annulla ");
      //Annulla.setFont(new Font("Courier", Font.PLAIN, 12));
      Conferma = new Button("Conferma");
      Conferma.setEnabled(false);
      //Conferma.setFont(new Font("Courier", Font.PLAIN, 12));

      //Creo le etichette
      etichetta1 = new Label("Selezionare il tipo di ricerca che si desidera effettuare:");
      etichetta1.setFont(ConfigurazioneSistema.font_titolo);
      etichetta2 = new Label("Risultato della ricerca:");
      etichetta2.setFont(ConfigurazioneSistema.font_titolo);
      etichetta3 = new Label("Ricerca per stanza:");
      etichetta3.setFont(ConfigurazioneSistema.font_titolo);
      etichetta4 = new Label("Ricerca per nome:");
      etichetta4.setFont(ConfigurazioneSistema.font_titolo);
      label1 = new Label("Stanza numero");
      label2 = new Label("Cognome");
      label3 = new Label("Nome");

      //Creo un Checkbox ad esclusione
      checkbox_group = new CheckboxGroup();
      checkboxes = new Checkbox[2];
      checkboxes[0] = new Checkbox("Ricerca per stanza",checkbox_group,false);
      checkboxes[1] = new Checkbox("Ricerca per nome",checkbox_group,false);


      //Creo i TextField e li rendo non editabili
      testo = new TextField[3];
      testo[0] = new TextField("", 6);
      testo[1] = new TextField("", 25);
      testo[2] = new TextField("", 25);
      for(int i=0;i<testo.length;++i)
	      testo[i].setEditable(false);

      //creo una TextArea
      list_soggiornanti = new List(10, false);
      list_soggiornanti.setFont(ConfigurazioneSistema.font_allineato);

      panel=new Panel[5];
      for(int i=0;i<panel.length;++i)
      {
         panel[i]=new Panel();
         panel[i].setLayout(gridbag);
      }
      //Creo il pannello in alto a sinistra
      Utils.constrain(panel[1], etichetta1, 0, 0, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 10, 0); 
      Utils.constrain(panel[1], checkboxes[0], 0, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 5, 0, 0); 
      Utils.constrain(panel[1], checkboxes[1], 0, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 5, 0, 0);
            
      //Creo il pannello ricerca per stanza
      Utils.constrain(panel[2], etichetta3, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 10, 0);
      Utils.constrain(panel[2], label1, 0, 1, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 5, 0, 20);
      Utils.constrain(panel[2], testo[0], 1, 1, 1, 1, GridBagConstraints.NONE,
                      GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);

      //Creo il pannello ricerca per nome e cognome
      Utils.constrain(panel[3], etichetta4, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 10, 0);
      Utils.constrain(panel[3], label2, 0, 1, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 5, 0, 20);
      Utils.constrain(panel[3], testo[1], 1, 1, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);
      Utils.constrain(panel[3], label3, 0, 2, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.SOUTHWEST, 0.0, 0.0, 20, 5, 0, 20);
      Utils.constrain(panel[3], testo[2], 1, 2, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.SOUTHWEST, 1.0, 0.0, 20, 0, 0, 0);

      //Creo il pannello dei pulsanti
      Utils.constrain(panel[0], Annulla, 1, 0, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.EAST, 0.3, 0.0, 10, 0, 10, 5);
      Utils.constrain(panel[0], Conferma, 3, 0, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.WEST, 0.3, 0.0, 10, 5, 10, 0);

      //Creo il pannello contenente la list_soggiornanti 
      Utils.constrain(panel[4], etichetta2, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 10, 0);
      Utils.constrain(panel[4], list_soggiornanti, 0, 1, 1, 1, GridBagConstraints.BOTH,
                         GridBagConstraints.NORTHWEST, 1.0, 1.0, 0, 5, 0, 20);

      //Attacco i pannelli al frame
      this.setLayout(gridbag);
      Utils.constrain(this, panel[1], 0, 0, 5, 4, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(this, panel[2], 0, 4, 5, 2, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 1.0, 5, 5, 5, 5);
      Utils.constrain(this, panel[3], 0, 6, 5, 3,GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 1.0, 5, 5, 5, 5);
      Utils.constrain(this, panel[0], 0, 11, 5, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.SOUTHEAST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(this, panel[4], 0, 12, 5, 7, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 1.0, 5, 5, 5, 5);
   }

   public void init()
   {
      checkboxes[0].addItemListener(new ItemListener()   
      {
         public void itemStateChanged(ItemEvent e)
         {
            if(checkboxes[0].getState())
            {
             	if (list_soggiornanti.getItemCount() > 0)
					list_soggiornanti.removeAll();   
                testo[0].setEditable(true);
                for(int i=1;i<testo.length;++i)
                {  
                	testo[i].setText("");
                   	testo[i].setEditable(false);
				}
				Conferma.setEnabled(true);
             }
         }
      });
                      
      checkboxes[1].addItemListener(new ItemListener()   
      {
         public void itemStateChanged(ItemEvent e)
         {
            if(checkboxes[1].getState())
            {
                if (list_soggiornanti.getItemCount() > 0)
					list_soggiornanti.removeAll();
                for(int i=1;i<3;++i)
                   testo[i].setEditable(true);
                testo[0].setText("");
                testo[0].setEditable(false);
				Conferma.setEnabled(true);
             }
         }
      });

      Conferma.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
			startConferma();
         }
       });

      Annulla.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            dispose();
            padre.setEnabled(true);
          }
       });
       
  		list_soggiornanti.addActionListener(new ActionListener()
      	{	
         	public void actionPerformed(ActionEvent e)
         	{
				startVediSogg();
          	}
       	});   
   }
   
   void startConferma()
   {
		int i;
		Frame msg;
				
		if (!errori())
		{
			if(checkboxes[0].getState())
				elenco_soggiornanti = (Principale.db).foundSoggiornanti(testo[0].getText(),true);
			else if(checkboxes[1].getState())
				elenco_soggiornanti = (Principale.db).foundSoggiornanti(testo[1].getText(), testo[2].getText());
			else
				elenco_soggiornanti = new ListaSoggiornanti();
			if (elenco_soggiornanti != null)
			{
				if (list_soggiornanti.getItemCount() > 0)
					list_soggiornanti.removeAll();
				for (i = 1; i <= elenco_soggiornanti.length(); i++)
					list_soggiornanti.addItem((elenco_soggiornanti.getSoggiornante(i)).toString());
				if (list_soggiornanti.getItemCount() == 0)
					msg = new AvvisoDialog(this, "Trovata nessuna corrispondenza!");
			}
			else
				msg = new MessageDialog(this, "Problemi con il database!");
		}
		else
			msg = new MessageDialog(this, "Errore nei parametri!");
   }
   
   boolean errori()
   {
		if(checkboxes[0].getState())
			return ( (testo[0].getText()).length() > 4 );
		else if(checkboxes[1].getState())
			return( (testo[1].getText()).length() > 20 || (testo[2].getText()).length() > 20 );
		else
			return true;
   }

	void startVediSogg()
	{
		if ( (elenco_soggiornanti != null) && (elenco_soggiornanti.length()>0) )		
			figlio = new VediSoggiornante(this, 
			elenco_soggiornanti.getSoggiornante(list_soggiornanti.getSelectedIndex()+1));
			
	}
}

