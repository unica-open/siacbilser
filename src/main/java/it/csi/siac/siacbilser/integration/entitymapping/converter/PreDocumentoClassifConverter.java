/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRPredocClass;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.PreDocumento;

// TODO: Auto-generated Javadoc
/**
 * The Class PreDocumentoClassifConverter.
 */
@Component
public class PreDocumentoClassifConverter extends ExtendedDozerConverter<PreDocumento<?, ?>, SiacTPredoc > {
	
	/**
	 * Instantiates a new pre documento classif converter.
	 */
	@SuppressWarnings("unchecked")
	public PreDocumentoClassifConverter() {
		super((Class<PreDocumento<?, ?>>)(Class<?>)PreDocumento.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumento<?, ?> convertFrom(SiacTPredoc src, PreDocumento<?, ?> dest) {
		
		if(src.getSiacRPredocClasses()!=null){
			for(SiacRPredocClass siacRPredocClass : src.getSiacRPredocClasses()){
				if(siacRPredocClass.getDataCancellazione()!=null){
					continue;
				}
				
				SiacTClass siacTClass = siacRPredocClass.getSiacTClass();
				
				String classifTipoCode = siacTClass.getSiacDClassTipo().getClassifTipoCode();
				SiacDClassTipoEnum tipo = SiacDClassTipoEnum.byCodice(classifTipoCode);
				if(tipo.getCodificaClass().equals(StrutturaAmministrativoContabile.class) ){
					StrutturaAmministrativoContabile sac = new StrutturaAmministrativoContabile();
					map(siacTClass,sac,BilMapId.SiacTClass_ClassificatoreGerarchico);
					dest.setStrutturaAmministrativoContabile(sac);
				} 
							
			}
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPredoc convertTo(PreDocumento<?, ?> src, SiacTPredoc dest) {
		
		dest.setSiacRPredocClasses(new ArrayList<SiacRPredocClass>());
		
		addClassif(dest, src.getStrutturaAmministrativoContabile());
		
		return dest;
	}
	
	/**
	 * Adds the classif.
	 *
	 * @param dest the dest
	 * @param src the src
	 */
	private void addClassif(SiacTPredoc dest, Codifica src) {
		if(src==null || src.getUid() == 0) { //facoltativo
			return;
		}
		SiacRPredocClass siacRPredocClass = new SiacRPredocClass();
		siacRPredocClass.setSiacTPredoc(dest);
		SiacTClass siacTClass = new SiacTClass();
		siacTClass.setUid(src.getUid());	
		siacRPredocClass.setSiacTClass(siacTClass);
		
		siacRPredocClass.setLoginOperazione(dest.getLoginOperazione());
		siacRPredocClass.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRPredocClass(siacRPredocClass);
	}



	

}
