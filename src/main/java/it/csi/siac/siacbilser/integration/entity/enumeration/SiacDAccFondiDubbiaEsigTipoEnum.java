/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaBase;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaGestione;
import it.csi.siac.siacbilser.model.fcde.AccantonamentoFondiDubbiaEsigibilitaRendiconto;
import it.csi.siac.siacbilser.model.fcde.TipoAccantonamentoFondiDubbiaEsigibilita;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaModelDetail;
import it.csi.siac.siacbilser.model.fcde.modeldetail.AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccommonser.util.dozer.MapId;

/**
 * The Enum SiacDDocStatoEnum.
 */
@EnumEntity(entityName="SiacDAccFondiDubbiaEsigTipo", idPropertyName="afdeTipoId", codePropertyName="afdeTipoCode")
public enum SiacDAccFondiDubbiaEsigTipoEnum {
		
	/** The previsione. */
	PREVISIONE("PREVISIONE",
			TipoAccantonamentoFondiDubbiaEsigibilita.PREVISIONE,
			AccantonamentoFondiDubbiaEsigibilita.class,
			AccantonamentoFondiDubbiaEsigibilitaModelDetail.class,
			BilMapId.SiacTAccFondiDubbiaEsig_AccantonamentoFondiDubbiaEsigibilita,
			BilMapId.SiacTAccFondiDubbiaEsig_AccantonamentoFondiDubbiaEsigibilita_ModelDetail) {
		@Override
		public AccantonamentoFondiDubbiaEsigibilita instantiateAccantonamento() {
			return new AccantonamentoFondiDubbiaEsigibilita();
		}
	},
	
	/** The gestione. */
	GESTIONE("GESTIONE",
			TipoAccantonamentoFondiDubbiaEsigibilita.GESTIONE,
			AccantonamentoFondiDubbiaEsigibilitaGestione.class,
			AccantonamentoFondiDubbiaEsigibilitaGestioneModelDetail.class,
			BilMapId.SiacTAccFondiDubbiaEsig_AccantonamentoFondiDubbiaEsigibilitaGestione,
			BilMapId.SiacTAccFondiDubbiaEsig_AccantonamentoFondiDubbiaEsigibilitaGestione_ModelDetail) {
		@Override
		public AccantonamentoFondiDubbiaEsigibilitaGestione instantiateAccantonamento() {
			return new AccantonamentoFondiDubbiaEsigibilitaGestione();
		}
	},
	
	/** The rendiconto. */
	RENDICONTO("RENDICONTO",
			TipoAccantonamentoFondiDubbiaEsigibilita.RENDICONTO,
			AccantonamentoFondiDubbiaEsigibilitaRendiconto.class,
			AccantonamentoFondiDubbiaEsigibilitaRendicontoModelDetail.class,
			BilMapId.SiacTAccFondiDubbiaEsig_AccantonamentoFondiDubbiaEsigibilitaRendiconto,
			BilMapId.SiacTAccFondiDubbiaEsig_AccantonamentoFondiDubbiaEsigibilitaRendiconto_ModelDetail) {
		@Override
		public AccantonamentoFondiDubbiaEsigibilitaRendiconto instantiateAccantonamento() {
			return new AccantonamentoFondiDubbiaEsigibilitaRendiconto();
		}
	},
	;
	
	/** The codice. */
	private final String codice;
	
	/** The tipo accantonamento fondi dubbia esigibilita. */
	private final TipoAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita;
	private final Class<? extends AccantonamentoFondiDubbiaEsigibilitaBase<?>> modelClass;
	private final Class<? extends ModelDetailEnum> modelDetailClass;
	private final MapId mapIdFull;
	private final MapId mapIdModelDetail;
	
	/**
	 * @param codice
	 * @param tipoAccantonamentoFondiDubbiaEsigibilita
	 * @param modelClass
	 * @param mapIdFull
	 * @param mapIdModelDetail
	 */
	private SiacDAccFondiDubbiaEsigTipoEnum(String codice, TipoAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita, Class<? extends AccantonamentoFondiDubbiaEsigibilitaBase<?>> modelClass, Class<? extends ModelDetailEnum> modelDetailClass, MapId mapIdFull, MapId mapIdModelDetail) {
		this.codice = codice;
		this.tipoAccantonamentoFondiDubbiaEsigibilita = tipoAccantonamentoFondiDubbiaEsigibilita;
		this.modelClass = modelClass;
		this.modelDetailClass = modelDetailClass;
		this.mapIdFull = mapIdFull;
		this.mapIdModelDetail = mapIdModelDetail;
	}

	/**
	 * Gets the codice.
	 *
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}
	
	/**
	 * Gets the tipo accantonamento fondi dubbia esigibilita.
	 *
	 * @return the tipoAccantonamentoFondiDubbiaEsigibilita
	 */
	public TipoAccantonamentoFondiDubbiaEsigibilita getTipoAccantonamentoFondiDubbiaEsigibilita() {
		return this.tipoAccantonamentoFondiDubbiaEsigibilita;
	}

	/**
	 * @return the modelClass
	 */
	public Class<? extends AccantonamentoFondiDubbiaEsigibilitaBase<?>> getModelClass() {
		return this.modelClass;
	}

	/**
	 * @return the modelDetailClass
	 */
	public Class<? extends ModelDetailEnum> getModelDetailClass() {
		return this.modelDetailClass;
	}

	/**
	 * @return the mapIdFull
	 */
	public MapId getMapIdFull() {
		return this.mapIdFull;
	}

	/**
	 * @return the mapIdModelDetail
	 */
	public MapId getMapIdModelDetail() {
		return this.mapIdModelDetail;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac d doc stato enum
	 */
	public static SiacDAccFondiDubbiaEsigTipoEnum byCodice(String codice){
		for(SiacDAccFondiDubbiaEsigTipoEnum e : SiacDAccFondiDubbiaEsigTipoEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacDAccFondiDubbiaEsigTipoEnum");
	}
	
	

	/**
	 * By stato operativo.
	 *
	 * @param tipoAccantonamentoFondiDubbiaEsigibilita the tipo accantonamento fondi dubbia esigibilita
	 * @return the siac d doc stato enum
	 */
	public static SiacDAccFondiDubbiaEsigTipoEnum byTipoAccantonamentoFondiDubbiaEsigibilita(TipoAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita){
		for(SiacDAccFondiDubbiaEsigTipoEnum e : SiacDAccFondiDubbiaEsigTipoEnum.values()){
			if(e.getTipoAccantonamentoFondiDubbiaEsigibilita().equals(tipoAccantonamentoFondiDubbiaEsigibilita)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il tipo  "+ tipoAccantonamentoFondiDubbiaEsigibilita + " non ha un mapping corrispondente in SiacDAccFondiDubbiaEsigTipoEnum");
	}
	
	/**
	 * By tipo accantonamento fondi dubbia esigibilita even null.
	 *
	 * @param tipoAccantonamentoFondiDubbiaEsigibilita the tipo accantonamento fondi dubbia esigibilita
	 * @return the siac D acc fondi dubbia esig tipo enum
	 */
	public static SiacDAccFondiDubbiaEsigTipoEnum byTipoAccantonamentoFondiDubbiaEsigibilitaEvenNull(TipoAccantonamentoFondiDubbiaEsigibilita tipoAccantonamentoFondiDubbiaEsigibilita){
		if(tipoAccantonamentoFondiDubbiaEsigibilita == null){
			return null;
		}
		return byTipoAccantonamentoFondiDubbiaEsigibilita(tipoAccantonamentoFondiDubbiaEsigibilita);
	}
	
	public abstract AccantonamentoFondiDubbiaEsigibilitaBase<?> instantiateAccantonamento();
	

}
