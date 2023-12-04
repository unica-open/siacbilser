/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTClassRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDCausale;
import it.csi.siac.siacbilser.integration.entity.SiacRCausaleClass;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.Causale;

/**
 * The Class CausaleClassifConverter.
 */
@Component
public class CausaleClassifConverter extends ExtendedDozerConverter<Causale , SiacDCausale > {
	
	@Autowired
	private SiacTClassRepository siacTClassRepository;

	/**
	 * Instantiates a new causale classif converter.
	 */
	public CausaleClassifConverter() {
		super(Causale.class, SiacDCausale.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Causale convertFrom(SiacDCausale src, Causale dest) {
		
		if(src.getSiacRCausaleClasses()!=null){
			for(SiacRCausaleClass siacRCausaleClass : src.getSiacRCausaleClasses()){
				if((src.getDateToExtract() == null && siacRCausaleClass.getDataCancellazione()!=null )
						|| (src.getDateToExtract() != null && !src.getDateToExtract().equals(siacRCausaleClass.getDataInizioValidita()))){
					continue;
				}
				
				SiacTClass siacTClass = siacTClassRepository.findOne(siacRCausaleClass.getSiacTClass().getUid());
				
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
	public SiacDCausale convertTo(Causale src, SiacDCausale dest) {
		
		dest.setSiacRCausaleClasses(new ArrayList<SiacRCausaleClass>());
		
		addClassif(dest, src.getStrutturaAmministrativoContabile());
		
		return dest;
	}
	
	/**
	 * Adds the classif.
	 *
	 * @param dest the dest
	 * @param src the src
	 */
	private void addClassif(SiacDCausale dest, Codifica src) {
		if(src==null || src.getUid() == 0) { //facoltativo
			return;
		}
		SiacRCausaleClass siacRCausaleClass = new SiacRCausaleClass();
		siacRCausaleClass.setSiacDCausale(dest);
		SiacTClass siacTClass = new SiacTClass();
		siacTClass.setUid(src.getUid());	
		siacRCausaleClass.setSiacTClass(siacTClass);
		
		siacRCausaleClass.setLoginOperazione(dest.getLoginOperazione());
		siacRCausaleClass.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRCausaleClass(siacRCausaleClass);
	}



	

}
