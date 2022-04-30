package com.warrenverr.ppick;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CrawlingExample {

    private static final String url = "https://www.campuspick.com/";

    public void process(){
        Connection conn = Jsoup.connect(url);

        Document document = null;

        try {
            document = conn.get();
        }catch (IOException e){
            e.printStackTrace();
        }

        List<String> list=getDataList(document);

    }

    private List<String> getDataList(Document document) {

        ArrayList<String> list = new ArrayList<>();

        Elements selects = document.select(".sentence-list");

        for (Element select: selects) {
            System.out.println(select.html());
        }
        return list;
    }


}
