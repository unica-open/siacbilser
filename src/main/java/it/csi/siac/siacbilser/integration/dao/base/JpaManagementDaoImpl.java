/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.base;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * DAO per il Management del JPA
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 24/09/2019
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class JpaManagementDaoImpl implements JpaManagementDao {
	
	@PersistenceContext protected EntityManager entityManager;

	@Override
	public void flushAndClear() {
		flush();
		entityManager.clear();
	}

	@Override
	public void flush() {
		entityManager.flush();
	}

}
