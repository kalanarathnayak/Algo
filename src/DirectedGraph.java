import java.io.FileReader;
import java.util.*;

public class DirectedGraph {
    private int V;
    private List<Integer>[] adj;

    public DirectedGraph(int V) {
        this.V = V;
        adj = new ArrayList[V];
        for (int i = 0; i < V; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    public void addEdge(int u, int v) {
        adj[u].add(v);
    }

    public int getNumVertices() {
        return V;
    }

    public int findSink() {
        boolean[] isSink = new boolean[V];
        Arrays.fill(isSink, true);
        for (int u = 0; u < V; u++) {
            for (int v : adj[u]) {
                isSink[v] = false;
            }
        }
        for (int v = 0; v < V; v++) {
            if (isSink[v]) {
                return v;
            }
        }
        return -1;
    }

    public void removeVertex(int v) {
        for (int u = 0; u < V; u++) {
            adj[u].remove((Integer) v);
        }
    }

    public boolean isAcyclic() {
        int numVertices = getNumVertices();
        int numRemoved = 0;
        while (numRemoved < numVertices) {
            int sink = findSink();
            if (sink == -1) {
                // There are no sinks, so the graph is acyclic
                System.out.println("The graph is acyclic");
                return true;
            }
            System.out.println("Eliminating sink " + sink);
            removeVertex(sink);
            numRemoved++;
        }
        // All vertices have been removed, but the graph still has edges, so it is cyclic
        System.out.println("The graph is cyclic");
        return false;
    }

    public List<Integer> findCycle() {
        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[V];
        boolean[] onStack = new boolean[V];
        for (int v = 0; v < V; v++) {
            if (!visited[v]) {
                if (dfs(v, stack, visited, onStack)) {
                    List<Integer> cycle = new ArrayList<>();
                    while (!stack.isEmpty()) {
                        cycle.add(stack.pop());
                    }
                    Collections.reverse(cycle);
                    return cycle;
                }
            }
        }
        return null;
    }

    private boolean dfs(int v, Stack<Integer> stack, boolean[] visited, boolean[] onStack) {
        visited[v] = true;
        onStack[v] = true;
        for (int w : adj[v]) {
            if (!visited[w]) {
                if (dfs(w, stack, visited, onStack)) {
                    stack.push(w);
                    return true;
                }
            } else if (onStack[w]) {
                stack.push(w);
                stack.push(v);
                return true;
            }
        }
        onStack[v] = false;
        return false;
    }

    public static DirectedGraph fromFile(String filename) throws Exception {
        Scanner scanner = new Scanner(new FileReader(filename));
        int V = scanner.nextInt();
        DirectedGraph graph = new DirectedGraph(V);
        while (scanner.hasNextInt()) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            graph.addEdge(u, v);
        }
        scanner.close();
        return graph;
    }

    public static void main(String[] args) {
        try {
            DirectedGraph graph = DirectedGraph.fromFile("/Users/kalanarathnayake/algo_cw/cw/src/input.txt");
            boolean isAcyclic = graph.isAcyclic();
            if (!isAcyclic) {
                List<Integer> cycle = graph.findCycle();
                System.out.print("Cycle: ");
                for (int v : cycle) {
                    System.out.print(v + " ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}