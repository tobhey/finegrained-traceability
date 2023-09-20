package interfacce;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;
import common.utility.*;
import moduli.*;

public class SubSoggiorno extends Frame
{
   Label label;
   Panel panel0, panel1;
   Button Arrivo, Cambio, Addebito, Telefonate, Calcolo, Ricerca, BeniServizi,Annulla;
   GridBagLayout gridbag=new GridBagLayout();
   Frame p = new Frame();	
   RicercaPrenotazione ric_pren;
   Frame figlio;

   public SubSoggiorno()
   {
      super("Soggiorno");
      setup();
      init();
      pack();
   }

   void setup()
   {
      this.setFont(ConfigurazioneSistema.font_sub);
      panel0=new Panel();
      panel0.setLayout(gridbag);
      panel0.setBackground(Color.white);
      
      Random rnd = new Random();
      int num;
      if ((num = (rnd.nextInt() % 5) ) < 0)
      	  num = num * -1;
      num ++;	   
      String file_name = new String("servizi"+num+".jpg");
      Immagine  figura = new Immagine(file_name);
      Etichetta etich = new Etichetta("Soggiorno.gif");
      Utils.constrain(panel0,etich, 0, 0, 2, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.CENTER, 0.5, 1.0,0,0,0,0);
      Utils.constrain(panel0,figura, 2, 0, 10, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.CENTER, 0.5, 1.0,0,0,0,0);
       
      panel1 = new Panel();
      panel1.setLayout(new GridLayout(8,1));
      Arrivo = new Button(" Arrivo clienti ");
      Cambio = new Button(" Cambio stanza ");
      Addebito = new Button(" Addebito spese ");
      Telefonate = new Button(" Telefonate ");
      Calcolo = new Button(" Calcolo conto ");
      Ricerca = new Button(" Ricerca... ");
      BeniServizi = new Button(" Beni & Servizi ");
      Annulla = new Button(" Indietro ");
      panel1.add(Arrivo);
      panel1.add(Addebito);
      panel1.add(BeniServizi);
      panel1.add(Telefonate);
      panel1.add(Cambio);
      panel1.add(Calcolo);
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
            p.setEnabled(true);
         }
      });
   
   	Arrivo.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            insSoggiornante();
         }
      });
    
    Calcolo.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            calcoloConto();
         }
      });
      
   	Addebito.addActionListener(new ActionListener()
    {
     	public void actionPerformed(ActionEvent e)
        {
            addebitaBeneServizio();
        }
   	});
   	
   	BeniServizi.addActionListener(new ActionListener()
    {
     	public void actionPerformed(ActionEvent e)
        {
            visitaBeneServizio();
        }
   	});
   	
   	Telefonate.addActionListener(new ActionListener()
    {
     	public void actionPerformed(ActionEvent e)
        {
            ricercaTelefonate();
        }
   	});
   	
   	Cambio.addActionListener(new ActionListener()
    {
     	public void actionPerformed(ActionEvent e)
        {
            cambiaStanza();
        }
   	});  
      
   	Ricerca.addActionListener(new ActionListener()
    {
     	public void actionPerformed(ActionEvent e)
        {
            startRicerca();
        }
   	});  
   }

	void insSoggiornante()
	{
		this.setEnabled(false);
		ric_pren = new RicercaPrenotazione("Arrivo clienti",3);
		ric_pren.setVisible(true);
		ric_pren.padre = this;
	}
	
	void addebitaBeneServizio()
	{
		figlio = new MascheraAddebiti(this);
	}
	
	void visitaBeneServizio()
	{
		figlio = new RicercaAddebiti(this);
	}
	
	void ricercaTelefonate()
	{
		figlio = new RicercaTelefonate(this);
	}
	
	void calcoloConto()
	{
		figlio = new MascheraCalcoloConto(this);
		figlio.setVisible(true);
	}
	
	void cambiaStanza()
	{
		figlio = new MascheraCambio(this);
	}

	void startRicerca()
	{
		figlio = new MascheraRicercaSoggiornante(this);
	}
}
