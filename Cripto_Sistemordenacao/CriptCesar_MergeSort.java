package Codigo_Intellij.Sistema_Java.Cripto_Sistemordenacao;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CriptCesar_MergeSort {

    public void executar(String textoCompleto, int deslocamento, boolean criptografar) {
        List<PalavraPosicao> palavrasComPosicao = processarFrase(textoCompleto);

        // Mostrar as palavras antes da ordenação
        System.out.println("\nPalavras antes da ordenação:");
        for (PalavraPosicao pp : palavrasComPosicao) {
            System.out.println(pp.palavra + " | " + pp.posicaoOriginal);
        }

        // Remover a ordenação se não for necessário
        palavrasComPosicao.sort(PalavraPosicao.ordenarPorPalavra());

        // Mostrar as palavras após a ordenação
        System.out.println("\nPalavras após a ordenação:");
        for (PalavraPosicao pp : palavrasComPosicao) {
            System.out.println(pp.palavra + " | " + pp.posicaoOriginal);
        }

        // Aplicar Cifra de César nas palavras ordenadas ou não, dependendo do seu desejo
        System.out.println("\nAplicagem da Cifra de Cesar:");
        for (PalavraPosicao pp : palavrasComPosicao) {
            if (criptografar) {
                pp.palavra = aplicarCifraDeCesar(pp.palavra, deslocamento);
                System.out.println("Criptografado: " + pp.palavra);
            } else {
                pp.palavra = aplicarCifraDeCesar(pp.palavra, -deslocamento); // Descriptografar
                System.out.println("Descriptografando: " + pp.palavra);
            }
        }

        // Salvar no arquivo na pasta desejada
        String caminhoSaida = "Texto_criptografado_Cesar.txt";
        try {
            salvarComPosicao(palavrasComPosicao, caminhoSaida);
            System.out.println("\nA frase criptografada/descriptografada foi salva no arquivo: " + caminhoSaida);
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }

    static class PalavraPosicao {
        String palavra;
        int posicaoOriginal;

        PalavraPosicao(String palavra, int posicaoOriginal) {
            this.palavra = palavra;
            this.posicaoOriginal = posicaoOriginal;
        }

        // Comparator para ordenar as palavras alfabeticamente ignorando o caso
        public static Comparator<PalavraPosicao> ordenarPorPalavra() {
            return (pp1, pp2) -> pp1.palavra.compareToIgnoreCase(pp2.palavra);
        }
    }

    private static List<PalavraPosicao> processarFrase(String frase) {
        List<PalavraPosicao> palavrasComPosicao = new ArrayList<>();
        String[] palavras = frase.split("\\s+");

        System.out.println("\nProcesso de Marcação");
        for (int i = 0; i < palavras.length; i++) {
            palavrasComPosicao.add(new PalavraPosicao(palavras[i], i));
            System.out.println("Palavra: " + palavras[i] + " | Posição: " + i);
        }

        return palavrasComPosicao;
    }

    private static String aplicarCifraDeCesar(String palavra, int deslocamento) {
        StringBuilder resultado = new StringBuilder();

        for (char caractere : palavra.toCharArray()) {
            if (Character.isLetter(caractere)) {
                // Verifica se é letra minúscula ou maiúscula e aplica o deslocamento correto
                char base = Character.isLowerCase(caractere) ? 'a' : 'A';
                // A operação abaixo garante que o deslocamento seja tratado corretamente, mesmo se for maior que 26
                char cifra = (char) ((caractere - base + deslocamento + 26) % 26 + base);
                resultado.append(cifra);
            } else {
                // Caracteres não alfabéticos são adicionados sem alteração
                resultado.append(caractere);
            }
        }

        return resultado.toString();
    }

    private static void salvarComPosicao(List<PalavraPosicao> palavrasComPosicao, String caminhoArquivo) throws IOException {
        // Escrever no arquivo no formato "Palavra | Posição"
        Path arquivoSaida = Paths.get(caminhoArquivo); // Caminho direto para o arquivo
        try (BufferedWriter writer = Files.newBufferedWriter(arquivoSaida)) {
            System.out.println("\nFormato Salvo:");
            for (PalavraPosicao pp : palavrasComPosicao) {
                writer.write(pp.palavra + " | " + pp.posicaoOriginal + "\n");
                System.out.println(pp.palavra + " | " + pp.posicaoOriginal);
            }
        }
    }
}
