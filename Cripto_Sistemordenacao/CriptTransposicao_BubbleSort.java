package Codigo_Intellij.Sistema_Java.Cripto_Sistemordenacao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class CriptTransposicao_BubbleSort {

    // Método para ordenar a chave usando BubbleSort e retornar uma chave ordenada como array de inteiros
    public static int[] bubbleSort(String chave) {
        String[] partesChave = chave.trim().split("\\s+");
        int[] chavenova = new int[partesChave.length];
        for (int i = 0; i < partesChave.length; i++) {
            chavenova[i] = Integer.parseInt(partesChave[i]);
        }

        int n = chavenova.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (chavenova[j] > chavenova[j + 1]) {
                    int temp = chavenova[j];
                    chavenova[j] = chavenova[j + 1];
                    chavenova[j + 1] = temp;
                }
            }
        }
        return chavenova;
    }

    // Método para ler o arquivo de entrada
    public static String entradadedados(String entradaarquivo) throws IOException {
        entradaarquivo = entradaarquivo.replace("\"", "");
        List<String> linhas = Files.readAllLines(Path.of(entradaarquivo));
        StringBuilder sb = new StringBuilder();
        for (String linha : linhas) {
            sb.append(linha).append("\n");
        }
        return sb.toString();
    }

    // Método para realizar a criptografia de transposição com chave ordenada
    public static StringBuilder CriptTransposicao(String chave, String texto) {
        texto = texto.replaceAll("\\s+", "");

        int[] chavenova = bubbleSort(chave);
        System.out.println("Texto original antes da transposição: " + texto);

        int numcolunas = chavenova.length;
        int numlinhas = (int) Math.ceil((double) texto.length() / numcolunas);
        char[][] grid = new char[numlinhas][numcolunas];

        int index = 0;
        for (int i = 0; i < numlinhas; i++) {
            for (int j = 0; j < numcolunas; j++) {
                if (index < texto.length()) {
                    grid[i][j] = texto.charAt(index++);
                } else {
                    grid[i][j] = ' ';
                }
            }
        }

        System.out.println("\nGrid após preenchimento:");
        for (int i = 0; i < numlinhas; i++) {
            for (int j = 0; j < numcolunas; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }

        StringBuilder resultado = new StringBuilder();
        System.out.println("\nEtapas da leitura das colunas na ordem da chave ordenada:");

        for (int coluna : chavenova) {
            int colIndex = coluna - 1;
            System.out.println("Lendo coluna original: " + coluna);
            for (int i = 0; i < numlinhas; i++) {
                resultado.append(grid[i][colIndex]);
                System.out.print(grid[i][colIndex] + " ");
            }
            System.out.println();
        }

        System.out.println("\nTexto Criptografado: " + resultado.toString());
        return resultado;
    }

    // Método para salvar o texto criptografado em um arquivo
    public static void salvarEmArquivo(String caminhoSaida, String textoCriptografado) {
        try {
            Files.writeString(Path.of(caminhoSaida), textoCriptografado, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Texto criptografado salvo em: " + caminhoSaida);
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
}
