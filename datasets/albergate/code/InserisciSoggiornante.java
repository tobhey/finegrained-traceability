package interfacce;
import java.awt.*;
import java.awt.event.*;
import moduli.*;
import common.utility.*;
import common.def.*;
import interfacce.*;
import java.util.Date;

public class InserisciSoggiornante extends MascheraSoggiorno
{
   Prenotazione prenotazione;
   int counter_ins = 1;
   int counter_tot;
   public InserisciSoggiornante(RicercaPrenotazione parent, Prenotazione p)
   {
      super("Inserimento dati", "Inserisci", p.getPensionamento(), Flag.ADULTO);
      padre = parent;
      padre.setEnabled(false);
      prenotazione = p;
      inizializza();
   }

   public void inizializza()
   {
      testo1.setText(prenotazione.getNumStanza());
      testo1.setEditable(false);
      testo2.setText(prenotazione.getCognome());
      testo3.setText(prenotazione.getNome());
      testo7.setText(prenotazione.getNumTel());
      testo13.setText(DateUtils.giveStringOfDate(prenotazione.getInizioSogg()));
      testo13.setEditable(false);
      testo14.setText(DateUtils.giveStringOfDate(prenotazione.getFineSogg()));
      testo14.setEditable(false);
      counter_tot = prenotazione.getNumPers();
      if (prenotazione.getRichLettoAgg() == Const.SI)
      	counter_tot++;
      changeTitle(counter_ins, counter_tot);
      
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
		
        if (!erroriLocale())
        {
			Soggiornante sogg = readDatiSogg(prenotazione, counter_ins);
			int j;
          	if ( (j = (Principale.db).writeSoggiornante(sogg)) == DataBase.OK )
          	{
				Annulla.setEnabled(false);
				cleanFields();
				if (counter_ins == counter_tot)
				{
					aggiornaDisp(sogg, Flag.OCCUPATA );
					dispose();
					padre.creaLista(1);
					padre.setEnabled(true);
				}
				counter_ins++;
				changeTitle(counter_ins, counter_tot);
           	}
			else
				msg = new MessageDialog(this, "Problemi con il database: "+DataBase.strErrore(j));
       	}
	}
	
	boolean erroriLocale()
	{
		if (!errori())
		{
			if (counter_ins == 1)
			{
				Frame msg;
				if ( (testo2.getText()).equals("") )
				{
					msg = new MessageDialog(this, " Per il primo soggiornante inserito deve essere digitato il cognome! ");
					return true;
				}
				if ( (testo3.getText()).equals("") )
				{
					msg = new MessageDialog(this, " Per il primo soggiornante inserito deve essere digitato il nome! ");
					return true;
				}
				if ( (testo6.getText()).equals("") )
				{
					msg = new MessageDialog(this, " Per il primo soggiornante inserito deve essere digitato il numero di documento! ");
					return true;
				}
				if ( (testo7.getText()).equals("") )
				{
					msg = new MessageDialog(this, " Per il primo soggiornante inserito deve essere digitato il numero telefonico! ");
					return true;
				}
				if ( (testo8.getText()).equals("") )
				{
					msg = new MessageDialog(this, " Per il primo soggiornante inserito deve essere digitato l'indirizzo! ");
					return true;
				}
				if ( (testo9.getText()).equals("") )
				{
					msg = new MessageDialog(this, " Per il primo soggiornante inserito deve essere digitato il C.A.P.! ");
					return true;
				}
				if ( (testo10.getText()).equals("") )
				{
					msg = new MessageDialog(this, " Per il primo soggiornante inserito deve essere digitato il comune! ");
					return true;
				}
				if ( (testo11.getText()).equals("") )
				{
					msg = new MessageDialog(this, " Per il primo soggiornante inserito deve essere digitata la citta! ");
					return true;
				}
				if ( (testo12.getText()).equals("") )
				{
					msg = new MessageDialog(this, " Per il primo soggiornante inserito deve essere digitata la nazione! ");
					return true;
				}
			}
			return false;
		}
		return true;
	}
}
