package Codigo_Intellij.Sistema_Java.Descriptografar;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SistemaRevercao_Cesar {

    static class PalavraPosicao {
        String palavra;
        int posicaoOriginal;

        PalavraPosicao(String palavra, int posicaoOriginal) {
            this.palavra = palavra;
            this.posicaoOriginal = posicaoOriginal;
        }

        // Comparator para ordenar as palavras pela posição original
        public static Comparator<PalavraPosicao> ordenarPorPosicaoOriginal() {
            return (pp1, pp2) -> Integer.compare(pp1.posicaoOriginal, pp2.posicaoOriginal);
        }
    }

    // Método que recebe o caminho do arquivo de entrada, o caminho do arquivo de saída e o deslocamento para descriptografar
    public static String descriptografar(String caminhoEntrada, String caminhoSaida, int deslocamento) throws IOException {
        System.out.println("Iniciando o processo de descriptografia...");

        List<PalavraPosicao> palavrasComPosicao = lerPalavrasComPosicao(caminhoEntrada);

        System.out.println("\nPalavras lidas do arquivo de entrada:");
        for (PalavraPosicao pp : palavrasComPosicao) {
            System.out.println(pp.palavra + " | Posição original: " + pp.posicaoOriginal);
        }

        // Descriptografar as palavras antes de ordenar pela posição original
        System.out.println("\nDescriptografando as palavras:");
        for (PalavraPosicao pp : palavrasComPosicao) {
            pp.palavra = aplicarCifraDeCesar(pp.palavra, -deslocamento); // Deslocamento negativo para descriptografar
            System.out.println("Descriptografado: " + pp.palavra);
        }

        // Ordenar as palavras pela posição original
        palavrasComPosicao.sort(PalavraPosicao.ordenarPorPosicaoOriginal());

        System.out.println("\nPalavras ordenadas pela posição original:");
        for (PalavraPosicao pp : palavrasComPosicao) {
            System.out.println(pp.palavra + " | Posição original: " + pp.posicaoOriginal);
        }

        // Salvar o texto descriptografado no arquivo de saída
        salvarTextoOriginal(palavrasComPosicao, caminhoSaida);
        System.out.println("\nO texto original foi salvo no arquivo: " + caminhoSaida);
        return caminhoSaida;
    }

    // Método para ler as palavras com suas posições originais de um arquivo
    private static List<PalavraPosicao> lerPalavrasComPosicao(String caminhoArquivo) throws IOException {
        List<PalavraPosicao> palavrasComPosicao = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(caminhoArquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(" \\| ");
                if (partes.length == 2) {
                    String palavra = partes[0].trim();
                    int posicaoOriginal = Integer.parseInt(partes[1].trim());
                    palavrasComPosicao.add(new PalavraPosicao(palavra, posicaoOriginal));
                }
            }
        }
        return palavrasComPosicao;
    }

    // Método para salvar o texto descriptografado no arquivo de saída
    private static void salvarTextoOriginal(List<PalavraPosicao> palavrasComPosicao, String caminhoArquivo) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(caminhoArquivo))) {
            for (PalavraPosicao pp : palavrasComPosicao) {
                writer.write(pp.palavra + " ");
            }
        }
    }

    // Função para aplicar a Cifra de César (descriptografando com deslocamento negativo)
    private static String aplicarCifraDeCesar(String palavra, int deslocamento) {
        StringBuilder resultado = new StringBuilder();
        for (char c : palavra.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char) ((c - base + deslocamento + 26) % 26 + base);
            }
            resultado.append(c);
        }
        return resultado.toString();
    }
}
