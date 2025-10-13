package com.acelerazg

import com.acelerazg.model.Listing
import groovy.transform.CompileStatic
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

@CompileStatic
class Crawler {
    static Set<Listing> listings = []

    static Set<Listing> run(String query, String state = "go", String region = "grande-goiania-e-anapolis/grande-goiania") {
        query = query.trim()
        listings = []
        setListings(query, state, region)
        listings.removeIf { Listing l -> l.price > getAveragePrice() }
        return listings
    }

    static void setListings(String query, String state, String region) {
        for (int i = 1; i < 4; i++) {
            String url = setUrlPage(query, i, state, region)
            Document document = request(url)
            if (document == null) {
                println "Request failed on url: ${url}"
                return
            }
            for (Element link : document.select(".AdListing_adListContainer__ALQla a[href]")) {
                String nextLink = link.absUrl("href")
                extractListing(nextLink, query)
            }
        }
    }

    static void extractListing(String url, String query) {
        Document listingDocument = request(url)
        if (listingDocument == null) {
            println "Request failed on url: ${url}"
            return
        }
        String title = listingDocument.select("#description-title span.ad__sc-1l883pa-2")[0].text()
        if (title =~ "(?i)(?<=^|\\n).*${query}.*") {
            Elements addressElements = listingDocument.select("#location span")
            String address = addressElements[1].text() + ", " + addressElements[2].text()
            double price = listingDocument.select("#price-box-container span.olx-text--title-large")[0].text()
                    .trim()
                    .substring(2)
                    .replace(".", "")
                    .toDouble()
            Listing listing = new Listing(title, price, address, url)
            listings.add(listing)
        }
    }

    static double getAveragePrice() {
        int amount = listings.size()
        if (amount == 0) throw new Exception("No listings found")
        double sum = 0
        for (Listing l : listings) {
            sum += l.price
        }
        return sum / amount
    }

    static String setUrlPage(String query, int page, String state, String region) {
        if (page > 3 || page < 1) return null
        query = query.replace(" ", "%20")
        return "https://www.olx.com.br/estado-${state}/${region}?q=${query}&sp=1&o=${page.toString()}"
    }

    static Document request(String url) {
        try {
            return Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) " + "Chrome/128.0.0.0 Safari/537.36")
                    .referrer("https://www.google.com/")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .header("Cache-Control", "no-cache")
                    .timeout(20000)
                    .ignoreHttpErrors(true)
                    .get()
        } catch (HttpStatusException e) {
            println "HTTP Error: ${e.statusCode} - $url"
            return null
        } catch (IOException e) {
            println "Conection Error: $url - ${e.message}"
            return null
        }
    }
}