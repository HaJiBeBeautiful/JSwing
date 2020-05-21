package sd.jswing.pro.simplecrawler;

import sd.jswing.pro.simplecrawler.component.CrawlerJframe;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        CrawlerJframe crawler = new CrawlerJframe("爬虫工具");
        crawler.init();
        crawler.setVisible(true);
    }
}
