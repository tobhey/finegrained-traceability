package interfacce;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import common.utility.*;
import common.def.*;
import moduli.*;

public class MascheraCalcoloConto extends Frame
{
   	Button Annulla, Visualizza;
   	TextField testo[];
   	Label etichetta1, etichetta2, etichetta3, etichetta4, etichetta5, stanza,
         stanza1, nome, cognome, stanza2;
   	Panel panel[];
   	CheckboxGroup checkbox_group;
   	Checkbox[] checkboxes;
   	GridBagLayout gridbag =new GridBagLayout();

   	// per la gerarchia
	Frame padre;
	MascheraVisualizzazioneConto visual;
	
	// variabili locali
	Soggiornante sogg_x_conto_sing;
	String L_st[];
	Date date_fine[]; // questo array e' necessario per la chiusura della comitiva
						// infatti per poter ripristinare le disponibilita della
						// stanza correttamente ho bisogno delle date di fine corrette
	Date date_inizio[];
	Date data_fine_magg;
	Date fine_sogg_anticipato;
   	int tipo_conto;
   	boolean qualcuno_tramite_agenzia;
   	boolean qualcuno_senza_agenzia;
   	
   	public MascheraCalcoloConto(Frame parent)
   	{
      super("Calcolo del Conto");
      padre = parent;
      padre.setEnabled(false);
      setupPanels();
      init();
      pack();
   }

   void setupPanels()
   {
	  this.setFont(ConfigurazioneSistema.font_base);
      //Creo i pulsanti
      Annulla = new Button("   Annulla   ");
      //Annulla.setFont(new Font("Courier", Font.PLAIN, 12));
      Visualizza = new Button("   Calcola   ");
      //Visualizza.setFont(new Font("Courier", Font.PLAIN, 12));
	  Visualizza.setEnabled(false);
      
      //Creo le etichette
      etichetta1 = new Label("Selezionare il tipo di conto che si desidera calcolare:");
      etichetta1.setFont(ConfigurazioneSistema.font_titolo);
      etichetta2 = new Label("Conto su una stanza:");
      etichetta2.setFont(ConfigurazioneSistema.font_titolo);
      etichetta3 = new Label("Conto su un cliente:");
      etichetta3.setFont(ConfigurazioneSistema.font_titolo);
      etichetta4 = new Label("Conto su piu' stanze:");
      etichetta4.setFont(ConfigurazioneSistema.font_titolo);
      stanza = new Label("Stanza numero");
      cognome = new Label("Cognome");
      nome = new Label("Nome");
      stanza1 = new Label("Stanza");
      stanza2 = new Label("Stanze Numero");

      //Creo i TextField e li rendo non editabili
      testo=new TextField[6];
      testo[0] = new TextField("", 4);
      testo[1] = new TextField("", 20);
      testo[2] = new TextField("", 20);
      testo[3] = new TextField("", 4);
      testo[4] = new TextField("", 30);
      testo[5] = new TextField("",10);
      for(int i=0;i<6;++i)
      	testo[i].setEditable(false);
       

      //Creo un Checkbox ad esclusione
      checkbox_group = new CheckboxGroup();
      checkboxes = new Checkbox[4];
      checkboxes[0] = new Checkbox(" Conto su una stanza",checkbox_group, false);
      checkboxes[1] = new Checkbox(" Conto su un cliente",checkbox_group, false);
      checkboxes[2] = new Checkbox(" Conto su piu'stanze",checkbox_group, false);
	  checkboxes[3] = new Checkbox(" Partenza anticipata", false);	
      panel=new Panel[5];
      for(int i=0;i<5;++i)
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
      Utils.constrain(panel[1], checkboxes[2], 0, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 5, 0, 0);
      Utils.constrain(panel[1], checkboxes[3], 0, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 5, 0, 0);
      Utils.constrain(panel[1], testo[5], 1, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 5, 0, 0);                  
            
      //Creo il pannello in alto a destra
      Utils.constrain(panel[0], Annulla, 0, 1, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 0.3, 0.0, 20, 0, 5, 20);
      Utils.constrain(panel[0], Visualizza, 0, 2, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 0.3, 0.0, 5, 0, 0, 20);
      

      //Creo il pannello conto su una stanza 
      Utils.constrain(panel[2], etichetta2, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 10, 0);
      Utils.constrain(panel[2], stanza, 0, 1, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 5, 0, 20);
      Utils.constrain(panel[2], testo[0], 1, 1, 1, 1, GridBagConstraints.NONE,
                      GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);

      //Creo il pannello conto su un cliente
      Utils.constrain(panel[3], etichetta3, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 10, 0);
      Utils.constrain(panel[3], cognome, 0, 1, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 5, 0, 20);
      Utils.constrain(panel[3], testo[1], 1, 1, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);
      Utils.constrain(panel[3], nome, 0, 2, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.SOUTHWEST, 0.0, 0.0, 20, 5, 0, 20);
      Utils.constrain(panel[3], testo[2], 1, 2, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.SOUTHWEST, 1.0, 0.0, 20, 0, 0, 0);
      Utils.constrain(panel[3], stanza1, 4, 2, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 5, 0, 20);
      Utils.constrain(panel[3], testo[3], 5, 2, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);

      //Creo il pannello conto su piu' stanze
      Utils.constrain(panel[4], etichetta4, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 10, 0);
      Utils.constrain(panel[4], stanza2, 0, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 5, 0, 20);
      Utils.constrain(panel[4], testo[4], 1, 1, 1, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 5); 
            
      //Attacco i pannelli al frame
      this.setLayout(gridbag);
      Utils.constrain(this, panel[1], 0, 0, 6, 4, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(this, panel[0], 6, 0, 1, 4, GridBagConstraints.BOTH,
                        GridBagConstraints.SOUTHEAST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(this, panel[2], 0, 4, 7, 2, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 1.0, 5, 5, 5, 5);
      Utils.constrain(this, panel[3], 0, 6, 7, 3,GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 1.0, 5, 5, 5, 5);
      Utils.constrain(this,panel[4], 0, 9, 7, 2,GridBagConstraints.HORIZONTAL,
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
               		tipo_conto = 1;
               		testo[0].setEditable(true);
               		Visualizza.setEnabled(true);
               		for(int i=1;i<5;++i)
               		{    
                   		testo[i].setEditable(false);
        				testo[i].setText("");
        			}     	 
            	}
         	}
      	});
                      
      	checkboxes[1].addItemListener(new ItemListener()   
      	{
         	public void itemStateChanged(ItemEvent e)
         	{
            	if(checkboxes[1].getState())
            	{
               		tipo_conto = 2;
               		Visualizza.setEnabled(true);
               		for(int i=1;i<4;++i)
                  		testo[i].setEditable(true);
               		testo[0].setEditable(false);
               		testo[0].setText("");
              		testo[4].setEditable(false);
             		testo[4].setText("");
             	}
         	}
      	});

      	checkboxes[2].addItemListener(new ItemListener()
      	{
         	public void itemStateChanged(ItemEvent e)
         	{
            	if(checkboxes[2].getState())
            	{
               		tipo_conto = 3;
               		Visualizza.setEnabled(true);
               		testo[4].setEditable(true);
               		for(int i=0;i<4;++i)
                  	{	
                  		testo[i].setEditable(false);
            			testo[i].setText("");
            		}
            	}
         	}
      	});
      	
      	checkboxes[3].addItemListener(new ItemListener()
      	{
         	public void itemStateChanged(ItemEvent e)
         	{
               	if (!checkboxes[3].getState())
               	{
               		testo[5].setEditable(false);
         			testo[5].setText("");
         		}
         		else
         		{
         			testo[5].setEditable(true);
         			testo[5].setText(DateUtils.parseDate(DateUtils.giveStringOfDate(new Date())));
         		}
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
       
       	Visualizza.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
				startVisualizza();
         	}
       	});
	}       

	void startVisualizza()
	{
   		if (!errori())
   		{	
	  		this.setEnabled(false);
   			if (checkboxes[0].getState())
				startContoStanza();
   			else
   			{
   				if (checkboxes[1].getState())
   				{
   					startContoSingolo();
   				}
   				else	
   		       		startContoComitiva();
   		    }
   			displayConto();
     	}
	}
	
  	void displayConto()
  	{
  		visual = new MascheraVisualizzazioneConto(testo[0].getText(),testo[1].getText(),
  									testo[2].getText(),testo[3].getText(),testo[4].getText(),tipo_conto);
  		visual.setVisible(true);
  		visual.conto = this;
  	}
  	     
	boolean problemiStanza(String stanza)
	{	
		Frame msg;
		ListaSoggiornanti L_sogg;
		
		if ((stanza).equals("") )
		{	
			msg = new AvvisoDialog(this, " Inserire il numero di stanza ");
			return true;
		}
		else
			if ((Principale.db).readStanza(stanza) == null )
			{
				msg = new MessageDialog(this, " La stanza "+stanza+" e' inesistente! ");
				return true;
			}
			else
			{
				L_sogg = (Principale.db).foundSoggiornanti(stanza, false);
				if (L_sogg == null)
				{	
					msg = new MessageDialog(this, " Problemi con il database! ");
					return true;	
				}
				else
					if (L_sogg.length() == 0)
					{
						msg = new MessageDialog(this, " La stanza "+stanza+" non e' attualmente occupata ");
						return true;	
					}	
			}	
		return false;
	}

	//	controlla che nella lista non vi siano stanze ripetute piu' volte
	int chkDouble()
	{
		for(int i = 0; i<L_st.length; i++)
			for(int j = 0; j<L_st.length; j++)
				if ( (!L_st[i].equals("")) && (!L_st[j].equals("")) && (i!=j) 
					&& (L_st[i].equals(L_st[j]))  )
					return j;
		return -1;			  
	}

	Date[] tornaDateFineMagg(String elenco[])
	{
		Date data_fine_stanza = null;
		Date fine_sogg = null;
		Date date_fine_loc[] = new Date[elenco.length];
		
		for (int i = 0; i < elenco.length; i++)
		{
			ListaSoggiornanti L_sogg = (Principale.db).foundSoggiornanti(elenco[i], false);
			if (L_sogg != null)
			{
				data_fine_stanza = new Date(0, 0, 1);
				for (int j = 1; j <= L_sogg.length(); j++)
				{
					fine_sogg = L_sogg.getSoggiornante(j).getFineSogg();
					if ( Utils.data1MaggioreData2( fine_sogg, data_fine_stanza ) )
						data_fine_stanza = new Date(fine_sogg.getYear(), fine_sogg.getMonth(), fine_sogg.getDate());
				}
				date_fine_loc[i] = data_fine_stanza;
			}
			else
				return null;
		}
		return date_fine_loc;
	}
	
	Date[] tornaDateInizioMin(String elenco[])
	{
		Date data_inizio_stanza = null;
		Date inizio_sogg = null;
		Date date_inizio_loc[] = new Date[elenco.length];
		
		for (int i = 0; i < elenco.length; i++)
		{
			ListaSoggiornanti L_sogg = (Principale.db).foundSoggiornanti(elenco[i], false);
			if (L_sogg != null)
			{
				data_inizio_stanza = new Date(3000, 0, 1);
				for (int j = 1; j <= L_sogg.length(); j++)
				{
					inizio_sogg = L_sogg.getSoggiornante(j).getInizioSogg();
					if ( Utils.data1MinoreData2( inizio_sogg, data_inizio_stanza ) )
						data_inizio_stanza = new Date(inizio_sogg.getYear(), inizio_sogg.getMonth(), inizio_sogg.getDate());
				}
				date_inizio_loc[i] = data_inizio_stanza;
			}
			else
				return null;
		}
		return date_inizio_loc;
	}
	
	boolean aggiornamentoFlagsEffettuato(String elenco[])
	{
		qualcuno_tramite_agenzia = false;
		qualcuno_senza_agenzia = false;
		Prenotazione pren;
		
		for (int i = 0; i<elenco.length; i++)
		{
			ListaSoggiornanti L_sogg = (Principale.db).foundSoggiornanti(elenco[i], false);
			if (L_sogg != null)
			{
				for (int j = 1; j <= L_sogg.length(); j++)
				{
					pren = (Principale.db).readPrenotazione((L_sogg.getSoggiornante(j)).getIdPrenotazione());
					if (pren == null)
						return false;
					else
					{
						if (pren.getTramiteAgenzia() == Const.SI)
							qualcuno_tramite_agenzia = qualcuno_tramite_agenzia || true;
						else
							qualcuno_senza_agenzia = qualcuno_tramite_agenzia || true;
					}
				}
			}
			else
				return false;
		}
		return true;
	}
	
	boolean errori()
	{
		Frame msg;
		Soggiornante sogg = new Soggiornante();

		if (checkboxes[0].getState())
		{
			if (problemiStanza(testo[0].getText()))
				return true;
			L_st = new String[1];
			L_st[0] = new String(testo[0].getText());
		}
		else
			if (checkboxes[1].getState())
			{
				L_st = new String[1];
				L_st[0] = new String(testo[3].getText());
				sogg = (Principale.db).foundSoggiornante(testo[3].getText(),testo[2].getText(),testo[1].getText());
				if (sogg == null)
				{
					msg = new AvvisoDialog(this, " Nessun soggiornante presente nella stanza "+testo[3].getText()+ " risponde al nominativo inserito! ");
					return true;
				}
				ListaSoggiornanti L_sogg = (Principale.db).foundSoggiornanti(testo[3].getText(), false);
				if (L_sogg == null)
				{
					msg = new MessageDialog(this, " Problemi con il database! ");
					return true;
				} 
				if (sogg.getPagato() == Const.SI)
				{
					msg = new AvvisoDialog(this, " Il soggiornante selezionato o ha gia' pagato o se ne e' gia' andato! ");
					return true;
				}
				else
				{
					// procedo con il verificare se il cliente e' l'ultimo rimasto
					ListaSoggiornanti sogg_no_pagato = new ListaSoggiornanti();
					for (int i=1; i<=L_sogg.length(); i++)
						if (L_sogg.getSoggiornante(i).getPagato() == Const.NO)
							sogg_no_pagato.addSoggiornante(L_sogg.getSoggiornante(i));
					if (sogg_no_pagato.length() == 1)
					{
						msg = new AvvisoDialog(this, " Il soggiornante e' l'ultimo rimasto. Scegliere 'conto per stanza'! ");
						return true;
					}
					sogg_x_conto_sing = sogg;
				}
			}
			else		
				if (checkboxes[2].getState())
				{
					L_st = Utils.parseStanze(testo[4].getText());
					if (L_st == null)
					{	
						msg = new MessageDialog(this, " Errore nell'elenco delle stanze. Inserire le stanze nell'elenco nel modo seguente: 101,102,103,...");
						return true;					
					}
					else
					{
						Stanza room;
						for (int i = 0; i < L_st.length; i++)
							if  (problemiStanza(L_st[i]))
								return true;
						int res = chkDouble();
						if ( res != -1)
						{
							msg = new MessageDialog(this, " Errore nell'elenco delle stanze. La stanza "+L_st[res]+" e presente piu' volte nell'elenco! ");
							return true;
						}	
					}
				}
		if (checkboxes[0].getState() || checkboxes[2].getState())
		{
			date_fine = tornaDateFineMagg(L_st);
			date_inizio = tornaDateInizioMin(L_st);
		}
		else
		{
			Prenotazione pren = (Principale.db).readPrenotazione(sogg.getIdPrenotazione());
			if (pren != null)
			{
				
				date_fine = new Date[1];
				date_inizio = new Date[1];
				date_fine[0] = sogg.getFineSogg();
				date_inizio[0] = sogg.getInizioSogg();
			}
			else
				date_fine = null;
		}
		if (date_fine == null || date_inizio == null)
		{
			msg = new MessageDialog(this, " Problemi con il database! ");
			return true;
		}
		// la variabile data_fine contiene le date di fine corrette

		data_fine_magg = new Date(0, 0, 1);
		for (int i=0; i<date_fine.length; i++)
			if (Utils.data1MaggioreData2(date_fine[i], data_fine_magg))
				data_fine_magg = date_fine[i];
		// ora la variabile data_fine_magg contiene la data di fine maggiore
		
		if (!aggiornamentoFlagsEffettuato(L_st))
		{
			msg = new MessageDialog(this, " Problemi con il database! ");
			return true;
		}
 		// da ora in poi le flags per l'agenzia contengono valori significativi

		// questo if deve essere messo prima di quello che segue
		if (qualcuno_tramite_agenzia && qualcuno_senza_agenzia)
		{
			msg = new MessageDialog(this, " L'elenco delle stanze include stanze commissionate e non! Procedere al calcolo separato. ");
			return true;
		}
		// L'if che segue deve essere posto dopo a quello che precede
		if (checkboxes[2].getState() && qualcuno_tramite_agenzia)
		{
			msg = new MessageDialog(this, " Il conto comitiva non e' possibile per le agenzie! ");
			return true;
		}

		// se arrivo qui significa che si puo' fare il conto normale,
		// rimane da verificare se si desidera fare una partenza anticipata
		// e in tal caso verificare che la data inserita sia corretta!
		if ( (checkboxes[3].getState()) && (DateUtils.dataCorretta(testo[5].getText())) )
		{	
			Date data = DateUtils.convertDate(testo[5].getText());
			Date data_oggi = new Date();
			// controllo che la data di partenza anticipata abbia senso:
			// 1) controllo che il cliente abbia soggiornato almento un giorno
			for (int i=0; i<date_inizio.length; i++)
				if (Utils.data1MaggioreData2(date_inizio[i], DateUtils.giornoPrimaDi(data)))
				{
					msg = new MessageDialog(this, " I clienti devono soggiornare almeno un giorno prima di partire! ");
					return true;
				}
			// 2) controllo che la partenza anticipata non sia prima di oggi
			if (Utils.data1MinoreData2(data, data_oggi))
			{
				msg = new AvvisoDialog(this, " La data non puo' essere precedente a quella odierna! ");
				testo[5].setText(DateUtils.parseDate(DateUtils.giveStringOfDate(data_oggi)));
				return true;
			}
			else
				// 3)  controllo che il fine soggiorno anticipato (ossia il giorno prima della
				//     partenza anticipata) non sia dopo il fine soggiorno effettivo
				for (int i=0; i<date_fine.length; i++)
					if (Utils.data1MaggioreData2(data, date_fine[i]))
					{
						msg = new AvvisoDialog(this, " Non si tratta di una partenza anticipata per la stanza "+L_st[i]+" con tale data! ");
						testo[5].setText(DateUtils.parseDate(DateUtils.giveStringOfDate(data_oggi)));
						return true;
					}
			fine_sogg_anticipato = DateUtils.giornoPrimaDi(data);
		}
		else
		{
			if  ( (checkboxes[3].getState()) && !DateUtils.dataCorretta(testo[5].getText()))
			{	
				msg = new MessageDialog(this, " La data di partenza anticipata e' errata! ");
				return true;
			}
			else
			{
				// il checkboxes[3] non e' attivo
				fine_sogg_anticipato = data_fine_magg;
				if (qualcuno_tramite_agenzia && checkboxes[1].getState())
				{	
					msg = new MessageDialog(this, " Il cliente e' in una stanza di agenzia! Se deve andarsene scegliere 'partenza anticipata'. ");
					return true;
				}
			}
		}
		return false;
	} // errori
	
	void startContoStanza()
	{
		Frame msg;
		
		if (!qualcuno_tramite_agenzia)
		{
			if (checkboxes[3].getState())
				CalcoloConto.calcolaContoStanza(testo[0].getText(), 1, false, fine_sogg_anticipato);
			else
				CalcoloConto.calcolaContoStanza(testo[0].getText(), 1, false, null);
		}
		else
		{	
			CalcoloConto.calcolaContoStanzaAgenzia(testo[0].getText());
		}		
	}

	void startContoSingolo()
	{
		if (qualcuno_tramite_agenzia)
		{
			Frame msg;
			Date fine_effettiva;
			if (checkboxes[3].getState())
				fine_effettiva = fine_sogg_anticipato;
			else
				fine_effettiva = data_fine_magg;
			
   			if ( Utils.data1MinoreData2(fine_effettiva, new Date()) )
				msg = new AskChiudiSingoloConAgenzia(this);
			else
				msg = new MessageDialog(this, " La data e' posteriore ad oggi: non e' possibile chiudere! ");
   			return;
   		}
   		else
			if ( (checkboxes[3].getState()) )
				CalcoloConto.calcolaContoSingolo(sogg_x_conto_sing, fine_sogg_anticipato);
			else
				CalcoloConto.calcolaContoSingolo(sogg_x_conto_sing, null);
	}
	
	void startContoComitiva()
	{
		if ( (checkboxes[3].getState()) )
			CalcoloConto.calcolaContoComitiva(L_st, fine_sogg_anticipato);
		else
			CalcoloConto.calcolaContoComitiva(L_st, null);
	}
	
	void startChiudiSingoloConAgenzia()
	{
		if (checkboxes[3].getState())
		{
			Date new_data = DateUtils.convertDate(testo[5].getText());
			new_data = DateUtils.giornoPrimaDi(new_data);
			(Principale.db).anticipaFineSogg(sogg_x_conto_sing.getNumStanza(),
									sogg_x_conto_sing.getIdSoggiornante(),
									new_data);
		}
		(Principale.db).reversePagato(sogg_x_conto_sing.getNumStanza(),
									  sogg_x_conto_sing.getIdSoggiornante());
		for (int i=0; i<testo.length-1; i++)
			testo[i].setText("");
	}
}