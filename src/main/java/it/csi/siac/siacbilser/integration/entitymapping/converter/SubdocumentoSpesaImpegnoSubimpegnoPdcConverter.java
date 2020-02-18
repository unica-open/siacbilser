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
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * The Class SubdocumentoSpesaImpegnoSubimpegnoPdcConverter.
 */
@Component
public class SubdocumentoSpesaImpegnoSubimpegnoPdcConverter extends GenericImpegnoSubimpegnoBaseConverter<SubdocumentoSpesa, SiacTSubdoc> {
	
	/**
	 * Instantiates a new subdocumento spesa impegno subimpegno pdc converter.
	 */
	public SubdocumentoSpesaImpegnoSubimpegnoPdcConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		final String methodName = "convertFrom";
		
		if(src.getSiacRSubdocMovgestTs()!=null){
			for(SiacRSubdocMovgestT siacRSubdocMovgestT : src.getSiacRSubdocMovgestTs()){
				log.debug(methodName, "SiacTSubdoc: id = " + src.getSubdocId());
				log.debug(methodName, "SiacRSubdocMovgestT: id = " + siacRSubdocMovgestT.getSubdocMovgestTsId());
				if(siacRSubdocMovgestT.getDataCancellazione()!=null) {
					continue;
				}
				SiacTMovgestT siacTMovgestT = siacRSubdocMovgestT.getSiacTMovgestT();
				impostaImpegnoESubImpegno(dest, siacTMovgestT);
			}
		}
		
		return dest;
	}
	

	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		// Non faccio nulla
		return dest;
	}
	
	@Override
	protected Impegno toImpegno(SiacTMovgestT siacTMovgestT) {
		SiacTMovgest siacTMovgest = siacTMovgestT.getSiacTMovgest();
		Impegno impegno = new Impegno();
		impegno.setUid(siacTMovgest.getUid());
		impegno.setNumero(siacTMovgest.getMovgestNumero());
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
			s.setNumero(new BigDecimal(siacTMovgestT.getMovgestTsCode()));
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
		impegno.setCodicePdc(siacTClass.getClassifCode());
		impegno.setCodPdc(siacTClass.getClassifCode());
		
		impegno.setDescPdc(siacTClass.getClassifDesc());
	}
	
}
