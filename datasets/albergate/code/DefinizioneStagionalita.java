package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import moduli.*;
import common.def.*;
import common.utility.Utils;
import java.util.Date;

public class DefinizioneStagionalita extends Frame
{
 	//Dichiaro gli oggetti che mi serviranno per costruire la finestra
   	Panel panel1, panel2, panel3, panel4, panel5;
   	Label etichetta1, etichetta2, etichetta3, etichetta, etichetta4;
   	Button  Annulla, Conferma, Aggiungi;
   	TextField data_inizio, data_fine;
   	List list;
   	GridBagLayout gridbag = new GridBagLayout();
	CheckboxGroup checkbox_group;
   	Checkbox[] checkboxes; 

	// variabili locali di supporto
   	char conf_stag_corr[], conf_stag_prox[];
   	Configurazione config_locale;
   	int anno_corr, anno_prox;

	// segue la finestra madre di questa
  	Frame config; 	

    public DefinizioneStagionalita(Frame parent)
    {
        super("Definisione delle stagionalita'");
        config = parent;
        config.setEnabled(false);
        setupPanels();
		initVectors();
        init();
        pack();
        setVisible(true);
    }
                     
	void setupPanels()
	{
		this.setFont(ConfigurazioneSistema.font_base);
		//Creo i TextField
		data_inizio = new TextField("", 12);
		data_fine = new TextField("", 12);

		//Creo i pulsanti e ne disabilito due
		Annulla = new Button("Annulla");
		Conferma = new Button("Conferma");
		Aggiungi = new Button("Aggiungi");
		Aggiungi.setEnabled(false);
			
		//Creo le etichette
		etichetta= new Label("Periodo di soggiorno");
		etichetta.setFont(ConfigurazioneSistema.font_titolo);
		etichetta1 = new Label("Data inizio:");
		etichetta2 = new Label("    Data fine:");
		etichetta3 = new Label ("Prospetto della distriduzione delle staginalita':");
		etichetta3.setFont(new Font("Courier", Font.BOLD, 12));
		Label space = new Label("               ");

		//Creo la lista
		list = new List(15,false);
           
		//Creo il pannello in alto      
		panel1 = new Panel();
		panel1.setLayout(gridbag);
		Utils.constrain(panel1, etichetta, 0, 0, 4, 1, GridBagConstraints.NONE,
			GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 10, 0);
		Utils.constrain(panel1, etichetta1, 0, 1, 1, 1, GridBagConstraints.NONE,
			GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);
		Utils.constrain(panel1, data_inizio, 1, 1, 1, 1, GridBagConstraints.NONE,
			GridBagConstraints.NORTHWEST,1.0,0.0,0,0,0,0);
		//Utils.constrain(panel1, space, 2, 1, 1, 1, GridBagConstraints.NONE,
			//GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);            
		Utils.constrain(panel1, etichetta2, 3, 1, 1, 1, GridBagConstraints.NONE,
			GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);
		Utils.constrain(panel1,data_fine, 4, 1, 1, 1, GridBagConstraints.NONE,
			GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);
		Utils.constrain(panel1, space, 5, 1, 1, 1, GridBagConstraints.NONE,
			GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);                         

		//Creo un pannello con due pulsanti
		panel2 = new Panel();
		panel2.setLayout(gridbag);
		Utils.constrain(panel2, Aggiungi, 0, 0, 1, 1, GridBagConstraints.NONE,
			GridBagConstraints.CENTER, 0.3, 0.0, 0, 0, 0, 10);

		//Creo un Checkbox ad esclusione
		checkbox_group = new CheckboxGroup();
		checkboxes = new Checkbox[5];
		checkboxes[0] = new Checkbox(" Alta stagione",checkbox_group,false);
		checkboxes[1] = new Checkbox(" Media stagione",checkbox_group,false);
		checkboxes[2] = new Checkbox(" Bassa stagione",checkbox_group,false);
		checkboxes[3] = new Checkbox(" Bassissima stagione",checkbox_group,false);
		checkboxes[4] = new Checkbox(" Chiusura",checkbox_group,false);
			
		etichetta4= new Label("Stagionalita' disponibili");
		etichetta4.setFont(new Font("Courier", Font.BOLD, 12));
      		
		//Creo il pannello 
		panel3 = new Panel();
		panel3.setLayout(gridbag);
		Utils.constrain(panel3, etichetta4, 0, 1, 4, 1, GridBagConstraints.NONE,
			GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 10, 0);
		Utils.constrain(panel3, checkboxes[0], 0, 2, 1, 1, GridBagConstraints.NONE,
			GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 5, 0, 0); 
		Utils.constrain(panel3, checkboxes[1], 0, 3, 1, 1, GridBagConstraints.NONE,
			GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 5, 0, 0);
		Utils.constrain(panel3, checkboxes[2], 0, 4, 1, 1, GridBagConstraints.NONE,
			GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 5, 0, 0);
		Utils.constrain(panel3, checkboxes[3], 0, 5, 1, 1, GridBagConstraints.NONE,
			GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 5, 0, 0);
		Utils.constrain(panel3, checkboxes[4], 0, 6, 1, 1, GridBagConstraints.NONE,
			GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 5, 0, 0);                        

		//Creo il pannello contenente la TextArea
		panel4 = new Panel();
		panel4.setLayout(gridbag);
		Utils.constrain(panel4, etichetta3, 0, 0, 2, 1, GridBagConstraints.NONE,
			GridBagConstraints.WEST, 1.0, 0.0, 0, 0, 0, 0);
		Utils.constrain(panel4,list, 0, 1, 4, 3, GridBagConstraints.BOTH,
			GridBagConstraints.CENTER, 3.0, 3.0, 0, 0, 0, 0);

		//Creo il pannello in basso con due pulsanti
		panel5 = new Panel();
		panel5.setLayout(gridbag);
		Utils.constrain(panel5, Annulla, 0, 1, 1, 1, GridBagConstraints.NONE,
			GridBagConstraints.SOUTHEAST, 0.3, 0.0, 0, 0, 0, 10);
		Utils.constrain(panel5, Conferma, 1, 1, 1, 1,GridBagConstraints.NONE,
			GridBagConstraints.SOUTHWEST, 0.3, 0.0, 0, 0, 0, 0);

		//Attacco i pannelli al frame
		this.setLayout(gridbag);
		Utils.constrain(this, panel1, 0, 0, 4, 2, GridBagConstraints.HORIZONTAL,
			GridBagConstraints.NORTHWEST, 1.0, 0.0, 15, 10, 10, 10);
		Utils.constrain(this, panel3, 0, 2, 4, 1,GridBagConstraints.HORIZONTAL,
			GridBagConstraints.NORTHEAST, 1.0, 0.0, 10, 10, 0, 10);
		Utils.constrain(this, panel2, 0, 5, 4, 1,GridBagConstraints.HORIZONTAL,
			GridBagConstraints.NORTHEAST, 1.0, 0.0, 10, 10, 0, 10);
		Utils.constrain(this, panel4, 0, 6, 4, 3, GridBagConstraints.BOTH,
			GridBagConstraints.CENTER, 2.0, 2.0, 10, 10, 10, 10);
		Utils.constrain(this, panel5, 0, 10, 4, 1, GridBagConstraints.HORIZONTAL,
			GridBagConstraints.SOUTHWEST, 1.0, 1.0, 10, 10, 15, 10);
	}

	//Gestione degli eventi
	public void init()
	{
		//Ascoltatore degli eventi della finestra
		checkboxes[0].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if ( (checkboxes[0].getState()) && (!(data_inizio.getText()).equals("")) && (!(data_fine.getText()).equals("")) )
				{
					Aggiungi.setEnabled(true);
				}
			}
		});

		checkboxes[1].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if ( (checkboxes[1].getState()) && (!(data_inizio.getText()).equals("")) && (!(data_fine.getText()).equals("")) )
				{
					Aggiungi.setEnabled(true);
				}
            }
		});

		checkboxes[2].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if ( (checkboxes[2].getState()) && (!(data_inizio.getText()).equals("")) && (!(data_fine.getText()).equals("")) )
				{
					Aggiungi.setEnabled(true);
				}
			}
		});

		checkboxes[3].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if ( (checkboxes[3].getState()) && (!(data_inizio.getText()).equals("")) && (!(data_fine.getText()).equals("")) )
				{
					Aggiungi.setEnabled(true);
				}
			}
		});

		checkboxes[4].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if ( (checkboxes[4].getState()) && (!(data_inizio.getText()).equals("")) && (!(data_fine.getText()).equals("")) )
				{
					Aggiungi.setEnabled(true);
				}
			}
		});
       
		/* Nel caso di annullamento dell'operazione ripristino i vettori
		 * originali della Configurazione
		 */   
            
		Annulla.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
				config.setEnabled(true);
			}
		});
            
		//Ascoltatore del pulsante Conferma, se il pulsante viene premuto si apre
		//la finestra riguardante l'inserimento della prenotazione
		Aggiungi.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				startAggiungi();
            }
        });
        
        Conferma.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
            {
				ConfigurazioneSistema.new_stag_anno_corr = (config_locale.getStagAnnoCorr());
				ConfigurazioneSistema.new_stag_anno_prox = (config_locale.getStagAnnoProx());
				dispose();
				config.setEnabled(true);
			}
		});
    }
	
	void startAggiungi()
	{
		char flag;
		String lista[];
		
		if (Utils.dateEPeriodoCorretti(this, data_inizio.getText(), data_fine.getText()))
		{
			if (checkboxes[0].getState() == true)
				flag = Flag.ALTA_STAGIONE;
			else	
				if (checkboxes[1].getState() == true)
					flag = Flag.MEDIA_STAGIONE;
				else
					if (checkboxes[2].getState() == true)
						flag = Flag.BASSA_STAGIONE;
					else
						if (checkboxes[3].getState() == true)
							flag = Flag.BASSISSIMA_STAGIONE;
						else
							flag = Flag.CHIUSO;
			config_locale.setStagione(new Date(DateUtils.invertDate(DateUtils.parseDate(data_inizio.getText()))), new Date(DateUtils.invertDate(DateUtils.parseDate(data_fine.getText()))), flag);
	        conf_stag_corr = config_locale.getStagionalita(Const.ANNO_CORRENTE);
	        conf_stag_prox = config_locale.getStagionalita(Const.ANNO_PROSSIMO);
			visStagionalita();
			data_inizio.setText("");	
			data_fine.setText("");
			for (int i = 0; i<checkboxes.length; i++)
				checkboxes[i].setState(false);
			Aggiungi.setEnabled(false);
		}
	}
	
    /* 	Creo un duplicato dei vettori delle stagionalita per permettere
     *	l'annullamento delle operazioni
     */
    void initVectors()
    {
        config_locale = new Configurazione();
        config_locale.setStagAnnoCorr(ConfigurazioneSistema.new_stag_anno_corr);
        config_locale.setStagAnnoProx(ConfigurazioneSistema.new_stag_anno_prox);
        conf_stag_corr = config_locale.getStagionalita(Const.ANNO_CORRENTE);
        conf_stag_prox = config_locale.getStagionalita(Const.ANNO_PROSSIMO);
        Costanti cost = new Costanti();
        anno_corr = cost.getAnnoCorr();
        anno_prox = cost.getAnnoProx();
  		visStagionalita();
    }
    
	void visStagionalita()
	{
		String lista[] = reportStagionalita();
		if (list.getItemCount() > 0)
			list.removeAll();
		for (int k = 0; k<lista.length; k++)
			list.addItem(lista[k]);
	}
	
	String[] reportStagionalita()
	{
		String lista_loc[] = new String[30];
		int index = 0;
		String str_aus;
		initLista(lista_loc);
		
	//Scansione dei 5 tipi di stagionalita per l'anno corrente
 		str_aus = Utils.scanVector(Flag.ALTA_STAGIONE, conf_stag_corr, anno_corr);
 		if ( !( str_aus.equals("") ) )
		{	
			lista_loc[index] = new String("Periodi di alta stagione per l'anno "+anno_corr);
			lista_loc[index+1] = new String(str_aus);
			index = index+3;
		}
		str_aus = Utils.scanVector(Flag.MEDIA_STAGIONE, conf_stag_corr, anno_corr);
 		if ( !( str_aus.equals("") ) )
		{	
			lista_loc[index] = new String("Periodi di media stagione per l'anno "+anno_corr);
			lista_loc[index+1] = new String(str_aus);
			index = index+3;
		}
		str_aus = Utils.scanVector(Flag.BASSA_STAGIONE, conf_stag_corr, anno_corr);
 		if ( !( str_aus.equals("") ) )
		{	
			lista_loc[index] = new String("Periodi di bassa stagione per l'anno "+anno_corr);
			lista_loc[index+1] = new String(str_aus);
			index = index+3;
		}
		str_aus = Utils.scanVector(Flag.BASSISSIMA_STAGIONE, conf_stag_corr, anno_corr);
 		if ( !( str_aus.equals("") ) )
		{	
			lista_loc[index] = new String("Periodi di bassissima stagione per l'anno "+anno_corr);
			lista_loc[index+1] = new String(str_aus);
			index = index+3;
		}
		str_aus = Utils.scanVector(Flag.CHIUSO, conf_stag_corr, anno_corr);
 		if ( !( str_aus.equals("") ) )
		{	
			lista_loc[index] = new String("Periodi di chiusura per l'anno "+anno_corr);
			lista_loc[index+1] = new String(str_aus);
			index = index+3;
		}
	
	//	Scansione dei 5 tipi di stagionalita per l'anno seguente 	
		str_aus = Utils.scanVector(Flag.ALTA_STAGIONE, conf_stag_prox, anno_prox);
 		if ( !( str_aus.equals("") ) )
		{	
			lista_loc[index] = new String("Periodi di alta stagione per l'anno "+anno_prox);
			lista_loc[index+1] = new String(str_aus);
			index = index+3;
		}
		str_aus = Utils.scanVector(Flag.MEDIA_STAGIONE, conf_stag_prox, anno_prox);
 		if ( !( str_aus.equals("") ) )
		{	
			lista_loc[index] = new String("Periodi di media stagione per l'anno "+anno_prox);
			lista_loc[index+1] = new String(str_aus);
			index = index+3;
		}
		str_aus = Utils.scanVector(Flag.BASSA_STAGIONE, conf_stag_prox, anno_prox);
 		if ( !( str_aus.equals("") ) )
		{	
			lista_loc[index] = new String("Periodi di bassa stagione per l'anno "+anno_prox);
			lista_loc[index+1] = new String(str_aus);
			index = index+3;
		}
		str_aus = Utils.scanVector(Flag.BASSISSIMA_STAGIONE, conf_stag_prox, anno_prox);
 		if ( !( str_aus.equals("") ) )
		{	
			lista_loc[index] = new String("Periodi di bassissima stagione per l'anno "+anno_prox);
			lista_loc[index+1] = new String(str_aus);
			index = index+3;
		}
		str_aus = Utils.scanVector(Flag.CHIUSO, conf_stag_prox, anno_prox);
 		if ( !( str_aus.equals("") ) )
		{	
			lista_loc[index] = new String("Periodi di chiusura per l'anno "+anno_prox);
			lista_loc[index+1] = new String(str_aus);
			index = index+3;
		}
		return lista_loc;
	}
	
	void initLista(String lista[])
	{
		for (int k = 0; k < lista.length; k++)
			lista[k] = new String("");
	}	
}

             
