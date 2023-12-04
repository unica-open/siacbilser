/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTMovgestHelper;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siacintegser.model.custom.oopp.MovimentoGestione;

@Component("SiacTMovgestMovimentoGestioneOOPPMapper")
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTMovgestMovimentoGestioneMapper extends BaseMovimentoGestioneMapper<MovimentoGestione> {

	@Autowired private SiacDSoggettoClasseClasseSoggettoMapper siacDSoggettoClasseClasseSoggettoMapper;
	@Autowired private SiacRMovgestTVincoloMapper siacRMovgestTVincoloMapper;
	@Autowired private SiacDSiopeAssenzaMotivazioneMovimentoGestioneMapper siacDSiopeAssenzaMotivazioneMovimentoGestioneMapper;
	@Autowired private SiacTBilElemCapitoloMapper siacTBilElemCapitoloMapper;
	@Autowired private SiacTMovgestRiaccertamentoMapper siacTMovgestRiaccertamentoMapper;
	@Autowired private SiacTMovgestTsSubMovimentoGestioneMapper siacTMovgestTsSubMovimentoGestioneMapper;
	@Autowired private SiacTClassMovimentoGestioneMapper siacTClassMovimentoGestioneMapper;
	@Autowired private SiacTMovgestAggiudicazioneRidcoiMapper siacTMovgestAggiudicazioneRidcoiMapper;
	@Autowired private SiacDMovgestStatoStatoMapper siacDMovgestStatoStatoMapper;
	@Autowired private SiacTSoggettoSoggettoIntegMapper siacTSoggettoSoggettoMapper;
	@Autowired private SiacTAttoAmmProvvedimentoMapper siacTAttoAmmProvvedimentoMapper;
	@Autowired @Qualifier("SiacTMovgestTsDetMovimentoGestioneOOPPMapper") private SiacTMovgestTsDetMovimentoGestioneMapper siacTMovgestTsDetMovimentoGestioneMapper;
	@Autowired private SiacRMovgestTsAttrMovimentoGestioneMapper siacRMovgestTsAttrMovimentoGestioneMapper;
	
	@Autowired private SiacTMovgestHelper siacTMovgestHelper;
	
	@Override
	public void map(SiacTMovgest o1, MovimentoGestione o2) {
		if (o1 == null || o2 == null) {
			return;
		}
		
		super.map(o1, o2);
		
		o2.setDescrizione(o1.getMovgestDesc());
		o2.setAnnoBilancio(NumberUtil.safeParseInt(o1.getSiacTBil().getSiacTPeriodo().getAnno()));
		o2.setCapitolo(siacTBilElemCapitoloMapper.map(siacTMovgestHelper.getSiacTBilElem(o1)));
		siacTMovgestTsDetMovimentoGestioneMapper.map(siacTMovgestHelper.getSiacTMovgestTsDetList(o1), o2);
		o2.setSoggetto(siacTSoggettoSoggettoMapper.map(siacTMovgestHelper.getSiacTSoggetto(o1)));
		o2.setClasseSoggetto(siacDSoggettoClasseClasseSoggettoMapper.map(siacTMovgestHelper.getSiacDSoggettoClasse(o1)));
		o2.setElencoVincoliImpegno(siacRMovgestTVincoloMapper.map(siacTMovgestHelper.getSiacRmovgestT1List(o1)));
		siacRMovgestTsAttrMovimentoGestioneMapper.map(siacTMovgestHelper.getSiacRattrList(o1), o2);
		siacDSiopeAssenzaMotivazioneMovimentoGestioneMapper.map(siacTMovgestHelper.getSiacDSiopeAssenzaMotivazione(o1), o2);
		o2.setProvvedimento(siacTAttoAmmProvvedimentoMapper.map(siacTMovgestHelper.getSiacTAttoAmm(o1), o1));
		o2.setRiaccertamento(siacTMovgestRiaccertamentoMapper.map(o1));
		o2.setElencoSubMovimentiGestione(siacTMovgestTsSubMovimentoGestioneMapper.map(siacTMovgestHelper.getSiacTMovgestSubNotAnnullatoList(o1)));
		siacTClassMovimentoGestioneMapper.map(siacTMovgestHelper.getSiacTClassList(o1), o2);
		o2.setRidcoi(siacTMovgestAggiudicazioneRidcoiMapper.map(siacTMovgestHelper.getSiacRMovgestAggiudicazioneSiacTmovgestDa(o1)));
		o2.setStato(siacDMovgestStatoStatoMapper.map(siacTMovgestHelper.getSiacDMovgestStato(o1)));
	}
}




