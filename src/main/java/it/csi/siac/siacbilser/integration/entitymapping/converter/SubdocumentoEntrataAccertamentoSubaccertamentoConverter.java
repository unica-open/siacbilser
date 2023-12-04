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
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.Accertamento;

/**
 * The Class SubdocumentoEntrataAccertamentoSubaccertamentoConverter.
 */
@Component
public class SubdocumentoEntrataAccertamentoSubaccertamentoConverter extends GenericAccertamentoSubaccertamentoBaseConverter<SubdocumentoEntrata, SiacTSubdoc> {
	
	
	/**
	 * Instantiates a new subdocumento entrata accertamento subaccertamento converter.
	 */
	public SubdocumentoEntrataAccertamentoSubaccertamentoConverter() {
		super(SubdocumentoEntrata.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoEntrata convertFrom(SiacTSubdoc src, SubdocumentoEntrata dest) {
		
		if(src.getSiacRSubdocMovgestTs()!=null) {
			for(SiacRSubdocMovgestT siacRSubdocMovgestT : src.getSiacRSubdocMovgestTs()){
				if(siacRSubdocMovgestT.getDataCancellazione()!=null) {
					continue;
				}
				
				SiacTMovgestT siacTMovgestT = siacRSubdocMovgestT.getSiacTMovgestT(); // siacTMovgestTRepository.findOne(siacRSubdocMovgestT.getSiacTMovgestT().getUid());
				
				impostaAccertamentoESubAccertamento(dest, siacTMovgestT);
				
						
				
			}
		}
		
		return dest;
	}
	

	@Override
	public SiacTSubdoc convertTo(SubdocumentoEntrata src, SiacTSubdoc dest) {
		
		Integer movgestId = getMovgestId(src.getAccertamento(), src.getSubAccertamento());
		
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
	protected void aggiungiInformazioniCapitoloAdAccertamento(SiacTMovgestT siacTMovgestT, Accertamento accertamento) {
		for(SiacRMovgestBilElem r :siacTMovgestT.getSiacTMovgest().getSiacRMovgestBilElems()){
			if(r.getDataCancellazione()==null){
				SiacTBilElem siacTBilElem = siacTBilElemRepository.findOne(r.getSiacTBilElem().getElemId());
				CapitoloEntrataGestione cap = new CapitoloEntrataGestione();
				map(siacTBilElem, cap, BilMapId.SiacTBilElem_Capitolo_Base);
				
				SiacRBilElemAttr siacRBilElemAttr = siacTBilElemRepository.findBilElemAttrByTipoAttrCode(siacTBilElem.getUid(), SiacTAttrEnum.FlagRilevanteIva.getCodice());
				if(siacRBilElemAttr!=null) {
					cap.setFlagRilevanteIva("S".equals(siacRBilElemAttr.getBoolean_()));
				}
				
				// Elemento del piano dei conti
				SiacRBilElemClass siacRBilElemClass = siacTBilElemRepository.findBilElemClassByTipoClassCodes(siacTBilElem.getUid(),
						Arrays.asList(SiacDClassTipoEnum.QuartoLivelloPdc.getCodice(), SiacDClassTipoEnum.QuintoLivelloPdc.getCodice()));
				if(siacRBilElemClass != null) {
					ElementoPianoDeiConti epdc = new ElementoPianoDeiConti();
					SiacTClass siacTClass = siacRBilElemClass.getSiacTClass();
					epdc.setUid(siacTClass.getUid());
					epdc.setCodice(siacTClass.getClassifCode());
					epdc.setDescrizione(siacTClass.getClassifDesc());
					
					TipoClassificatore tipoClassificatore = new TipoClassificatore();
					tipoClassificatore.setCodice(siacRBilElemClass.getSiacTClass().getSiacDClassTipo().getClassifTipoCode());
					tipoClassificatore.setUid(siacRBilElemClass.getSiacTClass().getSiacDClassTipo().getUid());
					epdc.setTipoClassificatore(tipoClassificatore);
					cap.setElementoPianoDeiConti(epdc);
				}
				
				// Categoria
				SiacRBilElemClass siacRBilElemClassCAT = siacTBilElemRepository.findBilElemClassByTipoClassCodes(siacTBilElem.getUid(),
						Arrays.asList(SiacDClassTipoEnum.Categoria.getCodice(), SiacDClassTipoEnum.Categoria.getCodice()));
				if(siacRBilElemClassCAT != null) {
					CategoriaTipologiaTitolo ctt = new CategoriaTipologiaTitolo();
					SiacTClass siacTClass = siacRBilElemClassCAT.getSiacTClass();
					ctt.setUid(siacTClass.getUid());
					ctt.setCodice(siacTClass.getClassifCode());
					ctt.setDescrizione(siacTClass.getClassifDesc());
					
					TipoClassificatore tipoClassificatore = new TipoClassificatore();
					tipoClassificatore.setCodice(siacRBilElemClassCAT.getSiacTClass().getSiacDClassTipo().getClassifTipoCode());
					tipoClassificatore.setUid(siacRBilElemClassCAT.getSiacTClass().getSiacDClassTipo().getUid());
					ctt.setTipoClassificatore(tipoClassificatore);
					cap.setCategoriaTipologiaTitolo(ctt);
				}
				
				accertamento.setCapitoloEntrataGestione(cap);
				
				break;
			}
		}
		
	}

}
