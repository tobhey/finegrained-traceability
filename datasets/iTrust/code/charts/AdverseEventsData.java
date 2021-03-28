package edu.ncsu.csc.itrust.charts;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.links.CategoryItemLinkGenerator;
import de.laures.cewolf.tooltips.CategoryToolTipGenerator;
import edu.ncsu.csc.itrust.beans.AdverseEventBean;

/**
 * This class handles the data for charting in CeWolf/JFreeChart. This class implements DatasetProducer,
 * CategoryToolTipGenerator, CategoryItemLinkGenerator, and Serializable.
 * 
 * @author Jason
 *
 */
public class AdverseEventsData implements DatasetProducer, CategoryToolTipGenerator, CategoryItemLinkGenerator, Serializable {
	
	/**
	 * The generated serializable ID.
	 */
	private static final long serialVersionUID = 6145689621506271656L;

	// Hardcoded months array to make implementation simpler for Adverse Event charts
    private final String[] months = {"Jan","Feb","Mar","Apr","May","June","July","Aug","Sept","Oct","Nov","Dec"};
   
    // Initialize the values for each month to 0
    private int[] values = {0,0,0,0,0,0,0,0,0,0,0,0};
    
    // This will be the list of adverse events
    private List<AdverseEventBean> adverseEvents = new LinkedList<AdverseEventBean>();
    
    // This will be the name of the prescription or immunization under analysis
    private String codeName;
    
    /**
     * Called from the JSP page to initialize the list of Adverse Events needed to
     * produce the desired chart.
     * 
     * @param adEvents
     * @param name
     */
    public void setAdverseEventsList(List<AdverseEventBean> adEvents, String name)
    {
    	adverseEvents = adEvents;
    	this.codeName = name;
    }
    
    /**
     * This method parses the list of Adverse Event Beans to initialize the chart dataset.
     */
    @SuppressWarnings("unchecked")
	public Object produceDataset(Map params) throws DatasetProduceException {
    	// The DefaultCategoryDataset is used for bar charts.
    	// This dataset class may change based on the type of chart you wish to produce.
        DefaultCategoryDataset dataset = new DefaultCategoryDataset(){
			/**
			 * The generated serializable ID.
			 */
			private static final long serialVersionUID = -8238489914590553747L;

			/**
			 * @see java.lang.Object#finalize()
			 */
			protected void finalize() throws Throwable {
				super.finalize();
			}
        };
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        // For each Adverse Event in the list,
        // parse the string repreentation of the report date
        // to a Java Date object. Then, get the month of the
        // Date object and increment the value count for that month.
        for(AdverseEventBean event : adverseEvents)
        {
        	Calendar cal = Calendar.getInstance();
        	try {
				cal.setTime(sdf.parse(event.getDate()));
			} catch (ParseException e) {
				e.printStackTrace();
				throw new DatasetProduceException(e.getMessage());
			}
        	int monthOfReport = cal.get(Calendar.MONTH);
        	values[monthOfReport]++;
        }
        
        // For each month, add the monthly values to the dataset for
        // producing the chart.
        for(int i = 0; i < 12; i++)
        {
        	// values[i] represents the number of adverse events for month i
        	// codeName represents the given prescription/immunization being analyzed
        	// month[i] is the static array of month names, to be used as labels on the chart
        	dataset.addValue(values[i],codeName,months[i]);
        }
          
        return dataset;
    }

    /**
     * This producer's data is invalidated after 5 seconds. By this method the
     * producer can influence Cewolf's caching behaviour the way it wants to.
     */
	@SuppressWarnings("unchecked")
	public boolean hasExpired(Map params, Date since) {		
		return (System.currentTimeMillis() - since.getTime())  > 5000;
	}

	/**
	 * Returns a unique ID for this DatasetProducer
	 */
	public String getProducerId() {
		return "AdverseEventsData DatasetProducer";
	}

    /**
     * Returns a link target for a special data item.
     */
    public String generateLink(Object data, int series, Object category) {
        return months[series];
    }
    
	/**
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * @see org.jfree.chart.tooltips.CategoryToolTipGenerator#generateToolTip(CategoryDataset, int, int)
	 */
	public String generateToolTip(CategoryDataset arg0, int series, int arg2) {
		return months[series];
	}

}
