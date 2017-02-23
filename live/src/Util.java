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
                videos.add(new Video(size));
            }

            lines.remove(0);

            //Reads all the endpoints

            List<Endpoint> endpoints = new ArrayList<Endpoint>();
            Map<Integer,Cache> idToCache = new HashMap<>();

            for(int i = 0; i < nrofendpoints; i++) {
            	String[] epLine = lines.get(0).split("[\\s]");				//Read the line for this endpoint
            	int cachesconnected = Integer.parseInt(epLine[1]);	    	//Get the nr of caches connected to it
                int ld = Integer.parseInt(epLine[0]);
            	Endpoint temp = new Endpoint(ld,cachesconnected);	         //Create the endpoint
                endpoints.add(temp);
            	lines.remove(0);											//Remove this line

            	//Read all the caches this endpoint is connected to 
            	for(int j = 0; j < cachesconnected; j++) {
            		String[] cacheLine = lines.get(0).split("[\\s]");		//Read the line for this specific cache
            		int cacheid = Integer.parseInt(cacheLine[0]);			//Read cacheid

            		if(idToCache.get(cacheid) == null) {					//If this cache doesn't exist, create it
            			idToCache.put(cacheid, new Cache(cachesize));
            		}

            		Cache c = idToCache.get(cacheid);						//Get the cache in question
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

            	Endpoint ep = endpoints.get(epId);							//Get the ep that generates the req
            	videos.get(vidId).requests.put(ep,reqs);					//Get the video and it's map, put the ep and the nr of requests

                return new World(nrofvideos,nrofendpoints,nrofcaches,cachesize,nrofrequests,endpoints,idToCache,videos);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Couldn't read file: " + filepath);
        }

        catch (IOException e) {
            System.out.println("IO Exception!");
        }

        return null;
    }

    public void printToFile (String path) {
        try {
            PrintWriter out = new PrintWriter(path);
            out.println("Stuff");
            out.println("More stuff");
            out.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("File not found!");
        }
    }
}