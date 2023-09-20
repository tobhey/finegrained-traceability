package interfacce;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import common.utility.*;

public class SubStorico extends Frame
{
   Label label;
   Panel panel0, panel1;
   Button Ricerca, Annulla;
   GridBagLayout gridbag = new GridBagLayout();
   Frame p = new Frame();
   Frame figlio;

   public SubStorico()
   {
      super("Storico Clienti");
      setup();
      init();
      pack();
      //setVisible(true);
   }

   void setup()
   {
      this.setFont(ConfigurazioneSistema.font_sub);
      panel0=new Panel();
      panel0.setLayout(gridbag);
      panel0.setBackground(Color.white);
      Immagine  figura = new Immagine("servizi5.jpg");
      Etichetta etich = new Etichetta("Storico.gif");
      Utils.constrain(panel0,etich, 0, 0, 2, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.CENTER, 1.0, 1.0,0,0,0,0);
      Utils.constrain(panel0,figura, 2, 0, 10, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.CENTER, 1.0, 1.0,0,0,0,0);
       
      panel1 = new Panel();
      panel1.setLayout(new GridLayout(2,1));
      Ricerca = new Button(" Ricerca ");
      Annulla = new Button(" Annulla ");
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

      Ricerca.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            start_ricerca();
         }
      });
   }

   void start_ricerca()
   {
      figlio = new MascheraRicercaStorico(this);
   }
}
