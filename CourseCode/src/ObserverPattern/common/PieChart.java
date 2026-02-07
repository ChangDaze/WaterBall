package ObserverPattern.common;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.PieChartBuilder;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.knowm.xchart.BitmapEncoder.saveBitmap;

public class PieChart {
    public void export(String fileName, List<String> series, List<Integer> numbers) throws IOException {
        org.knowm.xchart.PieChart chart = new PieChartBuilder().width(800).height(600).title("PieChart").build();
        Color[] sliceColors = series.stream().map(s->randomColor()).toArray(Color[]::new);
        chart.getStyler().setSeriesColors(sliceColors);
        for(int i = 0; i < series.size(); i++) {
            chart.addSeries(series.get(i), numbers.get(i));
        }
        saveBitmap(chart, fileName, BitmapEncoder.BitmapFormat.PNG);
    }

    public static Color randomColor() {
        Random random = new Random();
        return new Color(0xff000000+256*256*random.nextInt(256) + 256*random.nextInt(256) + random.nextInt(256));
    }
}
