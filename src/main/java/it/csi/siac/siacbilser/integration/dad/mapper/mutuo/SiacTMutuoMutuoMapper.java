/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.mutuo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dad.mapper.attoamministrativo.SiacTAttoAmmAttoAmministrativoMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.ente.SiacTEnteProprietarioEnteMapper;
import it.csi.siac.siacbilser.integration.dad.mapper.soggetto.SiacTSoggettoSoggettoMapper;
import it.csi.siac.siacbilser.integration.entity.SiacTMutuo;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siacbilser.model.mutuo.TipoTassoMutuo;
import it.csi.siac.siaccommonser.util.mapper.SiacTBaseExtEntitaExtMapper;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTMutuoMutuoMapper extends SiacTBaseExtEntitaExtMapper<SiacTMutuo, Mutuo> {
	
	@Autowired private SiacTEnteProprietarioEnteMapper siacTEnteProprietarioEnteMapper;
	@Autowired private SiacTAttoAmmAttoAmministrativoMapper siacTAttoAmmAttoAmministrativoMapper;
	@Autowired private SiacTSoggettoSoggettoMapper siacTSoggettoSoggettoMapper;
	
	@Override
	public void map(SiacTMutuo s,  Mutuo d) {
		super.map(s, d);
		d.setNumero(s.getMutuoNumero());
		d.setTassoInteresse(s.getMutuoTasso());
		d.setSommaMutuataIniziale(s.getMutuoSommaIniziale());
		d.setTassoInteresseEuribor(s.getMutuoTassoEuribor());
		d.setTassoInteresseSpread(s.getMutuoTassoSpread());
		d.setEnte(siacTEnteProprietarioEnteMapper.map(s.getSiacTEnteProprietario()));
		d.setTipoTasso(TipoTassoMutuo.fromCodice(s.getSiacDMutuoTipoTasso().getMutuoTipoTassoCode()));
		d.setAttoAmministrativo(siacTAttoAmmAttoAmministrativoMapper.map(s.getSiacTAttoAmm()));
		d.setSoggetto(siacTSoggettoSoggettoMapper.map(s.getSiacTSoggetto()));
	}
}
