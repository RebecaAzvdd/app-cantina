package com.example.cantina;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cantina.model.Aluno;
import com.example.cantina.repository.AlunoRepository;

public class AlunoCadastroActivity extends AppCompatActivity {

    private EditText etNome, etResponsavel, etTelefone, etCredito;
    private Button btnSalvar, btnCancelar;
    private AlunoRepository alunoRepo;

    private int alunoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_cadastro);

        // Inicializa componentes
        etNome = findViewById(R.id.etNome);
        etResponsavel = findViewById(R.id.etResponsavel);
        etTelefone = findViewById(R.id.etTelefone);
        etCredito = findViewById(R.id.etCredito);
        btnSalvar = findViewById(R.id.btnSalvar);
        btnCancelar = findViewById(R.id.btnCancelar);

        // Inicializa repositório
        alunoRepo = new AlunoRepository(this);

        // Verifica se é edição
        if (getIntent().hasExtra("alunoId")) {
            alunoId = getIntent().getIntExtra("alunoId", -1);
            carregarAluno(alunoId);
        }

        // Botões
        btnSalvar.setOnClickListener(v -> salvarAluno());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void carregarAluno(int id) {
        if (id <= 0) return;
        Aluno aluno = alunoRepo.buscarPorId(id);
        if (aluno != null) {
            etNome.setText(aluno.getNome());
            etResponsavel.setText(aluno.getResponsavel());
            etTelefone.setText(aluno.getTelefone());
            etCredito.setText(String.valueOf(aluno.getCredito()));
        }
    }

    private void salvarAluno() {
        String nome = etNome.getText().toString().trim();
        String responsavel = etResponsavel.getText().toString().trim();
        String telefone = etTelefone.getText().toString().trim();
        String creditoStr = etCredito.getText().toString().trim();
        double credito = 0;

        // Validação de crédito
        if (!TextUtils.isEmpty(creditoStr)) {
            try {
                credito = Double.parseDouble(creditoStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Crédito inválido", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Validação de campos obrigatórios
        if (TextUtils.isEmpty(nome)) {
            etNome.setError("Nome é obrigatório");
            etNome.requestFocus();
            return;
        }

        Aluno aluno = new Aluno(alunoId, nome, responsavel, telefone, credito);

        if (alunoId == -1) {
            // Novo aluno
            long id = alunoRepo.inserir(aluno);
            if (id > 0) {
                Toast.makeText(this, "Aluno cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao cadastrar aluno", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Edição
            if (alunoId > 0) {
                int linhas = alunoRepo.atualizar(aluno);
                if (linhas > 0) {
                    Toast.makeText(this, "Aluno atualizado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erro ao atualizar aluno", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "ID do aluno inválido", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
