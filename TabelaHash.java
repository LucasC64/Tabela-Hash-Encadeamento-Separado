class TabelaHash {
    private No[] compartimentos;
    private int tamanho;
    private static final double A = 0.6180339887;

    public void inicializar(int m) {
        this.tamanho = m;
        this.compartimentos = new No[m];
        int i = 0;
        while (i < m) {
            this.compartimentos[i] = null;
            i = i + 1;
        }
    }

    private int hashDivisao(int chave) {
        int valor = chave;
        if (valor < 0) {
            valor = 0 - valor;
        }
        int resultado = valor % tamanho;
        return resultado;
    }

    private int hashMultiplicacao(int chave) {
        int valor = chave;
        if (valor < 0) {
            valor = 0 - valor;
        }
        double produto = valor * A;
        double fracao = produto - (int)produto;
        int resultado = (int)(tamanho * fracao);
        return resultado;
    }

    private int hashDobramento(int chave) {
        int valor = chave;
        if (valor < 0) {
            valor = 0 - valor;
        }
        int bloco1 = valor % 1000;
        int bloco2 = (valor / 1000) % 1000;
        int bloco3 = valor / 1000000;
        int soma = bloco1 + bloco2 + bloco3;
        int resultado = soma % tamanho;
        return resultado;
    }

    public int calcularHash(int chave, String funcao) {
        int resultado = 0;

        if (funcao.equals("H_DIV")) {
            resultado = hashDivisao(chave);
        }

        if (funcao.equals("H_MUL")) {
            resultado = hashMultiplicacao(chave);
        }

        if (funcao.equals("H_FOLD")) {
            resultado = hashDobramento(chave);
        }

        return resultado;
    }

    public Registro inserir(int chave, String funcao) {
        int indice = calcularHash(chave, funcao);

        Registro reg = new Registro();
        reg.hash = indice;
        reg.colisoesTabela = 0;
        reg.colisoesLista = 0;

        No novoNo = new No();
        novoNo.chave = chave;
        novoNo.proximo = null;

        if (compartimentos[indice] == null) {
            compartimentos[indice] = novoNo;
        } else {
            reg.colisoesTabela = 1;
            No atual = compartimentos[indice];
            int contador = 0;

            while (atual.proximo != null) {
                contador = contador + 1;
                atual = atual.proximo;
            }

            reg.colisoesLista = contador;
            atual.proximo = novoNo;
        }

        return reg;
    }

    public int buscar(int chave, String funcao) {
        int indice = calcularHash(chave, funcao);
        int comparacoes = 0;
        int encontrado = 0;

        No atual = compartimentos[indice];

        while (atual != null) {
            comparacoes = comparacoes + 1;
            if (atual.chave == chave) {
                encontrado = 1;
                atual = null;
            } else {
                atual = atual.proximo;
            }
        }

        if (encontrado == 1) {
            return comparacoes;
        } else {
            return 0 - comparacoes;
        }
    }
}

