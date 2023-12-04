/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad.mapper;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacROrdinativoQuietanza;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativoT;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOrdinativoTsDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTLiquidazioneHelper;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTOrdinativoHelper;
import it.csi.siac.siacbilser.integration.entity.helper.SiacTOrdinativoTHelper;
import it.csi.siac.siaccommon.util.mapper.BaseMapper;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siacintegser.model.custom.oopp.Mandato;


@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SiacTOrdinativoTMandatoMapper extends BaseMapper<SiacTOrdinativoT, Mandato> {

	@Autowired private SiacTOrdinativoMandatoMapper siacTOrdinativoMandatoMapper;
	@Autowired private SiacTOrdinativoTsDetMandatoMapper siacTOrdinativoTsDetMandatoMapper;
	@Autowired private SiacTBilElemCapitoloMapper siacTBilElemCapitoloMapper;
	@Autowired private SiacTAttoAmmProvvedimentoMapper siacTAttoAmmProvvedimentoMapper;
	@Autowired private SiacTSoggettoSoggettoIntegMapper siacTSoggettoSoggettoMapper;
	@Autowired private SiacTLiquidazioneLiquidazioneMapper siacTLiquidazioneLiquidazioneMapper;
	@Autowired private SiacTMovgestTImpegnoMapper siacTMovgestTImpegnoMapper;
	@Autowired private SiacDOrdinativoStatoMapper siacDOrdinativoStatoMapper;
	@Autowired private SiacRLiquidazioneAttrMandatoMapper siacRLiquidazioneAttrMandatoMapper;	
	
	@Autowired private SiacTOrdinativoTHelper siacTOrdinativoTHelper;
	@Autowired private SiacTOrdinativoHelper siacTOrdinativoHelper; 
	@Autowired private SiacTLiquidazioneHelper siacTLiquidazioneHelper; 
	
	
	@Override
	public void map(SiacTOrdinativoT o1, Mandato o2) {
		siacTOrdinativoMandatoMapper.map(o1.getSiacTOrdinativo(), o2);
		o2.setStato(siacDOrdinativoStatoMapper.map(siacTOrdinativoHelper.getSiacDOrdinativoStato(o1.getSiacTOrdinativo())));
		o2.setCodiceSubOrdinativo(o1.getOrdTsCode());
		siacTOrdinativoTsDetMandatoMapper.map(siacTOrdinativoTHelper.getSiacTOrdinativoTsDet(o1, SiacDOrdinativoTsDetTipoEnum.ATTUALE), o2);
		o2.setAnnoEsercizio(o1 == null ? null : NumberUtil.safeParseInt(o1.getSiacTOrdinativo().getSiacTBil().getSiacTPeriodo().getAnno()));
		SiacTLiquidazione siacTLiquidazione = siacTOrdinativoTHelper.getSiacTLiquidazione(o1);
		o2.setLiquidazione(siacTLiquidazioneLiquidazioneMapper.map(siacTLiquidazione));
		siacRLiquidazioneAttrMandatoMapper.map(siacTLiquidazioneHelper.getSiacRSubdocAttrList(siacTLiquidazione), o2);
		o2.setImpegno(siacTMovgestTImpegnoMapper.map(siacTOrdinativoTHelper.getSiacTMovgestT(o1)));
		o2.getImpegno().setElencoVincoliImpegno(null);
		o2.setCreditore(siacTSoggettoSoggettoMapper.map(siacTOrdinativoHelper.getSiacTSoggetto(o1.getSiacTOrdinativo())));
		o2.setDataEmissione(o1.getSiacTOrdinativo().getOrdEmissioneData());
		o2.setDataQuietanza(getDataQuietanza(o1));
		o2.setProvvedimento(siacTAttoAmmProvvedimentoMapper.map(siacTOrdinativoHelper.getSiacTAttoAmm(o1.getSiacTOrdinativo())));
		o2.setCapitolo(siacTBilElemCapitoloMapper.map(siacTOrdinativoTHelper.getSiacTBilElem(o1)));
	}


	private Timestamp getDataQuietanza(SiacTOrdinativoT o1) {
		SiacROrdinativoQuietanza siacROrdinativoQuietanza = siacTOrdinativoHelper.getSiacROrdinativoQuietanza(o1.getSiacTOrdinativo());
		return siacROrdinativoQuietanza == null ? null : siacROrdinativoQuietanza.getOrdQuietanzaData();
	}
}

