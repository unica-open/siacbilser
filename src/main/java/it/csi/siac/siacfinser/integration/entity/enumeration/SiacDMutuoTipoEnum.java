/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entity.enumeration;

import it.csi.siac.siacfinser.integration.entity.converter.EnumEntityFin;
import it.csi.siac.siacfinser.model.mutuo.Mutuo.TipoMutuo;

@EnumEntityFin(entityName="SiacDMutuoTipoFin", idPropertyName="mutTipoId", codePropertyName="mutTipoCode")
public enum SiacDMutuoTipoEnum {

	
//	{RIS, AVL, BOC, FID, GAR, PRE}
	
	RiscossioneCompleta("RIS", TipoMutuo.RIS),
	AvanzamentoLavori("AVL", TipoMutuo.AVL),
	Boc("BOC", TipoMutuo.BOC),
	Fideiussione("FID", TipoMutuo.FID),
	Garanzie("GAR", TipoMutuo.GAR),
	PrestitoFlessibile("PRE", TipoMutuo.PRE);
	
	private String codice;
	private TipoMutuo tipoMutuo;
	

	/**
	 * @param codice
	 */
	SiacDMutuoTipoEnum(String codice, TipoMutuo tipoMutuo){
		this.codice = codice;
		this.tipoMutuo = tipoMutuo;
	}
	
	public String getCodice() {
		return codice;
	}
	
	public TipoMutuo getTipoMutuo() {
		return tipoMutuo;
	}

	public static SiacDMutuoTipoEnum byCodice(String codice){
		for(SiacDMutuoTipoEnum e : SiacDMutuoTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDMutuoTipoEnum");
	}
	
	public static SiacDMutuoTipoEnum byTipoMutuo(TipoMutuo tipoMutuo){
		for(SiacDMutuoTipoEnum e : SiacDMutuoTipoEnum.values()){
			if(e.getTipoMutuo().equals(tipoMutuo)){
				return e;
			}
		}
		throw new IllegalArgumentException("Lo tipo mutuo "+ tipoMutuo + " non ha un mapping corrispondente in SiacDMutuoTipoEnum");
	}
	
	
	
}
