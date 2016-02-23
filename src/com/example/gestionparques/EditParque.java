package com.example.gestionparques;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

public class EditParque extends Activity {
	ArrayList<Button> botones = new ArrayList<Button> (0);
	ArrayList<Boolean> estados = new ArrayList<Boolean>(0);
	String nom, limpieza;
	int n, m;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editparque);
		GridLayout grid1 = (GridLayout)findViewById(R.id.grid1);
		
		Bundle bundle = getIntent().getExtras();
		nom = bundle.getString("nombre");
		
		n = Integer.parseInt(bundle.getString("row"));
		m =  Integer.parseInt(bundle.getString("col"));
		limpieza = bundle.getString("limp");
		
		for (int p = 0; p < limpieza.length(); p++) {
			if (limpieza.charAt(p) == '0') estados.add(false);
			else estados.add(true);
		}

		//++n;
		Point size = new Point();
		WindowManager win = getWindowManager();
		win.getDefaultDisplay().getSize(size);
		int w = size.x/m;
		int h = (size.y-50)/n;//n+1 
		
		grid1.setColumnCount(m);
		grid1.setRowCount(n);
		grid1.setBackground(getResources().getDrawable(R.drawable.parque_planta));
		
		int cont = 0;
		for (int i = 0; i < n; i++) {
			//if (i < n-1) {
				for (int j = 0; j < m; j++) {
					Button b = new Button(getBaseContext());
					if (estados.get(cont) == false) b.setBackground(getResources().getDrawable(R.drawable.cuad_red));
					else b.setBackground(getResources().getDrawable(R.drawable.cuad_green));
					b.setAlpha((float)0.6);
					b.setTag(cont);
					++cont;
					b.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							cambia_estado(v);
						}
					});
					grid1.addView(b, w, h);
				}
			}
			/*else {
				Button a = new Button(getBaseContext());
				Button c = new Button(getBaseContext());
				a.setBackground(getResources().getDrawable(R.drawable.cuad_black));
				a.setTextSize((float)40);
				a.setText("Salir");
				a.setTextColor(Color.rgb(255,200,0));
				a.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent main = new Intent(EditParque.this, MainActivity.class);
						startActivity(main);
						finish();
					}
				});
				grid1.addView(a, w, h);
				for(int j = 0; j < m-2; j++) {
					Button b = new Button(getBaseContext());
					grid1.addView(b, w, h);
				}
				c.setBackground(getResources().getDrawable(R.drawable.cuad_blue));
				c.setText("Guardar y Salir");
				c.setTextColor(Color.rgb(255,200,0));
				c.setTextSize((float)40);
				c.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						try {guardar();
						
						}catch (Exception e){
							Log.e("GUARDAR", e.toString());
						}
						Intent main = new Intent(EditParque.this, MainActivity.class);
						startActivity(main);
						finish();
					}
				});
				grid1.addView(c, w, h);
			}
		}*/
	}
		
	public void cambia_estado(View v) {
        Button but = (Button)v;
        int i = (Integer) but.getTag();
        if (estados.get(i) == false) {
        	estados.set(i, true);
        	but.setBackground(getResources().getDrawable(R.drawable.cuad_green));
        }
        else {
        	estados.set(i, false);
        	but.setBackground(getResources().getDrawable(R.drawable.cuad_red));
        }
        
    }
	
	public void guardar() {
		String limp = new String();
		
		for (int k = 0; k < estados.size(); k++) {
			boolean b = estados.get(k);
			if (!b) limp += '0';
			else limp += '1';
		}
		SQLiteDatabase myDB = null;
		myDB = (this).openOrCreateDatabase("BDParques2", 1, null);     	
        myDB.execSQL("UPDATE parques SET limpieza = '" +limp+ "' WHERE nombre = '" + nom + "'");
        if(myDB!=null) {
			myDB.close();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0, 0, 0, "Salir"); 
		menu.add(0, 1, 0, "Guardar");
		menu.add(0, 2, 0, "Guardar y Salir");
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) { 
			case 0: 
				salir(); 
				return true; 
			case 1: 
				guardar(); 
				return true; 
			case 2:
				guardar();
				salir();
				return true;
			} 
		
		return false;
	}
	
	public void salir() {
		Intent main = new Intent(EditParque.this, MainActivity.class);
		startActivity(main);
		finish();
	}
	
}

