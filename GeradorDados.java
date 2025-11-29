public class GeradorDados {
    private static final int MIN = 100000000;
    private static final int MAX = 999999999;

    public static int[] gerarDataset(int n, int seed) {
        int[] dados = new int[n];
        long estado = seed;

        for (int i = 0; i < n; i++) {
            estado = (estado * 1103515245 + 12345);
            long intervalo = MAX - MIN + 1;
            dados[i] = (int)(MIN + (estado % intervalo));
        }
        return dados;
    }

    public static void embaralhar(int[] array, int seed) {
        long estado = seed;

        for (int i = array.length - 1; i > 0; i--) {
            estado = (estado * 1103515245 + 12345) & 0x7fffffffL;
            int j = (int)(estado % (i + 1));

            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }
}
