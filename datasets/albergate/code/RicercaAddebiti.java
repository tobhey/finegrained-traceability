package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import java.util.Date;
import moduli.*;

public class RicercaAddebiti extends Frame 
{  
    
    //Dichiarazioni di variabili
    Panel panel1, panel2, panel3;
    Label  etichetta1, label1, label3;
    List lista;
    Button  Annulla, Cerca;
    TextField stanza;
    GridBagLayout gridbag = new GridBagLayout();
	Frame padre = new Frame();
    Frame figlio;
    ListaAddebiti L;
    Addebito add;
    float totale_add;
    
    public RicercaAddebiti(Frame p)
    {
        super("Ricerca degli addebiti");
        padre = p;
        padre.setEnabled(false);
        setupPanels();
        init();
        pack();
        setVisible(true); 
    }
                     
        void setupPanels()
        {  
			this.setFont(ConfigurazioneSistema.font_base);
            //Creo due pulsanti  
            Annulla = new Button(" Fine  ");
            Cerca = new Button(" Cerca ");

            //Creo le etichette
            etichetta1 = new Label ("Inserire la stanza addebitata                         ");
            etichetta1.setFont(ConfigurazioneSistema.font_titolo);
            label1 = new Label("Numero Stanza   ");
			label3 = new Label("Risultato della ricerca");
			label3.setFont(ConfigurazioneSistema.font_titolo);
            
            //Creo i TextField e ne rendo  alcuni non editabili
            stanza = new TextField("", 4);
			
            //Creo il pannello in alto per inserimento dei dati del cliente
            panel1 = new Panel();
            panel1.setLayout(gridbag);
            Utils.constrain(panel1, etichetta1, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);
            Utils.constrain(panel1, label1, 0, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);
            Utils.constrain(panel1, stanza, 1, 1, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.NORTHWEST, 0.0, 0.0, 0, 0, 0, 0);
           
            //Creo il pannello in basso con due pulsanti                  
            panel2 = new Panel();
            panel2.setLayout(gridbag);
            Utils.constrain(panel2, Annulla, 0, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel2, Cerca, 1, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 5, 5, 5, 5);             
            
            //Creo il pannello con la lista dei risultati della ricerca
            lista = new List(10,false);
            lista.setFont(ConfigurazioneSistema.font_allineato);
            panel3 = new Panel();
            panel3.setLayout(gridbag);
            Utils.constrain(panel3, label3, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.WEST, 1.0, 0.0, 0, 0, 0, 0);
            Utils.constrain(panel3, lista, 0, 1, 4, 3, GridBagConstraints.BOTH,
                        GridBagConstraints.CENTER, 3.0, 3.0, 0, 0, 0, 0);             
            
            
            //Attacco i pannelli al frame
            this.setLayout(gridbag);
            Utils.constrain(this, panel1, 0, 1, 4, 2, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(this, panel2, 0, 14, 4, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5); 
        	Utils.constrain(this, panel3, 0, 15, 4, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5); 
        	panel3.setVisible(false);
        	pack();
        
        }

    public void init()
    {
		lista.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
				if (lista.getSelectedIndex() < (lista.getItemCount()-1))
				{
					add = L.getAddebito((lista.getSelectedIndexes())[0]+1);
					startVediAddebito(add);
				}
			}
      	});
 

		
	stanza.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if (noErrors())
				creaLista();
		}			      	
	});
	
    Cerca.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
				if (noErrors())
					creaLista();
			}	
      	});
      
  	Annulla.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
				dispose();
				padre.setEnabled(true);
			}
      	});    	
    } 

	void startVediAddebito(Addebito add)
	{
		figlio = new VediAddebito(this, add, totale_add); 
	}	
	
	
	public void creaLista()
	{
		Frame msg;
		BeneServizio extra;
		Addebito addebito;
		String desc = new String();
		totale_add = 0;
		
		L = (Principale.db).foundAddebiti(stanza.getText());
		if (L != null)
		{
			if (!L.isEmpty())
			{
				if (lista.getItemCount() > 0)
					lista.removeAll();
				panel3.setVisible(true);
				pack();
				for(int i = 1; i<=L.length(); i++)
				{	
					addebito = L.getAddebito(i);
					extra = (Principale.db).readBeneServizio(addebito.getCodExtra());
					if (extra != null)
						lista.addItem("DESCRIZIONE:"+Utils.completaStringa(extra.getDescrizione(), 10)+
									  " QUANTITA':"+Utils.completaStringa((""+addebito.getQuantita()), 2)+
									  " TOTALE ADDEBITO:"+
									  (Principale.config).getValuta()+"."+
									  addebito.getTotAddebito());	
					totale_add += addebito.getTotAddebito();
				}
				lista.addItem("L'ADDEBITO TOTALE DELLA STANZA "+stanza.getText()+" E' DI "+
								(Principale.config).getValuta()+"."+totale_add);
			}
			else
				msg = new AvvisoDialog(this, " Nessun addebitato trovato. ");
		}
		else
			msg = new MessageDialog(this, " Problemi con il database! ");
	}			

	boolean noErrors()
	{
		Frame msg;
		//Disponibilita disp = new Disponibilita();
		if ((stanza.getText().equals("")) ) 
		{
			msg = new AvvisoDialog(this,"Inserire il numero della stanza");
			return false;
		}
		else
			if( (Principale.db).readStanza(stanza.getText()) == null )
			{
				msg = new MessageDialog(this,"La stanza inserita e' inesistente!");
				return false;
			}
		return true;	
	}
}
