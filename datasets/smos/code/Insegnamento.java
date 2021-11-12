package smos.bean;

import smos.exception.ValoreNonValidoEccezione;

import java.io.Serializable;

/**
 * Classe utilizzata per modellare un insegnamento.
 * 
 * @author Giulio D'Amora
 * @version 1.0
 * 
 *          
 * 
 */
public class Insegnamento implements Serializable {

	private static final long serialVersionUID = 2523612738702790957L;
	private int id_insegnamento;
	private String nome;

	/**
	 * Il costruttore della classe.
	 */
	public Insegnamento() {
		this.id_insegnamento = 0;
	}

	/**
	 * Ritorna il nome dell'insegnamento
	 * 
	 * @return Ritorna il nome dell'insegnamento.
	 */
	public String ottenereNome() {
		return this.nome;
	}

	/**
	 * Setta il nome dell'insegnamento.
	 * 
	 * @param pNome
	 *            Il nome da settare.
	 * 
	 * @throws ValoreNonValidoEccezione
	 */
	public void settareNome(String pNome) throws ValoreNonValidoEccezione {
		if (pNome.length() <= 4)// da verificare il test
			throw new ValoreNonValidoEccezione();
		else
			this.nome = pNome;
	}

	/**
	 * Ritorna l'id dell'insegnamento.
	 * 
	 * @return l'id dell'insegnamento.
	 */
	public int ottenereId() {
		return this.id_insegnamento;
	}

	/**
	 * Setta l'id dell'insegnamento.
	 * 
	 * @param pId
	 *            L'id da settare.
	 */
	public void settareId(int pId) {
		this.id_insegnamento = pId;
	}

}
