package com.example.cantina.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cantina.R;
import com.example.cantina.model.Produto;

import java.util.List;

public class ProdutoListAdapter extends RecyclerView.Adapter<ProdutoListAdapter.ViewHolder> {

    private final List<Produto> produtos;
    private final OnProdutoClickListener listener;

    public interface OnProdutoClickListener {
        void onEditar(Produto produto, int position);
        void onDeletar(Produto produto, int position);
    }

    public ProdutoListAdapter(List<Produto> produtos, OnProdutoClickListener listener) {
        this.produtos = produtos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_produto_venda, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Produto produto = produtos.get(position);

        holder.tvNome.setText(produto.getDescricao());
        holder.tvPreco.setText(String.format("R$ %.2f", produto.getPreco()));
        holder.tvQtd.setVisibility(View.GONE); // esconder já que não existe aqui

        holder.btnEditar.setOnClickListener(v -> listener.onEditar(produto, position));
        holder.btnRemover.setOnClickListener(v -> listener.onDeletar(produto, position));
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvPreco, tvQtd;
        Button btnEditar, btnRemover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeProduto);
            tvPreco = itemView.findViewById(R.id.tvPrecoProduto);
            tvQtd = itemView.findViewById(R.id.tvQuantidade);
            btnEditar = itemView.findViewById(R.id.btnEditarQtd);
            btnRemover = itemView.findViewById(R.id.btnRemover);
        }
    }
}
