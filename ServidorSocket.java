import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ServidorSocket {

    private static List<Evento> eventos = new ArrayList<>();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static void main(String[] args) throws IOException {

        eventos.add(new Evento("Reunião de Equipe", "Sala A", LocalDateTime.of(2024, 12, 20, 14, 30, 0)));
        eventos.add(new Evento("Apresentação", "Auditório", LocalDateTime.of(2024, 12, 22, 10, 0, 0)));

        ServerSocket servidor = null;
        Socket conexao = null;
        BufferedReader entrada = null;

        try {
            servidor = new ServerSocket(7000);
            System.out.println("Servidor rodando na porta 7000...");

            conexao = servidor.accept();
            System.out.println("Cliente conectado!");

            entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            PrintStream saida = new PrintStream(conexao.getOutputStream());

            saida.println("=== SISTEMA DE EVENTOS ===");
            saida.println("Digite 1 - Cadastrar evento");
            saida.println("Digite 2 - Listar eventos");
            saida.println("Digite 3 - Sair");
            saida.println("Escolha uma opção:");

            String texto;
            do {
                texto = entrada.readLine();
                if (texto == null || "3".equals(texto)) {
                    break;
                }

                if ("1".equals(texto)) {
                    saida.println("Digite: descrição;local;data (ex: Festa;Salão;25/12/2024 20:00:00)");
                    String dados = entrada.readLine();

                    String[] partes = dados.split(";");
                    if (partes.length == 3) {
                        eventos.add(new Evento(partes[0], partes[1], LocalDateTime.parse(partes[2], formatter)));
                        saida.println("Evento cadastrado com sucesso!");
                    } else {
                        saida.println("Formato inválido!");
                    }

                } else if ("2".equals(texto)) {
                    saida.println("=== EVENTOS CADASTRADOS ===");
                    for (int i = 0; i < eventos.size(); i++) {
                        Evento e = eventos.get(i);
                        saida.println((i+1) + ". " + e.descricao + " - " + e.local + " - " + e.data.format(formatter));
                    }
                    saida.println("Total: " + eventos.size() + " eventos");

                } else {
                    saida.println("Opção inválida!");
                }

                saida.println("Digite 1 - Cadastrar evento");
                saida.println("Digite 2 - Listar eventos");
                saida.println("Digite 3 - Sair");
                saida.println("Escolha uma opção:");

            } while (true);

            saida.println("Conexão encerrada!");
            System.out.println("Cliente desconectou");

        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            if (conexao != null) conexao.close();
            if (servidor != null) servidor.close();
        }
    }

    static class Evento {
        String descricao;
        String local;
        LocalDateTime data;

        public Evento(String descricao, String local, LocalDateTime data) {
            this.descricao = descricao;
            this.local = local;
            this.data = data;
        }
    }
}