package v1;

import java.util.HashMap;
import java.util.Map;

public class Logger {
    private String _name;
    private Level _threshold;
    private Logger _parent;
    private Layout _layout;
    private CompositeExporter _exporter;

    private static Map<String, Logger> _configedLoggers = new HashMap<>();

    public String getName() {
        return _name;
    }

    public Level getThreshold() {
        return _threshold;
    }

    public Logger getParent() {
        return _parent;
    }

    public Layout getLayout() {
        return _layout;
    }

    public CompositeExporter getExporter() {
        return _exporter;
    }

    public Logger(String name, Level threshold, Logger parent, Layout layout, CompositeExporter exporter) {

        if(name == null && parent != null)
        {
            this._name = parent.getName();
        }
        else {
            this._name = name;
        }

        if(threshold == null && parent != null)
        {
            this._threshold = parent.getThreshold();
        }
        else {
            this._threshold = threshold;
        }

        if(layout == null && parent != null)
        {
            this._layout = parent.getLayout();
        }
        else {
            this._layout = layout;
        }

        if(exporter == null && parent != null)
        {
            this._exporter = parent.getExporter();
        }
        else {
            this._exporter = exporter;
        }

        this._parent = parent;
    }

    public void trace(String message) {
        if(_threshold.getPriority() < Level.TRACE.getPriority())
        {
            return;
        }

        _exporter.export(_layout.format(this,message));
    }

    public void debug(String message) {
        if(_threshold.getPriority() < Level.DEBUG.getPriority())
        {
            return;
        }

        _exporter.export(_layout.format(this,message));
    }

    public void info(String message) {
        if(_threshold.getPriority() < Level.INFO.getPriority())
        {
            return;
        }

        _exporter.export(_layout.format(this,message));
    }

    public void warn(String message) {
        if(_threshold.getPriority() < Level.WARN.getPriority())
        {
            return;
        }

        _exporter.export(_layout.format(this,message));
    }

    public void error(String message) {
        if(_threshold.getPriority() < Level.ERROR.getPriority())
        {
            return;
        }

        _exporter.export(_layout.format(this,message));
    }

    public static Logger getLogger(String name) {
        return _configedLoggers.get(name);
    }

    public static void declareLoggers(Logger... loggers) {
        for(Logger logger : loggers) {
            _configedLoggers.put(logger.getName(), logger);
        }
    }
}
