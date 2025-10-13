package com.acelerazg.persistency

import com.acelerazg.controller.EmailController
import com.acelerazg.dao.EmailDAO
import com.acelerazg.model.Email
import com.acelerazg.utils.EnvHandler
import groovy.transform.CompileStatic

@CompileStatic
class DatabaseCLI {
    static void main(String[] args) {
        EnvHandler.loadEnv()
        if (!DatabaseHandler.testConnection()) return
        EmailDAO emailDAO = new EmailDAO()
        EmailController emailController = new EmailController(emailDAO)

        String appInput = ""
        try (Scanner appScanner = new Scanner(System.in)) {
            while (appInput != "q") {
                println "Insert the desired operation:\n" + "1 - List all emails\n" + "2 - Insert an email\n" + "3 - Delete an email\n" + "q - Exit the application\n"
                appInput = appScanner.nextLine()
                switch (appInput) {
                    case "1":
                        emailController.handleGetAll().forEach { println it }
                        break
                    case "2":
                        String email = readNonEmptyEmail(appScanner, "Insert the new email: ", "Invalid input, try again.")
                        Email createdEmail = emailController.handleCreate(email)
                        if (createdEmail) {
                            println "Email ${createdEmail.email} inserted successfully!"
                        } else {
                            println "An error occurred. Try again."
                        }
                        break
                    case "3":
                        List<Email> emailList = emailController.handleGetAll()
                        emailList.forEach { println it }
                        int id = readId(appScanner, emailList, "Insert the email id:")
                        emailController.handleDelete(id)
                        println "Email deleted successfully!"
                        break
                }
            }
        }
    }

    static String readNonEmptyEmail(Scanner scanner, String prompt, String errorMessage) {
        String regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/
        while (true) {
            println prompt
            String input = scanner.nextLine().trim()
            if (input.size() > 255 || input.size() <= 0 || !(input =~ regex)) {
                println errorMessage
                continue
            }
            return input
        }
    }

    static <T> int readId(Scanner scanner, List<T> list, String message) {
        while (true) {
            println message
            try {
                int id = scanner.nextLine().toInteger()
                T object = list.stream()
                        .filter(c -> c["id"] == id)
                        .findFirst()
                        .orElse(null)
                if (!object) throw new Exception()
                return id
            } catch (Exception ignored) {
                println "Invalid Id. Try Again"
            }
        }
    }
}