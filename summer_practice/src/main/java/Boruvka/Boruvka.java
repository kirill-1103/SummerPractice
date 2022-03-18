package Boruvka;

import Graph.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Boruvka {
    private Graph graph;
    private Graph tree;
    private Edge[] cheapest;
    BoruvkaSteps steps;

    public Boruvka(Graph graph) {
        steps = new BoruvkaSteps();
        this.graph = graph;//входной граф
        tree = new Graph(new ArrayList<Edge>(), graph.getVertexes());//итоговое дерево
        cheapest = new Edge[graph.getVertexes()
                .size()];//здесь для каждой компоненты связности хранится самое дешевое ребро
        int size = graph.getCountVertexes();
        UnionFind unionFind = new UnionFind(size);//Отвечает за проверку входят в одну компоненту связности 2 вершины или нет; а также за объединение компонент
        for (int i = 1; i < size && tree.getEdges()
                .size() < size - 1; i *= 2) {
            for (Edge edge : graph.getEdges()) {
                int vertex1 = edge.getVertex1();
                int vertex2 = edge.getVertex2();
                int weight = edge.getWeight();
                int vertex1Parent = unionFind.find(vertex1);//родитель дерева в котором расположена вершина
                int vertex2Parent = unionFind.find(vertex2);

                if (vertex1Parent != vertex2Parent) {//если vertex1 и vertex2 не входят в одну компоненту
                    if (cheapest[graph.getVertexes()
                            .indexOf(vertex1Parent)] == null || cheapest[graph.getVertexes()
                            .indexOf(vertex1Parent)].getWeight() > weight) {//если для этой компоненты это минимальное ребро
                        cheapest[graph.getVertexes()
                                .indexOf(vertex1Parent)] = new Edge(vertex1, vertex2, weight);
                    }
                    if (cheapest[graph.getVertexes()
                            .indexOf(vertex2Parent)] == null
                            || cheapest[graph.getVertexes()
                            .indexOf(vertex2Parent)].getWeight() > weight) {
                        cheapest[graph.getVertexes()
                                .indexOf(vertex2Parent)] = new Edge(vertex1, vertex2, weight);
                    }
                }
            }
            for (Edge edge_ : tree.getEdges()) {
                edge_.setMarkLastAdded(false);
            }
            for (Integer j : graph.getVertexes()) {
                if (cheapest[graph.getVertexes()
                        .indexOf(j)] != null) {
                    Edge edge = cheapest[graph.getVertexes()
                            .indexOf(j)];
                    int vertex1Parent = unionFind.find(edge.getVertex1());
                    int vertex2Parent = unionFind.find(edge.getVertex2());
                    if (vertex1Parent != vertex2Parent) {
                        unionFind.union(vertex1Parent, vertex2Parent);
                        try {
                            Edge newEdge = new Edge(edge.getVertex1(), edge.getVertex2(), edge.getWeight());
                            newEdge.setMarkLastAdded(true);
                            tree.addEdge(newEdge);
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
            }
            saveBoruvkaStep();
            Arrays.fill(cheapest, null);
        }
        for (Edge edge_ : tree.getEdges()) {
            edge_.setMarkLastAdded(false);
        }
        saveBoruvkaStep();

    }

    private void saveBoruvkaStep() {
        steps.addStep(tree.getEdges());
    }

    public BoruvkaSteps getBoruvkaSteps() {
        return steps;
    }

    class UnionFind {
        private int[] parents;
        private int[] ranks;

        UnionFind(int n) {
            parents = new int[n];
            ranks = new int[n];
            for (Integer i : Boruvka.this.graph.getVertexes()) {
                parents[Boruvka.this.graph.getVertexes()
                        .indexOf(i)] = i;
                ranks[Boruvka.this.graph.getVertexes()
                        .indexOf(i)] = 0;
            }
        }

        public int find(int v) {
            while (v != parents[Boruvka.this.graph.getVertexes()
                    .indexOf(v)]) {
                v = parents[Boruvka.this.graph.getVertexes()
                        .indexOf(v)];
            }
            return v;
        }

        public void union(int v1, int v2) {
            int vertex1Parent = find(v1);
            int vertex2Parent = find(v2);
            if (vertex1Parent == vertex2Parent) {
                return;
            }
            if (ranks[Boruvka.this.graph.getVertexes()
                    .indexOf(vertex1Parent)] < ranks[Boruvka.this.graph.getVertexes()
                    .indexOf(vertex2Parent)]) {
                parents[Boruvka.this.graph.getVertexes()
                        .indexOf(vertex1Parent)] = vertex2Parent;
            } else if (ranks[Boruvka.this.graph.getVertexes()
                    .indexOf(vertex2Parent)] < ranks[Boruvka.this.graph.getVertexes()
                    .indexOf(vertex1Parent)]) {
                parents[Boruvka.this.graph.getVertexes()
                        .indexOf(vertex2Parent)] = vertex1Parent;
            } else {
                parents[Boruvka.this.graph.getVertexes()
                        .indexOf(vertex2Parent)] = vertex1Parent;
                ranks[Boruvka.this.graph.getVertexes()
                        .indexOf(vertex1Parent)]++;
            }
        }
    }
}


