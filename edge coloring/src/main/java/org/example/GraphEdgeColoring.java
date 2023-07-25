package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GraphEdgeColoring {
    private static int[] colors;

    public static void main(String[] args) throws IOException {
        //try {

            graphGenerator.main(args);
            List<String> passedVertexes = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader("graph.csv"));
            String[] line = reader.readLine().split("sdfs");
            List<Edge> edges = new ArrayList<>();
            int maxCntVertex =0;
            while (reader.ready()){
                line = reader.readLine().split(";");
                if (maxCntVertex < line.length-1){
                    maxCntVertex = line.length-1;
                }
                if (line.length != 1) {
                    //passedVertexes.add(line[0]);
                    for (int i = 1; i < line.length; i++) {
                        if (!passedVertexes.contains(line[i]+":"+line[0])&&!passedVertexes.contains(line[0]+":"+line[i])){
                            passedVertexes.add(line[0]+":"+line[i]);
                            edges.add(new Edge(line[0], line[i]));
                        }
                    }
                }else {
                    System.out.println(line[0]);
                }

            }
            // Пример графа с ребрами (A, B), (A, C), (B, C)
            /*List<Edge> edges = Arrays.asList(
                    new Edge("v1", "v2"),
                    new Edge("v2", "v3"),
                    new Edge("v2", "v3")
            );*/

            int numColors = getMinimumColors(edges);
            System.out.println("the most connected component:" + maxCntVertex);
            System.out.println("Minimum number of colors required: " + numColors);
        //}catch (Exception e){
            //System.out.println("something was wrong");
            //System.out.println(e);
        //}
    }

    public static int getMinimumColors(List<Edge> edges) {
        if (edges.isEmpty()) {
            return 0;
        }

        colors = new int[edges.size()];
        Arrays.fill(colors, -1);

        // Сортируем ребра по убыванию степени вершин
        edges.sort(Comparator.comparing((Edge edge) -> edge.getSource())
                .thenComparing(edge -> edge.getTarget())); // Добавляем стабильность сортировки


        int maxColor = 0;
        for (Edge edge : edges) {
            Set<Integer> adjacentColors = getAdjacentColors(edges, edge);
            int availableColor = 1;
            while (adjacentColors.contains(availableColor)) {
                availableColor++;
            }

            colors[edges.indexOf(edge)] = availableColor;
            maxColor = Math.max(maxColor, availableColor);
        }
        for (Edge edge:edges
             ) {
            System.out.println(edge.source + ":"+edge.target+"="+colors[edges.indexOf(edge)]);
        }
        return maxColor;
    }

    private static int getDegree(List<Edge> edges, String vertex) {
        int degree = 0;
        for (Edge edge : edges) {
            if (edge.getSource().equals(vertex) || edge.getTarget().equals(vertex)) {
                degree++;
            }
        }
        return degree;
    }

    private static Set<Integer> getAdjacentColors(List<Edge> edges, Edge edge) {
        Set<Integer> adjacentColors = new HashSet<>();
        for (Edge adjacentEdge : edges) {
            if (isAdjacent(edge, adjacentEdge)) {
                int color = colors[edges.indexOf(adjacentEdge)];
                if (color != -1) {
                    adjacentColors.add(color);
                }
            }
        }
        return adjacentColors;
    }

    private static boolean isAdjacent(Edge edge1, Edge edge2) {
        String source1 = edge1.getSource();
        String target1 = edge1.getTarget();
        String source2 = edge2.getSource();
        String target2 = edge2.getTarget();
        return (source1.equals(source2) || source1.equals(target2) || target1.equals(source2) || target1.equals(target2));
    }

    static class Edge {
        private String source;
        private String target;

        public Edge(String source, String target) {
            this.source = source;
            this.target = target;
        }

        public String getSource() {
            return source;
        }

        public String getTarget() {
            return target;
        }
    }
}


