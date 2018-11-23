/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package property_auctions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javaapplication1.FirefoxInitializer;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author gksde
 */
public class PropertyScraper {

    public static List<String> getAuctionList(String url) throws InterruptedException, InterruptedException {
        WebDriver driver = FirefoxInitializer.getDriver();
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        List<String> urls = new ArrayList<>();
        driver.get(url);
        String pages = driver.findElement(By.xpath("/html/body/main/div[1]/div/div[1]/div[14]/ul/li[2]")).getAttribute("innerText").split("of")[1].replaceAll("\\D+", "").trim();
        System.out.println(pages + "_______________________________************");
        Thread.sleep(5000);
        for (int i = 0; i < Integer.parseInt(pages); i++) {

            List<WebElement> elems = driver.findElement(By.xpath("/html/body/main/div[1]/div/div[1]")).findElements(By.xpath("./*"));

//            System.out.println("Current url - " + driver.getCurrentUrl());
//            System.out.println("Item Found - " + elems.size());
            for (WebElement elm : elems) {
                System.out.println(elm.getAttribute("class"));
//                System.out.println(elm.findElements(By.xpath("./*")).get(0)
//                        .findElement(By.className("waves-effect"))
//                        .findElements(By.xpath("./*")).get(0)
//                        .getTagName());
                if (!elm.getAttribute("class").contains("searchBox") || !elm.getAttribute("class").contains("center-align")) {
                    try {

                        urls.add(elm.findElements(By.xpath("./*")).get(0)
                                .findElement(By.className("waves-effect"))
                                .findElement(By.tagName("a")).getAttribute("href"));

                    } catch (Exception e) {
                        System.err.println("Can Not Find href In Target.. Skipping ");
                        continue;
//                        try {
//                            urls.add(elm.findElements(By.xpath("./*")).get(0)
//                                    .findElement(By.className("waves-effect"))
//                                    .findElement(By.tagName("a")).getAttribute("href"));
//                        } catch (Exception s) {
//                            JOptionPane.showMessageDialog(null, "Page might be changed.. can not find url in target elements..");
//                        }
                        //System.err.println("Can Not Find href In Target.. Changing Target To ---> findElements(By.xpath(\"./*\")).get(0)");
                    }
                }
            }

            try {
                WebElement next = driver.findElement(By.xpath("/html/body/main/div[1]/div/div[1]/div[14]/ul/li[3]/a/i"));
                jse.executeScript("arguments[0].scrollIntoView();", next);
                Thread.sleep(10000);
                try {
                    next.click();
                } catch (StaleElementReferenceException v) {
                    jse.executeScript("arguments[0].scrollIntoView();", next);
                    Thread.sleep(10000);
                    next.click();
                }
                System.out.println("CURRENT PAGE IS NUMBER " + i);
            } catch (NoSuchElementException t) {
                System.err.print("Pages Redirection End..");
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                System.err.println("Interrupted error in 1st method thread... line 62");
            }
        }
        System.out.println("FOUND " + urls.size() + " LINKS IN SCRAPER CLASS....");
        return urls;
    }

    public static PropertyContent doM(String url) throws InterruptedException {
        WebDriver driver = FirefoxInitializer.getDriver();
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        driver.get(url);
        Thread.sleep(10000);
        PropertyContent content = null;
        String profile_url = "";
        String date = "";
        String title = "";
        String address = "";
        String latitude = "";
        String longitude = "";
        String city = "";
        String price = "";
        String seller = "";
        String phone = "";
        String images = "";
        String description = "";

        WebElement panel_one = driver.findElement(By.xpath("/html/body/main/div[1]/div/div[1]/div[1]"));
        profile_url = url;
        title = panel_one.findElements(By.xpath("./*")).get(0).getAttribute("innerText");
        price = panel_one.findElements(By.xpath("./*")).get(1).getAttribute("innerText").replace("£", "").replace(",", "");
        address = panel_one.findElements(By.xpath("./*")).get(2).getAttribute("innerText");
        int size = panel_one.findElements(By.xpath("./*")).get(2).getAttribute("innerText").split(",").length;
        int cityLength = panel_one.findElements(By.xpath("./*")).get(2).getAttribute("innerText").split(",").length;
        System.out.println("City - "+panel_one.findElements(By.xpath("./*")).get(2).getAttribute("innerText")+" length -- "+cityLength);
        if (cityLength > 2) {
            city = panel_one.findElements(By.xpath("./*")).get(2).getAttribute("innerText").split(",")[cityLength-2];
        } else {
            city = panel_one.findElements(By.xpath("./*")).get(2).getAttribute("innerText").split(",")[cityLength - 1];
            city = city.substring(0, city.length()-2);
        }

        date = panel_one.findElement(By.tagName("ul")).findElements(By.xpath("./*")).get(0).findElement(By.tagName("span")).getAttribute("innerText");
        latitude = panel_one.findElement(By.tagName("ul")).findElements(By.xpath("./*")).get(1).getAttribute("innerHTML")
                .split(">")[0].split(" ")[2].split("=")[1].replace("\"", " ").split(",")[0];
        longitude = panel_one.findElement(By.tagName("ul")).findElements(By.xpath("./*")).get(1).getAttribute("innerHTML")
                .split(">")[0].split(" ")[2].split("=")[1].replace("\"", " ").split(",")[1];

        // login
        if (driver.findElement(By.xpath("/html/body/main/div[1]/div/div[2]")).findElements(By.xpath("./*")).size() < 3) {
            String logMail = "jmsmyth99@gmail.com";
            String logpass = "balaclava5";
            WebElement signIn = driver.findElement(By.xpath("/html/body/header/div/div/div[2]/div/ul/li[2]"));
            jse.executeScript("arguments[0].scrollIntoView();", signIn);
            Thread.sleep(3000);
            signIn.click();
            WebElement email = driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div/div/div[3]/span/div/div/div/div/div/div/div/div/div[4]/div[1]/div/input"));
            WebElement password = driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div/div/div[3]/span/div/div/div/div/div/div/div/div/div[4]/div[2]/div/div/input"));
            WebElement submit = driver.findElement(By.xpath("/html/body/div/div/div[2]/form/div/div/button"));
            Thread.sleep(3000);
            email.sendKeys(new String[]{"jmsmyth99@gmail.com"});
            password.sendKeys(new String[]{"balaclava5"});
            Thread.sleep(5000);
            submit.click();
            Thread.sleep(15000);
            System.out.println("Email , Password Entered ...");
        }

// need to login first
        WebElement panel_two = driver.findElement(By.xpath("/html/body/main/div[1]/div/div[2]")).findElements(By.xpath("./*")).get(0);
        int length = panel_two.getAttribute("innerText").split("\n").length;
        phone = panel_two.getAttribute("innerText").split("\n")[length - 1];
        seller = panel_two.findElements(By.tagName("h6")).get(1).getAttribute("innerText");

// image links.. login is not required..        
        try {
            List<WebElement> imageLinks = driver.findElement(By.xpath("/html/body/main/div[1]/div/div[1]/div[2]/div[1]/ul")).findElements(By.xpath("./*"));
            Thread.sleep(2000);
//            for (WebElement imageLink : imageLinks) {
            for (int i = 0; i < imageLinks.size(); i++) {
                
                try {
                    String toString = imageLinks.get(i).findElements(By.xpath("./*")).get(0).getCssValue("background-image").toString();
                    if (i != (imageLinks.size()-1)) {
                        images = images + toString.split("\"")[1] + " ; ";     
                    }else{
                        images = images + toString.split("\"")[1] ;
                    }
                                   

                } catch (NoSuchElementException e) {
                    System.err.println("WARNINNG_____  -----> IMAGE NOT FOUND. ");
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("WARNINNG_____  -----> IMAGE NOT FOUND. ");
                }
            }
            System.out.println("IMAGE - " + images);
        } catch (NoSuchElementException e) {
            System.err.println("WARNINNG_____  -----> NO IMAGES FOR THIS PROPERTY... ");
        }
// description
        WebElement desc = null;
        try {
            desc = driver.findElement(By.xpath("/html/body/main/div[1]/div/div[1]/div[3]"));
        } catch (NoSuchElementException w) {
            System.err.println("WARNINNG_____  -----> CAN NOT LOCATE DESCRIPTION ELEMENT.. \n CHANGING XPATH ");
            desc = driver.findElement(By.xpath("/html/body/main/div[1]/div/div[1]/div[2]"));
        }
        description = desc.getAttribute("innerHTML");
//        if (desc.getAttribute("innerText").toLowerCase().contains("description")) {
//            description = desc.getAttribute("innerHTML").toLowerCase().split("description")[1].split("<br>")[1];
//            System.out.println(desc.getAttribute("innerHTML").toLowerCase().split("description")[1].split("<br>")[1] + "_____________________SPLITED");
//// write something to split description
//
//        } else {
//            description = desc.getAttribute("innerText").toLowerCase();
//        }
        System.out.println("______________________________________________________________________________________\n");
        System.out.println(seller);
        System.out.println("______________________________________________________________________________________\n");
        System.out.println("______________________________________________________________________________________\n");
        System.out.println(city);
        System.out.println("______________________________________________________________________________________\n");

        content = new PropertyContent();
        content.setAddress(address);
        content.setCity(city);
        content.setDate(date);
        content.setDescription(description);
        content.setImages(images);
        content.setLatitude(latitude);
        content.setLongitude(longitude);
        content.setPhone(phone);
        content.setPrice(price);
        content.setSeller(seller);
        content.setTitle(title);
        content.setUrl(url);
        return content;
    }
}
