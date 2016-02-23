package com.example.gestionparques;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
//import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class MainActivity extends Activity implements OnClickListener{
	Button botoncrear;
    Button botonedit;
    Button botonexit;
    Button botonmodif;
    Button del;
    Spinner sp;
    String nom = null;
    TextView t;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = (TextView)findViewById(R.id.textView2);
        botoncrear = (Button) findViewById(R.id.crear);
        botonedit = (Button) findViewById(R.id.buttonedit);
    	botonedit.setOnClickListener(new View.OnClickListener() {
    	    @Override
    	    public void onClick(View v) {
    	        editar(v, nom);
    	    }
    	});
    	botonmodif = (Button) findViewById(R.id.button1);
    	botonmodif.setOnClickListener(new View.OnClickListener() {
    	    @Override
    	    public void onClick(View v) {
    	        modificar(v, nom);
    	    }
    	});
    	
    	del = (Button) findViewById(R.id.eliminar);
    	del.setOnClickListener(new View.OnClickListener() {
    	    @Override
    	    public void onClick(View v) {
    	        eliminar(v, nom);
    	    }
    	});

        sp = (Spinner) findViewById(R.id.spinner1);
        sp.setOnItemSelectedListener(seleccionado);
        loadSpinner();
    }
    
    private OnItemSelectedListener seleccionado = new Spinner.OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			//Toast.makeText(parent.getContext(),"Item selected : "+ parent.getItemAtPosition(pos).toString(),Toast.LENGTH_SHORT).show();
			nom = (String) sp.getItemAtPosition(pos);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	};
    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}
	
	public void lanzar(View v) {
		Intent crear = new Intent(MainActivity.this, Creacion.class);
		startActivity(crear);
		finish();
	}
	
	public void editar(View v, String text) {
		if (text == null) {
			Toast.makeText(this.getBaseContext(),"Selecciona un parque",Toast.LENGTH_SHORT).show();
		}
		else {
			String f = new String();
			String c = new String();
			String l = new String();
			SQLiteDatabase myDB = null;
			myDB = (this).openOrCreateDatabase("BDParques2", 1, null);
			String[] columns = {"filas", "columnas", "limpieza"};
			String[] param = {text};
			Cursor cu = myDB.query("parques", columns, "nombre = ?", param, null, null, null);
			if (cu.moveToFirst()) {
	            f = cu.getString(0);
	            c = cu.getString(1);
	            l = cu.getString(2);
	        } 
			else Toast.makeText(this, "Error de lectura de la BD", Toast.LENGTH_SHORT).show();
			
			/*String[] colum = {"limpieza"};
			Cursor cur = myDB.query("parques", colum, "nombre = ?", param, null, null, null);
			if (cur.moveToFirst()) l = cur.getString(0);
			Toast.makeText(this.getBaseContext(),l + " estados",Toast.LENGTH_SHORT).show();
			*/
			if(myDB!=null) myDB.close();
			
			Intent intent = new Intent(MainActivity.this, EditParque.class);
	        intent.putExtra("nombre", text);
	    	intent.putExtra("row", f);
	    	intent.putExtra("col", c);
	    	intent.putExtra("limp", l);
	        startActivity(intent);
	        finish();
		}
	}
	
	public void modificar(View v, String text) {
		if (text == null) {
			Toast.makeText(this.getBaseContext(),"Selecciona un parque",Toast.LENGTH_SHORT).show();
		}
		else {
			String f = new String();
			String c = new String();
			String l = new String();
			SQLiteDatabase myDB = null;
			myDB = (this).openOrCreateDatabase("BDParques2", 1, null);
			String[] columns = {"filas", "columnas"};
			String[] param = {text};
			Cursor cu = myDB.query("parques", columns, "nombre = ?", param, null, null, null);
			if (cu.moveToFirst()) {
	            f = cu.getString(0);
	            c = cu.getString(1);
	        } 
			else Toast.makeText(this, "Error de lectura de la BD", Toast.LENGTH_SHORT).show();
			
			if(myDB!=null) myDB.close();
			
			for (int i = 0; i < Integer.parseInt(f); i++) {
				for (int j = 0; j < Integer.parseInt(c); j++) l += '0';
			}
			
			Intent intent = new Intent(MainActivity.this, EditParque.class);
	        intent.putExtra("nombre", text);
	    	intent.putExtra("row", f);
	    	intent.putExtra("col", c);
	    	intent.putExtra("limp", l);
	        startActivity(intent);
	        finish();
		}
	}
	
	public void finalizar(View v) {
		finish();
	}
		
	public void loadSpinner() {
		SQLiteDatabase myDB = null;
		myDB = (this).openOrCreateDatabase("BDParques2", 1, null);
		myDB.execSQL("CREATE TABLE IF NOT EXISTS " + "parques" + " (" + "nombre" + " TEXT NOT NULL PRIMARY KEY, " + "filas" + " INTEGER NOT NULL, " + "columnas" + " INTEGER NOT NULL, " + "limpieza" + " TEXT NOT NULL);");
		String[] columns = {"nombre"};
		Cursor c = myDB.query("parques", columns, null, null, null, null, null);
		List<String> dynamicList = new ArrayList<String>();
		if (c.moveToFirst()) {
			dynamicList.add(c.getString(0));
			//for (int i = 0; i < c.getCount(); i++) {
			while (c.moveToNext()) {
				dynamicList.add(c.getString(0));
			}
		}
		else {
            Toast.makeText(this, "No hay ningun parque guardado",Toast.LENGTH_SHORT).show();
		}
		if(myDB!=null) myDB.close();

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
		android.R.layout.simple_spinner_item, dynamicList);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(dataAdapter);
	}
	
	public void eliminar(View v, String text) {
		if (text == null) {
			Toast.makeText(this.getBaseContext(),"Selecciona un parque",Toast.LENGTH_SHORT).show();
		}
		else {
			SQLiteDatabase myDB = null;
			myDB = (this).openOrCreateDatabase("BDParques2", 1, null);      	
	        myDB.execSQL("DELETE FROM parques WHERE nombre = '" + nom + "'");
	     	    		
			if(myDB!=null) myDB.close();
			Toast.makeText(this, "parque eliminado", Toast.LENGTH_SHORT).show();
			
			loadSpinner();
		}
	}
}
