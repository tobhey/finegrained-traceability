package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import java.util.Date;
import moduli.*;

public class RicercaPrenotazione extends Frame 
{  
	Prenotazione prenotazione;
    
    //Dichiarazioni di variabili
    Panel panel1, panel2, panel3, panel4; 
    Label  etichetta1, label1, label2, label3, label4;
    List lista;
    Button  Annulla, Cerca, Scegli, Aggiungi;
    TextField t_nome, t_cognome;
    Checkbox chbx1, chbx2, chbx3;
    CheckboxGroup filtri;
    GridBagLayout gridbag = new GridBagLayout();
	Frame padre = new Frame();
  	ListaPrenotazioni L, L_visual;
  	Frame figlio;  
    int to_call;
 	boolean first_time = true;   
    
    public RicercaPrenotazione(String titolo,int k)
    {
        super(titolo);
        to_call = k;
        setupPanels();
        init();
        pack(); 
    }
                     
        void setupPanels()
        {  
			this.setFont(ConfigurazioneSistema.font_base);
            
            //Creo dei pulsanti e ne disabilito due  
            Annulla = new Button(" Fine ");
            Cerca = new Button(" Cerca ");
            Scegli = new Button(" Scegli ");
			Aggiungi = new Button(" Aggiungi a stanza ");
            Scegli.setEnabled(false);
            Aggiungi.setEnabled(false);
            Cerca.setEnabled(true);
            //Creo le etichette
            etichetta1 = new Label ("Inserire il nominativo della prenotazione");
            etichetta1.setFont(ConfigurazioneSistema.font_titolo);
            label1 = new Label("Cognome");
            label2 = new Label("Nome");
			label3 = new Label("Risultato della ricerca");
			label3.setFont(ConfigurazioneSistema.font_titolo);
			label4 = new Label("Cercare le prenotazioni reletive a:");
			label4.setFont(ConfigurazioneSistema.font_titolo);
            
            //Creo i TextField e ne rendo  alcuni non editabili
            t_nome = new TextField("", 20);
            t_cognome = new TextField("", 20);
            
            //creo il pannello con i filtri per la ricerca
			filtri = new CheckboxGroup();
			chbx1 = new Checkbox(" Arrivi odierni",filtri, true);
			chbx2 = new Checkbox(" Soggiornanti attuali",filtri, false);
			chbx3 = new Checkbox(" Prenotazioni future", filtri,false);            
			
            //Creo il pannello in alto per inserimento dei dati del cliente
            panel1 = new Panel();
            panel1.setLayout(gridbag);
            Utils.constrain(panel1, etichetta1, 0, 0, 4, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);
            Utils.constrain(panel1, label1, 0, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.5, 5, 5, 5, 5);
            Utils.constrain(panel1, t_cognome,1, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel1, label2, 2, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.5, 5, 5, 5, 5);
            Utils.constrain(panel1, t_nome, 3, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);            
            Utils.constrain(panel1, label4, 0, 2, 4, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);
            Utils.constrain(panel1, chbx1, 0, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.5, 5, 5, 5, 5);
			if (to_call != 2 && to_call != 1)
            	Utils.constrain(panel1, chbx2, 0, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
            if (to_call != 3)
	            Utils.constrain(panel1, chbx3, 0, 5, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.5, 5, 5, 5, 5);
           
            //Creo il pannello in basso con due pulsanti                  
            panel2 = new Panel();
            panel2.setLayout(gridbag);
            Utils.constrain(panel2, Annulla, 0, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel2, Cerca, 1, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 5, 5, 5, 5);             
            Utils.constrain(panel2, Scegli, 2, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 5, 5, 5, 5);
            if (to_call ==3)
            Utils.constrain(panel2, Aggiungi, 3, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 5, 5, 5, 5);                                     
            
            
            
            //Creo il pannello con la lista dei risultati della ricerca
            lista = new List(10,false);
            lista.setFont(ConfigurazioneSistema.font_allineato);
            panel3 = new Panel();
            panel3.setLayout(gridbag);
            Utils.constrain(panel3, label3, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.WEST, 1.0, 0.0, 0, 0, 0, 0);
            Utils.constrain(panel3, lista, 0, 1, 4, 3, GridBagConstraints.BOTH,
                        GridBagConstraints.CENTER, 3.0, 3.0, 0, 0, 0, 0);             
            
            
            //Attacco i pannelli al frame
            this.setLayout(gridbag);
            Utils.constrain(this, panel1, 0, 1, 4, 6, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(this, panel2, 0, 14, 4, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.CENTER, 1.0, 0.0, 5, 5, 5, 5); 
        	Utils.constrain(this, panel3, 0, 15, 4, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5); 
        	panel3.setVisible(false);
        	pack();
        
        }

    public void init()
    {
		lista.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
         		prenotazione = L_visual.getPrenotazione((lista.getSelectedIndexes())[0]+1);
				switch (to_call)
				{
					case 1: startModifica(prenotazione); break;
					case 2: startCancella(prenotazione); break;	
					case 3: 
						if (!chbx2.getState())
							startInsSoggiornante(prenotazione);
						else
							startAggiungi();
						break;
					case 4: startVediPrenotazione(prenotazione); break;
				}
			}
      	});
     
	    Scegli.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
         		if (!noSelection())
         		{
         			prenotazione = L_visual.getPrenotazione((lista.getSelectedIndexes())[0]+1);
					switch (to_call)
					{
						case 1: startModifica(prenotazione); break;
						case 2: startCancella(prenotazione); break;	
						case 3: startInsSoggiornante(prenotazione); break;
						case 4: startVediPrenotazione(prenotazione); break;
					}
				}
			}
		});
        
		Aggiungi.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
				startAggiungi();
			}
    	});
		
		t_nome.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				creaLista(0);
			}			      	
		});
	
		t_cognome.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				creaLista(0);
			}			      	
		});	

    	Cerca.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
				creaLista(0);
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

    	chbx1.addItemListener(new ItemListener()   
      	{
         	public void itemStateChanged(ItemEvent e)
         	{
				if (!first_time)
         			creaLista(0);
         	}
      	});
                      
      	chbx2.addItemListener(new ItemListener()   
      	{
         	public void itemStateChanged(ItemEvent e)
         	{
				if (!first_time)
         			creaLista(0);
         	}
      	});
      
      	chbx3.addItemListener(new ItemListener()   
      	{
         	public void itemStateChanged(ItemEvent e)
         	{
				if (!first_time)
         			creaLista(0);	
        	}
      	});
    } 

	void startAggiungi()
	{
   		if (!noSelection())
   		{
   			this.setEnabled(false);
   			prenotazione = L_visual.getPrenotazione((lista.getSelectedIndexes())[0]+1);
			startAddSoggiornante(prenotazione);
		}
	}
	
	void startModifica(Prenotazione pren)
	{
		this.setEnabled(false);
		Stanza stanza = (Principale.db).readStanza(pren.getNumStanza());
		ModificaPrenotazione modify = new ModificaPrenotazione(pren, stanza, this);
		modify.setVisible(true);
		modify.padre = this;
		notifyRoomBlocked(pren, modify, null);	
	}

	void startCancella(Prenotazione pren)
	{
		this.setEnabled(false);
		Stanza stanza = (Principale.db).readStanza(pren.getNumStanza());
		CancellaPrenotazione cancel = new CancellaPrenotazione(pren, stanza, this);
		cancel.setVisible(true);
		cancel.padre = this;	
	}
	
	void startVediPrenotazione(Prenotazione pren)
	{
		this.setEnabled(false);
		Stanza stanza = (Principale.db).readStanza(pren.getNumStanza());
		VediPrenotazione visual = new VediPrenotazione(pren, stanza, this);
		visual.setVisible(true);
		visual.padre = this;	
		notifyRoomBlocked(pren, null, visual);	
	}	
	
	void startInsSoggiornante(Prenotazione pren)
	{
		Disponibilita disp;
		Frame msg;
		
		disp = (Principale.db).readDisponibilita(pren.getNumStanza());
		if	(disp == null)
			msg =  new MessageDialog(this," Inconsistenze sul database! ");
		else
		{
			if ( disp.getStatusGiorno(DateUtils.dataTogiorni(pren.getInizioSogg()),Const.ANNO_CORRENTE) == Flag.BLOCCATA )
				msg = new AvvisoDialog(this," La stanza "+pren.getNumStanza()+" non e' assegnata a tale prenotazione. Modificare la prenotazione. ");
			else
				figlio = new InserisciSoggiornante(this, pren);
		}
	}

	
	void notifyRoomBlocked(Prenotazione pren, ModificaPrenotazione father1, VediPrenotazione father2)
	{
		char flag; 
		Frame msg;
		ListaDisponibilita elenco_disp = (Principale.db).elencoDisponibilita();
		if (elenco_disp == null)
		{
			msg = new MessageDialog(this, " Problemi con il database nella lettura delle disponibilita'! ");
			return;
		}
		Disponibilita disp = Utils.getDispOfRoom(elenco_disp, pren.getNumStanza(), false);
		Costanti cost = new Costanti();
		if ( (((pren.getInizioSogg()).getYear())+1900 ) == cost.getAnnoCorr())
			flag = Const.ANNO_CORRENTE;
		else
			flag = Const.ANNO_PROSSIMO;	
		if (father2 == null)
			if (disp.getStatusGiorno(DateUtils.dataTogiorni(pren.getInizioSogg()), flag) == Flag.BLOCCATA)
    			msg = new AvvisoDialog(father1," ATTENZIONE: per questa prenotazione non e' ancora stata assegnata la stanza!!");
    		else
				(father1.Assegna).setEnabled(false);
		else
			if (disp.getStatusGiorno(DateUtils.dataTogiorni(pren.getInizioSogg()), flag) == Flag.BLOCCATA)
    			msg = new AvvisoDialog(father2," ATTENZIONE: per questa prenotazione non e' ancora stata assegnata la stanza!!");
    		else
				(father2.Assegna).setEnabled(false);			
	}
	
	public void creaLista(int back)
	{
		Frame msg;
		Prenotazione p;		
		Date today = new Date();
		L_visual = new ListaPrenotazioni();
		L = (Principale.db).foundPrenotazioni(t_cognome.getText(), t_nome.getText());
		if (L != null)
		{
			if (!L.isEmpty())
			{
				if (lista.getItemCount() > 0)
					lista.removeAll();
				for(int i = 1; i <= L.length(); i++)
				{
					p = L.getPrenotazione(i); 
					ListaSoggiornanti L_sogg = (Principale.db).foundSoggiornanti(p.getIdPrenotazione());
					if (chbx3.getState())	
					{
						if ( Utils.data1MaggioreData2( p.getInizioSogg(), today ) )
						{
							lista.addItem(p.toString());
							L_visual.addPrenotazione(p);
						}
					}
					else
					{
						if (chbx1.getState())
						{
							if (L_sogg.isEmpty())
							{
								if (((p.getInizioSogg()).getDate() == today.getDate())
									&& ((p.getInizioSogg()).getMonth() == today.getMonth())
									&& ((p.getInizioSogg()).getYear() == today.getYear()) )
								{	
									lista.addItem(p.toString());			
									L_visual.addPrenotazione(p);
								}
							}
						}
						else
						{	
							if ( !( Utils.data1MaggioreData2( p.getInizioSogg(), today ) ) && 
								!( Utils.data1MinoreData2( p.getFineSogg(), today) ) )
							{
								//controllo che sia un soggiornante gia registrato
								if (!L_sogg.isEmpty())
								{	
									lista.addItem(p.toString());				
									L_visual.addPrenotazione(p);
								}
							}
						}	
					}
				}					
				if (lista.getItemCount() == 0)
				{
					if (back == 0)
						msg = new AvvisoDialog(this, " Nessuna prenotazione trovata! ");
					Scegli.setEnabled(false);
					Aggiungi.setEnabled(false);
					panel3.setVisible(false);
					pack();
					if (to_call == 3)
						Aggiungi.setEnabled(false);
				}
				else
				{
					if (chbx1.getState())
					{
						Aggiungi.setEnabled(false);
		       			Scegli.setEnabled(true);
					}
					if (chbx2.getState())
					{
		       			Aggiungi.setEnabled(true);
						if (to_call == 3)
		       				Scegli.setEnabled(false);
    			   		else
			       			Scegli.setEnabled(true);
					}
					if (chbx3.getState())
					{
						Scegli.setEnabled(true);
					}
					panel3.setVisible(true);
					pack();
				}			
				if (first_time)
					first_time = false;
			}
			else
			{
				if (back == 0)
					msg = new AvvisoDialog(this, " Prenotazione non trovata! ");
				else
				{
					if (back == 1)
					{
						if (lista.getItemCount() > 0)
							lista.removeAll();
						panel3.setVisible(false);
						pack();
					}
				}
			}
		}
		else
			msg = new MessageDialog(this, " Problemi con il database! ");
	}			

	boolean noSelection()
	{
		Frame msg;
		if (lista.getSelectedIndex() == -1)
		{
			msg = new AvvisoDialog(this," Selezionare una prenotazione dalla lista e ripremere il tasto");
			return true;
		}	
		return false;
	}

	void startAddSoggiornante(Prenotazione pren)
	{
		ListaSoggiornanti L_sogg = (Principale.db).foundSoggiornanti(pren.getIdPrenotazione());
		if (L_sogg != null)
			if (L_sogg.length() > 0)
			{	
				Soggiornante sogg = L_sogg.getSoggiornante(1);
				Stanza room = (Principale.db).readStanza(sogg.getNumStanza());
				if ( (room.getPostiLetto() > L_sogg.length()) ||
	       			( (room.getPostiLetto() == L_sogg.length()) && (room.getDispLettoAgg() == Const.SI) ) )
		 			figlio = new AggiungiSoggiornante(this, pren, room, L_sogg.length());
				else
					figlio = new AvvisoDialog(this," Nessun posto disponibile nella stanza n. "+room.getNumStanza());
			}
			else
				figlio = new AvvisoDialog(this," La prenotazione non e' adempiuta. Premere 'Scegli' per gestire l'arrivo clienti");
		else
			figlio = new MessageDialog(this," Problemi con il database!");
	}
}
