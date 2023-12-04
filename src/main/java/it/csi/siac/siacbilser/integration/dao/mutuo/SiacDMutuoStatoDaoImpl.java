/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.mutuo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDMutuoStato;
import it.csi.siac.siaccommonser.integration.dao.base.JpaDao;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacDMutuoStatoDaoImpl extends JpaDao<SiacDMutuoStato, Integer> implements SiacDMutuoStatoDao {
	@PostConstruct
	public void init() {
	}
}
