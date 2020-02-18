/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemClass;
import it.csi.siac.siacbilser.integration.entity.SiacRGiustificativoMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativo;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siacfinser.model.Impegno;

/**
 * The Class RendicontoRichiestaImpegnoSubimpegnoConverter.
 */
@Component
public class RendicontoRichiestaImpegnoSubimpegnoConverter extends GenericImpegnoSubimpegnoBaseConverter<RendicontoRichiesta, SiacTGiustificativo > {
	
	/**
	 * Instantiates a new rendiconto richiesta impegno subimpegno converter.
	 */
	public RendicontoRichiestaImpegnoSubimpegnoConverter() {
		super(RendicontoRichiesta.class, SiacTGiustificativo.class);
	}

	@Override
	public RendicontoRichiesta convertFrom(SiacTGiustificativo src, RendicontoRichiesta dest) {
		final String methodName = "convertFrom";
		
		if(src.getSiacRGiustificativoMovgests()!=null){
			for(SiacRGiustificativoMovgest r : src.getSiacRGiustificativoMovgests()){
				if(r.getDataCancellazione()!=null) {
					continue;
				}
				
				log.debug(methodName, "r.uid: " + r.getUid());
				impostaImpegnoESubImpegno(dest, r.getSiacTMovgestT());		
			}
		}
		
		return dest;
	}

	@Override
	public SiacTGiustificativo convertTo(RendicontoRichiesta src, SiacTGiustificativo dest) {
		final String methodName = "convertTo";
		
		Integer movgestId = getMovgestId(src.getImpegno(), src.getSubImpegno());
		log.debug(methodName, "movgestId: " + movgestId);
		
		if(movgestId==null){
			return dest;
		}
		
		
		dest.setSiacRGiustificativoMovgests(new ArrayList<SiacRGiustificativoMovgest>());
		
		SiacRGiustificativoMovgest r = new SiacRGiustificativoMovgest();
		r.setSiacTGiustificativo(dest);
		SiacTMovgestT siacTMovgestT = new SiacTMovgestT();
		siacTMovgestT.setUid(movgestId);	
		r.setSiacTMovgestT(siacTMovgestT);
		
		r.setLoginOperazione(dest.getLoginOperazione());
		r.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRGiustificativoMovgest(r);
		
		return dest;
	}

	
	@Override
	protected void aggiungiInformazioniCapitoloAdImpegno(SiacTMovgestT siacTMovgestT, Impegno impegno) {
		for(SiacRMovgestBilElem r :siacTMovgestT.getSiacTMovgest().getSiacRMovgestBilElems()){
			if(r.getDataCancellazione()==null){
				SiacTBilElem siacTBilElem = siacTBilElemRepository.findOne(r.getSiacTBilElem().getElemId());
				CapitoloUscitaGestione cap = new CapitoloUscitaGestione();
				map(siacTBilElem, cap, BilMapId.SiacTBilElem_Capitolo_Base);
				
				SiacRBilElemAttr siacRBilElemAttr = siacTBilElemRepository.findBilElemAttrByTipoAttrCode(siacTBilElem.getUid(), SiacTAttrEnum.FlagRilevanteIva.getCodice());
				if(siacRBilElemAttr!=null) {
					cap.setFlagRilevanteIva("S".equals(siacRBilElemAttr.getBoolean_()));
				}
				
				// Elemento del piano dei conti
				SiacRBilElemClass siacRBilElemClassPDC = siacTBilElemRepository.findBilElemClassByTipoClassCodes(siacTBilElem.getUid(),
						Arrays.asList(SiacDClassTipoEnum.QuartoLivelloPdc.getCodice(), SiacDClassTipoEnum.QuintoLivelloPdc.getCodice()));
				
				if(siacRBilElemClassPDC != null) {
					ElementoPianoDeiConti epdc = new ElementoPianoDeiConti();
					SiacTClass siacTClass = siacRBilElemClassPDC.getSiacTClass();
					epdc.setUid(siacTClass.getUid());
					epdc.setCodice(siacTClass.getClassifCode());
					epdc.setDescrizione(siacTClass.getClassifDesc());
					
					TipoClassificatore tipoClassificatore = new TipoClassificatore();
					tipoClassificatore.setCodice(siacTClass.getSiacDClassTipo().getClassifTipoCode());
					tipoClassificatore.setUid(siacTClass.getSiacDClassTipo().getUid());
					epdc.setTipoClassificatore(tipoClassificatore);
					cap.setElementoPianoDeiConti(epdc);
				}
				
				SiacRBilElemClass siacRBilElemClassMAC = siacTBilElemRepository.findBilElemClassByTipoClassCodes(siacTBilElem.getUid(),
						Arrays.asList(SiacDClassTipoEnum.Macroaggregato.getCodice(), SiacDClassTipoEnum.Macroaggregato.getCodice()));
				
				if(siacRBilElemClassMAC != null) {
					Macroaggregato mac = new Macroaggregato();
					SiacTClass siacTClass = siacRBilElemClassMAC.getSiacTClass();
					mac.setUid(siacTClass.getUid());
					mac.setCodice(siacTClass.getClassifCode());
					mac.setDescrizione(siacTClass.getClassifDesc());
					
					TipoClassificatore tipoClassificatore = new TipoClassificatore();
					tipoClassificatore.setCodice(siacTClass.getSiacDClassTipo().getClassifTipoCode());
					tipoClassificatore.setUid(siacTClass.getSiacDClassTipo().getUid());
					mac.setTipoClassificatore(tipoClassificatore);
					cap.setMacroaggregato(mac);
				}
				impegno.setCapitoloUscitaGestione(cap);
				
				break;
			}
		}
		
	}
	

	

}
