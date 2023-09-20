package interfacce;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import common.utility.*;

public class SubStanze extends Frame
{
   Label label;
   Panel panel0, panel1;
   Button Inserimento, Modifica, Cancellazione, Ricerca, Annulla;
   GridBagLayout gridbag=new GridBagLayout();
   SubGestione s = new SubGestione();
   InserisciStanze call_1;
   ModificaStanze call_2;
   CancellaStanze call_3;
   VediStanza call_4;
   
   public SubStanze()
   {
      super("Gestione delle stanze");
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
      Immagine  figura = new Immagine("camera1.jpg");
      Etichetta etich = new Etichetta("Stanze.gif");
      Utils.constrain(panel0,etich, 0, 0, 2, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.CENTER, 0.5, 1.0,0,0,0,0);
      Utils.constrain(panel0,figura, 2, 0, 10, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.CENTER, 0.5, 1.0,0,0,0,0);
       
      panel1 = new Panel();
      panel1.setLayout(new GridLayout(5,1));
      Inserimento = new Button("Inserimento");
      Modifica = new Button("Modifica");
      Cancellazione = new Button("Cancellazione");
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
            insStanza();
         }
      });

      Modifica.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            modStanza();
         }
      });

      Cancellazione.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            cancStanza();
         }
      });
      
      Ricerca.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            vediStanza();
         }
      });
   }

	void insStanza()
	{
		this.setEnabled(false);
		call_1 = new InserisciStanze();
		call_1.setVisible(true);
		call_1.stanze = this;
	}

	void modStanza()
	{
		this.setEnabled(false);
		call_2 = new ModificaStanze();
		call_2.setVisible(true);
		call_2.stanze = this;
	}

	void cancStanza()
	{
		this.setEnabled(false);
		call_3 = new CancellaStanze();
		call_3.setVisible(true);
		call_3.stanze = this;
	}
	
	void vediStanza()
	{
		this.setEnabled(false);
		call_4 = new VediStanza();
		call_4.setVisible(true);
		call_4.stanze = this;
	}
}
