package TemplateMethodPattern.pratice1.v0;

public class Class1 {
    public void p1(int[] u) {
        int n = u.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (u[j] > u[j + 1]) {
                    int mak = u[j];
                    u[j] = u[j + 1];
                    u[j + 1] = mak;
                }
            }
        }
    }
}
