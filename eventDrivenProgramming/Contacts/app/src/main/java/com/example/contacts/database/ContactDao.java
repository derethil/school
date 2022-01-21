package com.example.contacts.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contacts.models.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * from contact")
    List<Contact> getContacts();

    @Query("SELECT * from contact WHERE id = :id LIMIT 1")
    Contact getContact(int id);

    @Insert
    long createContact(Contact contact);
}
