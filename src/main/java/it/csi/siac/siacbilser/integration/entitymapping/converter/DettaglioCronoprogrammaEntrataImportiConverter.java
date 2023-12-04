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

import org.dozer.DozerConverter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacTPeriodoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDBilElemDetTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTCronopElem;
import it.csi.siac.siacbilser.integration.entity.SiacTCronopElemDet;
import it.csi.siac.siacbilser.integration.entity.SiacTPeriodo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemDetTipoEnum;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.Esito;

// TODO: Auto-generated Javadoc
/**
 * The Class DettaglioCronoprogrammaEntrataImportiConverter.
 */
@Component
public class DettaglioCronoprogrammaEntrataImportiConverter extends DozerConverter<DettaglioEntrataCronoprogramma, SiacTCronopElem> {	

	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	/** The siac t periodo repository. */
	@Autowired
	private SiacTPeriodoRepository siacTPeriodoRepository;
	
	/**
	 * Instantiates a new dettaglio cronoprogramma entrata importi converter.
	 */
	public DettaglioCronoprogrammaEntrataImportiConverter() {
		super(DettaglioEntrataCronoprogramma.class, SiacTCronopElem.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DettaglioEntrataCronoprogramma convertFrom(SiacTCronopElem src, DettaglioEntrataCronoprogramma dest) {
		final String methodName = "convertFrom";
		
		BeanWrapper bwdest = PropertyAccessorFactory.forBeanPropertyAccess(dest);
		
		for (SiacTCronopElemDet det : src.getSiacTCronopElemDets()) {
			
			if(det.getDataCancellazione()!=null){
				continue;
			}
					
			String annoCompetenzaEntityStr = det.getSiacTPeriodo().getAnno();
			if(annoCompetenzaEntityStr!=null){
				dest.setAnnoCompetenza(Integer.valueOf(annoCompetenzaEntityStr));
			}
			
//			String annoEntrataEntityStr = siacTCronopElemDet.getAnnoEntrata();
//			if(annoCompetenzaEntityStr!=null){
//				dest.setAnnoEntrata(new Integer(annoEntrataEntityStr));
//			}			
						
			log.debug(methodName,"importo:"+det.getCronopElemDetImporto() + " tipo:"+det.getSiacDBilElemDetTipo().getElemDetTipoCode());			
			String importiCapitoloFieldName = SiacDBilElemDetTipoEnum.byCodice(det.getSiacDBilElemDetTipo().getElemDetTipoCode()).getImportiCapitoloFieldName();
			try{
				bwdest.setPropertyValue(importiCapitoloFieldName, det.getCronopElemDetImporto());
			} catch (BeansException be){
				log.warn(methodName, "Cannot set importo:"+det.getCronopElemDetImporto() + " tipo:"+det.getSiacDBilElemDetTipo().getElemDetTipoCode(),be);								
			}
			
			//result.add(ic); viene già aggiunto nel metodo byAnnoCompetenza
			
		
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCronopElem convertTo(DettaglioEntrataCronoprogramma src, SiacTCronopElem dest) {
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
				
				SiacTPeriodo siacTPeriodo = siacTPeriodoRepository.findByAnnoAndEnteProprietario(""+src.getAnnoCompetenza(),  dest.getSiacTEnteProprietario());
				// FIXME: valutare se abbia senso spostare questa eccezione fuori dal converter
				if(siacTPeriodo == null) {
					throw new BusinessException("Periodo non censito", Esito.FALLIMENTO, true);
				}
				
				det.setSiacTPeriodo(siacTPeriodo);				
				
				//det.setAnnoEntrata(src.getAnnoEntrata()!=null?""+src.getAnnoEntrata():null);
				

				siacTCronopElemDet.add(det);
			
			}
		}
		
		dest.setSiacTCronopElemDets(siacTCronopElemDet);
		
		return dest;
	}	
	
	
	
	


}
