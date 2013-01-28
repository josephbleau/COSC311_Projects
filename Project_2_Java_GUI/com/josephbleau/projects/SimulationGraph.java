package com.josephbleau.projects;

/* JFree packages for charting. 
 * Available here: http://sourceforge.net/projects/jfreechart/ */
import com.josephbleau.collections.Pair;
import java.util.ArrayList;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class SimulationGraph extends JFrame {
    public static final long serialVersionUID = 1L;
    
    DefaultCategoryDataset dataset;
    JFreeChart chart;
    
    public SimulationGraph(ArrayList<Pair<Integer, Double>> ds) {
        super("");
        
        this.dataset = new DefaultCategoryDataset();
        int size = ds.size();
        
        for(int i = 0; i < size; i++ ) {
            double n = ds.get(i).mSecond;
            Integer t = ds.get(i).mFirst;
            this.dataset.addValue(n, "N-Values", t);
        }
        
        this.chart = createChart();
    }
    
    public JFreeChart getChart(){ 
        return this.chart;
    }
    
    private JFreeChart createChart() {
        JFreeChart new_chart = ChartFactory.createLineChart(
                "N Values over Ticks",
                "Tick",
                "N",
                this.dataset,
                PlotOrientation.VERTICAL,
                false,
                false,
                false
        );
        
        return new_chart;
    }
}
