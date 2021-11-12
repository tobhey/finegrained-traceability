package smos.bean;

import java.io.Serializable;

/**
 * Classe impiegata per modellare una classe 
 * @author Nicola Pisanti
 * @version 1.0
 */
public class Classe implements Serializable{

	
	private static final long serialVersionUID = -8295647317972301446L; 

	private int idClasse; //Id della classe
	private int idIndirizzo;	//Id dell'indirizzo
	private String nome;	//Nome della classe
	private int annoAccademico; //Anno accademico della classe, da inserire l'anno del primo semestre.
	
	
	public Classe(){
		this.idIndirizzo=0;
		this.idClasse=0;
		
	}
	
	
	/**
	 * Metodo che restituisce l'anno accademico
	 * @return Un intero che rappresenta l'anno scolastico del primo semestre della classe.
	 */
	public int ottenereAnnoAccademico() {
		return annoAccademico;
	}
	
	
	
	/**
	 * Metodo che setta l'anno accademico
	 * @param Il nuovo anno accademico da impostare
	 */
	public void settareAnnoAccademico(int pAnnoAccademico) {
		this.annoAccademico = pAnnoAccademico;
	}
	
	
	/**
	 * Metodo per avere l'ID dell'indirizzo della classe
	 * @return Un intero che rappresenta l'ID dell'indirizzo della classe
	 */
	public int ottenereIdIndirizzo() {
		return idIndirizzo;
	}
	
	
	/**
	 * Metodo che setta l'ID dell'indirizzo della classe
	 * @param Il nuovo ID da settare
	 */
	public void settareIdIndirizzo(int pIdIndirizzo) {
		this.idIndirizzo = pIdIndirizzo;
	}
	
	
	/**
	 * Metodo che restituisce l'ID della classe 
	 * @return Un intero che rappresenta l'ID della classe
	 */
	public int ottenereIdClasse() {
		return idClasse;
	}
	
	
	/**
	 * Metodo che setta l'ID della classe
	 * @param Il nuovo ID da settare
	 */
	public void settareIdClasse(int pIdClasse) {
		this.idClasse = pIdClasse;
	}
	
	
	/**
	 * Metodo che restituisce il nome della classe
	 * @return Una stringa che rappresenta il nome della classe
	 */
	public String ottenereNome() {
		return nome;
	}
	
	
	/**
	 * Metodo che setta il nome della classe
	 * @param Il nuovo nome da settare
	 */
	public void settareNome(String pNome) {
		this.nome = pNome;
	}
	
	
	
	public String toString(){
		
		return (nome + " "+ annoAccademico+ " ID: "+ idClasse);
	}
	
	
}
