package TemplateMethodPattern.pratice1.v1;

public class Class1 extends ClassBase{
    /**
     * 比較大的往後排
     */
    @Override
    protected boolean compare(int a, int b) {
        return a > b;
    }
}
