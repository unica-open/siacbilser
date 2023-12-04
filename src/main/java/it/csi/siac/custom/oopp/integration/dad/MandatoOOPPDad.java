/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.custom.oopp.integration.dad.mapper.SiacTOrdinativoTMandatoMapper;
import it.csi.siac.siacbilser.integration.dad.ExtendedBaseDadImpl;
import it.csi.siac.siacbilser.integration.dao.SiacTOrdinativoTBilRepository;
import it.csi.siac.siacintegser.model.custom.oopp.Mandato;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class MandatoOOPPDad extends ExtendedBaseDadImpl {

	@Autowired private SiacTOrdinativoTBilRepository siacTOrdinativoTBilRepository;
	@Autowired private SiacTOrdinativoTMandatoMapper siacTOrdinativoTMandatoMapper;
	
	public List<Mandato> ricercaMandati(Integer annoBilancio, String cup, String codiceProgetto, String cig) {
		return siacTOrdinativoTMandatoMapper.map(siacTOrdinativoTBilRepository.findSiacTOrdinativoTPagamento(ente.getUid(), annoBilancio, cup, codiceProgetto, cig));
	}
}
