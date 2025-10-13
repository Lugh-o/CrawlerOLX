package com.acelerazg.utils

import com.acelerazg.Crawler
import com.acelerazg.model.Listing
import groovy.transform.CompileStatic

@CompileStatic
class CsvHandler {
    static void run() {
        Set<Listing> listings = Crawler.listings
        File outputDir = new File("out")
        if (!outputDir.exists()) outputDir.mkdirs()
        saveTasksToCsv(listings, "out/listings.csv")
    }

    static void saveTasksToCsv(Set<Listing> listings, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("title; price; address; url")
            writer.newLine()

            for (Listing l : listings) {
                String line = String.join(";",
                        l.title,
                        l.price.toString(),
                        l.address,
                        l.url)
                writer.write(line)
                writer.newLine()
            }
        }
    }
}
