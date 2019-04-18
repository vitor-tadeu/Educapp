package com.example.educapp.view.menu;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.educapp.R;
import com.example.educapp.fragments.CalendarioFragment;
import com.example.educapp.fragments.EventoConfirmadoFragment;
import com.example.educapp.fragments.TarefaConfirmadoFragment;
import com.example.educapp.util.layout_inflater.DialogFecharApp;
import com.example.educapp.util.navigation_bar.BottomNavigationViewHelper;
import com.example.educapp.util.navigation_bar.SectionsPageAdapter;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_home);
        chamaFuncao();
    }

    //Chama todas as funções necessarias
    private void chamaFuncao() {
        tabNavigation();
        navView();
    }

    private void tabNavigation() {
        //Configura o ViewPager com o adaptador de seções
        ViewPager mViewPager = findViewById(R.id.viewPager);
        setupViewPager(mViewPager);

        //Define o ID das tabs
        TabLayout tabLayout = findViewById(R.id.nav_tab);
        tabLayout.setupWithViewPager(mViewPager);

        //Insere o icone na tab
        tabLayout.getTabAt(0).setText(R.string.lista_tarefa);
        tabLayout.getTabAt(1).setText(R.string.calendário).select();
        tabLayout.getTabAt(2).setText(R.string.evento);
    }

    //Adiciona os fragmentos nos adapters
    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TarefaConfirmadoFragment());
        adapter.addFragment(new CalendarioFragment());
        adapter.addFragment(new EventoConfirmadoFragment());
        viewPager.setAdapter(adapter);
    }

    //Seleção dos botões da barra de menu
    private void navView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_criar_evento:
                        intent = new Intent(Home.this, Lembretes.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                    case R.id.nav_assistente:
                        intent = new Intent(Home.this, Assistente.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                    case R.id.nav_minha_conta:
                        intent = new Intent(Home.this, MinhaConta.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DialogFecharApp fecharApp = new DialogFecharApp(Home.this);
        fecharApp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        fecharApp.setCancelable(false);
        fecharApp.show();
    }
}