/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRLiquidazioneMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;

/**
 * The Class LiquidazioneImpegnoPianoDeiContiConverter
 */
@Component
public class LiquidazioneImpegnoPianoDeiContiConverter extends GenericImpegnoSubimpegnoBaseConverter<Liquidazione, SiacTLiquidazione> {
	
	/**
	 * Instantiates a new pre documento spesa impegno piano dei conti converter.
	 */
	public LiquidazioneImpegnoPianoDeiContiConverter() {
		super(Liquidazione.class, SiacTLiquidazione.class);
	}

	@Override
	public Liquidazione convertFrom(SiacTLiquidazione src, Liquidazione dest) {
		if(src.getSiacRLiquidazioneMovgests()!=null){
			for(SiacRLiquidazioneMovgest r : src.getSiacRLiquidazioneMovgests()){
				if(r.getDataCancellazione()!=null) {
					continue;
				}
				
				impostaImpegnoESubImpegno(dest, r.getSiacTMovgestT());
			}
		}
		
		return dest;
	}
	

	
	@Override
	public SiacTLiquidazione convertTo(Liquidazione src, SiacTLiquidazione dest) {
		return dest;
	}

	@Override
	protected Impegno toImpegno(SiacTMovgestT siacTMovgestT) {
		SiacTMovgest siacTMovgest = siacTMovgestT.getSiacTMovgest();
		Impegno impegno = new Impegno();
		impegno.setUid(siacTMovgest.getUid());
		impegno.setNumeroBigDecimal(siacTMovgest.getMovgestNumero());
		impegno.setDescrizione(siacTMovgest.getMovgestDesc());
		impegno.setAnnoMovimento(siacTMovgest.getMovgestAnno());
		impegno.setDataEmissione(siacTMovgestT.getDataCreazione());
	
		aggiungiPianoDeiContiImpegno(siacTMovgestT, impegno);
		
		return impegno;
	}
	
	@Override
	protected SubImpegno toSubImpegno(SiacTMovgestT siacTMovgestT) {
		String methodName = "toSubImpegno";
		SubImpegno s = new SubImpegno();
		s.setUid(siacTMovgestT.getUid());
		
		try{
			s.setNumeroBigDecimal(new BigDecimal(siacTMovgestT.getMovgestTsCode()));
		} catch(RuntimeException re) {
			log.error(methodName, "Impssibile ottenere un BigDecimal a partire dalla stringa: \"" + siacTMovgestT.getMovgestTsCode() + "\". Returning null!", re);
		}
		
		aggiungiPianoDeiContiImpegno(siacTMovgestT, s);
		return s;
	}

	private void aggiungiPianoDeiContiImpegno(SiacTMovgestT siacTMovgestT, Impegno impegno) {
		List<String> listaCodici = Arrays.asList(TipologiaClassificatore.PDC.name(), TipologiaClassificatore.PDC_I.name(), TipologiaClassificatore.PDC_II.name(),
				TipologiaClassificatore.PDC_III.name(), TipologiaClassificatore.PDC_IV.name(), TipologiaClassificatore.PDC_V.name());
		// Piano dei conti
		List<SiacTClass> siacTClasses = siacTMovgestTRepository.findSiacTClassByMovgestTsIdECodiciTipo(siacTMovgestT.getUid(), listaCodici);
		if(siacTClasses == null || siacTClasses.isEmpty()) {
			return;
		}
		SiacTClass siacTClass = siacTClasses.get(0);
		impegno.setCodPdc(siacTClass.getClassifCode());
		
		impegno.setDescPdc(siacTClass.getClassifDesc());
	}

}
