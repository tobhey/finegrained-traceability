package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import java.util.Date;
import moduli.*;

public class MascheraPrenotazione extends Frame
{  
    //Dichiarazioni di variabili
    Panel panel1, panel2, panel3;
    Label  etichetta1, label1, label2, label3, label4, label5, label6, label7, 
    	label8, label9, label10, label11, label12, label13;
    List lista;
    public Button  Annulla, Azione, Assegna, Cancella;
    TextField  testo[];
    Choice mychoice;
    Checkbox[] checkboxes;
    Checkbox myCheckbox, myCheckbox1;
    CheckboxGroup pensione;
    GridBagLayout gridbag = new GridBagLayout();
	Frame padre = new Frame();
  	ListaPrenotazioni L;  
    Stanza stanza;
    int caller;
      
    
    public MascheraPrenotazione(Stanza s, String data_i, String data_f, String title, String caption, int c, char pens)
    {
        super(title);
        caller = c;
        stanza = s;
        setupPanels(data_i, data_f, caption, pens);
        inizializza();
        pack(); 
    }
                     
        void setupPanels(String data_i, String data_f, String caption, char pens)
        {  
			this.setFont(ConfigurazioneSistema.font_base);
            
            //Creo dei pulsanti e ne disabilito due  
            Annulla = new Button(" Fine ");
            Azione = new Button(caption);
            Assegna = new Button(" Assegna Stanza ");
            Cancella = new Button(" Cancella prenotazione ");

            //Creo le etichette
            etichetta1 = new Label ("Inserimento dei dati della prenotazione:");
            etichetta1.setFont(ConfigurazioneSistema.font_titolo);
            label1 = new Label("Numero stanza");
            label2 = new Label("Cognome");
            label3 = new Label("Nome");
            label4 = new Label("Numero telefonico");
            label5 = new Label("Numero persone");
            label6 = new Label("Data inizio");
            label7 = new Label("Data fine");
            label8 = new Label("Caparra versata "+(Principale.config).getValuta()+".");
            label9 = new Label("Richieste particolari");
            label11 = new Label("Nome Agenzia");
            label10 = new Label("Tipo di pensionamento:");
            label10.setFont(ConfigurazioneSistema.font_titolo);
			label12 = new Label("Risultato della ricerca");
			label12.setFont(ConfigurazioneSistema.font_titolo);
			label13 = new Label("Data di prenotazione");
            
            //Creo i TextField e ne rendo  alcuni non editabili
            testo=new TextField[11];
            testo[0] = new TextField("", 4);
            testo[1] = new TextField("", 20);
            testo[2] = new TextField("", 20);
            testo[3] = new TextField("", 10);
            testo[4] = new TextField("", 12);
            testo[5] = new TextField("", 12);
            testo[6] = new TextField("", 10);
            testo[7] = new TextField("", 20);
            testo[8] = new TextField("", 20);
            testo[8].setEditable(false);
            testo[9] = new TextField("", 12);
            testo[9].setEditable(false);
            testo[10] = new TextField("",3); 
			
            //Creo un Checkbox ad esclusione 
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
            

            //Creo due Checkbox a se' stanti 
            myCheckbox = new Checkbox(" Letto aggiuntivo", null, false);
			if (stanza != null)
	            if (stanza.getDispLettoAgg() == Const.NO)
    	        	myCheckbox.setEnabled(false);
            
            myCheckbox1 = new Checkbox(" Prenotazione tramite agenzia", null, false);
			myCheckbox1.setEnabled(false);

            //Creo un Choice 
            if (caller == 1)
            {
            	mychoice = new Choice();
				if (stanza != null)
	            	for (int i=1; i<= stanza.getPostiLetto(); i++)
    		        	mychoice.addItem(""+i);
    			else
    				mychoice.addItem("1");	        
            }
            //Creo il pannello in alto per inserimento dei dati del cliente
            panel1 = new Panel();
            panel1.setLayout(gridbag);
            Utils.constrain(panel1, etichetta1, 0, 0, 4, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);
            Utils.constrain(panel1, label1, 0, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, testo[0], 1, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.5, 5, 5, 5, 5);
            Utils.constrain(panel1, label13, 2, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, testo[9], 3, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.5, 5, 5, 5, 5);
            Utils.constrain(panel1, label2, 0, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, testo[1], 1, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.5, 5, 5, 5, 5);
            Utils.constrain(panel1, label3, 2, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, testo[2], 3, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, label4, 0, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, testo[3], 1, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, label5, 2, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            if (caller == 1)
            	Utils.constrain(panel1, mychoice, 3, 3, 1, 1, GridBagConstraints.NONE,
                        	GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
            else
            	Utils.constrain(panel1, testo[10], 3, 3, 1, 1, GridBagConstraints.NONE,
                        	GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, label6, 0, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, testo[4], 1, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, label7, 2, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, testo[5], 3, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, label8, 0, 5, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, testo[6], 1, 5, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, label9, 0, 6, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, testo[7], 1, 6, 4, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, myCheckbox, 0, 7, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, myCheckbox1, 1, 7, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, label11, 2, 7, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, testo[8], 3, 7, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, label10, 0, 8, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, checkboxes[0], 0, 9, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, checkboxes[1], 0, 10, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, checkboxes[2], 0, 11, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, checkboxes[3], 0, 12, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
           
            //Creo il pannello in basso con due pulsanti                  
            panel2 = new Panel();
            panel2.setLayout(gridbag);
            Utils.constrain(panel2, Annulla, 0, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel2, Azione, 1, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 5, 5, 5, 5);             
            if (caller == 2 )
            	Utils.constrain(panel2, Assegna, 2, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 5, 5, 5, 5);             
            if (caller == 5 )
            	Utils.constrain(panel2, Cancella, 2, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 5, 5, 5, 5);             
            
            	
            //Creo il pannello con la lista dei risultati della ricerca
            lista = new List(7,false);
            lista.setFont(ConfigurazioneSistema.font_allineato);
            
            panel3 = new Panel();
            panel3.setLayout(gridbag);
            Utils.constrain(panel3, label12, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.WEST, 1.0, 0.0, 0, 0, 0, 0);
            Utils.constrain(panel3, lista, 0, 1, 4, 3, GridBagConstraints.BOTH,
                        GridBagConstraints.CENTER, 3.0, 3.0, 0, 0, 0, 0);             
            
            
            //Attacco i pannelli al frame
            this.setLayout(gridbag);
            Utils.constrain(this, panel1, 0, 1, 4, 13, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(this, panel2, 0, 14, 4, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5); 
        	Utils.constrain(this, panel3, 0, 15, 4, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5); 
        	panel3.setVisible(false);
        	pack();
        
        }

	public void inizializza()
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

	public Prenotazione readDatiPren()
	{
		Prenotazione pren = new Prenotazione();
		pren.setNumStanza(testo[0].getText());
		pren.setCognome(testo[1].getText());
		pren.setNome(testo[2].getText());
		pren.setNumTel(testo[3].getText());
		if (caller == 1)
			pren.setNumPers(Integer.parseInt(mychoice.getSelectedItem())); //Conversione String -> Int
		else
			pren.setNumPers(Integer.parseInt(testo[10].getText()));
		pren.setInizioSogg(DateUtils.convertDate(testo[4].getText()));
		pren.setFineSogg(DateUtils.convertDate(testo[5].getText()));
		
		if  ( (testo[6].getText().equals("")) )
			pren.setCaparra(0);
		else
			pren.setCaparra((Float.valueOf(testo[6].getText())).floatValue());  // Conversione String -> Float
		pren.setRichParticolari(testo[7].getText());
		if (myCheckbox.getState() == true)
			pren.setRichLettoAgg(Const.SI);
		if (myCheckbox1.getState() == true)
		{
			pren.setTramiteAgenzia(Const.SI);
			pren.setNomeAgenzia(testo[8].getText());
		}	
		
		if (checkboxes[0].getState() == true)
			pren.setPensionamento(Flag.SOLO_PERNOTTAMENTO);
		else	
			if (checkboxes[1].getState() == true)
				pren.setPensionamento(Flag.SOLO_COLAZIONE);
			else
				if (checkboxes[2].getState() == true)
					pren.setPensionamento(Flag.MEZZA_PENSIONE);
				else
					pren.setPensionamento(Flag.PENSIONE_COMPLETA);		 		 
		 
		pren.setIdPrenotazione(Principale.config.getIdPrenotazione());
		return pren;
	}
	
	public void writeDatiPren(Prenotazione pren)
	{
		myCheckbox.setState(false);
		myCheckbox1.setState(false);
		testo[8].setText("");
		testo[0].setText(pren.getNumStanza());
		testo[1].setText(pren.getCognome());
		testo[2].setText(pren.getNome());
		testo[3].setText(pren.getNumTel());
		
		testo[4].setText(DateUtils.giveStringOfDate(pren.getInizioSogg()));
		testo[5].setText(DateUtils.giveStringOfDate(pren.getFineSogg()));
		
		testo[6].setText(""+pren.getCaparra());
		testo[7].setText(pren.getRichParticolari());
		testo[8].setText(pren.getNomeAgenzia());
		testo[9].setText(DateUtils.giveStringOfDate(pren.getDataPren()));
		if (caller != 1)
		{
			testo[10].setText(""+pren.getNumPers());
		}
		if (pren.getTramiteAgenzia() == Const.SI)
		{	
			myCheckbox1.setState(true);
			testo[8].setText(pren.getNomeAgenzia());
		}
	}
	
	protected boolean errori()
	{
		boolean numeri_corretti;
		Frame msg;
		if (caller != 1)
		{
			if (Utils.isIntPos(testo[10].getText()))
			{
				int num = Integer.parseInt(testo[10].getText());
				if ( (num > stanza.getPostiLetto()) || (num <= 0) )
				{	
					msg = new MessageDialog(this," La stanza non puo' contenere "+num+" persone! "); 
					return true;
				}
			}
			else
			{
				msg = new MessageDialog(this," Inserire correttamente il numero di persone "); 
				return true; 
			}			
		}	
		if (!((testo[0].getText()).length() <= 4))
		{
			msg = new MessageDialog(this," Il numero di stanza deve avere al piu' 4 caratteri! "); 
			return true;
		}
		if (!((testo[1].getText()).length() <= 20))
		{
			msg = new MessageDialog(this," Il cognome deve avere al piu' 20 caratteri! "); 
			return true;
		}
		if (!((testo[2].getText()).length() <= 20))
		{
			msg = new MessageDialog(this," Il nome deve avere al piu' 20 caratteri! "); 
			return true;
		}
		if (!((testo[3].getText()).length() <= 16))
		{
			msg = new MessageDialog(this," Il numero di telefono deve avere al piu' 16 caratteri! "); 
			return true;
		}
		if ((testo[1].getText()).equals("")) 
		{
			msg = new MessageDialog(this," Manca il cognome! "); 
			return true;
		}
		if ((testo[2].getText()).equals("")) 
		{
			msg = new MessageDialog(this," Manca il nome! "); 
			return true;
		}
		if ((testo[3].getText()).equals(""))
		{
			msg = new MessageDialog(this," Manca il numero di telefono! "); 
			return true;
		}
		return false;
	}

	public void cleanFields()
	{
		testo[0].setText("");
        testo[1].setText("");
        testo[2].setText("");
        testo[3].setText("");
        testo[4].setText("");
        testo[5].setText("");
        testo[6].setText("");
        testo[7].setText("");
        testo[8].setText("");	
	}	
	
	void creaLista()
	{
		Frame msg;
		Prenotazione p;		
		
		L = (Principale.db).foundPrenotazioni(testo[1].getText(),testo[2].getText());
		if (L != null)
		{
			if (!L.isEmpty())
			{
				if (lista.getItemCount() > 0)
					lista.removeAll();
				panel3.setVisible(true);
				pack();
				for(int i = 1; i<=L.length(); i++)
					lista.addItem(L.getPrenotazione(i).toString());
			}
			else
				msg = new AvvisoDialog(this, "Prenotazione non trovata!");
		}
		else
			msg = new MessageDialog(this, "Problemi con il DataBase");
	}			
			

	void aggiornaDisp(Prenotazione pren, char tipo)
	{
		Disponibilita disp_da_cambiare = new Disponibilita();
		ListaDisponibilita elenco_disp = (Principale.db).elencoDisponibilita();
		if (elenco_disp == null)
		{
			Frame msg = new MessageDialog(this, " Problemi con il database nella lettura delle disponibilita'! ");
			return;
		}
		
		disp_da_cambiare = Utils.getDispOfRoom(elenco_disp, pren.getNumStanza(),true);
		if (tipo == Flag.DISPONIBILE) //cancellazione della prenotazione
		{	
			disp_da_cambiare.setDisponibilita(pren.getInizioSogg(), pren.getFineSogg(), tipo, Flag.ASSEGNATA);
			disp_da_cambiare.setDisponibilita(pren.getInizioSogg(), pren.getFineSogg(), tipo, Flag.BLOCCATA);
		}
		else
			{
				disp_da_cambiare.setDisponibilita(pren.getInizioSogg(), pren.getFineSogg(), tipo, Flag.DISPONIBILE);
				disp_da_cambiare.setDisponibilita(pren.getInizioSogg(), pren.getFineSogg(), tipo, Flag.COMMISSIONATA);
			}			
		(Principale.db).changeDisponibilita( disp_da_cambiare.getNumStanza(), 
											disp_da_cambiare.getDispAnnoCorr(),
											disp_da_cambiare.getDispAnnoProx() );
	}
}
