/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTClassRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocClass;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoAvviso;

/**
 * The Class SubdocumentoSpesaClassifConverter.
 */
@Component
public class SubdocumentoSpesaClassifConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {
	
	
	/** The siac t class repository. */
	@Autowired
	private SiacTClassRepository siacTClassRepository;
	
	
	/**
	 * Instantiates a new subdocumento spesa classif converter.
	 */
	public SubdocumentoSpesaClassifConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		
		
		for(SiacRSubdocClass siacRSubdocClass : src.getSiacRSubdocClasses()){
			if(siacRSubdocClass.getDataCancellazione()!=null){
				continue;
			}
			
			//SiacTClass siacTClass = siacRSubdocClass.getSiacTClass();
			
			SiacTClass siacTClass = siacTClassRepository.findOne(siacRSubdocClass.getSiacTClass().getUid());
			
			String classifTipoCode = siacTClass.getSiacDClassTipo().getClassifTipoCode();
			SiacDClassTipoEnum tipo = SiacDClassTipoEnum.byCodice(classifTipoCode);
			if(tipo == SiacDClassTipoEnum.TipoAvviso){
				TipoAvviso tipoAvviso = tipo.getCodificaInstance();
				map(siacTClass,tipoAvviso,BilMapId.SiacTClass_ClassificatoreGenerico);
				dest.setTipoAvviso(tipoAvviso);
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
		
		addClassif(dest, src.getTipoAvviso());
		
		return dest;
	}

	/**
	 * Adds the classif.
	 *
	 * @param dest the dest
	 * @param src the src
	 */
	private void addClassif(SiacTSubdoc dest, Codifica src) {
		if(src==null) { //facoltativo
			return;
		}
		SiacRSubdocClass siacRDocClass = new SiacRSubdocClass();
		siacRDocClass.setSiacTSubdoc(dest);
		SiacTClass siacTClass = new SiacTClass();
		siacTClass.setUid(src.getUid());	
		siacRDocClass.setSiacTClass(siacTClass);
		
		siacRDocClass.setLoginOperazione(dest.getLoginOperazione());
		siacRDocClass.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRSubdocClass(siacRDocClass);
	}

}
