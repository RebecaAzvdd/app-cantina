package com.example.cantina.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cantina.database.DatabaseHelper;
import com.example.cantina.model.ItemVenda;
import com.example.cantina.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ItemVendaRepository {

    private final DatabaseHelper dbHelper;

    public ItemVendaRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long inserirItemVenda(int idVenda, ItemVenda itemVenda) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id_venda", idVenda);
        values.put("id_produto", itemVenda.getProduto().getCodigo());
        values.put("quantidade", itemVenda.getQuantidade());

        long id = db.insert("ItemVenda", null, values);
        db.close();
        return id;
    }

    public List<ItemVenda> buscarItensPorVenda(int idVenda) {
        List<ItemVenda> itens = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT iv.id, iv.id_produto, iv.quantidade, " +
                "p.descricao, p.preco " +
                "FROM ItemVenda iv " +
                "INNER JOIN Produto p ON iv.id_produto = p.codigo " +
                "WHERE iv.id_venda = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idVenda)});

        if (cursor.moveToFirst()) {
            do {
                Produto produto = new Produto(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id_produto")),
                        cursor.getString(cursor.getColumnIndexOrThrow("descricao")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("preco"))
                );

                ItemVenda item = new ItemVenda(
                        produto,
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantidade"))
                );

                itens.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return itens;
    }

    public void excluirItensPorVenda(int idVenda) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("ItemVenda", "id_venda = ?", new String[]{String.valueOf(idVenda)});
        db.close();
    }

    public int atualizarQuantidade(int idVenda, int idProduto, int novaQuantidade) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("quantidade", novaQuantidade);

        int linhasAfetadas = db.update(
                "ItemVenda",
                values,
                "id_venda = ? AND id_produto = ?",
                new String[]{String.valueOf(idVenda), String.valueOf(idProduto)}
        );

        db.close();
        return linhasAfetadas;
    }

    public double calcularTotalVenda(int idVenda) {
        double total = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT iv.quantidade, p.preco " +
                "FROM ItemVenda iv " +
                "INNER JOIN Produto p ON iv.id_produto = p.codigo " +
                "WHERE iv.id_venda = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idVenda)});

        if (cursor.moveToFirst()) {
            do {
                int quantidade = cursor.getInt(cursor.getColumnIndexOrThrow("quantidade"));
                double preco = cursor.getDouble(cursor.getColumnIndexOrThrow("preco"));
                total += quantidade * preco;
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return total;
    }
}
