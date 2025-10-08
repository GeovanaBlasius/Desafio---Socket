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
                String linha;
                while ((linha = dados.readLine()) != null) {
                    System.out.println(linha);

                    if (linha.equals("Escolha uma opção:")) {
                        break;
                    }
                }

                System.out.print("> ");
                texto = entrada.nextLine();

                saida.println(texto);

                if ("1".equals(texto)) {
                    String instrucao = dados.readLine();
                    System.out.println(instrucao);

                    System.out.print("Dados do evento > ");
                    String dadosEvento = entrada.nextLine();
                    saida.println(dadosEvento);

                    String resposta = dados.readLine();
                    System.out.println(resposta);
                }

            } while (!"3".equals(texto));

            System.out.println("Conexão encerrada!");

        } catch (IOException e) {
            System.out.println("Erro de conexão: " + e.getMessage());
        } finally {
            if (cliente != null) cliente.close();
            entrada.close();
        }
    }
}