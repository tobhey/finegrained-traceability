package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.*;

public class MascheraTelefonate extends Frame
{
   	TextField num_stanza, data_tel, ora_i, ora_f, numero, durata, scatti, costo,
   			  tot_scatti, tot_costo;
   	Label etich1, etich2, etich3, etich4, etich5, etich6, etich7, etich8, etich9,
   		  etich10, etich11, etich12;
   	Panel panel1, panel2;
   	Button Annulla;
   	GridBagLayout gridbag = new GridBagLayout();
   	Frame padre = new Frame();
	Telefonata phone;
  	int scatti_totali; 	
   	public MascheraTelefonate(Frame p, Telefonata t, int tot)
   	{
      	super("Visualizzazione dei dati delle telefonate");
      	padre = p;
      	phone = t;
      	scatti_totali = tot;
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
      	etich1 = new Label("Dati della telefonata");
      	etich1.setFont(ConfigurazioneSistema.font_titolo);
      	etich2 = new Label("Numero Stanza");
      	etich3 = new Label("Data Telefonata");
      	etich4 = new Label("Ora di Inizio");
      	etich5 = new Label("Ora di Fine");
      	etich6 = new Label("Durata");
      	etich7 = new Label("Numero Chiamato");
      	etich8 = new Label("Scatti Addebitati");
		etich9 = new Label("Spesa Addebitata");
      	etich10 = new Label("Totale Scatti");
      	etich10.setFont(ConfigurazioneSistema.font_titolo);
      	etich11 = new Label("Totale Spesa");
		etich11.setFont(ConfigurazioneSistema.font_titolo);
		etich12 = new Label("Riassunto totali stanza "+phone.getNumStanza());
		etich12.setFont(ConfigurazioneSistema.font_titolo);
      
      	//Creo i TextField e ne rendo  alcuni non editabili
      	data_tel = new TextField("",12);
      	num_stanza = new TextField("",4);
      	ora_i = new TextField("",12);
     	ora_f = new TextField("",12);
      	durata = new TextField("",12);
      	scatti = new TextField("",4);
      	numero = new TextField("",18);
      	costo = new TextField("",12);
      	tot_scatti = new TextField("",4);
      	tot_costo = new TextField("",14);
      	
      	//Disabilita i TextField
      	data_tel.setEditable(false);
      	num_stanza.setEditable(false);
      	ora_i.setEditable(false);
     	ora_f.setEditable(false);
      	durata.setEditable(false);
      	scatti.setEditable(false);
      	numero.setEditable(false);
      	costo.setEditable(false);
      	tot_scatti.setEditable(false);
      	tot_costo.setEditable(false);

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
      	Utils.constrain(panel1, etich3, 2, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1,data_tel, 3, 1, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1, etich4, 0, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1,ora_i , 1, 2, 1, 1, GridBagConstraints.NONE,
                    GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1, etich5, 2, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1,ora_f , 3, 2, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1, etich6, 4, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1,durata, 5, 2, 1, 1, GridBagConstraints.NONE,
                    GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1, etich7, 0, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1,numero , 1, 3, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
		Utils.constrain(panel1, etich8, 0, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(panel1,scatti, 1, 4, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);                         
		Utils.constrain(panel1, etich9, 2, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
		Utils.constrain(panel1,costo, 3, 4, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
        Utils.constrain(panel1,etich12, 0, 5, 2, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);                 
        Utils.constrain(panel1, etich10, 0, 6, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
        Utils.constrain(panel1,tot_scatti, 1, 6, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);                         
		Utils.constrain(panel1, etich11, 2, 6, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
		Utils.constrain(panel1,tot_costo, 3, 6, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);                                          

      	panel2 = new Panel();
     	panel2.setLayout(gridbag);
      	Utils.constrain(panel2, Annulla, 0, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 0, 0, 0, 15);
		
      	//Attacco i pannelli al frame
      	this.setLayout(gridbag);
      	Utils.constrain(this, panel1, 0, 0, 6, 7, GridBagConstraints.NONE,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5);
      	Utils.constrain(this, panel2, 0, 7, 6, 1, GridBagConstraints.HORIZONTAL,
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
		data_tel.setText(DateUtils.giveStringOfDate(phone.getDataTel()));
      	num_stanza.setText(phone.getNumStanza());
      	ora_i.setText(""+phone.getInizioTel());
     	ora_f.setText(""+phone.getFineTel());
      	durata.setText(""+phone.getDurataTel());
      	scatti.setText(""+phone.getNumScatti());
      	numero.setText(phone.getNumChiamato());
      	costo.setText(""+((Principale.config).getCostoXScatto())*phone.getNumScatti());      	
      	tot_scatti.setText(""+scatti_totali);
      	tot_costo.setText(""+((Principale.config).getCostoXScatto())*scatti_totali);		
	}
}
