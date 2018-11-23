/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pugh_auctions;

import java.util.ArrayList;
import java.util.List;
import javaapplication1.FirefoxInitializer;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author dhanushka
 */
public class PughScraper {

    public static List<String> getAuctionList(String url) throws InterruptedException, InterruptedException {
        WebDriver driver = FirefoxInitializer.getDriver();
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        List<String> urls = new ArrayList<>();
        driver.get(url);
//        Thread.sleep(5000);
        System.out.println("GETTING URLS FROM " + url);
        List<WebElement> findElements = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[4]/div/div/div[2]")).findElements(By.xpath("./*"));
        for (WebElement findElement : findElements) {
            WebElement imgUrl = findElement.findElement(By.tagName("div")).findElement(By.className("listing-image"));
            System.out.println("COLLECTED URL - " + imgUrl.getAttribute("href"));
            urls.add(imgUrl.getAttribute("href"));
        }
        return urls;
    }

    public static PughContent doM(String url) throws InterruptedException {
        WebDriver driver = FirefoxInitializer.getDriver();
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        driver.get(url);
        Thread.sleep(10000);
        PughContent content = null;
        System.out.println("STARTING COLLECTING FROM " + url + "_________________________***");
        String profile_url = url;
        String date = "";
        String title = "";
        String address = "";
        String city = "";
        String price = "";
        String seller = "";
        String phone = "";
        String image = "";
        String description = "";
        String venue = "";
        String fees = "";
        String tenure = "";
        String outside = "";
        String planning = "";
        String general = "";
        String siteArea = "";
        String accommodation = "";

        address = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[5]/div/div/div[2]/div[2]/h1")).getAttribute("innerText");
        int addressLength = address.split(",").length;
        city = address.split(",")[addressLength - 3];
        try {
            price = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[5]/div/div/div[3]/div[3]/div[1]/div[1]/div/div[2]")).getAttribute("innerText").replaceAll("\\D+", "");
        } catch (NoSuchElementException e) {
            price = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[5]/div/div/div[3]/div[3]/div[1]/div[1]/div/div")).getAttribute("innerText").replaceAll("\\D+", "");
        }
//        image = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[5]/div/div/div[3]/div[1]/div[1]/a/img")).getAttribute("src");
        String images = "";
        try {
            List<WebElement> imgList = driver.findElement(By.className("slick-track")).findElements(By.className("slick-slide"));

//            for (WebElement img : imgList) {
            for (int i = 0; i < imgList.size(); i++) {
                if (i != (imgList.size()-1)) {
                    images += imgList.get(i).findElement(By.className("thumb-image")).getAttribute("href") + " ; ";
                }else{
                    images += imgList.get(i).findElement(By.className("thumb-image")).getAttribute("href");
                }
                
            }
            
        } catch (Exception e) {
            System.out.println("No Images..");
        }
//        System.out.println("Images : "+images);
//        WebElement auctionDetails = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[5]/div/div/div[3]/div[1]/ul/li[3]"));
        WebElement auctionDetails = driver.findElement(By.cssSelector("li.tab:nth-child(3) > a:nth-child(1)"));
        Thread.sleep(1000);
        auctionDetails.click();
        Thread.sleep(1000);
                                                        
        String venueData = driver.findElement(By.cssSelector("#auction-details > div:nth-child(1) > div:nth-child(2) > div:nth-child(2) > address:nth-child(2)")).getAttribute("innerText");
//        String venueData = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[5]/div/div/div[3]/div[1]/div[4]/div[3]/div/div/div[2]/address")).getAttribute("innerText");
        venue = venueData.split(",")[0];
        date = setDate(driver.findElement(By.cssSelector("#auction-details > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) > span:nth-child(2)")).getAttribute("innerText"));
//        date = setDate(driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[5]/div/div/div[3]/div[1]/div[4]/div[3]/div/div/div[1]/span[2]")).getAttribute("innerText"));
        try {
            fees = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[5]/div/div/div[3]/div[3]/div[1]/div[1]/a")).getAttribute("innerText");
        } catch (NoSuchElementException e) {
            System.err.println("No additional fees element found.. can be reserved item.. please check price to verify.");
        }

//        driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[5]/div/div/div[3]/div[1]/ul/li[1]")).click();
        driver.findElement(By.cssSelector("li.tab:nth-child(1) > a:nth-child(1) > span:nth-child(1)")).click();
        Thread.sleep(1000);
//        description = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[5]/div/div/div[3]/div[1]/div[4]/div[1]/div/div[2]/div[2]")).findElement(By.tagName("p")).getAttribute("innerText");
//        List<WebElement> details = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[1]/div[5]/div/div/div[3]/div[1]/div[4]/div[1]/div")).findElements(By.xpath("./*"));
        List<WebElement> details = driver.findElement(By.id("property-details")).findElement(By.className("inner")).findElements(By.xpath("./*"));
        for (WebElement detail : details) {
//
//            try {
//                System.out.println("Checking description titles --> " + detail.findElements(By.xpath("./*")).get(1).findElement(By.className("table")).getAttribute("innerText").replace("\n", "").trim());
//                if (detail.findElements(By.xpath("./*")).get(1).findElement(By.className("table")).getAttribute("innerText").replace("\n", "").trim().equals("Tenancy")) {
//                    tenure = detail.findElements(By.xpath("./*")).get(1).getAttribute("innerText");
//                    System.out.println("Setting tenancy _____________" + detail.findElements(By.xpath("./*")).get(1).getAttribute("innerText"));
//                }
//                if (detail.findElements(By.xpath("./*")).get(1).findElement(By.className("table")).getAttribute("innerText").replace("\n", "").trim().equals("Outside")) {
//                    outside = detail.findElements(By.xpath("./*")).get(1).getAttribute("innerText");
//                    System.out.println("Setting outside _____________" + detail.findElements(By.xpath("./*")).get(1).getAttribute("innerText"));
//                }
//                if (detail.findElements(By.xpath("./*")).get(1).findElement(By.className("table")).getAttribute("innerText").replace("\n", "").trim().equals("Planning")) {
//                    planning = detail.findElements(By.xpath("./*")).get(1).getAttribute("innerText");
//                    System.out.println("Setting planing _____________" + detail.findElements(By.xpath("./*")).get(1).getAttribute("innerText"));
//                }
//                if (detail.findElements(By.xpath("./*")).get(1).findElement(By.className("table")).getAttribute("innerText").replace("\n", "").trim().equals("General")) {
//                    general = detail.findElements(By.xpath("./*")).get(1).getAttribute("innerText");
//                    System.out.println("Setting general _____________" + detail.findElements(By.xpath("./*")).get(1).getAttribute("innerText"));
//                }
//                if (detail.findElements(By.xpath("./*")).get(1).findElement(By.className("table")).getAttribute("innerText").replace("\n", "").trim().equals("ApproxSiteArea")) {
//                    siteArea = detail.findElements(By.xpath("./*")).get(1).getAttribute("innerText");
//                    System.out.println("Setting Approx Site Area _____________" + detail.findElements(By.xpath("./*")).get(1).getAttribute("innerText"));
//                }
////                if (detail.findElements(By.xpath("./*")).get(1).findElement(By.className("table")).getAttribute("innerText").replace("\n", "").trim().equals("Accommodation")) {
////                    accommodation = detail.findElements(By.xpath("./*")).get(1).getAttribute("innerText");
////                    System.out.println("Setting Accommodation " + detail.findElements(By.xpath("./*")).get(1).getAttribute("innerText"));
////                }
//            } catch (Exception r) {
//                System.err.println("Skipped " + detail.getTagName());
//            }

//        
            if (detail.getTagName().equalsIgnoreCase("span")) {
                continue;
            }
            
        try {
             if (detail.findElement(By.xpath("./div[2]/div/div[2]/div")).getAttribute("innerText").equalsIgnoreCase("Description")) {
                description = detail.findElement(By.xpath("./div[2]/p")).getAttribute("innerText");
                 System.out.println("Description : "+description);
            }
             if (detail.findElement(By.xpath("./div[2]/div/div[2]/div")).getAttribute("innerText").equalsIgnoreCase("Tenancy")) {
                tenure = detail.findElement(By.xpath("./div[2]/p")).getAttribute("innerText");
                 System.out.println("Tenure : "+tenure);
            }
             if (detail.findElement(By.xpath("./div[2]/div/div[2]/div")).getAttribute("innerText").equalsIgnoreCase("Outside")) {
                outside = detail.findElement(By.xpath("./div[2]/p")).getAttribute("innerText");
                 System.out.println("Outside : "+outside);
            }
             if (detail.findElement(By.xpath("./div[2]/div/div[2]/div")).getAttribute("innerText").equalsIgnoreCase("Planning")) {
                planning = detail.findElement(By.xpath("./div[2]/p")).getAttribute("innerText");
                 System.out.println("Planning : "+planning);
            }
             if (detail.findElement(By.xpath("./div[2]/div/div[2]/div")).getAttribute("innerText").equalsIgnoreCase("General")) {
                general = detail.findElement(By.xpath("./div[2]/p")).getAttribute("innerText");
                 System.out.println("General : "+general);
            }
             if (detail.findElement(By.xpath("./div[2]/div/div[2]/div")).getAttribute("innerText").equalsIgnoreCase("ApproxSiteArea")) {
                siteArea = detail.findElement(By.xpath("./div[2]/p")).getAttribute("innerText");
                 System.out.println("Site Area : "+siteArea);
            }
             if (detail.findElement(By.xpath("./div[2]/div/div[2]/div")).getAttribute("innerText").equalsIgnoreCase("Accomadation")) {
                accommodation = detail.findElement(By.xpath("./div[2]/p")).getAttribute("innerText");
                 System.out.println("Accomadation : "+accommodation);
            }
//            #property-details > div:nth-child(1) > div:nth-child(2) > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > div:nth-child(1) 
            } catch (Exception r) {
                System.err.println("Skipped " + detail.getTagName());
            }
        }
        System.out.println("Url______________________________________________________________________________________\n");
        System.out.println(url);
        System.out.println("______________________________________________________________________________________\n");
        System.out.println("Address______________________________________________________________________________________\n");
        System.out.println(address);
        System.out.println("______________________________________________________________________________________\n");
        System.out.println("City______________________________________________________________________________________\n");
        System.out.println(city);
        System.out.println("______________________________________________________________________________________\n");
        System.out.println("Price______________________________________________________________________________________\n");
        System.out.println(price);
        System.out.println("______________________________________________________________________________________\n");
        System.out.println("1st Image______________________________________________________________________________________\n");
        System.out.println(images);
        System.out.println("______________________________________________________________________________________\n");
        System.out.println("Venue______________________________________________________________________________________\n");
        System.out.println(venue);
        System.out.println("______________________________________________________________________________________\n");
        System.out.println("Date______________________________________________________________________________________\n");
        System.out.println(date);
        System.out.println("______________________________________________________________________________________\n");
        System.out.println("Fees______________________________________________________________________________________\n");
        System.out.println(fees);
        System.out.println("______________________________________________________________________________________\n");
        System.out.println("Desc______________________________________________________________________________________\n");
        System.out.println(description);
        System.out.println("______________________________________________________________________________________\n");
        System.out.println("Tenure______________________________________________________________________________________\n");
        System.out.println(tenure);
        System.out.println("______________________________________________________________________________________\n");

        content = new PughContent();
        content.setAddress(address);
        content.setCity(city);
        content.setDate(date);
        content.setDescription(description);
        content.setImage(images);
        content.setPhone(phone);
        content.setPrice(price);
        content.setSeller(seller);
        content.setProfile_url(url);
        content.setFees(fees);
        content.setGeneral(general);
        content.setOutside(outside);
        content.setPlanning(planning);
        content.setSiteArea(siteArea);
        content.setTenure(tenure);
        content.setVenue(venue);

        return content;
    }

    private static String setDate(String date) {
        String day = date.split(" ")[1].replace(" ", "").replaceAll("\\D+", "");
        String month = date.split(" ")[2].replace(" ", "");
        String year = date.split(" ")[3].replace(" ", "");
        System.out.println("setting date -> " + month);
        if (month.equals("January")) {
            month = "01";
        } else if (month.equals("February")) {
            month = "02";
        } else if (month.equalsIgnoreCase("March")) {
            month = "03";
        } else if (month.equalsIgnoreCase("April")) {
            month = "04";
        } else if (month.equalsIgnoreCase("May")) {
            month = "05";
        } else if (month.equalsIgnoreCase("June")) {
            month = "06";
        } else if (month.equalsIgnoreCase("July")) {
            month = "07";
        } else if (month.equalsIgnoreCase("August")) {
            month = "08";
        } else if (month.equalsIgnoreCase("September")) {
            month = "09";
        } else if (month.equalsIgnoreCase("October")) {
            month = "10";
        } else if (month.equalsIgnoreCase("November")) {
            month = "11";
        } else if (month.equals("December")) {
            month = "12";
        }
        System.out.println(day);
        System.out.println(month);
        System.out.println(year);
        return month + "/" + day + "/" + year;
    }
}
