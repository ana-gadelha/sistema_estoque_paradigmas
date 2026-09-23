package estoque;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

/**
 * Interface gráfica (Swing) para o mesmo Estoque usado em EstoqueApp.
 * Não substitui EstoqueApp — é uma segunda forma de usar as mesmas classes
 * de domínio (Estoque, Product, ProdutoComum, ProdutoPerecivel), só que com
 * janela, tabela e botões em vez de texto no console.
 */
public class EstoqueGUI extends JFrame {
    private final Estoque estoque;
    private final ProdutoTableModel tableModel;
    private final JTable tabela;
    private final JLabel labelTotal;

    public EstoqueGUI(Estoque estoque) {
        super("Sistema de Estoque de Produtos");
        this.estoque = estoque;
        this.tableModel = new ProdutoTableModel(estoque);
        this.tabela = new JTable(tableModel);
        this.labelTotal = new JLabel();

        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(criarPainelCadastro(), BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
        add(criarPainelVendaETotal(), BorderLayout.SOUTH);

        atualizarTabelaETotal();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 420);
        setLocationRelativeTo(null);
    }

    private JPanel criarPainelCadastro() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        painel.setBorder(BorderFactory.createTitledBorder("Cadastrar produto"));

        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"Comum", "Perecível"});
        JTextField campoNome = new JTextField(10);
        JTextField campoPreco = new JTextField(6);
        JTextField campoQuantidade = new JTextField(5);
        JTextField campoValidade = new JTextField(4);
        campoValidade.setEnabled(false);

        comboTipo.addActionListener(e ->
            campoValidade.setEnabled("Perecível".equals(comboTipo.getSelectedItem())));

        JButton botaoCadastrar = new JButton("Cadastrar");

        painel.add(new JLabel("Tipo:"));
        painel.add(comboTipo);
        painel.add(new JLabel("Nome:"));
        painel.add(campoNome);
        painel.add(new JLabel("Preço (R$):"));
        painel.add(campoPreco);
        painel.add(new JLabel("Quantidade:"));
        painel.add(campoQuantidade);
        painel.add(new JLabel("Dias p/ vencer:"));
        painel.add(campoValidade);
        painel.add(botaoCadastrar);

        botaoCadastrar.addActionListener(e -> {
            try {
                String nome = campoNome.getText().trim();
                double preco = Double.parseDouble(campoPreco.getText().trim().replace(",", "."));
                int quantidade = Integer.parseInt(campoQuantidade.getText().trim());

                Product novo;
                if ("Perecível".equals(comboTipo.getSelectedItem())) {
                    int dias = Integer.parseInt(campoValidade.getText().trim());
                    novo = new ProdutoPerecivel(nome, preco, quantidade, dias);
                } else {
                    novo = new ProdutoComum(nome, preco, quantidade);
                }
                estoque.adicionarProduto(novo);
                atualizarTabelaETotal();

                campoNome.setText("");
                campoPreco.setText("");
                campoQuantidade.setText("");
                campoValidade.setText("");
                campoNome.requestFocus();
            } catch (QuantidadeInvalidaException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Quantidade inválida", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Preencha preço, quantidade (e dias, se for perecível) com números válidos.",
                    "Entrada inválida", JOptionPane.ERROR_MESSAGE);
            }
        });

        return painel;
    }

    private JPanel criarPainelVendaETotal() {
        JPanel painel = new JPanel(new BorderLayout());

        JPanel painelVenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        painelVenda.setBorder(BorderFactory.createTitledBorder("Vender produto selecionado na tabela"));
        JTextField campoQuantidadeVenda = new JTextField(5);
        JButton botaoVender = new JButton("Vender");
        painelVenda.add(new JLabel("Quantidade:"));
        painelVenda.add(campoQuantidadeVenda);
        painelVenda.add(botaoVender);

        botaoVender.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha == -1) {
                JOptionPane.showMessageDialog(this, "Selecione um produto na tabela primeiro.",
                    "Nenhum produto selecionado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                int quantidade = Integer.parseInt(campoQuantidadeVenda.getText().trim());
                estoque.venderProduto(linha, quantidade);
                atualizarTabelaETotal();
                campoQuantidadeVenda.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Digite uma quantidade numérica válida.",
                    "Entrada inválida", JOptionPane.ERROR_MESSAGE);
            } catch (ProdutoIndisponivelException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Produto indisponível", JOptionPane.ERROR_MESSAGE);
            }
        });

        labelTotal.setFont(labelTotal.getFont().deriveFont(Font.BOLD, 14f));
        labelTotal.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        JPanel painelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelTotal.add(labelTotal);

        painel.add(painelVenda, BorderLayout.WEST);
        painel.add(painelTotal, BorderLayout.EAST);
        return painel;
    }

    private void atualizarTabelaETotal() {
        tableModel.fireTableDataChanged();
        labelTotal.setText(String.format("Valor total do estoque: R$ %.2f", estoque.calcularValorTotalEstoque()));
    }

    /**
     * Modelo de tabela que lê direto do Estoque — não duplica os dados dos
     * produtos em outra lista, só exibe o que já existe em Estoque/Product.
     */
    private static class ProdutoTableModel extends AbstractTableModel {
        private final Estoque estoque;
        private final String[] colunas = {"#", "Nome", "Tipo", "Preço", "Quantidade", "Validade"};

        ProdutoTableModel(Estoque estoque) {
            this.estoque = estoque;
        }

        @Override public int getRowCount() { return estoque.totalDeProdutos(); }
        @Override public int getColumnCount() { return colunas.length; }
        @Override public String getColumnName(int col) { return colunas[col]; }

        @Override
        public Object getValueAt(int row, int col) {
            Product p = estoque.getProduto(row);
            switch (col) {
                case 0: return row;
                case 1: return p.getNome();
                case 2: return (p instanceof ProdutoPerecivel) ? "Perecível" : "Comum";
                case 3: return String.format("R$ %.2f", p.getPreco());
                case 4: return p.getQuantidade();
                case 5: return (p instanceof ProdutoPerecivel)
                        ? ((ProdutoPerecivel) p).getDiasParaVencer() + " dia(s)"
                        : "-";
                default: return "";
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Estoque estoque = new Estoque();
            try {
                estoque.adicionarProduto(new ProdutoComum("Caderno", 20.0, 10));
                estoque.adicionarProduto(new ProdutoComum("Caneta", 3.0, 50));
                estoque.adicionarProduto(new ProdutoPerecivel("Iogurte", 4.0, 15, 5));
                estoque.adicionarProduto(new ProdutoPerecivel("Carne", 40.0, 10, 2));
            } catch (QuantidadeInvalidaException e) {
                // não ocorre com estes valores fixos de exemplo
            }
            new EstoqueGUI(estoque).setVisible(true);
        });
    }
}
