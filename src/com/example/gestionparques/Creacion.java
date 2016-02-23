package com.example.gestionparques;

///import java.io.IOException;
//import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class Creacion extends Activity implements OnClickListener{
	private EditText t1;
	private EditText t2;
	private EditText t3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creacion);
        t1 = (EditText)findViewById(R.id.editNom);
        t1.setAlpha((float)0.8);
    	t2 = (EditText)findViewById(R.id.editRow);
    	t2.setAlpha((float)0.8);
    	t3 = (EditText)findViewById(R.id.t3);
    	t3.setAlpha((float)0.8);
	}
	
	public void crear(View v) {
		if ((t1.getText().toString().equals(""))) {
			Toast.makeText(this, "Introduzca un nombre válido", Toast.LENGTH_SHORT).show();
		}	
		else if (t2.getText().toString().equals("") || t2.getText().toString().equals("0")) {	
			Toast.makeText(this, "número de filas incorrecto", Toast.LENGTH_SHORT).show();
			Toast.makeText(this, "0 no es válido", Toast.LENGTH_SHORT).show();
		}
		else if (t3.getText().toString().equals("") || t3.getText().toString().equals("0"))	{
			Toast.makeText(this, "número de columnas incorrecto", Toast.LENGTH_SHORT).show();
			Toast.makeText(this, "0 no es válido", Toast.LENGTH_SHORT).show();
		}
		else {
			SQLiteDatabase myDB = null;
			myDB = (this).openOrCreateDatabase("BDParques2", 1, null);
			myDB.execSQL("CREATE TABLE IF NOT EXISTS " + "parques" + " (" + "nombre" + " TEXT NOT NULL PRIMARY KEY, " + "filas" + " INTEGER NOT NULL, " + "columnas" + " INTEGER NOT NULL, " + "limpieza" + " TEXT);");
			String nom = t1.getText().toString();
			String f = t2.getText().toString();
			String c = t3.getText().toString();
			String l = new String();
			int n = Integer.parseInt(f);
			int m = Integer.parseInt(c);
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) {
					l += '0';
				}
			}
			ContentValues registro = new ContentValues();
	        registro.put("nombre", nom);
	        registro.put("filas", f);
	        registro.put("columnas", c);
	        registro.put("limpieza", l);
	        myDB.insert("parques", null, registro);
	        
	        myDB.close();
	        t1.setText("");
	        t2.setText("");
	        t3.setText("");
	        Toast.makeText(this, "Se cargaron los datos del parque", Toast.LENGTH_SHORT).show();
	        Intent editar = new Intent(Creacion.this, EditParque.class);
	        editar.putExtra("nombre", nom);
	    	editar.putExtra("row", f);
	    	editar.putExtra("col", c);
	    	editar.putExtra("limp", l);
	        startActivity(editar);
	        finish();
		}
	}
	
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
	
	public void finalizar(View v) {
		Intent main = new Intent(Creacion.this, MainActivity.class);
        startActivity(main);
		finish();
	}
	
}
