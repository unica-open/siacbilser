/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacRAttoAmmClass;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class SiacRAttoAmmClassImpl.
 */
@Component
@Transactional
public class SiacRAttoAmmClassImpl extends JpaDao<SiacRAttoAmmClass, Integer> implements SiacRAttoAmmClassDao {
	
	

}
