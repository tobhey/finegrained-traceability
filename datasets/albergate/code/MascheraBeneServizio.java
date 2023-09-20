package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import moduli.BeneServizio;
import moduli.ListaBeniServizi;

public class MascheraBeneServizio extends Frame
{
	//Dichiarazioni di oggetti che ci serviranno per definire la finestra
	protected Label label, label1, label2, label3, label4, label5, label6, label7, label8,
		label9, label12, label13;
	protected TextField testo3, testo4;
	protected Panel panel[], buttons;
	protected Button annulla, conferma;
	protected CheckboxGroup tipi, bar, ristorante, servizi, bevande, cibi, antipasti,
				primi, secondi, contorni;
	protected Checkbox[] prima_scelta, scelta_bar, scelta_piatto, scelta_servizio, 
				scelta_bevanda, scelta_cibo, scelta_antipasto, scelta_primo, 
				scelta_secondo,	scelta_contorno;
	protected GridLayout grid = new GridLayout(4,1);
	protected GridLayout grid1 = new GridLayout(7,1);
	protected GridBagLayout gridbag = new GridBagLayout();

	// i seguenti due campi sono condivisi da cancellazione e modifica
	protected List elenco = new List(3, false); 
	protected ListaBeniServizi L;
	
	// il seguente attributo e' necessario per la gerarchia delle finestre
	Frame padre = new Frame();
	
	// i seguenti gestiscono la formazione dei primi tre caratteri del cod_extra
	int level = -1;
	char codice[] = new char[3];
 	 	  
	public MascheraBeneServizio(String title)
	{
		super(title);
		setup();
		init();
		setSize(350,600);
	}

	void setup()
	{
		this.setFont(ConfigurazioneSistema.font_base);
		elenco.setFont(ConfigurazioneSistema.font_allineato);
		//Creo i pannelli
		panel = new Panel[13];
		for(int i=0;i<10;++i)
		{
			panel[i] = new Panel();
			panel[i].setLayout(grid1);
			panel[i].setVisible(false);
		}
		for(int i=10; i<13; ++i)
		{
			panel[i] = new Panel();
			panel[i].setVisible(false);
		}

		//Creo il pannello in alto
		label = new Label("Tipi");
		label.setFont(ConfigurazioneSistema.font_titolo);
		tipi = new CheckboxGroup();
		prima_scelta = new Checkbox[5]; // mi serve piu' lungo per l'aggiungi
		prima_scelta[0] = new Checkbox("BAR", tipi, false);
		prima_scelta[1] = new Checkbox("RISTORANTE", tipi, false);
		prima_scelta[2] = new Checkbox("SERVIZI", tipi, false);
		prima_scelta[3] = new Checkbox("SUPPLEMENTI", tipi, false);
		prima_scelta[4] = new Checkbox("RIDUZIONI", tipi, false);
		panel[0].add(label);
		panel[0].add(prima_scelta[0]);
		panel[0].add(prima_scelta[1]);
		panel[0].add(prima_scelta[2]);
		panel[0].setVisible(true);

		//Attacco il pannello in alto al frame
		this.setLayout(grid);
		this.add(panel[0]);

		//Creo il pannello del bar
		label1 = new Label("Bar");
		label1.setFont(ConfigurazioneSistema.font_titolo);
		bar = new CheckboxGroup();
		scelta_bar = new Checkbox[2];
		scelta_bar[0] = new Checkbox("Bevande", bar, false);
		scelta_bar[1] = new Checkbox("Cibi", bar, false);
		panel[1].add(label1);
		panel[1].add(scelta_bar[0]);
		panel[1].add(scelta_bar[1]);

		//Creo il pannello dal ristorante
		label2 = new Label("Ristorante");
		label2.setFont(ConfigurazioneSistema.font_titolo);
		ristorante = new CheckboxGroup();
		scelta_piatto = new Checkbox[6];
		scelta_piatto[0] = new Checkbox("Antipasti", ristorante, false);
		scelta_piatto[1] = new Checkbox("Primi", ristorante, false);
		scelta_piatto[2] = new Checkbox("Secondi", ristorante, false);
		scelta_piatto[3] = new Checkbox("Contorni", ristorante, false);
		scelta_piatto[4] = new Checkbox("Dessert", ristorante, false);
		scelta_piatto[5] = new Checkbox("Frutta", ristorante, false);
		panel[2].add(label2);
		panel[2].add(scelta_piatto[0]);
		panel[2].add(scelta_piatto[1]);
		panel[2].add(scelta_piatto[2]);
		panel[2].add(scelta_piatto[3]);
		panel[2].add(scelta_piatto[4]);
		panel[2].add(scelta_piatto[5]);
		label3 = new Label("Servizi");
		label3.setFont(ConfigurazioneSistema.font_titolo);
		servizi = new CheckboxGroup();
		scelta_servizio = new Checkbox[2];
		scelta_servizio[0] = new Checkbox("Ricreativi", servizi, false);
		scelta_servizio[1] = new Checkbox("Altro", servizi, false);
		panel[3].add(label3);
		panel[3].add(scelta_servizio[0]);
		panel[3].add(scelta_servizio[1]);
		label4 = new Label("Bevande");
		label4.setFont(ConfigurazioneSistema.font_titolo);
		bevande = new CheckboxGroup();
		scelta_bevanda = new Checkbox[3];
		scelta_bevanda[0] = new Checkbox("Caffetteria", bevande, false);
		scelta_bevanda[1] = new Checkbox("Analcolici", bevande, false);
		scelta_bevanda[2] = new Checkbox("Alcolici", bevande, false);
		panel[4].add(label4);
		panel[4].add(scelta_bevanda[0]);
		panel[4].add(scelta_bevanda[1]);
		panel[4].add(scelta_bevanda[2]);

		//Creo il pannello dei cibi
		label5 = new Label("Cibi");
		label5.setFont(ConfigurazioneSistema.font_titolo);
		cibi = new CheckboxGroup();
		scelta_cibo = new Checkbox[3];
		scelta_cibo[0] = new Checkbox("Dolci", cibi, false);
		scelta_cibo[1]= new Checkbox("Salati", cibi, false); 
		panel[5].add(label5);
		panel[5].add(scelta_cibo[0]);
		panel[5].add(scelta_cibo[1]);

		//Creo il pannello degli antipasti
		label6 = new Label("Antipasti");
		label6.setFont(ConfigurazioneSistema.font_titolo);
		antipasti = new CheckboxGroup();
		scelta_antipasto = new Checkbox[3];
		scelta_antipasto[0] = new Checkbox("Freddi", antipasti, false);
		scelta_antipasto[1] = new Checkbox("Caldi", antipasti, false); 
		panel[6].add(label6);
		panel[6].add(scelta_antipasto[0]);
		panel[6].add(scelta_antipasto[1]);

		//Creo il pannello dei primi
		label7=new Label("Primi");
		label7.setFont(ConfigurazioneSistema.font_titolo);
		primi=new CheckboxGroup();
		scelta_primo=new Checkbox[3];
		scelta_primo[0]=new Checkbox("Solidi",primi,false);
		scelta_primo[1]=new Checkbox("Liquidi",primi,false); 
		panel[7].add(label7);
		panel[7].add(scelta_primo[0]);
		panel[7].add(scelta_primo[1]);

		//Creo il pannello dei secondi
		label8=new Label("Secondi");
		label8.setFont(ConfigurazioneSistema.font_titolo);
		secondi=new CheckboxGroup();
		scelta_secondo=new Checkbox[3];
		scelta_secondo[0]=new Checkbox("Carne",secondi,false);
		scelta_secondo[1]=new Checkbox("Pesce",secondi,false); 
		panel[8].add(label8);
		panel[8].add(scelta_secondo[0]);
		panel[8].add(scelta_secondo[1]);

		//Creo il pannello dei contorni
		label9 = new Label("Contorni");
		label9.setFont(ConfigurazioneSistema.font_titolo);
		contorni = new CheckboxGroup();
		scelta_contorno = new Checkbox[3];
		scelta_contorno[0] = new Checkbox("Verdura cotta", contorni, false);
		scelta_contorno[1] = new Checkbox("Verdura cruda", contorni, false); 
		scelta_contorno[2] = new Checkbox("Formaggio", contorni, false);
		panel[9].add(label9);
		panel[9].add(scelta_contorno[0]);
		panel[9].add(scelta_contorno[1]);
		panel[9].add(scelta_contorno[2]);

		//Creo il pannello BAR-Bevande
		panel[10].setLayout(gridbag);
		label12 = new Label("Nome del bene");
		label13 = new Label("Prezzo del bene "+(Principale.config).getValuta()+".");
		testo3 = new TextField("",40);
		testo4 = new TextField("",10);
		annulla = new Button("Annulla");
		conferma = new Button("Conferma");
		Utils.constrain(panel[10], label12, 0, 0, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 0.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[10], testo3, 1, 0, 4, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 1.0, 0.0, 0, 5, 5, 0);
		Utils.constrain(panel[10], label13, 0, 1, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 0.0, 0.0, 5, 5, 5, 0);
		Utils.constrain(panel[10], testo4, 1, 1, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 0);
		Utils.constrain(panel[10], annulla, 1, 2, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.EAST, 1.0, 0.0, 5, 5, 0, 5);
		Utils.constrain(panel[10], conferma, 2, 2, 1, 1,GridBagConstraints.NONE,
				GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 0, 5);
	}
              
	public void init()
	{
		prima_scelta[0].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(prima_scelta[0].getState())
				{
					inComuneABC(scelta_bar, 1, BeneServizio.BAR);
				}
			}
		});

		prima_scelta[1].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(prima_scelta[1].getState())
				{
					inComuneABC(scelta_piatto, 2, BeneServizio.RISTORANTE);
				}
			}
		});

		prima_scelta[2].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(prima_scelta[2].getState())
				{
					inComuneABC(scelta_servizio, 3, BeneServizio.SERVIZI);
				}
			}
		});


		scelta_bar[0].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_bar[0].getState())
				{
					inComuneGH(scelta_bevanda, 4, BeneServizio.BEVANDE);
				}
			}
		});

		scelta_bar[1].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_bar[1].getState())
				{
					inComuneGH(scelta_cibo, 5, BeneServizio.CIBI);
				}
			}
		});

		scelta_piatto[0].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_piatto[0].getState())
				{
					inComuneILMN(scelta_antipasto, 6, BeneServizio.ANTIPASTI);
				}
			}
		});

		scelta_piatto[1].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_piatto[1].getState())
				{
					inComuneILMN(scelta_primo, 7, BeneServizio.PRIMI);
				}
			}
		});

		scelta_piatto[2].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_piatto[2].getState())
				{
					inComuneILMN(scelta_secondo, 8, BeneServizio.SECONDI);
				}
			}
		});
	
		scelta_piatto[3].addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				if(scelta_piatto[3].getState())
				{
					inComuneILMN(scelta_contorno, 9, BeneServizio.CONTORNI);
				}
			}
		});

		annulla.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
				padre.setEnabled(true);
			}
		});
	} // init

	void inComuneABC(Checkbox[] scelte, int numero_pannello, char c)
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
	
	void inComuneGH(Checkbox[] scelte, int numero_pannello, char c)
	{
		level = 1;
		for(int i=2;i<13;++i)
		{
			if(panel[i].isVisible())
				remove(panel[i]);
		}
		this.add(panel[numero_pannello]);
		panel[numero_pannello].setVisible(true);
		setVisible(true);
		codice = composeCode(level, c);
	}
	
	void inComuneILMN(Checkbox[] scelte, int numero_pannello, char c)
	{
		level = 1;
		if (panel[1].isVisible())
			remove(panel[1]);
		for (int i = 3; i < 13; i++)
			if (panel[i].isVisible())
				remove(panel[i]);
		this.add(panel[numero_pannello]);
 		panel[numero_pannello].setVisible(true);
		setVisible(true);
		codice = composeCode(level, c);
	}
	
	boolean errore()
	{
		Frame msg;
		
		if ( !((testo3.getText()).length() < 33 ) )
		{
			msg = new MessageDialog(this, " La descrizione del bene o servizio deve essere di al piu' 32 caratteri! ");
			return true;
		}
		if ( (testo3.getText()).equals("") )
		{
			msg = new MessageDialog(this, " Manca la descrizione del bene o servizio! ");
			return true;
		}
		if ( (testo4.getText()).equals("") )
		{
			msg = new MessageDialog(this, " Manca il prezzo del bene o servizio! ");
			return true;
		}
		if ( !(Utils.isFloatPos(testo4.getText())) )
		{
			msg = new MessageDialog(this, " Il prezzo deve essere un numero positivo! ");
			return true;
		}
		return false;
	}
	
	char[] composeCode(int index, char tipo)
	{	
		codice[index] = tipo;
		for ( int i = index+1; i < 3; ++i)
			codice[i] = ' ';
		return codice;
	}		
		
	char[] completeCode()
	{
		if (codice[1] == ' ')
			codice[1] = '0'; 
		if (codice[2] == ' ')
			codice[2] = '0'; 
		return codice;
	}

	void o()
	{
		if (panel[10].isVisible()) // necessario nella finestra di modifica
			remove(panel[10]);
		if (panel[12].isVisible()) // necessario nella finestra di cancellazione
			remove(panel[12]);
		inComuneOP();
	}
   
	void p() // Frutta o dessert
	{
		if(panel[1].isVisible())
			remove(panel[1]);
		for(int i=3;i<13;++i)
		{
			if(panel[i].isVisible())
				remove(panel[i]);
		}
		inComuneOP();
	}

	void inComuneOP()
	{
		completeCode();
		if (elenco.getItemCount() > 0)  //per evitare warning
			elenco.removeAll();
		creaLista(); 
		this.add(panel[11]);
		panel[11].setVisible(true);
		setVisible(true);
	}
	
	void creaLista()
	{
		int i=1;
		BeneServizio b;		
		
		L = (Principale.db).elencoBeniServizi(new String(codice));
		if (L != null)
		{
			while (i <= L.length())
			{
				b = L.getBeneServizio(i);
				elenco.addItem(b.toString());
				i++;
			}
		}
	}
}