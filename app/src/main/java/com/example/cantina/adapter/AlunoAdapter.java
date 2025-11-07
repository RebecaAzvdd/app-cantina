package com.example.cantina.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cantina.R;
import com.example.cantina.model.Aluno;

import java.util.List;

public class AlunoAdapter extends RecyclerView.Adapter<AlunoAdapter.AlunoViewHolder>{

    private final List<Aluno> alunos;
    private final OnAlunoClickListener listener;

    public interface OnAlunoClickListener {
        void onAlunoClick(Aluno aluno);
        void onEditarClick(Aluno aluno);
        void onDeletarClick(Aluno aluno);
    }

    public AlunoAdapter(List<Aluno> alunos, OnAlunoClickListener listener) {
        this.alunos = alunos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlunoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aluno, parent, false);
        return new AlunoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlunoViewHolder holder, int position) {
        Aluno aluno = alunos.get(position);
        holder.tvNome.setText(aluno.getNome());
        holder.tvResponsavel.setText("Responsável: " + aluno.getResponsavel());
        holder.tvTelefone.setText("Telefone: " + aluno.getTelefone());

        holder.itemView.setOnClickListener(v -> listener.onAlunoClick(aluno));
        holder.btnEditar.setOnClickListener(v -> listener.onEditarClick(aluno));
        holder.btnDeletar.setOnClickListener(v -> listener.onDeletarClick(aluno));
    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }

    public static class AlunoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvResponsavel, tvTelefone;
        Button btnEditar, btnDeletar;

        public AlunoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeAluno);
            tvResponsavel = itemView.findViewById(R.id.tvResponsavel);
            tvTelefone = itemView.findViewById(R.id.tvTelefone);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnDeletar = itemView.findViewById(R.id.btnDeletar);
        }
    }
}
