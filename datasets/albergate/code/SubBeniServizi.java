package interfacce;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import common.utility.*;
import java.util.Random;

public class SubBeniServizi extends Frame
{
   Label label;
   Panel panel0, panel1;
   Button Inserimento, Modifica ,Cancellazione, Ricerca, Annulla ;
   GridBagLayout gridbag=new GridBagLayout();
   SubGestione s = new SubGestione();
   CancellaBeniServizi call_3;
   ModificaBeniServizi call_2;
   InserisciBeniServizi call_1;
   VediBeniServizi call_4;
   
   public SubBeniServizi()
   {
      super("Gestione dei Beni&Servizi");
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
      //Caricamento di un'immagine scelta a caso tra quelle disponibili
      Random rnd = new Random();
      int num;
      if ((num = (rnd.nextInt() % 5) ) < 0)
      	  num = num * -1;
      num ++;	   
      String file_name = new String("servizi"+num+".jpg");
      Immagine  figura = new Immagine(file_name);
      Etichetta etich = new Etichetta("Beni&Servizi.gif");
      Utils.constrain(panel0,etich, 0, 0, 2, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.CENTER, 0.5, 1.0,0,0,0,0);
      Utils.constrain(panel0,figura, 2, 0, 10, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.CENTER, 0.5, 1.0,0,0,0,0);
       
      panel1 = new Panel();
      panel1.setLayout(new GridLayout(5,1));
      Inserimento = new Button(" Inserimento ");
      Modifica = new Button("Modifica");
      Cancellazione = new Button(" Cancellazione ");
      Ricerca = new Button("Ricerca...");
      Annulla = new Button("Indietro");
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
            s.setEnabled(true);
         }
      });
      
      Inserimento.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            insBeneServizio();
         }
      });

      Modifica.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            modBeneServizio();
         }
      });

      Cancellazione.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            cancBeneServizio();
         }
      });
      
      Ricerca.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            vediBeneServizio();
         }
      });
   }

	void insBeneServizio()
	{
		this.setEnabled(false);
		call_1 = new InserisciBeniServizi("Inserimento di beni e servizi");
		call_1.padre = this;
		call_1.setVisible(true);
	}  

	void modBeneServizio()
	{
		this.setEnabled(false);
		call_2 = new ModificaBeniServizi();
		call_2.padre = this;
		call_2.setVisible(true);
	}  

	void cancBeneServizio()
	{
		this.setEnabled(false);
		call_3 = new CancellaBeniServizi();
		call_3.padre = this;
		call_3.setVisible(true);
	}
	
	void vediBeneServizio()
	{
		this.setEnabled(false);
		call_4 = new VediBeniServizi();
		call_4.padre = this;
		call_4.setVisible(true);
	}  
}
