package unisa.gps.etour.control.fuzzy;

import java.io.Serializable;
import java.util.Hashtable;

public class Category implements Serializable {

    /**
     * Class that describes the characteristics of a Category Contains a Hashtable
     * that represents the dictionary on The category that contains and for each
     * term associated In the category values of frequency, distance and relevance.
     * Provides methods to access, modify and auxiliary methods.
     */
    private static final long serialVersionUID = -8652232946927756089L;

    private String name; // name of the category
    private Hashtable<String, float[]> terms; // list of terms and their frequencies and distance rilavanza

    /**
     * Manufacturer: Get the category name as a parameter to create
     */
    public Category(String pName) {
        name = pName;
        terms = new Hashtable<String, float[]>();
    }

    /**
     * Returns the output Hashtable containing the terms With the respective values
     * of frequency, relevance and distance
     */

    public Hashtable<String, float[]> getTerms() {
        return terms;
    }

    /**
     * Returns the name of the output category
     */
    public String getName() {
        return name;
    }

    /**
     * Get the String as a parameter representing the term Of which you want to pick
     * the values of frequency, range and bearing
     */
    public float[] getVal(String pTerms) throws NullPointerException {
        if (termExists(pTerms))
            return terms.get(pTerms);

        return null;
    }

    /**
     * Adds an end to dictionary category
     */
    public void addTerm(String pTerm) {
        terms.put(pTerm, new float[3]);
    }

    /**
     * add an end to dictionary category Seven also the values of frequency,
     * distance and relevance
     */
    public boolean addTerm(String pTerm, float[] pVal) {
        if ((pVal == null) || (pTerm.equals("")))
            return false;
        terms.put(pTerm, pVal);

        return true;
    }

    /**
     * Set the values for the period pTerm
     */
    public boolean setValTerm(String pTerm, float[] pVal) throws NullPointerException {
        if (termExists(pTerm)) {
            terms.put(pTerm, pVal);
            return true;
        }

        return false;
    }

    public void setTerm(Hashtable<String, float[]> pTerm) {
        terms = pTerm;
    }

    /**
     * Returns True if the term is present in Dictionary of Category False otherwise
     */
    public boolean termExists(String pTerms) {
        try {
            float[] ret = terms.get(pTerms);
            if (ret != null)
                return true;
        } catch (NullPointerException e) {
            return false;
        }

        return false;
    }

}