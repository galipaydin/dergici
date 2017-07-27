/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.buyukveri.dergici.hindawi;

import java.io.File;
import java.io.FileWriter;
import org.buyukveri.common.WebPageDownloader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author galip
 */
public class JournalFetcher {

    public String folder = "/Users/galip/dev/data/dergici/hindawi";

    public void fetchHindawi() {
        try {
            String[] journals = {"jp",
                "jphar",
                "jr",
                "jvm",
                "js",
                "jo",
                "jobe",
                "aag",
                "ace",
                "aee",
                "amed",
                "amete",
                "ase",
                "complexity",
                "ijecol",
                "ijz",
                "jac"};

            for (String journal : journals) {
                getArticleLinks((String) journal, "https://www.hindawi.com/journals/" + (String) journal + "/contents");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void getArticleLinks(String journalName, String link) {
        try {
            journalName = journalName.toLowerCase().replaceAll(" ", "");

            String path = folder + "/" + journalName;
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }

            //kaç sayfa indirilecek? her sayfada 25 makale var
            for (int i = 1; i < 2; i++) {
                String contentslink = link + "/" + i;
                System.out.println(contentslink);
                Document doc = WebPageDownloader.getPage(contentslink);
                if (doc.getElementsByAttributeValueContaining("class", "middle_content") != null) {
                    Element cont = doc.getElementsByAttributeValueContaining("class", "middle_content").first();
                    Elements as = cont.getElementsByTag("a");
                    int cnt = 0;
                    for (Element a : as) {
                        String alink = a.attr("href");
                        if (alink.endsWith("/")) {
                            alink = alink.substring(0, alink.length() - 1);
                        }
                        String id = alink.substring(alink.lastIndexOf("/") + 1, alink.length());
                        if (id.length() == 7) {
                            String title = a.text();
//                            System.out.println(cnt++ + " - " + id + " - " + alink);
                            downloadArticle(journalName, path, id, title, "https://www.hindawi.com" + alink);
                        }
                    }
                } else {
                    System.out.println("NULL");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void downloadArticle(String journalName, String path, String id, String title, String link) {
        try {
            Document doc = WebPageDownloader.getPage(link);
            Element auth = doc.getElementsByAttributeValueContaining("class", "author_gp").first();
            Elements as = auth.getElementsByTag("a");
            FileWriter fw = new FileWriter(path + "/" + journalName + "_" + id + ".txt");
            fw.write("#&;title=" + title + "\n");
            fw.flush();

            String authors = "";
            for (Element a : as) {
                authors += a.text() + ";";
            }
            if (authors.endsWith(";")) {
                authors = authors.substring(0, authors.length() - 1);
            }

            fw.write("#&;authors=" + authors + "\n");
            fw.flush();
            fw.write("#&;content=\n");
            fw.flush();

            Elements cont = doc.getElementsByAttributeValueContaining("class", "xml-content");

            if (cont.size() == 3) {
                String content = cont.get(1).text();
                fw.write(content);
                fw.flush();
                fw.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        JournalFetcher j = new JournalFetcher();
        j.fetchHindawi();
//        j.getArticleLinks("js", "https://www.hindawi.com/journals/js/contents");
//        j.downloadArticle("https://www.hindawi.com/journals/js/2017/7879198/");
    }
}
