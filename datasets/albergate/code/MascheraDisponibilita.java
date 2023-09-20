package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.ListaDisponibilita;
import moduli.Disponibilita;
import moduli.ListaCommissionamenti;
import moduli.Commissionamento;


public class MascheraDisponibilita extends Frame
{
   Button Indietro, Visualizza;
   TextField testo;
   Label etichetta1, etichetta2, etichetta3, label3, label5, label1;
   Panel panel[];
   CheckboxGroup checkbox_group;
   Checkbox[] checkboxes;
   GridBagLayout gridbag = new GridBagLayout();
   List lista_disp;
   Frame padre;
   Costanti cost;
   int anno_corr, anno_prox;
   String agenzia;
   
   ListaDisponibilita L;

   public MascheraDisponibilita(Frame parent, String nome_ag)
   {
      super("Disponibilita delle stanze");
      padre = parent;
      agenzia = new String(nome_ag);
      padre.setEnabled(false);
      setupPanels();
      pack();
      init();
      this.setVisible(true);
   }

   void setupPanels()
   {
	  this.setFont(ConfigurazioneSistema.font_base);
      //Creo i pulsanti
      Indietro = new Button(" Indietro ");
      //Annulla.setFont(new Font("Courier", Font.PLAIN, 12));
      Visualizza = new Button("Visualizza disponibilita");
      Visualizza.setEnabled(false);
      //Conferma.setFont(new Font("Courier", Font.PLAIN, 12));

      //Creo le etichette
      etichetta1 = new Label("Selezionare il tipo di ricerca che si desidera effettuare:");
      etichetta1.setFont(ConfigurazioneSistema.font_titolo);
      etichetta2 = new Label("Risultato della ricerca:");
      etichetta2.setFont(ConfigurazioneSistema.font_titolo);
      etichetta3 = new Label("Ricerca per stanza:");
      etichetta3.setFont(ConfigurazioneSistema.font_titolo);
      label1 = new Label("Stanza numero");

      //Creo un Checkbox ad esclusione
      checkbox_group = new CheckboxGroup();
      checkboxes = new Checkbox[2];
      checkboxes[0] = new Checkbox(" Ricerca per stanza",checkbox_group,false);
      checkboxes[1] = new Checkbox(" Ricerca su tutte le stanze",checkbox_group,false);


      //Creo il TextField e lo rendo non editabili
      testo = new TextField("", 4);
      testo.setEditable(false);

      //creo una Lista
      lista_disp = new List(10, false);

      panel=new Panel[5];
      for(int i=0;i<panel.length;++i)
      {
         panel[i]=new Panel();
         panel[i].setLayout(gridbag);
      }
      
      //Creo il pannello in alto a sinistra
      Utils.constrain(panel[1], etichetta1, 0, 0, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 10, 0); 
      Utils.constrain(panel[1], checkboxes[0], 0, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 5, 0, 0); 
      Utils.constrain(panel[1], checkboxes[1], 0, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 5, 0, 0);
            
      //Creo il pannello ricerca per stanza
      Utils.constrain(panel[2], etichetta3, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 10, 0);
      Utils.constrain(panel[2], label1, 0, 1, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 5, 0, 20);
      Utils.constrain(panel[2], testo, 1, 1, 1, 1, GridBagConstraints.NONE,
                      GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);

      //Creo il pannello dei pulsanti
      Utils.constrain(panel[0], Indietro, 1, 0, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.EAST, 0.3, 0.0, 10, 0, 10, 5);
      Utils.constrain(panel[0], Visualizza, 3, 0, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.WEST, 0.3, 0.0, 10, 5, 10, 0);

      //Creo il pannello contenente la list_soggiornanti 
      Utils.constrain(panel[4], etichetta2, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 10, 0);
      Utils.constrain(panel[4], lista_disp, 0, 1, 1, 1, GridBagConstraints.BOTH,
                         GridBagConstraints.NORTHWEST, 1.0, 1.0, 0, 5, 0, 20);

      //Attacco i pannelli al frame
      this.setLayout(gridbag);
      Utils.constrain(this, panel[1], 0, 0, 5, 4, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(this, panel[2], 0, 4, 5, 2, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 1.0, 5, 5, 5, 5);
      Utils.constrain(this, panel[0], 0, 6, 5, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.SOUTHEAST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(this, panel[4], 0, 7, 5, 7, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 1.0, 5, 5, 5, 5);
   }

   public void init()
   {
      cost = new Costanti();
      anno_corr = cost.getAnnoCorr();
      anno_prox = cost.getAnnoProx();
      
      checkboxes[0].addItemListener(new ItemListener()   
      {
         public void itemStateChanged(ItemEvent e)
         {
                testo.setEditable(true);
				Visualizza.setEnabled(true);
				if (lista_disp.getItemCount() > 0)
					lista_disp.removeAll();
         }
      });
                      
      checkboxes[1].addItemListener(new ItemListener()   
      {
         public void itemStateChanged(ItemEvent e)
         {
                testo.setEditable(false);
				testo.setText("");
				Visualizza.setEnabled(true);
				if (lista_disp.getItemCount() > 0)
					lista_disp.removeAll();
         }
      });

      Visualizza.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
			Disponibilita disp = new Disponibilita();
			String str = new String("");
			//Tramuto temporaneamente tutti i commissionamenti di una certa agenzia 
			//in disponibilita
		
			ListaDisponibilita L_disp = (Principale.db).elencoDisponibilita();
			if (!agenzia.equals(""))
			{
				ListaCommissionamenti L_comm = (Principale.db).foundCommissionamenti(agenzia);		
				Commissionamento comm;
				for (int i = 1; i <= L_comm.length(); i++)
				{
					comm = L_comm.getCommissionamento(i);
					disp = Utils.getDispOfRoom(L_disp,comm.getNumStanza(),true);		
					disp.setDisponibilita(comm.getInizioComm(),comm.getFineComm(),Flag.DISPONIBILE, Flag.COMMISSIONATA);
					L_disp.addDisponibilita(disp);
				}
				str = new String("  ---  commissioni "+agenzia);
			}
			if (L_disp.length() > 0)
				lista_disp.addItem("TABELLA DELLE DISPONIBILITA'"+str);
			if (checkboxes[0].getState())
				showDispOfRoom(L_disp,testo.getText());
			else
				showDispOfHotel(L_disp);	
         }
       });

      Indietro.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            dispose();
            padre.setEnabled(true);
          }
       });
   }
   
	void showDispOfRoom(ListaDisponibilita L, String room)
	{
		Frame msg;
		Disponibilita disp;
		String aus;
		
		if (room.equals(""))
				msg = new MessageDialog(this,"Inserire il numero della stanza per la ricerca delle sue dispoibilita");
		else
		{	
			disp = Utils.getDispOfRoom(L, room, false);
			if (disp == null)
			{	
				msg = new MessageDialog(this,"La stanza inserita e' inesistente!");
				if (lista_disp.getItemCount() > 0)
					lista_disp.removeAll();
			}	
			else
			{
				lista_disp.addItem("");
				lista_disp.addItem("Disponibilita' stanza n. "+room+" per l'anno "+anno_corr);
				aus = new String(Utils.scanVectorForDisp(disp.getDispAnnoCorr(), anno_corr, (Principale.config).getStagAnnoCorr()));
				if (aus.equals(""))
					lista_disp.addItem("NESSUNA DISPONIBILITA'");
				else
					lista_disp.addItem(aus);
				lista_disp.addItem("");
				lista_disp.addItem("Disponibilita' stanza n. "+room+" per l'anno "+anno_prox);
				aus = new String(Utils.scanVectorForDisp(disp.getDispAnnoProx(), anno_prox, (Principale.config).getStagAnnoProx()));
				if (aus.equals(""))
					lista_disp.addItem("NESSUNA DISPONIBILITA'");
				else
					lista_disp.addItem(aus);
			}				
		}	
	}
	
	void showDispOfHotel(ListaDisponibilita L)
	{
		Frame msg;
		if (L.length() == 0)
			msg = new AvvisoDialog(this,"Nessuna stanza trovata!");
		else
			for (int i = 1; i <= L.length(); i++)
				showDispOfRoom(L, (L.getDisponibilita(i)).getNumStanza());
	}   
}

