package AbstractFactoryPattern;

public class PrettyButton extends Button{
    @Override
    protected char getTopLeftCorner() { return '┌'; }
    @Override
    protected char getTopRightCorner() { return '┐'; }
    @Override
    protected char getBottomLeftCorner() { return '└'; }
    @Override
    protected char getBottomRightCorner() { return '┘'; }
    @Override
    protected char getHorizontalEdge() { return '─'; }
    @Override
    protected char getVerticalEdge() { return '│'; }
}
