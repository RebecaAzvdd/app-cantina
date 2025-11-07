package com.example.cantina;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Botão Cadastrar Aluno
        findViewById(R.id.btnCadastrarAluno).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlunoCadastroActivity.class);
                startActivity(intent);
            }
        });

        // Botão Listar Alunos
        findViewById(R.id.btnListarAlunos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlunoActivity.class);
                startActivity(intent);
            }
        });

        // Botão Registrar Venda
        findViewById(R.id.btnVendas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VendaActivity.class);
                startActivity(intent);
            }
        });

        // Botão Cadastrar Produto
//        findViewById(R.id.btnProdutos).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, ProdutoCadastroActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        // Botão Histórico de Vendas
//        findViewById(R.id.btnHistorico).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, HistoricoVendaActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}
