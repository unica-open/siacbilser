/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.entity.SiacDAttoAmmTipo;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;

/**
 * The Class SiacDAttoAmmTipoImpl.
 */
@Component
@Transactional
public class SiacDAttoAmmTipoImpl extends JpaDao<SiacDAttoAmmTipo, Integer> implements SiacDAttoAmmTipoDao {
	
	

}
