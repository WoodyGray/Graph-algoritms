import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class HungarianAlgorithm {

    public static int[] maxMatching(ArrayList<ArrayList<Integer>> graph) {

        int n = graph.size(); // размер первой доли
        int m = graph.get(0).size(); // размер второй доли
        
        int[] u = new int[n+1]; // метки первой доли
        int[] v = new int[m+1]; // метки второй доли
        int[] p = new int[m+1]; // информация о путях в алгоритме
        int[] way = new int[m+1]; // информация о путях в алгоритме

        // выполняем цикл по всем вершинам первой доли графа
        for (int i = 1; i <= n; ++i) {
            // начинаем с i вершины
            p[0] = i;
            int j0 = 0;
            int[] minv = new int[m+1];
            //массив для хранение мин веса связей
            Arrays.fill(minv, Integer.MAX_VALUE);

            boolean[] used = new boolean[m+1];
            do {
                used[j0] = true;
                int i0 = p[j0], delta = Integer.MAX_VALUE, j1 = 0;
                for (int j = 1; j <= m; ++j) {
                    if (!used[j]) {
                        // вычисление веса ребра между i0 и j
                        int cur = graph.get(i0 - 1).get(j - 1) - u[i0] - v[j];
                        if (cur < minv[j]) {
                            // сохраняем в minv наименьшее значение для j
                            minv[j] = cur;
                            // сохраняем индекс предыдущей вершины
                            way[j] = j0;
                        }
                        if (minv[j] < delta) {
                            // чтобы она была минимальным весом ребер для всех ребер с непросмотреннами вершинами
                            delta = minv[j];
                            j1 = j;
                        }
                    }
                }
                for (int j = 0; j <= m; ++j) {
                    if (used[j]) {
                        // обновление потенциала вершини p[j] во множестве U
                        u[p[j]] += delta;
                        // обновление потенциала вершины j во множестве V
                        v[j] -= delta;
                    } else {
                        minv[j] -= delta;
                    }
                }
                // берем вершину с мин весом ребра
                j0 = j1;
            } while (p[j0] != 0); // пока путь не закончится в вершине 0

            do {
                int j1 = way[j0];
                p[j0] = p[j1]; // обновляем путь
                j0 = j1;
            } while (j0 != 0);
        }

        // после завершения алгоритма находим количество ребер в паросочетании и заполняем массив с результатом
        int matching = 0;
        int[] matchingResult = new int[n];
        for (int j = 1; j <= m; ++j)
            if (p[j] != 0) {
                matchingResult[p[j]-1] = j-1;
                matching++;
            }

        // возвращаем количество ребер в максимальном паросочетании
        return matchingResult;
    }

    public static void main(String[] args) throws IOException {

        //graphMatrix.main(args);
        BufferedReader graphFile = new BufferedReader(new FileReader("graph.csv"));
        HashMap<String, HashMap<String, Integer>> graph = new HashMap<>();
        String[] line;
        System.out.println("reading adjacency list start...");
        while (graphFile.ready()){
            line = graphFile.readLine().split(";");
            graph.put(line[0], new HashMap<>());
            String[] vertex;
            for (int i = 1; i < line.length; i++) {
                vertex = line[i].split(":");
                graph.get(line[0]).put(vertex[0], Integer.parseInt(vertex[1]));
            }

        }
        System.out.println("converting adjacency list to shares...");

        /*int[][] graph = {
                {2, 3, 0},
                {3, 1, 2},
                {2, 2, 2}
        };*/
        //System.out.println("Max matching: " + maxMatching(graph));
        ArrayList<ArrayList<Integer>> matrix = adjacencyListToMatrix(graph);
        if (matrix == null){
            System.out.println("something wrong, please return");
        }else {
            System.out.println("starting algorithm...");
            int[] result = maxMatching(matrix);
            //System.out.println(result);
            for (int i = 0; i < result.length; i++) {
                System.out.println("worker" + i + ": " + result[i]);
            }
        }
    }
    private static ArrayList<ArrayList<Integer>> adjacencyListToMatrix(HashMap<String, HashMap<String, Integer>> adjacencyList){
        ArrayList<HashSet<String>> shares = new ArrayList<>();
        HashSet<String> share;
        for (String vertex:adjacencyList.keySet()
             ) {
            //System.out.println(vertex);
            if (shares.size() == 0){
                share = new HashSet<>(adjacencyList.get(vertex).keySet());
                shares.add(share);
                shares.add(new HashSet<>(List.of(vertex)));
            }else {
                if (shares.size() > 2){
                    System.out.println(shares.size());
                    ArrayList<HashSet<String>> newShares = new ArrayList<>();
                    newShares.add(shares.get(0));
                    boolean runUsl;
                    for (int i = 1; i < shares.size(); i++) {
                        runUsl = true;
                        for (String ver: shares.get(i)
                             ) {
                            for (int j = 0; j < newShares.size() && runUsl; j++) {
                                if (newShares.get(j).contains(ver)){
                                    newShares.get(j).addAll(shares.get(i));
                                    runUsl = false;
                                }
                            }
                            if (!runUsl){
                                break;
                            }
                        }
                        if (runUsl){
                            newShares.add(shares.get(i));
                        }
                    }
                    shares = newShares;
                    System.out.println(shares.size());
                }
                int index1 = -1;
                for (int i = 0; i < shares.size(); i++) {
                    if (shares.get(i).contains(vertex)){
                        index1 = i;
                        shares.get(i).add(vertex);
                        break;
                    }
                }
                int index2 = -1;
                for (String ver: adjacencyList.get(vertex).keySet()
                ) {
                    for (int i = 0; i < shares.size(); i++) {
                        if (shares.get(i).contains(ver)){
                            index2 = i;
                            shares.get(i).addAll(adjacencyList.get(vertex).keySet());
                            break;
                        }
                    }
                    if (index2 != -1){
                        break;
                    }
                }
                if ((index2 != -1 || index1 != -1) && shares.size() == 2){
                    if (index2 == -1){
                        index2 = index1==1?0:1;
                        shares.get(index2).addAll(new HashSet<>(adjacencyList.get(vertex).keySet()));
                    } else if (index1 == -1) {
                        index1 = index2==1?0:1;
                        shares.get(index1).add(vertex);
                    }
                }else {
                    if (index1 == -1) {
                        share = new HashSet<>();
                        share.add(vertex);
                        shares.add(share);
                    }

                    if (index2 == -1) {
                        share = new HashSet<>(adjacencyList.get(vertex).keySet());
                        shares.add(share);
                    }
                }

            }
        }
        //System.out.println("count of shares: "+ shares.size());
        ArrayList<ArrayList<String>> sortedShares;
        if (shares.size() != 2){
            return null;
        }else {
            sortedShares = sortShares(shares);
            System.out.println("converting shares to matrix...");
            //sortedShares.get(0).forEach(System.out::print);
            return convertSharesToMatrix(sortedShares, adjacencyList);
        }

    }

    private static ArrayList<ArrayList<Integer>> convertSharesToMatrix(List<ArrayList<String>> shares, Map<String, HashMap<String, Integer>> adjacencyList){
        ArrayList<ArrayList<Integer>> graphMatrix = new ArrayList<>();
        for (String column: shares.get(0)
             ) {
            graphMatrix.add(new ArrayList<>());
            for (String line: shares.get(1)
                 ) {
                int value = adjacencyList.get(column).get(line) == null?0:adjacencyList.get(column).get(line);
                //System.out.println(value);
                graphMatrix.get(graphMatrix.size() - 1).add(value);
            }
        }
        return graphMatrix;
    }
    private static ArrayList<ArrayList<String>> sortShares(ArrayList<HashSet<String>> sharesSet){
        ArrayList<ArrayList<String>> sortedShares = new ArrayList<>();
        for (int i = 0; i < sharesSet.size(); i++) {
            sortedShares.add(new ArrayList<>(sharesSet.get(i)));
            sortedShares.get(i).sort(Comparator.comparingInt(s -> Integer.parseInt(s.substring(3))));
        }
        return sortedShares;
    }
}