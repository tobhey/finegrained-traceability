package interfacce;
import java.awt.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import java.util.Date;
import moduli.*;

public class PrenotazioniScadute extends Frame 
{  
	Prenotazione prenotazione;
    
    //Dichiarazioni di variabili
    Panel panel2, panel3; 
    Label  label3;
    List lista;
    Button  Annulla, Scegli;
    GridBagLayout gridbag = new GridBagLayout();
	SubPrenotazione padre = new SubPrenotazione();
  	ListaPrenotazioni L_pren;
  	Frame figlio;  
    int to_call;
 	boolean first_time = true;   
    
    public PrenotazioniScadute (SubPrenotazione p, ListaPrenotazioni L)
    {
        super("Prenotazioni in scadenza");
        padre = p;
        L_pren = L;
        setupPanels();
        init();
        pack(); 
    }
                     
        void setupPanels()
        {  
			this.setFont(ConfigurazioneSistema.font_base);
            
            //Creo dei pulsanti e ne disabilito due  
            Annulla = new Button(" Fine ");
            Scegli = new Button(" Visualizza ");
            //Creo le etichette
			label3 = new Label("Elenco delle prenotazioni non confermate                ");
			label3.setFont(ConfigurazioneSistema.font_titolo);
            
            //Creo il pannello in basso con due pulsanti                  
            panel2 = new Panel();
            panel2.setLayout(gridbag);
            Utils.constrain(panel2, Annulla, 0, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(panel2, Scegli, 1, 0, 1, 1, GridBagConstraints.NONE,
                        GridBagConstraints.CENTER, 0.0, 0.0, 5, 5, 5, 5);             
            
            //Creo il pannello con la lista dei risultati della ricerca
            lista = new List(10,false);
            panel3 = new Panel();
            panel3.setLayout(gridbag);
            Utils.constrain(panel3, label3, 0, 0, 2, 1, GridBagConstraints.NONE,
                        GridBagConstraints.WEST, 1.0, 0.0, 0, 0, 0, 0);
            Utils.constrain(panel3, lista, 0, 1, 4, 3, GridBagConstraints.BOTH,
                        GridBagConstraints.CENTER, 3.0, 3.0, 0, 0, 0, 0);             
            
            
            //Attacco i pannelli al frame
            this.setLayout(gridbag);
            Utils.constrain(this, panel3, 0, 0, 4, 10, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5);
            Utils.constrain(this, panel2, 0, 10, 4, 1, GridBagConstraints.HORIZONTAL,
                        GridBagConstraints.WEST, 1.0, 0.0, 5, 5, 5, 5); 
        	pack();
        
        }

    public void init()
    {
		if (lista.getItemCount() != 0)
			lista.removeAll();
		for(int i = 1; i<=L_pren.length(); i++)
			lista.addItem((L_pren.getPrenotazione(i)).toString());	
	
	
		lista.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
         		prenotazione = L_pren.getPrenotazione((lista.getSelectedIndexes())[0]+1);
				startControllaPren(prenotazione);
			}
      	});
     
   	 	Scegli.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
         		if (!noSelection())
         		{
         			prenotazione = L_pren.getPrenotazione((lista.getSelectedIndexes())[0]+1);
					startControllaPren(prenotazione); 
				}
			}
      });
        
  		Annulla.addActionListener(new ActionListener()
      	{
         	public void actionPerformed(ActionEvent e)
         	{
				dispose();
				if ( (SubPrenotazione.ric_stanza == null) && (SubPrenotazione.ric_stanza == null) )
					padre.setEnabled(true);
			}
      	});    	

    } 

	void startControllaPren(Prenotazione pren)
	{
		this.setEnabled(false);
		Stanza stanza = (Principale.db).readStanza(pren.getNumStanza());
		ControllaPrenotazione ctrl = new ControllaPrenotazione(pren, stanza, this);
		ctrl.setVisible(true);
		ctrl.padre = this;	
	}

	boolean noSelection()
	{
		Frame msg;
		if (lista.getSelectedIndex() == -1)
		{
			msg = new AvvisoDialog(this," Selezionare una prenotazione dalla lista e ripremere il tasto");
			return true;
		}	
		return false;
	}

}
