import ch.qos.logback.core.helpers.Transform;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

import java.io.*;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import static org.apache.commons.math3.complex.Complex.INF;


public class Main {

    private static HashMap<String , HashMap<String, Integer>> graph;
    private static ArrayList<String> vertexes;
    private static final int INF = Integer.MAX_VALUE;
    //private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors(); // Количество доступных процессоров
    private static void FloydAlgorithm(HashMap<String, HashMap<String, Integer>> graph) throws IOException {
        double[][] newGraph = convertToDoubleMatrix(graph);
        int n = newGraph.length;

        int[][] dist = new int[n][n];

        // Инициализация матрицы расстояний
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], INF);
            dist[i][i] = 0;
        }

        // Заполнение матрицы расстояний из исходного графа
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (newGraph[i][j] != 0) {
                    dist[i][j] = (int) newGraph[i][j];
                }
            }
        }

        long start = System.currentTimeMillis();

        // Алгоритм Флойда - параллельная версия
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (int k = 0; k < n; k++) {
            final int kk = k;
            List<Callable<Void>> tasks = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                final int ii = i;
                tasks.add(() -> {
                    for (int j = 0; j < n; j++) {
                        if (dist[ii][kk] != 0 && dist[kk][j] != 0 && dist[ii][kk] + dist[kk][j] < dist[ii][j]) {
                            dist[ii][j] = dist[ii][kk] + dist[kk][j];
                        }
                    }
                    return null;
                });
            }
            try {
                executor.invokeAll(tasks);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();

        long end = System.currentTimeMillis();
        System.out.println("Time of algorithm: " + (end - start) + " ms");
        System.out.println("Printing sorted graph...");
        //System.out.println(matrix);
        printGraph(convertToMapMatrix(dist), "sortedGraph.csv");
    }
    private static INDArray comparisonMatrix(INDArray matrix, INDArray buffMatrix){
        int n = matrix.columns();
        INDArray minimumMatrix = Nd4j.zeros(n,n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    //System.out.println(matrix.getDouble(i, j));
                    //System.out.println(buffMatrix.getDouble(i, j));
                    minimumMatrix.putScalar(i, j, Math.min(matrix.getDouble(i, j), buffMatrix.getDouble(i, j)));
                }else {
                    minimumMatrix.putScalar(i, j, 0);
                }
            }
        }
        return minimumMatrix;
    }
    private static double[][] convertToDoubleMatrix(HashMap<String, HashMap<String, Integer>> graph){
        double[][] result = new double[graph.size()][graph.size()];
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result.length; j++) {
                if (graph.containsKey("v"+i) && graph.get("v"+i).containsKey("v"+j)) {
                    result[i][j] = graph.get("v" + i).get("v" + j);
                }
            }
        }
        return result;
    }
    private static HashMap<String, HashMap<String, Double>> convertToMapMatrix(INDArray matrix){
        int n = matrix.columns();
        HashMap<String, HashMap<String, Double>> result = new HashMap<>();
        for (int i = 0; i < n; i++) {
            result.put("v"+i, new HashMap<>());
            for (int j = 0; j < n; j++) {
                result.get("v"+i).put("v"+j, matrix.getDouble(i, j));
            }
        }
        return result;
    }
    private static HashMap<String, HashMap<String, Integer>> convertToMapMatrix(int[][] matrix){
        int n = matrix.length;
        HashMap<String, HashMap<String, Integer>> result = new HashMap<>();
        for (int i = 0; i < n; i++) {
            result.put("v"+i, new HashMap<>());
            for (int j = 0; j < n; j++) {
                result.get("v"+i).put("v"+j, matrix[i][j]);
            }
        }
        return result;
    }
    public static void main(String[] args) throws IOException {
        try {
            graphGenerator.main(args);
            System.out.println("start of unpacking graph...");
            unpackingGraph("graph.csv");
            System.out.println("start of FloydAlgorithm...");
            FloydAlgorithm(graph);
        }catch (Exception e){
            System.out.println("something wrong, try again");
            System.out.println(e);
        }
    }
    private static void unpackingGraph(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String[] line = reader.readLine().split(":");
        vertexes = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(line[1]); i++) {
            vertexes.add("v"+i);
        }
        String[] ver;
        graph = new HashMap<>();
        while (reader.ready()){
            line = reader.readLine().split(";");
            graph.put(line[0], new HashMap<>());
            for (int i = 1; i < line.length; i++) {
                ver = line[i].split(":");
                graph.get(line[0]).put(ver[0], Integer.parseInt(ver[1]));

            }
            for (String i:vertexes) {
                if (i.equals(line[0])){
                    graph.get(line[0]).put(i,0);
                }else if (!graph.get(line[0]).containsKey(i)){
                    graph.get(line[0]).put(i,1000000);
                }
            }
        }
    }
    private static void printGraph(HashMap<String, HashMap<String, Integer>> graph, String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write("vertexCnt:" + graph.size()+"\n");
        writer.flush();
        StringJoiner line;
        for (String i:graph.keySet()){
            line = new StringJoiner(";");
            line.add(i);
            for (String j:graph.get(i).keySet()){
                if (graph.get(i).get(j)!=0) {
                    line.add(j+":" + graph.get(i).get(j));
                }
            }
            writer.write(line + "\n");
            writer.flush();
        }

    }
}