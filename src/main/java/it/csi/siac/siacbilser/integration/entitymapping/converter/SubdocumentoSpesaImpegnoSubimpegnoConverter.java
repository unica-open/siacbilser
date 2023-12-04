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
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * The Class SubdocumentoSpesaImpegnoSubimpegnoConverter.
 */
@Component
public class SubdocumentoSpesaImpegnoSubimpegnoConverter extends GenericImpegnoSubimpegnoBaseConverter<SubdocumentoSpesa, SiacTSubdoc> {
	
	/**
	 * Instantiates a new subdocumento spesa impegno subimpegno converter.
	 */
	public SubdocumentoSpesaImpegnoSubimpegnoConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
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
				
				
				SiacTMovgestT siacTMovgestT = siacRSubdocMovgestT.getSiacTMovgestT(); // lo fa già sotto:  siacTMovgestTRepository.findOne(siacRSubdocMovgestT.getSiacTMovgestT().getUid());
				
				impostaImpegnoESubImpegno(dest,siacTMovgestT);
				
			}
		}
		
		return dest;
	}
	
	@Override
	protected void impostaDatiAggiuntiviImpegno(SiacTMovgestT siacTMovgestT, Impegno impegno) {
		super.impostaDatiAggiuntiviImpegno(siacTMovgestT, impegno);
		aggiungiSiopeTipoDebito(siacTMovgestT, impegno);
	}
	@Override
	protected void impostaDatiAggiuntiviSubImpegno(SiacTMovgestT siacTMovgestT, SubImpegno subImpegno) {
		super.impostaDatiAggiuntiviSubImpegno(siacTMovgestT, subImpegno);
		aggiungiSiopeTipoDebito(siacTMovgestT, subImpegno);
	}
	

	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		
		Integer movgestId = getMovgestId(src.getImpegno(), src.getSubImpegno());
		
		if(movgestId==null){
			return null;
		}
		
		dest.setSiacRSubdocMovgestTs(new ArrayList<SiacRSubdocMovgestT>());
		
		SiacRSubdocMovgestT siacRSubdocMovgestT = new SiacRSubdocMovgestT();
		siacRSubdocMovgestT.setSiacTSubdoc(dest);
		SiacTMovgestT siacTMovgestT = new SiacTMovgestT();
		siacTMovgestT.setUid(movgestId);	
		siacRSubdocMovgestT.setSiacTMovgestT(siacTMovgestT);
		
		siacRSubdocMovgestT.setLoginOperazione(dest.getLoginOperazione());
		siacRSubdocMovgestT.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRSubdocMovgestT(siacRSubdocMovgestT);
		
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
