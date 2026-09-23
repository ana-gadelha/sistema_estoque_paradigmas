# Sistema de Estoque de Produtos

Exercício de implementação da disciplina **Paradigmas de Linguagens de Programação** (UNIPÊ),
sobre Classes Abstratas, Herança, Interfaces, Polimorfismo, Composição e Exceções em Java.

## O que o programa faz

Simula o controle de estoque de uma loja, com dois tipos de produto (comuns e perecíveis),
venda de itens e tratamento de situações inválidas através de uma hierarquia de exceções
própria, em vez de `if`s soltos.

## Estrutura

```
src/estoque/
├── EstoqueException.java              -> exceção base do domínio
├── QuantidadeInvalidaException.java   -> preço/quantidade negativos no cadastro
├── ProdutoIndisponivelException.java  -> venda maior que o estoque disponível
├── Vendavel.java                      -> interface implementada por Product
├── Product.java                       -> classe abstrata (nome, preço, quantidade)
├── ProdutoComum.java                  -> valor total = preço x quantidade
├── ProdutoPerecivel.java              -> valor total com 20% de desconto se faltam <= 3 dias para vencer
├── Estoque.java                       -> composição: TEM uma lista de Product
├── EstoqueApp.java                    -> classe principal (main) em modo texto/console
└── EstoqueGUI.java                    -> interface gráfica (Swing) das mesmas classes acima
```

`EstoqueApp` e `EstoqueGUI` são duas "portas de entrada" diferentes para as mesmas classes de
domínio (`Estoque`, `Product`, `ProdutoComum`, `ProdutoPerecivel`) — nenhuma regra de negócio é
duplicada entre elas, só muda a forma de mostrar e interagir.

## Como compilar e rodar

Pelo terminal, na raiz do projeto:

```bash
javac -d out $(find src -name "*.java")

# versão console (a que é avaliada pelo enunciado):
java -cp out estoque.EstoqueApp

# versão com interface gráfica:
java -cp out estoque.EstoqueGUI
```

Ou, no VS Code, basta abrir `src/estoque/EstoqueApp.java` ou `src/estoque/EstoqueGUI.java` e
clicar em **Run** (com a extensão Java instalada).

## EstoqueApp (console) — o que a saída demonstra

1. Cadastro de 2 produtos comuns e 2 perecíveis (um deles vencendo em breve).
2. Tentativa de cadastro com quantidade negativa → `QuantidadeInvalidaException` capturada.
3. Listagem do estoque cadastrado.
4. Uma venda válida, reduzindo a quantidade do produto.
5. Tentativa de vender mais do que o disponível → `ProdutoIndisponivelException` capturada.
6. Valor total do estoque, somado por polimorfismo (cada produto calcula do seu próprio jeito,
   sem o `Estoque` precisar saber se é um `ProdutoComum` ou `ProdutoPerecivel`).
7. Simulação de compra opcional (digite índice e quantidade) mostrando subtotal, desconto e
   valor final — pulada automaticamente se não houver teclado disponível na execução.

## EstoqueGUI (janela) — o que dá pra fazer

- Ver todos os produtos numa tabela (nome, tipo, preço, quantidade, validade).
- Cadastrar um produto novo (comum ou perecível) preenchendo o formulário no topo.
- Selecionar um produto na tabela, informar uma quantidade e clicar em **Vender**.
- Ver o valor total do estoque atualizado automaticamente a cada cadastro ou venda.
- Erros (quantidade inválida, produto indisponível, entrada com texto no lugar de número)
  aparecem como caixas de diálogo, em vez de mensagens de console.
