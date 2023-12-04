/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Arrays;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRPrimaNotaClass;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.business.service.util.NumericUtils;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;

@Component
public class PrimaNotaStrutturaCompententeConverter extends ExtendedDozerConverter<PrimaNota, SiacTPrimaNota> {

	public PrimaNotaStrutturaCompententeConverter() {
		super(PrimaNota.class, SiacTPrimaNota.class);
	}

	@Override
	public SiacTPrimaNota convertTo(PrimaNota source, SiacTPrimaNota destination) {
		if(source == null || source.getStrutturaCompetente() == null || source.getStrutturaCompetente().getUid() == 0
			// SIAC-8134 la struttura competente vale solo per le prime note libere
			|| TipoCausale.Integrata.equals(source.getTipoCausale())) return destination;
		
		SiacRPrimaNotaClass siacRPrimaNotaClass = new SiacRPrimaNotaClass();
		siacRPrimaNotaClass.setSiacTPrimaNota(destination);
		SiacTClass siacTClass = new SiacTClass();
		siacTClass.setUid(source.getStrutturaCompetente().getUid());	
		siacRPrimaNotaClass.setSiacTClass(siacTClass);
		siacRPrimaNotaClass.setDataInizioValidita(new Date());
		siacRPrimaNotaClass.setDataCreazione(new Date());
		siacRPrimaNotaClass.setDataModifica(new Date());
		siacRPrimaNotaClass.setLoginOperazione(destination.getLoginOperazione());
		siacRPrimaNotaClass.setSiacTEnteProprietario(destination.getSiacTEnteProprietario());
		
		destination.setSiacRPrimaNotaClasses(Arrays.asList(siacRPrimaNotaClass));
		
		return destination;
	}

	@Override
	public PrimaNota convertFrom(SiacTPrimaNota source, PrimaNota destination) {
		if(source == null || CollectionUtils.isEmpty(source.getSiacRPrimaNotaClasses())) {
			return destination;
		}
		
		for(SiacRPrimaNotaClass siacRSubdocClass : source.getSiacRPrimaNotaClasses()){
			if(siacRSubdocClass.getDataCancellazione() != null){
				continue;
			}
			
			if(siacRSubdocClass.getSiacTClass() != null && siacRSubdocClass.getSiacTClass().getSiacDClassTipo() != null
					&& siacRSubdocClass.getSiacTClass().getSiacDClassTipo().getClassifTipoCode() != null
					&& NumericUtils.valorizzatoEMaggioreDiZero(siacRSubdocClass.getSiacTClass().getClassifId())) {
				
				String classifTipoCode = siacRSubdocClass.getSiacTClass().getSiacDClassTipo().getClassifTipoCode();
				SiacDClassTipoEnum tipo = SiacDClassTipoEnum.byCodice(classifTipoCode);
				if(tipo == SiacDClassTipoEnum.Cdc){
					StrutturaAmministrativoContabile strutturaCompetente = tipo.getCodificaInstance();
					map(siacRSubdocClass.getSiacTClass(), strutturaCompetente, BilMapId.SiacTClass_StrutturaAmministrativoContabile_Reduced);
					destination.setStrutturaCompetente(strutturaCompetente);
				} 
			}
		}	
		
		return destination;
	}
	
}
