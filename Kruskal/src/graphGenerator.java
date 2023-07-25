import org.ini4j.Ini;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringJoiner;

public class graphGenerator {
    private static int[][] graph;

    public static void main(String[] args) {
        try {
            Ini config = new Ini(new File("configToCraft.ini"));
            Integer vertexCnt = Integer.parseInt(config.get("parametersToCraft", "vertexCnt"));
            Integer maxWeight = Integer.parseInt(config.get("parametersToCraft", "maxOwnership"));
            Integer maxRibCnt = Integer.parseInt(config.get("parametersToCraft", "maxRibCnt"));
            System.out.println("start of initialization graph...");
            initGraph(vertexCnt, maxRibCnt, maxWeight);
        }catch (Exception e){
            System.out.println("something went wrong");
        }

    }
    private static void initGraph(Integer vertexCnt, Integer maxRibCnt, Integer maxWeight) throws IOException {
        graph = new int[vertexCnt][vertexCnt];
        int ribCnt;
        int weight;
        int vertex;
        for (int i = 0; i < vertexCnt; i++) {
            ribCnt = (int) Math.round(Math.random()*(maxRibCnt-1)+1);
            for (int j = 0; j < ribCnt; j++) {
                vertex = (int) Math.round(Math.random() * (vertexCnt - 1));
                while (graph[i][vertex] != 0 && vertex == i) {
                    vertex = (int) Math.round(Math.random() * (vertexCnt - 1));
                }
                weight = (int) Math.round(Math.random() * (maxWeight - 1) + 1);
                graph[i][vertex] = weight;
                //graph[vertex][i] = weight;
            }
        }
        System.out.println("printing graph...");
        printGraph(graph, "graph.csv");
    }
    private static void printGraph(int[][] graph, String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write("vertexCnt:" + graph.length+"\n");
        writer.flush();
        StringJoiner line;
        for (int i = 0; i < graph.length; i++) {
            line = new StringJoiner(";");
            line.add("v"+i);
            for (int j = 0; j < graph.length; j++) {
                if (graph[i][j]!=0) {
                    line.add("v" + j + ":" + graph[i][j]);
                }
            }
            writer.write(line + "\n");
            writer.flush();
        }

    }
}
