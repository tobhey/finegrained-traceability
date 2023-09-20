package interfacce;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import common.utility.*;

public class SubCommissioni extends Frame
{
   	Label label;
   	Panel panel0, panel1;
   	Button Inserimento, Modifica, Cancellazione, Ricerca, Annulla;
   	GridBagLayout gridbag=new GridBagLayout();
   	Frame p = new Frame();
   	MascheraRicercaStanza mask_ric;
	ModificaCommissioni mask_mod; 
	CancellaCommissioni mask_canc;
	VediCommissione mask_vis;
	
    public SubCommissioni()
    {
        super("Commissioni");
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
        Immagine  figura = new Immagine("camera2.jpg");
        Etichetta etich = new Etichetta("Commissioni.gif");
        Utils.constrain(panel0,etich, 0, 0, 2, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.CENTER, 0.5, 1.0,0,0,0,0);
        Utils.constrain(panel0,figura, 2, 0, 10, 12,GridBagConstraints.VERTICAL,
                     GridBagConstraints.CENTER, 0.5, 1.0,0,0,0,0);
       
        panel1 = new Panel();
        panel1.setLayout(new GridLayout(5,1));
        Inserimento = new Button(" Inserimento ");
        Modifica = new Button(" Modifica ");
        Cancellazione = new Button(" Cancellazione ");
		Ricerca = new Button(" Ricerca... ");        
        Annulla = new Button(" Indietro ");
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
                p.setEnabled(true);
            }
        });
      
        Inserimento.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                startRic();
            }
        });
      
        Modifica.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                startMod();
            }
        });

        Cancellazione.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                startCanc();
            }
        });
        
        Ricerca.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                startVis();
            }
        });
    }

	void startRic()
	{
		this.setEnabled(false);
		mask_ric = new MascheraRicercaStanza("Ricerca stanze per commissioni",1);
		mask_ric.setVisible(true);
		mask_ric.padre = this; 
	}
	
	void startMod()
	{
		this.setEnabled(false);
		mask_mod = new ModificaCommissioni();
		mask_mod.setVisible(true);
		mask_mod.padre = this; 
	}
	
	void startCanc()
	{
		this.setEnabled(false);
		mask_canc = new CancellaCommissioni();
		mask_canc.setVisible(true);
		mask_canc.padre = this; 
	}

	void startVis()
	{
		this.setEnabled(false);
		mask_vis = new VediCommissione();
		mask_vis.setVisible(true);
		mask_vis.padre = this; 
	}
	
}
