package com.wkswind.comicviewer.utils;

import com.wkswind.comicviewer.bean.GalleryItem;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/15.
 */

public class ContentParser {
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

}
