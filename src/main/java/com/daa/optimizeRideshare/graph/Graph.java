package com.daa.optimizeRideshare.graph;

import java.util.*;

public abstract class Graph
{

    private int numVertices;
    private int numEdges;

    //optional association of String labels to vertices
    private Map<Integer,String> vertexLabels;

    /**
     * Create a new empty Graph
     */
    public Graph() {
        numVertices = 0;
        numEdges = 0;
        vertexLabels = null;
    }


    /**
     * Report size of vertex set
     * @return The number of vertices in the graph.
     */
    public int getNumVertices() {
        return numVertices;
    }


    /**
     * Report size of edge set
     * @return The number of edges in the graph.
     */
    public int getNumEdges() {
        return numEdges;
    }

    /**
     * Add new vertex to the graph.  This vertex will
     * have as its index the next available integer.
     * Precondition: contiguous integers are used to
     * index vertices.
     * @return index of newly added vertex
     */
    public int addVertex() {
        implementAddVertex();
        numVertices ++;
        return (numVertices-1);
    }

    /**
     * Abstract method implementing adding a new
     * vertex to the representation of the graph.
     */
    public abstract void implementAddVertex();

    /**
     * Add new edge to the graph between given vertices,
     * @param v Index of the start point of the edge to be added.
     * @param w Index of the end point of the edge to be added.
     */
    public void addEdge(int v , int w) {
        numEdges ++;
        if (v < numVertices && w < numVertices) {
            implementAddEdge(v , w);
        }
        else {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Abstract method implementing adding a new
     * edge to the representation of the graph.
     */
    public abstract void implementAddEdge(int v, int w);

    /**
     * Get all (out-)neighbors of a given vertex.
     * @param v Index of vertex in question.
     * @return List of indices of all vertices that are adjacent to v
     * 	via outgoing edges from v.
     */
    public abstract List<Integer> getNeighbors(int v);

    /**
     * Get all in-neighbors of a given vertex.
     * @param v Index of vertex in question.
     * @return List of indices of all vertices that are adjacent to v
     * 	via incoming edges to v.
     */
    public abstract List<Integer> getInNeighbors(int v);



    /**
     * The degree sequence of a graph is a sorted (organized in numerical order
     * from largest to smallest, possibly with repetitions) list of the degrees
     * of the vertices in the graph.
     *
     * @return The degree sequence of this graph.
     */


    public List<Integer> degreeSequence()
    {

        // XXX: Implement in part 1 of week 2
        List<Integer> degreeList = new ArrayList<>();


        for(int i = 0; i < numVertices; i++)
        {
            degreeList.add(getNeighbors(i).size() + getInNeighbors(i).size());

        }

        Collections.sort(degreeList, Collections.reverseOrder());

        return degreeList;
    }

    /**
     * Get all the vertices that are 2 away from the vertex in question.
     * @param v The starting vertex
     * @return A list of the vertices that can be reached in exactly two hops (by
     * following two edges) from vertex v.
     * XXX: Implement in part 2 of week 2 for each subclass of Graph
     */
    public abstract List<Integer> getDistance2(int v);

    /** Return a String representation of the graph
     * @return A string representation of the graph
     */
    public String toString() {
        String s = "\nGraph with " + numVertices + " vertices and " + numEdges + " edges.\n";
        s += "Degree sequence: " + degreeSequence() + ".\n";
        if (numVertices <= 20) s += adjacencyString();
        return s;
    }

    /**
     * Generate string representation of adjacency list
     * @return the String
     */
    public abstract String adjacencyString();


    // The next methods implement labeled vertices.
    // Basic graphs may or may not have labeled vertices.

    /**
     * Create a new map of vertex indices to string labels
     * (Optional: only if using labeled vertices.)
     */
    public void initializeLabels() {
        vertexLabels = new HashMap<Integer,String>();
    }

    public boolean hasVertex(int v)
    {
        return v < getNumVertices();
    }

    public boolean hasVertex(String s)
    {
        return vertexLabels.containsValue(s);
    }

    public void addLabel(int v, String s) {
        if (v < getNumVertices() && !vertexLabels.containsKey(v))
        {
            vertexLabels.put(v, s);
        }
        else {
            System.out.println("ERROR: tried to label a vertex that is out of range or already labeled");
        }
    }

    public String getLabel(int v) {
        if (vertexLabels.containsKey(v)) {
            return vertexLabels.get(v);
        }
        else return null;
    }

    public int getIndex(String s) {
        for (Map.Entry<Integer,String> entry : vertexLabels.entrySet()) {
            if (entry.getValue().equals(s))
                return entry.getKey();
        }
        System.out.println("ERROR: No vertex with this label");
        return -1;
    }

}
