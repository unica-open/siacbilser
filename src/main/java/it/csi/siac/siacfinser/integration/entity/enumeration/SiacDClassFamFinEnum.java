/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.enumeration;

import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

//@EnumEntityFin(entityName="SiacDClassFamFin", idPropertyName="classifFamId", codePropertyName="classifFamCode")
public enum SiacDClassFamFinEnum {
	
	
//	CR-2023 ContoEconomico("00018", ElementoContoEconomico.class),
	PianoDeiConti("00008", ElementoPianoDeiConti.class),
	StrutturaAmministrativaContabile("00005", StrutturaAmministrativoContabile.class);
	
	private String codice;
	private Class<? extends Codifica> codificaClass;
		

	SiacDClassFamFinEnum(String codice, Class<? extends Codifica> codificaClass){		
		this.codice = codice;
		this.codificaClass = codificaClass;
	}

	

	public String getCodice() {
		return codice;
	}

	public Class<? extends Codifica> getCodificaClass() {
		return codificaClass;
	}
	
	
	public static SiacDClassFamFinEnum byCodice(String codice){
		for(SiacDClassFamFinEnum e : SiacDClassFamFinEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDClassFamFinEnum");
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Codifica> T getCodificaInstance() {
		try {
			return (T) codificaClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Eccezione istanziamento SiacTipoClassificatore."+name()+"->"+codificaClass + " ["+codice+"]",e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Impossibile accedere al costruttore vuoto di  "+codificaClass + " ["+codice+"]",e);
		}
	}

}
