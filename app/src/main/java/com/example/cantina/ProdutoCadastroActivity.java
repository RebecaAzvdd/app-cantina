package com.example.cantina;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cantina.model.Produto;
import com.example.cantina.repository.ProdutoRepository;
import com.google.android.material.textfield.TextInputEditText;

public class ProdutoCadastroActivity extends AppCompatActivity {

    private TextInputEditText inputDescricao, inputPreco;
    private ProdutoRepository produtoRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_cadastro);

        inputDescricao = findViewById(R.id.inputDescricao);
        inputPreco = findViewById(R.id.inputPreco);

        produtoRepository = new ProdutoRepository(this);

        findViewById(R.id.btnSalvarProduto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarProduto();
            }
        });
    }

    private void salvarProduto() {
        String descricao = inputDescricao.getText().toString().trim();
        String precoStr = inputPreco.getText().toString().trim();

        if (descricao.isEmpty() || precoStr.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        double preco = Double.parseDouble(precoStr);
        Produto produto = new Produto(0, descricao, preco);

        long id = produtoRepository.inserir(produto);

        if (id > 0) {
            Toast.makeText(this, "Produto cadastrado!", Toast.LENGTH_SHORT).show();
            finish(); // volta para tela anterior
        } else {
            Toast.makeText(this, "Erro ao salvar produto!", Toast.LENGTH_SHORT).show();
        }
    }
}
