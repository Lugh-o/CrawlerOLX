package com.acelerazg.dao

import com.acelerazg.model.Email
import com.acelerazg.persistency.DatabaseHandler
import groovy.transform.CompileStatic

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

@CompileStatic
class EmailDAO {
    List<Email> getAll() {
        String sql = "SELECT * FROM email_list;"

        Connection connection = null
        PreparedStatement statement = null
        ResultSet response = null
        List<Email> emails = []

        try {
            connection = DatabaseHandler.getConnection()
            statement = connection.prepareStatement(sql)
            response = statement.executeQuery()
            while (response.next()) {
                emails.add(new Email(
                        response.getInt("id"),
                        response.getString("email")))
            }
        } finally {
            DatabaseHandler.closeQuietly(response, statement, connection)
        }
        return emails
    }

    Email getById(int id) {
        String sql = """
            SELECT * FROM email_list
            WHERE id = ?;
        """

        Connection connection = null
        PreparedStatement statement = null
        ResultSet response = null
        Email email = null

        try {
            connection = DatabaseHandler.getConnection()
            statement = connection.prepareStatement(sql)
            statement.setObject(1, id)
            response = statement.executeQuery()
            if (response.next()) {
                email = new Email(response.getInt("id"),
                        response.getString("email"))
            }
        } finally {
            DatabaseHandler.closeQuietly(response, statement, connection)
        }
        return email
    }

    Email create(Email email) {
        String sql = """
            INSERT INTO email_list (email) VALUES
            (?);
        """

        Connection connection = null
        PreparedStatement statement = null
        ResultSet response = null

        try {
            connection = DatabaseHandler.getConnection()
            connection.autoCommit = false

            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            statement.setString(1, email.email)
            statement.executeUpdate()
            response = statement.getGeneratedKeys()

            if (response.next()) {
                email.id = response.getInt("id")
            }

            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(response, statement, connection)
        }

        return email
    }

    Email update(int id, Email email) {
        String sql = """
            UPDATE email_list SET email = ?
            WHERE email = ?;
        """

        Connection connection = null
        PreparedStatement statement = null

        try {
            connection = DatabaseHandler.getConnection()
            connection.autoCommit = false

            statement = connection.prepareStatement(sql)
            statement.setString(1, email.email)
            statement.executeUpdate()

            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(statement, connection)
        }

        return getById(id)
    }

    void delete(int id) {
        String sql = "DELETE FROM email_list WHERE id = ?"
        Connection connection = null
        PreparedStatement statement = null

        try {
            connection = DatabaseHandler.getConnection()
            connection.autoCommit = false

            statement = connection.prepareStatement(sql)
            statement.setInt(1, id)
            statement.executeUpdate()

            connection.commit()
        } catch (Exception e) {
            if (connection != null) connection.rollback()
            throw e
        } finally {
            DatabaseHandler.closeQuietly(statement, connection)
        }
    }
}