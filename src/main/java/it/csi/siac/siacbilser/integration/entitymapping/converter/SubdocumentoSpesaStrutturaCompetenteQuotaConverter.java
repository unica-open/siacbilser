/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.apache.commons.collections.CollectionUtils;

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocClass;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.business.service.util.NumericUtils;

/**
 * SIAC-8153
 * 
 * @author todescoa
 *
 */
public class SubdocumentoSpesaStrutturaCompetenteQuotaConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {

	/**
	 * Instantiates a new subdocumento spesa classif converter.
	 */
	public SubdocumentoSpesaStrutturaCompetenteQuotaConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		
		if(src != null && dest != null && CollectionUtils.isNotEmpty(src.getSiacRSubdocClasses())) {
			
			for(SiacRSubdocClass siacRSubdocClass : src.getSiacRSubdocClasses()){
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
						map(siacRSubdocClass.getSiacTClass(), strutturaCompetente, BilMapId.SiacTClass_StrutturaAmministrativoContabile);
						dest.setStrutturaCompetenteQuota(strutturaCompetente);
					} 
				}
			}	
			
		}
		
		return dest;
	}
	
	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		
		dest.setSiacRSubdocClasses(new ArrayList<SiacRSubdocClass>());
		
		addClassificatoreGerarchico(dest, src.getStrutturaCompetenteQuota());
		
		return dest;
	}

	/**
	 * Adds the classif.
	 *
	 * @param dest the dest
	 * @param src the src
	 */
	private void addClassificatoreGerarchico(SiacTSubdoc subdoc, ClassificatoreGerarchico classificatore) {
		if(classificatore == null || classificatore.getUid() == 0) { //facoltativo
			return;
		}
		SiacRSubdocClass siacRDocClass = new SiacRSubdocClass();
		siacRDocClass.setSiacTSubdoc(subdoc);
		SiacTClass siacTClass = new SiacTClass();
		siacTClass.setUid(classificatore.getUid());	
		siacRDocClass.setSiacTClass(siacTClass);
		
		siacRDocClass.setLoginOperazione(subdoc.getLoginOperazione());
		siacRDocClass.setSiacTEnteProprietario(subdoc.getSiacTEnteProprietario());
		
		subdoc.getSiacRSubdocClasses().add(siacRDocClass);
	}
	
}
