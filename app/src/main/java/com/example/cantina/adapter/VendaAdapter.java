package com.example.cantina.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cantina.R;
import com.example.cantina.model.Venda;
import com.example.cantina.repository.VendaRepository;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class VendaAdapter extends RecyclerView.Adapter<VendaAdapter.VendaViewHolder> {

    private final List<Venda> vendas;
    private final Context context;
    private final VendaRepository vendaRepository;
    private final NumberFormat formatReal = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public VendaAdapter(Context context, List<Venda> vendas) {
        this.context = context;
        this.vendas = vendas;
        this.vendaRepository = new VendaRepository(context);
    }

    @NonNull
    @Override
    public VendaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_venda, parent, false);
        return new VendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendaViewHolder holder, int position) {
        Venda venda = vendas.get(position);

        String nomeCliente = venda.getAluno() != null ?
                venda.getAluno().getNome() :
                venda.getNomeAlunoNaoCadastrado();

        holder.tvCliente.setText(nomeCliente);
        holder.tvData.setText(venda.getData());
        holder.tvTotal.setText(formatReal.format(venda.getValorTotal()));

        holder.btnExcluir.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Excluir Venda")
                    .setMessage("Tem certeza que deseja excluir?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        vendaRepository.deletar(venda.getId());
                        vendas.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        holder.btnDetalhes.setOnClickListener(v -> {
            // 🔹 futura Activity de detalhes
        });
    }

    @Override
    public int getItemCount() {
        return vendas.size();
    }

    public static class VendaViewHolder extends RecyclerView.ViewHolder {

        TextView tvCliente, tvData, tvTotal;
        Button btnDetalhes, btnExcluir;

        public VendaViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCliente = itemView.findViewById(R.id.tvClienteVenda);
            tvData = itemView.findViewById(R.id.tvDataVenda);
            tvTotal = itemView.findViewById(R.id.tvTotalVenda);
            btnDetalhes = itemView.findViewById(R.id.btnDetalhesVenda);
            btnExcluir = itemView.findViewById(R.id.btnExcluirVenda);
        }
    }
}
