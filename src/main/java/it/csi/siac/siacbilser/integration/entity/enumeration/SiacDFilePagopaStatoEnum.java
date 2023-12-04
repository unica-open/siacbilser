/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.epay.epaywso.riconciliazione_versamenti_psp.types.StatoElaborazioneFlussoType;

public enum SiacDFilePagopaStatoEnum {

	 IN_ACQUISIZIONE(StatoElaborazioneFlussoType.DA_ELABORARE),
	 TRASMESSO(StatoElaborazioneFlussoType.DA_ELABORARE),
	 ANNULLATO(StatoElaborazioneFlussoType.IN_ERRORE),
	 ELABORATO_SCARTATO(StatoElaborazioneFlussoType.IN_ERRORE),
	 ELABORATO_ERRATO(StatoElaborazioneFlussoType.IN_ERRORE),
	 ELABORATO_KO(StatoElaborazioneFlussoType.IN_ERRORE),
	 ELABORATO_OK(StatoElaborazioneFlussoType.ELABORATO),
	 ELABORATO_IN_CORSO_SC(StatoElaborazioneFlussoType.DA_ELABORARE),
	 ELABORATO_IN_CORSO_ER(StatoElaborazioneFlussoType.DA_ELABORARE),
	 ELABORATO_IN_CORSO(StatoElaborazioneFlussoType.DA_ELABORARE),
	 RIFIUTATO(StatoElaborazioneFlussoType.IN_ERRORE),
	 ACQUISITO(StatoElaborazioneFlussoType.DA_ELABORARE),
	 ;
	
	private StatoElaborazioneFlussoType statoElaborazioneFlussoType;
	
	SiacDFilePagopaStatoEnum(StatoElaborazioneFlussoType statoElaborazioneFlussoType) {
		this.statoElaborazioneFlussoType = statoElaborazioneFlussoType;
	}

	public StatoElaborazioneFlussoType getStatoElaborazioneFlussoType() {
		return statoElaborazioneFlussoType;
	}
	
	public static StatoElaborazioneFlussoType toStatoElaborazioneFlussoType(String siacDFilePagopaStatoCode) {
		SiacDFilePagopaStatoEnum siacDFilePagopaStatoToStatoElaborazioneFlussoTypeEnum = 
				SiacDFilePagopaStatoEnum.valueOf(siacDFilePagopaStatoCode);

		return siacDFilePagopaStatoToStatoElaborazioneFlussoTypeEnum == null ? null :
			siacDFilePagopaStatoToStatoElaborazioneFlussoTypeEnum.getStatoElaborazioneFlussoType();
	}
	
	public String getCodice() {
		return name();
	}
}
