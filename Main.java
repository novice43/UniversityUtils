import javax.swing.*;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Vertex
{
    static int vertexCount;
    static mxGraph graph;
    static Object defaultParent;
    private Object parent;
    private int number;
    private int parentNumber;
    private String name;
    private int v;
    private int v1;
    private int v2;
    private int v3;
    List<Vertex> child = new ArrayList<>();
    private Vertex thisParent;
    private Object thisVertex;


    Vertex(String name, int v, int v1, int v2, int v3)
    {
        this(defaultParent, name, v, v1, v2, v3);
    }
    private Vertex(Object parent, String name, int v, int v1, int v2, int v3)
    {
        vertexCount++;
        this.number = vertexCount;
        this.parent = parent;
        if(name != null) this.name = name;
        else
            this.name = number + "-" + parentNumber;
        this.v = v;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }


    public List<Vertex> getChild()
    {
        return child;
    }


    void draw()
    {
        this.parentNumber = thisParent != null ? thisParent.number : 0;
        this.name = number + "-" + parentNumber;
        thisVertex = graph.insertVertex(parent, null, name, v, v1, v2, v3);
        for (Vertex child : child)
        {
            child.thisParent = this;
            child.draw();
            graph.insertEdge(parent, null, "", thisVertex, child.thisVertex);
        }
    }


    void show()
    {
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.setUseBoundingBox(false);

        layout.execute(parent);
    }
}

public class Main extends JFrame
{
    private static int MAX_CHILDREN ;

    private static int MAX_VERTEX ;

    private static Random rand = new Random();

    private static Vertex root;

    public static void main(String[] args)
    {
        Scanner scn = new Scanner(System.in);
        System.out.print("Кол-во вершин(узлов): ");
        MAX_VERTEX = scn.nextInt();
        System.out.print("Максимальное кол-во потомков от одной вершины: ");
        MAX_CHILDREN = scn.nextInt();
        Main frame = new Main();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setVisible(true);
    }
    private Main()
    {
        super("Graph");

        mxGraph graph = new mxGraph();
        Vertex.defaultParent = graph.getDefaultParent();
        Vertex.graph = graph;
        graph.getModel().beginUpdate();
        try
        {
            root = new Vertex(null, 0, 0, 30, 15);
            generateGraph(new Vertex[] {root});
            root.draw();
            root.show();
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);
    }
    private void generateGraph(Vertex[] thisLevelRoots)
    {
        if(thisLevelRoots.length == 0) return;
        Vertex vert;
        List<Vertex> children = new ArrayList<>();
        int count;
        boolean ok = false;
        for(int a = 0; a < thisLevelRoots.length; a++)
        {
            count = rand.nextInt(MAX_CHILDREN);
            if(count != 0) ok = true;
            if(a == thisLevelRoots.length-1 && !ok)
                while(count == 0)
                    count = rand.nextInt(MAX_CHILDREN);
            for (int i = 0; i < count; i++)
            {
                if (Vertex.vertexCount >= MAX_VERTEX) return;
                vert = new Vertex(null, 0, 0, 30, 15);
                children.add(vert);
                thisLevelRoots[a].child.add(vert);
            }
        }
        generateGraph(children.toArray(new Vertex[0]));
    }
}
