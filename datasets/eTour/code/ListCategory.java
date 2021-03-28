package unisa.gps.etour.control.fuzzy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class ListCategory implements Serializable {

    /**
     * Keeps track of data in each category
     */
    private static final long serialVersionUID = 1L;
    private Hashtable<String, Category> categories; // hash table that keeps for each category a Class category
    private Hashtable<String, float[]> totTerms; // hash table that keeps the terms of all Categories
    float maxDist; // contains the maximum distances

    /**
     * The constructor initializes the two hash tables that contain Categories and
     * terms of all categories
     */
    public ListCategory() {
        categories = new Hashtable<String, Category>();
        totTerms = new Hashtable<String, float[]>();
    }

    /**
     * Access method attribute maxDist
     *
     * @Return the maximum distance of all the terms in all categories
     */
    public float getMaxDist() {
        return maxDist;
    }

    /**
     * Access method to the table of categories
     *
     * @Return categories
     */
    public Hashtable<String, Category> getAllCategories() {
        return categories;
    }

    /**
     * Method of accessing the table of total time
     *
     * @Return totTermini
     */
    public Hashtable<String, float[]> getTotTerms() {
        return totTerms;
    }

    /**
     * Method to access a category in the table of Categories
     *
     * @Return object categories representing the category name PNameCategoria
     */
    public Category getCategory(String pCategoryName) {

        if (categoryExists(pCategoryName)) { // if there is the appropriate category
            return categories.get(pCategoryName); // returns the value to pCategoryName
        }
        return null; // otherwise null
    }

/**
* Method of accessing the values of a particular term in this
* Category table
*
* @Return Returns the values associated with the term pTerms
*/
public float [] getTerms (String pTerms)
{
    if (termExists (pTerms)) { // if the term is present in table Terms of total
    return (float []) totTerms.get (pTerms); // return the value
    }
    return null; // null otherwise
}

    /**
     * Method which allows you to add a category to the table of Categories
     *
     * @Param pCategoryName category name to add
     * @Param object associated pCategory category
     * @Return true if the operation was successfully carried out false Otherwise
     */
    public boolean addCategory(String pCategoryName, Category pCategory) {
        if (!categoryExists(pCategoryName)) { // if the category exists
            return false; // returns false
        }
        categories.put(pCategoryName, pCategory); // otherwise load the Category in the table

        return true; // returns true
    }

    /**
     * Edit a category of the category table
     *
     * @Param pCategoryName category name to edit
     * @Param object pCategory be associated with this category
     * @Return true if the operation was successfully carried out false Otherwise
     */
    public boolean setCategoria(String pCategoryName, Category pCategory) {
        if (categoryExists(pCategoryName)) {// if the category does not exist
            return false; // returns false
        }
        categories.put(pCategoryName, pCategory); // update the table of Catogorie

        return true; // returns true
    }

    /**
     * Method which allows you to set the value of a term in the tables Total time
     *
     * @Param name pTerms term
     * @Param pVal value to associate with the term
     */
    public void setTerms(String pTerms, float[] pVal) {

        totTerms.put(pTerms, pVal);
    }

    /**
     * Method which allows the value of the seven kings of the maximum distance of
     * Terms from one category
     *
     * @Param pMaxDist
     */
    public void setMaxDist(float pMaxDist) {
        maxDist = pMaxDist;
    }

    /**
     * Method which allows to derive a collection of names of iterable All
     * categories in the categories table
     *
     * @Return String iterable Collection
     */
    public Iterable<String> categories() {
        List<String> toReturn = new ArrayList<String>();// create a new list
        for (Enumeration<String> val = categories.keys(); val.hasMoreElements();) { // iterates N Times where N is the
                                                                                    // number by categories in table
            toReturn.add(val.nextElement());// adds to the list the name of a category

        }

        return toReturn;
    }

    /**
     * Method aids to verify the existence of a category In the table of categories
     *
     * @Param pKey name of the category 
     * @Return true if the category exists false otherwise
     */
    public boolean categoryExists(String pKey) {
        try {
            categories.get(pKey); // try to extract the category name pKey
            // The table of categories
            return true; // if the transaction does not raise exceptions category
            // Exists and returns true
        } catch (NullPointerException e) {
            return false; // false otherwise
        }
    }

    /**
     * Method aids to verify the existence of a term In the table of total time
     *
     * @Param pKey term
     * @Return true if the term exists false otherwise
     */
    public boolean termExists(String pKey) {
        // see categoryExists
        try {
            if (totTerms.get(pKey) != null) {
                return true;
            }
        } catch (NullPointerException e) {
            return false;
        }

        return false;
    }

}