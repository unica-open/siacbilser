/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;


public class HibernateExtendedJpaDialect extends HibernateJpaDialect {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -7184946542173885820L;
	
	private static final transient Logger LOGGER = LoggerFactory.getLogger(HibernateExtendedJpaDialect.class);
	
	
	
 
    /**
     * This method is overridden to set custom isolation levels on the connection.
     *
     * @param entityManager the entity manager
     * @param definition the definition
     * @return the object
     * @throws PersistenceException the persistence exception
     * @throws SQLException the SQL exception
     * @throws TransactionException the transaction exception
     */
    @Override
    public Object beginTransaction(final EntityManager entityManager, final TransactionDefinition definition) throws SQLException { 
        Session session = getSession(entityManager);
        if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT) {
            session.getTransaction().setTimeout(definition.getTimeout());
        }
 
        entityManager.getTransaction().begin();
        LOGGER.debug("Transaction started");
 
        session.doWork(new Work() {
 
            @Override
            public void execute(Connection connection) throws SQLException {
                LOGGER.debug("The connection instance is {}", connection);
                LOGGER.debug("The isolation level of the connection is {} and the isolation level set on the transaction is {}",
                        connection.getTransactionIsolation(), definition.getIsolationLevel());
                DataSourceUtils.prepareConnectionForTransaction(connection, definition);
            }
        });
 
        return prepareTransaction(entityManager, definition.isReadOnly(), definition.getName());
    }
 
}