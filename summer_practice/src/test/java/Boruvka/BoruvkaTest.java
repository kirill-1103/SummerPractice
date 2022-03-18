package Boruvka;

import Input.CheckingCorrect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import Graph.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoruvkaTest {
    ArrayList<ArrayList<Integer>> matrix1;
    ArrayList<ArrayList<Integer>> matrix2;
    ArrayList<ArrayList<Integer>> matrix3;

    Graph graph1 ;
    Graph graph2 ;
    Graph graph3 ;


    @BeforeEach
    void setUp() {
       matrix1= new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(-1,1,4,8)),
                new ArrayList<>(Arrays.asList(1,-1,2,3)),
                new ArrayList<>(Arrays.asList(4,2,-1,5)),
                new ArrayList<>(Arrays.asList(8,3,5,-1))
        ));

        matrix2= new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(-1,1,-1,8)),
                new ArrayList<>(Arrays.asList(1,-1,-1,10)),
                new ArrayList<>(Arrays.asList(-1,-1,-1,5)),
                new ArrayList<>(Arrays.asList(8,10,5,-1))
        ));

        matrix3= new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(-1,-1,8,-1,1)),
                new ArrayList<>(Arrays.asList(-1,-1,6,-1,-1)),
                new ArrayList<>(Arrays.asList(8,6,-1,2,12)),
                new ArrayList<>(Arrays.asList(-1,-1,2,-1,-1)),
                new ArrayList<>(Arrays.asList(1,-1,12,-1,-1))
        ));

        try{
            graph1 = new Graph(matrix1);
            graph2 = new Graph(matrix2);
            graph3 = new Graph(matrix3);
        }catch (IOException e){
            Logger logger = LogManager.getLogger();
            logger.info("in BoruvkaTest: "+e.getMessage());
        }

    }

    @Test
    void test1(){
        Graph tree = getTree(graph1);
        int sumWeight = findWeight(tree);
        boolean expectedBool =  true;
        boolean actualBool = sumWeight==6 && !haveLoop(tree);
        assertEquals(expectedBool,actualBool);
    }

    @Test
    void test2(){
        Graph tree = getTree(graph2);
        int sumWeight = findWeight(tree);
        boolean expectedBool =  true;
        boolean actualBool = sumWeight==14 && !haveLoop(tree) ;

        assertEquals(expectedBool,actualBool);
    }

    @Test
    void test3(){
        Graph tree = getTree(graph3);
        int sumWeight = findWeight(tree);
        boolean expectedBool =  true;
        boolean actualBool = sumWeight==17 && !haveLoop(tree) ;

        assertEquals(expectedBool,actualBool);
    }

    private int findWeight(Graph tree){
        int sumWeight=0;
        for(Edge edge:tree.getEdges()){
            sumWeight+=edge.getWeight();
        }
        return sumWeight;
    }

    private Graph getTree(Graph graph){
        Boruvka boruvka = new Boruvka(graph);
        BoruvkaSteps steps = boruvka.getBoruvkaSteps();
        Optional<ArrayList<Edge>> edgesOpt = steps.lastStep();
        ArrayList<Edge> edges = edgesOpt.get();

        ArrayList<Integer> vertexes = new ArrayList<>(graph.getVertexes());
        Graph tree = new Graph(edges,vertexes);
        return tree;
    }

    private boolean haveLoop(Graph graph){
        Integer vertex ;
        int index = 0;
        while(index!=graph.getVertexes().size()){//удаляем вершины с единственным ребром, пока такие не закончатся
            vertex = graph.getVertexes().get(index);
            int quantityEdges = 0;
            Edge findedEdge = null;
            for(Edge edge:graph.getEdges()){
                if(edge.getVertex1()==vertex || edge.getVertex2()==vertex){
                    quantityEdges++;
                    findedEdge = edge;
                }
            }
            if(quantityEdges==1){
                graph.deleteEdge(findedEdge.getVertex1(),findedEdge.getVertex2());
                graph.deleteVertex(vertex);
                index=0;
            }
            index++;
        }
        return graph.getVertexes().size()!=1;
    }

}