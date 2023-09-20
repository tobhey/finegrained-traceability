package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.*;

public class MascheraPerAddebitare extends Frame
{
	// variabili per tenere tutto in memoria
	ListaBeniServizi elenco_extra = new ListaBeniServizi();
	ListaAddebiti elenco_addebiti = new ListaAddebiti();
	ListaBeniServizi elenco_extra_addebitati = new ListaBeniServizi();
	
	// oggetti per la costruzione della finestra
   	Label etich4, etich5, etich6, etich7;
	Panel panel1, panel2, panel3, panel4, panel5, panel6, panel7;
   	Button conferma, annulla, addebita, deaddebita, aggiungi;
   	TextField extra_add, totale;
	List addebiti, extra;
	GridBagLayout gridbag = new GridBagLayout();
	
	// attributi per la gestione della gerarchia delle finestre;
	Frame padre = new Frame();      
	AggiungiBeneServizio nuovo;
	
	// flag per capire se si tratta di un addebito o di un deaddebito
	boolean addebito;

    // variabili per il travaso del database in memoria
    int quantita_mod[] = new int[0]; // quantita aggiunge o tolte a vecchi addebiti

	public MascheraPerAddebitare(String titolo, String bottone_conf)
	{
		super(titolo);
		setupPanels(bottone_conf);
		init();
		pack();
	}
	
	// Creazione della finestra
	void setupPanels(String bottone_conf)
	{
		this.setFont(ConfigurazioneSistema.font_base);
		// creo l'elenco degli addebiti alla stanza e degli extra
		addebiti = new List(10, false);
		addebiti.setFont(ConfigurazioneSistema.font_allineato);
		extra = new List(10, false);
		extra.setFont(ConfigurazioneSistema.font_allineato);
  
		// creo i textfield
		extra_add = new TextField("", 40);
		totale = new TextField("", 20);
		extra_add.setEditable(false);
		totale.setEditable(false);

		// creo i pulsanti
		addebita = new Button("Addebita");
		deaddebita = new Button("Togli dall'addebito");
		aggiungi = new Button("Aggiungi nuovo bene/servizio");
		annulla = new Button("Annulla");
		conferma = new Button(bottone_conf);
		
		// creo le label
		etich4 = new Label("Informazioni sugli addebiti");
		etich4.setFont(ConfigurazioneSistema.font_titolo);
		etich5 = new Label("Ultimo addebito effettuato");
		etich6 = new Label("Lista degli addebiti");
		etich7 = new Label("Totale dell'addebito");

		// creo il primo pannello vuoto
		panel1 = new Panel();
		
		// creo pannello di informazione degli addebiti
		panel2 = new Panel();
		panel2.setLayout(gridbag);
		Utils.constrain(panel2, etich4, 0, 0, 3, 1);
		Utils.constrain(panel2, etich5, 0, 2, 3, 1, 10, 0, 0, 0);
		Utils.constrain(panel2, extra_add, 0, 3, 1, 1, 0, 20, 0, 0);
		Utils.constrain(panel2, etich6, 0, 5, 3, 1, 10, 0, 0, 0);
		Utils.constrain(panel2, addebiti, 0, 6, 2, 4, GridBagConstraints.BOTH,
					GridBagConstraints.WEST, 1.0, 1.0, 0, 20, 0, 20);
		Utils.constrain(panel2, etich7, 0, 12, 3, 1, 10, 0, 0, 0);
		Utils.constrain(panel2, totale, 0, 13, 1, 1, 0, 20, 0, 0);

		// creo pannello per addebita
		panel3 = new Panel();
		panel3.setLayout(gridbag);
		Utils.constrain(panel3, addebita, 0, 0, 1, 1, GridBagConstraints.HORIZONTAL,
					GridBagConstraints.CENTER, 1.0, 0.0, 0, 0, 0, 5);
		Utils.constrain(panel3, deaddebita, 1, 0, 1, 1, GridBagConstraints.HORIZONTAL,
					GridBagConstraints.CENTER, 1.0, 0.0, 0, 5, 0, 0);

		// creo pannello vuoto per num_stanza o coperti
		panel4 = new Panel();
		
		// creo pannello per aggiungi
		panel5 = new Panel();
		panel5.setLayout(gridbag);
		Utils.constrain(panel5, aggiungi, 0, 0, 1, 1, GridBagConstraints.NONE,
					GridBagConstraints.CENTER, 1.0, 0.0, 5, 5, 5, 5);
		
		// creo pannello per annulla
		panel6 = new Panel();
		panel6.setLayout(gridbag);
		Utils.constrain(panel6, annulla, 0, 0, 1, 1, GridBagConstraints.NONE,
					GridBagConstraints.CENTER, 1.0, 0.0, 0, 0, 0, 0);
	
		// creo pannello per conferma	
		panel7 = new Panel();
		panel7.setLayout(gridbag);
		Utils.constrain(panel7, conferma, 0, 0, 1, 1, GridBagConstraints.NONE,
					GridBagConstraints.CENTER, 1.0, 0.0, 0, 0, 0, 0);

		// costruisco la finestra definitiva attaccando i pannelli
		this.setLayout(gridbag);
		Utils.constrain(this, panel1, 0, 0, 1, 1, GridBagConstraints.VERTICAL,
					GridBagConstraints.WEST, 0.5, 0.5, 5, 5, 5, 5);
		Utils.constrain(this, panel2, 1, 0, 1, 4, GridBagConstraints.VERTICAL,
					GridBagConstraints.WEST, 0.5, 0.5, 5, 5, 5, 5);
		Utils.constrain(this, panel3, 0, 1, 1, 1, GridBagConstraints.VERTICAL,
					GridBagConstraints.CENTER, 0.0, 0.0, 5, 0, 5, 0);
		Utils.constrain(this, panel4, 0, 2, 1, 1, GridBagConstraints.VERTICAL,
					GridBagConstraints.WEST, 0.5, 0.5, 5, 0, 5, 0);
		Utils.constrain(this, panel5, 0, 3, 1, 1, GridBagConstraints.VERTICAL,
					GridBagConstraints.CENTER, 0.0, 0.0, 5, 5, 5, 5);
		Utils.constrain(this, panel6, 0, 4, 1, 1, GridBagConstraints.BOTH,
					GridBagConstraints.CENTER, 0.0, 0.0, 5, 0, 5, 0);
		Utils.constrain(this, panel7, 1, 4, 2, 1, GridBagConstraints.BOTH,
					GridBagConstraints.CENTER, 0.0, 0.0, 5, 0, 5, 0);
	}

	// Per la gestione degli eventi della finestra
	void init()
	{
		annulla.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
				padre.setEnabled(true);
			}
		});

        aggiungi.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
            	startAggiungi();
            }
        });
	}

    void startAggiungi()
    {
		nuovo = new AggiungiBeneServizio(this);
    }
}

