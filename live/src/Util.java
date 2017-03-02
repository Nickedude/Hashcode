import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.stream.Collectors;
import java.util.*;
import java.io.*;


public class Util {
   

   public World parseWorld(Path filepath) {
        try {

            List<String> lines = Files.lines(filepath, StandardCharsets.UTF_8).collect(Collectors.toList());

            //Reads the first row
            String[] firstLine = lines.get(0).split("[\\s]");
            int nrofvideos = Integer.parseInt(firstLine[0]);
            int nrofendpoints = Integer.parseInt(firstLine[1]);
            int nrofrequests = Integer.parseInt(firstLine[2]);
            int nrofcaches = Integer.parseInt(firstLine[3]);
            int cachesize = Integer.parseInt(firstLine[4]);

            lines.remove(0);

            //Reads the size of all videos
            List<Video> videos = new ArrayList<>();
            String[] secondLine = lines.get(0).split("[\\s]");

            for (int i = 0; i < nrofvideos; i++) {
                int size = Integer.parseInt(secondLine[i]);
                videos.add(new Video(size,i));
            }

            lines.remove(0);

            //Reads all the endpoints

            List<Endpoint> endpoints = new ArrayList<Endpoint>();
            Map<Integer,Cache> idToCache = new HashMap<>();

            for(int i = 0; i < nrofendpoints; i++) {
            	String[] epLine = lines.get(0).split("[\\s]");				//Read the line for this endpoint
            	int cachesconnected = Integer.parseInt(epLine[1]);	    	//Get the nr of caches connected to it
                int ld = Integer.parseInt(epLine[0]);
            	Endpoint temp = new Endpoint(ld,cachesconnected);	        //Create the endpoint
                endpoints.add(temp);
            	lines.remove(0);											//Remove this line

            	//Read all the caches this endpoint is connected to 
            	for(int j = 0; j < cachesconnected; j++) {
            		String[] cacheLine = lines.get(0).split("[\\s]");		//Read the line for this specific cache
            		int cacheid = Integer.parseInt(cacheLine[0]);			//Read cacheid


                    idToCache.putIfAbsent(cacheid,  new Cache(cachesize));    //If this cache doesn't exist, create it

            		Cache c = idToCache.get(cacheid);						//Get the cache in question
                    c.endpoints.add(temp);                                  //Make this cache aware of what endpoint is connected to it
            		int lat = Integer.parseInt(cacheLine[1]);				//Read the latency to this cache
            		temp.cacheLatMap.put(c,lat);							//Save the latency to this cache for this endpoint
            		lines.remove(0);
            	}
            }

            //Now only the requests remain
            while((lines.size() != 0)) {
            	String[] reqLine = lines.get(0).split("[\\s]");				//Read the line for this request
            	int vidId = Integer.parseInt(reqLine[0]);
            	int epId  = Integer.parseInt(reqLine[1]);
            	int reqs  = Integer.parseInt(reqLine[2]);
            	lines.remove(0);

            	Endpoint ep = endpoints.get(epId);
            	Integer noRequests = videos.get(vidId).requests.putIfAbsent(ep,reqs);;
            	if(!(noRequests == null)) {
                    videos.get(vidId).requests.put(ep,reqs + noRequests);					//Get the video and it's map, put the ep and the nr of requests
                    //Get the ep that generates the req
                }
            }
            System.out.println("Parse successfull!");
            return new World(nrofvideos,nrofendpoints,nrofcaches,cachesize,nrofrequests,endpoints,idToCache,videos);

        } catch (FileNotFoundException e) {
            System.out.println("Couldn't read file: " + filepath);
        }

        catch (IOException e) {
            System.out.println("IO Exception!");
        }

        return null;
    }

    public void printToFile (World w, String path) {
        try {
            PrintWriter out = new PrintWriter(path);
            int nr = 0;
            List<String> cstrs = new ArrayList<>();

            for(int i = 0; i < w.nrOfCaches; i++) {             //Iterate through all caches
                Cache c = w.caches.get(i);                      //Get next cache
                if(c.videos.size() != 0) {                      //If it's used
                    nr++;                               
                    String s = i +" ";                          //Start building a string
                    for(int j = 0; j < c.videos.size(); j++) {  //Loop through all it's videos
                        s = s + c.videos.get(j).id + " ";       //Add the video it uses to the string
                    }
                    cstrs.add(s);
                }
            }

            out.println(nr);
            for(int i = 0; i < cstrs.size(); i++) {
                out.println(cstrs.get(i));
            }

            out.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }
}