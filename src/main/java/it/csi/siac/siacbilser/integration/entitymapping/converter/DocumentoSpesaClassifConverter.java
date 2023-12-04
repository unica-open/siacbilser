/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTClassRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRDocClass;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoImpresa;

/**
 * The Class DocumentoSpesaClassifConverter.
 */
@Component
public class DocumentoSpesaClassifConverter extends ExtendedDozerConverter<DocumentoSpesa, SiacTDoc> {
	
	@Autowired
	private SiacTClassRepository siacTClassRepository;
	
	
	/**
	 * Instantiates a new documento spesa classif converter.
	 */
	public DocumentoSpesaClassifConverter() {
		super(DocumentoSpesa.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DocumentoSpesa convertFrom(SiacTDoc src, DocumentoSpesa dest) {
		
		final String methodName = "convertFrom";
		
		for(SiacRDocClass siacRDocClass : src.getSiacRDocClasses()){
			if(siacRDocClass.getDataCancellazione()!=null){
				continue;
			}
			
			SiacTClass siacTClass = siacRDocClass.getSiacTClass();
			log.debug(methodName,  "siacRDocClass.getSiacTClass().getUid() " +siacTClass.getUid());
			
			String classifTipoCode = siacTClassRepository.findCodiceTipoClassificatoreByClassifId(siacTClass.getUid());
			
			log.debug(methodName, "classifTipoCode " + classifTipoCode);
			
			SiacDClassTipoEnum tipo = SiacDClassTipoEnum.byCodice(classifTipoCode);
			if(SiacDClassTipoEnum.TipoImpresa.equals(tipo)){
				TipoImpresa tipoImpresa = tipo.getCodificaInstance();
				map(siacTClass,tipoImpresa,BilMapId.SiacTClass_ClassificatoreGenerico);
				dest.setTipoImpresa(tipoImpresa);
			} else if(SiacDClassTipoEnum.CentroDiResponsabilita.equals(tipo) || SiacDClassTipoEnum.Cdc.equals(tipo)){
				StrutturaAmministrativoContabile sac = tipo.getCodificaInstance();
				map(siacTClass, sac, BilMapId.SiacTClass_ClassificatoreGerarchico);
				dest.setStrutturaAmministrativoContabile(sac);
			}
		}
		
		return dest;
	}
	

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTDoc convertTo(DocumentoSpesa src, SiacTDoc dest) {
		
		dest.setSiacRDocClasses(new ArrayList<SiacRDocClass>());
		
		addClassif(dest, src.getTipoImpresa());
		addClassif(dest, src.getStrutturaAmministrativoContabile());
		
		return dest;
	}

	/**
	 * Adds the classif.
	 *
	 * @param dest the dest
	 * @param src the src
	 */
	private void addClassif(SiacTDoc dest, Codifica src) {
		if(src==null || src.getUid() == 0) { //facoltativo
			return;
		}
		SiacRDocClass siacRDocClass = new SiacRDocClass();
		siacRDocClass.setSiacTDoc(dest);
		SiacTClass siacTClass = new SiacTClass();
		siacTClass.setUid(src.getUid());	
		siacRDocClass.setSiacTClass(siacTClass);
		
		siacRDocClass.setLoginOperazione(dest.getLoginOperazione());
		siacRDocClass.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRDocClass(siacRDocClass);
	}

}
