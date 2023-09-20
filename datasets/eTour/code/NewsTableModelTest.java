/**
 * Class tests for NewsTableModel
 *
 * @ Author Mario Gallo
 * @ Version 0.1
 *
 *  2007 eTour Project - Copyright by DMI SE @ SA Lab - University of Salerno
 */
package unisa.gps.etour.gui.operatoragency.tables.test;

import java.util.ArrayList;
import java.util.Date;

import unisa.gps.etour.bean.BeanNews;
import unisa.gps.etour.gui.operatoragency.tables.NewsTableModel;
import junit.framework.TestCase;

public class NewsTableModelTest
{

private NewsTableModel TableModel;
private BeanNews aNews;
private BeanNews aNewsModify;

public NewsTableModelTest (String pName)
{
super (pName);
aNews = new BeanNews ( "An example of news", new Date (), new Date (), 2,1);
aNewsModify = new BeanNews ( "A news amended sample", new Date (), new Date (), 3,1);
}

protected void setUp () throws Exception
{
super.setUp ();
NewsTableModel = new TableModel ();
}

/*
 * Verify the behavior of the manufacturer with an ArrayList of BeanNews.
 */
public void testConstructorWithArrayList ()
{
ArrayList <BeanNews>  test = new ArrayList <BeanNews> ();
for (int i = 0; i <10; i++)
{
test.add (new BeanNews ( "text" + i, new Date (), new Date (), 5, i));
}
NewsTableModel = new TableModel (test);
for (int i = 0; i <10; i++)
{
assertSame (test.get (i). getId (), tableModel.getID (i));
}

}

/*
 * Verify the manufacturer with an ArrayList Compor zero.
 */
public void testConstructorWithArrayListNull ()
{
NewsTableModel = new TableModel (null);
}

/*
 * Verify the behavior of the manufacturer with an empty ArrayList.
 */
public void testConstructorWithArrayListEmpty ()
{
NewsTableModel = new TableModel (<BeanNews> new ArrayList ());
}

/*
 * Verify the behavior of the method with the correct parameters.
 */
public void testGetValueAtParametersCorrect ()
{
// Put bean in two model test.
tableModel.insertNews (aNews);
tableModel.insertNews (aNewsModify);

// Verify the data entered.
assertSame (aNews.getNews (), tableModel.getValueAt (0, 0));
assertSame (aNews.getPriorita (), tableModel.getValueAt (0, 1));
assertSame (aNewsModify.getNews (), tableModel.getValueAt (1, 0));
assertSame (aNewsModify.getPriorita (), tableModel.getValueAt (1, 1));
}

/*
 * Verify Compor the method with an index row fold.
 */
public void testGetValueAtLineBusted ()
{
try
{
tableModel.getValueAt (12, 0);
fail ( "Should be thrown");
}
catch (IllegalArgumentException success)
{
}
}

/*
 * Verify Compor of the method with a column index busted.
 */
public void testGetValueAtColumnBusted ()
{
try
{
tableModel.getValueAt (0, -121334);
fail ( "Should be thrown");
}
catch (IllegalArgumentException success)
{
}
}

/*
 * Verify Compor method with proper parameter.
 */
public void testInsertNewsParameterCorrect ()
{
tableModel.insertNews (aNews);
assertSame (aNews.getId (), tableModel.getID (0));
}

/*
 * Verify Compor method with parameter to null
 */
public void testInsertNewsParameterNull ()
{
try
{
tableModel.insertNews (null);
fail ( "Should be thrown");
}
catch (IllegalArgumentException success)
{
}
}

/*
 * Verify Compor method with proper parameter.
 */
public void testUpdateNewsParameterCorrect ()
{
tableModel.insertNews (aNews);
tableModel.updateNews (aNewsModify);
assertSame (aNewsModify.getNews (), tableModel.getValueAt (0, 0));
assertSame (aNewsModify.getPriorita (), tableModel.getValueAt (0, 1));
assertSame (aNewsModify.getId (), tableModel.getID (0));
}

/*
 * Verify Compor method with parameter to null
 */
public void testUpdateNewsParameterNull ()
{

try
{
tableModel.updateNews (null);
fail ( "Should be thrown");
}
catch (IllegalArgumentException success)
{
}
}

/*
 * Verify Compor method with proper parameter.
 */
public void testRemoveNewsParameterCorrect ()
{
tableModel.insertNews (aNews);
assertSame (aNews.getId (), tableModel.removeNews (0));
}

/*
 * Verify Compor of the method with row index busted.
 */
public void testRemoveNewsLineBusted ()
{
try
{
tableModel.removeNews (-1231);
fail ( "Should be thrown");
}
catch (IllegalArgumentException success) ()
}

} 
