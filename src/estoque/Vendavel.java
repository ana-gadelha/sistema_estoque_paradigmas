package estoque;

/**
 * Contrato para qualquer coisa que possa ser vendida.
 * Product implementa esta interface.
 */
public interface Vendavel {
    void vender(int quantidadeDesejada) throws ProdutoIndisponivelException;
}
