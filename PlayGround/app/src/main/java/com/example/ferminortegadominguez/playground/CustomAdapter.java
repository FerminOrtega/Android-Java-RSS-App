package com.example.ferminortegadominguez.playground;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    SimpleDateFormat formatofecha=new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
    Date date;
    DownloadImageWithURLTask downloadTask;
    ImageView image;
    File file = new File("/data/data/com.example.ferminortegadominguez.playground/favoritos.txt");
    FileWriter fileWriter;
    BufferedReader br;
    String categoriaenviada;

    public static boolean check = true;

    public CustomAdapter(Context context, String categoria) {
        this.context = context;
        this.categoriaenviada = categoria;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return MainActivity.listaArticulos.size();
    }

    @Override
    public Object getItem(int position) {
        return MainActivity.listaArticulos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;


        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.publicacion, null, true);


            holder.titular = (TextView) convertView.findViewById(R.id.item);
            holder.contenido = (TextView) convertView.findViewById(R.id.contenido);
            holder.categoria = (TextView) convertView.findViewById(R.id.categoria);
            holder.fecha = (TextView) convertView.findViewById(R.id.fecha);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.imageButton = (ImageButton) convertView.findViewById(R.id.bookmark);
            holder.reloj = (ImageView) convertView.findViewById(R.id.imageView2);
            holder.fecha = (TextView) convertView.findViewById(R.id.fecha);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }
            if(MainActivity.listaArticulos.get(position).title.equals("No tienes Favoritos en la lista")){
                holder.titular.setText(MainActivity.listaArticulos.get(position).title);
                holder.reloj.setVisibility(View.INVISIBLE);
                holder.imageButton.setVisibility(View.INVISIBLE);
            }else {


                holder.titular.setText(MainActivity.listaArticulos.get(position).title);
                holder.contenido.setText(MainActivity.listaArticulos.get(position).descripcion);
                if (MainActivity.listaArticulos.get(position).category.equals(" Now ")) {
                    holder.categoria.setTextAppearance(R.style.color_now);
                } else if (MainActivity.listaArticulos.get(position).category.trim().equals("Culture")) {
                    holder.categoria.setTextAppearance(R.style.color_culture);
                } else if (MainActivity.listaArticulos.get(position).category.equals(" Lit ")) {
                    holder.categoria.setTextAppearance(R.style.color_lit);
                } else if (MainActivity.listaArticulos.get(position).category.equals(" Fire ")) {
                    holder.categoria.setTextAppearance(R.style.color_fire);
                } else if (MainActivity.listaArticulos.get(position).category.equals(" Sports ")) {
                    holder.categoria.setTextAppearance(R.style.color_sports);
                } else if (MainActivity.listaArticulos.get(position).category.equals(" Food ")) {
                    holder.categoria.setTextAppearance(R.style.color_food);
                } else if (MainActivity.listaArticulos.get(position).category.equals(" Do ")) {
                    holder.categoria.setTextAppearance(R.style.color_do);
                } else {
                    holder.categoria.setTextAppearance(R.style.color_food);
                }
                holder.categoria.setText(MainActivity.listaArticulos.get(position).category);
                try {
                    holder.fecha.setText(formatDate(formatofecha.parse(MainActivity.listaArticulos.get(position).date)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                downloadTask = new DownloadImageWithURLTask(holder.imageView);
                downloadTask.execute(MainActivity.listaArticulos.get(position).enclosure);

            }



        try {
            br = new BufferedReader(new FileReader(file));
            String st;

            while ((st = br.readLine()) != null)
                if (st.equals(MainActivity.listaArticulos.get(position).link)) {
                    holder.imageButton.setImageResource(R.drawable.bookmarkon);
                    holder.imageButton.setActivated(true);
                    //MainActivity.listaFavoritos.add(MainActivity.listaArticulos.get(position).link);
                }

        } catch (IOException e) {
            e.getMessage();
        }


        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!holder.imageButton.isActivated()) {
                    holder.imageButton.setImageResource(R.drawable.bookmarkon);
                    check = false;
                    MainActivity.listaFavoritos.add(MainActivity.listaArticulos.get(position).link);
                    Toast.makeText(context.getApplicationContext(), "Se ha anadido a Favoritos", Toast.LENGTH_SHORT).show();
                } else {
                    holder.imageButton.setImageResource(R.drawable.bookmarkoff);
                    check = true;
                    for (int i = 0; i < MainActivity.listaFavoritos.size(); i++) {
                        if (MainActivity.listaFavoritos.get(i).equals(MainActivity.listaArticulos.get(position).link)) {
                            MainActivity.listaFavoritos.remove(i);
                        }
                    }
                    Toast.makeText(context.getApplicationContext(), "Se ha eliminado de Favoritos", Toast.LENGTH_SHORT).show();

                }

                try {
                    fileWriter = new FileWriter(file);
                    for (int i = 0; i < MainActivity.listaFavoritos.size(); i++) {
                        fileWriter.write(MainActivity.listaFavoritos.get(i) + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fileWriter.close();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        holder.titular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadTask = new DownloadImageWithURLTask(MainActivity.imagen_noticia);
                downloadTask.execute(MainActivity.listaArticulos.get(position).enclosure);
                MainActivity.titular_noticia.setText(MainActivity.listaArticulos.get(position).title);
                MainActivity.descripcion_noticia.loadData(MainActivity.listaArticulos.get(position).contenido2,"text/html; charset=utf-8", "utf-8");
                MainActivity.descripcion_noticia.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                MainActivity.autor_noticia.setText("Por: "+MainActivity.listaArticulos.get(position).autor);
                MainActivity.content_main.setVisibility(View.INVISIBLE);
                MainActivity.content_videos.setVisibility(View.INVISIBLE);
                MainActivity.content_noticia.setVisibility(View.VISIBLE);
            }
        });


        return convertView;
    }

    private static String formatDate(Date fechapublic){
        Date fecha = new Date();
        int diferencia=(int) ((fecha.getTime()-fechapublic.getTime())/1000);
        String Diferencia;
        int dias=0;
        int horas=0;
        if(diferencia>86400) {
            dias=(int)Math.floor(diferencia/86400);
            diferencia=diferencia-(dias*86400);
        }
        if(diferencia>3600) {
            horas=(int)Math.floor(diferencia/3600);
            diferencia=diferencia-(horas*3600);
        }
        if(dias==0){
            return horas+"'";
        }
        return dias+"d "+horas+"'";
    }

    private class DownloadImageWithURLTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        InputStream in;
        String pathToFile;
        Bitmap bitmap = null;

        public DownloadImageWithURLTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            pathToFile = urls[0];
            try {
                in = new java.net.URL(pathToFile).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private class ViewHolder {

        protected TextView fecha;
        private TextView titular, contenido, categoria;
        private ImageButton imageButton;
        private ImageView imageView;
        protected ImageView reloj;

    }

}
