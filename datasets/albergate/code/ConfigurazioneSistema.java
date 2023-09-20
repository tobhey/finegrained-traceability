package interfacce;
import java.awt.*;
import java.awt.event.*;
import java.lang.String;
import common.utility.*;
import common.def.*;
import moduli.*;

public class ConfigurazioneSistema extends Frame
{
	// font costanti per uniformare l'aspetto delle finestre
	public static final Font font_base = new Font("TimesRoman", Font.PLAIN, 12);
	public static final Font font_sub = new Font("Courier", Font.BOLD, 18);
	public static final Font font_titolo = new Font("Courier", Font.BOLD, 12);
	public static final Font font_allineato = new Font("Courier", Font.PLAIN, 12);

	// componenti della finestra di configurazione
    TextField giorni_blocco, sup_bassissima, sup_bassa, sup_media, sup_alta,sup_letto_agg,
             costo_x_scatto, sup_neonato, rid_bambino, rid_ragazzo, rid_comitiva,
             password, num_max_stanze, px_colazione, px_mezza, px_intera, coperto,
             min_pers_comitiva, cambio, albergo, indirizzo_alb, comune_alb, 
             citta_alb, rag_soc, licenza;
    Label cambio_L_E;
    Label etichetta, etich1, etich2, etich3, etich4, etich5, etich6, etich7, etich8,
         etich9, etich10, etich11 ,etich11_1, etich12, etich13, etich14, etich15, etich16, etich17,
         etich18, etich19, etich20, etichetta1, etichetta2, etichetta3,etichetta4,
         etichetta5, etich21, etich22, etichetta6, etich23,etich24,etich25, etichetta7;
    Panel panel0, panel1, panel2, panel3, panel4;
    GridBagLayout gridbag = new GridBagLayout();
    CheckboxGroup checkbox;
    Checkbox[] checkboxes;
    Button Annulla, Passwd, Conferma, Stagioni;
    DefinizioneStagionalita def_stag;

	// per il cambio di password e stagionalita
	static String new_passwd;
	static char[] new_stag_anno_corr, new_stag_anno_prox;
   
	// per la gerarchia delle finestre
	SubGestione padre = new SubGestione();

	// per fregare l'ascoltatore
	static Configurazione this_config;
	DataBase db;
	
	boolean first_start;
   
    public ConfigurazioneSistema(Configurazione config, boolean prima_volta, DataBase archivio)
    {
      super("Configurazione di sistema");
      this_config = config;
      db = archivio;
      new_passwd = this_config.getPassword();
      new_stag_anno_corr = this_config.getStagionalita(Const.ANNO_CORRENTE);
      new_stag_anno_prox = this_config.getStagionalita(Const.ANNO_PROSSIMO);
      first_start = prima_volta;
      setupPanels(prima_volta);
      init();
      pack();
    }

  	public  void setupPanels(boolean prima_volta)
   	{
   	  this.setFont(font_base);
      //creo le etichette
      cambio_L_E = new Label("Cambio Lira <-> EURO");
      etichetta = new Label("Licenza:");
      etichetta.setFont(new Font("Courier", Font.BOLD, 12));
      etichetta1 = new Label("DATI DI CONFIGURAZIONE");
      etichetta1.setFont(new Font("Courier", Font.BOLD, 12));
      etichetta2 = new Label("Supplementi dovuto alle stagionionalita'");
      etichetta2.setFont(new Font("Courier", Font.BOLD, 12));
      etichetta3 = new Label("Riduzioni");
      etichetta3.setFont(new Font("Courier", Font.BOLD, 12));
      etichetta4 = new Label("Ristorazione");
      etichetta4.setFont(new Font("Courier", Font.BOLD, 12));
      etichetta5 = new Label("Valuta");
      etichetta5.setFont(new Font("Courier", Font.BOLD, 12));
      etichetta6 = new Label("Cambio di password utente");
      etichetta6.setFont(new Font("Courier", Font.BOLD, 12));
      etichetta7 = new Label("Definizione delle stagionalita'");
      etichetta7.setFont(new Font("Courier", Font.BOLD, 12));
      
      licenza = new TextField("",35);
      albergo = new TextField("",25);
      comune_alb =new TextField("",35);
      citta_alb = new TextField("",25);
      indirizzo_alb = new TextField("",35);
      rag_soc = new TextField("",35);
      giorni_blocco = new TextField("",4);
      sup_bassissima = new TextField("",9);
      sup_bassa = new TextField("",9);
      sup_media = new TextField("",9);
      sup_alta = new TextField("",9);
      sup_neonato = new TextField("",9);
      sup_letto_agg = new TextField("",9);
      rid_bambino = new TextField("",4);
      rid_ragazzo = new TextField("",4);
      rid_comitiva = new TextField("",4);
      min_pers_comitiva = new TextField("",4);
      num_max_stanze = new TextField("",4);
      num_max_stanze.setEditable(false);
      px_colazione = new TextField("",9);
      px_mezza = new TextField("",9);
      px_intera = new TextField("",9);
      coperto = new  TextField("",9);
      costo_x_scatto = new TextField("",9);
      password = new TextField("",20);
      cambio = new TextField("",8);

      //creo un Checkbox
      checkbox = new CheckboxGroup();
      checkboxes = new Checkbox[2];
      checkboxes[0] = new Checkbox(" Lire",checkbox,true);
      checkboxes[1] = new Checkbox(" EURO",checkbox,false);
	  writeDatiConf();
	  
	  if (prima_volta)
			licenza.setEditable(true);
	  else
			licenza.setEditable(false);

      licenza.setForeground(Color.red.darker());
      albergo.setForeground(Color.blue);
      comune_alb.setForeground(Color.blue);
      citta_alb.setForeground(Color.blue);
      rag_soc.setForeground(Color.blue);
      password.setEchoChar('*');

      etich1 = new Label("Dati dell'albergo");
      etich1.setFont(new Font("Courier", Font.BOLD, 12));
      etich2 = new Label("Nome dell'albergo:");
      etich3 = new Label("Indirizzo dell'albergo:");
      indirizzo_alb.setForeground(Color.blue);
      etich4 = new Label("Comune dell'albergo:");
      etich5 = new Label("Citta' dell'albergo:");
      etich6 = new Label("Numero di giorni massimo di bloccaggio di una stanza");
      etich7 = new Label("Supplemento bassisima stagione");
      etich8 = new Label("Supplemento bassa stagione");
      etich9 = new Label("Supplemento media stagione");
      etich10 = new Label("Supplemento alta stagione");
      etich11 = new Label("Supplemento per neonati");
      etich11_1 = new Label("Supplemento letto aggiuntivo");
      etich12 = new Label("Riduzione per bambini %");
      etich13 = new Label("Riduzione per ragazzi %");
      etich14 = new Label("Riduzione per comitive %");
      etich15 = new Label("Numero minimo di persone per comitiva");
      etich16 = new Label("Numero di stanze");
      etich17 = new Label("Supplemento prima colazione");
      etich18 = new Label("Supplemento mezza pensione");
      etich19 = new Label("Supplemento pensione completa");
      etich20 = new Label("Coperto per la ristorazione");
      etich21 = new Label("Costo di uno scatto telefonico");
      etich22 = new Label("Password utente");
      etich23 = new Label("Lire");
      etich24 = new Label("Dollari");
      etich25 = new Label("Ragione sociale");

      //creo i pulsanti
      Annulla = new Button("Annulla");
      Passwd = new Button("Cambio di password");
      Conferma = new Button("Conferma");
      Stagioni = new Button("Definisci Stagionalita'");

      panel0 = new Panel();
      panel0.setLayout(gridbag);
      Utils.constrain(panel0, etichetta, 0, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0); 
      Utils.constrain(panel0, licenza, 1, 0, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0); 
      Utils.constrain(panel0, etich1, 0, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);
      Utils.constrain(panel0, etich2, 0, 2, 1, 1, GridBagConstraints.NONE,
                    GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel0, albergo, 1, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel0, etich3, 2, 2, 1, 1, GridBagConstraints.NONE,
                    GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel0, indirizzo_alb, 3, 2, 1, 1, GridBagConstraints.NONE,
                    GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel0, etich4, 0, 3, 1, 1, GridBagConstraints.NONE,
                    GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel0, comune_alb, 1, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel0, etich5, 2, 3, 1, 1, GridBagConstraints.NONE,
                    GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel0, citta_alb, 3, 3, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel0, etich25, 0, 4, 1, 1, GridBagConstraints.NONE,
                    GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel0, rag_soc, 1, 4, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);

      panel1 = new Panel();
      panel1.setLayout(gridbag);
      Utils.constrain(panel1, etichetta1, 0, 0, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 5, 5, 5); 
      Utils.constrain(panel1, etich6, 0, 1, 5, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 5, 5, 5); 
      Utils.constrain(panel1, giorni_blocco, 5, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 5, 5, 5); 
      Utils.constrain(panel1, etich21, 7, 1, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 5, 5, 5); 
      Utils.constrain(panel1, costo_x_scatto, 10, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 5, 5, 5); 
      Utils.constrain(panel1, etichetta2, 0, 2, 4, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, etichetta3, 5, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, etichetta4, 8, 2, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
	// supplementi
      Utils.constrain(panel1, etich7, 0, 3, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, sup_bassissima, 3, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, etich8, 0, 4, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, sup_bassa, 3, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, etich9, 0, 5, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, sup_media, 3, 5, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, etich10, 0, 6, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, sup_alta, 3, 6, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, etich11, 0, 7, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, sup_neonato, 3, 7, 1, 1, GridBagConstraints.NONE,
                       GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, etich11_1, 0, 8, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, sup_letto_agg, 3, 8, 1, 1, GridBagConstraints.NONE,
                       GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);                 
	
	// riduzioni
      Utils.constrain(panel1, etich12, 4, 3, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, rid_bambino, 7, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, etich13, 4, 4, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, rid_ragazzo, 7, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, etich14, 4, 5, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, rid_comitiva, 7, 5, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, etich15, 4, 6, 3, 1, GridBagConstraints.NONE,
                       GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, min_pers_comitiva, 7, 6, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 

	// ristorazione
      Utils.constrain(panel1, etich17, 8, 3, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, px_colazione, 11, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, etich18, 8, 4, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, px_mezza, 11, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, etich19, 8, 5, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, px_intera, 11, 5, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, etich20, 8, 6, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, coperto, 11, 6, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, cambio_L_E, 5, 7, 3, 1, GridBagConstraints.NONE,
                       GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, cambio, 7, 7, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, etich16, 9, 7, 4, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, num_max_stanze, 11, 7, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 

      panel2 = new Panel();
      panel2.setLayout(gridbag);
      Utils.constrain(panel2, etichetta5, 0, 0, 1, 1, GridBagConstraints.NONE,
                       GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel2, checkboxes[0], 0, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel2, checkboxes[1], 0, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 

      panel3 = new Panel();
      panel3.setLayout(gridbag);
      Utils.constrain(panel3, etichetta6, 0, 0, 1, 1, GridBagConstraints.NONE,
                       GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel3, Passwd, 1, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel3, etichetta7, 0, 1, 1, 1, GridBagConstraints.NONE,
                       GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel3, Stagioni, 1, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);                   
      
      
      
      panel4 = new Panel();
      panel4.setLayout(gridbag);
      Utils.constrain(panel4, Annulla, 8, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTH, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel4, Conferma, 9, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTH, 0.0, 0.0, 5, 5, 5, 5); 

      this.setLayout(gridbag);
      Utils.constrain(this,panel0, 0, 0, 12, 6, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(this,panel1, 0, 6, 12, 9, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(this,panel2, 0, 15, 1, 3, GridBagConstraints.HORIZONTAL,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(this,panel3, 1, 15, 11, 2, GridBagConstraints.HORIZONTAL,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(this,panel4, 0, 19, 12, 1, GridBagConstraints.HORIZONTAL,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
   }
   
   public void init()
   {
      Annulla.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            dispose();
			if (!first_start)
	            padre.setEnabled(true);
	        else
	        	System.exit(0);
         }
      });
      
      Conferma.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
			startConferma();
		 }
      });

      checkboxes[0].addItemListener(new ItemListener()   
      {
         public void itemStateChanged(ItemEvent e)
         {
            if(checkboxes[0].getState())
            {
            	startAvviso(Const.LIRE);
            }
         }
      });
                      
      checkboxes[1].addItemListener(new ItemListener()   
      {
         public void itemStateChanged(ItemEvent e)
         {
            if(checkboxes[1].getState())
            {
            	startAvviso(Const.EURO);	
            }
         }
      });
      
      
      Passwd.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            startChangePwd();
         }
      });
      
      Stagioni.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            setStagioni();
         }
      });
   }
	
	void startAvviso(char nuova_valuta)
	{
		Frame msg;
		if (nuova_valuta != this_config.getValuta() && !first_start)
		{
			msg = new AvvisoDialog(this, "Attenzione: il cambio LIRA<->EURO effettuato alla conferma modifichera' le cifre della finestra corrente!");
		}
	}
	
    void startChangePwd()
    {
      this.setEnabled(false);
      CambioPassword cambio = new CambioPassword();
      cambio.setVisible(true);
      cambio.conf = this;
    }
 
 	void startConferma()
	{
			MessageDialog msg;
			int j;
				
			if (!errore())
			{
				char vecchia_valuta = this_config.getValuta();
				readDatiConf();
				this_config.setPassword(new_passwd);
				this_config.setStagionalita(new_stag_anno_corr, new_stag_anno_prox);
				if ( (j = (db.writeConfigurazione(this_config))) != DataBase.OK ) 
				{
					msg = new MessageDialog(this, "Problemi con il database: "+DataBase.strErrore(j));
				}
				else
				{
					if ( vecchia_valuta != this_config.getValuta() && !first_start)
					{
						j = (Principale.db).startCambioValuta();
						if (j != DataBase.OK)
						{
							msg = new MessageDialog(this, "Errore con il database: "+DataBase.strErrore(j));
							return;
						}
						this_config = (Principale.db).readConfigurazione();
						Principale.config = this_config;
					}
					if (!(first_start))
					{
						padre.pass.p.setTitle(this_config.getNomeAlbergo());
						padre.setEnabled(true);
					}
					else
					{
						Frame p = new Principale(this_config, db);
						p.setVisible(true);
					}						
					dispose();
				}
			}
	}
	
	void setStagioni()
	{
      def_stag = new DefinizioneStagionalita(this);
	}	
	
	boolean errore()
	{
		Frame msg;
		
		if ( !((licenza.getText()).length() < 33 ) )
		{
			msg = new MessageDialog(this, " La licenza deve essere composta da al piu' 32 caratteri! ");
			return true;
		}
		if ( !((albergo.getText()).length() < 21 ))
		{
			msg = new MessageDialog(this, " Il nome dell'albergo deve essere composto da al piu' 20 caratteri! ");
			return true;
		}
      	if ( !((comune_alb.getText()).length() < 33 ) )
		{
			msg = new MessageDialog(this, " Il comune deve essere composto da al piu' 32 caratteri! ");
			return true;
		}
		if ( !((indirizzo_alb.getText()).length() < 33 ) )
		{
			msg = new MessageDialog(this, " L'indirizzo deve essere composto da al piu' 32 caratteri! ");
			return true;
		}
		if ( !((citta_alb.getText()).length() < 21 ) )
		{
			msg = new MessageDialog(this, " La citta' deve essere composta da al piu' 20 caratteri! ");
			return true;
		}
		if ( !((rag_soc.getText()).length() < 33 ) )
		{
			msg = new MessageDialog(this, " La ragione sociale deve essere composta da al piu' 32 caratteri! ");
			return true;
		}
		if ( !Utils.isIntPos(giorni_blocco.getText()) )
		{
			msg = new MessageDialog(this, " Il numero di giorni di blocco deve essere un numero intero positivo! ");
			return true;
		}
		if ( !Utils.isFloatPos(sup_bassissima.getText()) ||
			 !Utils.isFloatPos(sup_bassa.getText()) ||
			 !Utils.isFloatPos(sup_media.getText()) ||
			 !Utils.isFloatPos(sup_alta.getText()) ||
			 !Utils.isFloatPos(sup_neonato.getText()) ||
			 !Utils.isFloatPos(sup_letto_agg.getText()) ||
			 !Utils.isFloatPos(px_colazione.getText()) ||
			 !Utils.isFloatPos(px_mezza.getText()) ||
			 !Utils.isFloatPos(px_intera.getText()) )
		{
			msg = new MessageDialog(this, " I supplementi devono essere numeri positivi! ");
			return true;
		}
		if ( !Utils.isIntPos(rid_bambino.getText()) ||
			 !Utils.isIntPos(rid_ragazzo.getText()) ||
			 !Utils.isIntPos(rid_comitiva.getText()) )
		{
			msg = new MessageDialog(this, " deve essere un numero intero! ");
			return true;
		}
		if ( !Utils.isIntPos(min_pers_comitiva.getText()) )
		{
			msg = new MessageDialog(this, " Il numero di persone comitiva deve essere un numero intero positivo! ");
			return true;
		}
		if ( !Utils.isIntPos(num_max_stanze.getText()) )
		{
			msg = new MessageDialog(this, " Il numero di stanze deve essere un numero intero positivo! ");
			return true;
		}
		if ( !Utils.isFloatPos(coperto.getText()) )
		{
			msg = new MessageDialog(this, " Il coperto deve essere un numero positivo! ");
			return true;
		}
		if ( !Utils.isFloatPos(costo_x_scatto.getText()) )
		{
			msg = new MessageDialog(this, " Il costo dello scatto telefonico deve essere un numero positivo! ");
			return true;
		}
		if	( !Utils.isFloatPos(cambio.getText()) )
		{
			msg = new MessageDialog(this, " Il coefficiente di cambio LIRE<->EURO deve essere un numero positivo e diverso da zero! ");
			return true;
		}
		else
			if ( !((Float.valueOf(cambio.getText())).floatValue() > 0) )
			{
				msg = new MessageDialog(this, " Il coefficiente di cambio LIRE<->EURO deve essere un numero positivo e diverso da zero! ");
				return true;
			}
		return false;
	}
	
	void readDatiConf()
	{
		this_config.setLicenza(licenza.getText());
		this_config.setNomeAlbergo(albergo.getText());
      	this_config.setComuneAlb(comune_alb.getText());
      	this_config.setIndirizzoAlb(indirizzo_alb.getText());
      	this_config.setCittaAlb(citta_alb.getText());
      	this_config.setRagSoc(rag_soc.getText());
		this_config.setGiorniBlocco(Integer.parseInt(giorni_blocco.getText()));
      	this_config.setSupplemento((Float.valueOf(sup_bassissima.getText())).floatValue(),Flag.BASSISSIMA_STAGIONE);
      	this_config.setSupplemento((Float.valueOf(sup_bassa.getText())).floatValue(),Flag.BASSA_STAGIONE);
      	this_config.setSupplemento((Float.valueOf(sup_media.getText())).floatValue(),Flag.MEDIA_STAGIONE);
      	this_config.setSupplemento((Float.valueOf(sup_alta.getText())).floatValue(),Flag.ALTA_STAGIONE);
      	this_config.setSupNeonato((Float.valueOf(sup_neonato.getText())).floatValue());
      	this_config.setSupLettoAgg((Float.valueOf(sup_letto_agg.getText())).floatValue());
      	this_config.setRiduzione(Integer.parseInt(rid_bambino.getText()),Flag.BAMBINO);
      	this_config.setRiduzione(Integer.parseInt(rid_ragazzo.getText()),Flag.RAGAZZO);
      	this_config.setRiduzione(Integer.parseInt(rid_comitiva.getText()),Flag.COMITIVA);
      	this_config.setMinPersComit(Integer.parseInt(min_pers_comitiva.getText()));
      	this_config.setNumStanze(Integer.parseInt(num_max_stanze.getText()));
      	this_config.setPxColazione((Float.valueOf(px_colazione.getText())).floatValue());
      	this_config.setPxMezza((Float.valueOf(px_mezza.getText())).floatValue());
      	this_config.setPxIntera((Float.valueOf(px_intera.getText())).floatValue());	
      	this_config.setCoperto((Float.valueOf(coperto.getText())).floatValue());
      	this_config.setCostoXScatto((Float.valueOf(costo_x_scatto.getText())).floatValue());
      	this_config.setCoeffXCambio((Float.valueOf(cambio.getText())).floatValue());
      	if (checkboxes[0].getState())
      		this_config.setValuta(Const.LIRE);
      	else
      		this_config.setValuta(Const.EURO);
	}
	
	void writeDatiConf()
	{
		//String s = new String(Principale.simbol);
		
		licenza.setText(""+this_config.getLicenza());
		albergo.setText(""+this_config.getNomeAlbergo());
      	comune_alb.setText(""+this_config.getComuneAlb());
      	indirizzo_alb.setText(""+this_config.getIndirizzoAlb());
      	citta_alb.setText(""+this_config.getCittaAlb());
      	rag_soc.setText(""+this_config.getRagSoc());
		giorni_blocco.setText(""+this_config.getGiorniBlocco());
      	sup_bassissima.setText(""+this_config.getSupplemento(Flag.BASSISSIMA_STAGIONE));
      	sup_bassa.setText(""+this_config.getSupplemento(Flag.BASSA_STAGIONE));
      	sup_media.setText(""+this_config.getSupplemento(Flag.MEDIA_STAGIONE));
      	sup_alta.setText(""+this_config.getSupplemento(Flag.ALTA_STAGIONE));
      	sup_neonato.setText(""+this_config.getSupNeonato());
      	sup_letto_agg.setText(""+this_config.getSupLettoAgg());
      	rid_bambino.setText(""+this_config.getRiduzione(Flag.BAMBINO));
      	rid_ragazzo.setText(""+this_config.getRiduzione(Flag.RAGAZZO));
      	rid_comitiva.setText(""+this_config.getRiduzione(Flag.COMITIVA));
      	min_pers_comitiva.setText(""+this_config.getMinPersComit());
      	num_max_stanze.setText(""+this_config.getNumStanze());
      	px_colazione.setText(""+this_config.getPxColazione());
      	px_mezza.setText(""+this_config.getPxMezza());
      	px_intera.setText(""+this_config.getPxIntera());	
      	coperto.setText(""+this_config.getCoperto());
      	costo_x_scatto.setText(""+this_config.getCostoXScatto());
      	cambio.setText(""+this_config.getCoeffXCambio());
		if (this_config.getValuta() == Const.LIRE)
			checkbox.setSelectedCheckbox(checkboxes[0]);
		else
			checkbox.setSelectedCheckbox(checkboxes[1]);
	}
}

/****************************************************************************/
class CambioPassword extends Frame
{
   TextField testo1, testo2, testo3;
   Label label1, label2, label3;
   GridBagLayout grid = new GridBagLayout();
   Panel pannello, pannello_x_annulla;
   Frame conf = new Frame();
   Button conferma, cambio, annulla;
   MessageDialog dialog;
   
   public CambioPassword()
   {
      super("Cambio di password");
      setupPanels();
      init();
      pack();
   }

   void setupPanels()
   {
   	  this.setFont(ConfigurazioneSistema.font_base);
      //creo le etichette
      label1 = new Label("Inserire vecchia password");
      label2 = new Label("Inserire nuova password");
      label3 = new Label("Reinserire la nuova password");

      //creo i textField
      testo1 = new TextField("",20);
      testo2 = new TextField("",20);
      testo1.setEchoChar('*');
      testo2.setEchoChar('*');
      testo2.setEditable(false);

      conferma = new Button("Conferma");
      cambio = new Button("Cambio di password");
      cambio.setEnabled(false);
	  annulla = new Button("Annulla");
      pannello = new Panel();
      pannello.setLayout(grid);
      Utils.constrain(pannello, label1, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(pannello, testo1, 2, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(pannello, label2, 0, 1, 2, 1, GridBagConstraints.NONE,
                       GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(pannello, testo2, 2, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(pannello, conferma, 3, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(pannello, cambio, 3, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
                        
	  pannello_x_annulla = new Panel();
	  pannello_x_annulla.setLayout(grid);
      Utils.constrain(pannello_x_annulla, annulla, 0, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 1.0, 0.0, 5, 5, 5, 5); 

      this.setLayout(grid);
      Utils.constrain(this,pannello, 0, 0, 4, 2, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(this,pannello_x_annulla, 0, 2, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.CENTER, 1.0, 0.0, 5, 5, 5, 5);
  }

  void init()
  {
      annulla.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            dispose();
            conf.setEnabled(true);
         }
      });

      conferma.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            g();
         }
      });

      testo1.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            g();
         }
      });

      cambio.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            i();
         }
      });

      testo2.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            i();
         }
      });
   }

   void g()
   {
		String tmp = testo1.getText();
		if(tmp.equals(ConfigurazioneSistema.new_passwd))
		{
			testo2.setEditable(true);
			testo1.setEditable(false);
			cambio.setEnabled(true);
			conferma.setEnabled(false);
		}
		else
		{
			dialog = new MessageDialog(this,"La password non e' corretta");
			dialog.setVisible(true);
			testo1.setText("");
		}
	}

	void i()
	{
        String tmp = testo2.getText();
		Messaggio messaggio1 = new Messaggio(tmp);
		messaggio1.setVisible(true);
		messaggio1.c = this;
		this.setEnabled(false);
	}
}

/****************************************************************************/
class Messaggio extends Frame
{
   Label msg;
   Button OK;
   CambioPassword c = new CambioPassword();
   Avviso dialog;
   GridBagLayout gridbag = new GridBagLayout();
   TextField testo3;
   Panel pannello;
   String pwd_passata;
    
   public Messaggio(String text)
   {
      super("Messaggio per il cambio di password");
      setup();
      pwd_passata = text;
      init();
      pack(); 
   }

   void setup()
   {
	  this.setFont(ConfigurazioneSistema.font_base);
      //creo un pulsante
      OK = new Button("OK");

      //creo un'etichetta
      msg = new Label("Reinserire la nuova password");

      //creo un textField
      testo3 = new TextField("",20);
      testo3.setEchoChar('*');

      pannello = new Panel();
      pannello.setLayout(gridbag);
      Utils.constrain(pannello, msg, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(pannello, testo3, 2, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(pannello, OK, 4, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 

      this.setLayout(gridbag);
      Utils.constrain(this, pannello, 0, 0, 4, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
   }

   public void init()
   {
      OK.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
         	startOK();
		 }
      });

      testo3.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
         	startOK();
         }   
      });
   }

	void startOK()
	{
           if((testo3.getText()).equals(pwd_passata))
           {
               ConfigurazioneSistema.new_passwd = testo3.getText();
		       dialog = new Avviso(this,"La password verra' cambiata alla conferma");
           }
           else
		       dialog = new Avviso(this,"La nuova password non e' stata accettata");
      	   dialog.setVisible(true);
	}
}

/****************************************************************************/
class Avviso extends Frame
{
   Button OK;
   Messaggio padre;

   public Avviso(Messaggio parent, String testo)
   {
      super("Attenzione");
      padre = parent;
      padre.setEnabled(false);
      
	  this.setFont(ConfigurazioneSistema.font_titolo);
      Panel textPanel = new Panel();
      Panel buttonPanel = new Panel();
      textPanel.add(new Label(testo));
      OK = new Button("OK");
      buttonPanel.add(OK);
      add("North",textPanel);
      add("Center",buttonPanel);
      init();
      pack();
      setVisible(true);
   }

   public void init()
   {
      OK.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
         	esci();
         }
      });
   }
	
	void esci()
	{
		dispose();
		padre.dispose();
		padre.c.dispose();
		padre.c.conf.setEnabled(true);
	}
}
