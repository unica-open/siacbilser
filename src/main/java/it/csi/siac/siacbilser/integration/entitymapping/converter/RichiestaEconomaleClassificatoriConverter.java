/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRRichiestaEconClass;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;

/**
 * The Class DocumentoEntrataSubdocumentoIvaConverter.
 */
@Component
public class RichiestaEconomaleClassificatoriConverter extends ExtendedDozerConverter<RichiestaEconomale, SiacTRichiestaEcon> {
	
	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public RichiestaEconomaleClassificatoriConverter() {
		super(RichiestaEconomale.class, SiacTRichiestaEcon.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public RichiestaEconomale convertFrom(SiacTRichiestaEcon src, RichiestaEconomale dest) {
		if(src.getSiacRRichiestaEconClasses() == null){
			return dest;
		}
		List<ClassificatoreGenerico> classificatori = new ArrayList<ClassificatoreGenerico>();
		for(SiacRRichiestaEconClass siacRRichiestaEconClass : src.getSiacRRichiestaEconClasses()){
			if(siacRRichiestaEconClass.getDataCancellazione() == null){
				ClassificatoreGenerico classificatore = new ClassificatoreGenerico();
				map(siacRRichiestaEconClass.getSiacTClass(),classificatore,BilMapId.SiacTClass_ClassificatoreGenerico);
				classificatori.add(classificatore);
			}
		}
		dest.setClassificatoriGenerici(classificatori);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTRichiestaEcon convertTo(RichiestaEconomale src, SiacTRichiestaEcon dest) {
		if(src.getClassificatoriGenerici() == null || src.getClassificatoriGenerici().isEmpty()){
			return dest;
		}
		List<SiacRRichiestaEconClass> siacRRichiestaEconClasses = new ArrayList<SiacRRichiestaEconClass>();
		for(ClassificatoreGenerico classificatore : src.getClassificatoriGenerici()){
			SiacRRichiestaEconClass siacRRichiestaEconClass = new SiacRRichiestaEconClass();
			siacRRichiestaEconClass.setSiacTRichiestaEcon(dest);
			siacRRichiestaEconClass.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			siacRRichiestaEconClass.setLoginOperazione(dest.getLoginOperazione());
			SiacTClass siacTClass = new SiacTClass();
			siacTClass.setUid(classificatore.getUid());
			siacRRichiestaEconClass.setSiacTClass(siacTClass );
			siacRRichiestaEconClasses.add(siacRRichiestaEconClass);
		}
		dest.setSiacRRichiestaEconClasses(siacRRichiestaEconClasses);
		return dest;
	}



	

}
