package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import java.util.Date;
import moduli.*;

public class MascheraRicercaStanza extends Frame
{
  	 //Dichiaro gli oggetti che mi serviranno per costruire la finestra
    Panel panel1, panel2, panel3, panel4, panel5;
	Label etichetta1, etichetta2, etichetta3, etichetta, etichetta4, etichetta5;
	Button  Avvio, Annulla, ShowDisp, Annulla1, Conferma;
	TextField data_inizio, data_fine, nome_ag;
	List lista;
	Checkbox con_ag;
	GridBagLayout gridbag = new GridBagLayout();
	InserisciPrenotazione pren;
	InserisciCommissioni comm;
	Frame figlio;
	Frame padre = new Frame();
    MascheraCambio father;
    int caller;
    ListaStanze stanze_disp;
    Date save_date;
	String nomi_agenzie[] = new String[0];

	// seguono due variabili necessarie per il passaggio dei parametri 
	// della Inserisci
    String data1;
    String data2;
    
    public MascheraRicercaStanza(String title, int c) 
    {
        super(title);
        caller = c;
        setupPanels();
        init();
        pack();
    }
                     
    void setupPanels()
    {
		this.setFont(ConfigurazioneSistema.font_base);
        //Creo i TextField
        data_inizio = new TextField("", 12);
        data_fine = new TextField("", 12);
		nome_ag = new TextField("", 20);
		nome_ag.setEditable(false);
        
        //Creo i pulsanti e ne disabilito due
        Annulla = new Button("Indietro");
        Annulla1 = new Button("Annulla");
        Conferma = new Button(" Scegli ");
        Avvio = new Button("Avvio Ricerca");
        ShowDisp = new Button("Disponibilita'");
        Annulla1.setEnabled(false);
        Conferma.setEnabled(false);

        //Creo le etichette
        etichetta= new Label("Periodo di soggiorno");
        etichetta.setFont(ConfigurazioneSistema.font_titolo);
        etichetta1 = new Label("Data inizio:");
        etichetta2 = new Label("Data fine:");
        etichetta3 = new Label ("Risultato della ricerca:");
        etichetta3.setFont(ConfigurazioneSistema.font_titolo);
		etichetta4 = new Label(" Nome agenzia");
		etichetta5 = new Label ("Prenotazione di stanze commissionate:");
        etichetta5.setFont(ConfigurazioneSistema.font_titolo);

        //Creo la lista
        lista = new List(15, false);

       	//Creo il checkbox
       	con_ag = new Checkbox(" Prenotazione tramite agenzia", false);
       
        //Creo il pannello in alto      
        panel1 = new Panel();
        panel1.setLayout(gridbag);
        Utils.constrain(panel1, etichetta, 0, 0, 4, 1, GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 10, 0);
        Utils.constrain(panel1, etichetta1, 0, 1, 1, 1, GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);
        Utils.constrain(panel1, data_inizio, 1, 1, 1, 1, GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST,1.0,0.0,0,0,0,0);
        Utils.constrain(panel1, etichetta2, 2, 1, 1, 1, GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);
        Utils.constrain(panel1,data_fine, 3, 1, 1, 1, GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);
        
        panel5 = new Panel();
        panel5.setLayout(gridbag);
        Utils.constrain(panel5, etichetta5, 0, 0, 1, 1, GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);        
        Utils.constrain(panel5, con_ag,     0, 1, 1, 1, GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);
        Utils.constrain(panel5, etichetta4, 1, 1, 1, 1, GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);
        Utils.constrain(panel5, nome_ag,    2, 1, 1, 1, GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);             

        
        
        //Creo un pannello con due pulsanti
        panel2 = new Panel();
        panel2.setLayout(gridbag);
        Utils.constrain(panel2, Annulla, 1, 0, 1, 1, GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST, 0.3, 0.0, 0, 0, 0, 1);
        Utils.constrain(panel2, Avvio, 2, 0, 1, 1, GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST, 0.3, 0.0, 0, 0, 0, 1);
        Utils.constrain(panel2, ShowDisp, 3, 0, 1, 1, GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST, 0.3, 0.0, 0, 0, 0, 1);             

        //Creo il pannello contenente la lista
        panel3 = new Panel();
        panel3.setLayout(gridbag);
        Utils.constrain(panel3, etichetta3, 0, 0, 2, 1, GridBagConstraints.NONE,
                     GridBagConstraints.WEST, 1.0, 0.0, 0, 0, 0, 0);
        Utils.constrain(panel3,lista, 0, 1, 4, 3, GridBagConstraints.BOTH,
                     GridBagConstraints.CENTER, 3.0, 3.0, 0, 0, 0, 0);

        //Creo il pannello in basso con due pulsanti
        panel4 = new Panel();
        panel4.setLayout(gridbag);
        Utils.constrain(panel4, Annulla1, 0, 1, 1, 1, GridBagConstraints.NONE,
                     GridBagConstraints.SOUTHEAST, 0.3, 0.0, 0, 0, 0, 10);
        Utils.constrain(panel4, Conferma, 1, 1, 1, 1,GridBagConstraints.NONE,
                     GridBagConstraints.SOUTHWEST, 0.3, 0.0, 0, 0, 0, 0);

        
        //Attacco i pannelli al frame
        this.setLayout(gridbag);
        Utils.constrain(this, panel1, 0, 0, 4, 2, GridBagConstraints.HORIZONTAL,
                     GridBagConstraints.NORTHWEST, 1.0, 0.0, 15, 10, 10, 10);
        Utils.constrain(this, panel5, 0, 2, 4, 1, GridBagConstraints.HORIZONTAL,
                     GridBagConstraints.NORTHWEST, 1.0, 0.0, 15, 10, 10, 10);             
        Utils.constrain(this, panel2, 0, 3, 4, 1,GridBagConstraints.HORIZONTAL,
                     GridBagConstraints.NORTHEAST, 1.0, 0.0, 10, 10, 0, 10);
        Utils.constrain(this, panel3, 0, 4, 4, 3, GridBagConstraints.BOTH,
                     GridBagConstraints.CENTER, 2.0, 2.0, 10, 10, 10, 10);
        Utils.constrain(this, panel4, 0, 7, 4, 1, GridBagConstraints.HORIZONTAL,
                     GridBagConstraints.SOUTHWEST, 1.0, 1.0, 10, 10, 15, 10);
		
		if (caller == 1)
		{		
			panel5.setVisible(false);
			pack();
		}
	}

    //Gestione degli eventi
    public void init()
    {
        //Ascoltatore del pulsante Avvio,se il pulsante viene premuto viene
        //lanciata la procedura di ricerca stanze libere
        Avvio.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
				ctrlAvvio();
            } 
        });

        //Ascoltatore del pulsante Annulla,se il pulsante viene prumeto si torna
        //alla maschera precedente
        Annulla.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dispose();
                if (caller == 3)
                	father.setEnabled(true);
            	else
            	   	padre.setEnabled(true);
            }
        });
        
        //Ascoltatore del pulsante ShowDisp,se il pulsante viene premuto si 
        //mostrera' una maschera con le disponibilita dell'albergo
        ShowDisp.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
				lookDisp();                
            }
        });

        //Ascoltatore del pulsante Annulla1,se il pulsante viene premuto si
        //vengono riabilitati i componenti del pannello in alto e
        //disabilitati quelli del pannello in basso
        Annulla1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	startAnnulla1();
			}
		});

        //Ascoltatore del pulsante Conferma, se il pulsante viene premuto si apre
        //la finestra riguardante l'inserimento della prenotazione
        Conferma.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
				startInserimento();
			}
		});
			
		lista.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				startInserimento();
			}
        });
	
		con_ag.addItemListener(new ItemListener()   
      	{
         	public void itemStateChanged(ItemEvent e)
         	{
               	
               	if (con_ag.getState())
               		nome_ag.setEditable(true);
               	else
               	{
               		nome_ag.setEditable(false);
         			nome_ag.setText("");
         		}
         	}
      	});
	}

	boolean dataInizioIsNotBeforeToday()
	{
		Date inizio_pren = DateUtils.convertDate(data_inizio.getText());
		Date today = new Date();
		
		if (inizio_pren.getYear() < today.getYear())
			return false;
		else
			if (inizio_pren.getYear() > today.getYear())
				return true;
			else // l'anno e' il medesimo
				if (inizio_pren.getMonth() < today.getMonth())
					return false;
				else
					if (inizio_pren.getMonth() > today.getMonth())
						return true;
					else // l'anno e il mese sono i medesimi
						if (inizio_pren.getDate() < today.getDate())
							return false;
						else
							return true;
	}
	
	/*	se la ricerca viene lanciata per un cambio stanza (caller = 3) si deve
	 * 	impedire che la data di fine venga posticipata rispetto la fine del soggiorno
	 *	o anticipata rispetto la data del cambio stanza.
	 */	
	void ctrlAvvio()
	{
		Frame msg;

		if (Utils.dateEPeriodoCorretti(this, data_inizio.getText(), data_fine.getText()))
		{
			if (con_ag.getState() && nome_ag.getText().equals(""))
			{
				msg = new MessageDialog(this, " Manca il nome dell'agenzia! ");
				return;
			}
			
			ListaDisponibilita L_disp = (Principale.db).elencoDisponibilita();
			if (L_disp == null)
			{
				msg = new MessageDialog(this, " Problemi con il database nella lettura delle disponibilita'! ");
				return;
			}
			if (caller == 3)
			{	
				if (Utils.data1MaggioreData2(DateUtils.convertDate(data_fine.getText()), save_date) )
				{	
					msg = new AvvisoDialog(this, "ATTENZIONE: la data non puo' essere posticipata");
					data_fine.setText(DateUtils.parseDate(DateUtils.giveStringOfDate(save_date)));
				}
				else
					if (Utils.data1MinoreData2(DateUtils.convertDate(data_fine.getText()), DateUtils.convertDate(data_inizio.getText()) ) )
					{
						msg = new AvvisoDialog(this, "ATTENZIONE: la data non puo' essere precedente alla data di inizio");
						data_fine.setText(DateUtils.parseDate(DateUtils.giveStringOfDate(save_date)));			
					}
					else
					{
						stanze_disp = new ListaStanze();
						startAvvio(L_disp, 0);
					}
			}
			else
			{
				if (dataInizioIsNotBeforeToday())
				{
					stanze_disp = new ListaStanze();
					startAvvio(L_disp, 0);
				}
				else
					msg = new MessageDialog(this, " La data di inizio deve essere maggiore o uguale di quella odierna! ");
			}
		}
	}
	
	void startAnnulla1()
	{
		data_inizio.setEditable(true);
		data_fine.setEditable(true);
		con_ag.setEnabled(true);
		if ( con_ag.getState() )
			nome_ag.setEditable(true);
		Annulla.setEnabled(true);
		Avvio.setEnabled(true);
		Annulla1.setEnabled(false);
		Conferma.setEnabled(false);
		if (lista.getItemCount() > 0)
			lista.removeAll();
		stanze_disp = new ListaStanze();
	}
	
	void startAvvio(ListaDisponibilita L_disp, int k)
	{
		if (con_ag.getState())
			startSearchComm(L_disp, k);
		
		// le date sono corrette se si arriva qui in quanto controllate in ctrlAvvio()
		Frame msg;
		String str = new String("");
		ListaCommissionamenti L_comm = new ListaCommissionamenti();
		ListaStanze elenco_stanze_disp = new ListaStanze();

		// variabili necessarie per passaggio dei parametri della Inserisci
		data1 = DateUtils.parseDate(data_inizio.getText());
		data2 = DateUtils.parseDate(data_fine.getText());

		// scansione delle disponibilita
		Stanza stanza = null;
		Disponibilita disp_attuale = null;
		for (int i = 1; i <= L_disp.length(); i++)
		{
			disp_attuale = L_disp.getDisponibilita(i);
			if ( disp_attuale.isDisponibile( DateUtils.convertDate(data_inizio.getText()), DateUtils.convertDate(data_fine.getText()), Flag.DISPONIBILE) )
			{
				stanza = (Principale.db).readStanza(disp_attuale.getNumStanza());
				if (stanza != null)
				{
					elenco_stanze_disp.addStanza(stanza);
					stanze_disp.addStanza(stanza);
				}
				else
					msg = new MessageDialog(this, " Problemi con il database! ");
			}
		}
		
		if (!stanze_disp.isEmpty())
		{
			Avvio.setEnabled(false);
			Annulla.setEnabled(false);
			data_inizio.setEditable(false);
			data_fine.setEditable(false);
			Annulla1.setEnabled(true);
			Conferma.setEnabled(true);
			con_ag.setEnabled(false);
			nome_ag.setEditable(false);
			for (int i = 1; i <= elenco_stanze_disp.length(); i++)
				lista.addItem(str+(elenco_stanze_disp.getStanza(i)).toString());
		}
		else
			if (k == 0)
				if (!con_ag.getState())
					msg = new AvvisoDialog(this, " Nessuna stanza disponibile in tale periodo! ");
				else
					msg = new AvvisoDialog(this, " Nessuna stanza disponibile o commisionata in tale periodo! ");	
	}
	
	void startSearchComm(ListaDisponibilita L_disp, int k)
	{
		// le date sono corrette se si arriva qui in quanto controllate in ctrlAvvio()
		Frame msg;
		
		Commissionamento c;
		Stanza stanza = null;
		Disponibilita disp_loc = null;
		ListaCommissionamenti L_comm = new ListaCommissionamenti();;
		ListaStanze elenco_stanze_disp = new ListaStanze();
		Date d1 = DateUtils.convertDate(data_inizio.getText());
 		Date d2 = DateUtils.convertDate(data_fine.getText());
		
		data1 = DateUtils.parseDate(data_inizio.getText());
		data2 = DateUtils.parseDate(data_fine.getText());
		L_comm = (Principale.db).foundCommissionamentiSenzaLike(nome_ag.getText());
		if (L_comm == null)
		{
			msg = new MessageDialog(this, " Problemi con il database nella lettura delle commissioni! ");
			return;
		}
		if ( !L_comm.isEmpty() )
		{
			nomi_agenzie = new String[L_comm.length()];
			int indice_nomi = 0;
			for (int i = 1; i <= L_comm.length(); i++)
			{
				c = L_comm.getCommissionamento(i);
				// Controllo che le date inserite appartengano al periodo commissionato
				if( (!Utils.data1MaggioreData2(c.getInizioComm(), d1)) && (!(c.getFineComm()).before(d2))) 
				{
					disp_loc = Utils.getDispOfRoom(L_disp, c.getNumStanza(), false);
					if (disp_loc.isDisponibile(d1, d2, Flag.COMMISSIONATA))
					{
						stanza = (Principale.db).readStanza(disp_loc.getNumStanza());
						if (stanza != null)
						{	
							elenco_stanze_disp.addStanza(stanza);
							stanze_disp.addStanza(stanza);
							nomi_agenzie[indice_nomi] = new String(c.getNomeAgenzia());
							indice_nomi++;
						}
						else
						{
							msg = new MessageDialog(this, " Problemi con il database! ");
							return;
						}
					}
				}
			}
		}	
		if (!stanze_disp.isEmpty())
		{
			Avvio.setEnabled(false);
			Annulla.setEnabled(false);
			data_inizio.setEditable(false);
			data_fine.setEditable(false);
			Annulla1.setEnabled(true);
			Conferma.setEnabled(true);
			for (int i = 1; i <= elenco_stanze_disp.length(); i++)
				lista.addItem("COMMISSIONATA A "+nomi_agenzie[i-1]+" "+(elenco_stanze_disp.getStanza(i)).toString());
		}
	} // startSearchComm
	
	void restartAvvio()
	{
		Frame msg;
		
		ListaDisponibilita L_disp = (Principale.db).elencoDisponibilita();
		if (L_disp == null)
		{
			msg = new MessageDialog(this, " Problemi con il database nella lettura delle disponibilita'! ");
			return;
		}
		startAnnulla1();
		startAvvio(L_disp, 1);
	}
	
    void startInserimento()
    {
		Frame msg;
		if (lista.getSelectedIndex() != -1)
			switch (caller)
			{
				case 1: visualInsertComm(); break;
				case 2: visualInsertPren(); break;
				case 3: returnFreeRoom(); break;
			}	
		else
			msg = new MessageDialog(this, " Manca la selezione! ");
    }
    
    void lookDisp()
    {
   	 		figlio = new MascheraDisponibilita(this, nome_ag.getText());
    }
    
    void visualInsertPren()
	{
        this.setEnabled(false);
        Stanza stanza = stanze_disp.getStanza(lista.getSelectedIndex()+1);
		if (con_ag.getState())
	        pren = new InserisciPrenotazione(stanza, data1, data2, nome_ag.getText());
		else 
	        pren = new InserisciPrenotazione(stanza, data1, data2, "");
        pren.setVisible(true);
        pren.mask_ricerca = this;
    } 

    void visualInsertComm()
	{
        this.setEnabled(false);
        Stanza stanza = stanze_disp.getStanza(lista.getSelectedIndex()+1);
        comm = new InserisciCommissioni(stanza, data1, data2);
        comm.setVisible(true);
        comm.mask_ricerca = this;
    }
    
    void returnFreeRoom()
	{
        Stanza stanza = stanze_disp.getStanza(lista.getSelectedIndex()+1);
        father.stanza2.setText(stanza.getNumStanza());
        father.setEnabled(true);
        father.stanza1.setEditable(false);
        father.Ricerca.setEnabled(false);
    	father.Conferma.setEnabled(true);
    	dispose();
    }
}
