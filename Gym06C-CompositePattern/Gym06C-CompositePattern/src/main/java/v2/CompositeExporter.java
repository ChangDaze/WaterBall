package v2;

import java.util.List;

public class CompositeExporter {
    private final List<CompositeExporter> children;

    public CompositeExporter(CompositeExporter... exporters) {
        this.children = List.of(exporters);
    }

    public CompositeExporter(List<CompositeExporter> exporters) {
        this.children = exporters;
    }

    public void export(String message){
        for (CompositeExporter exporter : children) {
            exporter.export(message);
        }
    };
}
