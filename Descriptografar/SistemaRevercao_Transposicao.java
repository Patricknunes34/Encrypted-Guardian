package Codigo_Intellij.Sistema_Java.Descriptografar;

public class SistemaRevercao_Transposicao {

    // Método para ordenar a chave usando BubbleSort
    public static int[] bubbleSort(String chave) {
        System.out.println("\nIniciando a ordenação da chave...");
        // Converte a chave para um array de inteiros
        String[] partesChave = chave.trim().split("\\s+");
        int[] chavenova = new int[partesChave.length];
        for (int i = 0; i < partesChave.length; i++) {
            chavenova[i] = Integer.parseInt(partesChave[i]);
        }

        // Aplica BubbleSort na chave convertida para ordenar
        int n = chavenova.length;
        System.out.println("Ordenando a chave usando BubbleSort...");
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (chavenova[j] > chavenova[j + 1]) {
                    // Troca os valores
                    int temp = chavenova[j];
                    chavenova[j] = chavenova[j + 1];
                    chavenova[j + 1] = temp;
                }
            }
        }

        // Mostra a chave ordenada
        System.out.print("Chave ordenada: ");
        for (int i : chavenova) {
            System.out.print(i + " ");
        }
        System.out.println();
        return chavenova; // Retorna a chave ordenada como array de inteiros
    }

    public String descriptografar(String chave, String textoCriptografado) {
        System.out.println("\nIniciando a descriptografia...");

        int numcolunas = chave.replaceAll("\\s", "").length();
        int numlinhas = (int) Math.ceil((double) textoCriptografado.length() / numcolunas);
        char[][] grid = new char[numlinhas][numcolunas];

        // Ordena a chave antes de começar a descriptografia
        System.out.println("Ordenando a chave antes de preencher o grid...");
        int[] chaveOrdenada = bubbleSort(chave);

        // Preenche o grid de acordo com a ordem das colunas na chave ordenada
        System.out.println("Preenchendo o grid com o texto criptografado...");
        int index = 0;
        for (int coluna : chaveOrdenada) {
            int colIndex = coluna - 1;
            if (colIndex >= 0 && colIndex < numcolunas) {
                System.out.println("Preenchendo coluna " + colIndex + " com caracteres...");
                for (int i = 0; i < numlinhas; i++) {
                    if (index < textoCriptografado.length()) {
                        grid[i][colIndex] = textoCriptografado.charAt(index++);
                    } else {
                        grid[i][colIndex] = ' ';
                    }
                }
            }
        }

        // Exibe o grid preenchido
        System.out.println("Grid preenchido:");
        for (int i = 0; i < numlinhas; i++) {
            for (int j = 0; j < numcolunas; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }

        // Reconstrói o texto original linha por linha
        System.out.println("\nReconstruindo o texto original...");
        StringBuilder textoDescriptografado = new StringBuilder();
        for (int i = 0; i < numlinhas; i++) {
            for (int j = 0; j < numcolunas; j++) {
                textoDescriptografado.append(grid[i][j]);
            }
        }

        // Mostra o texto final após descriptografar
        String resultado = textoDescriptografado.toString().trim();
        System.out.println("Texto Descriptografado: " + resultado);
        return resultado;
    }
}
