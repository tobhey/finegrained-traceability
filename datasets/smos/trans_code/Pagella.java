package smos.bean;

import java.util.ArrayList;

/**
 * 
 * Class used to model a report card.
 * 
 * @author Luigi Colangelo
 * @version 1.0
 * 
 * 
 */
public class SchoolReport {
	public ArrayList<Grade> schoolReport;

	/**
	 * The constructor of the class
	 */
	public SchoolReport() {
		schoolReport = new ArrayList<Grade>();
	}

	/**
	 * Method that returns the vote from the index in the array given in input
	 *
	 * @param pInd index of the vote in the array
	 * @return the vote of the given index
	 * @throws ValueInvalidException
	 */
	public Grade getGrade(int pInd) throws ValueInvalidException {
		if (pInd < 0 || pInd >= schoolReport.size())
			throw new ValueInvalidException("invalid index!");
		return (schoolReport.get(pInd));
	}

	/**
	 * Method that adds a vote to the array.
	 *
	 * @param pGrade the grade to add
	 * @throws ValueInvalidException
	 */
	public void addGrade(Grade pGrade) throws ValueInvalidException {
		if (pGrade == null)
			throw new ValueInvalidException("grade non valido!");
		else
			schoolReport.add(pGrade);
	}

	/**
	 * Method that delete a grade from the array
	 *
	 * @param pId the index of the grade to remove from the array.
	 * @throws ValueInvalidException
	 */
	public void remove(int pId) throws ValueInvalidException {
		if (pId < 0 || pId >= schoolReport.size())
			throw new ValueInvalidException("invalid index!");
		schoolReport.remove(pId);
	}

	public String ToString() {
		String pag = "";
		for (Grade e : schoolReport) {
			pag = pag + "\n" + e.toString();
		}
		return pag;
	}

}
