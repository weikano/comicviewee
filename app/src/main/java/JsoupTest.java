package com.wkswind.wnacg.viewer.test;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by �Ϸ粻�� on 2017/5/14.
 */
public class JsoupTest {
    public static void main(String[] args) throws IOException {
//        detail();
        detailToDownload("http://www.wnacg.org/download-index-aid-38251.html");
    }

    private static void detail() throws IOException {
        Document doc = Jsoup.connect("https://www.wnacg.org/photos-index-aid-38251.html").get();
        printLine("userful");
        Element userWrap = doc.getElementsByClass("userwrap").first();
        String title = userWrap.select("h2").first().text();
        String downloadUrl = doc.getElementsByClass("downloadbtn").first().attr("href");
        System.out.println(title);
        System.out.println(downloadUrl);

        printLine("分类页数");
        Element infoWrap = doc.getElementsByClass("asTBcell uwconn").first();
        Elements labels = infoWrap.select("label");
        int page = 0;
        String category = null;
        for (int i = 0; i < 2; i++) {
            String label = labels.get(i).text();
            if(label.contains("頁數：")){
                page = Integer.parseInt(label.replace("頁數：","").replace("P",""));
            }else if(label.contains("分類：")) {
                category = label.replace("分類：","");
            }
        }
        System.out.println(page);
        System.out.println(category);
        printLine("tags");
        Element tags = doc.getElementsByClass("addtags").first();
        System.out.println(tags.text());
        printLine("brief");
        System.out.println(infoWrap.select("p").first());

        printLine("grid");
        Element grid = doc.getElementsByClass("grid").first();
        Element galleryWrap = grid.getElementsByClass("gallary_wrap").first();
        Element cc = galleryWrap.getElementsByClass("cc").first();
        Elements items = cc.getElementsByClass("li gallary_item");

//        System.out.println(items)
        if(items != null && !items.isEmpty()){
            String firtPage = items.first().select("a").first().attr("href");
            int firt = Integer.parseInt(firtPage.replace("/photos-view-id-", "").replace(".html", ""));
            System.out.println(firt);
            System.out.println("/photos-view-id-" + (firt + page - 1) +".html");
        }

//        for (Element item : items) {
//            System.out.println(item.select("a").first().attr("href"));
//        }
    }

    private static void printLine(String tag){
        System.out.println("======#### " + tag + "================");
    }

    //http://www.wnacg.org/download-index-aid-38251.html
    private static void detailToDownload(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc.getElementsByClass("down_btn").first().attr("href"));
    }
}
