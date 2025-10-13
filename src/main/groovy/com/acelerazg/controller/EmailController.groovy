package com.acelerazg.controller

import com.acelerazg.dao.EmailDAO
import com.acelerazg.model.Email
import groovy.transform.CompileStatic

@CompileStatic
class EmailController {
    private final EmailDAO emailDAO

    EmailController(EmailDAO emailDAO) {
        this.emailDAO = emailDAO
    }

    List<Email> handleGetAll() {
        return emailDAO.getAll()
    }

    Email handleGetById(int id) {
        return emailDAO.getById(id)
    }

    Email handleCreate(String address) {
        Email email = new Email(address)
        return emailDAO.create(email)
    }

    void handleDelete(int id) {
        emailDAO.delete(id)
    }
}