package com.example.cantina.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cantina.db";
    private static final int DATABASE_VERSION = 1;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Aluno (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT, " +
                "responsavel TEXT, " +
                "telefone TEXT, " +
                "credito REAL DEFAULT 0)");

        db.execSQL("CREATE TABLE Produto (" +
                "codigo INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "descricao TEXT, " +
                "preco REAL)");

        db.execSQL("CREATE TABLE Venda (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_aluno INTEGER, " +
                "nome_aluno_nao_cadastrado TEXT, " +
                "data TEXT NOT NULL, " +
                "metodo_pagamento TEXT NOT NULL, " +
                "valor_total REAL NOT NULL DEFAULT 0, " +
                "FOREIGN KEY(id_aluno) REFERENCES Aluno(id) ON DELETE SET NULL ON UPDATE CASCADE)");


        db.execSQL("CREATE TABLE ItemVenda (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_venda INTEGER, " +
                "id_produto INTEGER, " +
                "quantidade INTEGER, " +
                "FOREIGN KEY(id_venda) REFERENCES Venda(id), " +
                "FOREIGN KEY(id_produto) REFERENCES Produto(codigo))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Aluno");
        db.execSQL("DROP TABLE IF EXISTS Produto");
        db.execSQL("DROP TABLE IF EXISTS Venda");
        db.execSQL("DROP TABLE IF EXISTS ItemVenda");
        onCreate(db);
    }
}
