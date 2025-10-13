package com.acelerazg.persistency

import com.acelerazg.utils.EnvHandler
import groovy.transform.CompileStatic

@CompileStatic
class Migrate {
    static void main(String[] args) {
        EnvHandler.loadEnv()
        DatabaseHandler.runSql("db/schema.sql")
    }
}