package com.wkswind.comicviewer.utils;

import android.text.TextUtils;

import com.wkswind.comicviewer.api.ViewerWNAcg;
import com.wkswind.comicviewer.bean.ComicDetail;
import com.wkswind.comicviewer.bean.GalleryItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;

/**
 * Created by Administrator on 2017/5/15.
 */

public class ContentParser {
    private static final NumberFormat nf;
    static {
        nf = NumberFormat.getInstance();
    }

    public static HttpUrl pageUrl(ComicDetail detail,int page) {
        int length = String.valueOf(detail.getPage()).length();
        nf.setMaximumIntegerDigits(length);
        nf.setMinimumIntegerDigits(length);
        return ViewerWNAcg.URL.newBuilder().addEncodedPathSegment(detail.getIndex()).addEncodedPathSegment(nf.format(page)+".jpg").build();
    }

    public static List<GalleryItem> parseGalleryItems(Document document) {
        Elements elements = document.getElementsByClass("li gallary_item");
        int size = elements.size();
        ArrayList<GalleryItem> items = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            items.add(parseGalleryItemInner(elements.get(i)));
        }
        return items;
    }

    private static GalleryItem parseGalleryItemInner(Element element) {
        GalleryItem item = new GalleryItem();
        item.setIndex(element.select("a").first().attr("href"));
        Elements imgElement = element.select("img");
        item.setBackground(imgElement.attr("src"));
        item.setTitle(imgElement.attr("alt"));
        item.setInfo(element.getElementsByClass("info_col").first().text());
        return item;
    }

//    "https://www.wnacg.org/photos-index-aid-38251.html"
    public static ComicDetail parseDetail(Document doc) throws IOException {
        ComicDetail detail = new ComicDetail();
        Element userWrap = doc.getElementsByClass("userwrap").first();
        detail.setTitle(userWrap.select("h2").first().text());
        detail.setDownloadPageUrl(doc.getElementsByClass("downloadbtn").first().attr("href"));
        Element infoWrap = doc.getElementsByClass("asTBcell uwconn").first();
        Elements labels = infoWrap.select("label");
        int page = -1;
        String category = null;
        for (int i = 0; i < 2; i++) {
            String label = labels.get(i).text();
            if(label.contains("頁數：")){
                page = Integer.parseInt(label.replace("頁數：","").replace("P",""));
            }else if(label.contains("分類：")) {
                category = label.replace("分類：","");
            }
        }
        if(page > 0) {
            detail.setPage(page);
        }
        if(!TextUtils.isEmpty(category)){
            detail.setCategory(category);
        }
        Element tags = doc.getElementsByClass("addtags").first();
        detail.setTags(tags.text());
        detail.setBrief(infoWrap.select("p").first().text());

        Element grid = doc.getElementsByClass("grid").first();
        Element galleryWrap = grid.getElementsByClass("gallary_wrap").first();
        Element cc = galleryWrap.getElementsByClass("cc").first();
        Elements items = cc.getElementsByClass("li gallary_item");

//        System.out.println(items)
        if(items != null && !items.isEmpty()){
            String firtPage = items.first().select("a").first().attr("href").replace("/","");
            Document pageDoc = Jsoup.connect(ViewerWNAcg.URL.newBuilder().addPathSegment(firtPage).toString()).get();
            Element sd = pageDoc.getElementsByClass("sd").first();
            String script = sd.select("script").first().html();
            int begin = script.indexOf("/data/");
            int end = script.lastIndexOf("/");
            detail.setIndex(script.substring(begin+1, end));
//            detail.setFirstPage(Integer.parseInt(firtPage.replace("/photos-view-id-", "").replace(".html", "")));
        }
        return detail;
    }

    public static String parseDownloadUrl(Document doc) throws IOException {
        return doc.getElementsByClass("down_btn").first().attr("href");
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
