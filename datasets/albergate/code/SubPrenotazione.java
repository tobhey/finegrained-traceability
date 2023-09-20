package interfacce;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import common.utility.*;

public class SubPrenotazione extends Frame
{
   Label label;
   Panel panel0, panel1;
   Button Inserimento, Modifica, Cancellazione, Ricerca, Annulla ;
   GridBagLayout gridbag = new GridBagLayout();
   Frame p = new Frame();
   static public MascheraRicercaStanza ric_stanza = null;
   static public RicercaPrenotazione ric_pren = null;
   	
   
   public SubPrenotazione()
   {
      super("Prenotazione");
      setup();
      init();
      pack();
   }

   void setup()
   {
      Immagine  figura = new Immagine("camera2.jpg");
      Etichetta etich = new Etichetta("Prenotazioni.gif");
      this.setFont(ConfigurazioneSistema.font_sub);
      panel0=new Panel();
      panel0.setLayout(gridbag);
      panel0.setBackground(Color.white);
      
      Utils.constrain(panel0,etich, 0, 0, 2, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.CENTER, 0.5, 1.0,0,0,0,0);
      Utils.constrain(panel0,figura, 2, 0, 10, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.CENTER, 0.5, 1.0,0,0,0,0);
       
      panel1 = new Panel();
      panel1.setLayout(new GridLayout(5,1));
      Inserimento = new Button(" Inserimento ");
      Modifica = new Button(" Modifica ");
      Cancellazione = new Button(" Cancellazione ");
      Ricerca = new Button(" Ricerca... ");
      Annulla = new Button(" Indietro ");
      panel1.add(Inserimento);
      panel1.add(Modifica);
      panel1.add(Cancellazione);
	  panel1.add(Ricerca);
      panel1.add(Annulla);
      this.setLayout(gridbag);
      Utils.constrain(this,panel0, 0, 0, 12, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.WEST, 1.0, 1.0, 0, 0, 0, 0);
      Utils.constrain(this,panel1, 12, 0, 1, 12,GridBagConstraints.BOTH,
                     GridBagConstraints.WEST, 1.0, 1.0, 0, 0, 0, 0);


   }

   void init()
   {
      Annulla.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            dispose();
            if (Principale.call_list != null)
            	(Principale.call_list).dispose();
            p.setEnabled(true);
         }
      });
      
      Inserimento.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            insPrenotazione();
         }
      });
      
      Modifica.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            modPrenotazione();
         }
      });
      
      Cancellazione.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            cancPrenotazione();
         }
      });
   	  
   	  Ricerca.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            vediPrenotazione();
         }
      });		
   }

	void insPrenotazione()
	{
		this.setEnabled(false);
		ric_stanza = new MascheraRicercaStanza("Ricerca stanze per prenotazioni",2);
		ric_stanza.setVisible(true);
		ric_stanza.padre = this;
	}
	
	void modPrenotazione()
	{
		this.setEnabled(false);
		ric_pren = new RicercaPrenotazione("Ricerca prenotazione per modifica",1);
		ric_pren.setVisible(true);
		ric_pren.padre = this;
	}
	
	void cancPrenotazione()
	{
		this.setEnabled(false);
		ric_pren = new RicercaPrenotazione("Ricerca prenotazione per cancellazione",2);
		ric_pren.setVisible(true);
		ric_pren.padre = this;
	}
	
	void vediPrenotazione()
	{
		this.setEnabled(false);
		ric_pren = new RicercaPrenotazione("Ricerca prenotazione per visualizzazione",4);
		ric_pren.setVisible(true);
		ric_pren.padre = this;
	}
}
