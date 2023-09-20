package interfacce;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import common.def.*;
import common.utility.Utils;
import common.utility.DateUtils;
import moduli.*;

public class MascheraCambio extends Frame
{
	TextField stanza1, stanza2;
	Button Annulla, Conferma, Ricerca;
	Label etichetta1, etichetta2;
	Panel panel1, panel2;
	GridBagLayout gridbag = new GridBagLayout();
	Prenotazione p;
	Frame padre;
	MascheraRicercaStanza figlio;
	
	public MascheraCambio(Frame parent)
	{
      super("Cambio di stanza");
      padre = parent;
      padre.setEnabled(false);
      setup();
      init();
      pack();
      this.setVisible(true);
	}

	void setup()
	{
	  this.setFont(ConfigurazioneSistema.font_base);
      stanza1 = new TextField("", 4);
      stanza2 = new TextField("", 4);
      stanza2.setEditable(false);
      etichetta1 = new Label("Stanza attuale");
      etichetta2 = new Label("     Nuova stanza");
      Annulla = new Button(" Annulla ");
      Ricerca = new Button(" Ricerca ");
      Conferma = new Button("Conferma");
      Conferma.setEnabled(false);
				
      panel1 = new Panel();
      panel1.setLayout(gridbag);
      Utils.constrain(panel1, etichetta1, 0, 0, 1, 1, GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);
      Utils.constrain(panel1, stanza1,    1, 0, 1, 1, GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);
      Utils.constrain(panel1, etichetta2, 2, 0, 1, 1, GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);
      Utils.constrain(panel1, stanza2,    3, 0, 1, 1, GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);

      panel2 = new Panel();
      panel2.setLayout(gridbag);
      Utils.constrain(panel2, Annulla,  0, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 0, 0, 0, 0);
      Utils.constrain(panel2, Ricerca,  1, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 0, 0, 0, 0);
      Utils.constrain(panel2, Conferma, 2, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 0, 0, 0, 0);                  

      
      this.setLayout(gridbag);
      Utils.constrain(this, panel1, 0, 0, 1, 1, GridBagConstraints.BOTH,
                        GridBagConstraints.WEST, 1.0, 1.0, 10, 10, 10, 10);
      Utils.constrain(this, panel2, 0, 1, 1, 1, GridBagConstraints.BOTH,
                        GridBagConstraints.EAST, 1.0, 1.0, 10, 10, 10, 10);
	}

	public void init()
	{
      
      
      Annulla.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            dispose();
            padre.setEnabled(true);
         }
      });

      Conferma.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
			startConferma();
         }
      });
      
      Ricerca.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
			if (noErrors()) 
			{	
				//stanza1.setEditable(false);
				startRicerca();
         	}
         }
      });
	}
   
	void startConferma()
	{
		Frame msg;
		ListaDisponibilita elenco_disp = (Principale.db).elencoDisponibilita();
		if (elenco_disp == null)
		{
			msg = new MessageDialog(this, " Problemi con il database nella lettura delle disponibilita'! ");
			return;
		}
		else
		{   
			if (ctrlPosti())
			{
				int ris;
				if ( (ris = (Principale.db).changeStanzaSoggiornanti(stanza1.getText(), stanza2.getText())) > 0 )
				{
					if ( (ris = (Principale.db).changeStanzaAddebiti(stanza1.getText(), stanza2.getText())) < 0 )
							msg = new MessageDialog(this, "Problemi con il database: "+DataBase.strErrore(ris));
					if ( (ris = (Principale.db).changeStanzaTelefonate(stanza1.getText(), stanza2.getText())) < 0 )
							msg = new MessageDialog(this, "Problemi con il database: "+DataBase.strErrore(ris));
					Utils.aggiornaDisp(elenco_disp,stanza1.getText(),p.getInizioSogg(),p.getFineSogg(),Flag.DISPONIBILE,Flag.OCCUPATA,true);		
					Utils.aggiornaDisp(elenco_disp,stanza2.getText(),p.getInizioSogg(),p.getFineSogg(),Flag.OCCUPATA,Flag.DISPONIBILE,true);		
					Utils.aggiornaDisp(elenco_disp,stanza2.getText(),p.getInizioSogg(),p.getFineSogg(),Flag.OCCUPATA,Flag.COMMISSIONATA,true);		
				
					Utils.aggiornaDisp(elenco_disp,stanza2.getText(),p.getInizioSogg(),p.getFineSogg(),Flag.OCCUPATA,Flag.DISPONIBILE,true);		
					Utils.aggiornaDisp(elenco_disp,stanza2.getText(),p.getInizioSogg(),p.getFineSogg(),Flag.OCCUPATA,Flag.COMMISSIONATA,true);		
					if (p.getTramiteAgenzia() == Const.SI)
					{	
						Utils.restoreCommissioni(stanza1.getText());
					}
					msg = new AvvisoDialog(this,"La stanza e' stata cambiata e tutti i dati riferiti ad essa aggiornati");
					Conferma.setEnabled(false);
				}
				else
					if ( (ris != 0) && (!(ris == DataBase.DONT_EXISTS_STANZA) ) )
						msg = new MessageDialog(this, "Problemi con il database: "+DataBase.strErrore(ris));
			}
		}
	}
   
	boolean ctrlPosti()
	{
		Frame msg;
		ListaSoggiornanti L_sogg = (Principale.db).foundSoggiornanti(stanza1.getText(),false);
		Stanza room2 = (Principale.db).readStanza(stanza2.getText());
		if ( !(room2 ==null) && !(L_sogg == null))
			if	(L_sogg.length() > contaPosti(room2))
			{	
				msg = new AvvisoDialog(this,"La stanza scelta e' troppo piccola per "+L_sogg.length()+" persone"); 			
				return false;
			}
			else 
			 return true;
		else
		{
		 	msg = new MessageDialog(this," Problemi con il database! ");
			return false;
		}
	}
	
	int contaPosti(Stanza s)
	{
		int c = s.getPostiLetto();
		if (s.getDispLettoAgg() == Const.SI)
			c++;
		return c;	
	}
	
	void startRicerca()
	{
		this.setEnabled(false);
		figlio = new MascheraRicercaStanza("Ricerca di stanze disponibili per cambio stanza",3);
		Soggiornante sogg = (Principale.db).readSoggiornante(stanza1.getText(),1);
		p = (Principale.db).readPrenotazione(sogg.getIdPrenotazione());
		if ( !(p == null))
		{	
			figlio.data_inizio.setText(DateUtils.parseDate(DateUtils.giveStringOfDate(new Date())));
			figlio.data_inizio.setEditable(false);
			figlio.data_fine.setText(DateUtils.parseDate(DateUtils.giveStringOfDate(p.getFineSogg())));
			figlio.save_date = p.getFineSogg();
			if (p.getTramiteAgenzia() == Const.SI)
			{
				figlio.con_ag.setState(true);
				figlio.con_ag.setEnabled(false);
				figlio.nome_ag.setText(p.getNomeAgenzia());
				figlio.nome_ag.setEditable(false);
			}	
			figlio.setVisible(true);
			figlio.father = this;
		}
	}
	
	boolean noErrors()
	{
		Frame msg;
		Disponibilita disp = new Disponibilita();
		ListaDisponibilita elenco_disp = (Principale.db).elencoDisponibilita();
		
		if (elenco_disp == null)
		{
			msg = new MessageDialog(this, " Problemi con il database nella lettura delle disponibilita'! ");
			return false;
		}
		
		if ((stanza1.getText().equals("")) ) 
		{
			msg = new AvvisoDialog(this," Inserire il numero della stanza da cambiare! ");
			return false;
		}
		else
		{
			if( (Principale.db).readStanza(stanza1.getText()) == null )
			{
				msg = new MessageDialog(this," La stanza inserita e' inesistente! ");
				return false;
			}
			else
			{	
				disp = Utils.getDispOfRoom(elenco_disp, stanza1.getText(), true);
				if (disp != null)
				{
					if (  disp.getStatusGiorno(DateUtils.dataTogiorni(new Date()),Const.ANNO_CORRENTE) != Flag.OCCUPATA )
					{	
						msg = new MessageDialog(this," La stanza inserita non e' attualmente occupata! ");
						return false;
					}
				}
			}
		}
		return true;	
	}
}
