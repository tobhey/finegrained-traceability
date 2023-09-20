package interfacce;
import java.awt.*;
import java.awt.event.*;
import moduli.*;
import common.utility.*;
import common.def.*;
import java.util.Date;

public class VediSoggiornante extends MascheraSoggiorno
{
	Frame padre;   
   	public VediSoggiornante(Frame p, Soggiornante sogg)
   	{
      	super("Visualizzazione dei dati del soggiornante"," Fine ", sogg.getPensionamento(), sogg.getStatus() );
      	padre=p;
      	padre.setEnabled(false);
      	inizializza(sogg);
      	pack();
      	this.setVisible(true);
   	}

   public void inizializza(Soggiornante sogg)
   {
      panel3.remove(Annulla);
      disableFields();
      writeDatiSogg(sogg);
      
      
      Conferma.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            dispose();
            padre.setEnabled(true);
         }
      });
	}
	
	void disableFields()
	{
		testo1.setEditable(false);
		testo2.setEditable(false);
		testo3.setEditable(false);
		testo4.setEditable(false);
		testo5.setEditable(false);
		testo6.setEditable(false);
		testo7.setEditable(false);
		testo8.setEditable(false);
		testo9.setEditable(false);
		testo10.setEditable(false);
		testo11.setEditable(false);
		testo12.setEditable(false);
		testo13.setEditable(false);
		testo14.setEditable(false);
		for (int i=0; i<checkboxes.length; i++)
			checkboxes[i].setEnabled(false);
		for (int i=0; i<checkboxes1.length; i++)
			checkboxes1[i].setEnabled(false);
	}

}
