package TemplateMethodPattern.pratice1.v0;

public class Class2 {
    public void p2(int[] k) {
        int n = k.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (k[j] < k[j + 1]) {
                    int ppp = k[j];
                    k[j] = k[j + 1];
                    k[j + 1] = ppp;
                }
            }
        }
    }
}
