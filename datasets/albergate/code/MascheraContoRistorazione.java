package interfacce;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.*;

public class MascheraContoRistorazione extends MascheraPerAddebitare
{
        // oggetti per la costruzione della finestra
        Label etich1, etich2, etich3;
        TextField num_coperti;

        public MascheraContoRistorazione(Frame parent)
        {
                super("Servizio ristorazione", "Stampa del conto");
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
				
				elenco_extra = (Principale.db).menuRistorante();
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
                num_coperti = new TextField("", 4);
                num_coperti.setEditable(true);

                // creo le label
                etich1 = new Label("Gestione degli addebiti              ");
                etich1.setFont(ConfigurazioneSistema.font_titolo);
                etich2 = new Label("Scegliere l'extra da addebitare ");
                etich3 = new Label("Numero di coperti ");

                // creo pannello di sinistra
                this.remove(panel1);
                panel1 = new Panel();
                panel1.setLayout(gridbag);
                Utils.constrain(panel1, etich1, 0, 0, 6, 1);
                Utils.constrain(panel1, etich2, 0, 1, 6, 1, 10, 0, 0, 0);
                Utils.constrain(panel1, extra, 0, 2, 6, 4, GridBagConstraints.BOTH,
                                GridBagConstraints.WEST, 1.0, 1.0, 0, 20, 0, 20);

				// creo pannello per coperti
				this.remove(panel4);
				panel4 = new Panel();
				panel4.setLayout(gridbag);
                Utils.constrain(panel4, etich3, 0, 0, 1, 1, 5, 5, 5, 5);
                Utils.constrain(panel4, num_coperti, 1, 0, 1, 1, GridBagConstraints.NONE,
                                GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5);

                // aggiungo il pannello appena creato al resto
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
                                stampaConto();
                        }
                });

                addebita.addActionListener(new ActionListener()
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                if (!errore())
                                {
                                    aggiornaAvideo(true);
                                }
                        }
                });

                deaddebita.addActionListener(new ActionListener()
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                if (!errore())
                                {
                                        aggiornaAvideo(false);
                                }
                        }
                });

                extra.addActionListener(new ActionListener()
                {
                        public void actionPerformed(ActionEvent e)
                        {
                                if (!errore())
                                {
                                        aggiornaAvideo(true);
                                }
                        }
                });
        }

        // Ritorna false se si sta' cercando di addebitare un extra in
        // mancanza dei dati necessari per l'addebito: coperti e extra.
        boolean errore()
        {
        		Frame msg;
        		
                if ((num_coperti.getText()).equals(""))
                {
                        msg = new MessageDialog(this, "Inserire il numero di coperti!");
                        return true;
                }
                else if ( !Utils.isIntPos(num_coperti.getText()) )
                {
                		msg = new MessageDialog(this, "Il numero dei coperti deve essere un numero intero!");
                		return true;
                }
                else if (extra.getSelectedIndex() == -1)
                {
                        msg = new MessageDialog(this, "Manca selezione!");
                        return true;
                }
                else
                        return false;
        }

        int posExtraInAddebiti(BeneServizio b)
        {
                for (int i = 1; i <= elenco_addebiti.length(); i++)
                        if ( ((elenco_addebiti.getAddebito(i)).getCodExtra()).equals(b.getCodExtra()) )
                                return i;
                return -1;
        }

        void aggiornaAvideo(boolean addebito)
        {
        		Frame msg;
                int supp[];

                BeneServizio extra_scelto = elenco_extra.getBeneServizio( extra.getSelectedIndex() + 1 );
                int pos;
                if ((pos = posExtraInAddebiti(extra_scelto)) < 0 && addebito)
                {
                        Addebito a = new Addebito("RIST", extra_scelto.getCodExtra(), 0, 0);
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
                                        if ( quantita_mod[pos - 1] > 0)
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
                float conto_di_questo_extra;
                
                if (addebiti.getItemCount() > 0)
                        addebiti.removeAll();
                while ( i <= elenco_addebiti.length() )
                {
						if (quantita_mod[i-1] != 0)
						{
	                        e = elenco_extra_addebitati.getBeneServizio(i);
	                        conto_di_questo_extra = quantita_mod[i-1]*e.getPxUnitario();
    	                    if ( Integer.parseInt(e.getCodExtra()) >= 40000000 && Integer.parseInt(e.getCodExtra()) < 50000000)
        	                        addebiti.addItem("S "+Utils.completaStringa(e.getDescrizione(), 32)+" "+(Principale.config).getValuta()+"."+(elenco_addebiti.getAddebito(i)).getTotAddebito());
            	            else if ( Integer.parseInt(e.getCodExtra()) >= 50000000)
                	                addebiti.addItem("R "+Utils.completaStringa(e.getDescrizione(), 32)+" "+(Principale.config).getValuta()+"."+(elenco_addebiti.getAddebito(i)).getTotAddebito());
                    	    else
                        	        addebiti.addItem( Utils.completaStringa(e.getDescrizione(),10)+Utils.completaStringa(" x "+quantita_mod[i-1], 5)+(Principale.config).getValuta()+"."+conto_di_questo_extra );
	                        conto = conto + conto_di_questo_extra;
    	                }
   	                    i++;
                }
                totale.setText((Principale.config).getValuta()+"."+conto);
        }

        void stampaConto()
        {
			if (!errore())
			{
	        	writeFile();
                dispose();
                padre.setEnabled(true);
	        }
        }

		private void writeFile()
		{
			Frame msg;
			float costo_coperti;
		
			DataOutputStream outStream;
			try
			{	
				outStream = new DataOutputStream(new FileOutputStream("conto.abg"));
			}
			catch (IOException ex)
			{
				msg = new MessageDialog(this,"Errore nell'apertura del file del conto");
				return;
			}
			try
			{
				outStream.writeBytes( (Principale.config).getNomeAlbergo()+"\n" );
				outStream.writeBytes( "\n" );
				outStream.writeBytes( (Principale.config).getRagSoc()+"\n" );
				outStream.writeBytes( (Principale.config).getIndirizzoAlb()+"\n" );
				outStream.writeBytes( (Principale.config).getComuneAlb()+"\n" );
				outStream.writeBytes( (Principale.config).getCittaAlb()+"\n" );
				outStream.writeBytes( "\n" );

				BeneServizio extra;
				Addebito a;				
				int num;
				String supporto;
				float conto_totale;
				
				for(int i=1; i<=elenco_addebiti.length(); i++)
				{
					if (quantita_mod[i-1] != 0)
					{
						a = elenco_addebiti.getAddebito(i);
						extra = elenco_extra_addebitati.getBeneServizio(i);
						num = quantita_mod[i-1];
						if ( (extra.getCodExtra()).compareTo("40000000") < 0 )
							supporto = new String(extra.getDescrizione()+" x "+num+" ");
						else if ( (extra.getCodExtra()).compareTo("50000000") < 0 )
							supporto = new String("Supplemento per "+extra.getDescrizione()+" ");
						else
							supporto = new String("Riduzione per "+extra.getDescrizione()+" ");
						while (supporto.length() < 52)
							supporto = new String(supporto+" ");
						outStream.writeBytes(supporto+(Principale.config).getValuta()+"."+
												num*extra.getPxUnitario()+"\n" );
					}
				}
				supporto = new String("Coperto x "+num_coperti.getText());
				while (supporto.length() < 52)
					supporto = new String(supporto+" ");
				costo_coperti = Integer.parseInt(num_coperti.getText())*(Principale.config).getCoperto();
				outStream.writeBytes(supporto+(Principale.config).getValuta()+"."+costo_coperti+"\n");
				outStream.writeBytes("\n");
				supporto = new String("Totale");
				while (supporto.length() < 52)
					supporto = new String(supporto+" ");
				conto_totale = (Float.valueOf( new String((totale.getText()).substring(2)) )).floatValue()+costo_coperti;
				outStream.writeBytes( supporto+(Principale.config).getValuta()+"."+conto_totale );
				outStream.close();
			}	
			catch (IOException ex)
			{
				msg = new MessageDialog(this,"Errore nella scrittura del file del conto");	
				return;
			}			
		}		
}