package Codigo_Intellij.Sistema_Java.Cripto_Sistemordenacao;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class CriptAES_TimSort {
    private static final String ALGORITMO = "AES";
    private static SecretKey chaveSecreta;

    // Construtor que recebe a chave como string
    public CriptAES_TimSort(String chave) {
        if (chave.length() != 16) { // Verifica tamanho da chave
            throw new IllegalArgumentException("A chave deve ter exatamente 16 caracteres (128 bits para AES-128).");
        }
        this.chaveSecreta = new SecretKeySpec(chave.getBytes(), ALGORITMO);
    }

    // Método para marcar o texto original
    public static Map<String, String> marcarTexto(String texto) {
        Map<String, String> textoMarcado = new LinkedHashMap<>();
        String[] palavras = texto.split("\\s+");

        for (int i = 0; i < palavras.length; i++) {
            String palavraLimpa = palavras[i].replaceAll("[^a-zA-Záàãâäéèêëíìîïóòõôöúùûüç]", "");
            textoMarcado.put(palavraLimpa, "Marcação " + (i + 1));
        }

        // Removido: System.out.println("\nTexto original com marcação:");
        // Removido: textoMarcado.forEach((palavra, marcacao) ->
        // Removido: System.out.println(palavra + " | " + marcacao)
        // Removido: );

        return textoMarcado;
    }

    // Método para ordenar e criptografar texto marcado
    public static List<String> ordenarComTimSort(String texto, String caminhoSaida) {
        // Realiza a marcação do texto
        Map<String, String> textoMarcado = marcarTexto(texto);

        // Extrair palavras para ordenação
        List<String> palavras = new ArrayList<>(textoMarcado.keySet());
        Collections.sort(palavras, String.CASE_INSENSITIVE_ORDER);

        // Exibir palavras que já estavam na posição certa
        System.out.println("\nPalavras que estão na posição certa:");
        int index = 1;
        for (String palavra : palavras) {
            if (textoMarcado.get(palavra).equals("Marcação " + index)) {
                System.out.println("Posição " + index + ": " + palavra);
            }
            index++;
        }

        System.out.println("\nTexto após ordenação:");
        for (int i = 0; i < palavras.size(); i++) {
            System.out.println("Posição " + (i + 1) + ": " + palavras.get(i));
        }

        System.out.println("\nFinalização dos métodos:");
        System.out.println("Texto já criptografado e ordenado | Marcação:");
        StringBuilder textoFinalCriptografado = new StringBuilder();
        for (int i = 0; i < palavras.size(); i++) {
            try {
                String palavraCriptografada = criptografar(palavras.get(i));
                System.out.println(palavraCriptografada + " | " + textoMarcado.get(palavras.get(i)));
                textoFinalCriptografado.append(palavraCriptografada).append(" ");
            } catch (Exception e) {
                System.out.println("Erro ao criptografar palavra: " + palavras.get(i));
            }
        }

        String textoCriptografadoCompleto = textoFinalCriptografado.toString().trim();
        salvarComMarcacao(caminhoSaida, palavras, textoMarcado);

        System.out.println("Texto salvo em " + caminhoSaida);
        return palavras;
    }

    public static String criptografar(String texto) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.ENCRYPT_MODE, chaveSecreta);
        byte[] textoCriptografado = cipher.doFinal(texto.getBytes());
        return Base64.getEncoder().encodeToString(textoCriptografado);
    }

    // Método para salvar o texto criptografado com as marcações no arquivo
    public static void salvarComMarcacao(String caminhoSaida, List<String> textoOrdenado, Map<String, String> textoMarcado) {
        try {
            List<String> linhasComMarcacao = new ArrayList<>();
            for (int i = 0; i < textoOrdenado.size(); i++) {
                String textoCriptografado = criptografar(textoOrdenado.get(i)); // Criptografa a palavra
                String linhaComMarcacao = textoCriptografado + " | " + textoMarcado.get(textoOrdenado.get(i));
                linhasComMarcacao.add(linhaComMarcacao);
            }

            String resultado = String.join(System.lineSeparator(), linhasComMarcacao);

            Files.writeString(Path.of(caminhoSaida), resultado, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            System.out.println("Erro ao salvar o arquivo com marcação: " + e.getMessage());
        }
    }

    // Método para salvar o texto criptografado em um arquivo
    public static void salvarEmArquivo(String caminhoSaida, String textoCriptografado) {
        try {
            Files.writeString(Path.of(caminhoSaida), textoCriptografado, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
}
