/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTGiustificativo;
import it.csi.siac.siacbilser.integration.entity.SiacTLiquidazione;
import it.csi.siac.siacbilser.integration.entity.SiacTModifica;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTOrdinativo;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNotaRateiRisconti;
import it.csi.siac.siacbilser.integration.entity.SiacTRichiestaEcon;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.CecMapId;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siaccommonser.integration.entity.SiacTBase;
import it.csi.siac.siaccommonser.util.dozer.MapId;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siacfin2ser.model.AccertamentoModelDetail;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ImpegnoModelDetail;
import it.csi.siac.siacfin2ser.model.LiquidazioneModelDetail;
import it.csi.siac.siacfin2ser.model.SubAccertamentoModelDetail;
import it.csi.siac.siacfin2ser.model.SubImpegnoModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.Rateo;
import it.csi.siac.siacgenser.model.Risconto;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * Descrive il mapping dell codifiche.
 * 
 * @author Valentina
 */
@EnumEntity(entityName="SiacDCollegamentoTipo", idPropertyName="collegamentoTipoId", codePropertyName="collegamentoTipoCode")
public enum SiacDCollegamentoTipoEnum {
	
	
	
	Impegno("I", TipoCollegamento.IMPEGNO, Impegno.class, SiacTMovgest.class,"movgestId", BilMapId.SiacTMovgest_Impegno_Soggetto, BilMapId.SiacTMovgest_Impegno_ModelDetail, ImpegnoModelDetail.class, MovimentoJpqlEnum.Impegno, Boolean.FALSE),
	Accertamento("A", TipoCollegamento.ACCERTAMENTO, Accertamento.class,SiacTMovgest.class,"movgestId", BilMapId.SiacTMovgest_Accertamento_Soggetto, BilMapId.SiacTMovgest_Accertamento_ModelDetail, AccertamentoModelDetail.class, MovimentoJpqlEnum.Accertamento, Boolean.TRUE),
	Liquidazione("L", TipoCollegamento.LIQUIDAZIONE, Liquidazione.class, SiacTLiquidazione.class,"liqId", BilMapId.SiacTLiquidazione_Liquidazione_Soggetto, BilMapId.SiacTLiquidazione_Liquidazione_ModelDetail, LiquidazioneModelDetail.class, MovimentoJpqlEnum.Liquidazione, Boolean.FALSE),
	@Deprecated DocumentoEntrata("DE", TipoCollegamento.DOCUMENTO_ENTRATA, DocumentoEntrata.class, SiacTDoc.class,"docId", BilMapId.SiacTDoc_DocumentoEntrata_Minimal, BilMapId.SiacTDoc_DocumentoEntrata_Minimal, null, MovimentoJpqlEnum.DocumentoEntrata, null),
	SubdocumentoEntrata("SE", TipoCollegamento.SUBDOCUMENTO_ENTRATA, SubdocumentoEntrata.class, SiacTSubdoc.class,"subdocId", BilMapId.SiacTSubdoc_SubdocumentoEntrata_Base, BilMapId.SiacTSubdoc_SubdocumentoEntrata_ModelDetail, SubdocumentoEntrataModelDetail.class,  MovimentoJpqlEnum.SubdocumentoEntrata, Boolean.TRUE),
	@Deprecated DocumentoSpesa("DS", TipoCollegamento.DOCUMENTO_SPESA, DocumentoSpesa.class, SiacTDoc.class,"docId", BilMapId.SiacTDoc_DocumentoSpesa_Minimal, BilMapId.SiacTDoc_DocumentoSpesa_Minimal, null, MovimentoJpqlEnum.DocumentoSpesa, null),
	SubdocumentoSpesa("SS", TipoCollegamento.SUBDOCUMENTO_SPESA, SubdocumentoSpesa.class, SiacTSubdoc.class,"subdocId", BilMapId.SiacTSubdoc_SubdocumentoSpesa_Base, BilMapId.SiacTSubdoc_SubdocumentoSpesa_ModelDetail, SubdocumentoSpesaModelDetail.class, MovimentoJpqlEnum.SubdocumentoSpesa, Boolean.FALSE),
	OrdinativoPagamento("OP", TipoCollegamento.ORDINATIVO_PAGAMENTO, OrdinativoPagamento.class, SiacTOrdinativo.class,"ordId", BilMapId.SiacTOrdinativo_Ordinativo, BilMapId.SiacTOrdinativo_Ordinativo, null, MovimentoJpqlEnum.OrdinativoPagamento,Boolean.FALSE),
	OrdinativoIncasso("OI", TipoCollegamento.ORDINATIVO_INCASSO, OrdinativoIncasso.class, SiacTOrdinativo.class,"ordId", BilMapId.SiacTOrdinativo_Ordinativo, BilMapId.SiacTOrdinativo_Ordinativo, null, MovimentoJpqlEnum.OrdinativoIncasso,Boolean.TRUE),
	PrimaNota("P", TipoCollegamento.PRIMA_NOTA, PrimaNota.class, SiacTPrimaNota.class, null, null, null, null, MovimentoJpqlEnum.PrimaNotaPura,null),
	Epilogativo("EP", TipoCollegamento.EPILOGATIVO, null, null, null, null, null, null, null,null),
	ContoOrdine("CO", TipoCollegamento.CONTO_ORDINE, null, null, null, null, null, null, null,null),

	// Rm Aggiungo i tipo evento di FIN che mancavano
	// fixme: RICORDARSI --> bisogna cambiare le query
	SubImpegno("SI", TipoCollegamento.SUBIMPEGNO, it.csi.siac.siacfinser.model.SubImpegno.class, SiacTMovgestT.class,"movgestTsId", BilMapId.SiacTMovgestT_SubImpegno_Minimal, BilMapId.SiacTMovgestT_SubImpegno_ModelDetail, SubImpegnoModelDetail.class, MovimentoJpqlEnum.SubImpegno, Boolean.FALSE),
	SubAccertamento("SA", TipoCollegamento.SUBACCERTAMENTO, it.csi.siac.siacfinser.model.SubAccertamento.class, SiacTMovgestT.class,"movgestTsId", BilMapId.SiacTMovgestT_SubAccertamento_Minimal, BilMapId.SiacTMovgestT_SubAccertamento_ModelDetail, SubAccertamentoModelDetail.class, MovimentoJpqlEnum.SubAccertamento, Boolean.TRUE),
	ModificaMovimentoGestioneSpesa("MMGS", TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_SPESA, it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa.class, SiacTModifica.class,"modId", BilMapId.SiacTModifica_ModificaMovimentoGestioneSpesa_BIL, BilMapId.SiacTModifica_ModificaMovimentoGestioneSpesa_BIL, null, MovimentoJpqlEnum.ModificaMovimentoGestioneSpesa,null),
	ModificaMovimentoGestioneEntrata("MMGE", TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA, it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata.class, SiacTModifica.class,"modId", BilMapId.SiacTModifica_ModificaMovimentoGestioneEntrata_BIL, BilMapId.SiacTModifica_ModificaMovimentoGestioneEntrata_BIL, null,MovimentoJpqlEnum.ModificaMovimentoGestioneEntrata,null),
	
	
	RichiestaEconomale("RE", TipoCollegamento.RICHIESTA_ECONOMALE, RichiestaEconomale.class, SiacTRichiestaEcon.class, "riceconId", CecMapId.SiacTRichiestaEcon_RichiestaEconomale, CecMapId.SiacTRichiestaEcon_RichiestaEconomale, null, MovimentoJpqlEnum.RichiestaEconomale,null),
	RendicontoRichiesta("RR", TipoCollegamento.RENDICONTO_RICHIESTA, RendicontoRichiesta.class, SiacTGiustificativo.class, "gstId", CecMapId.SiacTGiustificativo_RendicontoRichiesta, CecMapId.SiacTGiustificativo_RendicontoRichiesta, null, MovimentoJpqlEnum.RendicontoRichiesta, null),
	
	Rateo("RT", TipoCollegamento.RATEO, Rateo.class, SiacTPrimaNotaRateiRisconti.class, "pnotarrId", GenMapId.SiacTPrimaNotaRateiRisconti_Rateo, GenMapId.SiacTPrimaNotaRateiRisconti_Rateo, null, MovimentoJpqlEnum.Rateo,null),
	Risconto("RS", TipoCollegamento.RISCONTO, Risconto.class, SiacTPrimaNotaRateiRisconti.class, "pnotarrId", GenMapId.SiacTPrimaNotaRateiRisconti_Risconto, GenMapId.SiacTPrimaNotaRateiRisconti_Risconto, null, MovimentoJpqlEnum.Risconto,null),
	
	;
	
	private final TipoCollegamento tipoCollegamento;
	private final String collegamentoTipoCode;
	private final Class<? extends Entita> modelClass;
	private final Class<? extends SiacTBase> entityClass;
	private final String idColumnName;
	private final MapId mapId;
	private final MapId mapIdModelDetail;
	private final Class<? extends ModelDetailEnum> modelDetailClass;
	//private String jpql;
	private final MovimentoJpqlEnum movimentoJpqlEnum;
	private final Boolean associabileRateoAttivoRiscontoPassivo;
	
	/**
	 * 
	 * @param modelClass Classe di Model
	 * @param entityClass Classe di Entitty (es: SiacD*.class)
	 * @param idColumnName nome jpql della colonna contenente l'id
	 * @param mapId mapId per il mapping di Dozer
	 */
	private SiacDCollegamentoTipoEnum(String collegamentoTipoCode, TipoCollegamento tipoCollegamento, Class<? extends Entita> modelClass, Class<? extends SiacTBase> entityClass, String idColumnName,
							MapId mapId, MapId mapIdModelDetail, Class<? extends ModelDetailEnum> modelDetailClass, MovimentoJpqlEnum movimentoJpqlEnum, Boolean associabileRateoAttivoRiscontoPassivo) {
		this.tipoCollegamento = tipoCollegamento;
		this.collegamentoTipoCode = collegamentoTipoCode;
		this.modelClass = modelClass;
		this.entityClass = entityClass;
		this.idColumnName = idColumnName;
		this.mapId = mapId;
		this.mapIdModelDetail = mapIdModelDetail;
		this.modelDetailClass = modelDetailClass;
		this.movimentoJpqlEnum = movimentoJpqlEnum;
		this.associabileRateoAttivoRiscontoPassivo =associabileRateoAttivoRiscontoPassivo;
	}
	
	
	public static SiacDCollegamentoTipoEnum byCollegamentoTipoCode(String collegamentoTipoCode){
		for(SiacDCollegamentoTipoEnum ce : SiacDCollegamentoTipoEnum.values()){
			if(ce.getCollegamentoTipoCode()!=null && ce.getCollegamentoTipoCode().equals(collegamentoTipoCode)){
				return ce;
			}
		}
		throw new IllegalArgumentException("Impossibile trovare un mapping per il codice: "+ collegamentoTipoCode + " in SiacDCollegamentoTipoEnum");
	}
	
	
	public static SiacDCollegamentoTipoEnum byModelClass(Class<? extends Entita> modelClass){
		for(SiacDCollegamentoTipoEnum ce : SiacDCollegamentoTipoEnum.values()){
			if(ce.getModelClass()!=null && ce.getModelClass().equals(modelClass)){
				return ce;
			}
		}
		throw new IllegalArgumentException("Impossibile trovare un mapping per la classe: "+ modelClass + " in SiacDCollegamentoTipoEnum");
	}
	
	public static SiacDCollegamentoTipoEnum byTipoCollegamento(TipoCollegamento tipoCollegamento){
		for(SiacDCollegamentoTipoEnum ce : SiacDCollegamentoTipoEnum.values()){
			if(ce.getTipoCollegamento()!=null && ce.getTipoCollegamento().equals(tipoCollegamento)){
				return ce;
			}
		}
		throw new IllegalArgumentException("Impossibile trovare un mapping per il tipo collegamento: "+ tipoCollegamento + " in SiacDCollegamentoTipoEnum");
	}
	
	public static SiacDCollegamentoTipoEnum byTipoCollegamentoEvenNull(TipoCollegamento tipoCollegamento) {
		if(tipoCollegamento==null){
			return null;
		}
		return byTipoCollegamento(tipoCollegamento); 
	}
	
	/**
	 * Gets the codifica instance.
	 *
	 * @param <T> the generic type
	 * @return the codifica instance
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entita> T getModelInstance() {
		try {
			return (T) modelClass.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("Eccezione istanziamento SiacDCollegamentoTipoEnum."+name()+"->"+modelClass + " ["+collegamentoTipoCode+"]",e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Impossibile accedere al costruttore vuoto di  "+modelClass + " ["+collegamentoTipoCode+"]",e);
		}
	}


	/**
	 * @return the entityClass
	 */
	public Class<? extends SiacTBase> getEntityClass() {
		return entityClass;
	}

	/**
	 * @return the idColumnName
	 */
	public String getIdColumnName() {
		return idColumnName;
	}

	
	/**
	 * @return the mapId
	 */
	public MapId getMapId() {
		return mapId;
	}

	/**
	 * @return the mapIdModelDetail
	 */
	public MapId getMapIdModelDetail() {
		return mapIdModelDetail;
	}


	/**
	 * @return the tipoCollegamento
	 */
	public TipoCollegamento getTipoCollegamento() {
		return tipoCollegamento;
	}
	
	/**
	 * @return the collegamentoTipoCode
	 */
	public String getCollegamentoTipoCode() {
		return collegamentoTipoCode;
	}
	
	/**
	 * @return the collegamentoTipoCode
	 */
	public String getCodice() {
		return getCollegamentoTipoCode();
	}

	/**
	 * @return the modelClass
	 */
	public Class<? extends Entita> getModelClass() {
		return modelClass;
	}

	/**
	 * @return the movimentoJpqlEnum
	 */
	public MovimentoJpqlEnum getMovimentoJpqlEnum() {
		return movimentoJpqlEnum;
	}


	/**
	 * @return the modelDetailClass
	 */
	public Class<? extends ModelDetailEnum> getModelDetailClass() {
		return modelDetailClass;
	}


		/**
	 * @return the associabileRateoAttivoRiscontoPassivo
	 */
	public Boolean isAssociabileRateoAttivoRiscontoPassivo() {
		if(this.associabileRateoAttivoRiscontoPassivo == null) {
			//mantengo qui l'informazione che null -> non posso avere ratei o risconti (per coerenza con il pregresso)
			throw new IllegalArgumentException("Tipo collegamento " + this.collegamentoTipoCode + " non supportato.");
		}
		return associabileRateoAttivoRiscontoPassivo;
	}

		
//	/**
//	 * @return the jpql
//	 */
//	public String getJpql() {
//		return movimentoJpqlEnum.getJpql();
//	}
	


//	/**
//	 * @return the siacDEventoFamTipoEnum
//	 */
//	public SiacDEventoFamTipoEnum getSiacDEventoFamTipoEnum() {
//		return siacDEventoFamTipoEnum;
//	}
//
//
//	/**
//	 * @param siacDEventoFamTipoEnum the siacDEventoFamTipoEnum to set
//	 */
//	public void setSiacDEventoFamTipoEnum(
//			SiacDEventoFamTipoEnum siacDEventoFamTipoEnum) {
//		this.siacDEventoFamTipoEnum = siacDEventoFamTipoEnum;
//	}
//	
//	

	

}
