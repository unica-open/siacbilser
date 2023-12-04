/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.mutuo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRMutuoRipartizione;
import it.csi.siac.siaccommonser.integration.dao.base.SiacTBaseDaoImpl;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacRMutuoRipartizioneDaoImpl extends SiacTBaseDaoImpl<SiacRMutuoRipartizione, Integer> implements SiacRMutuoRipartizioneDao {
}
