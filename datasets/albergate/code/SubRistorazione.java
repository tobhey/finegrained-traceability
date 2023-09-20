package interfacce;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import common.utility.*;
import AlberGate;

public class SubRistorazione extends Frame
{
   	Label label;
   	Panel panel0, panel1;
   	Button Calcolo, Annulla;
   	GridBagLayout gridbag=new GridBagLayout();
	Frame p = new Frame();
	MascheraContoRistorazione rist;

   public SubRistorazione()
   {
      super("Ristorazione");
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
      Immagine  figura = new Immagine("ristorazione.jpg");
      Etichetta etich = new Etichetta("Ristorazione.gif");
      Utils.constrain(panel0,etich, 0, 0, 2, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.CENTER, 0.5, 1.0,0,0,0,0);
      Utils.constrain(panel0,figura, 2, 0, 10, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.CENTER, 0.5, 1.0,0,0,0,0);
       
      panel1 = new Panel();
      panel1.setLayout(new GridLayout(2,1));
      Calcolo = new Button(" Calcolo conto ");
      Annulla = new Button(" Indietro ");
      panel1.add(Calcolo);
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

      Calcolo.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
			startCalcolo();
         }
      });
   }
   
   void startCalcolo()
   {
   		this.setEnabled(false);
		rist = new MascheraContoRistorazione(this);
		rist.setVisible(true);
		rist.padre = this;
   }
}
