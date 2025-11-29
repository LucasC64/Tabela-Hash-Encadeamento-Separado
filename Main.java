import java.io.FileWriter;
import java.io.IOException;

public class Main {

    private static final int[] TAMANHOS = {1009, 10007, 100003};
    private static final int[] DATASETS = {1000, 10000, 100000};
    private static final int[] SEEDS = {137, 271828, 314159};
    private static final String[] FUNCOES = {"H_DIV", "H_MUL", "H_FOLD"};

    public static void main(String[] args) {

        try (FileWriter writer = new FileWriter("resultados.csv")) {

            // Cabeçalho do CSV
            writer.write("m,n,func,seed,ins_ms,coll_tbl,coll_lst,find_ms_hits,find_ms_misses,cmp_hits,cmp_misses,checksum\n");

            // Geração dos dados
            for (int m : TAMANHOS)
                for (int n : DATASETS)
                    for (String func : FUNCOES)
                        for (int seed : SEEDS) {

                            String linhaCSV = ExperimentoHash.executar(m, n, func, seed);
                            writer.write(linhaCSV + "\n");
                        }

            System.out.println("Arquivo 'resultados.csv' gerado com sucesso!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
