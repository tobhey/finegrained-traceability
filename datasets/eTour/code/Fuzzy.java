package unisa.gps.etour.control.fuzzy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Scanner;
/**
 * Class that implements the methods used to calculate the Fuzzy Category of
 * membership of a refreshment or a cultural property.
 */
public class Fuzzy {


    /**
     * Method for calculating the relevance of a term.
     *
     * @Param distance: Contains the distance of the term from category analyzed 
     * @Param pMaxDist: Contains  maximum distance of all terms in all categories. 
     * @Return Returns the relevance of the term in the category.
     */
    private static float relevance(float distance, float pMaxDist) {
        return (distance / pMaxDist);

    }

    /**
     * Method for calculating the distance between a term and a category
     *
     * @Param pTerms: Contains the period analyzed 
     * @Param pTotTerms: Tables of the total frequency of terms. 
     * @Param textFrequency: Table of the terms of the text analyzed. 
     * @Param pCategory: Category analyzed. 
     * @Return Returns
     * the distance of the term pTerms by category PCategoria
     */
    private static float distance(String pTerms, Hashtable<String, float[]> pTotTerms, float textFrequency,
            Category pCategory) {

// The first variable tracks the frequency of a term
// Relating to a category
// The second keeps track of fraquenza a deadline for all
// Categories
        float[] categoryFrequency = new float[3];
        float[] totalFrequency = new float[3];

        if (pCategory.termExists(pTerms)) // if the term is In category
        {
// Its frequency in this category is equal to the frequency
// KnowledgeBase
// More frequency in the search text
            categoryFrequency = pCategory.getVal(pTerms);
            categoryFrequency[0] += textFrequency;
        } else {

// Otherwise it is equal to the frequency of the term in the text
// Analyzed
            categoryFrequency[0] = textFrequency;
        }
        if (pTotTerms.get(pTerms) != null) // if the term exists in Table of the total time
            totalFrequency = pTotTerms.get(pTerms); // Get the value

        totalFrequency[0] += textFrequency; 
        // the total frequency is given Frequency in the text  Plus any frequency stored in memory in the table Total time

        return (categoryFrequency[0] / totalFrequency[0]);// distance is Equal to Frequency in Category The total frequency 

    }

    /**
     * Method for calculating the distance of a term from one category. Used for
     * training
     *
     * @Param pTerms period to analyze 
     * @Param pCategory category from which you
     * must calculate the distance 
     * @Param table pTotTerms total time 
     * @Return Returns the distance of a term from one category
     */
    private static float distance(String pTerms, Category pCategory, Hashtable<String, float[]> pTotTerms) {
        return ((pCategory.getVal(pTerms))[0] / (pTotTerms.get(pTerms))[0]);
    }

    /**
     * Implementation of a function T-Norm
     *
     * @Param a first value 
     * @Param b the second value 
     * @Return returns the value calculated using a function T-Norm
     */
    private static float tNorm(float a, float b) {
        return ((a * b) / ((2 - ((a + b) - (a * b)))));
    }

    /**
     * Implementation of a function S-Norm
     *
     * @Param a first value
     * @Param b the second value 
     * @Return returns the value calculated using a function S-Norm
     */
    private static float sNorm(float a, float b) {
        return ((a + b) / (1 + a * b));
    }

    /**
     * Calculation of similarity between a category and a given text input
     *
     * @Param table pTerm worded. It must contain to  term values of
     * importance and belonging situated in Vector float in positions 1 and 2. 
     * @Return returns a numeric value that indicates the similarity of a Text with
     * the category on which one has calculated the values of Importance and
     * belonging 
     */
    private static float similarity(Hashtable<String, float[]> pTerm) {
        float sum = 0; // return value

        for (float[] val : pTerm.values())// for all elements of
// Table
        {
            sum += (tNorm(val[1], val[0])) / (sNorm(val[1], val[0]));// performs
// Sum of the values given by the division of function T-Norm
// With the function S-Norm made
// Between relevance and belonging
        }

        return sum;
    }

    /**
     * Method for the calculation of membership of a text to a category
     *
     * @Param val indicates the similarity of a text with a category 
     * @Param maxSimilarity indicates the maximum similarity found 
     * @Return a value in the interval [0,1] that indicates the degree of Membership of the
     * text to the category x
     */
    private static float membership(float val, float maxSimilarity) {
        return (val / maxSimilarity);

    }

    /**
     * Method to delete a tense special characters and to bring all uppercase to
     * lowercase
     *
     * @Param pStr transform 
     * @Return the text to lowercase
     * characters and no special
     */
    private static String replaceAndLower(String pStr) {
        pStr = pStr.toLowerCase();
        pStr = pStr.replace(",", "");
        pStr = pStr.replace(".", "");
        pStr = pStr.replace("!", "");
        pStr = pStr.replace("?", "");
        pStr = pStr.replace("'", "");

        return pStr;
    }

    /**
     * Method of retrieving the category you belong to a text
     *
     * @Param pDescription text to analyze 
     * @return a String indicating the
     * category @Throws RemoteException
     */
    public static String calculateCategory(String pDescription) throws RemoteException {
        if ((pDescription == null) || (pDescription.equals("")))
            return "NULL";
        String text = pDescription;
// Table of terms associated with the text portion. Will contain Values of frequency, relevance, membership for each term
        Hashtable<String, float[]> textData = new Hashtable<String, float[]>();
// Table of categories, each category will contain the value of Similarity and belonging Text
        Hashtable<String, float[]> textCategory = new Hashtable<String, float[]>();
        text = replaceAndLower(text); // delete characters Returns the text by replacing Uppercase with lowercase
        String[] textSplit = text.split("");
        for (int i = 0; i < textSplit.length; i++) {
            // For each end of the text
            float[] toPut = new float[3]; // value to assign to the String in the hash table
            float[] valTmp; // temporary variable containing the values
// Associated with the String if it already exists in the hash table
// If the String is present in the table picks up the values
// And an increase in saving them in to put
// Otherwise initialize the new String with frequency = 1
            if (textSplit[i].length() <= 3) // delete undefined terms
// As important
                continue;
            if (exists(textSplit[i], textData)) // if the time analyzed
// Is present in table the terms of the analyzed text
            {
// We get the value of frequency in the table and there
// Adds one
                valTmp = textData.get(textSplit[i]);
                toPut[0] = valTmp[0];
                toPut[0] += (float) 1 / textSplit.length;

            } else {
                toPut[0] = (float) 1 / textSplit.length;
// Otherwise initialize the value of frequency to a
//  the total number of terms (relative frequency)
            }
// Insert the new entry in the table
// System.out.println (toPut [0]);
            textData.put(textSplit[i], toPut);
        }

// You try to open the knowledge base
        ListCategory list;
        try {
            list = openList();
        } catch (ClassNotFoundException e) // error opening file kb.sbt
        {
            throw new RemoteException("The knowledge base is missing or corrupt");
        } catch (Exception e) {
            throw new RemoteException("The knowledge base is missing or corrupt");
        }

// Is taken from the base of knowledge to the table of total time
        Hashtable<String, float[]> totTermini = list.getTotTerms();
        float maxSimilarity = -1; // holds the value of maximum similarity
        for (String CategoryName : list.categories()) {// For all the categories in the knowledge base
            float[] toPut = new float[3]; // value to assign to the String
// In the hash table
            for (Enumeration<String> val = textData.keys(); val.hasMoreElements();) {// For all elements of the table
                                                                                      // of the terms of the text
                String term = val.nextElement();
// We get the value of a term
                float[] tmp = textData.get(term);
// Calculate range and bearing
                tmp[1] = distance(term, totTermini, tmp[0], list.getCategory(CategoryName));
                tmp[2] = relevance(tmp[1], list.getMaxDist());
                textData.put(term, tmp);
            }
// We calculate the similarity Once the analysis
// All the terms in a category
            toPut[0] = similarity(textData);
            textCategory.put(CategoryName, toPut);
            if (maxSimilarity < toPut[0]) // we update the value of maximum
// If necessary similarity
            {
                maxSimilarity = toPut[0];
            }
        }

        for (String CategoryName : list.categories()) {// For each category
// We get the value of similarity of the text with the category
// Analyzed
            float[] tmp = textCategory.get(CategoryName);
            tmp[1] = membership(tmp[0], maxSimilarity); // we calculate
// Membership
// Text to the similarity
            textCategory.put(CategoryName, tmp); // save everything in
// Category table
        }

        return maxMembership(textCategory); // returns the name output
// Category
// With the maximum degree of membership
    }

    /**
     * Method to find the category with which the text has the highest degree of
     * Membership
     *
     * @Param pTextCategory table of categories to the text 
     * @return a String indicating the name of the category with which The text has the
     * highest degree of membership
     */
    private static String maxMembership(Hashtable<String, float[]> pTextCategory) {
        String toReturn = null; // return value
        float max = -1; // Maximum value of membership

        for (Enumeration<String> elm = pTextCategory.keys(); elm.hasMoreElements();) {// For all categories of the
                                                                                        // table of categories of text
            String category = elm.nextElement();
// Values are taken of similarity and belonging associated with
// Category
            float[] tmp = pTextCategory.get(category);
            if (tmp[1] > max) {// If the degree of membership affiliation just uploaded
// Is greater than the previous update data max and toReturn
                toReturn = category;
                max = tmp[1];
            }

        }
        return toReturn;
    }

    /**
     * Method used to check whether a term is presented in table The terms of the
     * text
     *
     * @Param pStr period to analyze @Param pTable tables in terms of the text 
     * @Return returns true if the term exists false otherwise
     */
    private static boolean exists(String pStr, Hashtable<String, float[]> pTable) {
        try {
            if (pTable.get(pStr) != null)
                return true;
        } catch (NullPointerException e) {
            return false;
        }

        return false;
    }

    /**
     * Method used to retrieve the knowledge base
     *
     * @return an object representing the type ElencoCategorie
     * KnowledgeBase 
     */
    private static ListCategory openList() throws IOException, ClassNotFoundException {
        File KBase = new File("kb.sbt");// you open the file kb.sbt
        FileInputStream kBaseStream = new FileInputStream(KBase); // creates
// A stream with the file
        ObjectInputStream kBaseObj = new ObjectInputStream(kBaseStream);
// Create a stream object with the file
        ListCategory toReturn;

        toReturn = (ListCategory) kBaseObj.readObject();
// Object is extracted and saved in the file returned in output
        return toReturn;
    }

    /**
     * Method used to create the file. Used in training
     *
     * @Param path String indicating the path in which to create the file 
     * @Return an ObjectOutputStream to the file created 
     */
    private static ObjectOutputStream createFile(String path) throws IOException {
        ObjectOutputStream toReturn;
        File f = new File(path); // file is created
        if (f.exists()) {
            f.delete();
        }
        FileOutputStream fOut = new FileOutputStream(path);
        toReturn = new ObjectOutputStream(fOut); // create the stream

        return toReturn;
    }

    /**
     * Method used to create the knowledge base
     *
     */
    public static void training() throws RemoteException {

        String[] listCategory = new String[4]; // array containing names of category be analyzed Knowledge base
        ListCategory list = new ListCategory();

        ObjectOutputStream listOut;
        try {
// Try to create the file
            listOut = createFile("kb.sbt");
        } catch (Exception e) {
            throw new RemoteException("Error creating file kb.sbt");
        }

        listCategory[0] = "art";
        listCategory[1] = "cinema";
        listCategory[2] = "sport";

        for (int i = 0; i < 3; i++)// for each category
        {
// Create a new object of type Category, which will contain all
// Category data to be analyzed
            Category toPutCat = new Category(listCategory[i]);
// If the inclusion of the category in the table of categories
// Not successful
// We throw an exception
            if (!list.addCategory(listCategory[i], toPutCat)) {
                throw new RemoteException("Error creating data of category" + listCategory[i]);
            }
// You try to read from the folder containing the lyrics of a
// Category
// 100 sample test
            for (Integer j = new Integer(1); j <= 100; j++) {
// Path of the folder category
                String path = "kb /" + listCategory[i] + "/" + j.toString();
// Try to read the file ith
                FileReader textReader;
                try {
                    textReader = new FileReader(path);
                } catch (FileNotFoundException e) {
// If the file does not exist it continues execution from
// File i +1
// System.out.println ( "Error on file" + path);
                    continue;
                }
                Scanner textScanner = new Scanner(textReader);
                while (textScanner.hasNextLine()) {
// Read the text file line by line
                    String txt = textScanner.nextLine();
                    txt = replaceAndLower(txt);
                    String[] toIterate = txt.split("");
                    for (int k = 0; k < toIterate.length; k++) {// For each end of the line
                        if (toIterate[k].length() <= 3) // remove the effect
// Undefined terms
// Relevant
                            continue;
                        float[] valTerm, valTotTerm;
// If the term is present in the table of terms
// The class analyzed
                        if (list.getCategory(listCategory[i]).termExists(toIterate[k])) {
// Its frequency is equal to the value stored in
// Table plus one the total number of
// Terms of the text
                            valTerm = list.getCategory(listCategory[i]).getVal(toIterate[k]);
                            valTerm[0] += (float) 1 / toIterate.length;
                            valTotTerm = list.getTerms(toIterate[k]);
                            valTotTerm[0] += (float) 1 / toIterate.length;
                        } else {
// otherwise it is equal to one  the total number of words of text
                            valTerm = new float[3];
                            valTotTerm = new float[1];
                            valTerm[0] = (float) 1 / toIterate.length;
                            valTotTerm[0] = (float) 1 / toIterate.length;
                        }
// save the values calculated in the table of terms of the category analyzed
                        list.setTerms(toIterate[k], valTotTerm);
                        list.getCategory(listCategory[i]).addTerm(toIterate[k], valTerm);
                        if (list.getMaxDist() < valTotTerm[0])
                            list.setMaxDist(valTotTerm[0]);
                    }
                }

            }
        }
        for (String categoryName : list.categories()) {// for each category
// is preflushed the table of terms
            Hashtable<String, float[]> termCategory = list.getCategory(categoryName).getTerms();

// all the terms are analyzed in the table of loaded terms
            for (Enumeration<String> enumTerm = termCategory.keys(); enumTerm.hasMoreElements();) {
// is calculating bearing and distance
                String term = enumTerm.nextElement();
                float[] val = termCategory.get(term);
                val[1] = distance(term, list.getCategory(categoryName), list.getTotTerms());
                val[2] = relevance(val[1], list.getMaxDist());
            }
// data is stored in the table of the terms of the class
            list.getCategory(categoryName).setTerm(termCategory);
        }
        try {// writing the results of operations on files
            listOut.writeObject(list);
        } catch (Exception e) {
            throw new RemoteException("Error writing file");
        }
    }
}