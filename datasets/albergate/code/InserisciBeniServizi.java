package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import moduli.BeneServizio;
import moduli.DataBase;

public class InserisciBeniServizi extends MascheraBeneServizio
{
	BeneServizio extra = null;

	public InserisciBeniServizi(String title)
	{
		super(title);
		initialize();
		setSize(350,520);
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
				startConferma(padre);
			}
		});
	} // init

	void startConferma(Frame padre)
	{
		MessageDialog msg;
		if (!errore())
		{
			Float px = Float.valueOf(testo4.getText());
			extra = new BeneServizio((new String(codice))+tornaCodId((Principale.config).getIdBeneservizio()), testo3.getText(), px.floatValue());
			int j;
			if ( (j = (Principale.db).newIdBeneservizio()) == DataBase.OK )
			{
				(Principale.config).updateIdBeneservizio();
				if ((j = (Principale.db).writeBeneServizio(extra)) == DataBase.OK)
				{
					if (padre instanceof MascheraAddebiti)
					{ // questo serve in quanto l'AggiungiBeneServizio e' una estensione di questa classe
						dispose();
						padre.setEnabled(true);
						((MascheraAddebiti) padre).inizExtra();
					}
					else
					{ // questo serve in quanto AggiungiBeneServizio e' una estensione di questa
					  // classe ed e' utilizzata dalla maschera del conto ristorazione
						if (padre instanceof MascheraContoRistorazione)
						{
							dispose();
							padre.setEnabled(true);
							((MascheraContoRistorazione) padre).inizExtra();
						}
						else
						{
							testo3.setText("");
							testo4.setText("");
							panel[10].setVisible(true);
						}
					}
				}
				else
					msg = new MessageDialog(this, "Problemi con il database: "+DataBase.strErrore(j));
			}
			else
				msg = new MessageDialog(this, "Problemi con il database: "+DataBase.strErrore(j));
		}
	}

	void o()
	{
		completeCode();
		this.add(panel[10]);
		panel[10].setVisible(true);
		testo3.setText("");
		testo4.setText("");
		setVisible(true);
	}
   
	void p()
	{
		completeCode();
		if(panel[1].isVisible())
			remove(panel[1]);
		for(int i=3;i<13;++i)
		{
			if(panel[i].isVisible())
				remove(panel[i]);
		}
		o();
	}
	
	String tornaCodId(long id)
	{
		completeCode();
		String str_id = Long.toString(id);
		while (str_id.length() < 5)
			str_id = new String("0"+str_id);
		return str_id;
	}
}