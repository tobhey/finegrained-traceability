package interfacce;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import common.utility.*;

public class SubGestione extends Frame
{
   	Label label;
   	Panel panel0, panel1;
   	Button Stanze, BeniServizi, Configurazione, Annulla ;
   	GridBagLayout gridbag=new GridBagLayout();
  	Frame p1 = new Frame();
   	Password pass = new Password();
   	SubStanze call_stanze;	
   	SubBeniServizi call_beniservizi;
   	ConfigurazioneSistema call_config;
   
   public SubGestione()
   {
      super("Gestione albergo");
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
      Immagine  figura = new Immagine("alb1.jpg");
      Etichetta etich = new Etichetta("Gestione.gif");
      Utils.constrain(panel0,etich, 0, 0, 2, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.CENTER, 0.5, 1.0,0,0,0,0);
      Utils.constrain(panel0,figura, 2, 0, 10, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.CENTER, 0.5, 1.0,0,0,0,0);
       
      panel1 = new Panel();
      panel1.setLayout(new GridLayout(4,1));
      Stanze = new Button("Stanze");
      BeniServizi = new Button(" Beni & Servizi ");
      Configurazione = new Button(" Configurazione ");
      Annulla = new Button(" Indietro ");
      panel1.add(Stanze);
      panel1.add(BeniServizi);
      panel1.add(Configurazione);
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
            pass.setEnabled(true);
            pass.p.setEnabled(true);
         }
      });
      
      Stanze.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            gestioneStanze();
         }
      });
      
      BeniServizi.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            gestioneBeniServizi();
         }
      });
      
      Configurazione.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            startConfigurazione();
         }
      });
   }

	void gestioneStanze()
	{
		this.setEnabled(false);
		call_stanze = new SubStanze();
		call_stanze.setVisible(true);
		call_stanze.s = this;
	}
	
	void gestioneBeniServizi()
	{
		this.setEnabled(false);
		call_beniservizi = new SubBeniServizi();
		call_beniservizi.setVisible(true);
		call_beniservizi.s = this;
	}
	
	void startConfigurazione()
	{
		this.setEnabled(false);
		call_config = new ConfigurazioneSistema(Principale.config, false, Principale.db);
		call_config.setVisible(true);
		call_config.padre = this;
	}
}
