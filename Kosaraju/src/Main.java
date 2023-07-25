import org.ini4j.Ini;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.*;

public class Main {
    private int[] dist;
    private int[] prev;
    private Set<Integer> visited;
    private PriorityQueue<Vertex> pq;
    private ArrayList<HashMap<Integer,Integer>> graph;
    private static class Ver{
        int id;
        HashMap<Integer, Integer> edges;
        public Ver(int id){
            this.id = id;
            edges = new HashMap<>();
        }
    }

    public Main(ArrayList<HashMap<Integer, Integer>> graph) {
        this.graph = graph;
    }

    public List<Integer> findShortestPath(int source, int dest) {
        dist = new int[graph.size()];
        prev = new int[graph.size()];
        visited = new HashSet<>();
        pq = new PriorityQueue<>(graph.size(), new VertexComparator());

        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        pq.offer(new Vertex(source, 0));

        while (!pq.isEmpty()) {
            int curr = pq.poll().getVertex();
            visited.add(curr);

            //for (int neighbor = 0; neighbor < graph.size(); neighbor++) {
            for (Integer neighbor: graph.get(curr).keySet()) {
                int distance = graph.get(curr).get(neighbor);//[curr][neighbor];
                if (distance != 0 && !visited.contains(neighbor)) {
                    int alt = dist[curr] + distance;
                    if (alt < dist[neighbor]) {
                        dist[neighbor] = alt;
                        prev[neighbor] = curr;
                        pq.offer(new Vertex(neighbor, dist[neighbor]));
                    }
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        int curr = dest;
        while (curr != source) {
            path.add(curr);
            curr = prev[curr];
        }
        path.add(source);
        Collections.reverse(path);

        return path;
    }

    private static class Vertex {
        private int vertex;
        private int priority;

        public Vertex(int vertex, int priority) {
            this.vertex = vertex;
            this.priority = priority;
        }

        public int getVertex() {
            return vertex;
        }

        public int getPriority() {
            return priority;
        }
    }

    private static class VertexComparator implements Comparator<Vertex> {
        public int compare(Vertex v1, Vertex v2) {
            return v1.getPriority() - v2.getPriority();
        }
    }
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        graphGenerator.main(args);
        long endTime = System.currentTimeMillis();
        System.out.println("time of generate graph: " + (endTime-startTime));
        try {
            startTime = System.currentTimeMillis();
            System.out.println("unpacking graph...");
            ArrayList<HashMap<Integer, Integer>> adjacencyMap = unpackingGraph("graph.csv");
            endTime = System.currentTimeMillis();
            System.out.println("time of unpacking graph: "+(endTime-startTime));
            System.out.println("finding shortest distance...");
            startTime = System.currentTimeMillis();
            Main dijkstra = new Main(adjacencyMap);
            List<Integer> path = dijkstra.findShortestPath(0, 19999);

            System.out.println("Shortest path from 0 to 19999:");
            StringJoiner answer = new StringJoiner("->");
            for (Integer vertex : path) {
                answer.add(String.valueOf(vertex));


            }
            System.out.println(answer);
            endTime = System.currentTimeMillis();
            System.out.println("time of dijkstra algorithm: "+(endTime-startTime));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static ArrayList<HashMap<Integer, Integer>> unpackingGraph(String graphFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(graphFile));
        //Ini config = new Ini(new File("configToCraft.ini"));
        //int vertexCnt = Integer.parseInt(config.get("parametersToCraft","vertexCnt"));
        ArrayList<HashMap<Integer, Integer>> result = new ArrayList<>();
        String[] line;
        String[] vertex;
        while (reader.ready()){
            line = reader.readLine().split(";");
            result.add(new HashMap<>());
            for (int i = 1; i < line.length; i++) {
                vertex = line[i].split(":");

                if (!vertex[1].equals("0")){
                    result.get(result.size() -1).put(Integer.parseInt(vertex[0]), Integer.parseInt(vertex[1]));
                }
            }
        }
        return result;
    }
}