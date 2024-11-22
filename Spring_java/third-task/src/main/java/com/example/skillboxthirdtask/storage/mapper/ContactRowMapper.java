package com.example.skillboxthirdtask.storage.mapper;


import com.example.skillboxthirdtask.entity.Contact;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Data
@RequiredArgsConstructor
@Component
public class ContactRowMapper implements RowMapper<Contact> {

    @Override
    public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contact contact = new Contact();
        contact.setId(rs.getLong(Contact.Fields.id));
        contact.setName(rs.getString(Contact.Fields.name));
        contact.setSurname(rs.getString(Contact.Fields.surname));
        contact.setEmail(rs.getString(Contact.Fields.email));
        contact.setPhone(rs.getString(Contact.Fields.phone));
        return contact;
    }
}
