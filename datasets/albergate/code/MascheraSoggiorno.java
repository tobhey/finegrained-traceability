package interfacce;
import java.awt.*;
import java.awt.event.*;
import moduli.*;
import common.utility.*;
import common.def.*;
import java.util.Date;

public class MascheraSoggiorno extends Frame
{
   //Dichiaro gli oggetti che utilizzero' per costruire la finestra
   Label etichetta, etichetta1, label1, label2, label3, label4, label5,
         label6, label7, label8, label9, label10, label11, label12, label13,
         label14, label15, label16;
   TextField testo1, testo2, testo3, testo4, testo5, testo6, testo7, testo8,
             testo9, testo10, testo11, testo12, testo13, testo14;
   Button Annulla, Conferma;
   CheckboxGroup pensione, cliente;
   Checkbox[] checkboxes, checkboxes1;
   Panel panel1, panel2, panel3;
   GridBagLayout gridbag=new GridBagLayout();
   RicercaPrenotazione padre = new RicercaPrenotazione("",3);
   
   public MascheraSoggiorno(String title, String caption, char pens, char status)
   {
      super(title);
      padre.setEnabled(false);
      setupPanels(caption, pens, status);
      init();
      pack();
      this.setVisible(true);
   }

   void setupPanels(String caption, char pens, char status)
   {
	  this.setFont(ConfigurazioneSistema.font_base);
      //Creo  le etichette
      etichetta=new Label("Dati del cliente");
      etichetta.setFont(ConfigurazioneSistema.font_titolo);
      label1 = new Label("Stanza numero");
      label2 = new Label("Cognome");
      label3 = new Label("Nome");
      label4 = new Label("Luogo di nascita");
      label5 = new Label("Data di nascita");
      label6 = new Label("Documento di identita'");
      label7 = new Label("Numero telefonico");
      label8 = new Label("Indirizzo");
      label9 = new Label("C.A.P");
      label10 = new Label("Comune di residenza");
      label11 = new Label("Citta'");
      label12 = new Label("Nazione");
      etichetta1 = new Label("Dati del soggiorno");
      etichetta1.setFont(ConfigurazioneSistema.font_titolo);
      label13 = new Label("Data inizio soggiorno");
      label14 = new Label("Data fine soggiorno");
      label15 = new Label("Tipo di pensionamento");
      label15.setFont(ConfigurazioneSistema.font_titolo);
      label16 = new Label("Status cliente");
      label16.setFont(ConfigurazioneSistema.font_titolo);

      //Creo i TextField
      testo1 = new TextField("", 4);
      testo2 = new TextField("", 20);
      testo3 = new TextField("", 20);
      testo4 = new TextField("", 32);
      testo5 = new TextField("", 10);
      testo6 = new TextField("", 16);
      testo7 = new TextField("", 16);
      testo8 = new TextField("", 52);
      testo9 = new TextField("", 8);
      testo10 = new TextField("", 32);
      testo11 = new TextField("", 20);
      testo12 = new TextField("", 20);
      testo13 = new TextField("", 10);
      testo14 = new TextField("", 10);

      //Creo i bottoni
      Annulla = new Button("Annulla");
      Conferma = new Button(caption);

      //Creo i Checkbox ad esclusione
      pensione = new CheckboxGroup();
      checkboxes = new Checkbox[4];
      checkboxes[0] = new Checkbox(" Solo pernottamento", pensione, false);
      checkboxes[1] = new Checkbox(" Prima colazione", pensione, false);
      checkboxes[2] = new Checkbox(" Mezza pensione", pensione, false);
      checkboxes[3] = new Checkbox(" Pensione completa", pensione, false);
	  switch (pens)
	  {
	  	case Flag.SOLO_PERNOTTAMENTO: 
	  		checkboxes[0] = new Checkbox(" Solo pernottamento", pensione, true);
	  		break;
	  	case Flag.SOLO_COLAZIONE: 
			checkboxes[1] = new Checkbox(" Prima colazione", pensione, true);
	  		break;
	  	case Flag.MEZZA_PENSIONE:
			checkboxes[2] = new Checkbox(" Mezza pensione", pensione, true);
	  		break;
	  	case Flag.PENSIONE_COMPLETA: 
			checkboxes[3] = new Checkbox(" Pensione completa", pensione, true);
	  		break;
	  }

      cliente = new CheckboxGroup();
      checkboxes1 = new Checkbox[4];
      checkboxes1[0] = new Checkbox(" Neonato (0-2 anni)", cliente, false);
      checkboxes1[1] = new Checkbox(" Bambino (3-12 anni)", cliente, false);
      checkboxes1[2] = new Checkbox(" Ragazzo (12-17 anni)", cliente, false);
      checkboxes1[3] = new Checkbox(" Adulto  (>=18 anni)", cliente, false);
	  switch (status)
	  {
	  	case Flag.NEONATO: 
		    checkboxes1[0] = new Checkbox(" Neonato (0-2 anni)", cliente, true);
	  		break;
	  	case Flag.BAMBINO: 
			checkboxes1[1] = new Checkbox(" Bambino (3-12 anni)", cliente, true);
	  		break;
	  	case Flag.RAGAZZO:
			checkboxes1[2] = new Checkbox(" Ragazzo (12-17 anni)", cliente, true);
	  		break;
	  	case Flag.ADULTO: 
			checkboxes1[3] = new Checkbox(" Adulto  (>=18 anni)", cliente, true);
	  		break;
	  }

      //Creo il pannello in alto contenente i dati del cliente
      panel1 = new Panel();
      panel1.setLayout(gridbag);
      Utils.constrain(panel1, etichetta, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, label1, 0, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, testo1, 1, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, label2, 0, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, testo2, 1, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, label3, 2, 2, 1 ,1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, testo3, 3, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, label4, 0, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, testo4, 1, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, label5, 2, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, testo5, 3, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, label6, 0, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, testo6, 1, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, label7, 2, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, testo7, 3, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, label8, 0, 5, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, testo8, 1, 5, 4, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, label9, 0, 6, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, testo9, 1, 6, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, label10, 2, 6, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, testo10, 3, 6, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, label11, 0, 7, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, testo11, 1, 7, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, label12, 2, 7, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, testo12, 3, 7, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);

      //Creo il pannello centrale con i dati del soggiorno
      panel2 = new Panel();
      panel2.setLayout(gridbag);
      Utils.constrain(panel2, etichetta1, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel2, label13, 0, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHEAST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel2, testo13, 1, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel2, label14, 2, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHEAST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel2, testo14, 3, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel2, label15, 0, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel2, checkboxes[0], 0, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel2, checkboxes[1], 0, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel2, checkboxes[2], 0, 5, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel2, checkboxes[3], 0, 6, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel2, label16, 1, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel2, checkboxes1[0], 1, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel2, checkboxes1[1], 1, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel2, checkboxes1[2], 1, 5, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel2, checkboxes1[3], 1, 6, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);                  

      //Creo il pannello in basso con due pulsanti
      panel3 = new Panel();
      panel3.setLayout(gridbag);
      Utils.constrain(panel3, Annulla, 0, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.EAST, 0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel3, Conferma, 1, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.WEST, 0, 0.0, 5, 5, 5, 5);

      //Attacco i pannelli al frame
      this.setLayout(gridbag);
      Utils.constrain(this, panel1, 0, 0, 4, 8, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.NORTH, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(this,panel2, 0, 8, 4, 7, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.CENTER, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(this,panel3,0, 15, 4, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.SOUTH, 1.0, 0.0, 5, 5, 5, 5);
    }

   public void init()
   {
      Annulla.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            dispose();
            padre.setEnabled(true);
         }
      });
	}
	
	public Soggiornante readDatiSogg(Prenotazione pren, int cont)
	{
		Frame msg;
		String data_inizio,data_fine;
		Date data1, data2;
		Soggiornante sogg = new Soggiornante();
		sogg.setNumStanza(pren.getNumStanza());
		sogg.setCognome(testo2.getText());
		sogg.setNome(testo3.getText());
		sogg.setLuogoNasc(testo4.getText());
		sogg.setDataNasc(DateUtils.convertDate(testo5.getText()));
		sogg.setNumDoc(testo6.getText());
		sogg.setNumTel(testo7.getText());
		sogg.setIndirizzo(testo8.getText());
		sogg.setCap(testo9.getText());
		sogg.setComune(testo10.getText());
		sogg.setCitta(testo11.getText());
		sogg.setNazione(testo12.getText());
        sogg.setInizioSogg(pren.getInizioSogg());
        sogg.setFineSogg(pren.getFineSogg());
        sogg.setIdPrenotazione(pren.getIdPrenotazione());
        sogg.setIdSoggiornante(cont);
        
		if (checkboxes[0].getState() == true)
			sogg.setPensionamento(Flag.SOLO_PERNOTTAMENTO);
		else	
			if (checkboxes[1].getState() == true)
				sogg.setPensionamento(Flag.SOLO_COLAZIONE);
			else
				if (checkboxes[2].getState() == true)
					sogg.setPensionamento(Flag.MEZZA_PENSIONE);
				else
					if (checkboxes[3].getState() == true)
						sogg.setPensionamento(Flag.PENSIONE_COMPLETA);
		
		if (checkboxes1[0].getState() == true)
			sogg.setStatus(Flag.NEONATO);
		else
			if (checkboxes1[1].getState() == true)
				sogg.setStatus(Flag.BAMBINO);
			else
				if (checkboxes1[2].getState() == true)
					sogg.setStatus(Flag.RAGAZZO);
				else
					sogg.setStatus(Flag.ADULTO);	
		return sogg;
	
	}	

	public void writeDatiSogg(Soggiornante sogg)
	{
		testo1.setText(sogg.getNumStanza());
		testo2.setText(sogg.getCognome());
		testo3.setText(sogg.getNome());
		testo4.setText(sogg.getLuogoNasc());
		testo5.setText(DateUtils.parseDate(DateUtils.giveStringOfDate(sogg.getDataNasc())));
		testo6.setText(sogg.getNumDoc());
		testo7.setText(sogg.getNumTel());
		testo8.setText(sogg.getIndirizzo());
		testo9.setText(sogg.getCap());
		testo10.setText(sogg.getComune());
		testo11.setText(sogg.getCitta());
		testo12.setText(sogg.getNazione());
		testo13.setText(DateUtils.parseDate(DateUtils.giveStringOfDate(sogg.getInizioSogg())));
		testo14.setText(DateUtils.parseDate(DateUtils.giveStringOfDate(sogg.getFineSogg())));
	}

	void changeTitle(int parz, int tot)
	{
		this.setTitle("Inserimento dei dati del soggiornante  ("+parz+"/"+tot+")");
	}

	void cleanFields()
	{
		testo2.setText("");
		testo3.setText("");
		testo4.setText("");
		testo5.setText("");
		testo6.setText("");
		testo7.setText("");
		testo8.setText("");
		testo9.setText("");
		testo10.setText("");
		testo11.setText("");
		testo12.setText("");
	}	

	void aggiornaDisp(Soggiornante sogg, char tipo)
	{
		Disponibilita disp_da_cambiare = new Disponibilita();
		ListaDisponibilita elenco_disp = (Principale.db).elencoDisponibilita();
		if (elenco_disp == null)
		{
			Frame msg = new MessageDialog(this, " Problemi con il database nella lettura delle disponibilita'! ");
			return;
		}
		
		disp_da_cambiare = Utils.getDispOfRoom(elenco_disp, sogg.getNumStanza(), true);
		disp_da_cambiare.setDisponibilita(sogg.getInizioSogg(), sogg.getFineSogg(), tipo, Flag.ASSEGNATA) ;
		(Principale.db).changeDisponibilita( disp_da_cambiare.getNumStanza(), 
											disp_da_cambiare.getDispAnnoCorr(),
											disp_da_cambiare.getDispAnnoProx() );
	}

	boolean errori()
	{
		Frame msg;
		String data_nasc = new String();  
		//Controllo data di nascita
		if ( (testo5.getText().equals("")) )
		{	
			msg = new AvvisoDialog(this," Inserire la data di nascita! ");
			return true;
		}
		else		
			data_nasc = DateUtils.parseDate(testo5.getText());
		if ( data_nasc.equals(Errore.DATA_NON_CORRETTA))
		{	
			msg = new MessageDialog(this," Data Nascita Errata! ");
			return true;
		}
		else
		{
			if ( !( DateUtils.isDataRight(data_nasc)) )
			{	
				msg = new MessageDialog(this,"Data Nascita Insensata");
				return true;
			}
		}
		if ( !((testo2.getText()).length() <= 20) )
		{
			msg = new MessageDialog(this, " Il cognome deve essere composto da al piu' 20 caratteri! ");
			return true;
		}
		if ( !((testo3.getText()).length() <= 20) )
		{
			msg = new MessageDialog(this, " Il nome deve essere composto da al piu' 20 caratteri! ");
			return true;
		}
		if ( !((testo4.getText()).length() <= 32) )
		{
			msg = new MessageDialog(this, " Il luogo di nascita deve essere composto da al piu' 32 caratteri! ");
			return true;
		}
		if ( !((testo6.getText()).length() <= 16) )
		{
			msg = new MessageDialog(this, " Il documento di identita' deve essere composto da al piu' 16 caratteri! ");
			return true;
		}
		if ( !((testo7.getText()).length() <= 16) )
		{
			msg = new MessageDialog(this, " Il numero telefonico deve essere composto da al piu' 16 caratteri! ");
			return true;
		}
		if ( !((testo8.getText()).length() <= 32) )
		{
			msg = new MessageDialog(this, " L'indirizzo deve essere composto da al piu' 32 caratteri! ");
			return true;
		}
		if ( !((testo9.getText()).length() <= 5) )
		{
			msg = new MessageDialog(this, " Il C.A.P. deve essere composto da al piu' 5 caratteri! ");
			return true;
		}
		if ( !((testo10.getText()).length() <= 32) )
		{
			msg = new MessageDialog(this, " Il comune deve essere composto da al piu' 32 caratteri! ");
			return true;
		}
		if ( !((testo11.getText()).length() <= 20) )
		{
			msg = new MessageDialog(this, " La citta deve essere composto da al piu' 20 caratteri! ");
			return true;
		}
		if ( !((testo12.getText()).length() <= 20) )
		{
			msg = new MessageDialog(this, " La nazione deve essere composto da al piu' 20 caratteri! ");
			return true;
		}
		return false;
	}
}
