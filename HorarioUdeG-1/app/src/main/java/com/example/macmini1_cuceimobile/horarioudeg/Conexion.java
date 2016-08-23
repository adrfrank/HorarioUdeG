package com.example.macmini1_cuceimobile.horarioudeg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class Conexion extends SQLiteOpenHelper {
	final static String NAME_DATABASE = "horario";
	final static int VERSION_DATABASE = 1;
	final static String query = "CREATE TABLE alumno (_id INTEGER PRIMARY KEY AUTOINCREMENT, codigo TEXT NOT NULL,nombre TEXT,centro TEXT,carrera TEXT, pass TEXT NOT NULL);";
	final static String query2 = "CREATE TABLE materias (_id INTEGER PRIMARY KEY AUTOINCREMENT, NRC TEXT , clave TEXT,materia TEXT,seccion TEXT,creditos TEXT," +
			"horario TEXT,lunes TEXT,martes TEXT,miercoles TEXT,jueves TEXT, viernes TEXT, sabado TEXT,edificio TEXT,aula TEXT,profesor TEXT,fecha_inicio TEXT," +
			"fecha_fin TEXT);";

	public Conexion (Context context, String name, CursorFactory factory,int version) {
		
		super(context, NAME_DATABASE, factory, VERSION_DATABASE);
		//Log.d ("Logcat", "Creado");
		//super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(query);
		db.execSQL(query2);

		Log.d("Logcat", "animales creados");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
		    switch (oldVersion) {
		        case 1:
		            //executeSQLScript(db, "update_v2.sql");
		        case 2:		        	
		            //executeSQLScript(db, "update_v3.sql");
		    }
		}
	}
}
