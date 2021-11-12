package smos.bean;

import java.util.ArrayList;

import smos.exception.ValoreNonValidoEccezione;

/**
 * 
 * Classe utilizzata per modellare una pagella.
 * 
 * @author Luigi Colangelo 
 * @version 1.0
 * 
 *          
 */
public class Pagella {
	public ArrayList<Voto> pagella; 
	
	/**
	 * Il costruttore della classe
	 */
	public Pagella(){
		pagella=new ArrayList<Voto>();
	}
	
	/**
	 * Metodo che restituisce il voto dall'indice nell'array dato in input
	 * @param pInd indice del voto nell'array
	 * @return il voto dell'indice dato
	 * @throws ValoreNonValidoEccezione
	 */
	public Voto ottenereVoto(int pInd) throws ValoreNonValidoEccezione{
		if(pInd<0 || pInd>=pagella.size())throw new ValoreNonValidoEccezione("indice non valido!");
		return (pagella.get(pInd));
	}
	
	/**
	 * Metodo che aggiunge un voto all'array.
	 * @param pVoto il voto da aggiungere
	 * @throws ValoreNonValidoEccezione 
	 */
	public void aggiungereVoto(Voto pVoto) throws ValoreNonValidoEccezione{
		if(pVoto==null)throw new ValoreNonValidoEccezione("voto non valido!");
		else pagella.add(pVoto);
	}
	
	/**
	 * Metodo che elimina un voto dall'array
	 * @param pId l'indice del voto da eliminare dall'array.
	 * @throws ValoreNonValidoEccezione 
	 */
	public void eliminare(int pId) throws ValoreNonValidoEccezione{
		if(pId<0 || pId>=pagella.size())throw new ValoreNonValidoEccezione("indice non valido!");
		pagella.remove(pId);
	}
	
	
	public String ToString(){
		String pag="";
		for(Voto e: pagella){
			pag=pag+"\n"+e.toString();
		}
	return pag;
	}
    
}
