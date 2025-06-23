package TemplateMethodPattern.pratice1.v1;

public abstract class ClassBase {
    /**
     * 依實作條件排序
     */
    public void p(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (compare(array[j], array[j + 1])) {
                    int mak = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = mak;
                }
            }
        }
    }

    /**
     * 決定排序中兩個數值的比較方式
     */
    protected abstract boolean compare(int a, int b);
}
