package com.example.cantina;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cantina.adapter.ProdutoListAdapter;
import com.example.cantina.model.Produto;
import com.example.cantina.repository.ProdutoRepository;

import java.util.List;

public class ProdutoListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProdutoRepository repository;
    private List<Produto> listaProdutos;
    private ProdutoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_list);

        recyclerView = findViewById(R.id.rvListaProdutos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        repository = new ProdutoRepository(this);

        carregarProdutos();

        ImageView ivLogo = findViewById(R.id.ivLogo);
        ivLogo.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void carregarProdutos() {
        listaProdutos = repository.listarTodos();

        adapter = new ProdutoListAdapter(listaProdutos, new ProdutoListAdapter.OnProdutoClickListener() {
            @Override
            public void onEditar(Produto produto, int position) {
                Intent intent = new Intent(ProdutoListActivity.this, ProdutoCadastroActivity.class);
                intent.putExtra("produto_id", produto.getCodigo());
                startActivity(intent);
            }

            @Override
            public void onDeletar(Produto produto, int position) {
                mostrarDialogExcluir(produto, position);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void mostrarDialogExcluir(Produto produto, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir produto")
                .setMessage("Deseja realmente remover este produto?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    repository.deletar(produto.getCodigo());
                    listaProdutos.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, "Produto removido!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarProdutos(); // sempre atualizar ao voltar do editar/cadastro
    }
}
