package Codigo_Intellij.Sistema_Java.Cripto_Sistemordenacao;

import Codigo_Intellij.Sistema_Java.Descriptografar.SistemaRevercao_AES;
import Codigo_Intellij.Sistema_Java.Descriptografar.SistemaRevercao_Cesar;
import Codigo_Intellij.Sistema_Java.Descriptografar.SistemaRevercao_Transposicao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static Map<String, Long> temposCriptografias = new HashMap<>();

    public List<String> leituraDeLinha(String diretorio_usuario) {
        try {
            return Files.readAllLines(Paths.get(diretorio_usuario));
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Main principal = new Main();

        while (true) {
            System.out.println("\n---------------------------------- Encrypted Guardian -------------------------------------------");
            System.out.println("1) Criptografar uma mensagem de um arquivo.txt");
            System.out.println("2) Ler um arquivo.txt criptografado");
            System.out.println("3) Finalizar Sistema \n");

            int escolha123 = -1;
            boolean entradaValida = false;

            while (!entradaValida) {
                try {
                    escolha123 = scanner.nextInt();
                    scanner.nextLine();  // Limpar o buffer
                    entradaValida = true;
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida! Por favor, insira (1, 2 ou 3).");
                    scanner.nextLine();  // Limpar o buffer
                }
            }

            if (escolha123 == 1) {
                criptografarMensagem(scanner, principal);
            } else if (escolha123 == 2) {
                descriptografarMensagem(scanner, principal);
            } else if (escolha123 == 3) {
                exibirCriptografiaMaisRapida();
                System.out.println("Sistema finalizado. Obrigado por usar o Encrypted Guardian!");
                break;
            } else {
                System.out.println("Opção inválida, escolha de novo");
            }
        }
        scanner.close();
    }

    private static void criptografarMensagem(Scanner scanner, Main principal) {
        System.out.println("Informe o diretório do arquivo que deseja criptografar:");
        String diretorio_usuario = scanner.nextLine().replace("\"", "");

        List<String> linhas = principal.leituraDeLinha(diretorio_usuario);

        if (linhas != null) {
            String textoCompleto = String.join(System.lineSeparator(), linhas);
            System.out.println("Texto lido do arquivo: " + "\n" + textoCompleto);

            System.out.println("\nAgora informe qual tipo de criptografia gostaria de usar:");
            System.out.println("A) AES");
            System.out.println("B) CÉSAR");
            System.out.println("C) TRANSPOSIÇÃO");
            String escolhaabcd = scanner.nextLine().trim();

            try {
                long startTime, endTime = 0;

                switch (escolhaabcd.toUpperCase()) {
                    case "A":
                        try {
                            // Instancia CriptAES_TimSort com a chave fornecida
                            CriptAES_TimSort criptAES = new CriptAES_TimSort("minhachave123456");
                            String caminhoSaida = "Texto_criptografado_AES.txt";

                            // Marca o início do tempo de execução
                            startTime = System.nanoTime();

                            // Passando o texto original (não o caminho) para ordenar com TimSort
                            List<String> textoOrdenado = criptAES.ordenarComTimSort(textoCompleto, caminhoSaida);  // Aqui, textoCompleto é o texto original que você deseja ordenar

                            // Gera o mapa de marcação com base no texto original
                            Map<String, String> textoMarcado = CriptAES_TimSort.marcarTexto(textoCompleto);  // Marca o texto original

                            // Salva o texto criptografado com marcações no arquivo
                            criptAES.salvarComMarcacao(caminhoSaida, textoOrdenado, textoMarcado);  // Passando textoOrdenado e textoMarcado para salvar no arquivo

                            // Criptografa o texto ordenado
                            String textoCriptografado = criptAES.criptografar(String.join(" ", textoOrdenado));

                            // Marca o final do tempo de execução
                            endTime = System.nanoTime(); // Usando nanoTime para precisão
                            temposCriptografias.put("AES", (endTime - startTime) / 1000000);  // Convertendo de nanosegundos para milissegundos
                            System.out.println("Criptografia AES concluída em: " + (endTime - startTime) / 1000000 + " Milissegundos.");

                        } catch (Exception e) {
                            System.out.println("Erro ao criptografar com AES: " + e.getMessage());
                        }
                        break;

                    case "B":
                        int deslocamento = 3;
                        startTime = System.nanoTime();
                        CriptCesar_MergeSort criptCesar = new CriptCesar_MergeSort();
                        criptCesar.executar(textoCompleto, deslocamento, true);
                        endTime = System.nanoTime(); // Usando nanoTime para precisão
                        temposCriptografias.put("César", (endTime - startTime) / 1000000); // Convertendo de nanosegundos para milissegundos
                        System.out.println("Criptografia César concluída em: " + (endTime - startTime) / 1000000 + " Milissegundos.");
                        break;

                    case "C":
                        startTime = System.nanoTime();
                        CriptTransposicao_BubbleSort criptTransposicaoBubbleSort = new CriptTransposicao_BubbleSort();
                        // Chamar o método que será usado pelo programa para passar o valor ou informação do usuário
                        String texto = CriptTransposicao_BubbleSort.entradadedados(diretorio_usuario);
                        String textoCriptografado = String.valueOf(CriptTransposicao_BubbleSort.CriptTransposicao("1 2 3 1 5", texto));

                        String caminhoSaidatransposicao = "Texto_criptografado_Transposicao.txt";
                        salvarEmArquivo(caminhoSaidatransposicao, textoCriptografado);
                        endTime = System.nanoTime(); // Usando nanoTime para precisão
                        temposCriptografias.put("Transposição", (endTime - startTime) / 1000000); // Convertendo de nanosegundos para milissegundos
                        System.out.println("Criptografia Transposição concluída em: " + (endTime - startTime) / 1000000 + " Milissegundos.");
                        break;

                    default:
                        System.out.println("Escolha inválida. Tente novamente.");
                }
            } catch (Exception e) {
                System.out.println("Erro ao criptografar: " + e.getMessage());
            }
        }
    }

    private static void descriptografarMensagem(Scanner scanner, Main principal) {
        System.out.println("Informe o diretório do arquivo criptografado que deseja ler:");
        String diretorio_usuario = scanner.nextLine().replace("\"", "");

        List<String> linhas = principal.leituraDeLinha(diretorio_usuario);

        if (linhas != null) {
            String textoCompleto = String.join(System.lineSeparator(), linhas);
            System.out.println("Conteúdo do arquivo criptografado: \n" + textoCompleto);

            System.out.println("\nQual tipo de criptografia foi usado?\n1) AES\n2) César\n3) Transposição\n");
            String tipoCriptografia = scanner.nextLine().trim();

            try {
                switch (tipoCriptografia) {
                    case "1":
                        try {
                            // Instanciando o objeto de descriptografia com a chave fornecida
                            SistemaRevercao_AES timsortAes = new SistemaRevercao_AES("minhachave123456");

                            // Verifique se o textoCompleto está em Base64 válido antes de passar para descriptografar
                            // Separar as partes base64 do textoCompleto e chamar a descriptografia
                            List<String> linhasRestauradas = timsortAes.descriptografarEReverterOrdenacao(textoCompleto);

                            // Exibir o texto restaurado após a descriptografia e a ordenação, com as palavras separadas por espaço
                            System.out.println("Texto restaurado após descriptografia e ordenação:");
                            System.out.println(String.join(" ", linhasRestauradas));  // Usando espaço como separador

                            // Salva o texto restaurado em arquivo
                            salvarEmArquivo("texto_restaurado_AES.txt", String.join(" ", linhasRestauradas));  // Salva com palavras separadas por espaço

                        } catch (Exception e) {
                            System.out.println("Erro ao descriptografar e restaurar a ordenação com AES: " + e.getMessage());
                        }
                        break;

                    case "2":
                        int deslocamento = 3;
                        String Descriptografia = SistemaRevercao_Cesar.descriptografar(diretorio_usuario, "Descriptografia_Cesar.txt", deslocamento);
                        String caminhoSaidaxor = "Descriptografia_Cesar.txt";
                        salvarEmArquivo(caminhoSaidaxor,Descriptografia);
                        break;

                    case "3":
                        SistemaRevercao_Transposicao sistemaTransposicao = new SistemaRevercao_Transposicao();
                        List<String> linhasArquivo = principal.leituraDeLinha(diretorio_usuario);
                        if (linhasArquivo != null) {
                            String textoCriptografado = String.join(System.lineSeparator(), linhasArquivo);
                            String textoDescriptografado = sistemaTransposicao.descriptografar("4 3 2 1 5", textoCriptografado);

                            String caminhoSaid = "Texto_descript_Trasnpo.txt";
                            salvarEmArquivo(caminhoSaid,textoDescriptografado);

                        } else {
                            System.out.println("Erro ao ler o arquivo para descriptografia.");
                        }
                        break;

                    default:
                        System.out.println("Descriptografia não suportada para o tipo: " + tipoCriptografia);
                }
            } catch (Exception e) {
                System.out.println("Erro ao descriptografar: " + e.getMessage());
            }
        }
    }

    private static void exibirCriptografiaMaisRapida() {
        if (temposCriptografias.isEmpty()) {
            System.out.println("Nenhuma criptografia foi testada.");
        } else {
            temposCriptografias.entrySet()
                    .stream()
                    .min(Map.Entry.comparingByValue())
                    .ifPresent(entry -> System.out.println("A criptografia mais rápida foi: " + entry.getKey() + " com tempo: " + entry.getValue() + " Milisegundos"));
        }
    }

    private static void salvarEmArquivo(String caminho, String texto) {
        try {
            Files.write(Paths.get(caminho), texto.getBytes());
            System.out.println("Texto salvo em " + caminho);
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
}
