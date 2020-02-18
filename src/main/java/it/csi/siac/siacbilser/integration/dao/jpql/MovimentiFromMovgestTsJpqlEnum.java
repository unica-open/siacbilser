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

import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDCollegamentoTipoEnum;

/**
 * Enumera gli SQL necessari ad individuare i Movimenti a partire dall'id di {@link SiacTMovgest}.
 * 
 * @author Domenico
 */
public enum MovimentiFromMovgestTsJpqlEnum {
	
	LIQUIDAZIONE(Arrays.asList(SiacDCollegamentoTipoEnum.Liquidazione), 
			new StringBuilder()
				.append(" SELECT ")
				.append(" siac_r_liquidazione_movgest.liq_id ")  
				.append(" FROM ") 
				.append(" siac_t_movgest_ts ")
				.append(" ,siac_r_liquidazione_movgest ")
				.append(" WHERE ")
				.append(" 	   siac_t_movgest_ts.movgest_ts_id = siac_r_liquidazione_movgest.movgest_ts_id ")
				.append(" AND siac_t_movgest_ts.movgest_ts_id = :movgestTsId ")   
				.append(" AND siac_r_liquidazione_movgest.data_cancellazione is null ")
				.append(" AND siac_t_movgest_ts.data_cancellazione is NULL ") 
			.toString(),
			new StringBuilder()
				.append(" SELECT ") 
				.append("	rlm.siacTLiquidazione.liqId ")  
				.append(" FROM ") 
				.append("	SiacRLiquidazioneMovgest  rlm ")
				.append(" WHERE ") 
				.append("	rlm.siacTMovgestT.movgestTsId = :movgestTsId ")  		
			.toString()					
			),
	
	SUBDOC(Arrays.asList(SiacDCollegamentoTipoEnum.SubdocumentoSpesa, SiacDCollegamentoTipoEnum.SubdocumentoEntrata), 
			new StringBuilder()
			.append(" SELECT siac_t_subdoc.subdoc_id ")
			.append(" FROM ")
			.append(" siac_t_subdoc ")
			.append(" ,siac_r_subdoc_movgest_ts ")
			.append(" ,siac_t_movgest_ts ")
			.append(" WHERE ")
			.append("     siac_t_subdoc.subdoc_id = siac_r_subdoc_movgest_ts.subdoc_id ")
			.append(" AND siac_r_subdoc_movgest_ts.movgest_ts_id = siac_t_movgest_ts.movgest_ts_id ")
			.append(" AND siac_t_movgest_ts.movgest_ts_id = :movgestTsId ")
			.append(" AND siac_t_subdoc.data_cancellazione IS NULL ")
			.append(" AND siac_r_subdoc_movgest_ts.data_cancellazione IS NULL ")
			.append(" AND siac_t_movgest_ts.data_cancellazione IS NULL ")
			.toString(), 
		new StringBuilder()
			.append(" SELECT rsdmg.subdocId ")
			.append(" FROM ")
			.append(" SiacRSubdocMovgestT rsdmg ")
			.append(" WHERE ")
			.append("     rsdmg.dataCancellazione IS NULL ")
			.append(" AND rsdmg.siacTMovgestT.movgestTsId = :movgestTsId ")
			.toString()),

	
	ORDINATIVO_INC(Arrays.asList(SiacDCollegamentoTipoEnum.OrdinativoIncasso),
			new StringBuilder()
			.append(" SELECT tot.ord_id ") 
			.append(" FROM  ")
			.append("   Siac_T_Ordinativo_Ts tot, ")
			.append("   Siac_R_Ordinativo_Ts_Movgest_Ts rotmt, ")
			.append("   siac_t_movgest_ts mts,  ")
			.append("   siac_D_Movgest_Ts_Tipo dmgtst ")
			.append(" WHERE  ")
			.append("       tot.ord_ts_id  = rotmt.ord_ts_id  ")                         
			.append("   AND rotmt.movgest_ts_id =  mts.movgest_ts_id ")
			.append("   AND mts.movgest_ts_tipo_id =  dmgtst.movgest_ts_tipo_id ")
			.append("   AND mts.movgest_ts_id = :movgestTsId ")
			.append("   AND dmgtst.movgest_ts_tipo_code = 'S'  ") 
			.append("   AND tot.data_cancellazione is null ")
			.append("   AND rotmt.data_cancellazione is null ")
			.append("   AND mts.data_cancellazione is null  ")
			.append("   AND dmgtst.data_cancellazione is null ")
			.toString(), 
			new StringBuilder()
				.append(" SELECT tot.siacTOrdinativo.ordId ")
				.append(" FROM SiacTOrdinativoT tot, SiacROrdinativoTsMovgestT rotmt ")
				.append(" WHERE rotmt.siacTOrdinativoT = tot                         ")
				.append(" AND rotmt.siacTMovgestT.movgestTsId = :movgestTsId")
				.append(" AND rotmt.siacTMovgestT.siacDMovgestTsTipo.movgestTsTipoCode = 'S'  ")
				.append(" AND tot.dataCancellazione IS NULL  ")
				.append(" AND rotmt.dataCancellazione IS NULL ")
				.toString()),
	
	ORDINATIVO_PAG(Arrays.asList(SiacDCollegamentoTipoEnum.OrdinativoPagamento),
			new StringBuilder()
			.append(" SELECT siac_t_ordinativo_ts.ord_id ") 
			.append(" FROM ") 
			.append("   siac_t_ordinativo_ts ") 
			.append("  ,siac_R_Liquidazione_Ord   ") 
			.append("  ,siac_t_Liquidazione ") 
			.append("  ,Siac_R_Liquidazione_Movgest") 
			.append("  ,Siac_T_Movgest_Ts ") 
			.append("  ,siac_D_Movgest_Ts_Tipo") 
			.append(" WHERE ") 
			.append("    siac_t_ordinativo_ts.ord_ts_id =  siac_R_Liquidazione_Ord.sord_id ") 
			.append(" AND siac_R_Liquidazione_Ord.liq_id = siac_t_Liquidazione.liq_id   ") 
			.append(" AND siac_t_Liquidazione.liq_id = Siac_R_Liquidazione_Movgest.liq_id ") 
			.append(" AND Siac_R_Liquidazione_Movgest.movgest_ts_id = Siac_T_Movgest_Ts.movgest_ts_id ") 
			.append(" AND Siac_T_Movgest_Ts.movgest_ts_tipo_id = siac_D_Movgest_Ts_Tipo.movgest_ts_tipo_id ") 
			.append(" AND siac_D_Movgest_Ts_Tipo.movgest_ts_tipo_code = 'S' ") 
			.append(" AND Siac_T_Movgest_Ts.movgest_Ts_Id = :movgestTsId ") 
			.append(" AND siac_t_ordinativo_ts.data_cancellazione is null ") 
			.append(" AND siac_R_Liquidazione_Ord.data_cancellazione is null  ")  
			.append(" AND siac_t_Liquidazione.data_cancellazione is null ") 
			.append(" AND Siac_R_Liquidazione_Movgest.data_cancellazione is null ") 
			.append(" AND Siac_T_Movgest_Ts.data_cancellazione is null ") 
			.append(" AND siac_D_Movgest_Ts_Tipo.data_cancellazione is null") 

			.toString(), 
			new StringBuilder()
				.append(" SELECT tot.siacTOrdinativo.ordId ")
				.append(" FROM SiacTOrdinativoT tot, SiacRLiquidazioneOrd rlo  ")
				.append(" WHERE rlo.siacTOrdinativoT = tot  ")
				.append(" AND EXISTS( ")
				.append(" 	FROM rlo.siacTLiquidazione tl, SiacRLiquidazioneMovgest rlm, SiacTMovgestT tmt")
				.append(" 	WHERE rlm.siacTLiquidazione = tl")
				.append(" 	AND rlm.siacTMovgestT = tmt")
				.append("   AND tmt.movgestTsId = :movgestTsId")
				.append("   AND tmt.siacDMovgestTsTipo.movgestTsTipoCode = 'S'")
				.append(" 	AND tmt.dataCancellazione IS NULL")
				.append(" 	AND rlm.dataCancellazione IS NULL")
				.append(" 	)                                 ")
				.append(" AND tot.dataCancellazione IS NULL ")
				.append(" AND rlo.dataCancellazione IS NULL ")
			.toString()),
	;
		
	private final List<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums;
	private final String nativeSql;
	private final String jpql;
	
	public static MovimentiFromMovgestTsJpqlEnum bySiacDCollegamentoTipoEnum(SiacDCollegamentoTipoEnum sdcte){
		for(MovimentiFromMovgestTsJpqlEnum e : MovimentiFromMovgestTsJpqlEnum.values()){
			if(e.getSiacDCollegamentoTipoEnums().contains(sdcte)){
				return e;
			}
			
		}
		throw new IllegalArgumentException("Il tipo di collegamento "+ sdcte.name() +" non supporta la ricerca per Movimento Gestione. "
				+ "Dettagli: Collegamento tipo: "+ sdcte.name() + " non ha un mapping corrispondente in "+ MovimentiFromMovgestTsJpqlEnum.class.getSimpleName());
	}
	
	/**
	 * Ottiene il Set di MovimentiFromBilElemJpqlEnum a partire da un insieme di SiacDCollegamentoTipoEnum.
	 * 
	 * @param siacDCollegamentoTipoEnums
	 * @return il set di MovimentiFromBilElemJpqlEnum
	 */
	public static Set<MovimentiFromMovgestTsJpqlEnum> toMovimentiFromBilElemJpqlEnum(Collection<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums){
		Set<MovimentiFromMovgestTsJpqlEnum> result = EnumSet.noneOf(MovimentiFromMovgestTsJpqlEnum.class);
		
		if(siacDCollegamentoTipoEnums!=null){
			for(SiacDCollegamentoTipoEnum e : siacDCollegamentoTipoEnums){
				result.add(bySiacDCollegamentoTipoEnum(e));
			}
		}
		return result;
	}
		
	private MovimentiFromMovgestTsJpqlEnum(List<SiacDCollegamentoTipoEnum> siacDCollegamentoTipoEnums, String nativeSql, String jpql){
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