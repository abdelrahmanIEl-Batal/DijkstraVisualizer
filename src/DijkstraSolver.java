import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.util.Pair;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.*;

public class DijkstraSolver {
    private int n, source, destination;
    private ArrayList<Edge> edges;
    private ArrayList<Edge> dijkstraEdges;
    private int [] distance,parent;
    boolean isGraphDirected = false;
    private Boolean canReach;

    Map< Pair<Integer,Integer>, Integer > cost;
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
        cost = new HashMap<>();
    }

    private void Initialize()
    {
        this.cost = new HashMap<>();
        dijkstraEdges = new ArrayList<>();
        parent = new int[n];
        distance = new int[n];
        Arrays.fill(distance,Integer.MAX_VALUE);
        Arrays.fill(parent,-1);
        canReach = true;
        for(int i=0;i<edges.size();++i)
        {
            Edge e = edges.get(i);
            int u = e.getU() , v = e.getV(), cost = e.getCost();
            if(isGraphDirected){
                adj[u].add(new Pair<>(v,cost));
                this.cost.put(new Pair<>(u,v),cost);
            }
            else{
                adj[u].add(new Pair<>(v,cost));
                adj[v].add(new Pair<>(u,cost));
                this.cost.put(new Pair<>(u,v),cost);
                this.cost.put(new Pair<>(v,u),cost);
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
                    parent[to] = v;
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
        if(parent[s]==-1) return;
        int p = parent[s];
        dijkstraEdges.add(new Edge(p,s,this.cost.get(new Pair<>(p,s))));
        getPath(p);
    }

    public boolean canReachDestination(){
        return canReach;
    }

    public ArrayList<Edge> getDijkstraEdges(){
        return dijkstraEdges;
    }
}
