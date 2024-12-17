package main;

import main.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Scanner;

public class ConsoleMenuController {
    private final ShowServices showServices;
    private final Scanner scanner;

    public ConsoleMenuController() {
        this.showServices = new ShowServices(new ShowRepositoryImpl());
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Criar Show");
            System.out.println("2. Comprar Ingresso");
            System.out.println("3. Criar Relatório");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

            switch (choice) {
                case 1:
                    criarShow();
                    break;
                case 2:
                    comprarIngresso();
                    break;
                case 3:
                    criarRelatorio();
                    break;
                case 4:
                    System.out.println("Saindo...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void criarShow() {
        System.out.print("Data (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        Date date = Date.from(LocalDate.parse(dateStr).atStartOfDay(ZoneId.systemDefault()).toInstant());

        System.out.print("Artista: ");
        String artista = scanner.nextLine();

        System.out.print("Cache: ");
        Double cache = scanner.nextDouble();

        System.out.print("Total Despesas: ");
        Double totalDespesas = scanner.nextDouble();

        System.out.print("Quantidade de Lotes: ");
        Integer quantLotes = scanner.nextInt();

        System.out.print("Quantidade de Ingressos por Lote: ");
        Integer quantIngressosPorLote = scanner.nextInt();

        System.out.print("Preço Normal: ");
        Double precoNormal = scanner.nextDouble();

        System.out.print("É Data Especial (true/false): ");
        Boolean isDataEspecial = scanner.nextBoolean();

        System.out.print("Desconto Lote: ");
        Double descontoLote = scanner.nextDouble();

        System.out.print("Percentual VIP (20 a 30): ");
        Double vip = scanner.nextDouble();

        showServices.criarShow(date, artista, cache, totalDespesas, quantLotes, quantIngressosPorLote, precoNormal,
                isDataEspecial, descontoLote, vip);
        System.out.println("Show criado com sucesso!");
    }

    private void comprarIngresso() {
        System.out.print("Data do Show (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        Date date = Date.from(LocalDate.parse(dateStr).atStartOfDay(ZoneId.systemDefault()).toInstant());

        System.out.print("Artista: ");
        String artista = scanner.nextLine();

        System.out.print("ID do Lote: ");
        Long idLote = scanner.nextLong();

        System.out.print("Tipo de Ingresso (VIP, NORMAL, MEIA_ENTRADA): ");
        TicketTypeEnum tipo = TicketTypeEnum.valueOf(scanner.next().toUpperCase());

        try {
            |Ticket ingresso = showServices.comprarIngresso(date, artista, idLote, tipo);
            System.out.println("Ingresso comprado com sucesso: " + ingresso.toString());
        } catch (Exception e) {
            System.out.println("Erro ao comprar ingresso: " + e.getMessage());
        }
    }

    private void criarRelatorio() {
        System.out.print("Data do Show (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        Date date = Date.from(LocalDate.parse(dateStr).atStartOfDay(ZoneId.systemDefault()).toInstant());

        System.out.print("Artista: ");
        String artista = scanner.nextLine();

        try {
            Report relatorio = showServices.criarReport(date, artista);
            System.out.println("Relatório criado com sucesso:");
            System.out.println("Ingressos Meia Entrada: " + relatorio.getNumTicketMeia());
            System.out.println("Ingressos Normal: " + relatorio.getNumTicketNormal());
            System.out.println("Ingressos VIP: " + relatorio.getNumTicketVip());
            System.out.println("Valor Total: " + relatorio.getValueTotal());
            System.out.println("Status: " + relatorio.getStatus());
        } catch (Exception e) {
            System.out.println("Erro ao criar relatório: " + e.getMessage());
        }
    }
}