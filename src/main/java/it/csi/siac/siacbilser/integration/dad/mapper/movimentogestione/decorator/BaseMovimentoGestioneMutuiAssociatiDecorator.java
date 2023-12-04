/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad.mapper.movimentogestione.decorator;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.dad.mapper.mutuo.SiacRMutuoMovgestTMutuoAssociatoMovimentoGestioneMapper;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMovgestHelper;
import it.csi.siac.siaccommon.util.mapper.BaseMapperDecorator;
import it.csi.siac.siacfinser.model.MovimentoGestione;
 
public abstract class BaseMovimentoGestioneMutuiAssociatiDecorator<MG extends MovimentoGestione> extends BaseMapperDecorator<SiacTMovgest, MG> {
	
	@Autowired SiacTMovgestHelper siacTMovgestHelper;
	@Autowired SiacRMutuoMovgestTMutuoAssociatoMovimentoGestioneMapper siacRMutuoMovgestTMutuoAssociatoMovimentoGestioneMapper;
	
	@Override
	public void decorate(SiacTMovgest s, MG d) {
		d.setElencoMutuiAssociati(siacRMutuoMovgestTMutuoAssociatoMovimentoGestioneMapper.map(siacTMovgestHelper.getSiacRMutuoMovgestTList(s)));
	}	
}
