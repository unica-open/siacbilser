/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.jpql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;

/**
 * Enumera gli SQL necessari ad individuare i Movimenti a partire dall'id di {@link SiacTBilElem}.
 * 
 * @author Domenico
 */
public enum MovimentiFromSoggettoJpqlEnum {
	
	MOVGEST(Arrays.asList(SiacDCollegamentoTipoEnum.Impegno, SiacDCollegamentoTipoEnum.Accertamento),
			new StringBuilder()
				.append(" SELECT tm.movgest_id ")
				.append(" FROM siac_t_movgest tm, siac_t_movgest_ts tmt, siac_r_movgest_ts_sog rmts, siac_d_movgest_ts_tipo dmtt")
				.append(" WHERE rmts.movgest_ts_id = tmt.movgest_ts_id ")
				.append(" AND tmt.movgest_id = tm.movgest_id ")
				.append(" AND tmt.movgest_ts_tipo_id = dmtt.movgest_ts_tipo_id ")
				.append(" AND dmtt.movgest_ts_tipo_code = 'T' ")
				.append(" AND rmts.soggetto_id = :soggettoId ")
				.append(" AND tm.data_cancellazione IS NULL ")
				.append(" AND tmt.data_cancellazione IS NULL ")
				.append(" AND rmts.data_cancellazione IS NULL ")
				.append(" AND dmtt.data_cancellazione IS NULL ")
				.toString(),
			new StringBuilder()
				.append(" SELECT tm.movgestId ")
				.append(" FROM SiacTMovgest tm, SiacTMovgestT tmt, SiacRMovgestTsSog rmts ")
				.append(" WHERE rmts.siacTMovgestT = tmt ")
				.append(" AND tmt.siacTMovgest = tm ")
				.append(" AND rmts.siacTSoggetto.soggettoId = :soggettoId ")
				.append(" AND tmt.siacDMovgestTsTipo.movgestTsTipoCode = 'T' ")
				.append(" AND tm.dataCancellazione IS NULL ")
				.append(" AND tmt.dataCancellazione IS NULL ")
				.append(" AND rmts.dataCancellazione IS NULL ")
				.toString()),
	
	SUBMOVGEST(Arrays.asList(SiacDCollegamentoTipoEnum.SubImpegno, SiacDCollegamentoTipoEnum.SubAccertamento),
			new StringBuilder()
				.append(" SELECT tmt.movgest_ts_id ")
				.append(" FROM siac_t_movgest_ts tmt,  siac_r_movgest_ts_sog rmts , siac_d_movgest_ts_tipo dmtt ")
				.append(" WHERE tmt.movgest_ts_tipo_id = dmtt.movgest_ts_tipo_id ")
				.append(" AND rmts.movgest_ts_id = tmt.movgest_ts_id ")
				.append(" AND rmts.soggetto_id = :soggettoId ")
				.append(" AND dmtt.movgest_ts_tipo_code = 'S' ")
				.append(" AND tmt.data_cancellazione IS NULL ")
				.append(" AND rmts.data_cancellazione IS NULL ")
				.toString(),
			new StringBuilder()
				.append(" SELECT tmt.movgestTsId ")
				.append(" FROM SiacTMovgestT tmt, SiacRMovgestTsSog rmts ")
				.append(" WHERE rmts.siacTMovgestT = tmt ")
				.append(" AND rmts.siacTSoggetto.soggettoId = :soggettoId ")
				.append(" AND tmt.siacDMovgestTsTipo.movgestTsTipoCode = 'S' ")
				.append(" AND tmt.dataCancellazione IS NULL ")
				.append(" AND rmts.dataCancellazione IS NULL ")
				.toString()),
	
	SUBDOC(Arrays.asList(SiacDCollegamentoTipoEnum.SubdocumentoSpesa, SiacDCollegamentoTipoEnum.SubdocumentoEntrata),
			new StringBuilder()
				.append(" SELECT ts.subdoc_id ")
				.append(" FROM siac_t_subdoc ts, siac_t_doc td, siac_r_doc_sog rds ")
				.append(" WHERE ts.doc_id = td.doc_id ")
				.append(" AND rds.doc_id = td.doc_id ")
				.append(" AND rds.soggetto_id = :soggettoId ")
				.append(" AND ts.data_cancellazione IS NULL ")
				.append(" AND td.data_cancellazione IS NULL ")
				.append(" AND rds.data_cancellazione IS NULL ")
				.toString(), 
			new StringBuilder()
				.append(" SELECT ts.subdocId ")
				.append(" FROM SiacTSubdoc ts, SiacTDoc td, SiacRDocSog rds ")
				.append(" WHERE ts.siacTDoc = td ")
				.append(" AND rds.siacTDoc = td ")
				.append(" AND rds.siacTSoggetto.soggettoId = :soggettoId ")
				.append(" AND ts.dataCancellazione IS NULL ")
				.append(" AND td.dataCancellazione IS NULL ")
				.append(" AND rds.dataCancellazione IS NULL ")
				.toString()),
	
	LIQUIDAZIONE(Arrays.asList(SiacDCollegamentoTipoEnum.Liquidazione),
			new StringBuilder()
				.append(" SELECT tl.liq_id ")
				.append(" FROM siac_t_liquidazione tl, siac_r_liquidazione_soggetto rls ")
				.append(" WHERE rls.liq_id = tl.liq_id ")
				.append(" AND rls.soggetto_id = :soggettoId ")
				.append(" AND tl.data_cancellazione IS NULL ")
				.append(" AND rls.data_cancellazione IS NULL ")
				.toString(),
			new StringBuilder()
				.append(" SELECT tl.liqId ")
				.append(" FROM SiacTLiquidazione tl, SiacRLiquidazioneSoggetto rls ")
				.append(" WHERE rls.siacTLiquidazione = tl ")
				.append(" AND rls.siacTSoggetto.soggettoId = :soggettoId ")
				.append(" AND tl.dataCancellazione IS NULL ")
				.append(" AND rls.dataCancellazione IS NULL ")
				.toString()),
	
	ORDINATIVO(Arrays.asList(SiacDCollegamentoTipoEnum.OrdinativoIncasso, SiacDCollegamentoTipoEnum.OrdinativoPagamento),
			new StringBuilder()
				.append(" SELECT tot.ord_id")
				.append(" FROM siac_t_ordinativo tot, siac_r_ordinativo_soggetto ros ")
				.append(" WHERE ros.ord_id = tot.ord_id")
				.append(" AND ros.soggetto_id = :soggettoId")
				.append(" AND ros.data_cancellazione IS NULL ")
				.append(" AND tot.data_cancellazione IS NULL ")
				.toString(), 
			new StringBuilder()
				.append(" SELECT tot.ordId ")
				.append(" FROM SiacTOrdinativo tot, SiacROrdinativoSoggetto ros ")
				.append(" WHERE ros.siacTOrdinativo = tot ")
				.append(" AND ros.siacTSoggetto.soggettoId = :soggettoId ")
				.append(" AND ros.dataCancellazione IS NULL ")
				.append(" AND tot.dataCancellazione IS NULL ")
				.toString()),
	MODIFICA(Arrays.asList(SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneSpesa, SiacDCollegamentoTipoEnum.ModificaMovimentoGestioneEntrata), 
			new StringBuilder()
				.append(" SELECT rms.mod_id as col_0_0_  ")
				.append(" FROM siac_r_modifica_stato rms  ")
				.append(" WHERE rms.data_cancellazione IS NULL ")
				.append(" AND EXISTS  ")
				.append("     ( ")
				.append("         SELECT tmtdm.movgest_ts_det_mod_id, rmts.movgest_ts_sog_id  ")
				.append("         FROM siac_t_movgest_ts_det_mod tmtdm, siac_r_movgest_ts_sog rmts  ")
				.append("         WHERE tmtdm.movgest_ts_id=rmts.movgest_ts_id  ")
				.append("         AND tmtdm.mod_stato_r_id=rms.mod_stato_r_id  ")
				.append("         AND rmts.soggetto_id= :soggettoId  ")
				.append("         AND (tmtdm.data_cancellazione IS NULL)  ")
				.append("         AND (rmts.data_cancellazione IS NULL) ")
				.append("     )  ")
				.append("     OR EXISTS  ")
				.append("     ( ")
				.append("         SELECT rmtsm.movgest_ts_sog_mod_id, rmts.movgest_ts_sog_id  ")
				.append("         FROM siac_r_movgest_ts_sog_mod rmtsm, siac_r_movgest_ts_sog rmts  ")
				.append("         WHERE rmtsm.movgest_ts_id=rmts.movgest_ts_id  ")
				.append("         AND rmtsm.mod_stato_r_id=rms.mod_stato_r_id  ")
				.append("         AND rmts.soggetto_id= :soggettoId  ")
				.append("         AND (rmtsm.data_cancellazione IS NULL)  ")
				.append("         AND (rmts.data_cancellazione IS NULL) ")
				.append("     )  ")
				.append("     OR EXISTS  ")
				.append("     ( ")
				.append("         SELECT rmtscm.movgest_ts_sogclasse_mod_id, rmts.movgest_ts_sog_id  ")
				.append("         FROM siac_r_movgest_ts_sogclasse_mod rmtscm, siac_r_movgest_ts_sog rmts  ")
				.append("         WHERE rmtscm.movgest_ts_id=rmts.movgest_ts_id  ")
				.append("         AND rmtscm.mod_stato_r_id=rms.mod_stato_r_id  ")
				.append("         AND rmts.soggetto_id= :soggettoId  ")
				.append("         AND (rmtscm.data_cancellazione IS NULL)  ")
				.append("         AND (rmts.data_cancellazione IS NULL) ")
				.append("     ) ")
				.toString(), 
			new StringBuilder()
				.append(" SELECT rms.siacTModifica.modId ")
				.append(" FROM SiacRModificaStato rms ")
				.append(" WHERE rms.dataCancellazione IS NULL ")
				.append(" AND ( ")
				.append("     EXISTS ( ")
				.append("         FROM SiacTMovgestTsDetMod tmtdm, SiacRMovgestTsSog rmtsog ") 
				.append("         WHERE tmtdm.siacTMovgestT = rmtsog.siacTMovgestT ")
				.append("         AND tmtdm.siacRModificaStato = rms ")
				.append("         AND rmtsog.siacTSoggetto.soggettoId = :soggettoId ")
				.append("         AND tmtdm.dataCancellazione IS NULL ") 
				.append("         AND rmtsog.dataCancellazione IS NULL ") 
				.append("     ) ")
				.append("     OR EXISTS ( ")
				.append("         FROM SiacRMovgestTsSogMod rmtsm, SiacRMovgestTsSog rmtsog ") 
				.append("         WHERE rmtsm.siacTMovgestT = rmtsog.siacTMovgestT ")
				.append("         AND rmtsm.siacRModificaStato = rms ")
				.append("         AND rmtsog.siacTSoggetto.soggettoId = :soggettoId ")
				.append("         AND rmtsm.dataCancellazione IS NULL ") 
				.append("         AND rmtsog.dataCancellazione IS NULL ") 
				.append("     ) ")
				.append("     OR EXISTS ( ")
				.append("         FROM SiacRMovgestTsSogclasseMod rmtscm, SiacRMovgestTsSog rmtsog ") 
				.append("         WHERE rmtscm.siacTMovgestT = rmtsog.siacTMovgestT ")
				.append("         AND rmtscm.siacRModificaStato = rms ")
				.append("         AND rmtsog.siacTSoggetto.soggettoId = :soggettoId ")
				.append("         AND rmtscm.dataCancellazione IS NULL ") 
				.append("         AND rmtsog.dataCancellazione IS NULL ") 
				.append("     ) ")                                                   
				.append(" ) ")
				.toString()),
	
	;
	
	private final List<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums;
	private final String nativeSql;
	private final String jpql;
	
	public static MovimentiFromSoggettoJpqlEnum bySiacDCollegamentoTipoEnum(SiacDCollegamentoTipoEnum sdcte){
		for(MovimentiFromSoggettoJpqlEnum e : MovimentiFromSoggettoJpqlEnum.values()){
			if(e.getSiacDCollegamentoTipoEnums().contains(sdcte)){
				return e;
			}
			
		}
		throw new IllegalArgumentException("Il tipo di collegamento "+ sdcte.name() +" non supporta la ricerca per Capitolo. "
				+ "Dettagli: Collegamento tipo: "+ sdcte.name() + " non ha un mapping corrispondente in "+ MovimentiFromSoggettoJpqlEnum.class.getSimpleName());
	}
	
	/**
	 * Ottiene il Set di MovimentiFromBilElemJpqlEnum a partire da un insieme di SiacDCollegamentoTipoEnum.
	 * 
	 * @param siacDCollegamentoTipoEnums
	 * @return il set di MovimentiFromBilElemJpqlEnum
	 */
	public static Set<MovimentiFromSoggettoJpqlEnum> toMovimentiFromBilElemJpqlEnum(Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums){
		Set<MovimentiFromSoggettoJpqlEnum> result = EnumSet.noneOf(MovimentiFromSoggettoJpqlEnum.class);
		
		if(siacDCollegamentoTipoEnums!=null){
			for(SiacDCollegamentoTipoEnum e : siacDCollegamentoTipoEnums){
				result.add(bySiacDCollegamentoTipoEnum(e));
			}
		}
		return result;
	}
	
	
	private MovimentiFromSoggettoJpqlEnum(List<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums, String nativeSql, String jpql){
		this.siacDCollegamentoTipoEnums = siacDCollegamentoTipoEnums;
		this.nativeSql = nativeSql;
		this.jpql = jpql;
	}


	
	public String getNativeSql() {
		return nativeSql;
	}
	
	public String getJpql() {
		return jpql;
	}

	public List<SiacDCollegamentoTipoEnum> getSiacDCollegamentoTipoEnums() {
		return new ArrayList<SiacDCollegamentoTipoEnum>(siacDCollegamentoTipoEnums);
	}
	
	public Set<String> getSiacDCollegamentoTipoCodes() {
		Set<String> result = new HashSet<String>();

		for(SiacDCollegamentoTipoEnum e : siacDCollegamentoTipoEnums){
			result.add(e.getCodice());
		}
		return result;
	}
	
	
}