public class ExperimentoHash {

    private static final int REPETICOES = 5;

    public static String executar(int m, int n, String funcao, int seed) {

        int[] dataset = GeradorDados.gerarDataset(n, seed);

        long tempoInsercaoTotal = 0;
        int colisoesTabela = 0;
        int colisoesLista = 0;
        int[] primeirosHashes = new int[10];

        // Inserção
        for (int r = 0; r < REPETICOES; r++) {

            TabelaHash tabela = new TabelaHash();
            tabela.inicializar(m);

            long inicio = System.currentTimeMillis();

            for (int i = 0; i < n; i++) {
                Registro resultado = tabela.inserir(dataset[i], funcao);

                if (r == 0) {
                    if (i < 10) primeirosHashes[i] = resultado.hash;
                    colisoesTabela += resultado.colisoesTabela;
                    colisoesLista += resultado.colisoesLista;
                }
            }

            tempoInsercaoTotal += (System.currentTimeMillis() - inicio);
        }

        double tempoInsercaoMedio = tempoInsercaoTotal / (double) REPETICOES;

        int checksum = 0;
        for (int h : primeirosHashes) checksum += h;
        checksum %= 1000003;

        int[] loteBusca = gerarLoteBusca(dataset, n, seed);

        EstatisticasBusca estat = executarBuscas(dataset, loteBusca, m, n, funcao, seed);

        return m + "," + n + "," + funcao + "," + seed + "," +
                tempoInsercaoMedio + "," + colisoesTabela + "," + colisoesLista + "," +
                estat.tempoHits + "," + estat.tempoMisses + "," +
                estat.comparacoesHits + "," + estat.comparacoesMisses + "," +
                checksum;
    }

    private static int[] gerarLoteBusca(int[] dataset, int n, int seed) {
        int[] lote = new int[n];
        int metade = n / 2;

        for (int i = 0; i < metade; i++) lote[i] = dataset[i * 2];

        long estado = seed + 999;
        for (int i = 0; i < metade; i++) {
            estado = (estado * 1103515245 + 12345) & 0x7fffffffL;
            lote[metade + i] = 100000000 + (int)(estado % 900000000);
        }

        GeradorDados.embaralhar(lote, seed + 123);
        return lote;
    }

    private static EstatisticasBusca executarBuscas(
            int[] dataset, int[] lote, int m, int n, String funcao, int seed
    ) {
        long totalHits = 0;
        long totalMisses = 0;
        int cmpHits = 0;
        int cmpMisses = 0;
        int contHits = 0;
        int contMisses = 0;

        for (int r = 0; r < REPETICOES; r++) {

            TabelaHash tabela = new TabelaHash();
            tabela.inicializar(m);

            for (int v : dataset) tabela.inserir(v, funcao);

            for (int valor : lote) {
                long ini = System.nanoTime();
                int res = tabela.buscar(valor, funcao);
                long fim = System.nanoTime();

                if (res > 0) {
                    totalHits += (fim - ini);
                    if (r == 0) {
                        cmpHits += res;
                        contHits++;
                    }
                } else {
                    totalMisses += (fim - ini);
                    if (r == 0) {
                        cmpMisses += (-res);
                        contMisses++;
                    }
                }
            }
        }

        EstatisticasBusca e = new EstatisticasBusca();
        e.tempoHits = totalHits / (double)(contHits * REPETICOES) / 1_000_000.0;
        e.tempoMisses = totalMisses / (double)(contMisses * REPETICOES) / 1_000_000.0;
        e.comparacoesHits = cmpHits / (double) contHits;
        e.comparacoesMisses = cmpMisses / (double) contMisses;
        return e;
    }

    private static class EstatisticasBusca {
        double tempoHits;
        double tempoMisses;
        double comparacoesHits;
        double comparacoesMisses;
    }
}
