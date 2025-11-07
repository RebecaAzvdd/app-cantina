package com.example.cantina;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cantina.adapter.ProdutoVendaAdapter;
import com.example.cantina.model.Aluno;
import com.example.cantina.model.ItemVenda;
import com.example.cantina.model.Produto;
import com.example.cantina.model.Venda;
import com.example.cantina.repository.AlunoRepository;
import com.example.cantina.repository.ProdutoRepository;
import com.example.cantina.repository.VendaRepository;

import java.util.ArrayList;
import java.util.List;

public class VendaActivity extends AppCompatActivity {

    private RecyclerView rvProdutos;
    private ProdutoVendaAdapter adapter;
    private Venda vendaAtual;

    private AlunoRepository alunoRepo;
    private ProdutoRepository produtoRepo;
    private VendaRepository vendaRepo;

    private Spinner spAlunos;
    private EditText etAlunoNaoCadastrado;
    private Spinner spPagamento;
    private TextView tvTotal;
    private Button btnFinalizar;

    private List<Aluno> alunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda);

        alunoRepo = new AlunoRepository(this);
        produtoRepo = new ProdutoRepository(this);
        vendaRepo = new VendaRepository(this);

        rvProdutos = findViewById(R.id.rvProdutos);
        rvProdutos.setLayoutManager(new LinearLayoutManager(this));

        spAlunos = findViewById(R.id.spAlunos);
        etAlunoNaoCadastrado = findViewById(R.id.etAlunoNaoCadastrado);
        spPagamento = findViewById(R.id.spPagamento);
        tvTotal = findViewById(R.id.tvTotal);
        btnFinalizar = findViewById(R.id.btnFinalizar);

        vendaAtual = new Venda();

        carregarAlunos();
        carregarProdutos();
        configurarAdapter();
        configurarPagamento();

        btnFinalizar.setOnClickListener(v -> finalizarVenda());
    }

    private void carregarAlunos() {
        alunos = alunoRepo.listarTodos();
        List<String> nomes = new ArrayList<>();
        nomes.add("Aluno não cadastrado");
        for (Aluno a : alunos) {
            nomes.add(a.getNome());
        }
        ArrayAdapter<String> adapterAlunos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nomes);
        adapterAlunos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAlunos.setAdapter(adapterAlunos);
    }

    private void carregarProdutos() {
        List<Produto> produtos = produtoRepo.listarTodos();
        for (Produto p : produtos) {
            vendaAtual.adicionarItem(p, 1); // quantidade inicial 1
        }
    }

    private void configurarAdapter() {
        adapter = new ProdutoVendaAdapter(vendaAtual.getItens(), new ProdutoVendaAdapter.OnItemVendaClickListener() {
            @Override
            public void onEditarQuantidade(ItemVenda item, int position) {
                mostrarDialogEditarQuantidade(item, position);
            }

            @Override
            public void onRemoverItem(ItemVenda item, int position) {
                vendaAtual.getItens().remove(position);
                adapter.notifyItemRemoved(position);
                atualizarTotal();
            }
        });
        rvProdutos.setAdapter(adapter);
        atualizarTotal();
    }

    private void configurarPagamento() {
        String[] formas = {"Dinheiro", "Cartão Débito", "Cartão Crédito"};
        ArrayAdapter<String> adapterPagamento = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, formas);
        adapterPagamento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPagamento.setAdapter(adapterPagamento);
    }

    private void atualizarTotal() {
        tvTotal.setText(String.format("Total: R$ %.2f", vendaAtual.getValorTotal()));
    }

    private void mostrarDialogEditarQuantidade(ItemVenda item, int position) {
        EditText etQtd = new EditText(this);
        etQtd.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        etQtd.setText(String.valueOf(item.getQuantidade()));

        new AlertDialog.Builder(this)
                .setTitle("Editar quantidade")
                .setView(etQtd)
                .setPositiveButton("OK", (dialog, which) -> {
                    String strQtd = etQtd.getText().toString();
                    if (!TextUtils.isEmpty(strQtd)) {
                        int novaQtd = Integer.parseInt(strQtd);
                        item.setQuantidade(novaQtd);
                        adapter.notifyItemChanged(position);
                        atualizarTotal();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void finalizarVenda() {
        int posAluno = spAlunos.getSelectedItemPosition();
        if (posAluno == 0) { // aluno não cadastrado
            String nome = etAlunoNaoCadastrado.getText().toString().trim();
            if (TextUtils.isEmpty(nome)) {
                Toast.makeText(this, "Informe o nome do aluno não cadastrado", Toast.LENGTH_SHORT).show();
                return;
            }
            vendaAtual.setNomeAlunoNaoCadastrado(nome);
        } else {
            vendaAtual.setAluno(alunos.get(posAluno - 1));
        }

        vendaAtual.setMetodoPagamento(spPagamento.getSelectedItem().toString());

        long idVenda = vendaRepo.inserir(vendaAtual);
        if (idVenda > 0) {
            Toast.makeText(this, "Venda registrada com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erro ao registrar a venda", Toast.LENGTH_SHORT).show();
        }
    }
}
