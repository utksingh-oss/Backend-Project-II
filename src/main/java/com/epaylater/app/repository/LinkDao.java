package com.epaylater.app.repository;


import com.epaylater.app.entities.Debit_Credit_Link;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.List;

public interface LinkDao {
    @Transaction
    @SqlUpdate("INSERT INTO debit_credit_link(debit_id, credit_id) VALUES(:debit_id, :credit_id)")
    @GetGeneratedKeys
    int createLink(@BindBean Debit_Credit_Link link);

    @SqlQuery("SELECT * FROM debit_credit_link")
    @RegisterBeanMapper(Debit_Credit_Link.class)
    List<Debit_Credit_Link> getLinks();

    @SqlQuery("SELECT * FROM debit_credit_link WHERE debit_id = :id")
    @RegisterBeanMapper(Debit_Credit_Link.class)
    Debit_Credit_Link getLinkForDebitId(@Bind("id") Long id);
}
