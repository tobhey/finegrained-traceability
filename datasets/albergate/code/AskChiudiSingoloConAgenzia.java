package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import interfacce.MascheraVisualizzazioneConto;

public class AskChiudiSingoloConAgenzia extends Frame
{
   	Button button1, button2, button3;
   	MascheraCalcoloConto padre;
   	Panel panel1, panel2;
   	Label message;
   	GridBagLayout gridbag = new GridBagLayout();
   	
   	public AskChiudiSingoloConAgenzia(MascheraCalcoloConto parent)
   	{
      	super("Cosa devo fare?");
      	padre = parent;
      	padre.setEnabled(false);
      	setupPanel();
      	init();
      	pack();
      	setVisible(true);
   }

	void setupPanel()
	{
      	message = new Label("Calcolare il conto di un soggiornante venuto con agenzia implica cambiare la sua data di partenza.");
      	button1 = new Button(" Annulla ");
      	button2 = new Button(" Cambia data ");        
        this.setFont(ConfigurazioneSistema.font_titolo);
        panel1 = new Panel();
        panel1.setLayout(gridbag);
        Utils.constrain(panel1, message, 0, 0, 4, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 0, 0, 0, 0);
      	//Creo il pannello in basso con due pulsanti                  
        panel2 = new Panel();
        panel2.setLayout(gridbag);
        Utils.constrain(panel2, button1, 0, 0, 1, 1, GridBagConstraints.NONE,
        	            GridBagConstraints.CENTER, 0.0, 0.0, 0, 0, 0, 15);
            Utils.constrain(panel2, button2, 1, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 0, 15, 0, 0);             
        //Attacco i pannelli al frame
        this.setLayout(gridbag);
        Utils.constrain(this, panel1, 0, 1, 4, 2, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5);
        Utils.constrain(this, panel2, 0, 14, 4, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5); 
	}
   
   
   public void init()
   {
      button1.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
         	startAzione();
         }
      });
      
      button2.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
			padre.startChiudiSingoloConAgenzia();
			startAzione();
         }
      });
   }
   
   void startAzione()
   {
       	setVisible(false);
        dispose();
        padre.setEnabled(true);
   }
}
