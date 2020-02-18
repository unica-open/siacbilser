/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacTClassFamTree;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

// TODO: Auto-generated Javadoc
/**
 * The Class SiacTClassFamTreeDaoImpl.
 */
@Component
@Transactional
public class SiacTClassFamTreeDaoImpl extends JpaDao<SiacTClassFamTree, Integer> implements SiacTClassFamTreeDao {
	
}
