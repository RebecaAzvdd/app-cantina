package com.example.cantina.model;

import java.util.ArrayList;
import java.util.List;

public class Venda {

    private int id;
    private List<ItemVenda> itens;
    private String data;
    private String metodoPagamento;
    private double valorTotal;

    // 🔹 Identificação do aluno (cadastrado ou não)
    private Integer alunoId;                // id do aluno cadastrado (pode ser null)
    private Aluno aluno;                    // objeto aluno (opcional)
    private String nomeAlunoNaoCadastrado;  // nome do aluno não cadastrado

    // 🔹 Construtores
    public Venda() {
        this.itens = new ArrayList<>();
    }

    public Venda(int id, String data, String metodoPagamento, double valorTotal,
                 Integer alunoId, Aluno aluno, String nomeAlunoNaoCadastrado) {
        this.id = id;
        this.data = data;
        this.metodoPagamento = metodoPagamento;
        this.valorTotal = valorTotal;
        this.alunoId = alunoId;
        this.aluno = aluno;
        this.nomeAlunoNaoCadastrado = nomeAlunoNaoCadastrado;
        this.itens = new ArrayList<>();
    }

    // ===============================
    // 🔹 Getters e Setters
    // ===============================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ItemVenda> getItens() {
        if (itens == null) {
            itens = new ArrayList<>();
        }
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens != null ? itens : new ArrayList<>();
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    /**
     * Calcula automaticamente o valor total com base nos itens da venda.
     */
    public double getValorTotal() {
        double total = 0;
        if (itens != null) {
            for (ItemVenda item : itens) {
                total += item.getTotalItem();
            }
        }
        return total;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Integer getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Integer alunoId) {
        this.alunoId = alunoId;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
        if (aluno != null) {
            this.alunoId = aluno.getId();
            this.nomeAlunoNaoCadastrado = null;
        }
    }

    public String getNomeAlunoNaoCadastrado() {
        return nomeAlunoNaoCadastrado;
    }

    public void setNomeAlunoNaoCadastrado(String nomeAlunoNaoCadastrado) {
        this.nomeAlunoNaoCadastrado = nomeAlunoNaoCadastrado;
        if (nomeAlunoNaoCadastrado != null && !nomeAlunoNaoCadastrado.isEmpty()) {
            this.aluno = null;
            this.alunoId = null;
        }
    }

    // ===============================
    // 🔹 Métodos de Negócio
    // ===============================

    /**
     * Adiciona um produto à venda com a quantidade informada.
     */
    public void adicionarItem(Produto produto, int quantidade) {
        if (produto == null || quantidade <= 0) return;
        if (itens == null) itens = new ArrayList<>();

        itens.add(new ItemVenda(produto, quantidade));
    }

    /**
     * Remove todos os itens da venda.
     */
    public void limparVenda() {
        if (itens != null) {
            itens.clear();
        }
    }

    // ===============================
    // 🔹 toString()
    // ===============================

    @Override
    public String toString() {
        String nomeAluno = aluno != null ? aluno.getNome() :
                (nomeAlunoNaoCadastrado != null ? nomeAlunoNaoCadastrado : "Sem aluno");
        return "Venda {" +
                "id=" + id +
                ", aluno='" + nomeAluno + '\'' +
                ", data='" + data + '\'' +
                ", metodoPagamento='" + metodoPagamento + '\'' +
                ", valorTotal=" + getValorTotal() +
                ", itens=" + itens.size() +
                '}';
    }
}
