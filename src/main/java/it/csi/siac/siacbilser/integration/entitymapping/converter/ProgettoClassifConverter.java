/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRProgrammaClass;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTProgramma;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.TipoAmbito;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Converter per i classificatori del progetto
 * @author elisa
 * @version 1.0.0 - 28-02-2019
 *
 */
@Component
public class ProgettoClassifConverter extends ExtendedDozerConverter<Progetto, SiacTProgramma> {
	
	/**
	 * Instantiates a new progetto tipo ambito converter.
	 */
	public ProgettoClassifConverter() {
		super(Progetto.class, SiacTProgramma.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Progetto convertFrom(SiacTProgramma src, Progetto dest) {
		if(src.getSiacRProgrammaClasses() == null) {
			return dest;
		}
		
		for(SiacRProgrammaClass siacRProgrammaClass : src.getSiacRProgrammaClasses()) {
			if(siacRProgrammaClass.getDataCancellazione() != null) {
				continue;
			}
			
			SiacTClass siacTClass = siacRProgrammaClass.getSiacTClass();
			SiacDClassTipoEnum siacDClassTipoEnum = SiacDClassTipoEnum.byCodiceEvenNull(siacTClass.getSiacDClassTipo().getClassifTipoCode());
			
			if(SiacDClassTipoEnum.TipoAmbito.equals(siacDClassTipoEnum)) {
				TipoAmbito tipoAmbito = new TipoAmbito();
				map(siacTClass, tipoAmbito, BilMapId.SiacTClass_ClassificatoreGenerico);
				dest.setTipoAmbito(tipoAmbito);
			}
			
			if(SiacDClassTipoEnum.Cdc.equals(siacDClassTipoEnum) || SiacDClassTipoEnum.CentroDiRespondabilita.equals(siacDClassTipoEnum)) {
				StrutturaAmministrativoContabile sac = siacDClassTipoEnum.getCodificaInstance();
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
	public SiacTProgramma convertTo(Progetto src, SiacTProgramma dest) {
		dest.setSiacRProgrammaClasses(new ArrayList<SiacRProgrammaClass>());
		
		addClassif(dest, src.getTipoAmbito());
		addClassif(dest, src.getStrutturaAmministrativoContabile());
		
		return dest;
	}
	
	/**
	 * Adds the classif.
	 *
	 * @param dest the dest
	 * @param src the src
	 */
	private void addClassif(SiacTProgramma dest, Codifica src) {
		if(src==null || src.getUid() == 0) { //facoltativo
			return;
		}
		SiacRProgrammaClass siacRProgrammaClass = new SiacRProgrammaClass();
		siacRProgrammaClass.setSiacTProgramma(dest);
		SiacTClass siacTClass = new SiacTClass();
		siacTClass.setUid(src.getUid());	
		siacRProgrammaClass.setSiacTClass(siacTClass);
		
		siacRProgrammaClass.setLoginOperazione(dest.getLoginOperazione());
		siacRProgrammaClass.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRProgrammaClass(siacRProgrammaClass);
	}
	
}
