/**
 * The class that implements the Runnable interface is the one
 * that defines what is to be done by each 'thread' running concurrently.
 * In our case, grabbing pages from the 'work' queue and processing them.
 *
 */

public class ConcurrentSpider implements Runnable {
    private String beginningURL = null;
    /**
     * Helps download and parse the web pages.
     */
    private HttpHelper helper = new HttpHelper();
    /**
     * Maximum number of urls that should be scraped.
     */
    private int maxUrls = 3;   // you can experiment with this value	
    private int urlCount = 0;


    // To continue the 'pattern' from RunThreadedSpider, have this shared data
    // be passed into the constructor from the single place where it was originally created.
    private SharedSpiderData sharedData;
	
    /**
     * Create a new spider with access to the shared data by passing it a reference.
     * This constructor could be called many times.
     * @param data
     */
    public ConcurrentSpider(SharedSpiderData data) {
	sharedData = data;
    }

    /**
     * Create a new spider with access to the shared data by passing it a reference.
     * Also give it the starting point to begin scraping.  This constructor should be called
     * once.
     * @param data
     * @param startURL
     */
    public ConcurrentSpider(SharedSpiderData data, String startURL) {
	sharedData = data;
	beginningURL = startURL;
    }
	
    /**
     * The method that is executed when you 'start()' a thread with this class.
     * Thus, the thread behavior is here in this run method.
     */
    public void run() {
	// initial thread to put the starting point
	// URL onto the work queue.
	if (beginningURL != null) {  
	    try {
		sharedData.getWork().put(beginningURL);
	    } catch (InterruptedException e) {
		// catch errors that can occur from the 'put' to the shared queue
		System.out.println("Error putting data into work queue");
		e.printStackTrace();
	    }
	}
		
	while (!sharedData.getWork().isEmpty() && urlCount <= maxUrls) {  // each thread does a certain amount of 'work'
	    String url= sharedData.getWork().remove();

	    //grab from the work queue and process the page
        if(!sharedData.getFinished().contains(url)){
            processPage(url);
            sharedData.getFinished().add(url);
        }
	    
	    urlCount++;    
	}
	System.err.println("ConcurrentSpider done with URLs");
    }
		
    /**
     * Retrieves content from a url and processes that content. 
     * @param url
     */
    public void processPage(String url) {
	String html = helper.retrieve(url);
    if(html==null) return;
	for (String url2 : helper.extractLinks(url, html)) {
	    System.out.println("next URL on " + url + ":" + url2);
	    if (!helper.isImage(url2)) {
		//process the page here
            try{
                sharedData.getWork().put(url2);
                sharedData.getUrlCounter().countWord(url2);
            }catch(InterruptedException ex){
                System.out.println("Couldn't add data to the queue");
            }
		
	    }
	}
    }

}
