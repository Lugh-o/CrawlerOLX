package com.acelerazg

import com.acelerazg.model.Listing
import com.acelerazg.persistency.DatabaseHandler
import com.acelerazg.utils.CsvHandler
import com.acelerazg.utils.EmailHandler
import com.acelerazg.utils.EnvHandler
import groovy.transform.CompileStatic

@CompileStatic
class Main{
    static void main(String[] args) {
        EnvHandler.loadEnv()
        if (!DatabaseHandler.testConnection()) return

        Set<Listing> listings = Crawler.run("iphone 11")
        CsvHandler.run()
        EmailHandler.run()

        println "Listing with lowest price: " + listings[0]
        println "Listing with highest price: " + listings[listings.size() - 1]
    }
}
