package com.example.cantina;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cantina.adapter.AlunoAdapter;
import com.example.cantina.model.Aluno;
import com.example.cantina.repository.AlunoRepository;

import java.util.List;

public class AlunoActivity extends AppCompatActivity {


    private RecyclerView rvAlunos;
    private AlunoRepository alunoRepo;
    private AlunoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno);

        rvAlunos = findViewById(R.id.rvAlunos);
        rvAlunos.setLayoutManager(new LinearLayoutManager(this));

        alunoRepo = new AlunoRepository(this);
        carregarAlunos();
    }

    private void carregarAlunos() {
        List<Aluno> alunos = alunoRepo.listarTodos();

        if (alunos.isEmpty()) {
            Toast.makeText(this, "Nenhum aluno cadastrado", Toast.LENGTH_SHORT).show();
        }

        if (adapter == null) {
            adapter = new AlunoAdapter(alunos, new AlunoAdapter.OnAlunoClickListener() {
                @Override
                public void onAlunoClick(Aluno aluno) {
                }

                @Override
                public void onEditarClick(Aluno aluno) {
                    Intent intent = new Intent(AlunoActivity.this, AlunoCadastroActivity.class);
                    intent.putExtra("alunoId", aluno.getId());
                    startActivity(intent);
                }

                @Override
                public void onDeletarClick(Aluno aluno) {
                    new androidx.appcompat.app.AlertDialog.Builder(AlunoActivity.this)
                            .setTitle("Confirmação")
                            .setMessage("Deseja realmente deletar o aluno " + aluno.getNome() + "?")
                            .setPositiveButton("Sim", (dialog, which) -> {
                                alunoRepo.deletar(aluno.getId());
                                carregarAlunos();
                                Toast.makeText(AlunoActivity.this, "Aluno deletado", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Não", null)
                            .show();
                }
            });
            rvAlunos.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
