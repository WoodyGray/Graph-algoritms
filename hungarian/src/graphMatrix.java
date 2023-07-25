import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

public class graphMatrix {
    private static int[][] graphMatrix;
    public static void main(String[] args) {
        try {
            Ini config = new Ini(new File("configToCraft.ini"));
            int vertexCnt = Integer.parseInt(config.get("parametersToCraft", "vertexCnt"));
            int maxOwnership = Integer.parseInt(config.get("parametersToCraft", "maxOwnership"));
            initGraph(vertexCnt, maxOwnership);
            //initGraphMatrix(workersCnt, techCnt, maxOwnership);
        }catch (InvalidFileFormatException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static void initGraph(int vertexCnt, int maxWeight ){
        int cntOfDole = (int) Math.round(Math.random()*vertexCnt);
        //System.out.println(cntOfDole);
        if (cntOfDole == 0){
            cntOfDole =1;
        }
        initGraphMatrix(cntOfDole, vertexCnt-cntOfDole, maxWeight);
        HashMap<String, ArrayList<String>> adjacencyList = new HashMap<>();
        int id = 0;
        for (int i = 0; i < graphMatrix.length; i++) {
            adjacencyList.put("ver"+id, new ArrayList<>());
            for (int j = 0; j < graphMatrix[i].length; j++) {
                if (graphMatrix[i][j] != 0) {
                    adjacencyList.get("ver" + id).add("ver" + (graphMatrix.length  + j) + ":" + graphMatrix[i][j]);
                }
            }
            id++;
        }
        for (int i = 0; i < graphMatrix[0].length; i++) {
            adjacencyList.put("ver"+id, new ArrayList<>());
            for (int j = 0; j < graphMatrix.length; j++) {
                if (graphMatrix[j][i] != 0) {
                    adjacencyList.get("ver" + id).add("ver" + j + ":" + graphMatrix[j][i]);
                }
            }
            id++;
        }
        try {
            //System.out.println(adjacencyList.size());
            printGraph(adjacencyList, "graph.csv");
        }catch (Exception e){
            System.out.println(e);
        }

    }
    private static void printGraph(HashMap<String, ArrayList<String>> adjacencyList, String path) throws IOException {
        BufferedWriter graphFile = new BufferedWriter(new FileWriter(path));
        StringJoiner line = new StringJoiner(";");
        for (String vertex:adjacencyList.keySet()
             ) {
            line.add(vertex);
            for (String toVer: adjacencyList.get(vertex)
                 ) {
                line.add(toVer);
            }
            graphFile.write(line +"\n");
            graphFile.flush();
            line = new StringJoiner(";");
        }
    }
    private static void initGraphMatrix(int workerCnt, int techCnt, int maxOwnership){
        System.out.println("start of generating matrix...");
        graphMatrix = new int[workerCnt][techCnt];
        for (int i = 0; i < workerCnt; i++) {
            for (int j = 0; j < techCnt; j++) {
                int randOwnership = (int) Math.round((Math.random())*(maxOwnership));
                graphMatrix[i][j] = randOwnership;
            }
        }
        try {
            System.out.println("trying print matrix to file...");
            printGraphMatrix(graphMatrix, "graphMatrix.csv");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    private static void printGraphMatrix(int[][] matrix, String pathToPrint) throws IOException {
        BufferedWriter graphFile = new BufferedWriter(new FileWriter(pathToPrint));
        StringJoiner line = new StringJoiner(";");
        line.add("\\|/->");
        for (int i = 0; i < matrix[0].length; i++) {
            line.add("tech" + i);
        }
        graphFile.write(line.toString()+"\n");
        graphFile.flush();
        for (int i = 0; i < matrix.length; i++) {
            line = new StringJoiner(";");
            line.add("worker" + i);
            for (int j = 0; j < matrix[0].length; j++) {
                line.add(String.valueOf(matrix[i][j]));
            }
            graphFile.write(line.toString()+"\n");
            graphFile.flush();
        }
        System.out.println("print matrix to file complete");
    }

}
