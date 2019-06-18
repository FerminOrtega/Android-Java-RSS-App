package com.example.ferminortegadominguez.playground;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PGPortadaXMLParser {
    public static final Object Item = "";
    private static final String ns = null;
    public XmlPullParser parser;
    public List<Item> entries;
    public List<Item> entriesfavoritos;
    public String name;
    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readRss(parser);
        } finally {
            //in.close();
        }
    }

    private List readRss(XmlPullParser parser) throws XmlPullParserException, IOException {
        entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "rss");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
             name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("channel")) {
                entries=readChannel(parser);
            } else {
                skip(parser);
            }
        }
        return entries;
    }
    private List readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("item")) {
                entries.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        if(DownloadXML.categoria.equals("favoritos")){
            entriesfavoritos = new ArrayList();
            for(int i = 0; i<entries.size();i++){
                for(int o = 0; o<MainActivity.listaFavoritos.size();o++){
                    if(entries.get(i).link.equals(MainActivity.listaFavoritos.get(o))){
                        entriesfavoritos.add(entries.get(i));
                    }
                }
            }
            if (entriesfavoritos.isEmpty()){
                entriesfavoritos.add(new Item("No tienes Favoritos en la lista", "", "", "", "", "", ""));
            }
            return entriesfavoritos;
        }else{
            return entries;
        }


    }
    public static class Item {
        public final String title;
        public final String link;
        public final String enclosure;
        public final String date;
        public final String category;
        public final String descripcion;
        public final String autor;
        public final String contenido2;

        public Item(String title, String link, String enclosure, String date, String category, String descripcion, String autor) {
            this.title = title;
            this.link = link;
            this.enclosure = enclosure;
            this.date = date;
            this.category = category;
            this.descripcion = descripcion;
            this.autor = autor;
            contenido2 = null;
        }
        public Item(String title, String link, String enclosure, String date, String category, String descripcion, String autor, String contenido) {
            this.title = title;
            this.link = link;
            this.enclosure = enclosure;
            this.date = date;
            this.category = category;
            this.descripcion = descripcion;
            this.autor = autor;
            this.contenido2 = contenido;
        }
    }
    public String title ;
    public String link ;
    public String enclosure ;
    public String date ;
    public String category ;
    public String descripcion ;
    public String autor ;
    public String contenido2 ;

    private Item readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
         title = null;
         link = null;
         enclosure = null;
         date = null;
         category = null;
         descripcion = null;
         autor = null;
         contenido2 =null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = read(parser, "title");
            } else if (name.equals("link")) {
                link = read(parser, "link");
            } else if (name.equals("enclosure")) {
                enclosure = readLink(parser);
            } else if (name.equals("pubDate")) {
                date = read(parser, "pubDate");
            } else if (name.equals("category")) {
                category = read(parser, "category");
            } else if (name.equals("description")) {
                descripcion = read(parser, "description");
            } else if (name.equals("dc:creator")) {
                autor = read(parser, "dc:creator");
            } else if (name.equals("media:thumbnail")) {
                enclosure = readLink2(parser);
            }else if (name.equals("content:encoded")) {
                contenido2 = read(parser, "content:encoded");
            }else if (name.equals("author")) {
                autor = read(parser, "author");
            } else {
                skip(parser);
            }
        }
        if(contenido2==null){
            return new Item(title, link, enclosure, date, category, descripcion, autor);
        }else {
            return new Item(title, link, enclosure, date, category, descripcion, autor, contenido2);
        }

    }
    public String contenido;
    private String read(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
         contenido = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return contenido;
    }
    public String linktag;
    public String tag;
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
         linktag = "";
        parser.require(XmlPullParser.START_TAG, ns, "enclosure");
         tag = parser.getName();
        //String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("enclosure")) {
            //if (relType.equals("alternate")){
            linktag = parser.getAttributeValue(null, "url");
            parser.nextTag();
            //}
        }
        parser.require(XmlPullParser.END_TAG, ns, "enclosure");

        return linktag;
    }
    private String readLink2(XmlPullParser parser) throws IOException, XmlPullParserException {
        linktag = "";
        parser.require(XmlPullParser.START_TAG, ns, "media:thumbnail");
        tag = parser.getName();
        //String relType = parser.getAttributeValue(null, "rel");
        if (tag.equals("media:thumbnail")) {
            //if (relType.equals("alternate")){
            linktag = parser.getAttributeValue(null, "url");
            parser.nextTag();
            //}
        }
        parser.require(XmlPullParser.END_TAG, ns, "media:thumbnail");

        return linktag;
    }
    public String result;
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
         result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
