package edu.javacourse.city.dao;

import edu.javacourse.city.domain.PersonRequest;
import edu.javacourse.city.domain.PersonResponse;
import edu.javacourse.city.exception.PersonCheckException;

import java.sql.*;

public class PersonCheckDao {

    private static final String SQL_REQUEST = "select temporal from cr_address_person ap " +
            "inner join cr_person p on p.person_id = ap.person_id " +
            "inner join cr_address a on a.address_id = ap.address_id " +
            "where " +
            "CURRENT_DATE >= ap.start_date and (CURRENT_DATE <= ap.end_date or ap.end_date is null) " +
            "and upper(p.sur_name) = upper(?) " +
            "and upper(p.given_name) = upper(?) " +
            "and upper(p.patronymic) = upper(?) " +
            "and p.date_of_birth = ? " +
            "and a.street_code = ? " +
            "and upper(a.building) = upper(?) ";

    public PersonResponse checkPerson(PersonRequest request) throws PersonCheckException {
        PersonResponse response = new PersonResponse();

        String sql = SQL_REQUEST;
        if (request.getExtension() != null) {
            sql +=  "and upper(a.extension) = upper(?) ";
        } else {
            sql += "and a.extension is null ";
        }

        if (request.getApartment() != null) {
            sql += "and upper(a.apartment) = upper(?) ";
        } else {
            sql += "and a.apartment is null ";
        }

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int count = 1;
            statement.setString(count++, request.getSurname());
            statement.setString(count++, request.getGivenName());
            statement.setString(count++, request.getPatronymic());
            statement.setDate(count++, Date.valueOf(request.getDateOfBirth()));
            statement.setInt(count++, request.getStreetCode());
            statement.setString(count++, request.getBuilding());
            if (request.getExtension() != null)
                statement.setString(count++, request.getExtension());
            if(request.getApartment() != null)
                statement.setString(count++, request.getApartment());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                response.setRegistered(true);
                response.setTemporal(resultSet.getBoolean("temporal"));
            }

        } catch (SQLException e) {
            throw new PersonCheckException(e);
        }

        return response;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost/city_register",
                "postgres", "postgres");
    }

}
