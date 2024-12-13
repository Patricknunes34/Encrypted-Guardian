package Codigo_Intellij.Sistema_Java.Descriptografar;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Comparator;

public class SistemaRevercao_AES {

    private static final String ALGORITMO = "AES";
    private SecretKeySpec chaveSecreta;

    public SistemaRevercao_AES(String chave) {
        if (chave.length() != 16) {
            throw new IllegalArgumentException("A chave deve ter exatamente 16 caracteres (128 bits para AES-128).");
        }
        this.chaveSecreta = new SecretKeySpec(chave.getBytes(), ALGORITMO);
        System.out.println("\nChave secreta configurada: " + chave + "\n");  // Exibe a chave configurada
    }

    // Método para descriptografar texto criptografado com AES
    public String descriptografar(String textoCriptografado) throws Exception {
        System.out.println("\nIniciando a descriptografia do texto criptografado...");  // Etapa 1
        System.out.println("Texto criptografado: \n" + textoCriptografado);  // Exibe o texto criptografado
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        cipher.init(Cipher.DECRYPT_MODE, chaveSecreta);
        byte[] textoDescriptografado = cipher.doFinal(Base64.getDecoder().decode(textoCriptografado));
        String resultado = new String(textoDescriptografado);
        System.out.println("\nTexto descriptografado: \n" + resultado);  // Etapa 2
        return resultado;
    }

    // Método para ordenar texto com base nas marcações
    public List<String> ordenarPorMarcacao(List<String> linhas) {
        System.out.println("\nIniciando a ordenação das linhas com base nas marcações...");  // Etapa 3
        // Lista de objetos para armazenar a linha e a marcação
        List<MensagemComMarcacao> mensagensComMarcacao = new ArrayList<>();

        for (String linha : linhas) {
            String[] partes = linha.split(" \\|");
            String base64 = partes[0].trim();  // Texto criptografado
            String marcacao = partes.length > 1 ? partes[1].trim() : ""; // Marcação
            mensagensComMarcacao.add(new MensagemComMarcacao(base64, marcacao));

            System.out.println("Linha adicionada à lista de marcação: base64 = " + base64 + ", marcação = " + marcacao);  // Exibe linha e marcação
        }

        // Ordena pela marcação
        Collections.sort(mensagensComMarcacao, Comparator.comparingInt(m -> Integer.parseInt(m.getMarcacao().replaceAll("[^0-9]", ""))));

        List<String> linhasOrdenadas = new ArrayList<>();
        for (MensagemComMarcacao mensagem : mensagensComMarcacao) {
            linhasOrdenadas.add(mensagem.getBase64());
        }

        System.out.println("\nLinhas ordenadas: ");
        for (String linha : linhasOrdenadas) {
            System.out.println(linha);  // Exibe as linhas ordenadas uma por uma
        }
        return linhasOrdenadas;
    }

    public List<String> descriptografarEReverterOrdenacao(String textoCriptografadoBase64) {
        try {
            System.out.println("\nIniciando o processo de descriptografar e reverter a ordenação...");  // Etapa 4
            String[] linhas = textoCriptografadoBase64.split("\n");
            List<String> textosBase64 = new ArrayList<>();

            for (String linha : linhas) {
                String base64 = linha.split(" \\|")[0].trim();
                textosBase64.add(base64);
                System.out.println("\nTexto base64 extraído da linha: \n" + base64);  // Exibe cada base64 extraído
            }

            List<String> linhasOrdenadas = ordenarPorMarcacao(new ArrayList<>(List.of(textoCriptografadoBase64.split("\n"))));

            List<String> textoDescriptografado = new ArrayList<>();
            for (String base64 : linhasOrdenadas) {
                System.out.println("\nDescriptografando texto base64: \n" + base64);  // Exibe o texto antes de descriptografar
                textoDescriptografado.add(descriptografar(base64));
            }

            System.out.println("\nTexto final após descriptografar e reverter a ordenação: ");
            for (String linha : textoDescriptografado) {
                System.out.println(linha);  // Exibe o texto final após a descriptografia
            }

            return textoDescriptografado;
        } catch (Exception e) {
            System.out.println("Erro ao descriptografar e restaurar a ordenação com AES: " + e.getMessage());
            return null;
        }
    }

    private static class MensagemComMarcacao {
        private String base64;
        private String marcacao;

        public MensagemComMarcacao(String base64, String marcacao) {
            this.base64 = base64;
            this.marcacao = marcacao;
        }

        public String getBase64() {
            return base64;
        }

        public String getMarcacao() {
            return marcacao;
        }
    }
}
