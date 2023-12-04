/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * The Class SubdocumentoEntrataAccertamentoSubaccertamentoPdcConverter.
 */
@Component
public class SubdocumentoEntrataAccertamentoSubaccertamentoPdcConverter extends GenericAccertamentoSubaccertamentoBaseConverter<SubdocumentoEntrata, SiacTSubdoc> {
	
	/**
	 * Instantiates a new subdocumento entrata accertamento subaccertamento pdc converter.
	 */
	public SubdocumentoEntrataAccertamentoSubaccertamentoPdcConverter() {
		super(SubdocumentoEntrata.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoEntrata convertFrom(SiacTSubdoc src, SubdocumentoEntrata dest) {
		final String methodName = "convertFrom";
		
		if(src.getSiacRSubdocMovgestTs()!=null){
			for(SiacRSubdocMovgestT siacRSubdocMovgestT : src.getSiacRSubdocMovgestTs()){
				log.debug(methodName, "SiacTSubdoc: id = " + src.getSubdocId());
				log.debug(methodName, "SiacRSubdocMovgestT: id = " + siacRSubdocMovgestT.getSubdocMovgestTsId());
				if(siacRSubdocMovgestT.getDataCancellazione()!=null) {
					continue;
				}
				SiacTMovgestT siacTMovgestT = siacRSubdocMovgestT.getSiacTMovgestT();
				impostaAccertamentoESubAccertamento(dest, siacTMovgestT);
			}
		}
		
		return dest;
	}
	

	@Override
	public SiacTSubdoc convertTo(SubdocumentoEntrata src, SiacTSubdoc dest) {
		// Non faccio nulla
		return dest;
	}
	
	@Override
	protected Accertamento toAccertamento(SiacTMovgestT siacTMovgestT) {
		SiacTMovgest siacTMovgest = siacTMovgestT.getSiacTMovgest();
		Accertamento accertamento = new Accertamento();
		accertamento.setUid(siacTMovgest.getUid());
		accertamento.setNumeroBigDecimal(siacTMovgest.getMovgestNumero());
		accertamento.setDescrizione(siacTMovgest.getMovgestDesc());
		accertamento.setAnnoMovimento(siacTMovgest.getMovgestAnno());
		accertamento.setDataEmissione(siacTMovgestT.getDataCreazione());
	
		aggiungiPianoDeiConti(siacTMovgestT, accertamento);
		
		return accertamento;
	}
	
	@Override
	protected SubAccertamento toSubAccertamento(SiacTMovgestT siacTMovgestT) {
		String methodName = "toSubAccertamento";
		SubAccertamento s = new SubAccertamento();
		s.setUid(siacTMovgestT.getUid());
		
		try{
			s.setNumeroBigDecimal(new BigDecimal(siacTMovgestT.getMovgestTsCode()));
		} catch(RuntimeException re) {
			log.error(methodName, "Impssibile ottenere un BigDecimal a partire dalla stringa: \"" + siacTMovgestT.getMovgestTsCode() + "\". Returning null!", re);
		}
		
		aggiungiPianoDeiConti(siacTMovgestT, s);
		return s;
	}

	private void aggiungiPianoDeiConti(SiacTMovgestT siacTMovgestT, Accertamento accertamento) {
		List<String> listaCodici = Arrays.asList(TipologiaClassificatore.PDC.name(), TipologiaClassificatore.PDC_I.name(), TipologiaClassificatore.PDC_II.name(),
				TipologiaClassificatore.PDC_III.name(), TipologiaClassificatore.PDC_IV.name(), TipologiaClassificatore.PDC_V.name());
		// Piano dei conti
		List<SiacTClass> siacTClasses = siacTMovgestTRepository.findSiacTClassByMovgestTsIdECodiciTipo(siacTMovgestT.getUid(), listaCodici);
		if(siacTClasses == null || siacTClasses.isEmpty()) {
			return;
		}
		SiacTClass siacTClass = siacTClasses.get(0);
		accertamento.setCodPdc(siacTClass.getClassifCode());
		accertamento.setDescPdc(siacTClass.getClassifDesc());
	}
	
}
