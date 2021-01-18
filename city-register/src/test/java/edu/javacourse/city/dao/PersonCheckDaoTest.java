package edu.javacourse.city.dao;

import edu.javacourse.city.domain.PersonRequest;
import edu.javacourse.city.domain.PersonResponse;
import edu.javacourse.city.exception.PersonCheckException;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class PersonCheckDaoTest {

    @Test
    public void checkPerson() throws PersonCheckException {

        PersonRequest request = new PersonRequest();
        request.setSurname("Васильев");
        request.setGivenName("Павел");
        request.setPatronymic("Николаевич");
        request.setDateOfBirth(LocalDate.of(1995, 3, 18));
        request.setStreetCode(1);
        request.setBuilding("10");
        request.setExtension("2");
        request.setApartment("121");

        PersonCheckDao dao = new PersonCheckDao();
        PersonResponse response = dao.checkPerson(request);
        Assert.assertTrue(response.isRegistered());
        Assert.assertFalse(response.isTemporal());

    }

    @Test
    public void checkPerson2() throws PersonCheckException {

        PersonRequest request = new PersonRequest();
        request.setSurname("Васильева");
        request.setGivenName("Ирина");
        request.setPatronymic("Петровна");
        request.setDateOfBirth(LocalDate.of(1997, 8, 21));
        request.setStreetCode(1);
        request.setBuilding("271");

        PersonCheckDao dao = new PersonCheckDao();
        PersonResponse response = dao.checkPerson(request);
        Assert.assertTrue(response.isRegistered());
        Assert.assertFalse(response.isTemporal());

    }
}