package com.example.educapp.view.minha_conta;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.educapp.R;
import com.example.educapp.view.menu.MinhaConta;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil extends AppCompatActivity implements View.OnClickListener {
    private EditText editNomePerfil;
    private CircleImageView imgUsuarioPerfil;
    private LinearLayout inflateMenuFoto;

    private static final int CODIGO_GALERIA = 1;
    private static final int PERMISSAO_REQUEST = 2;
    private static final int CODIGO_CAMERA = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conta_perfil);
        chamaFuncao();
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        iniciaVariavel();
        imagemCircular();
        recebeNome();
    }

    //Pegando as referências dos Botões e vinculando ao click
    public void iniciaVariavel() {
        editNomePerfil = findViewById(R.id.editNomePerfil);
        imgUsuarioPerfil = findViewById(R.id.imgUsuario);
        inflateMenuFoto = findViewById(R.id.inflateMenuFoto);

        FloatingActionButton fabAlterarFoto = findViewById(R.id.fabAlterarFoto);
        FloatingActionButton fabGaleria = findViewById(R.id.fabGaleria);
        FloatingActionButton fabCamera = findViewById(R.id.fabCamera);
        FloatingActionButton fabRemoverFoto = findViewById(R.id.fabRemoverFoto);
        ImageButton imgFechar = findViewById(R.id.imgFechar);

        fabAlterarFoto.setOnClickListener(this);
        fabGaleria.setOnClickListener(this);
        fabCamera.setOnClickListener(this);
        fabRemoverFoto.setOnClickListener(this);
        imgFechar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAlterarFoto:
                abreMenuFoto();
                break;
            case R.id.fabGaleria:
                acessoGaleria();
                abreGaleria();
                break;
            case R.id.fabCamera:
                abreCamera();
                break;
            case R.id.fabRemoverFoto:
                removeFoto();
                break;
            case R.id.imgFechar:
                fechaMenuFoto();
                break;
        }
    }

    private void imagemCircular() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_user);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imgUsuarioPerfil.setImageDrawable(roundedBitmapDrawable);
    }

    //Infla a layout das opções da foto
    private void abreMenuFoto() {
        if (inflateMenuFoto.getVisibility() == View.GONE) {
            inflateMenuFoto.setVisibility(View.VISIBLE);
        }
    }

    //Remove o layout das opções da foto
    private void fechaMenuFoto() {
        if (inflateMenuFoto.getVisibility() == View.VISIBLE) {
            inflateMenuFoto.setVisibility(View.GONE);
        }
    }

    private void abreGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CODIGO_GALERIA);
    }

    private void abreCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CODIGO_CAMERA);
        }
    }

    private void removeFoto() {
        imgUsuarioPerfil.setImageResource(R.drawable.img_user);
        fechaMenuFoto();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_GALERIA && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(uri, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String fotoPath = c.getString(columnIndex);
            c.close();
            Bitmap bitmap = BitmapFactory.decodeFile(fotoPath);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            imgUsuarioPerfil.setImageBitmap(bitmapReduzido);
            imgUsuarioPerfil.setTag(fotoPath);
        }

        if (requestCode == CODIGO_CAMERA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgUsuarioPerfil.setImageBitmap(imageBitmap);
        }
        fechaMenuFoto();
    }

    //Tratamento da resposta do usuario
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSAO_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //A permissão foi aceita
            } else {
                //A permissão foi negada
            }
        }
    }

    //Permissão para acessar ou negar o acesso a galeria
    private void acessoGaleria() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
            }
        }
    }

    //Envia a foto para Minha COnta
    private void enviaFoto() {
        Intent intent = new Intent(Perfil.this, MinhaConta.class);
        intent.putExtra("foto", R.drawable.img_user);
        startActivity(intent);
    }

    //Envia o nome para Minha COnta
    private void enviaNome() {
        if (TextUtils.isEmpty(editNomePerfil.getText().toString()) || editNomePerfil.getText().toString().trim().isEmpty()) {
            editNomePerfil.requestFocus();
            editNomePerfil.setError(getString(R.string.msgNome));
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("nome", editNomePerfil.getText().toString());
            Intent intent = new Intent(Perfil.this, MinhaConta.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void recebeNome() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String nome = bundle.getString("nome");
                editNomePerfil.setText(nome);
                editNomePerfil.clearFocus();
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                editNomePerfil.setFocusable(false);
                editNomePerfil.setFocusableInTouchMode(false);
                editNomePerfil.setFocusable(true);
                editNomePerfil.setFocusableInTouchMode(true);
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (inflateMenuFoto.getVisibility() == View.VISIBLE) {
            inflateMenuFoto.setVisibility(View.GONE);
        } else {
           // enviaFoto();
            enviaNome();
            finish();
        }
    }
}