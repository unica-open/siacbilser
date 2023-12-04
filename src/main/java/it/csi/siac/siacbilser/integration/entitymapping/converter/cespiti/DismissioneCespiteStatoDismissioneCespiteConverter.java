/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter.cespiti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.entity.SiacDCespitiDismissioniStato;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiDismissioni;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCespitiDismissioniStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siaccespser.model.DismissioneCespite;

/**
 * The Class DismissioneCespiteStatoDismissioneCespiteConverter.
 * @author elisa
 * @version 1.0.0 - 30-08-2018
 */
@Component
public class DismissioneCespiteStatoDismissioneCespiteConverter extends ExtendedDozerConverter<DismissioneCespite, SiacTCespitiDismissioni> {
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 *  Costruttore.
	 */
	public DismissioneCespiteStatoDismissioneCespiteConverter() {
		super(DismissioneCespite.class, SiacTCespitiDismissioni.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DismissioneCespite convertFrom(SiacTCespitiDismissioni src, DismissioneCespite dest) {
		String methodName = "convertFrom";
		
		if(src.getSiacDCespitiDismissioniStato() == null){
			log.warn(methodName, "Variazione cespite [uid: " + src.getUid() + "] priva dello Stato! Controllare su DB. Entita associata: " + src.getClass().getSimpleName());
			return dest;
		}
		
		dest.setStatoDismissioneCespite(SiacDCespitiDismissioniStatoEnum.byCodice(src.getSiacDCespitiDismissioniStato().getCesDismissioniStatoCode()).getStatoDismissioneCespite());
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTCespitiDismissioni convertTo(DismissioneCespite src, SiacTCespitiDismissioni dest) {
		if(src.getStatoDismissioneCespite() == null) {
			return dest;
		}
		
		SiacDCespitiDismissioniStatoEnum siacDCespitiVariazioneStatoEnum = SiacDCespitiDismissioniStatoEnum.byStatoDismissioneCespite(src.getStatoDismissioneCespite());
		SiacDCespitiDismissioniStato siacDCespitiVariazioneStato = eef.getEntity(siacDCespitiVariazioneStatoEnum, dest.getSiacTEnteProprietario().getUid(), SiacDCespitiDismissioniStato.class);
		dest.setSiacDCespitiDismissioniStato(siacDCespitiVariazioneStato);
		return dest;
	}


}
