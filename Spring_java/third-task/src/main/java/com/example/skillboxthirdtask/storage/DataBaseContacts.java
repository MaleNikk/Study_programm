package com.example.skillboxthirdtask.storage;

import com.example.skillboxthirdtask.entity.Contact;
import com.example.skillboxthirdtask.exception.SearchException;
import com.example.skillboxthirdtask.storage.mapper.ContactRowMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
@RequiredArgsConstructor
@Slf4j
public class DataBaseContacts implements ContactStorage {

    private final JdbcTemplate jdbcTemplate;
    @Getter
    private final HashSet<Long> numbersId;

    @Override
    public List<Contact> searchAll() {
        log.debug("Enjoy DataBaseContacts method search all.");
        String sql = "SELECT * FROM contact";
        for (Contact contact:jdbcTemplate.query(sql, new ContactRowMapper())){
            numbersId.add(contact.getId());
        }
        return jdbcTemplate.query(sql, new ContactRowMapper());
    }

    @Override
    public Optional<Contact> searchById(Long id) {
        log.debug("Enjoy DataBaseContacts method search by id: {}.", id);
        String sql = "SELECT * FROM contact WHERE id = ?";
        Contact contact = DataAccessUtils.singleResult(
                jdbcTemplate.query(sql,
                        new ArgumentPreparedStatementSetter(new Object[]{id}),
                        new RowMapperResultSetExtractor<>(new ContactRowMapper(), 1)));

        return Optional.ofNullable(contact);
    }

    @Override
    public Contact saveContact(Contact contact) {
        log.debug("Enjoy DataBaseContacts method save contact: {}.", contact);
        contact.setId(searchAll().size() + 1);
        String sql = "INSERT INTO contact (id, name, surname, email, phone) VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                contact.getId(),
                contact.getName(),
                contact.getSurname(),
                contact.getEmail(),
                contact.getPhone());
        return contact;
    }

    @Override
    public Contact editContact(Contact contact) {
        log.debug("Enjoy DataBaseContacts method edit contact: {}.", contact);
        Contact contactEdited = searchById(contact.getId()).orElse(null);
        if (contactEdited != null) {
            String sql = "UPDATE contact SET name = ?, surname = ?, email = ?, phone = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    contact.getName(),
                    contact.getSurname(),
                    contact.getEmail(),
                    contact.getPhone(),
                    contact.getId());
            return contact;
        }
        log.warn("Contact {} for edited not fount", contact);
        throw new SearchException(MessageFormat.format("Contact: {0} are not present in data base!", contact));
    }

    @Override
    public void removeContactById(Long id) {
        log.debug("Enjoy DataBaseContacts method remove by id: {}.", id);
        String sql = "DELETE FROM contact WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void batchInsert(List<Contact> contacts) {
        log.debug("Enjoy DataBaseContacts method batchInsert.");
        String sql = "INSERT INTO contact (id, name, surname, email, phone) VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Contact contact = contacts.get(i);
                ps.setLong(1, contact.getId());
                ps.setString(2, contact.getName());
                ps.setString(3, contact.getSurname());
                ps.setString(4, contact.getEmail());
                ps.setString(5, contact.getPhone());
            }

            @Override
            public int getBatchSize() {
                return contacts.size();
            }
        });
    }

    @Override
    public HashSet<Long> numbersId() {
        log.debug("Enjoy DataBaseContacts method numbersId.");
        String sql = "SELECT * FROM contact";
        for (Contact contact:jdbcTemplate.query(sql, new ContactRowMapper())){
            numbersId.add(contact.getId());
        }
        return getNumbersId();
    }
}
