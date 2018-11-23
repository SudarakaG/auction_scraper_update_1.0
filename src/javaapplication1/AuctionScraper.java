/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author Eminda
 */
public class AuctionScraper {

    public static List<String> getAuctionList(String url) {
        WebDriver driver = ChromeDriverInitialize.getDriver();
        driver.get(url);
        Document doc = Jsoup.parse(driver.getPageSource());
        Element body = doc.body();

        List<WebElement> elems = driver.findElements(By.xpath("/html/body/section/div[4]/div/div[4]/div"));

        List<String> urls = new ArrayList<>();

        for (WebElement elm : elems) {
            try {
                urls.add(elm.findElement(By.tagName("a")).getAttribute("href"));
            } catch (Exception e) {
            }
        }

        return urls;
    }
    
    public static AuctionContent doM(String url) throws InterruptedException{
         WebDriver driver = ChromeDriverInitialize.getDriver();
        driver.get(url);
        Document doc = Jsoup.parse(driver.getPageSource());
        Element body = doc.body();

        AuctionContent auctionContent = new AuctionContent();
        auctionContent.setUrl(url);
        auctionContent.setAddress(driver.findElement(By.xpath("/html/body/section/div[1]/div[1]/div/div/div/div[1]/p/b")).getText());
        System.out.println(driver.findElement(By.xpath("/html/body/section/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[3]/h4/span")).getText());
//        String[] adds = driver.findElement(By.xpath("/html/body/div[1]/div/div/div[9]/div/div/div/div[1]/div[2]")).getText().split(",");
//        auctionContent.setCity(adds[adds.length - 2]);£
        String price=driver.findElement(By.xpath("/html/body/section/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[3]/h4/span")).getText().trim();
        int index=price.indexOf("£");
        if(index!=-1) {
            System.out.println("PP" + price);
            price = price.substring(index+1).trim();
            System.out.println(price);
            index = price.indexOf(" ");
            if (index != -1) {
                price = price.substring(0, index);
            }
        }
        auctionContent.setPrice(price.replace(",", ""));
        auctionContent.setSeller(driver.findElement(By.xpath("/html/body/div[2]/div/div/div[1]/a/h2")).getText());
        auctionContent.setPhone(driver.findElement(By.xpath("/html/body/section/div[1]/div[2]/div[2]/div[1]/div[1]/div[2]/div/p/b")).getText());
        
        List<WebElement> imageList=driver.findElement(By.xpath("/html/body/section/div[1]/div[2]/div[2]/div[2]/div/div[2]")).findElements(By.tagName("img"));
        String images="";
        int i=0;
        for(WebElement image:imageList){
            System.out.println(image.getAttribute("innerHTML"));
            String imageUrl=image.getAttribute("src");
            int t=imageUrl.indexOf("?");
            if(t!=-1){
                imageUrl=imageUrl.split("\\?")[0];
            }
            images+=imageUrl;
            if(++i<imageList.size()){
                images+=",";
            }
            
        }
        auctionContent.setImage(images);
        auctionContent.setAuctionVenue(driver.findElement(By.xpath("/html/body/section/div[1]/div[2]/div[2]/div[1]/div[4]/div[1]/p[2]")).getText());
        auctionContent.setAuctionTime(driver.findElement(By.xpath("/html/body/section/div[1]/div[2]/div[2]/div[1]/div[4]/div[1]/div[2]/p[2]")).getText());
        auctionContent.setDescription(driver.findElement(By.xpath("/html/body/section/div[1]/div[2]/div[2]/div[2]/div/div[4]")).getAttribute("innerHTML"));
        auctionContent.setAuctionDate(driver.findElement(By.xpath("/html/body/section/div[1]/div[2]/div[2]/div[1]/div[4]/div[1]/div[1]/p[2]")).getText());
//        auctionContent.setTenure(driver.findElement(By.xpath("/html/body/section/div[1]/div[2]/div[2]/div[1]/div[1]/div[1]/div[3]/ul/li[2]")).getText().replace("Tenure:","").trim());

//        WebElement iFrame=driver.findElement(By.xpath("//*[@id=\"iFrameResizer0\"]"));
        String ads[]=auctionContent.getAddress().split(",");
        auctionContent.setCity(ads[ads.length-2]);

        WebElement allDes=driver.findElement(By.xpath("/html/body/section/div[1]/div[2]/div[2]/div[2]/div/div[4]"));

        List<WebElement> pTags=allDes.findElements(By.tagName("p"));

        //set tenoure
        boolean scrape=false;
        String s="";
        for(WebElement elm:pTags){
            if(elm.getAttribute("class").trim().equals("auction-info-header") && scrape){
                break;
            }
            if(scrape){
                s+=elm.getText();
            }
            if(elm.getText().toLowerCase().contains("tenure")){
                scrape=true;
            }
        }
        auctionContent.setTenure(s);

        //connectedperspn
        scrape=false;
        s="";
        for(WebElement elm:pTags){
            if(elm.getAttribute("class").trim().equals("auction-info-header") && scrape){
                break;
            }
            if(scrape){
                s+="<p>"+elm.getText()+"</p>";
            }
            if(elm.getText().toLowerCase().contains("connected")){
                scrape=true;
            }
        }
        auctionContent.setConnectedPerson(s);
        //additional
        scrape=false;
        s="";
        for(WebElement elm:pTags){
            if(elm.getAttribute("class").trim().equals("auction-info-header") && scrape){
                break;
            }
            if(scrape){
                s+="<p>"+elm.getText()+"</p>";
            }
            if(elm.getText().toLowerCase().contains("additional fee")){
                scrape=true;
            }
        }
        auctionContent.setAdditional(s);

        //energy
        scrape=false;
        s="";
        for(WebElement elm:pTags){
            if(elm.getAttribute("class").trim().equals("auction-info-header") && scrape){
                break;
            }
            if(scrape){
                s+="<p>"+elm.getText()+"</p>";
            }
            if(elm.getText().toLowerCase().contains("energy efficiency rating")){
                scrape=true;
            }
        }
        auctionContent.setEnergyEfficientRate(s);




        return auctionContent;
    }


}
