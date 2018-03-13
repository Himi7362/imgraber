package himi.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	ArrayList<String> dotaHeroImgs=null;//全部英雄
//    	Document doc =  Jsoup.connect("http://fight.pcgames.com.cn/warcraft/dota/heros").get();
    	Document doc =  Jsoup.connect("http://dota.uuu9.com/hero").get();
    	String title = doc.title();
//    	System.out.println(title);
//    	List elements = doc.select("img[src~=(?i)\\.(png|jpe?g)]");//列出了所有的img
    	int i=0;
    	List elements = doc.select("ul.cl.con.picbox li a");
//    	List elements = doc.getElementsByClass("ulPic");
    	
    	
    	for (Object obj : elements) {
			System.out.println(++i+obj.toString());
			if(i>=76){
				if(i==76){
					System.out.println("\n====斧王开始===="+obj.toString()+"\n");
					dotaHeroImgs = new ArrayList<String>();
				}
				dotaHeroImgs.add(obj.toString());

			}
			
		}
    	if(dotaHeroImgs!=null)
			System.out.println("\n\nuuu9英雄数"+dotaHeroImgs.toString());
    	int j=0;
    	for (Object obj : dotaHeroImgs) {
			System.out.println("全部英雄"+(++j)+obj.toString());//36力量  35敏捷 39智力
		}
//    	File f1 = new File("")
    	
    }
}
