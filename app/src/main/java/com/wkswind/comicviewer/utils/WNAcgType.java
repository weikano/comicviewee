package com.wkswind.comicviewer.utils;

import com.wkswind.comicviewer.R;

/**
 * Created by �Ϸ粻�� on 2017/5/14.
 */
public enum WNAcgType {

    HOME("/index.php"),

    LIST("/albums.html"),

    DOUJIN("/albums-index-cate-5.html"),
    DOUJIN_CN("/albums-index-cate-1.html"),
    DOUJIN_JA("/albums-index-cate-12.html"),
    DOUJIN_CG("/albums-index-cate-2.html"),
    DOUJIN_COSPLAY("/albums-index-cate-3.html"),

    BOOKLET("/albums-index-cate-6.html"),
    BOOKLET_CN("/albums-index-cate-9.html"),
    BOOKLET_JA("/albums-index-cate-13.html"),

    MAGAZINE("/albums-index-cate-7.html"),
    MAGAZINE_CN("/albums-index-cate-10.html"),
    MAGAZINE_JA("/albums-index-cate-14.html"),
    ;
    private String index;
    WNAcgType(String index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return index;
    }

    public static WNAcgType menuIdToType(int id) {
        switch (id) {
            case R.id.nav_home:
                return HOME;

            case R.id.nav_doujin:
                return DOUJIN;
            case R.id.nav_doujin_cg:
                return DOUJIN_CG;
            case R.id.nav_doujin_cn:
                return DOUJIN_CN;
            case R.id.nav_doujin_cosplay:
                return DOUJIN_COSPLAY;
            case R.id.nav_doujin_ja:
                return DOUJIN_JA;

            case R.id.nav_boolet:
                return BOOKLET;
            case R.id.nav_boolet_cn:
                return BOOKLET_CN;
            case R.id.nav_boolet_ja:
                return BOOKLET_JA;

            case R.id.nav_magazine:
                return MAGAZINE;
            case R.id.nav_magazine_cn:
                return MAGAZINE_CN;
            case R.id.nav_magazine_ja:
                return MAGAZINE_JA;
            default:
                return null;
        }
    }

    public String withPage(int page) {
        String index = toString();
        if(page < 2){
            return index;
        }
        return index.replace("-cate-","-page-" + page + "-cate-");
    }
}
