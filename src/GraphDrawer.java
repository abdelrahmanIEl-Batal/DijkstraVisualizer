import com.sun.xml.internal.messaging.saaj.soap.JpegDataContentHandler;
import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.AbstractGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.*;
import javafx.scene.shape.Circle;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.time.chrono.JapaneseChronology;
import java.util.ArrayList;

public class GraphDrawer
{
    AbstractGraph mst;
    String windowName;
    boolean right;
    private String Cost;
    private String extra;
    private JFrame frame;
    private JPanel mainPanel;
    private JLabel costLabel, valueLabel;
    ArrayList<Edge> snapShot;
    int step,source,destination;
    private JButton nextStep;
    private Rectangle rect;
    private boolean isDirectedGraph;
    private BasicVisualizationServer<Integer, String> vv;

    GraphDrawer(){};

    public GraphDrawer(int nodes, ArrayList<Edge> edgeList, String windowName, boolean isDirected, boolean right, int source, int destination)
    {
        this.source = source;
        this.destination = destination;
        mst = new SparseMultigraph<Integer, String>();
        for (int i = 0; i < nodes; ++i)
            mst.addVertex(i);

        extra = new String();
        for (Edge e : edgeList)
        {
            addEdge(e, isDirected);
        }
        this.windowName = windowName;
        this.right = right;
    }

    void addEdge(Edge e, boolean isDirected)
    {
        extra+=" ";
        String eCost = Integer.toString(e.getCost());
        Integer u = e.getU(), v = e.getV();
        eCost += extra;
        if (isDirected)
            mst.addEdge(eCost, u, v, EdgeType.DIRECTED);
        else
            mst.addEdge(eCost, u, v,EdgeType.UNDIRECTED);
        extra += " ";
    }

    void addEdge(Edge e)
    {
        String eCost = Integer.toString(e.getCost());
        Integer u = e.getU(), v = e.getV();
        eCost += extra;
        if (isDirectedGraph)
            mst.addEdge(eCost, u, v, EdgeType.DIRECTED);
        else
            mst.addEdge(eCost, u, v);
        extra += " ";
    }

    public void draw()
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        rect = defaultScreen.getDefaultConfiguration().getBounds();

        Layout<Integer, String> layout = new CircleLayout<>(mst);
        layout.setSize(new Dimension((int) rect.getMaxX() / 2, (int) rect.getMaxY() / 2));
        vv = new BasicVisualizationServer<Integer, String>(layout);
        vv.setPreferredSize(new Dimension((int) rect.getMaxX() / 2, (int) rect.getMaxY() / 2));
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        Transformer<String, Font> edgeFontTransfomer = new Transformer<String, Font>()
        {
            @Override
            public Font transform(String s)
            {
                return new Font(s, 0, 20);
            }
        };

        Transformer<Integer, Font> NodeFontTransformer = new Transformer<Integer, Font>()
        {
            @Override
            public Font transform(Integer s)
            {
                return new Font(s.toString(), 0, 20);
            }
        };

        Transformer<Integer,Paint> vertexColor = new Transformer<Integer,Paint>() {
            public Paint transform(Integer i) {
                if(i == source) return Color.GREEN;
                if(i == destination) return Color.GREEN;
                return Color.RED;
            }
        };

        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeFontTransformer(edgeFontTransfomer);
        vv.getRenderContext().setVertexFontTransformer(NodeFontTransformer);
        vv.getRenderContext().setVertexShapeTransformer(new Transformer<Integer, Shape>()
        {
            @Override
            public Shape transform(Integer u)
            {
                return new Ellipse2D.Double(-15, -15, 30, 30);
            }
        });
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        frame = new JFrame(windowName);
        frame.getContentPane().add(vv);
        frame.pack();
        if (right) {
            frame.setLocation((int) rect.getMaxX() - (int) rect.getMaxX() / 2, 0);
            updateGraph();
        }
        frame.setVisible(true);
    }



    public void DijkstraGraphDrawer(int nodes, ArrayList<Edge> edgeList, String windowName, boolean isDirected, boolean right,int source,int destination)
    {
        this.source = source;
        this.destination = destination;
        this.isDirectedGraph = isDirected;
        mst = new SparseMultigraph();
        this.snapShot = new ArrayList<>(edgeList);
        this.windowName = windowName;
        this.right = right;
        Edge curr = snapShot.get(step++);
        Cost = new String(Integer.toString(curr.getCost()));
        mst.addVertex(curr.getU()); mst.addVertex(curr.getV());
        this.extra = new String();
        extra+=" ";
        addEdge(curr);
    }

    public void updateGraph()
    {
        nextStep = new JButton("Next Step");
        JPanel panel = new JPanel();
        panel.add(nextStep);
        JLabel info = new JLabel("Cost so far: ");
        JLabel soFarCost = new JLabel(Cost);
        panel.add(info);
        panel.add(soFarCost);
        nextStep.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(step < snapShot.size() ){
                    Edge curr = snapShot.get(step);
                    step++;
                    addEdge(curr);
                    int cc = curr.getCost();
                    cc+= Integer.parseInt(soFarCost.getText());
                    soFarCost.setText(Integer.toString(cc));
                }
                else{
                    JOptionPane.showMessageDialog(null, "There are no more steps!");
                }
                frame.getContentPane().remove(vv);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
                rect = defaultScreen.getDefaultConfiguration().getBounds();

                Layout<Integer, String> layout = new CircleLayout<>(mst);
                layout.setSize(new Dimension((int) rect.getMaxX() / 2, (int) rect.getMaxY() / 2));
                BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<Integer, String>(layout);
                vv.setPreferredSize(new Dimension((int) rect.getMaxX() / 2, (int) rect.getMaxY() / 2));
                vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
                Transformer<String, Font> edgeFontTransfomer = new Transformer<String, Font>()
                {
                    @Override
                    public Font transform(String s)
                    {
                        return new Font(s, 0, 20);
                    }
                };

                Transformer<Integer, Font> NodeFontTransformer = new Transformer<Integer, Font>()
                {
                    @Override
                    public Font transform(Integer s)
                    {
                        return new Font(s.toString(), 0, 20);
                    }
                };
                Transformer<Integer,Paint> vertexColor = new Transformer<Integer,Paint>() {
                    public Paint transform(Integer i) {
                        if(i == source) return Color.GREEN;
                        if(i == destination) return Color.GREEN;
                        return Color.RED;
                    }
                };
                vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
                vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
                vv.getRenderContext().setEdgeFontTransformer(edgeFontTransfomer);
                vv.getRenderContext().setVertexFontTransformer(NodeFontTransformer);
                vv.getRenderContext().setVertexShapeTransformer(new Transformer<Integer, Shape>()
                {
                    @Override
                    public Shape transform(Integer u)
                    {
                        return new Ellipse2D.Double(-15, -15, 30, 30);
                    }
                });
                vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
                frame.getContentPane().add(vv);
                frame.repaint();
                frame.setVisible(true);
            }
        });
        this.frame.add(panel,BorderLayout.WEST);
    }



}