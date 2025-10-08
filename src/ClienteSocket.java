import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteSocket {
    public static void main(String[] args) throws IOException {

        Scanner entrada = new Scanner(System.in);
        String texto = "";
        Socket cliente = null;
        PrintStream saida = null;
        BufferedReader dados = null;

        try {
            cliente = new Socket("127.0.0.1", 7000);
            System.out.println("Conectado ao servidor");

            dados = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            saida = new PrintStream(cliente.getOutputStream());

            do {
                // Lê TODAS as linhas do menu antes de pedir input
                String linha;
                StringBuilder menuCompleto = new StringBuilder();

                while ((linha = dados.readLine()) != null) {
                    menuCompleto.append(linha).append("\n");

                    // Para quando chegar na linha que pede a opção
                    if (linha.contains("Escolha uma opção:") ||
                            linha.contains("Escolha:") ||
                            linha.equals("Escolha uma opção:")) {
                        break;
                    }
                }

                // Mostra o menu completo de uma vez
                System.out.print(menuCompleto.toString());

                // Pede a opção do usuário
                System.out.print("> ");
                texto = entrada.nextLine();

                // Envia a opção para o servidor
                saida.println(texto);

                // Se for cadastrar evento, processa os dados
                if ("1".equals(texto)) {
                    // Lê a instrução do servidor
                    String instrucao = dados.readLine();
                    System.out.println(instrucao);

                    // Pede os dados do usuário
                    System.out.print("Dados do evento > ");
                    String dadosEvento = entrada.nextLine();
                    saida.println(dadosEvento);
                }

            } while (!"3".equals(texto)); // Use "3" para sair, não "sair"

            System.out.println("Conexão encerrada!");

        } catch (IOException e) {
            System.out.println("Erro de conexão: " + e.getMessage());
        } finally {
            if (cliente != null) cliente.close();
            entrada.close();
        }
    }
}