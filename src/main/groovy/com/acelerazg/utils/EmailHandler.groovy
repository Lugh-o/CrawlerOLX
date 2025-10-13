package com.acelerazg.utils

import com.acelerazg.Crawler
import com.acelerazg.controller.EmailController
import com.acelerazg.dao.EmailDAO
import com.acelerazg.model.Email
import com.acelerazg.model.Listing
import groovy.transform.CompileStatic

import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

@CompileStatic
class EmailHandler {
    static void run() {
        Set<Listing> listings = Crawler.listings

        EmailDAO emailDAO = new EmailDAO()
        EmailController emailController = new EmailController(emailDAO)
        List<Email> emailList = emailController.handleGetAll()
        emailList.forEach { Email e -> sendEmail(e.email, listings) }
    }

    static void sendEmail(String recipientAddress, Set<Listing> listings) {
        final String username = EnvHandler.get('MAIL_USERNAME')
        final String password = EnvHandler.get('MAIL_PASSWORD')

        Properties props = new Properties()
        props.put("mail.smtp.auth", "true")
        props.put("mail.smtp.starttls.enable", "true")
        props.put("mail.smtp.host", EnvHandler.get('MAIL_HOST'))
        props.put("mail.smtp.port", EnvHandler.get('MAIL_PORT'))

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password)
            }
        })

        try {
            Message message = new MimeMessage(session)
            message.setFrom(new InternetAddress(EnvHandler.get('MAIL_ADDRESS')))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientAddress))
            message.setSubject("Webscraper OLX – Anúncios Encontrados")

            Multipart multipart = new MimeMultipart()

            MimeBodyPart textContent = new MimeBodyPart()

            StringBuilder html = new StringBuilder()
            html << "<h3>Foram encontrados os seguintes anúncios:</h3>"
            html << "<ul style='font-family:Arial, sans-serif;'>"
            for (Listing l : listings) {
                html << "<li><a href='${l.url}' target='_blank'>${l.title}; R\$ ${l.price}; ${l.address}</a></li>"
            }
            html << "</ul>"
            html << "<p style='font-size:12px;color:gray;'>Mensagem automática do Webscraper OLX.</p>"

            textContent.setContent(html.toString(), "text/html; charset=utf-8")
            multipart.addBodyPart(textContent)

            message.setContent(multipart)

            Transport.send(message)
            println "E-mail enviado com sucesso para ${recipientAddress}"

        } catch (MessagingException e) {
            println "Erro ao enviar e-mail: ${e.message}"
            e.printStackTrace()
        }
    }
}
