package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.*;

public class MascheraAddebiti extends MascheraPerAddebitare
{
        // variabili per il travaso del database in memoria
        String stanza_prec = new String("");

        // oggetti per la costruzione della finestra
        Label etich1, etich2, etich3;
        TextField num_stanza;

		
        public MascheraAddebiti(Frame parent)
        {
                super("Addebito spese e servizi extra", "Conferma l'addebito");
                padre = parent;
                padre.setEnabled(false);
                inizExtra();
                setupPanello();
                inizializza();
                pack();
                setVisible(true);
        }

        // Travaso dati degli extra nelle strutture dati apposite
        void inizExtra()
        {
				MessageDialog msg;
				
				elenco_extra = (Principale.db).elencoSenzaSuppRid();
                if (elenco_extra != null)
                {
                	if (extra.getItemCount() > 0)
                    	extra.removeAll();
                    int i=1;
                    while (i <= elenco_extra.length())
                    {
	                    extra.addItem( (elenco_extra.getBeneServizio(i)).toString() );
                        i++;
                    }
                }
        }

        // Creazione della finestra
        void setupPanello()
        {
                num_stanza = new TextField("", 4);
                num_stanza.setEditable(true);

                // creo le label
                etich1 = new Label("Gestione degli addebiti              ");
                etich1.setFont(ConfigurazioneSistema.font_titolo);
                etich2 = new Label("Scegliere l'extra da addebitare ");
                etich3 = new Label("Addebitare alla stanza numero ");

                // creo pannello di sinistra
                this.remove(panel1);
                panel1 = new Panel();
                panel1.setLayout(gridbag);
                Utils.constrain(panel1, etich1, 0, 0, 6, 1);
                Utils.constrain(panel1, etich2, 0, 1, 6, 1, 10, 0, 0, 0);
                Utils.constrain(panel1, extra, 0, 2, 6, 4, GridBagConstraints.BOTH,
                                GridBagConstraints.WEST, 1.0, 1.0, 0, 20, 0, 20);

				this.remove(panel4);
				panel4 = new Panel();
				panel4.setLayout(gridbag);
                Utils.constrain(panel4, etich3, 0, 0, 1, 1, 5, 5, 5, 5);
                Utils.constrain(panel4, num_stanza, 1, 0, 1, 1, GridBagConstraints.NONE,
                                GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5);

                // aggiungo i pannelli appena creati al resto
                Utils.constrain(this, panel1, 0, 0, 1, 1, GridBagConstraints.VERTICAL,
                                        GridBagConstraints.WEST, 0.5, 0.5, 5, 5, 5, 5);
                Utils.constrain(this, panel4, 0, 2, 1, 1, GridBagConstraints.VERTICAL,
                                        GridBagConstraints.WEST, 0.5, 0.5, 5, 5, 5, 5);
        }

        // Per la gestione degli eventi della finestra
        void inizializza()
        {
                conferma.addActionListener(new ActionListener()
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                scriviSuDB(num_stanza.getText());
                                dispose();
                                padre.setEnabled(true);
                        }
                });

                addebita.addActionListener(new ActionListener()
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                if (!errore())
                                {
                                		addebito = true;
                                        aggiornaAddebiti();
                                }
                        }
                });

                deaddebita.addActionListener(new ActionListener()
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                if (!errore())
                                {
                                		addebito = false;
                                        aggiornaAddebiti();
                                }
                        }
                });

                num_stanza.addActionListener(new ActionListener()
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                if (!errore())
                                {
                                		addebito = true;
                                        aggiornaAddebiti();
                                }
                        }
                });

                extra.addActionListener(new ActionListener()
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                if (!errore())
                                {
                                		addebito = true;
                                        aggiornaAddebiti();
                                }
                        }
                });
        }

        // Ritorna false se si sta' cercando di addebitare un extra in
        // mancanza dei dati necessari per l'addebito: num_stanza e extra.
        boolean errore()
        {
                if ((num_stanza.getText()).equals("") && (extra.getSelectedIndex() == -1))
                {
                        MessageDialog p = new MessageDialog(this, "Manca stanza e selezione!");
                        return true;
                }
                else if ((num_stanza.getText()).equals(""))
                {
                        MessageDialog p = new MessageDialog(this, "Manca stanza!");
                        return true;
                }
                else if (extra.getSelectedIndex() == -1)
                {
                        MessageDialog p = new MessageDialog(this, "Manca selezione!");
                        return true;
                }
                else if ( (num_stanza.getText()).length() > 4 )
                {
                        MessageDialog p = new MessageDialog(this, "Errore nel numero di stanza!");
                        return true;
                }
                else
                        return false;
        }

        void annullaAddebitiStanzaPrec()
        {
                stanza_prec = new String(num_stanza.getText());
   	        	aggiornaAvideo();
        }

        // Aggiorna il panel2, che e' quello di informazione sugli addebiti
        // di una data stanza, in modo che contenga le informazioni
        // aggiornate sugli addebiti.
        void aggiornaAddebiti()
        {
                if ( !(stanza_prec.equals(num_stanza.getText())) )
					AltraStanza();
                else
					aggiornaAvideo();
        }

		boolean thereIsModifiche()
		{
			boolean modifiche = false;
			for (int i = 0; i < quantita_mod.length; i++)
				modifiche = modifiche || (quantita_mod[i] != 0);
			return modifiche;
		}
		
        // Reinizzializza l'array degli addebiti in quanto si cambia stanza
        // e chiede se gli addebiti fatti alla stanza precedente vanno
        // confermati o meno.
        void AltraStanza()
        {
                Frame msg;
                FinModifiche p;

                if ( (Principale.db).readStanza(num_stanza.getText()) != null )
                {
                	ListaSoggiornanti L;
					if ( (L = (Principale.db).foundSoggiornanti(num_stanza.getText(), false) ) != null )
					{
						if (!L.isEmpty())
						{
	                        if (!stanza_prec.equals("") && thereIsModifiche())
							{
           	                    p = new FinModifiche(this, "Attenzione: cambio di stanza!");
           	                }
            	            else
            	            {
								riscriviElencoAddebiti();
								annullaAddebitiStanzaPrec();
							}
						}
						else
						{
							msg = new AvvisoDialog(this, "Nessun soggiornante occupa la stanza!");
							num_stanza.setText(stanza_prec);
						}
					}
					else
					{
						msg = new MessageDialog(this, "Problemi con il database: errore.");
					}
				}
                else
                {
	                msg = new MessageDialog(this, "Stanza inesistente!");
                    num_stanza.setText(stanza_prec);
                }
        }

		void riscriviElencoAddebiti()
		{
			MessageDialog msg;
			
			elenco_addebiti = (Principale.db).foundAddebiti(num_stanza.getText());
			elenco_extra_addebitati = new ListaBeniServizi();
            if (elenco_addebiti != null)
            {
            	// inizializzo l'array delle modifiche e degli extra addebitati
                quantita_mod = new int[elenco_addebiti.length()];
                BeneServizio extra;
                for (int i=0; i<quantita_mod.length; i++)
                {
                	quantita_mod[i] = 0;
                	extra = (Principale.db).readBeneServizio( (elenco_addebiti.getAddebito(i+1)).getCodExtra() );
                	if (extra == null)
	                {
	                	msg = new MessageDialog(this, "Uno degli extra addebitati non e' stato trovato!");
	                	extra = new BeneServizio( (elenco_addebiti.getAddebito(i+1)).getCodExtra(), "Sconosciuto", 0 );
	                }
                	elenco_extra_addebitati.addBeneServizio( extra );
                }
            }
            else
                msg = new MessageDialog(this, "Errore nel database!");
		}
 		
        int posExtraInAddebiti(BeneServizio b)
        {
                for (int i = 1; i <= elenco_addebiti.length(); i++)
                        if ( ((elenco_addebiti.getAddebito(i)).getCodExtra()).equals(b.getCodExtra()) )
                                return i;
                return -1;
        }

        void aggiornaAvideo()
        {
        		Frame msg;
                int supp[];

                BeneServizio extra_scelto = elenco_extra.getBeneServizio( extra.getSelectedIndex() + 1 );
                int pos;
                if ((pos = posExtraInAddebiti(extra_scelto)) < 0 && addebito)
                {
                        Addebito a = new Addebito(num_stanza.getText(), extra_scelto.getCodExtra(), 0, 0);
                        elenco_addebiti.addAddebito( a );
                        elenco_extra_addebitati.addBeneServizio( extra_scelto );

                        // allungo l'array delle quantita' modificate
                        supp = new int[elenco_addebiti.length()];
                        for (int i = 0; i<quantita_mod.length; i++)
                                supp[i] = quantita_mod[i];
                        supp[supp.length - 1] = 1;
                        quantita_mod = supp;
                        pos = quantita_mod.length;
                        extra_add.setText(extra.getSelectedItem());
                        continuaAggiornamento();
                }
                else
                {
                        if (pos < 0)
                        {
                                // sto cercando di togliere da un addebito inesistente
                                msg = new AvvisoDialog(this, "Stai cercando di togliere da un addebito ormai inesistente!");
                        }
                        else
                        {
                                // aggiorno tale array per contenere l'addebito appena effettuato
                                if (addebito)
                                {
                                        quantita_mod[pos - 1]++;
                                        extra_add.setText(extra.getSelectedItem());
                                        continuaAggiornamento();
                                }
                                else
                                        if ( (quantita_mod[pos - 1]+(elenco_addebiti.getAddebito(pos)).getQuantita()) > 0)
                                        {
                                                quantita_mod[pos - 1]--;
                                                extra_add.setText(extra.getSelectedItem());
                                                continuaAggiornamento();
                                        }
                                        else
			                                msg = new AvvisoDialog(this, "Stai cercando di togliere da un addebito ormai inesistente!");
                        }
                }
        }

        void continuaAggiornamento()
        {
                int i = 1;
                BeneServizio e;
                float conto = 0;
                if (addebiti.getItemCount() > 0)
                        addebiti.removeAll();
                while ( i <= elenco_addebiti.length() )
                {
					if ( ((elenco_addebiti.getAddebito(i)).getQuantita() + quantita_mod[i-1]) > 0 )
					{
                        e = elenco_extra_addebitati.getBeneServizio(i);
                        if ( Integer.parseInt(e.getCodExtra()) >= 40000000 && Integer.parseInt(e.getCodExtra()) < 50000000)
                                addebiti.addItem("S "+Utils.completaStringa(e.getDescrizione(), 32)+" "+(Principale.config).getValuta()+"."+(elenco_addebiti.getAddebito(i)).getTotAddebito());
                        else if ( Integer.parseInt(e.getCodExtra()) >= 50000000)
                                addebiti.addItem("R "+Utils.completaStringa(e.getDescrizione(), 32)+" "+(Principale.config).getValuta()+"."+(elenco_addebiti.getAddebito(i)).getTotAddebito());
                        else
                                addebiti.addItem(Utils.completaStringa(e.getDescrizione(), 10)+
                                				Utils.completaStringa(" x "+(elenco_addebiti.getAddebito(i)).getQuantita(), 5)+" +("+quantita_mod[i-1]+")");
                        conto = conto + (elenco_addebiti.getAddebito(i)).getTotAddebito() + quantita_mod[i-1]*e.getPxUnitario();
                    }
                    i++;
                }
                totale.setText((Principale.config).getValuta()+"."+conto);
        }

        void scriviSuDB(String stanza)
        {
                for (int i = 1; i <= elenco_addebiti.length(); i++)
                {
                        if (quantita_mod[i-1] != 0)
                        {
                                if ( (quantita_mod[i - 1]+(elenco_addebiti.getAddebito(i)).getQuantita()) > 0)
                                {
                                        int j = (Principale.db).addAddebito(stanza, (elenco_addebiti.getAddebito(i)).getCodExtra(), quantita_mod[i-1] );
                                        if (j < 0)
                                                DataBase.visErrore(j);
                                }
                                else
                                {
                                        int j = (Principale.db).delAddebito(stanza, (elenco_addebiti.getAddebito(i)).getCodExtra() );
                                        if (j < 0)
                                                DataBase.visErrore(j);
                                }
                        }
                }
        }
}

/****************************************************************************/
class FinModifiche extends Frame
{
        Button ok, annulla;
        Label msg = new Label("Si vogliono salvare le modifiche degli addebiti della stanza precedente?");
        GridBagLayout gridbag = new GridBagLayout();
        String titolo;

        MascheraAddebiti padre = null;

        public FinModifiche(MascheraAddebiti parent, String title)
        {
                super(title);

				padre = parent;
				padre.setEnabled(false);

                titolo = new String(title);

                this.setLayout(gridbag);
                ok = new Button("Salva le modifiche");
                annulla = new Button("Annulla le modifiche");
                Utils.constrain(this, msg, 0, 0, 5, 2, GridBagConstraints.BOTH,
                                        GridBagConstraints.CENTER, 1.0, 1.0, 20, 20, 20, 20);
                Utils.constrain(this, annulla, 1, 2, 1, 1, GridBagConstraints.NONE,
                                        GridBagConstraints.CENTER, 1.0, 1.0, 20, 20, 20, 20);
                Utils.constrain(this, ok, 3, 2, 1, 1, GridBagConstraints.NONE,
                                        GridBagConstraints.CENTER, 1.0, 1.0, 20, 20, 20, 20);
                this.pack();
                init();
                setVisible(true);
        }

        public void init()
        {
                ok.addActionListener(new ActionListener()
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                padre.scriviSuDB(padre.stanza_prec);
                                inComune();
                        }
                });

                annulla.addActionListener(new ActionListener()
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                inComune();
                        }
                });
        }

        void inComune()
        {
                padre.riscriviElencoAddebiti();
                padre.annullaAddebitiStanzaPrec();
                padre.setEnabled(true);
                dispose();
        }
}
