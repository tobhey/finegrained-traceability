package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.*;

public class MascheraCommissioni extends Frame
{
   TextField num_stanza, nome_agenzia, inizio_comm, fine_comm, scadenza_comm ,num_tel_agenzia;
   Label etichetta3, etich1, etich2, etich3, etich4, etich5, etich6, etich7;
   Panel panel1, panel2, panel3, panel4, panel5;
   List lista;
   Button Annulla, Azione, Cerca;
   GridBagLayout gridbag = new GridBagLayout();
   SubCommissioni padre = new SubCommissioni();
   ListaCommissionamenti L;
   int caller;
   public MascheraCommissioni(String data1, String data2, String title, String caption, int c)
   {
      super(title);
      caller = c;
      setupPanels(data1, data2, caption);
      inizializza();
      pack();
   }

   	void setupPanels(String data1, String data2, String caption)
   	{
	    this.setFont(ConfigurazioneSistema.font_base);
      	//Creo le etichette
      	etichetta3 = new Label("Inserimento dati della commissione");
      	etichetta3.setFont(ConfigurazioneSistema.font_titolo);
      	etich1 = new Label("Nome Agenzia");
      	etich2 = new Label("Numero Stanza");
      	etich3 = new Label("Data inizio Commissione");
      	etich4 = new Label("Data fine Commissione");
      	etich5 = new Label("Data scadenza Commissione");
      	etich6 = new Label("Numero telefonico Agenzia");
      	etich7 = new Label("Risultato della ricerca");
		etich7.setFont(ConfigurazioneSistema.font_titolo);
		
      
      //Creo i TextField e ne rendo  alcuni non editabili
      nome_agenzia = new TextField("",20);
      num_stanza = new TextField("",4);
      inizio_comm = new TextField("",12);
      fine_comm = new TextField("",12);
      scadenza_comm = new TextField("",12);
      num_tel_agenzia = new TextField("",16);

      //Creo i pulsanti
      Annulla = new Button(" Fine ");
      Azione = new Button(caption);
      Cerca = new Button(" Cerca ");

      //Creo il pannello
      panel3 = new Panel();
      panel3.setLayout(gridbag);
      Utils.constrain(panel3, etichetta3, 0, 0, 3, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0); 
      Utils.constrain(panel3, etich1, 0, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel3,nome_agenzia , 1, 1, 1, 1, GridBagConstraints.NONE,
                    GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel3, etich2, 2, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel3,num_stanza, 3, 1, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel3, etich3, 0, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel3,inizio_comm , 1, 2, 1, 1, GridBagConstraints.NONE,
                    GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel3, etich4, 2, 2, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel3,fine_comm , 3, 2, 1, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel3, etich5, 0, 3, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel3,scadenza_comm , 1, 3, 1, 1, GridBagConstraints.NONE,
                    GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel3, etich6, 0, 4, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel3,num_tel_agenzia , 1, 4, 4, 1, GridBagConstraints.NONE,
                         GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);

      panel4 = new Panel();
      panel4.setLayout(gridbag);
      Utils.constrain(panel4, Annulla, 0, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 5, 5, 5, 15);
      Utils.constrain(panel4, Azione, 1, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 5, 5, 5, 5);      
      if ((caller == 2) || (caller == 3) )
      Utils.constrain(panel4,Cerca, 2, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 5, 5, 5, 5);                               
		
		
		lista = new List(10, false);
		lista.setFont(ConfigurazioneSistema.font_allineato);
		
		panel5 = new Panel();
		panel5.setLayout(gridbag);
        Utils.constrain(panel5, etich7, 0, 0, 2, 1, GridBagConstraints.NONE,
                       GridBagConstraints.WEST, 1.0, 0.0, 0, 0, 0, 0);
        Utils.constrain(panel5,lista, 0, 1, 4, 3, GridBagConstraints.BOTH,
                        GridBagConstraints.CENTER, 3.0, 3.0, 0, 0, 0, 0);
		      
      
      //Attacco i pannelli al frame
      this.setLayout(gridbag);
      Utils.constrain(this, panel3, 0, 0, 6, 5, GridBagConstraints.NONE,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(this, panel4, 0, 5, 6, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(this, panel5, 0, 6, 6, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5);                  
	panel5.setVisible(false);
	pack();	   		
   
   }

   public void inizializza()
   {
      Annulla.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
			dispose();
			padre.setEnabled(true);
         }
      });
   }

	Commissionamento readDatiComm()
	{
		Commissionamento comm = new Commissionamento();
		comm.setIdCommissionamento((Principale.config).getIdCommissionamento());
		comm.setNomeAgenzia(nome_agenzia.getText());
		comm.setNumStanza(num_stanza.getText());
		comm.setInizioComm(DateUtils.convertDate(inizio_comm.getText()));
		comm.setFineComm(DateUtils.convertDate(fine_comm.getText()));
		comm.setScadenzaComm(DateUtils.convertDate(scadenza_comm.getText()));
		comm.setNumTel(num_tel_agenzia.getText());
		return comm;
	}

	void writeDatiComm(Commissionamento comm)
	{
		nome_agenzia.setText(comm.getNomeAgenzia());
		num_stanza.setText(comm.getNumStanza());
		inizio_comm.setText(DateUtils.giveStringOfDate(comm.getInizioComm()));							
		fine_comm.setText(DateUtils.giveStringOfDate(comm.getFineComm()));
		scadenza_comm.setText(DateUtils.giveStringOfDate(comm.getScadenzaComm()));
		num_tel_agenzia.setText(comm.getNumTel());
	}

	protected boolean errori()
	{
		Frame msg;
		
		if (!((num_stanza.getText()).length() <= 4))
		{
			msg = new MessageDialog(this, " Il numero della stanza deve avere al piu' 4 caratteri! ");
			return true;
		}
		if ((num_stanza.getText()).equals(""))
		{
			msg = new MessageDialog(this, " Manca il numero di stanza! ");
			return true;
		}
		if ((nome_agenzia.getText()).equals("")) 
		{
			msg = new MessageDialog(this, " Manca il nome dell'agenzia! ");
			return true;
		}
		if (!((nome_agenzia.getText()).length() <= 20))
		{
			msg = new MessageDialog(this, " Il nome dell'agenzia deve avere al piu' 20 caratteri! ");
			return true;
		}
		if ((scadenza_comm.getText()).equals(""))
		{
			msg = new MessageDialog(this, " Manca la data di scadenza! ");
			return true;
		}
		if (!(DateUtils.dataCorretta(scadenza_comm.getText()) ))
		{
			msg = new MessageDialog(this, " La data di scadenza e' non corretta! ");
			return true;
		}
		if ((num_tel_agenzia.getText()).equals(""))
		{
			msg = new MessageDialog(this, " Manca il numero di telefono! ");
			return true;
		}
		if (!((num_tel_agenzia.getText()).length() <= 16))
		{
			msg = new MessageDialog(this, " Il numero di telefono deve avere al piu' 16 caratteri! ");
			return true;
		}
		return false;
	}

	void cleanFields()
	{
	  nome_agenzia.setText("");
      num_stanza.setText("");
      inizio_comm.setText("");
      fine_comm.setText("");
      scadenza_comm.setText("");
      num_tel_agenzia.setText("");
	}

	// procedura utilizzata da Modifica e Cancella
	void creaLista()
	{
		Frame msg;
		
		Commissionamento c;
		L = (Principale.db).foundCommissionamenti(new String(nome_agenzia.getText()));
		if (L != null)
		{
			if (lista.getItemCount() > 0)
				lista.removeAll();
			if (!L.isEmpty())
			{
				panel5.setVisible(true);
				pack();
				for (int i = 1; i<=L.length(); i++)
					lista.addItem((L.getCommissionamento(i)).toString());
			}
			else
				msg = new AvvisoDialog(this, "Agenzia "+nome_agenzia.getText()+" non trovata!");
		}
		else
			msg = new MessageDialog(this, "Problemi con il database!");
	}
	
	void aggiornaDisp(Commissionamento comm, char tipo)
	{
		Disponibilita disp_da_cambiare = new Disponibilita();
		ListaDisponibilita elenco_disp = (Principale.db).elencoDisponibilita();
		if (elenco_disp == null)
		{
			Frame msg = new MessageDialog(this, " Problemi con il database nella lettura delle disponibilita'! ");
			return;
		}
		
		disp_da_cambiare = Utils.getDispOfRoom(elenco_disp,comm.getNumStanza(),true);
		if (tipo == Flag.COMMISSIONATA)
			disp_da_cambiare.setDisponibilita(comm.getInizioComm(), comm.getFineComm(), tipo, Flag.DISPONIBILE);
		else
			disp_da_cambiare.setDisponibilita(comm.getInizioComm(), comm.getFineComm(), tipo, Flag.COMMISSIONATA);			
		(Principale.db).changeDisponibilita( disp_da_cambiare.getNumStanza(), 
											disp_da_cambiare.getDispAnnoCorr(),
											disp_da_cambiare.getDispAnnoProx() );
	}
}
