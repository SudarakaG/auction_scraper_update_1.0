/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gibson_auctions;

import javaapplication1.*;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * @author Eminda
 */
public class AuctionScraper {
    
    private static String auctionVenue = "";
    private static String auctionDate = "";
    private static String auctionTime = "";

    public static List<String> getAuctionList(String url) {
        WebDriver driver = FirefoxInitializer.getDriver();
        driver.get(url);
        Document doc = Jsoup.parse(driver.getPageSource());
        Element body = doc.body();
        
        JavascriptExecutor js = (JavascriptExecutor) driver;

        List<String> urls = new ArrayList<>();
        
        WebElement elmAuctionDetails = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[1]/div[1]/p"));
        String auctionDetails = elmAuctionDetails.getAttribute("innerText");
        
//        System.out.println("+++++"+auctionDetails);

        auctionVenue = auctionDetails.split("Entry Deadline")[0].trim();
        auctionDate = auctionDetails.split("Auction Date: ")[1].split("Auction Start Time: ")[0];
        auctionDate = formatDate(auctionDate);
        auctionTime = auctionDetails.split("Auction Date: ")[1].split("Auction Start Time: ")[1];
        
//        auctionVenue = auctionDetails.split("<br><br>")[1].replace("<strong>", "").replace("<br>", ",");
        System.out.println("*****"+auctionVenue);
//        
//        auctionDate = auctionDetails.split("<br><br>")[2].split("</b>")[1].split("<br>")[0];
        System.out.println("*****"+auctionDate);
//        
//        auctionTime = auctionDetails.split("<br><br>")[2].split("</b>")[3].split("<br>")[0];
        System.out.println("*****"+auctionTime);

        int noOfPages = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[1]/span[4]")).getAttribute("innerText").split("of")[1].trim());
        for (int i = 0; i < noOfPages; i++) {

            List<WebElement> elems = driver.findElements(By.id("property_box"));
            for (WebElement elm : elems) {
                
                try {
                    urls.add(elm.findElement(By.xpath("./table/tbody/tr/td[1]/a")).getAttribute("href"));
                    System.out.println(elems.size() + " elements..");
                    System.out.println("*** " + elm.findElement(By.xpath("./table/tbody/tr/td[1]/a")).getAttribute("href"));
                } catch (Exception e) {
                }                
                
            }
            int pageNo = Integer.parseInt(driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[1]/span[4]")).getAttribute("innerText").split("of")[0].replace("Page ", "").trim());
            
            if (pageNo == noOfPages) {
                continue;
            }
            
            WebElement nxtbtn = null;
            if (pageNo == 1) {
                nxtbtn = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[1]/span[3]/a"));
            }else{
                nxtbtn = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[2]/div[1]/span[3]/a[2]"));
            }
            js.executeScript("arguments[0].scrollIntoView();",nxtbtn );	
            nxtbtn.click();

        }

        return urls;
    }

    public static AuctionContent doM(String url) throws InterruptedException {
        WebDriver driver = FirefoxInitializer.getDriver();
        driver.get(url);
        Document doc = Jsoup.parse(driver.getPageSource());
        Element body = doc.body();

        AuctionContent auctionContent = new AuctionContent();

        auctionContent.setUrl(url);
        
        auctionContent.setAuctionVenue(auctionVenue);
        auctionContent.setAuctionDate(auctionDate);
        auctionContent.setAuctionTime(auctionTime);

        WebElement elmAddress = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/table/tbody/tr[2]/td/table/tbody/tr[3]/td/table/tbody/tr[1]/td/table/tbody/tr[4]/td[2]/span"));
        System.out.println("Address : " + elmAddress.getAttribute("innerText"));
        auctionContent.setAddress(elmAddress.getAttribute("innerText"));

        String[] splitAddress = elmAddress.getAttribute("innerText").split(",");
        System.out.println("City : " + splitAddress[(splitAddress.length - 2)]);
        auctionContent.setCity(splitAddress[(splitAddress.length - 2)]);

        WebElement elmPrice = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/table/tbody/tr[2]/td/table/tbody/tr[3]/td/table/tbody/tr[1]/td/table/tbody/tr[1]/td[2]/table/tbody/tr/td[2]/font[1]"));
        System.out.println("Price : " + elmPrice.getAttribute("innerText").split("£")[1]);
        auctionContent.setPrice(elmPrice.getAttribute("innerText").split("£")[1].replace(",", ""));

        auctionContent.setSeller("Gibsons auctions");
        auctionContent.setPhone("028 9039 3966");

//        WebElement elmImg = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/table/tbody/tr[2]/td/table/tbody/tr[3]/td/table/tbody/tr[1]/td/table/tbody/tr[6]/td[2]/table/tbody/tr[1]/td"));
//        System.out.println("Image : " + elmImg.findElement(By.tagName("img")).getAttribute("src"));
//        auctionContent.setImage(elmImg.findElement(By.tagName("img")).getAttribute("src"));
        List<WebElement> imgList = driver.findElement(By.className("thumbs")).findElements(By.tagName("a"));
        String image = "";
//        for (WebElement img : imgList) {
        for (int i = 0; i < imgList.size(); i++) {
            if (i != (imgList.size()-1)) {
                image += imgList.get(i).getAttribute("href")+" ; ";
            }else{
                image += imgList.get(i).getAttribute("href");
            }
            
        }
        System.out.println("Images : "+image);
        auctionContent.setImage(image);

        WebElement elmFeatures = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/table/tbody/tr[2]/td/table/tbody/tr[3]/td/table/tbody/tr[1]/td/table/tbody/tr[17]/td[2]"));
        List<WebElement> features = elmFeatures.findElements(By.xpath("./li"));
        String proFeturs = "";
        for (WebElement feature : features) {
            proFeturs = proFeturs + feature.getAttribute("innerText") + "\n";
        }
        System.out.println("Features : " + proFeturs);
        auctionContent.setFeatures(proFeturs);

        WebElement elmDisclaim = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/table/tbody/tr[2]/td/table/tbody/tr[3]/td/table/tbody/tr[5]/td/p"));
        System.out.println("Disclaimer : " + elmDisclaim.getAttribute("innerText"));
        auctionContent.setDisclaimer(elmDisclaim.getAttribute("innerText"));

        WebElement elmDesc = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/table/tbody/tr[2]/td/table/tbody/tr[3]/td/table/tbody/tr[1]/td/table/tbody/tr[21]/td[2]"));
//        System.out.println("Description : "+elmDesc.getAttribute("innerHTML"));
        String description = elmDesc.getAttribute("innerHTML").replace("<br> <br>", "<br><br>");
        try {

            String[] splitDesc = description.split("<br><br>", 2);
            if (!splitDesc[0].equalsIgnoreCase("")) {
                System.out.println("Description : " + splitDesc[0]);
                System.out.println("Comprises : " + splitDesc[1].replace("<br>", "\n").replace("<b>", "").replace("</b>", "").replace("<u>", "").replace("</u>", "").replace("<i>", "").replace("</i>", "").replace("•	", ""));

                auctionContent.setDescription(splitDesc[0].trim());
                auctionContent.setAccomadation(splitDesc[1].trim().replace("<br>", "\n").replace("<b>", "").replace("</b>", "").replace("<u>", "").replace("</u>", "").replace("<i>", "").replace("</i>", "").replace("•	", ""));
            }else{
                auctionContent.setDescription(elmDesc.getAttribute("innerText"));
            }
            
        } catch (Exception e) {
            auctionContent.setDescription(elmDesc.getAttribute("innerText"));
            System.out.println("No Comprises..");
        }

//        String desc = description.split("Accommodation Comprises :")[0];
//        System.out.println("Description : "+desc);
//        System.out.println("Comprises : "+description.split("Accommodation Comprises :")[1]);
        
        String viewTime = driver.findElement(By.xpath("/html/body/div[1]/div/div[2]/table/tbody/tr[2]/td/table/tbody/tr[3]/td/table/tbody/tr[1]/td/table/tbody/tr[13]/td[2]/font")).getAttribute("innerText");
        

        return auctionContent;
    }

    private static String formatDate(String auctionDate) {
        String month = auctionDate.split(" ")[1];
        String m = "";
        if (month.equalsIgnoreCase("January")) {
            m = "01";
        }else if (month.equalsIgnoreCase("February")) {
            m = "02";
        }else if (month.equalsIgnoreCase("March")) {
            m = "03";
        }else if (month.equalsIgnoreCase("April")) {
            m = "04";
        }else if (month.equalsIgnoreCase("May")) {
            m = "05";
        }else if (month.equalsIgnoreCase("June")) {
            m = "06";
        }else if (month.equalsIgnoreCase("July")) {
            m = "07";
        }else if (month.equalsIgnoreCase("August")) {
            m = "08";
        }else if (month.equalsIgnoreCase("September")) {
            m = "09";
        }else if (month.equalsIgnoreCase("October")) {
            m = "10";
        }else if (month.equalsIgnoreCase("November")) {
            m = "11";
        }else {
            m = "12";
        }
        
        String date = auctionDate.split(" ")[0]+" "+m+" "+auctionDate.split(" ")[2];
        
        return date;
    }

}
