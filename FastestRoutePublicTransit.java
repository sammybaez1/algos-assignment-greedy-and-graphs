/**
 * Public Transit
 * Author: Sammy Baez and Carolyn Yao
 * Does this compile? Y
 */

/**
 * This class contains solutions to the Public, Public Transit problem in the
 * shortestTimeToTravelTo method. There is an existing implementation of a
 * shortest-paths algorithm. As it is, you can run this class and get the solutions
 * from the existing shortest-path algorithm.
 */
public class FastestRoutePublicTransit {

  /**
   * The algorithm that could solve for shortest travel time from a station S
   * to a station T given various tables of information about each edge (u,v)
   *
   * @param S the s th vertex/station in the transit map, start From
   * @param T the t th vertex/station in the transit map, end at
   * @param startTime the start time in terms of number of minutes from 5:30am
   * @param lengths lengths[u][v] The time it takes for a train to get between two adjacent stations u and v
   * @param first first[u][v] The time of the first train that stops at u on its way to v, int in minutes from 5:30am
   * @param freq freq[u][v] How frequently is the train that stops at u on its way to v
   * @return shortest travel time between S and T
   */
  public int myShortestTravelTime(
    int S,
    int T,
    int startTime,
    int[][] lengths,
    int[][] first,
    int[][] freq
  ) {
	  int numVertices = lengths[0].length;
	  int totalWaitTime = 0;

	  //Will keep track of what the current time would be if we'd have taken a certain path between vertex u and v 
	  int simulatedCurrentTime[]=  new int[numVertices]; 
	  //We start at source station at given start Time
	  simulatedCurrentTime[S] = startTime;  

	  int currentTime = simulatedCurrentTime[S];

	  /*This will keep track of the parent of the current station so that 
	   * we can get the accurate current time by adding up the waitTime of all the stations 
	   * in the path that lead to the current station we're looking at 
	   */
	  int parent[] = new int[numVertices];

	  parent[S] = -1;// Source station has no parent 

	  // This is the array where we'll store all the final shortest times
	  int[] times = new int[numVertices];

	  // processed[i] will true if vertex i's shortest time is already finalized
	  Boolean[] processed = new Boolean[numVertices];

	  // Initialize all distances as INFINITE and processed[] as false
	  for (int v = 0; v < numVertices; v++) {
		  times[v] = Integer.MAX_VALUE;
		  processed[v] = false;
	  }

	  // Distance of source vertex from itself is always 0
	  times[S] = 0;

	  // Find shortest path to all the vertices
	  for (int count = 0; count < numVertices - 1 ; count++) {
		  // Pick the minimum distance vertex from the set of vertices not yet processed.
		  // u is always equal to source in first iteration.
		  // Mark u as processed.
		  int u = findNextToProcess(times, processed);
		  processed[u] = true;
		  if(parent[u] != -1){//If the current station is not the start station, get the current time 
			  currentTime = simulatedCurrentTime[u];
		  }
		  else{
			  currentTime = startTime;
		  }
		  // Update time value of all the adjacent vertices of the picked vertex.
		  for (int v = 0; v < numVertices; v++) {
			  if(!processed[v] && lengths[u][v]!=0){
				  //Parent of this station is the current station we're processing
				  parent[v] = u;
				  //This is the case where the first train has not arrived, so wait time will be the sum
				  // of waiting for the first train to arrive and the time it takes to reach the next station
				  if(u != v  && first[u][v] > currentTime){
					  totalWaitTime = (first[u][v] - currentTime) + lengths[u][v];
				  }

				  /* This is the case where the first train has already arrived, 
				   * wait time will be the sum of waiting for the next train and the time it takes to reach the next station
				   * waiting for the next train to arrive will be simulated by 
				   * looping and adding the freq[u][v] to first[u][v] until next train arrival > the current time 
				   */
				  else if(u != v && first[u][v] < currentTime){
					  int nextTrainTime = first[u][v];
					  while(nextTrainTime  < currentTime){
						  nextTrainTime += freq[u][v];
					  }
					  totalWaitTime = (nextTrainTime - currentTime) + lengths[u][v];
				  }

				  // Update time[v] if total weight of path from source to v through u is smaller than current value of time[v]
				  if (times[u] != Integer.MAX_VALUE && times[u]+totalWaitTime < times[v]) {
					  times[v] = times[u] + totalWaitTime;
					  //If the algorithm comes back this station we'll be able to check what the current time was at this station 
					  simulatedCurrentTime[v] += simulatedCurrentTime[parent[v]] + totalWaitTime;
				  }
			  }

		  }
	  }

	  return times[T];
  }

  /**
   * Finds the vertex with the minimum time from the source that has not been
   * processed yet.
   * @param times The shortest times from the source
   * @param processed boolean array tells you which vertices have been fully processed
   * @return the index of the vertex that is next vertex to process
   */
  public int findNextToProcess(int[] times, Boolean[] processed) {
    int min = Integer.MAX_VALUE;
    int minIndex = -1;

    for (int i = 0; i < times.length; i++) {
      if (processed[i] == false && times[i] <= min) {
        min = times[i];
        minIndex = i;
      }
    }
    return minIndex;
  }

  public void printShortestTimes(int times[]) {
    System.out.println("Vertex Distances (time) from Source");
    for (int i = 0; i < times.length; i++)
        System.out.println(i + ": " + times[i] + " minutes");
  }

  /**
   * Given an adjacency matrix of a graph, implements
   * @param graph The connected, directed graph in an adjacency matrix where
   *              if graph[i][j] != 0 there is an edge with the weight graph[i][j]
   * @param source The starting vertex
   */
  public void shortestTime(int graph[][], int source) {
    int numVertices = graph[0].length;

    // This is the array where we'll store all the final shortest times
    int[] times = new int[numVertices];

    // processed[i] will true if vertex i's shortest time is already finalized
    Boolean[] processed = new Boolean[numVertices];

    // Initialize all distances as INFINITE and processed[] as false
    for (int v = 0; v < numVertices; v++) {
      times[v] = Integer.MAX_VALUE;
      processed[v] = false;
    }

    // Distance of source vertex from itself is always 0
    times[source] = 0;

    // Find shortest path to all the vertices
    for (int count = 0; count < numVertices - 1 ; count++) {
      // Pick the minimum distance vertex from the set of vertices not yet processed.
      // u is always equal to source in first iteration.
      // Mark u as processed.
      int u = findNextToProcess(times, processed);
      processed[u] = true;

      // Update time value of all the adjacent vertices of the picked vertex.
      for (int v = 0; v < numVertices; v++) {
        // Update time[v] only if is not processed yet, there is an edge from u to v,
        // and total weight of path from source to v through u is smaller than current value of time[v]
        if (!processed[v] && graph[u][v]!=0 && times[u] != Integer.MAX_VALUE && times[u]+graph[u][v] < times[v]) {
          times[v] = times[u] + graph[u][v];
        }
      }
    }

    printShortestTimes(times);
  }

  public static void main (String[] args) {
    /* length(e) */
    int lengthTimeGraph[][] = new int[][]{
      {0, 4, 0, 0, 0, 0, 0, 8, 0},
      {4, 0, 8, 0, 0, 0, 0, 11, 0},
      {0, 8, 0, 7, 0, 4, 0, 0, 2},
      {0, 0, 7, 0, 9, 14, 0, 0, 0},
      {0, 0, 0, 9, 0, 10, 0, 0, 0},
      {0, 0, 4, 14, 10, 0, 2, 0, 0},
      {0, 0, 0, 0, 0, 2, 0, 1, 6},
      {8, 11, 0, 0, 0, 0, 1, 0, 7},
      {0, 0, 2, 0, 0, 0, 6, 7, 0}
    };
    FastestRoutePublicTransit t = new FastestRoutePublicTransit();
    t.shortestTime(lengthTimeGraph, 0);

    // You can create a test case for your implemented method for extra credit below
    int lengthTimeGraph2[][] = new int[][]{
    	{0, 3, 0, 6, 0},
    	{3, 0, 2, 0, 7},
    	{0, 2, 0, 0, 5},
    	{6, 0, 0, 0, 4},
    	{0, 7, 5, 4, 0},
    };


    int firstTimeGraph[][] = new int[][]{
    	{-1, 10, -1, 30, -1},
    	{10, -1, 0, -1, 5},
    	{-1, 0, -1, -1, 20},
    	{30, -1, -1, -1, 25},
    	{-1, 5, 20, 25, -1},
    };


    int freqTimeGraph[][] = new int[][]{
    	{0, 5, 0, 3, 0},
    	{5, 0, 10, 0, 6},
    	{0, 10, 0, 0, 7},
    	{3, 0, 0, 0, 7},
    	{0, 6, 7, 7, 0},
    };
    
       int time = t.myShortestTravelTime(0, 4, 3, lengthTimeGraph2, firstTimeGraph, freqTimeGraph);
       System.out.println("Travel time from 0 to 4 is : " +  time);
    
  }
}

