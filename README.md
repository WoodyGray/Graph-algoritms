# Графы и транспортные сети: Реализации алгоритмов

## Описание
Данный репозиторий содержит реализации различных алгоритмов на графах и транспортных сетях. Здесь вы найдете Венгерский алгоритм для поиска максимального паросочетания в двудольном графе, алгоритм Дейкстры находящий кратчайший путь в транспортной сети, алгоритм Флойда для поиска кратчайшего маршрута между всеми вершинами и реализацию произвольного метода реберной раскраски.

# 1. Венгерский алгоритм
На вход подается CSV файл с двудольным графом, где столбцы первая доля, строки вторая. Также есть парсер который попробует преобразовать обычный граф к двудольному. Результатом программы является максимальное парасочетание.

# 2. Алгоритм Дейкстры
Алгоритм Дейкстры - это алгоритм поиска кратчайшего пути во взвешенном графе с неотрицательными весами ребер. Особенность реализации в том, что программа может обработать большой граф за оптимальное время.

Алгоритм начинает работу с выбора стартовой вершины и установки для нее нулевого расстояния. Затем он переходит к соседним вершинам и обновляет их расстояния, если находит более короткий путь. Этот процесс повторяется, пока все вершины не будут посещены.

Алгоритм Дейкстры использует очередь с приоритетом для хранения вершин, которые нужно обработать. В начале алгоритма в очередь добавляется стартовая вершина с приоритетом 0. Затем на каждой итерации из очереди извлекается вершина с наименьшим приоритетом и обрабатывается.

Для каждой соседней вершины, которая еще не была обработана, алгоритм вычисляет новое расстояние, равное сумме расстояния до текущей вершины и веса ребра, соединяющего текущую вершину с соседней. Если это новое расстояние короче, чем текущее расстояние до соседней вершины, то оно обновляется.

Алгоритм продолжает работу, пока все вершины не будут обработаны, либо пока не будет найден кратчайший путь до конечной вершины. В результате выполнения алгоритма для каждой вершины графа будет найден кратчайший путь до стартовой вершины, а также его длина.

# 3. Алгоритм Флойда-Уоршалла
На вход подается матрица графа n x n, на выходе получаем отсортированную матрицу грава с кратчайшими путями.

Алгоритм Флойда-Уоршелла (также известный как алгоритм Флойда) — это алгоритм динамического программирования для нахождения кратчайших путей во всех парах вершин взвешенного ориентированного графа.

1) Создается матрица расстояний размера n × n, где n - это количество вершин в графе. Начальное значение каждого элемента в матрице - это вес ребра, которое соединяет соответствующие вершины, если такое ребро существует, иначе оно равно бесконечности.

2) Для каждой пары вершин (i, j) в графе, попробуем найти путь, который проходит через вершину k (1 <= k <= n), где k является промежуточной вершиной на пути от i к j. Если такой путь короче, чем текущий путь от i к j, обновлям значение элемента матрицы расстояний (i, j) на значение нового пути.

3) Повторяем шаг 2 для каждой промежуточной вершины k (1 <= k <= n).

4) После завершения шага 3, матрица расстояний будет содержать кратчайшие расстояния между всеми парами вершин в графе.

# 4. Произвольный метод реберной раскраски
На вход подается граф в виде CSV файла, результатом выводится минимальное кол-во различных цветов.

Метод реберной расскраски - это метод раскрашивания ребер графа таким образом, чтобы никакие два инцидентных ребра не имели одинаковый цвет.

Произвольный метод реберной расскраски заключается в следующих шагах:

1) Выбрать произвольное ребро из графа.

2) Присвоить этому ребру произвольный цвет, например, цвет 1.

3) Перейти к следующему ребру, которое не было раскрашено, и присвоить ему цвет, отличный от цвета любого инцидентного ребра.

4) Повторять шаг 3 для всех оставшихся нераскрашенных ребер графа.

# P.s. Весь код сделан в обучающих целях
Я не являюсь профессиональным разработчиком, данные программы сделаны в целях обучения, я никого не призываю использовать данный код как профессиональный.

# Graphs and transport networks: Algorithm Implementations

## Description
This repository contains implementations of various algorithms on graphs and transport networks. Here you will find the Hungarian algorithm for finding the maximum matching in a bipartite graph, Dijkstra's algorithm for finding the shortest path in a transport network, Floyd's algorithm for finding the shortest route between all vertices and the implementation of an arbitrary edge coloring method.

# 1. Hungarian algorithm
The input is a CSV file with a bipartite graph, where the columns are the first fraction, the rows are the second. There is also a parser that will try to convert an ordinary graph to a bipartite one. The result of the program is the maximum matching.

# 2. Dijkstra's algorithm
Dijkstra's algorithm is an algorithm for finding the shortest path in a weighted graph with non-negative edge weights. The peculiarity of the implementation is that the program can process a large graph in optimal time.

The algorithm starts by selecting a starting vertex and setting a zero distance for it. Then it goes to neighboring vertices and updates their distances if it finds a shorter path. This process is repeated until all vertices are visited.

Dijkstra's algorithm uses a priority queue to store the vertices that need to be processed. At the beginning of the algorithm, a starting vertex with priority 0 is added to the queue. Then, at each iteration, the vertex with the lowest priority is extracted from the queue and processed.

For each neighboring vertex that has not yet been processed, the algorithm calculates a new distance equal to the sum of the distance to the current vertex and the weight of the edge connecting the current vertex to the neighboring one. If this new distance is shorter than the current distance to the neighboring vertex, then it is updated.

The algorithm continues to work until all vertices are processed, or until the shortest path to the final vertex is found. As a result of the algorithm, for each vertex of the graph, the shortest path to the starting vertex will be found, as well as its length.

# 3. Floyd-Warshall algorithm
The input is a graph matrix n x n, at the output we get a sorted graph matrix with shortest paths.

The Floyd-Warshell algorithm (also known as Floyd's algorithm) is a dynamic programming algorithm for finding shortest paths in all pairs of vertices of a weighted directed graph.

1) A distance matrix of size n × n is created, where n is the number of vertices in the graph. The initial value of each element in the matrix is the weight of the edge that connects the corresponding vertices, if such an edge exists, otherwise it is equal to infinity.

2) For each pair of vertices (i, j) in the graph, let's try to find a path that passes through vertex k (1 <= k <= n), where k is an intermediate vertex on the path from i to j. If such a path is shorter than the current path from i to j, update the value of the distance matrix element (i, j) to the value of the new path.

3) Repeat step 2 for each intermediate vertex k (1 <= k <= n).

4) After completing step 3, the distance matrix will contain the shortest distances between all pairs of vertices in the graph.

# 4. Arbitrary edge coloring method
A graph is submitted to the input in the form of a CSV file, the result is a minimum number of different colors.

The edge coloring method is a method of coloring the edges of a graph in such a way that no two incident edges have the same color.

An arbitrary edge coloring method consists of the following steps:

1) Select an arbitrary edge from the graph.

2) Assign an arbitrary color to this edge, for example, color 1.

3) Go to the next edge that has not been colored and assign it a color different from the color of any incident edge.

4) Repeat step 3 for all remaining unpainted edges of the graph.

# P.s. All the code is made for educational purposes
I am not a professional developer, these programs are made for training purposes, I do not encourage anyone to use this code as a professional.
