package smos.bean;
import java.io.Serializable;

/**
 *  Classe utilizzata per modellare un indirizzo.
 *
 * 
 */
public class Indirizzo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9194626030239503689L;
	
	private int idIndirizzo;
	private String nome;
	
	/**
	 * Il costruttore della classe.
	 */
	public Indirizzo(){
		this.idIndirizzo= 0;
	}
		
	/**
	 * @return Ritorna l' id dell' indirizzo.
	 */
	public int ottenereIdIndirizzo() {
		return idIndirizzo;
	}
	
	/**
	 * Setta l' id dell' indirizzo.
	 * @param pIdIndirizzo
	 * 			l' id da settare.
	 */
	public void settareIdIndirizzo(int pIdIndirizzo) {
		this.idIndirizzo = pIdIndirizzo;
	}
	
	/**
	 * @return Ritorna il nome dell' indirizzo.
	 */
	public String ottenereNome() {
		return nome;
	}
	
	/**
	 * Setta il nome dell' indirizzo.
	 * @param pNome
	 * 			Il nome da settare.
	 */
	public void settareNome(String pNome) {
		this.nome = pNome;
	}
	
	
}
