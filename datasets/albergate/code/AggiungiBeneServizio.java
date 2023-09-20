package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import moduli.*;


public class AggiungiBeneServizio extends InserisciBeniServizi
{
	Button annulla1, annulla2, conferma1, conferma2;
	TextField tf_supp, tf_rid, tf_stanza_supp, tf_stanza_rid, tf_mot_supp, tf_mot_rid;
	Label label_supp, label_rid, label_stanza_supp, label_stanza_rid, label_mot_supp, label_mot_rid;
	
	public AggiungiBeneServizio(Frame parent)
	{
		super("Creazione di un nuovo bene/servizio/riduzione/supplemento");
		padre = parent;
		padre.setEnabled(false);
		setupNuovi();
		inizializza();
		setSize(450,600);
		setVisible(true);
	}

	void setupNuovi()
	{
		//Creo i pannelli
		remove(panel[0]);
		panel[0] = new Panel();
		panel[0].setLayout(grid1);
		panel[0].setVisible(false);

		//Creo il pannello in alto
		label = new Label("Tipi");
		label.setFont(ConfigurazioneSistema.font_titolo);
		tipi = new CheckboxGroup();
		panel[0].add(label);
		panel[0].add(prima_scelta[0]);
		panel[0].add(prima_scelta[1]);
		panel[0].add(prima_scelta[2]);
		panel[0].add(prima_scelta[3]);
		panel[0].add(prima_scelta[4]);
		panel[0].setVisible(true);

		//Attacco il pannello in alto al frame
		this.setLayout(grid);
		this.add(panel[0]);

		//Creo il pannello supplemento
		panel[11].setLayout(gridbag);
		label_supp = new Label("Ammontare del supplemento "+(Principale.config).getValuta()+".");
		tf_supp = new TextField("", 10);
		label_stanza_supp = new Label("A carico della stanza numero");
		tf_stanza_supp = new TextField("", 6);
		if (padre instanceof MascheraContoRistorazione)
			tf_stanza_supp.setEnabled(false);
		label_mot_supp = new Label("Motivazione");
		tf_mot_supp = new TextField("", 35);
		annulla1 = new Button("Annulla");
		conferma1 = new Button("Conferma");
		Utils.constrain(panel[11], label_supp, 0, 0, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 0.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[11], tf_supp, 1, 0, 4, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 1.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[11], label_stanza_supp, 0, 1, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 0.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[11], tf_stanza_supp, 1, 1, 4, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 1.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[11], label_mot_supp, 0, 2, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 0.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[11], tf_mot_supp, 1, 2, 4, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 1.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[11], annulla1, 1, 3, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.EAST, 0.0, 0.0, 5, 0, 5, 10);
		Utils.constrain(panel[11], conferma1, 3, 3, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 1.0, 0.0, 5, 10, 5, 0);

		//Creo il pannello riduzione
		panel[12].setLayout(gridbag);
		label_rid = new Label("Ammontare della riduzione "+(Principale.config).getValuta()+".");
		tf_rid = new TextField("",10);
		label_stanza_rid = new Label("A carico della stanza numero");
		tf_stanza_rid = new TextField("", 6);
		if (padre instanceof MascheraContoRistorazione)
			tf_stanza_rid.setEnabled(false);
		label_mot_rid = new Label("Motivazione");
		tf_mot_rid = new TextField("", 35);
		annulla2 = new Button("Annulla");
		conferma2 = new Button("Conferma");
		Utils.constrain(panel[12], label_rid, 0, 0, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 0.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[12], tf_rid, 1, 0, 4, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 1.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[12], label_stanza_rid, 0, 1, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 0.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[12], tf_stanza_rid, 1, 1, 4, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 1.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[12], label_mot_rid, 0, 2, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 0.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[12], tf_mot_rid, 1, 2, 4, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 1.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[12], annulla2, 1, 3, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.EAST, 0.0, 0.0, 5, 5, 5, 0);
		Utils.constrain(panel[12], conferma2, 2, 3, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 0);
	}
              
	public void inizializza()
	{
		prima_scelta[3].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(prima_scelta[3].getState())
				{
					inComuneDE(11, BeneServizio.SUPPLEMENTI);
				}
			}
		});

		prima_scelta[4].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(prima_scelta[4].getState())
				{
					inComuneDE(12, BeneServizio.RIDUZIONI);
				}
			}
		});

		annulla1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
				padre.setEnabled(true);
			}
		});

		annulla2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
				padre.setEnabled(true);
			}
		});

		conferma1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				scriviSuDB( tf_supp.getText(), tf_stanza_supp.getText(), tf_mot_supp.getText() );
			}           
		});

		conferma2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				scriviSuDB( new String("-"+tf_rid.getText()), tf_stanza_rid.getText(), tf_mot_rid.getText() );
			}
		});
	} // init

	void inComuneDE(int numero_pannello, char c)
	{
		level = 0;
		for(int i=1;i<13;++i)
		{
			if(panel[i].isVisible())
				remove(panel[i]);
		}
		this.add(panel[numero_pannello]);
		panel[numero_pannello].setVisible(true);
		setVisible(true);
		codice = composeCode(level, c);
	}
	
	void scriviSuDB(String sup_rid, String stanza, String mot)
	{
		MessageDialog msg;
		
		completeCode();
		Float px = Float.valueOf(sup_rid);
		extra = new BeneServizio((new String(codice)) + tornaCodId((Principale.config).getIdBeneservizio()), 
							mot, px.floatValue());
		if (padre instanceof MascheraAddebiti)
		{
			if ( ((Principale.db).readStanza(stanza) != null) && Utils.isFloatPos(sup_rid) && (mot.length() < 33) )
			{
				ListaSoggiornanti L_sogg = (Principale.db).foundSoggiornanti(stanza, false);
				if (L_sogg != null)
				{
					if (!L_sogg.isEmpty())
					{
						int j;
						if ((j = (Principale.db).newIdBeneservizio()) == DataBase.OK)
						{
							(Principale.config).updateIdBeneservizio();
							if ((j = (Principale.db).writeBeneServizio(extra)) == DataBase.OK)
							{
								Addebito da_addebitare = new Addebito(stanza, extra.getCodExtra(), 
																	  1, px.floatValue());
								if ((j = (Principale.db).writeAddebito(da_addebitare)) == DataBase.OK)
								{
									dispose();
									if ( stanza.equals(((MascheraAddebiti) padre).stanza_prec) )
									{
										(((MascheraAddebiti) padre).elenco_addebiti).addAddebito(da_addebitare);
										(((MascheraAddebiti) padre).elenco_extra_addebitati).addBeneServizio(extra);
			
						                /* travaso delle quantita modificate nel nuovo array per poi 
						                   aggiungere il supplemento o riduzione appena inseriti */
				    		            int nuove_quantita_mod[] = new int[((MascheraAddebiti) padre).elenco_addebiti.length()];
		    	        		    
	            					    for (int i = 0; i < ((MascheraAddebiti) padre).quantita_mod.length; i++)
						                	nuove_quantita_mod[i] = ((MascheraAddebiti) padre).quantita_mod[i];
						                nuove_quantita_mod[nuove_quantita_mod.length-1] = 0;
						                ((MascheraAddebiti) padre).quantita_mod = nuove_quantita_mod;
		    			            
										((MascheraAddebiti) padre).continuaAggiornamento();
		
										/* se il numero di stanza era stato cambiato senza addebiti
										   allora rimetto il precedente numero di stanza nel textfield
										   della finestra Addebiti */
									}
									if ( !(((MascheraAddebiti) padre).num_stanza.getText()).equals(((MascheraAddebiti) padre).stanza_prec) )
										((MascheraAddebiti) padre).num_stanza.setText( ((MascheraAddebiti) padre).stanza_prec );
									padre.setEnabled(true);
								}
								else
									msg = new MessageDialog(this, "Errore nell'addebitare: "+DataBase.strErrore(j));
							}
							else
								msg = new MessageDialog(this, "Problemi con il database: "+DataBase.strErrore(j));
						}
						else
							msg = new MessageDialog(this, "Errore nell'aggiornamento: "+DataBase.strErrore(j));
					}
					else
						msg = new MessageDialog(this, " La stanza inserita non e' occupata! ");
				}
				else
					msg = new MessageDialog(this, " Problemi con il database! ");
			}
			else
				msg = new MessageDialog(this, "Errore nei parametri!");
		}
		else
		{ // si tratta della maschera del conto ristorazione
			dispose();
			Addebito da_addebitare = new Addebito("RIST", extra.getCodExtra(),
												  1, px.floatValue());
			(((MascheraContoRistorazione) padre).elenco_addebiti).addAddebito(da_addebitare);
			(((MascheraContoRistorazione) padre).elenco_extra_addebitati).addBeneServizio(extra);

            /* travaso delle quantita modificate nel nuovo array per poi 
               aggiungere il supplemento o riduzione appena inseriti */

			int nuove_quantita_mod[] = new int[((MascheraContoRistorazione) padre).elenco_addebiti.length()];
			for (int i = 0; i < ((MascheraContoRistorazione) padre).quantita_mod.length; i++)
				nuove_quantita_mod[i] = ((MascheraContoRistorazione) padre).quantita_mod[i];
            nuove_quantita_mod[nuove_quantita_mod.length-1] = 1;
            ((MascheraContoRistorazione) padre).quantita_mod = nuove_quantita_mod;
            
			((MascheraContoRistorazione) padre).continuaAggiornamento();
			padre.setEnabled(true);
		}
	}
}