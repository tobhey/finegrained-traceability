package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import moduli.BeneServizio;
import moduli.DataBase;
import moduli.ListaBeniServizi;

public class ModificaBeniServizi extends MascheraBeneServizio
{
	BeneServizio extra = null;
	Label elenco_extra1, elenco_extra2;
	Button annulla3;
		
	public ModificaBeniServizi()
	{
		super("Modifica dei dati di un bene o servizio");
		setupPannello();
		initialize();
	}
	
	void setupPannello()
	{
		panel[11].setLayout(gridbag);
		elenco_extra1 = new Label("Scegliere il bene o servizio, appartenente");
		elenco_extra2 = new Label("alla categoria selezionata, da modificare");
		elenco = new List(3, false);		
		annulla3 = new Button(" Fine ");
		Utils.constrain(panel[11], elenco_extra1, 0, 0, 5, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 0.0, 0.0, 0, 0, 0, 0);
		Utils.constrain(panel[11], elenco_extra2, 0, 1, 5, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 0.0, 0.0, 0, 0, 5, 0);
		Utils.constrain(panel[11], elenco, 0, 2, 5, 1,GridBagConstraints.HORIZONTAL,
				GridBagConstraints.CENTER, 1.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[11], annulla3, 2, 3, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.CENTER, 1.0, 0.0, 5, 5, 5, 5);
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

		conferma.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				startConferma();
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

	void startConferma()
	{
		int id;
		MessageDialog msg;
		
		if (!errore())
		{
			Float px = Float.valueOf(testo4.getText());
			int j;
			if ((j = (Principale.db).changeBeneServizio(extra.getCodExtra(),
							testo3.getText(), px.floatValue())) == DataBase.OK)
			{
				for(int i=1;i<13;++i)
					if(panel[i].isVisible())
						remove(panel[i]);
				panel[0].setVisible(true);
			}
			else
				msg = new MessageDialog(this, "Problemi con il data base: "+DataBase.strErrore(j));
		}
	}
	
	void beneSelezionato()
	{
		extra = L.getBeneServizio((elenco.getSelectedIndexes())[0]+1);
		remove(panel[11]);
		this.add(panel[10]);
		testo3.setText(extra.getDescrizione());
		testo4.setText(extra.getPxUnitario()+"");
		panel[10].setVisible(true);
		setVisible(true);
	}
}