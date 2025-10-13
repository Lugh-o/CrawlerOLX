package com.acelerazg.model

import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString(includeSuperProperties = true, includePackage = false, includeNames = true, ignoreNulls = true)
class Email {
    int id
    String email

    Email(String email) {
        this.email = email
    }

    Email(int id, String email) {
        this.id = id
        this.email = email
    }
}
