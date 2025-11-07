package com.example.cantina.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.cantina.database.DatabaseHelper;
import com.example.cantina.model.Aluno;

import java.util.ArrayList;
import java.util.List;

public class AlunoRepository {

    private final DatabaseHelper dbHelper;

    public AlunoRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public long inserir(Aluno aluno) {
        SQLiteDatabase db = null;
        long id = -1;

        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nome", aluno.getNome());
            values.put("responsavel", aluno.getResponsavel());
            values.put("telefone", aluno.getTelefone());
            values.put("credito", aluno.getCredito());

            id = db.insertOrThrow("Aluno", null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return id;
    }

    public Aluno buscarPorId(long id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Aluno aluno = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query(
                    "Aluno",
                    new String[]{"id", "nome", "responsavel", "telefone", "credito"},
                    "id = ?",
                    new String[]{String.valueOf(id)},
                    null, null, null
            );

            if (cursor != null && cursor.moveToFirst()) {
                aluno = new Aluno(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                        cursor.getString(cursor.getColumnIndexOrThrow("responsavel")),
                        cursor.getString(cursor.getColumnIndexOrThrow("telefone")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("credito"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }

        return aluno;
    }

    public List<Aluno> listarTodos() {
        List<Aluno> alunos = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM Aluno ORDER BY nome ASC", null);

            if (cursor.moveToFirst()) {
                do {
                    Aluno aluno = new Aluno(
                            cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                            cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                            cursor.getString(cursor.getColumnIndexOrThrow("responsavel")),
                            cursor.getString(cursor.getColumnIndexOrThrow("telefone")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("credito"))
                    );
                    alunos.add(aluno);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }

        return alunos;
    }

    public List<Aluno> buscarPorNome(String nome) {
        List<Aluno> alunos = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query(
                    "Aluno",
                    new String[]{"id", "nome", "responsavel", "telefone", "credito"},
                    "nome LIKE ?",
                    new String[]{"%" + nome + "%"},
                    null, null,
                    "nome ASC"
            );

            if (cursor.moveToFirst()) {
                do {
                    Aluno aluno = new Aluno(
                            cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                            cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                            cursor.getString(cursor.getColumnIndexOrThrow("responsavel")),
                            cursor.getString(cursor.getColumnIndexOrThrow("telefone")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("credito"))
                    );
                    alunos.add(aluno);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }

        return alunos;
    }

    public int atualizar(Aluno aluno) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("responsavel", aluno.getResponsavel());
        values.put("telefone", aluno.getTelefone());
        values.put("credito", aluno.getCredito());

        int linhasAfetadas = db.update(
                "Aluno",
                values,
                "id = ?",
                new String[]{String.valueOf(aluno.getId())}
        );

        db.close();
        return linhasAfetadas;
    }


    public boolean deletar(long id) {
        SQLiteDatabase db = null;
        boolean sucesso = false;

        try {
            db = dbHelper.getWritableDatabase();
            int linhas = db.delete("Aluno", "id = ?", new String[]{String.valueOf(id)});
            sucesso = (linhas > 0);
        } catch (SQLException e) {
            e.printStackTrace();
            sucesso = false;
        } finally {
            if (db != null && db.isOpen()) db.close();
        }

        return sucesso;
    }

    public boolean atualizarCredito(long idAluno, double novoCredito) {
        SQLiteDatabase db = null;
        boolean sucesso = false;

        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("credito", novoCredito);

            int linhas = db.update("Aluno", values, "id = ?", new String[]{String.valueOf(idAluno)});
            sucesso = (linhas > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) db.close();
        }

        return sucesso;
    }
}
