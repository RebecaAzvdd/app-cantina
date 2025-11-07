package com.example.cantina.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cantina.R;
import com.example.cantina.model.ItemVenda;

import java.util.List;

public class ProdutoVendaAdapter extends RecyclerView.Adapter<ProdutoVendaAdapter.ProdutoVendaViewHolder>{

    private final List<ItemVenda> itens;
    private final OnItemVendaClickListener listener;

    public interface OnItemVendaClickListener {
        void onEditarQuantidade(ItemVenda item, int position);
        void onRemoverItem(ItemVenda item, int position);
    }

    public ProdutoVendaAdapter(List<ItemVenda> itens, OnItemVendaClickListener listener) {
        this.itens = itens;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProdutoVendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_produto_venda, parent, false);
        return new ProdutoVendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoVendaViewHolder holder, int position) {
        ItemVenda item = itens.get(position);

        holder.tvNome.setText(item.getProduto().getDescricao());
        holder.tvPreco.setText(String.format("R$ %.2f", item.getProduto().getPreco()));
        holder.tvQtd.setText("Qtd: " + item.getQuantidade());

        holder.btnEditar.setOnClickListener(v -> listener.onEditarQuantidade(item, position));
        holder.btnRemover.setOnClickListener(v -> listener.onRemoverItem(item, position));
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public static class ProdutoVendaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvPreco, tvQtd;
        Button btnEditar, btnRemover;

        public ProdutoVendaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeProduto);
            tvPreco = itemView.findViewById(R.id.tvPrecoProduto);
            tvQtd = itemView.findViewById(R.id.tvQuantidade);
            btnEditar = itemView.findViewById(R.id.btnEditarQtd);
            btnRemover = itemView.findViewById(R.id.btnRemover);
        }
    }
}
