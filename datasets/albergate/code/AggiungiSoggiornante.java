package interfacce;
import java.awt.*;
import java.awt.event.*;
import moduli.*;
import common.utility.*;
import common.def.*;
import interfacce.*;
import java.util.Date;

public class AggiungiSoggiornante extends MascheraSoggiorno
{
   	Prenotazione prenotazione;
   	int num_sogg;
	Stanza stanza; 
  	
  	public AggiungiSoggiornante(RicercaPrenotazione parent, Prenotazione p, Stanza s, int num)
   	{
      	super("Inserimento dati del soggiornante","Aggiungi agli altri", p.getPensionamento(), Flag.ADULTO);
      	padre = parent;
      	stanza = s;
      	num_sogg= num;
      	prenotazione = p;
      	inizializza();
   	}

   public void inizializza()
   {
      testo1.setText(stanza.getNumStanza());
      testo1.setEditable(false);
      testo13.setText(DateUtils.giveStringOfDate(new Date()));
      testo13.setEditable(false);
      testo14.setText(DateUtils.giveStringOfDate(prenotazione.getFineSogg()));
      testo14.setEditable(false);
      
      Conferma.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
			startConferma();
         }
      });
      
    }

	void startConferma()
	{
		Frame msg;
		
        if (!errori())
        {
			Soggiornante sogg = readDatiSogg(prenotazione, num_sogg+1);
			sogg.setInizioSogg(new Date());
			sogg.setFineSogg(prenotazione.getFineSogg());
			sogg.setNumStanza(stanza.getNumStanza());
			int j;
          	if ( (j = (Principale.db).writeSoggiornante(sogg)) == DataBase.OK )
          	{     //	Modifico la prenotazione relativa
				if (stanza.getPostiLetto() > num_sogg)
					prenotazione.setNumPers(num_sogg+1);
				else
					if ( (stanza.getPostiLetto() == num_sogg) && (stanza.getDispLettoAgg() == Const.SI) )
					 	prenotazione.setRichLettoAgg(Const.SI);
							
				(Principale.db).changePrenotazione(prenotazione.getIdPrenotazione(), prenotazione.getNumStanza(), 
														prenotazione.getNumPers(),prenotazione.getNome(),prenotazione.getCognome(), 
														prenotazione.getNumTel(), prenotazione.getInizioSogg(), prenotazione.getFineSogg(), prenotazione.getDataPren(),
														prenotazione.getCaparra(), prenotazione.getRichLettoAgg(), prenotazione.getPensionamento(),
														prenotazione.getTramiteAgenzia(), prenotazione.getNomeAgenzia(), prenotazione.getRichParticolari());	
				msg = new AvvisoLocale(this, " Nuovo soggiornante registrato! ");
           	}
			else
				msg = new MessageDialog(this, "Problemi con il database: "+DataBase.strErrore(j));
       	}
	}
}

class AvvisoLocale extends Frame
{
   Button OK;
   AggiungiSoggiornante padre;

   public AvvisoLocale(AggiungiSoggiornante parent, String testo)
   {
      super("Avviso");
      padre = parent;
      padre.setEnabled(false);
	  this.setFont(ConfigurazioneSistema.font_titolo);
      Panel textPanel = new Panel();
      Panel buttonPanel = new Panel();
      textPanel.add(new Label(testo));
      OK = new Button("OK");
      buttonPanel.add(OK);
      add("North",textPanel);
      add("Center",buttonPanel);
      init();
      pack();
      //setSize(250,100);
      setVisible(true);
   }

   public void init()
   {
      OK.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
         	setVisible(false);
			dispose();
			padre.dispose();
			padre.padre.setEnabled(true);
         }
      });
   }
}
