import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.util.Pair;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.*;

public class DijkstraSolver {
    private int n, source, destination;
    private ArrayList<Edge> edges;
    private ArrayList<Edge> dijkstraEdges;
    private int [] distance;
    ArrayList<Pair<Integer,Integer> > parent;
    boolean isGraphDirected = false;
    private Boolean canReach;

    List< Pair<Integer, Integer> > [] adj;
    PriorityQueue<Pair<Integer,Integer>> q;

    DijkstraSolver(int n, ArrayList<Edge> edges,int source, int destination, boolean isDirected)
    {
        this.n = n;
        this.edges = edges;
        this.source = source;
        this.destination = destination;
        this.isGraphDirected = isDirected;
        adj = new List[n];
        for(int i=0;i<n;++i) adj[i] = new ArrayList<>();
    }

    private void Initialize()
    {
        dijkstraEdges = new ArrayList<>();
        parent = new ArrayList<>(n);
        distance = new int[n];
        Arrays.fill(distance,Integer.MAX_VALUE);
        for(int i=0;i<n;++i) parent.add(new Pair<>(-1,0));
        canReach = true;
        for(int i=0;i<edges.size();++i)
        {
            Edge e = edges.get(i);
            int u = e.getU() , v = e.getV(), cost = e.getCost();
            if(isGraphDirected){
                adj[u].add(new Pair<>(v,cost));
            }
            else{
                adj[u].add(new Pair<>(v,cost));
                adj[v].add(new Pair<>(u,cost));
            }
        }

        q = new PriorityQueue<>(new Comparator<Pair<Integer, Integer>>() {
            @Override
            public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
                return p1.getKey() - p2.getKey();
            }
        });
    }

    public void Dijkstra()
    {
        Initialize();
        distance[source] = 0;
        q.add(new Pair<>(0,source));
        while(q.size() > 0)
        {
            Pair<Integer,Integer> curr = q.poll();
            int v = curr.getValue();
            int cost = curr.getKey();
            if(cost!=distance[v]) continue;

            for(int i=0;i<adj[v].size();++i)
            {
                int to = adj[v].get(i).getKey();
                int newCost = adj[v].get(i).getValue();
                if(distance[v] + newCost < distance[to])
                {
                    distance[to] = distance[v] + newCost;
                    parent.set(to,new Pair<>(v,newCost));
                    q.add(new Pair<>(distance[to],to));
                }
            }
        }
        if(distance[destination] == Integer.MAX_VALUE)
        {
            canReach = false;
            return ;
        }
        getPath(destination);
        Collections.reverse(dijkstraEdges);
    }

    private void getPath(int s)
    {
        if(parent.get(s).getKey()==-1) return;
        int p = parent.get(s).getKey();
        dijkstraEdges.add(new Edge(p,s,parent.get(s).getValue()));
        getPath(p);
    }

    public boolean canReachDestination(){
        return canReach;
    }

    public ArrayList<Edge> getDijkstraEdges(){
        return dijkstraEdges;
    }
}
