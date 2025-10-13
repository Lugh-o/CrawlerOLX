package com.acelerazg.model

import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true, ignoreNulls = true)
class Listing {
    String title
    double price
    String address
    String url

    Listing(String title, double price, String address, String url) {
        this.title = title
        this.price = price
        this.address = address
        this.url = url
    }
}
