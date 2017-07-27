/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.common;

import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author galip
 */
public class WebPageDownloader {

    public static Document getPage(String url) {
        try {

            Response response = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                    .referrer("http://www.google.com")
                    .timeout(12000)
                    .followRedirects(true)
                    .execute();
            Document doc = response.parse();
            return doc;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
//        finally {
//            try {
//                is.close();
//            } catch (IOException ioe) {
//                System.out.println(ioe.getMessage());
//            }
//        }
    }

    public static Document getFile(String filePath) {
        URL u;
        InputStream is = null;
        DataInputStream dis;
        String s = "";
        try {
            Document doc = Jsoup.parse(new File(filePath), "UTF-8");
            //System.out.println(doc.title());
            return doc;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    public static void main(String[] args) {
//        WebPageDownloader.getFile("/Users/galip/NetBeansProjects/NewsDownloader/src/main/resources/commentspage.html");
      Document doc =  WebPageDownloader.getPage("http://finans.mynet.com/haber/detay/borsa/yatirimciya-kirmizi-bayrakli-uyari/334/");
        System.out.println(doc.html());
    }
}
