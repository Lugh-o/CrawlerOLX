package controller

import com.acelerazg.controller.EmailController
import com.acelerazg.dao.EmailDAO
import com.acelerazg.model.Email
import spock.lang.Specification

class EmailControllerTest extends Specification {
    EmailDAO emailDAO
    EmailController controller

    def setup() {
        emailDAO = Mock()
        controller = new EmailController(emailDAO)
    }

    def "HandleGetAll"() {
        given:
        Email fake1 = new Email(1, "mail@mail.com")
        Email fake2 = new Email(2, "another@mail.com")
        emailDAO.getAll() >> [fake1, fake2]

        when:
        List<Email> emailList = controller.handleGetAll()

        then:
        emailList.size() == 2
        emailList[0].email == "mail@mail.com"
    }

    def "HandleGetById"() {
        given:
        int id = 1
        Email fake1 = new Email(1, "mail@mail.com")
        emailDAO.getById(id) >> fake1

        when:
        Email email = controller.handleGetById(id)

        then:
        email != null
        email.email == "mail@mail.com"
    }

    def "HandleCreate"() {
        given:
        String address = "mail@mail.com"
        Email fakeWithId = new Email(1, address)
        emailDAO.create(_ as Email) >> fakeWithId

        when:
        Email created = controller.handleCreate(address)

        then:
        created != null
        created.email == address
    }

    def "HandleDelete"() {
        given:
        int id = 42

        when:
        controller.handleDelete(id)

        then:
        1 * emailDAO.delete(id)
    }
}
