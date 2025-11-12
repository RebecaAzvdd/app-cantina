package com.example.cantina;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cantina.adapter.VendaAdapter;
import com.example.cantina.model.Venda;
import com.example.cantina.repository.VendaRepository;

import java.util.Calendar;
import java.util.List;

public class HistoricoVendasActivity extends AppCompatActivity {

    private RecyclerView rvVendas;
    private Button btnDataInicio, btnDataFim, btnLimparFiltro;
    private VendaRepository vendaRepository;

    private String dataInicio = null;
    private String dataFim = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_vendas);

        rvVendas = findViewById(R.id.rvVendas);
        btnDataInicio = findViewById(R.id.btnDataInicio);
        btnDataFim = findViewById(R.id.btnDataFim);
        btnLimparFiltro = findViewById(R.id.btnLimparFiltro);

        vendaRepository = new VendaRepository(this);

        btnDataInicio.setOnClickListener(v -> escolherData(true));
        btnDataFim.setOnClickListener(v -> escolherData(false));
        btnLimparFiltro.setOnClickListener(v -> limparFiltro());

        carregarVendas();

        ImageView imgLogo = findViewById(R.id.imgLogo);
        imgLogo.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void escolherData(boolean inicio) {
        Calendar calendario = Calendar.getInstance();

        new DatePickerDialog(this,
                (view, ano, mes, dia) -> {
                    String data = ano + "-" + (mes + 1) + "-" + dia;

                    if (inicio) {
                        dataInicio = data;
                        btnDataInicio.setText(data);
                    } else {
                        dataFim = data;
                        btnDataFim.setText(data);
                    }

                    if (dataInicio != null && dataFim != null) {
                        filtrar();
                    }
                },
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void filtrar() {
        List<Venda> vendas = vendaRepository.filtrarPorData(dataInicio, dataFim);
        rvVendas.setAdapter(new VendaAdapter(this, vendas));
    }

    private void limparFiltro() {
        dataInicio = null;
        dataFim = null;

        btnDataInicio.setText("Data Inicial");
        btnDataFim.setText("Data Final");

        carregarVendas();
    }

    private void carregarVendas() {
        List<Venda> vendas = vendaRepository.listarTodas();
        rvVendas.setLayoutManager(new LinearLayoutManager(this));
        rvVendas.setAdapter(new VendaAdapter(this, vendas));
    }
}
