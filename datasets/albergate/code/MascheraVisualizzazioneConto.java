package interfacce;
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import common.utility.*;
import common.def.*;
import moduli.*;
import java.util.Date;
import java.util.Properties;

public class MascheraVisualizzazioneConto extends Frame
{
   	Button Stampa,OK,Partenza;
   	TextField testo1, testo2, testo3, testo4;
   	Label etichetta, label1, label2, label3, label4;
   	Panel  panel1, panel2, panel3;
   	GridBagLayout gridbag = new GridBagLayout();
   	TextArea text;
   	BufferedReader inStream;
	int chiusura; 
	
	MascheraCalcoloConto conto = new MascheraCalcoloConto(new Frame());

   	public MascheraVisualizzazioneConto(String t0, String t1, String t2, String t3, 
   										String t4, int tipo)
   	{
      	super("Visualizzazione del conto");
      	chiusura = tipo;
      	setup(t0,t1,t2,t3,t4);
   	  	readConto();	
      	init();
      	pack();
   	}

   void setup(String t0, String t1, String t2, String t3, String t4)
   {
      for (int i=0; i<conto.testo.length-1; i++)
			conto.testo[i].setText("");
	  this.setFont(ConfigurazioneSistema.font_base);
      etichetta = new Label("Intestazione");
      etichetta.setFont(ConfigurazioneSistema.font_titolo);
      label1 = new Label("Stanza numero");
      label2 = new Label("Cognome");
      label3 = new Label("Nome");
      label4 = new Label("Lista stanze");
      Stampa = new Button("  Stampa  ");
      OK = new Button(" Indietro ");
      Partenza = new Button(" Chiudi conto ");
      testo1 = new TextField("",4);
      testo2 = new TextField("",20);
      testo3 = new TextField("",20);
      testo4 = new TextField("",30);
      testo1.setText(t0+t3);
      testo2.setText(t1);
      testo3.setText(t2);
      testo4.setText(t4);
      testo1.setEditable(false);
      testo2.setEditable(false);
      testo3.setEditable(false);
      testo4.setEditable(false);
      
      text = new TextArea(25,50);
      text.setEditable(false);
      text.setFont(ConfigurazioneSistema.font_allineato);

      panel1=new Panel();
      panel1.setLayout(gridbag);
      Utils.constrain(panel1,etichetta,0,0,1,1,GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST,1.0,0.0,0,0,5,0);   
      Utils.constrain(panel1,label1,0,1,1,1,GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST,0.0,0.0,5,0,5,0);   
      Utils.constrain(panel1,testo1,1,1,1,1,GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST,1.0,0.0,5,0,5,0);   
      Utils.constrain(panel1,label2,0,2,1,1,GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST,0.0,0.0,5,0,5,0);   
      Utils.constrain(panel1,testo2,1,2,1,1,GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST,1.0,0.0,5,0,5,0);   
      Utils.constrain(panel1,label3,2,2,1,1,GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST,1.0,0.0,5,0,5,0);   
      Utils.constrain(panel1,testo3,3,2,1,1,GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST,1.0,0.0,5,0,5,0);   
      Utils.constrain(panel1,label4,0,3,1,1,GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST,1.0,0.0,5,0,5,0);   
      Utils.constrain(panel1,testo4,1,3,1,1,GridBagConstraints.NONE,
                     GridBagConstraints.NORTHWEST,1.0,0.0,5,0,5,0);                  


      panel2=new Panel();
      panel2.setLayout(gridbag);
      Utils.constrain(panel2,text,0,0,4,4,GridBagConstraints.BOTH,
                     GridBagConstraints.NORTHWEST,1.0,1.0,5,0,5,0);   


      panel3=new Panel();
      panel3.setLayout(gridbag);
      Utils.constrain(panel3,Stampa,0,0,1,1,GridBagConstraints.NONE,
                     GridBagConstraints.SOUTHEAST,0,0.0,5,5,5,5);   
      Utils.constrain(panel3,Partenza,1,0,1,1,GridBagConstraints.NONE,
                     GridBagConstraints.CENTER,0,0.0,5,5,5,5);   
      Utils.constrain(panel3,OK,2,0,1,1,GridBagConstraints.NONE,
                     GridBagConstraints.SOUTHWEST,0.0,0.0,5,5,5,5);   

      this.setLayout(gridbag);
      Utils.constrain(this,panel1,0,0,4,3,GridBagConstraints.HORIZONTAL,
                     GridBagConstraints.NORTHWEST,1.0,0.0,10,10,10,10);   
      Utils.constrain(this,panel2,0,3,4,6,GridBagConstraints.BOTH,
                     GridBagConstraints.NORTHWEST,1.0,1.0,10,10,10,10);   
      Utils.constrain(this,panel3,0,9,4,1,GridBagConstraints.HORIZONTAL,
                     GridBagConstraints.SOUTHWEST,1.0,0.0,10,10,10,10);   

   }

   public void init()
   {
      OK.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
            dispose();
	      	conto.setEnabled(true);
         }
      });
      
      Stampa.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
			startStampa();
         }
      });
      
      Partenza.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e)
         {
			startChiudiConto();	         	
         	
         }
      });
   
   }

	void readConto()
	{
		Frame msg;
		DataInputStream inStream;
		try
		{
			inStream = new DataInputStream(new FileInputStream("conto.abg"));
		}
		catch (IOException ex)
		{
			msg = new MessageDialog(this, " Errore nell'apertura del file del conto! ");
			return;
		}
		try
		{
			String newText="";
			String line;
			while((line=inStream.readLine())!=null)
				newText=newText+line+"\n";
			text.setText(newText);
			inStream.close();
		}
		catch (IOException ex)
		{
			msg = new MessageDialog(this, " Errore durante la lettura del file del conto! ");
		}		
	}

	void startChiudiConto()
	{
		Frame msg;
		AskChiudiConto ask;

		Date today = new Date();
		Date fine_effettiva;
		if (conto.checkboxes[3].getState())
			fine_effettiva = conto.fine_sogg_anticipato;
		else
			fine_effettiva = conto.data_fine_magg;
		if ( Utils.data1MinoreData2( fine_effettiva, today ) )
			ask = new AskChiudiConto(this);
		else
		{
			msg = new AvvisoDialog(this, " Non e' possibile chiudere il conto fino al giorno della partenza! ");
		}
	}

	void startChiusura()
	{
		switch (chiusura)
		{
			case 1: chiudiStanza(testo1.getText(), conto.date_inizio[0], conto.date_fine[0],1); break;
			case 2: chiudiSingolo(); break;
			case 3: chiudiComitiva(); break;	
		}
	}
	
	void chiudiStanza(String stanza, Date data_inizio, Date data_fine,int caller)
	{
		long id;
		Soggiornante sogg;
		ListaSoggiornanti L_sogg = (Principale.db).foundSoggiornanti(stanza, false);
		id = (L_sogg.getSoggiornante(1)).getIdPrenotazione();
		ListaDisponibilita L_disp = (Principale.db).elencoDisponibilita();
		Frame msg;

		if (L_disp == null)
		{
			msg = new MessageDialog(this, " Problemi con il database nella lettura delle disponibilita'! ");
			return;
		}
		
		// cancellazione della prenotazione 
		(Principale.db).delPrenotazione(id);

		// spostamento nello storico e cancellazione dei soggiornanti
		for(int i = 1; i <= L_sogg.length(); i++)
		{	
			sogg = L_sogg.getSoggiornante(i);
			if ( !(sogg.getCognome()).equals("") &&
				 !(sogg.getNome()).equals("") &&
				 !(sogg.getIndirizzo()).equals("") &&
				 !(sogg.getComune()).equals("") &&
				 !(sogg.getCitta()).equals("") &&
				 !(sogg.getNumTel()).equals("") &&
				 !(sogg.getCap()).equals("") &&
				 !(sogg.getNumDoc()).equals("") )
			{
				int j =	(Principale.db).writeStorico(sogg.toCliente());
				if (j != DataBase.OK)
				{
					msg = new MessageDialog(this, " Problemi con il database nella scrittura nello storico! ");
					return;
				}
			}
			(Principale.db).delSoggiornante(stanza,sogg.getIdSoggiornante());
		}
		// cancellazione degli addebiti
		(Principale.db).delAddebito(stanza);
		
		// cancellazione dei supp e rid dai beni/servizi
		(Principale.db).delSuppRid(stanza);
		
		// cancellazione delle telefonate
		(Principale.db).delTelefonate(stanza);
		
		// aggiornamento delle disponibilita: questo devono essere fatto affinche'
		// le informazioni riportate dalla machera di visualizzazione delle disponibilita'
		// sia coerente
		if (conto.qualcuno_tramite_agenzia)
		{
			Utils.aggiornaDisp(L_disp, stanza, data_inizio, data_fine, Flag.DISPONIBILE, Flag.OCCUPATA, true);
			Utils.restoreCommissioni(stanza);
		}
		else
		{
			Disponibilita disp = (Principale.db).readDisponibilita(stanza);
			if (disp != null)
			{
				disp.setDisponibilita(data_inizio, data_fine, Flag.DISPONIBILE, Flag.OCCUPATA);
				(Principale.db).changeDisponibilita(disp.getNumStanza(), disp.getDispAnnoCorr(), disp.getDispAnnoProx());
			}
		}
		if (caller == 1)
			startStampa();
	}
	
	void  chiudiSingolo()
	{
		if (conto.checkboxes[3].getState())
		{
			(Principale.db).anticipaFineSogg(conto.sogg_x_conto_sing.getNumStanza(),
									conto.sogg_x_conto_sing.getIdSoggiornante(),
									conto.fine_sogg_anticipato);
		}
		(Principale.db).reversePagato(conto.sogg_x_conto_sing.getNumStanza(),
									  conto.sogg_x_conto_sing.getIdSoggiornante());
	}
	
	void  chiudiComitiva()
	{
		for (int i=0; i < conto.L_st.length; i++)
			chiudiStanza(conto.L_st[i], conto.date_inizio[i], conto.date_fine[i],2);
		startStampa();
	}

	void startStampa()
	{
		Properties prop = new Properties();
		Toolkit tk = Toolkit.getDefaultToolkit();
		PrintJob pj = tk.getPrintJob(this,"Stampa del conto",prop);
		
		if (pj != null)
		{
			Graphics g = pj.getGraphics();
			text.printAll(g);
			g.dispose();
			pj.end();
		}
	}
}
