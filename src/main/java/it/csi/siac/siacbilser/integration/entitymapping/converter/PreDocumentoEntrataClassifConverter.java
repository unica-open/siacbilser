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
import it.csi.siac.siacbilser.model.ContoCorrentePredocumentoEntrata;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;

/**
 * The Class PreDocumentoClassifConverter.
 */
@Component
public class PreDocumentoEntrataClassifConverter extends ExtendedDozerConverter<PreDocumentoEntrata , SiacTPredoc > {
	
	/**
	 * Instantiates a new pre documento classif converter.
	 */
	public PreDocumentoEntrataClassifConverter() {
		super(PreDocumentoEntrata.class, SiacTPredoc.class);
	}

	
	@Override
	public PreDocumentoEntrata convertFrom(SiacTPredoc src, PreDocumentoEntrata dest) {
		
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
					continue;
				}
				
				if(tipo.getCodificaClass().equals(ContoCorrentePredocumentoEntrata.class) ){
					ContoCorrentePredocumentoEntrata cc = new ContoCorrentePredocumentoEntrata();
					map(siacTClass,cc,BilMapId.SiacTClass_ClassificatoreGenerico);
					dest.setContoCorrente(cc);
				}
							
			}
		}
		
		return dest;
	}


	@Override
	public SiacTPredoc convertTo(PreDocumentoEntrata src, SiacTPredoc dest) {
		dest.setSiacRPredocClasses(new ArrayList<SiacRPredocClass>());
		
		addClassif(dest, src.getStrutturaAmministrativoContabile());
		addClassif(dest, src.getContoCorrente());
		
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
