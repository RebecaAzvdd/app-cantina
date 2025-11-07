package com.example.cantina.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.cantina.database.DatabaseHelper;
import com.example.cantina.model.Venda;

import java.util.ArrayList;
import java.util.List;

public class VendaRepository {

    private final DatabaseHelper dbHelper;

    public VendaRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public long inserir(Venda venda) {
        SQLiteDatabase db = null;
        long id = -1;

        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            if (venda.getAluno() != null) {
                values.put("id_aluno", venda.getAluno().getId());
                values.putNull("nome_aluno_nao_cadastrado");
            } else {
                values.putNull("id_aluno");
                values.put("nome_aluno_nao_cadastrado", venda.getNomeAlunoNaoCadastrado());
            }

            values.put("data", venda.getData());
            values.put("metodo_pagamento", venda.getMetodoPagamento());
            values.put("valor_total", venda.getValorTotal());

            id = db.insertOrThrow("Venda", null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }

        return id;
    }

    public Venda buscarPorId(long id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Venda venda = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query(
                    "Venda",
                    new String[]{"id", "id_aluno", "nome_aluno_nao_cadastrado", "data", "metodo_pagamento", "valor_total"},
                    "id = ?",
                    new String[]{String.valueOf(id)},
                    null, null, null
            );

            if (cursor.moveToFirst()) {
                venda = new Venda();
                venda.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                venda.setData(cursor.getString(cursor.getColumnIndexOrThrow("data")));
                venda.setMetodoPagamento(cursor.getString(cursor.getColumnIndexOrThrow("metodo_pagamento")));
                venda.setValorTotal(cursor.getDouble(cursor.getColumnIndexOrThrow("valor_total")));

                int alunoId = cursor.getInt(cursor.getColumnIndexOrThrow("id_aluno"));
                String nomeNaoCadastrado = cursor.getString(cursor.getColumnIndexOrThrow("nome_aluno_nao_cadastrado"));

                if (alunoId > 0) {
                    venda.setAlunoId(alunoId);
                } else {
                    venda.setNomeAlunoNaoCadastrado(nomeNaoCadastrado);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        return venda;
    }

    public List<Venda> listarTodas() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<Venda> vendas = new ArrayList<>();

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM Venda ORDER BY data DESC", null);

            if (cursor.moveToFirst()) {
                do {
                    Venda venda = new Venda();
                    venda.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    venda.setData(cursor.getString(cursor.getColumnIndexOrThrow("data")));
                    venda.setMetodoPagamento(cursor.getString(cursor.getColumnIndexOrThrow("metodo_pagamento")));
                    venda.setValorTotal(cursor.getDouble(cursor.getColumnIndexOrThrow("valor_total")));

                    int alunoId = cursor.getInt(cursor.getColumnIndexOrThrow("id_aluno"));
                    String nomeNaoCadastrado = cursor.getString(cursor.getColumnIndexOrThrow("nome_aluno_nao_cadastrado"));

                    if (alunoId > 0) {
                        venda.setAlunoId(alunoId);
                    } else {
                        venda.setNomeAlunoNaoCadastrado(nomeNaoCadastrado);
                    }

                    vendas.add(venda);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        return vendas;
    }

    public int atualizar(Venda venda) {
        SQLiteDatabase db = null;
        int linhas = 0;

        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            if (venda.getAluno() != null) {
                values.put("id_aluno", venda.getAluno().getId());
                values.putNull("nome_aluno_nao_cadastrado");
            } else {
                values.putNull("id_aluno");
                values.put("nome_aluno_nao_cadastrado", venda.getNomeAlunoNaoCadastrado());
            }

            values.put("data", venda.getData());
            values.put("metodo_pagamento", venda.getMetodoPagamento());
            values.put("valor_total", venda.getValorTotal());

            linhas = db.update("Venda", values, "id = ?", new String[]{String.valueOf(venda.getId())});

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }

        return linhas;
    }

    public void deletar(long id) {
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();
            db.delete("Venda", "id = ?", new String[]{String.valueOf(id)});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null) db.close();
        }
    }

    public List<Venda> filtrarPorData(String dataInicio, String dataFim) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<Venda> vendas = new ArrayList<>();

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(
                    "SELECT * FROM Venda WHERE date(data) BETWEEN date(?) AND date(?) ORDER BY data DESC",
                    new String[]{dataInicio, dataFim}
            );

            if (cursor.moveToFirst()) {
                do {
                    Venda venda = new Venda();
                    venda.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    venda.setData(cursor.getString(cursor.getColumnIndexOrThrow("data")));
                    venda.setMetodoPagamento(cursor.getString(cursor.getColumnIndexOrThrow("metodo_pagamento")));
                    venda.setValorTotal(cursor.getDouble(cursor.getColumnIndexOrThrow("valor_total")));

                    vendas.add(venda);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }

        return vendas;
    }

}
