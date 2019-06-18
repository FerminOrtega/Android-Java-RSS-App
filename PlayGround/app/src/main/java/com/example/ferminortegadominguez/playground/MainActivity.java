package com.example.ferminortegadominguez.playground;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private RecyclerView recyclerView;


    public static ArrayList<String> listaFavoritos = new ArrayList<>();
    public static List<PGPortadaXMLParser.Item> listaArticulos = null;
    public static ArrayList<String> listaVideos= new ArrayList<>();
    public List<PGPortadaXMLParser.Item> lista;
    public static ConstraintLayout content_main;
    public static ConstraintLayout content_videos;
    public static ConstraintLayout content_noticia;
    TextView textView;
    Menu menu;
    NavigationView navigationView;
    ListView lvl;
    CustomAdapter adaptor;

    public static TextView titular_noticia;
    public static TextView autor_noticia;
    public static ImageView imagen_noticia;
    public static WebView descripcion_noticia;

    File file = new File("/data/data/com.example.ferminortegadominguez.playground/favoritos.txt");
    public static MediaController mediaController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mediaController = new MediaController(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        content_main = findViewById(R.id.content_main);
        content_videos = findViewById(R.id.content_videos);
        content_noticia = findViewById(R.id.content_noticia);
        content_main.setVisibility(View.VISIBLE);
        content_videos.setVisibility(View.INVISIBLE);
        content_noticia.setVisibility(View.INVISIBLE);

        titular_noticia = findViewById(R.id.titular);
        descripcion_noticia = findViewById(R.id.descripcion_noticia);
        imagen_noticia = findViewById(R.id.imageViewnoticia);
        autor_noticia = findViewById(R.id.autor);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        menu = navigationView.getMenu();
        listaVideos.add("2q7XGuH2Q");
        listaVideos.add("2q7XGuH2Q");
        listaVideos.add("2q7XGuH2Q");
        listaVideos.add("2q7XGuH2Q");



        setColores();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                MainActivity.listaFavoritos.add(st);
            }

        } catch (IOException e) {
            e.getMessage();
        }

        listaArticulos = listaPrinci("https://www.playgroundmag.net/rss.xml", "novedades");
        lvl = findViewById(R.id.lista);

        adaptor = new CustomAdapter(this, "novedades");

        lvl.setAdapter(adaptor);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        textView = findViewById(R.id.tvtema);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            textView.setText("Novedades");
            textView.setTextSize(50);
            listaArticulos.clear();
            listaArticulos = listaPrinci("https://www.playgroundmag.net/rss.xml", "novedades");
            content_main.setVisibility(View.VISIBLE);
            content_videos.setVisibility(View.INVISIBLE);
            content_noticia.setVisibility(View.INVISIBLE);
            adaptor = new CustomAdapter(this, "novedades");

            lvl.setAdapter(adaptor);
        } else if (id == R.id.nav_now) {
            textView.setText("Now");
            textView.setTextSize(50);
            listaArticulos.clear();
            listaArticulos = listaPrinci("https://www.playgroundmag.net/now/rss.xml", "now");
            content_main.setVisibility(View.VISIBLE);
            content_videos.setVisibility(View.INVISIBLE);
            content_noticia.setVisibility(View.INVISIBLE);
            adaptor = new CustomAdapter(this, "now");

            lvl.setAdapter(adaptor);

        } else if (id == R.id.nav_culture) {
            textView.setText("Culture");
            textView.setTextSize(50);
            listaArticulos.clear();
            listaArticulos = listaPrinci("https://www.playgroundmag.net/cultura/rss.xml", "cultura");
            content_main.setVisibility(View.VISIBLE);
            content_videos.setVisibility(View.INVISIBLE);
            content_noticia.setVisibility(View.INVISIBLE);
            adaptor = new CustomAdapter(this, "cultura");

            lvl.setAdapter(adaptor);

        } else if (id == R.id.nav_lit) {
            textView.setText("Lit");
            textView.setTextSize(50);
            listaArticulos.clear();
            listaArticulos = listaPrinci("https://www.playgroundmag.net/lit/rss.xml", "lit");
            content_main.setVisibility(View.VISIBLE);
            content_videos.setVisibility(View.INVISIBLE);
            content_noticia.setVisibility(View.INVISIBLE);
            adaptor = new CustomAdapter(this, "lit");

            lvl.setAdapter(adaptor);

        } else if (id == R.id.nav_fire) {
            textView.setText("Fire");
            textView.setTextSize(50);
            listaArticulos.clear();
            listaArticulos = listaPrinci("https://www.playgroundmag.net/fire/rss.xml", "fire");
            content_main.setVisibility(View.VISIBLE);
            content_videos.setVisibility(View.INVISIBLE);
            content_noticia.setVisibility(View.INVISIBLE);
            adaptor = new CustomAdapter(this, "fire");

            lvl.setAdapter(adaptor);

        } else if (id == R.id.nav_sports) {
            textView.setText("Sport");
            textView.setTextSize(50);
            listaArticulos.clear();
            listaArticulos = listaPrinci("https://www.playgroundmag.net/sports/rss.xml", "sport");
            content_main.setVisibility(View.VISIBLE);
            content_videos.setVisibility(View.INVISIBLE);
            content_noticia.setVisibility(View.INVISIBLE);
            adaptor = new CustomAdapter(this, "sport");

            lvl.setAdapter(adaptor);

        } else if (id == R.id.nav_food) {
            textView.setText("Food");
            textView.setTextSize(50);
            listaArticulos.clear();
            listaArticulos = listaPrinci("https://www.playgroundmag.net/food/rss.xml", "food");
            content_main.setVisibility(View.VISIBLE);
            content_videos.setVisibility(View.INVISIBLE);
            content_noticia.setVisibility(View.INVISIBLE);
            adaptor = new CustomAdapter(this, "food");

            lvl.setAdapter(adaptor);

        } else if (id == R.id.nav_do) {
            textView.setText("Do");
            textView.setTextSize(50);
            listaArticulos.clear();
            listaArticulos = listaPrinci("https://www.playgroundmag.net/do/rss.xml", "do");
            content_main.setVisibility(View.VISIBLE);
            content_videos.setVisibility(View.INVISIBLE);
            content_noticia.setVisibility(View.INVISIBLE);
            adaptor = new CustomAdapter(this, "do");

            lvl.setAdapter(adaptor);
        } else if (id == R.id.nav_favoritos) {
            textView.setText("Favoritos");
            textView.setTextSize(50);
            listaArticulos.clear();
            content_main.setVisibility(View.VISIBLE);
            content_videos.setVisibility(View.INVISIBLE);
            content_noticia.setVisibility(View.INVISIBLE);
            listaArticulos = listaPrinci("https://aluminiosortegadominguez.com/playgroundmag/articulos.xml", "favoritos");
            adaptor = new CustomAdapter(this, "favoritos");
            lvl.setAdapter(adaptor);
        } else if (id == R.id.nav_videos) {
            textView.setText("Vídeos");
            textView.setTextSize(50);
            listaArticulos.clear();
            content_main.setVisibility(View.INVISIBLE);
            content_noticia.setVisibility(View.INVISIBLE);
            content_videos.setVisibility(View.VISIBLE);
            setUpRecyclerView();
            populateRecyclerView();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private List listaPrinci(String url, String categoria) {
        DownloadXML.URL = url;
        DownloadXML.categoria=categoria;
        DownloadXML.loadPage();


        DownloadXML.bandera = false;
        while (DownloadXML.bandera == false) {
            System.out.println("Cargando");

        }
        DownloadXML.bandera = false;
        lista = DownloadXML.articulos;
        return lista;
    }

    public void setColores() {
        MenuItem inicio = menu.findItem(R.id.nav_inicio);
        SpannableString s = new SpannableString(inicio.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.color_inicio), 0, s.length(), 0);
        inicio.setTitle(s);

        MenuItem now = menu.findItem(R.id.nav_now);
        s = new SpannableString(now.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.color_now), 0, s.length(), 0);
        now.setTitle(s);

        MenuItem culture = menu.findItem(R.id.nav_culture);
        s = new SpannableString(culture.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.color_culture), 0, s.length(), 0);
        culture.setTitle(s);

        MenuItem lit = menu.findItem(R.id.nav_lit);
        s = new SpannableString(lit.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.color_lit), 0, s.length(), 0);
        lit.setTitle(s);

        MenuItem fire = menu.findItem(R.id.nav_fire);
        s = new SpannableString(fire.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.color_fire), 0, s.length(), 0);
        fire.setTitle(s);

        MenuItem sports = menu.findItem(R.id.nav_sports);
        s = new SpannableString(sports.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.color_sports), 0, s.length(), 0);
        sports.setTitle(s);

        MenuItem food = menu.findItem(R.id.nav_food);
        s = new SpannableString(food.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.color_food), 0, s.length(), 0);
        food.setTitle(s);

        MenuItem doo = menu.findItem(R.id.nav_do);
        s = new SpannableString(doo.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.color_do), 0, s.length(), 0);
        doo.setTitle(s);
        navigationView.setNavigationItemSelectedListener(this);

        MenuItem favoritos = menu.findItem(R.id.nav_favoritos);
        s = new SpannableString(favoritos.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.color_fire), 0, s.length(), 0);
        favoritos.setTitle(s);
        navigationView.setNavigationItemSelectedListener(this);

        MenuItem videos = menu.findItem(R.id.nav_videos);
        s = new SpannableString(videos.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.color_do), 0, s.length(), 0);
        videos.setTitle(s);
        navigationView.setNavigationItemSelectedListener(this);
    }
    /**
     * setup the recyclerview here
     */
    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * populate the recyclerview and implement the click event here
     */
    private void populateRecyclerView() {
        final ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = generateDummyVideoList();
        YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(this, youtubeVideoModelArrayList);
        recyclerView.setAdapter(adapter);

        //set click event
        recyclerView.addOnItemTouchListener(new RecyclerViewOnClickListener(this, new RecyclerViewOnClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //start youtube player activity by passing selected video id via intent
                startActivity(new Intent(MainActivity.this, YoutubePlayerActivity.class)
                        .putExtra("video_id", youtubeVideoModelArrayList.get(position).getVideoId()));

            }
        }));
    }


    /**
     * method to generate dummy array list of videos
     *
     * @return
     */
    private ArrayList<YoutubeVideoModel> generateDummyVideoList() {
        ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = new ArrayList<>();

        //get the video id array, title array and duration array from strings.xml
        String[] videoIDArray = getResources().getStringArray(R.array.video_id_array);
        String[] videoTitleArray = getResources().getStringArray(R.array.video_title_array);
        String[] videoDurationArray = getResources().getStringArray(R.array.video_duration_array);

        //loop through all items and add them to arraylist
        for (int i = 0; i < videoIDArray.length; i++) {

            YoutubeVideoModel youtubeVideoModel = new YoutubeVideoModel();
            youtubeVideoModel.setVideoId(videoIDArray[i]);
            youtubeVideoModel.setTitle(videoTitleArray[i]);
            youtubeVideoModel.setDuration(videoDurationArray[i]);

            youtubeVideoModelArrayList.add(youtubeVideoModel);

        }

        return youtubeVideoModelArrayList;
    }




}
