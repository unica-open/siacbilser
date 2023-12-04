/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacTPeriodoRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTQuadroEconomicoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTCronopElem;
import it.csi.siac.siacbilser.integration.entity.SiacTCronopElemDet;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacbilser.integration.entity.SiacTQuadroEconomico;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Esito;

// TODO: Auto-generated Javadoc
/**
 * The Class DettaglioCronoprogrammaUscitaImportiConverter.
 */
@Component
public class DettaglioCronoprogrammaUscitaImportiConverter extends ExtendedDozerConverter<DettaglioUscitaCronoprogramma, SiacTCronopElem > {
	

	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	@Autowired
	private SiacTQuadroEconomicoRepository siacTQuadroEconomicoRepository;

	/** The siac t periodo repository. */
	@Autowired
	private SiacTPeriodoRepository siacTPeriodoRepository;
	
	
	/**
	 * Instantiates a new dettaglio cronoprogramma uscita importi converter.
	 */
	public DettaglioCronoprogrammaUscitaImportiConverter() {
		super(DettaglioUscitaCronoprogramma.class, SiacTCronopElem.class);
	}
	
	

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DettaglioUscitaCronoprogramma convertFrom(SiacTCronopElem src, DettaglioUscitaCronoprogramma dest) {
		final String methodName = "convertFrom";
		
		BeanWrapper bwdest = PropertyAccessorFactory.forBeanPropertyAccess(dest);
		
		for (SiacTCronopElemDet det : src.getSiacTCronopElemDets()) {
			
			if(det.getDataCancellazione()!=null){
				continue;
			}
			
			if(det.getSiacTPeriodo() != null && det.getSiacTPeriodo().getAnno() != null) {
				dest.setAnnoCompetenza(Integer.valueOf(det.getSiacTPeriodo().getAnno()));
			}
			
			String annoEntrataEntityStr = det.getAnnoEntrata();
			if(annoEntrataEntityStr!=null){
				dest.setAnnoEntrata(Integer.valueOf(annoEntrataEntityStr));
			}
			
			log.debug(methodName,"importo:"+det.getCronopElemDetImporto() + " tipo:"+det.getSiacDBilElemDetTipo().getElemDetTipoCode());
			String importiCapitoloFieldName = SiacDBilElemDetTipoEnum.byCodice(det.getSiacDBilElemDetTipo().getElemDetTipoCode()).getImportiCapitoloFieldName();
			try{
				bwdest.setPropertyValue(importiCapitoloFieldName, det.getCronopElemDetImporto());
			} catch (BeansException be){
				log.warn(methodName, "Cannot set importo:"+det.getCronopElemDetImporto() + " tipo:"+det.getSiacDBilElemDetTipo().getElemDetTipoCode(),be);
			}
			
			aggiungiDatiQuadroEconomico(det,dest);
			
			dest.setImportoQuadroEconomico(det.getQuadroEconomicoDetImporto());
		
		}
		
		return dest;
	}



	private void aggiungiDatiQuadroEconomico(SiacTCronopElemDet det, DettaglioUscitaCronoprogramma dest) {
		if(det.getSiacTQuadroEconomicoPadre() == null && det.getSiacTQuadroEconomicoFiglio() == null) {
			return;
		}
		int uidQuadro = det.getSiacTQuadroEconomicoFiglio() != null? det.getSiacTQuadroEconomicoFiglio().getUid() : det.getSiacTQuadroEconomicoPadre().getUid();
		SiacTQuadroEconomico siacTQuadroEconomico = siacTQuadroEconomicoRepository.findOne(Integer.valueOf(uidQuadro));
		dest.setQuadroEconomico(mapNotNull(siacTQuadroEconomico, QuadroEconomico.class, BilMapId.SiacTQuadroEconomico_QuadroEconomico));
	}



	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCronopElem convertTo(DettaglioUscitaCronoprogramma src, SiacTCronopElem dest) {
		final String methodName = "convertTo";		
		
		Map<String,BigDecimal> fieldsNameValueMap = Utility.getFieldNameValueMapByAnnotationImportoPersistente(src);
		
		List<SiacTCronopElemDet> siacTCronopElemDet = new ArrayList<SiacTCronopElemDet>();
		
		for (Entry<String, BigDecimal> fieldNameValue : fieldsNameValueMap.entrySet()) {
			
			String fieldName = fieldNameValue.getKey();
			BigDecimal fieldValue  = fieldNameValue.getValue();
			
			log.debug(methodName,"Mapping importo: "+ fieldName+" value: "+fieldValue);
			
			if(fieldValue!=null) {
				
				SiacTCronopElemDet det = new SiacTCronopElemDet();
				det.setCronopElemDetImporto(fieldValue);	
				
				SiacDBilElemDetTipoEnum siacDBilElemDetTipoEnum = SiacDBilElemDetTipoEnum.byImportiCapitoloModelFieldName(fieldName);
				
				SiacDBilElemDetTipo siacDBilElemDetTipo = eef.getEntity(siacDBilElemDetTipoEnum,dest.getSiacTEnteProprietario().getEnteProprietarioId(),  SiacDBilElemDetTipo.class); //.getEntity();
				det.setSiacDBilElemDetTipo(siacDBilElemDetTipo);	
		
				det.setSiacTCronopElem(dest);
				det.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
				det.setLoginOperazione(dest.getLoginOperazione());
				
				//det.setCronopElemDetDesc(""+siacDBilElemDetTipoEnum.getCodice());
				
				// SIAC-4153-4103: aggiunta if per permettere all'anno di essere null
				if(src.getAnnoCompetenza() != null) {
					SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(""+src.getAnnoCompetenza(),  dest.getSiacTEnteProprietario());
					// FIXME: valutare se abbia senso spostare questa eccezione fuori dal converter
					if(siacTPeriodo == null) {
						throw new BusinessException("Periodo non censito", Esito.FALLIMENTO, true);
					}
					det.setSiacTPeriodo(siacTPeriodo);
				}
				
				
				
				det.setAnnoEntrata(src.getAnnoEntrata()!=null?""+src.getAnnoEntrata():null);
				
				aggiungiSiacTQuadroEconomicoPadreFiglio(src, det);
				
				det.setQuadroEconomicoDetImporto(src.getImportoQuadroEconomico());

				siacTCronopElemDet.add(det);
			
			}
		}
		
		dest.setSiacTCronopElemDets(siacTCronopElemDet);
		
		return dest;
	}



	private void aggiungiSiacTQuadroEconomicoPadreFiglio(DettaglioUscitaCronoprogramma src, SiacTCronopElemDet det) {
		if(src.getQuadroEconomico() == null) {
			return;
		}
		SiacTQuadroEconomico siacTQuadroEconomico = siacTQuadroEconomicoRepository.findOne(Integer.valueOf(src.getQuadroEconomico().getUid()));
		if(siacTQuadroEconomico == null) {
			return;
		}
		if(siacTQuadroEconomico.getSiacTQuadroEconomicoPadre() == null) {
			det.setSiacTQuadroEconomicoPadre(siacTQuadroEconomico);
			return;
		}
		det.setSiacTQuadroEconomicoFiglio(siacTQuadroEconomico);
		det.setSiacTQuadroEconomicoPadre( siacTQuadroEconomico.getSiacTQuadroEconomicoPadre());
	}	

}
