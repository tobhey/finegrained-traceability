package interfacce;
import java.awt.*;
import java.awt.event.*;

import interfacce.common.utility.Utils;
import moduli.*;

public class CancellaBeniServizi extends MascheraBeneServizio
{
	BeneServizio extra = null;
	Label elenco_extra1, elenco_extra2, desc_extra, px_extra;
	TextField desc, prezzo;
	Button annulla3, annulla_canc, conferma_canc;
		
	public CancellaBeniServizi()
	{
		super("Cancellazione di un bene o servizio");
		setupPannello();
		initialize();
	}
	
	void setupPannello()
	{
		panel[11].setLayout(gridbag);
		elenco_extra1 = new Label("Scegliere il bene o servizio, appartenente");
		elenco_extra2 = new Label("alla categoria selezionata, da cancellare");
		annulla3 = new Button(" Fine ");
		Utils.constrain(panel[11], elenco_extra1, 0, 0, 5, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 0.0, 0.0, 0, 0, 0, 0);
		Utils.constrain(panel[11], elenco_extra2, 0, 1, 5, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 0.0, 0.0, 0, 0, 5, 0);
		Utils.constrain(panel[11], elenco, 0, 2, 5, 1,GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER, 1.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[11], annulla3, 2, 3, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.CENTER, 1.0, 0.0, 5, 5, 5, 5);

		panel[12].setLayout(gridbag);
		desc_extra = new Label("Nome del bene");
		px_extra = new Label("Prezzo del bene");
		desc = new TextField("",40);
		prezzo = new TextField("",10);
		desc.setEditable(false);
		prezzo.setEditable(false);
		annulla_canc = new Button("Annulla");
		conferma_canc = new Button("Conferma");
		Utils.constrain(panel[12], desc_extra, 0, 0, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 0.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[12], desc, 1, 0, 4, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 1.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[12], px_extra, 0, 1, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 0.0, 0.0, 5, 5, 5, 0);
		Utils.constrain(panel[12], prezzo, 1, 1, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 0);
		Utils.constrain(panel[12], annulla_canc, 1, 2, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.EAST, 1.0, 0.0, 5, 5, 0, 5);
		Utils.constrain(panel[12], conferma_canc, 2, 2, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 0, 5);
	}

	public void initialize()
	{
		scelta_piatto[4].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_piatto[4].getState())
				{
					level = 1;
					codice = composeCode(level, BeneServizio.DESSERT);
					p();
				}
			}
		});

		scelta_piatto[5].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_piatto[5].getState())
				{
					level = 1;
					codice = composeCode(level, BeneServizio.FRUTTA);
					p();
				}
			}
		});

		scelta_servizio[0].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_servizio[0].getState())
				{
					level = 1;
					codice = composeCode(level, BeneServizio.RICREATIVI);
					o();
				}
			}
		});

		scelta_servizio[1].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_servizio[1].getState())
				{
					level = 1;
					codice = composeCode(level, BeneServizio.ALTRO);
					o();
				}
			}
		});

		scelta_bevanda[0].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_bevanda[0].getState())
				{
					level = 2;
					codice = composeCode(level, BeneServizio.CAFFETTERIA);
					o();
				}
			}
		});

		scelta_bevanda[1].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_bevanda[1].getState())
				{
					level = 2;
					codice = composeCode(level, BeneServizio.ANALCOLICI);
					o();
				}
			}
		});

		scelta_bevanda[2].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_bevanda[2].getState())
				{
					level = 2;
					codice = composeCode(level, BeneServizio.ALCOLICI);
					o();
				}
			}
		});

		scelta_cibo[0].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_cibo[0].getState())
				{
					level = 2;
					codice = composeCode(level, BeneServizio.DOLCI);
					o();
				}
			}
		});

		scelta_cibo[1].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_cibo[1].getState())
				{
					level = 2;
					codice = composeCode(level, BeneServizio.SALATI);
					o();
				}
			}
		});

		scelta_antipasto[0].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_antipasto[0].getState())
				{
					level = 2;
					codice = composeCode(level, BeneServizio.FREDDI);
					o();
				}
			}
		});

		scelta_antipasto[1].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_antipasto[1].getState())
				{
					level = 2;
					codice = composeCode(level, BeneServizio.CALDI);
					o();
				}
			}
		});

		scelta_primo[0].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_primo[0].getState())
				{
					level = 2;
					codice = composeCode(level, BeneServizio.SOLIDI);
					o();
				}
			}
		});

		scelta_primo[1].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_primo[1].getState())
				{
					level = 2;
					codice = composeCode(level, BeneServizio.LIQUIDI);
					o();
				}
			}
		});

		scelta_secondo[0].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_secondo[0].getState())
				{
					level = 2;
					codice = composeCode(level, BeneServizio.CARNE);
					o();
				}
			}
		});

		scelta_secondo[1].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_secondo[1].getState())
				{
					level = 2;
					codice = composeCode(level, BeneServizio.PESCE);
					o();
				}
			}
		});

		scelta_contorno[0].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_contorno[0].getState())
				{
					level = 2;
					codice = composeCode(level, BeneServizio.VERDURA_COTTA);
					o();
				}
			}
		});

		scelta_contorno[1].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_contorno[1].getState())
				{
					level = 2;
					codice = composeCode(level, BeneServizio.VERDURA_CRUDA);
					o();
				}
			}
		});

		scelta_contorno[2].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_contorno[2].getState())
				{
					level = 2;
					codice = composeCode(level, BeneServizio.FORMAGGIO);
					o();
				}
			}
		});

		conferma_canc.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				startConfermaCanc();
			}
		});

		annulla_canc.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				goBack();
			}
		});

		annulla3.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
				padre.setEnabled(true);
			}
		});

		elenco.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				beneSelezionato();
			}
		});
	} // init

	void startConfermaCanc()
	{
		int id;
		MessageDialog msg;
						
		// questa e' una forzatura sporca
		testo3 = desc;
		testo4 = prezzo;
		int j;
		if ((j = (Principale.db).delBeneServizio(extra.getCodExtra())) == DataBase.OK)
		{
			for(int i=1;i<13;++i)
				if(panel[i].isVisible())
					remove(panel[i]);
			panel[0].setVisible(true);
		}
		else
			msg = new MessageDialog(this, "Problemi con il data base: "+DataBase.strErrore(j));
	}
	
	void beneSelezionato()
	{
		extra = L.getBeneServizio((elenco.getSelectedIndexes())[0]+1);
		remove(panel[11]);
		this.add(panel[12]);
		desc.setText(extra.getDescrizione());
		prezzo.setText(extra.getPxUnitario()+"");
		panel[12].setVisible(true);
		setVisible(true);
	}

	void goBack()
	{
		remove(panel[12]);
		this.add(panel[11]);
		panel[11].setVisible(true);
		setVisible(true);
	}


}