package com.example.cantina.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cantina.database.DatabaseHelper;
import com.example.cantina.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    private final DatabaseHelper dbHelper;

    public ProdutoRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long inserir(Produto produto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = -1;

        try {
            ContentValues values = new ContentValues();
            values.put("descricao", produto.getDescricao());
            values.put("preco", produto.getPreco());

            id = db.insert("Produto", null, values);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return id;
    }

    public Produto buscarPorId(int codigo) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Produto produto = null;

        try {
            Cursor cursor = db.query(
                    "Produto",
                    new String[]{"codigo", "descricao", "preco"},
                    "codigo = ?",
                    new String[]{String.valueOf(codigo)},
                    null, null, null
            );

            if (cursor.moveToFirst()) {
                produto = new Produto(
                        cursor.getInt(cursor.getColumnIndexOrThrow("codigo")),
                        cursor.getString(cursor.getColumnIndexOrThrow("descricao")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("preco"))
                );
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return produto;
    }

    public List<Produto> listarTodos() {
        List<Produto> produtos = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM Produto", null);

            if (cursor.moveToFirst()) {
                do {
                    Produto produto = new Produto(
                            cursor.getInt(cursor.getColumnIndexOrThrow("codigo")),
                            cursor.getString(cursor.getColumnIndexOrThrow("descricao")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("preco"))
                    );
                    produtos.add(produto);
                } while (cursor.moveToNext());
            }

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return produtos;
    }

    public int atualizar(Produto produto) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int linhasAfetadas = 0;

        try {
            ContentValues values = new ContentValues();
            values.put("descricao", produto.getDescricao());
            values.put("preco", produto.getPreco());

            linhasAfetadas = db.update(
                    "Produto",
                    values,
                    "codigo = ?",
                    new String[]{String.valueOf(produto.getCodigo())}
            );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return linhasAfetadas;
    }

    public void deletar(int codigo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.delete("Produto", "codigo = ?", new String[]{String.valueOf(codigo)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }
}
