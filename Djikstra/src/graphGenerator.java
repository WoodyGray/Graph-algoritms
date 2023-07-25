import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

public class graphGenerator {
    public static void main(String[] args) {
        try {
            Ini config = new Ini(new File("configToCraft.ini"));
            int vertexCnt = Integer.parseInt(config.get("parametersToCraft", "vertexCnt"));
            int maxWeight = Integer.parseInt(config.get("parametersToCraft", "maxOwnership"));
            int minRibCnt = Integer.parseInt(config.get("parametersToCraft", "minRibCnt"));
            System.out.println("start of initialization graph...");
            initGraph(vertexCnt, maxWeight, minRibCnt);
        } catch (InvalidFileFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void initGraph(int vertexCnt, int maxWeight, int minRibCnt){
        int[][] adjacencyMap = new int[vertexCnt][vertexCnt];
        //ArrayList<ArrayList<Integer>> adjacencyMap = new ArrayList<>();
        int ribCnt;
        boolean run;
        int weight;
        for (int i = 0; i < vertexCnt; i++) {
            if (i != 0){
                weight = (int) Math.round(Math.random() * (maxWeight - 1) + 1);
                adjacencyMap[0][i] = weight;
                adjacencyMap[i][0] = weight;
            }

            ribCnt = (int) Math.round(Math.random()*(vertexCnt -2)+1);
            while (ribCnt > minRibCnt){
                ribCnt = (int) Math.round(Math.random()*(vertexCnt -2)+1);
            }
            while (ribCnt !=0){
                int j = (int) Math.round(Math.random()*(vertexCnt-1));
                if (i != j){
                    if (adjacencyMap[i][j] == 0){
                        weight = (int) Math.round(Math.random() * (maxWeight - 1) + 1);
                        adjacencyMap[i][j]=weight;
                        adjacencyMap[j][i]=weight;
                        ribCnt--;
                    }else {
                        ribCnt--;
                    }
                }

            }
        }
        try {
            //System.out.println(adjacencyList.size());
            System.out.println("printing graph to file...");
            printGraph(adjacencyMap, "graph.csv");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    private static void printGraph(int[][] adjacencyList, String path) throws IOException {
        BufferedWriter graphFile = new BufferedWriter(new FileWriter(path));
        StringJoiner line = new StringJoiner(";");

        for (int vertex=0;vertex<adjacencyList.length;vertex++)
         {
            line.add(String.valueOf(vertex));
            for (int toVer=0;toVer<adjacencyList[vertex].length;toVer++
            ) {
                if (adjacencyList[vertex][toVer] != 0) {
                    line.add(toVer + ":" + adjacencyList[vertex][toVer]);
                }
            }
            graphFile.write(line +"\n");
            graphFile.flush();
            line = new StringJoiner(";");
        }
    }
}
