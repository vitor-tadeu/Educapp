package com.example.educapp.view.menu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.educapp.R;
import com.example.educapp.dao.CriarEventoDAO;
import com.example.educapp.dao.TarefaNomeDAO;
import com.example.educapp.model.CriarEvento;
import com.example.educapp.model.CriarTarefa;
import com.example.educapp.model.TarefaNome;
import com.example.educapp.util.navigation_bar.BottomNavigationViewHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Assistente extends AppCompatActivity {
    private TextView txtMsgEntrada, txtMsgUsuario;
    private TextToSpeech myTTS;
    private SpeechRecognizer mySR;
    private Calendar dateTime = Calendar.getInstance();

    public static Integer flagEvento = 0;
    public static Integer flagTarefa = 0;
    public static String nomeEvento;
    public static String tipoEvento;
    public static String dataEvento;
    public static String horaEvento;
    public static String nomeTarefa;
    public static String itemTarefa;

    public static Integer idNomeTarefa;

    private TarefaNomeDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_assistente);
        chamaFuncao();
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        navView();
        iniciaVariavel();
        microfone();
        initializeTextToSpeech();
        informacao();
    }

    //Pegando as referências das Views
    private void iniciaVariavel() {
        txtMsgEntrada = findViewById(R.id.txtMsgEntrada);
        txtMsgUsuario = findViewById(R.id.txtMsgUsuario);
        dao = new TarefaNomeDAO(getApplicationContext());
    }

    //Seleção dos botões da barra de menu
    private void navView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        intent = new Intent(Assistente.this, Home.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                    case R.id.nav_criar_evento:
                        intent = new Intent(Assistente.this, Lembretes.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                    case R.id.nav_minha_conta:
                        intent = new Intent(Assistente.this, MinhaConta.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                }
                return false;
            }
        });
    }

    //Inicia o microfone
    private void microfone() {
        ImageButton btnMicrofone = findViewById(R.id.btnMicrofone);
        btnMicrofone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                mySR.startListening(intent);
            }
        });
        initializeSpeechRecognizer();
    }

    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            mySR = SpeechRecognizer.createSpeechRecognizer(this);
            mySR.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle bundle) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float v) {

                }

                @Override
                public void onBufferReceived(byte[] bytes) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int i) {

                }

                @Override
                public void onResults(Bundle bundle) {
                    List<String> results = bundle.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                    );
                    assert results != null;
                    processResult(results.get(0));
                    ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null) {
                        txtMsgEntrada.setText(matches.get(0));
                    }
                }

                @Override
                public void onPartialResults(Bundle bundle) {

                }

                @Override
                public void onEvent(int i, Bundle bundle) {

                }
            });
        }
    }

    private void processResult(String command) {
        command = command.toLowerCase();
        if (command.equals("salvar tarefa")){
            flagTarefa = 0;
        }
        switch (flagEvento){
            case 1:
                String nome = command.substring(0,1).toUpperCase().concat(command.substring(1));
                nomeEvento = nome;
                speak("Qual vai ser o tipo do evento?");
                txtMsgUsuario.setText("Qual vai ser o tipo do evento?");
                flagEvento = 2;
                delay();
                break;
            case 2:
                String tipo = command.substring(0,1).toUpperCase().concat(command.substring(1));
                tipoEvento = tipo;
                speak("O evento foi criado, gostaria de mais alguma coisa?");
                txtMsgUsuario.setText("O evento foi criado, gostaria de mais alguma coisa?");
                flagEvento = 3;
                delay2();
                break;
            default:
                break;
        }

        switch (flagTarefa){
            case 1:
                String nome = command.substring(0,1).toUpperCase().concat(command.substring(1));
                nomeTarefa = nome;
                TarefaNome tarefa = new TarefaNome();
                tarefa.setNomeLista(nomeTarefa);
                dao.inserir(tarefa);

                speak("Qual o primeiro item da lista?");
                txtMsgUsuario.setText("Qual o primeiro item da lista?");
                flagTarefa = 2;
                delay();
                break;
            case 2:
                String item = command.substring(0,1).toUpperCase().concat(command.substring(1));
                itemTarefa = item;
                salvaItem(itemTarefa);

                speak("Qual o próximo item?");
                txtMsgUsuario.setText("Qual o próximo item?");
                flagTarefa = 3;
                delay();
                break;
            case 3:
                String item2 = command.substring(0,1).toUpperCase().concat(command.substring(1));
                itemTarefa = item2;
                salvaItem(itemTarefa);

                speak("Qual o próximo item?");
                txtMsgUsuario.setText("Qual o próximo item?");
                flagTarefa = 2;
                delay();
                break;
            default:
                break;
        }

        if (command.contains("qual")) {
            if (command.contains("seu nome")) {
                speak("Meu nome é Lori");
                txtMsgUsuario.setText("Meu nome é Lori");
                //txtMsgUsuario.setText(getString(R.string.fala_2));
            }
        }

        if (command.contains("criar")) {
            if (command.contains("evento")) {
                speak("Qual é o nome do evento?");
                txtMsgUsuario.setText("Qual é o nome do evento?");
                flagEvento = 1;
                delay();
            }
        }

        if (flagEvento == 3) {
            if (command.contains("não")) {
                speak("Até logo");
                txtMsgUsuario.setText("Até logo");

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat hora = new SimpleDateFormat("HH:mm");
                dataEvento = dateFormat.format(dateTime.getTime());
                horaEvento = hora.format(dateTime.getTime());

                CriarEvento evento = new CriarEvento();
                evento.setTitulo(nomeEvento);
                evento.setDescricao("");
                evento.setTipoEvento(tipoEvento);
                evento.setData(dataEvento);
                evento.setHora(horaEvento);
                evento.setAddTarefa("");

                CriarEventoDAO dao = new CriarEventoDAO(getApplicationContext());
                dao.inserir(evento);
                flagEvento = 4;
            }
        }

        if (command.contains("criar")) {
            if (command.contains("tarefa")) {
                speak("Qual é o nome da lista de tarefa?");
                txtMsgUsuario.setText("Qual é o nome da lista de tarefa?");
                flagTarefa = 1;
                delay();
            }
        }

        if (command.contains("salvar")) {
            if (command.contains("tarefa")) {
                speak("Sua lista de tarefa foi salva, gostaria de mais alguma coisa?");
                txtMsgUsuario.setText("Sua lista de tarefa foi salva, gostaria de mais alguma coisa?");
                delay3();
            }
        }

        if (command.contains("não")) {
            speak("Até logo");
            txtMsgUsuario.setText("Até logo");
        }
    }

    private void salvaItem(String itemSalvar) {
        ArrayList<TarefaNome> tarefas = (ArrayList<TarefaNome>) dao.listar();
        for (int i = 0; i < tarefas.size(); i++) {
            TarefaNome tarefa = tarefas.get(i);
            idNomeTarefa = Integer.parseInt(tarefa.getId().toString());
        }

        CriarTarefa tarefa = new CriarTarefa();
        tarefa.setIdNome(idNomeTarefa);
        tarefa.setItemLista(itemSalvar);
        dao.inserirItem(tarefa);
    }

    private void delay() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                inicializaSPR();
            }
        }, 2000);
    }

    private void delay2() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                inicializaSPR();
            }
        }, 3500);
    }

    private void delay3() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                inicializaSPR();
            }
        }, 4000);
    }

    private void inicializaSPR() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        mySR.startListening(intent);
        initializeSpeechRecognizer();
    }

    private void initializeTextToSpeech() {
        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (myTTS.getEngines().size() == 0) {
                    Toast.makeText(Assistente.this, getString(R.string.sem_suporte),
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    myTTS.setLanguage((Locale.getDefault()));
                    speak(getString(R.string.fala_1));
                    txtMsgUsuario.setText(getString(R.string.fala_1));
                }
            }
        });
    }

    private void speak(String message) {
        if (Build.VERSION.SDK_INT >= 21) {
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            myTTS.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        myTTS.shutdown();
    }

    private void informacao() {
        Button btnInformacao = findViewById(R.id.btnInformacao);
        btnInformacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Assistente.this, AssistenteInformacao.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Assistente.this, Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }
}