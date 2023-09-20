package interfacce;
import java.awt.*;
import java.awt.event.*;
import moduli.*;
import common.utility.*;
import common.def.*;
import java.util.Date;

public class VediCliente extends MascheraSoggiorno
{
	Frame padre;   
   	public VediCliente(Frame p, Cliente cli)
   	{
      	super("Visualizzazione dei dati del cliente"," Fine ",' ',' ');
      	padre=p;
      	padre.setEnabled(false);
      	inizializza(cli);
      	pack();
      	
   	}

   public void inizializza(Cliente cli)
   {
      remove(panel2);
      Utils.constrain(panel1, label13, 0, 8, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, testo13, 1, 8, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5); 
      Utils.constrain(panel1, label14, 2, 8, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 5, 5, 5, 5);
      Utils.constrain(panel1, testo14, 3, 8, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 1.0, 0.0, 5, 5, 5, 5);
      panel3.remove(Annulla);
      pack();
      disableFields();
      writeDatiCliente(cli);
      this.setVisible(true);
      
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
	}
	
	public void writeDatiCliente(Cliente cliente)
	{
		testo1.setText(cliente.getNumStanza());
		testo2.setText(cliente.getCognome());
		testo3.setText(cliente.getNome());
		testo4.setText(cliente.getLuogoNasc());
		testo5.setText(DateUtils.giveStringOfDate(cliente.getDataNasc()));
		testo6.setText(cliente.getNumDoc());
		testo7.setText(cliente.getNumTel());
		testo8.setText(cliente.getIndirizzo());
		testo9.setText(cliente.getCap());
		testo10.setText(cliente.getComune());
		testo11.setText(cliente.getCitta());
		testo12.setText(cliente.getNazione());
		testo13.setText(DateUtils.giveStringOfDate(cliente.getInizioSogg()));
		testo14.setText(DateUtils.giveStringOfDate(cliente.getFineSogg()));
	}

}
