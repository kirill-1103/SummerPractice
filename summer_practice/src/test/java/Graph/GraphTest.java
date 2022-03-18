package Graph;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;



class GraphTest {

    ArrayList<ArrayList<Integer>> defaultMatrix ;
    Graph defaultGraph ;

    @BeforeEach
    void setUp() {
        defaultMatrix = getMatrix();
        try{
            defaultGraph=new Graph(defaultMatrix);
        }catch (IOException e){
            Logger logger = LogManager.getLogger();
            logger.info("in GraphTest: "+e.getMessage());
        }
    }

    @Test
    void testConstructor(){
        try{
            Graph graph = new Graph(defaultMatrix);
        }catch (IOException e){
            assertEquals(0,1);
            Logger logger = LogManager.getLogger();
            logger.info("in GraphTest: "+e.getMessage());
        }
    }

    @Test
    void getCountVertexes() {
        assertEquals(5,defaultGraph.getCountVertexes());
    }

    @Test
    void getCountEdges() {
        assertEquals(5,defaultGraph.getCountEdges());
    }

    @Test
    void addVertexTestCountVertexes(){
        defaultGraph.deleteVertex(2);
        defaultGraph.deleteVertex(4);
        defaultGraph.addVertex();

        assertEquals(4,defaultGraph.getCountVertexes());
    }

    @Test
    void addVertexTestVertexesArray() {
        defaultGraph.deleteVertex(2);
        defaultGraph.deleteVertex(4);
        defaultGraph.addVertex();
        ArrayList<Integer> vertexes = new ArrayList<>(Arrays.asList(0,1,2,3));
        assertEquals(vertexes,defaultGraph.getVertexes());
    }

    @Test
    void deleteVertexTestCountVertexes() {
        defaultGraph.deleteVertex(3);
        assertEquals(4,defaultGraph.getCountVertexes());
    }

    @Test
    void deleteVertexTestVertexesArray() {
        defaultGraph.deleteVertex(3);
        assertEquals(new ArrayList<>(Arrays.asList(0,1,2,4)), defaultGraph.getVertexes());
    }

    @Test
    void deleteVertexTestEdgesArray() {
        defaultGraph.deleteVertex(3);
        ArrayList<Edge> edges1 = new ArrayList<Edge>(Arrays.asList(
                new Edge(0,4,1),
                new Edge(0,2,8),
                new Edge(4,2,12),
                new Edge(1,2,6)
        ));
        ArrayList<Edge> edges2 = new ArrayList<Edge>(defaultGraph.getEdges());
        edges1.sort((o1,o2)->o1.getWeight()-o2.getWeight());
        edges2.sort((o1,o2)->o1.getWeight()-o2.getWeight());
        assertEquals(edges1,edges2);
    }

    @Test
    void deleteVertexTestNonExistentVertex() {
        boolean b;
        try{
            defaultGraph = defaultGraph.copyGraph();
            defaultGraph.deleteVertex(5);
            b=false;
        }catch (IndexOutOfBoundsException e){
            b=true;
            Logger logger = LogManager.getLogger();
            logger.info("in graphTests: " + e.getMessage());
        }
        assertTrue(b);
    }

    @Test
    void addEdgeTestAddedEdge() {
        try {
            Edge newEdge = new Edge(1,3,12);
            defaultGraph.addEdge(newEdge);
            assertEquals(defaultGraph.getEdges().get(defaultGraph.getEdges().size() - 1), newEdge);
        }catch (IOException ex){
            Logger logger = LogManager.getLogger();
            logger.info("in GraphTest: +" + ex.getMessage());
        }
    }

    @Test
    void addEdgeTestCountEdges() {
        try {
            defaultGraph.addEdge(new Edge(1,3,12));
            assertEquals(defaultGraph.getCountEdges(), 6);
        }catch (IOException e){
            Logger logger = LogManager.getLogger();
            logger.info("in GraphTest: " + e.getMessage());
        }
    }

    @Test
    void deleteEdgeTestCountEdges() {
        defaultGraph.deleteEdge(0, 2);
        int actualCount = defaultGraph.getCountEdges();
        int expectedCount = 4;
        assertEquals(expectedCount,actualCount);
    }

    @Test
    void deleteEdgeTestGraph() {
        defaultGraph.deleteEdge(0, 2);
        ArrayList<ArrayList<Integer>> matrix= new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(-1,-1,-1,-1,1)),
                new ArrayList<>(Arrays.asList(-1,-1,6,-1,-1)),
                new ArrayList<>(Arrays.asList(-1,6,-1,2,12)),
                new ArrayList<>(Arrays.asList(-1,-1,2,-1,-1)),
                new ArrayList<>(Arrays.asList(1,-1,12,-1,-1))
        ));
        try {
            Graph newGraph = new Graph(matrix);
            assertEquals(newGraph, defaultGraph);
        }catch (IOException e){
            Logger logger = LogManager.getLogger();
            logger.info("in GraphTest: " + e.getMessage());
        }
    }

    @Test
    void deleteEdgeTestNonExistentEdge() {
        boolean b;
        try{
            defaultGraph.deleteEdge(0,3);
            b=false;
        }catch (IndexOutOfBoundsException e){
            b=true;
        }
        assertTrue(b);
    }


    ArrayList<ArrayList<Integer>> getMatrix(){
        ArrayList<ArrayList<Integer>> matrix= new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(-1,-1,8,-1,1)),
                new ArrayList<>(Arrays.asList(-1,-1,6,-1,-1)),
                new ArrayList<>(Arrays.asList(8,6,-1,2,12)),
                new ArrayList<>(Arrays.asList(-1,-1,2,-1,-1)),
                new ArrayList<>(Arrays.asList(1,-1,12,-1,-1))
        ));
        return matrix;
    }


}