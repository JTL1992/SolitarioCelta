package es.upm.miw.SolitarioCelta.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;

import java.util.Timer;
import java.util.TimerTask;

import es.upm.miw.SolitarioCelta.AlertView.ReiniciarDialog;
import es.upm.miw.SolitarioCelta.AlertView.RestoreDialog;
import es.upm.miw.SolitarioCelta.AlertView.RestoreNameListDialog;
import es.upm.miw.SolitarioCelta.AlertView.SceltaSaveDialog;
import es.upm.miw.SolitarioCelta.AlertView.SetNameDialog;
import es.upm.miw.SolitarioCelta.Modelos.GameTimer;
import es.upm.miw.SolitarioCelta.Modelos.JuegoCelta;
import es.upm.miw.SolitarioCelta.Modelos.Result;
import es.upm.miw.SolitarioCelta.R;
import es.upm.miw.SolitarioCelta.utils.FileUtils;

public class MainActivity extends Activity {

    private static final int TIME_TASK = 1;
    private static final int FINCHA_TASK = 2;
    private Handler handler;
    private TimerTask timerTask;
    private Timer timer;
    public GameTimer gameTimer;
    public JuegoCelta juego;
    private TextView tvTime,tvFicha;

    public FileUtils fileUtils;

    private final String GRID_KEY = "GRID_KEY";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        juego = new JuegoCelta();
        fileUtils = new FileUtils(this);
        gameTimer = new GameTimer();
        mostrarTablero();
        inicialGameSettring();
    }

    /**
     * Se ejecuta al pulsar una ficha
     * Las coordenadas (i, j) se obtienen a partir del nombre, ya que el botón
     * tiene un identificador en formato pXY, donde X es la fila e Y la columna
     * @param v Vista de la ficha pulsada
     */
    public void fichaPulsada(View v) {
        String resourceName = getResources().getResourceEntryName(v.getId());
        int i = resourceName.charAt(1) - '0';   // fila
        int j = resourceName.charAt(2) - '0';   // columna

        juego.jugar(i, j);

        mostrarTablero();
        handler.sendEmptyMessage(FINCHA_TASK);
        if (juego.juegoTerminado()) {
            // TODO guardar puntuación
            timeTaskStop();
            saveResult();
        }
    }

    private void saveResult() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String name = sharedPref.getString("nombreJugador", "");
        if ("".equals(name)){
            new SetNameDialog().show(getFragmentManager(), "SETNAME");
        }else{
            saveToFile(name);
        }

    }

    public void saveToFile(String name) {
        Result result = new Result(name, juego.numeroFichas(), getTimerTime());
        JSONArray results = fileUtils.readFile2JsonArray("SolitarioResults");
        if (null == results){
            results = new JSONArray();
            results.put(result.getResultJSONObject());
        }
        else {
            results = result.getOrderedResults(results);
        }
        Log.v("results",results.toString());
        new ReiniciarDialog().show(getFragmentManager(), "ALERT DIALOG");
        fileUtils.saveJsonArray2File(results,"SolitarioResults");
    }

    /**
     * Visualiza el tablero
     */
    public void mostrarTablero() {
        RadioButton button;
        String strRId;
        String prefijoIdentificador = getPackageName() + ":id/p"; // formato: package:type/entry
        int idBoton;

        for (int i = 0; i < JuegoCelta.TAMANIO; i++)
            for (int j = 0; j < JuegoCelta.TAMANIO; j++) {
                strRId = prefijoIdentificador + Integer.toString(i) + Integer.toString(j);
                idBoton = getResources().getIdentifier(strRId, null, null);
                if (idBoton != 0) { // existe el recurso identificador del botón
                    button = (RadioButton) findViewById(idBoton);
                    button.setChecked(juego.obtenerFicha(i, j) == JuegoCelta.FICHA);
                }
            }
    }

    public void inicialGameSettring(){
        tvTime = (TextView) findViewById(R.id.tiktak);
        tvFicha = (TextView) findViewById(R.id.ficha);
        tvTime.setText("00:00.00");
        tvFicha.setText("32");
        gameTimer.gameTimerStart();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case TIME_TASK:
                        tvTime.setText(gameTimer.getTimerString());
                        break;
                    case FINCHA_TASK:
                        tvFicha.setText(juego.numeroFichas()+"");
                        break;
                }
            }
        };
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(TIME_TASK);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,10);
    }

    public void gameRestart(String time, int ficha, String seria){
        tvTime.setText(time);
        tvFicha.setText(""+ficha);
        juego.deserializaTablero(seria);
        mostrarTablero();
        gameTimer.gameTimerRestartFromTimer(time);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(TIME_TASK);
            }
        };
        timer.schedule(timerTask, 0, 10);
    }

    /**
     * Guarda el estado del tablero (serializado)
     * @param outState Bundle para almacenar el estado del juego
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(GRID_KEY, juego.serializaTablero());
        super.onSaveInstanceState(outState);
    }

    /**
     * Recupera el estado del juego
     * @param savedInstanceState Bundle con el estado del juego almacenado
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String grid = savedInstanceState.getString(GRID_KEY);
        juego.deserializaTablero(grid);
        mostrarTablero();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcAjustes:
                startActivity(new Intent(this, PrefsActivity.class));
                return true;
            case R.id.opcAcercaDe:
                startActivity(new Intent(this, AcercaDe.class));
                return true;
            // TODO!!! resto opciones
            case R.id.opcReiniciarPartida:
                timeTaskStop();
                new ReiniciarDialog().show(getFragmentManager(),"REINICIAR");
                return true;
            case R.id.opcGuardarPartida:
                timeTaskStop();
                new SceltaSaveDialog().show(getFragmentManager(), "NAME");
                return true;
            case R.id.opcRecuperarPartida:
                timeTaskStop();
                restoreGame(this, juego);
                return true;
            case R.id.opcMejoresResultados:
                startActivity(new Intent(this, ResultsListActivity.class));
                return true;
            case R.id.opcEliminarPartida:
                fileUtils.deleteFile("SolitarioCelta");
                Toast.makeText(this,"delete successfully",Toast.LENGTH_SHORT).show();
                return true;
            default:
                Toast.makeText(
                        this,
                        getString(R.string.txtSinImplementar),
                        Toast.LENGTH_SHORT
                ).show();
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        timeTaskStop();
    }

    private  void restoreGame(MainActivity main, JuegoCelta sCelta) {
        String data = fileUtils.readFile2String("SolitarioCelta");
        if ("".equals(data)){
            Toast.makeText(main, "no hay partida guardada!", Toast.LENGTH_SHORT).show();
            timeTaskRestart();
            return;
        }
        String nowData = sCelta.serializaTablero();
        Log.v("now data", nowData);
        if (juego.numeroFichas() != 32){
            new RestoreDialog().show(getFragmentManager(),"restore");
        } else {
            showGameListDialog();
        }
    }

    public void showGameListDialog() {
        new RestoreNameListDialog().show(getFragmentManager(), "gamelist");
    }

    public void timeTaskRestart(){
        gameTimer.gameTimerRestart();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(TIME_TASK);
            }
        };
        timer.schedule(timerTask, 0, 10);
    }

    public void timeTaskStop(){
        gameTimer.gameTimerStop();
        timerTask.cancel();
        timer.purge();
    }

    public String getTimerTime(){
       return tvTime.getText().toString();
    }
}

