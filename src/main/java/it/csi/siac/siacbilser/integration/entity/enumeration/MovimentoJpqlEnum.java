/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Reperisce le entities a partire da enteProprietarioId, annoMovimento, numeroMovimento e numeroSubmovimento.
 * 
 * @author Valentina
 * @author Domenico
 */
public enum MovimentoJpqlEnum {
	
	
	Impegno(" FROM SiacTMovgest s WHERE s.dataCancellazione IS NULL AND s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " AND s.movgestAnno = :annoMovimento AND s.movgestNumero = :numeroMovimento AND s.siacDMovgestTipo.movgestTipoCode = 'I' ",
			NumeroMovimentoType.BIG_DECIMAL, null),
	
	Accertamento(" FROM SiacTMovgest s WHERE s.dataCancellazione IS NULL AND s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " AND s.movgestAnno = :annoMovimento AND s.movgestNumero = :numeroMovimento AND s.siacDMovgestTipo.movgestTipoCode = 'A' ",
			NumeroMovimentoType.BIG_DECIMAL, null),
	
	Liquidazione(" FROM SiacTLiquidazione l WHERE l.dataCancellazione IS NULL AND l.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " AND l.liqAnno = :annoMovimento AND l.liqNumero = :numeroMovimento ",
			NumeroMovimentoType.BIG_DECIMAL, null),
	
	DocumentoEntrata(" FROM SiacTDoc d WHERE d.dataCancellazione IS NULL AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " AND d.docAnno = :annoMovimento AND UPPER(d.docNumero) = UPPER(CAST(:numeroMovimento as string)) AND d.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = 'E' ",
			NumeroMovimentoType.STRING, null),
			
	SubdocumentoEntrata(" FROM SiacTSubdoc s WHERE s.dataCancellazione IS NULL AND s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
					+ " AND s.siacTDoc.docAnno = :annoMovimento AND UPPER(s.siacTDoc.docNumero) = UPPER(CAST(:numeroMovimento as string)) "
					+ " AND (:numeroSubmovimento IS NULL OR s.subdocNumero = CAST(CAST(:numeroSubmovimento AS string) AS integer)) "
					+ " AND s.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = 'E' ",
			NumeroMovimentoType.STRING, NumeroSubmovimentoType.INTEGER),
	
	DocumentoSpesa(" FROM SiacTDoc d WHERE d.dataCancellazione IS NULL AND d.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " AND d.docAnno = :annoMovimento AND UPPER(d.docNumero) = UPPER(CAST(:numeroMovimento as string)) AND d.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = 'S' ",
			NumeroMovimentoType.STRING, null),
			
	SubdocumentoSpesa(" FROM SiacTSubdoc s WHERE s.dataCancellazione IS NULL AND s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
					+ " AND s.siacTDoc.docAnno = :annoMovimento AND UPPER(s.siacTDoc.docNumero) = UPPER(cast(:numeroMovimento as string)) "
					+ " AND (:numeroSubmovimento IS NULL OR s.subdocNumero = CAST(CAST(:numeroSubmovimento AS string) AS integer)) "
					+ " AND s.siacTDoc.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = 'S' ",
			NumeroMovimentoType.STRING, NumeroSubmovimentoType.INTEGER),
	
	OrdinativoPagamento(" FROM SiacTOrdinativo o WHERE o.dataCancellazione IS NULL AND o.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " AND o.ordAnno = :annoMovimento AND o.ordNumero = :numeroMovimento AND o.siacDOrdinativoTipo.ordTipoCode = 'P' ",
			NumeroMovimentoType.BIG_DECIMAL, null),
	
	OrdinativoIncasso(" FROM SiacTOrdinativo o WHERE o.dataCancellazione IS NULL AND o.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " AND o.ordAnno = :annoMovimento AND o.ordNumero = :numeroMovimento AND o.siacDOrdinativoTipo.ordTipoCode = 'I' ",
			NumeroMovimentoType.BIG_DECIMAL, null),
	
	PrimaNotaPura(" FROM SiacTPrimaNota p WHERE p.dataCancellazione IS NULL AND p.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " AND p.siacTBil.siacTPeriodo.anno = CAST(:annoMovimento AS string) AND p.pnotaNumero = :numeroMovimento ",
			NumeroMovimentoType.INTEGER, null),
			
	// RM aggiunte query per gestione tipo eventi: subImpegno, subAccertamento, modificaMovimentoGestioneSpesa/Entrata		
	SubImpegno(" FROM SiacTMovgestT s WHERE s.dataCancellazione IS NULL AND s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
					+ " AND s.siacTMovgest.movgestAnno = :annoMovimento AND s.siacTMovgest.movgestNumero = CAST(CAST(:numeroMovimento AS string) AS integer) "
					+ " AND (:numeroSubmovimento IS NULL OR s.movgestTsCode = CAST(:numeroSubmovimento as string) ) "
					+ " AND s.siacDMovgestTsTipo.movgestTsTipoCode = 'S' AND  s.siacTMovgest.siacDMovgestTipo.movgestTipoCode = 'I' ",
			NumeroMovimentoType.BIG_DECIMAL, NumeroSubmovimentoType.STRING),
					
	SubAccertamento(" FROM SiacTMovgestT s WHERE s.dataCancellazione IS NULL AND s.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
					+ " AND s.siacTMovgest.movgestAnno = :annoMovimento AND s.siacTMovgest.movgestNumero = CAST(CAST(:numeroMovimento AS string) AS integer) "
					+ " AND (:numeroSubmovimento IS NULL OR s.movgestTsCode = CAST(:numeroSubmovimento as string) ) "
					+ " AND s.siacDMovgestTsTipo.movgestTsTipoCode = 'S' AND  s.siacTMovgest.siacDMovgestTipo.movgestTipoCode = 'A' ",
			NumeroMovimentoType.BIG_DECIMAL, NumeroSubmovimentoType.STRING),
			
	
	ModificaMovimentoGestioneSpesa(" FROM SiacTModifica m "
			+ " WHERE m.dataCancellazione IS NULL "
			+ " AND m.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND EXISTS ( "
			+ "     FROM SiacRModificaStato rms "
			+ "     WHERE rms.siacTModifica = m "
			+ "     AND rms.dataCancellazione IS NULL "
			+ "     AND ("
			+ "         EXISTS ( "
			+ "             FROM SiacRMovgestTsSogMod rmtsm, SiacTMovgestT tmt "
			+ "             WHERE rmtsm.siacRModificaStato = rms "
			+ "             AND tmt = rmtsm.siacTMovgestT "
			+ "             AND rmtsm.dataCancellazione IS NULL "
			+ "             AND tmt.siacTMovgest.movgestAnno = :annoMovimento "
			+ "             AND tmt.siacTMovgest.movgestNumero = CAST(CAST(:numeroMovimento AS string) AS integer) "
			+ "             AND (:numeroSubmovimento IS NULL OR tmt.movgestTsCode = CAST(:numeroSubmovimento as string))"
//			+ "             AND (:numeroSubmovimento IS NULL OR :numeroSubmovimento = '' OR tmt.movgestTsCode = :numeroSubmovimento) "
			+ "             AND tmt.siacTMovgest.siacDMovgestTipo.movgestTipoCode = 'I' "
			+ "         ) OR EXISTS( "
			+ "             FROM SiacRMovgestTsSogclasseMod rmtsm, SiacTMovgestT tmt "
			+ "             WHERE rmtsm.siacRModificaStato = rms "
			+ "             AND tmt = rmtsm.siacTMovgestT "
			+ "             AND rmtsm.dataCancellazione IS NULL "
			+ "             AND tmt.siacTMovgest.movgestAnno = :annoMovimento "
			+ "             AND tmt.siacTMovgest.movgestNumero = CAST(CAST(:numeroMovimento AS string) AS integer) "
			+ "             AND (:numeroSubmovimento IS NULL OR tmt.movgestTsCode = CAST(:numeroSubmovimento as string))"
//			+ "             AND (:numeroSubmovimento IS NULL OR :numeroSubmovimento = '' OR tmt.movgestTsCode = :numeroSubmovimento) "
			+ "             AND tmt.siacTMovgest.siacDMovgestTipo.movgestTipoCode = 'I' "
			+ "         ) OR EXISTS( "
			+ "             FROM SiacTMovgestTsDetMod tmtdm, SiacTMovgestT tmt "
			+ "             WHERE tmtdm.siacRModificaStato = rms "
			+ "             AND tmt = tmtdm.siacTMovgestT "
			+ "             AND tmtdm.dataCancellazione IS NULL "
			+ "             AND tmt.siacTMovgest.movgestAnno = :annoMovimento "
			+ "             AND tmt.siacTMovgest.movgestNumero = CAST(CAST(:numeroMovimento AS string) AS integer) "
			+ "             AND (:numeroSubmovimento IS NULL OR tmt.movgestTsCode = CAST(:numeroSubmovimento as string))"
			//+ "             AND (:numeroSubmovimento IS NULL OR :numeroSubmovimento = '' OR tmt.movgestTsCode = :numeroSubmovimento) "
			+ "             AND tmt.siacTMovgest.siacDMovgestTipo.movgestTipoCode = 'I' "
			+ "         ) "
			+ "     ) "
			+ " ) ",
			NumeroMovimentoType.BIG_DECIMAL, NumeroSubmovimentoType.STRING),

	ModificaMovimentoGestioneEntrata(" FROM SiacTModifica m "
			+ " WHERE m.dataCancellazione IS NULL "
			+ " AND m.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND EXISTS ( "
			+ "     FROM SiacRModificaStato rms "
			+ "     WHERE rms.siacTModifica = m "
			+ "     AND rms.dataCancellazione IS NULL "
			+ "     AND ("
			+ "         EXISTS ( "
			+ "             FROM SiacRMovgestTsSogMod rmtsm, SiacTMovgestT tmt "
			+ "             WHERE rmtsm.siacRModificaStato = rms "
			+ "             AND tmt = rmtsm.siacTMovgestT "
			+ "             AND rmtsm.dataCancellazione IS NULL "
			+ "             AND tmt.siacTMovgest.movgestAnno = :annoMovimento "
			+ "             AND tmt.siacTMovgest.movgestNumero = CAST(CAST(:numeroMovimento AS string) AS integer) "
			+ "             AND (:numeroSubmovimento IS NULL OR tmt.movgestTsCode = CAST(:numeroSubmovimento as string))"
//			+ "             AND (:numeroSubmovimento IS NULL OR :numeroSubmovimento = '' OR tmt.movgestTsCode = :numeroSubmovimento) "
			+ "             AND tmt.siacTMovgest.siacDMovgestTipo.movgestTipoCode = 'A' "
			+ "         ) OR EXISTS( "
			+ "             FROM SiacRMovgestTsSogclasseMod rmtsm, SiacTMovgestT tmt "
			+ "             WHERE rmtsm.siacRModificaStato = rms "
			+ "             AND tmt = rmtsm.siacTMovgestT "
			+ "             AND rmtsm.dataCancellazione IS NULL "
			+ "             AND tmt.siacTMovgest.movgestAnno = :annoMovimento "
			+ "             AND tmt.siacTMovgest.movgestNumero = CAST(CAST(:numeroMovimento AS string) AS integer) "
			+ "             AND (:numeroSubmovimento IS NULL OR tmt.movgestTsCode = CAST(:numeroSubmovimento as string))"
//			+ "             AND (:numeroSubmovimento IS NULL OR :numeroSubmovimento = '' OR tmt.movgestTsCode = :numeroSubmovimento) "
			+ "             AND tmt.siacTMovgest.siacDMovgestTipo.movgestTipoCode = 'A' "
			+ "         ) OR EXISTS( "
			+ "             FROM SiacTMovgestTsDetMod tmtdm, SiacTMovgestT tmt "
			+ "             WHERE tmtdm.siacRModificaStato = rms "
			+ "             AND tmt = tmtdm.siacTMovgestT "
			+ "             AND tmtdm.dataCancellazione IS NULL "
			+ "             AND tmt.siacTMovgest.movgestAnno = :annoMovimento "
			+ "             AND tmt.siacTMovgest.movgestNumero = CAST(CAST(:numeroMovimento AS string) AS integer) "
			+ "             AND (:numeroSubmovimento IS NULL OR tmt.movgestTsCode = CAST(:numeroSubmovimento as string))"
//			+ "             AND (:numeroSubmovimento IS NULL OR :numeroSubmovimento = '' OR tmt.movgestTsCode = :numeroSubmovimento) "
			+ "             AND tmt.siacTMovgest.siacDMovgestTipo.movgestTipoCode = 'A' "
			+ "         ) "
			+ "     ) "
			+ " ) ",
			NumeroMovimentoType.BIG_DECIMAL, NumeroSubmovimentoType.STRING),
			
			
	RichiestaEconomale(" FROM SiacTRichiestaEcon r WHERE r.dataCancellazione IS NULL AND r.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " AND r.siacTBil.siacTPeriodo.anno = CAST(:annoMovimento AS string) AND r.riceconNumero = :numeroMovimento ",
			NumeroMovimentoType.INTEGER, null),
			
	RendicontoRichiesta(" FROM SiacTGiustificativo g WHERE g.dataCancellazione IS NULL AND g.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId"
			+ " AND g.siacTRichiestaEcon.siacTBil.siacTPeriodo.anno = CAST(:annoMovimento as string) AND g.siacTRichiestaEcon.riceconNumero = :numeroMovimento ",
			NumeroMovimentoType.INTEGER, null), 
	
	
	Rateo(" FROM SiacTPrimaNotaRateiRisconti r WHERE r.dataCancellazione IS NULL AND r.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND r.anno = CAST(:annoMovimento AS string) ", 
			null, null), 
	
	
	Risconto(" FROM SiacTPrimaNotaRateiRisconti r WHERE r.dataCancellazione IS NULL AND r.siacTEnteProprietario.enteProprietarioId = :enteProprietarioId "
			+ " AND r.anno = CAST(:annoMovimento AS string) ", 
			null, null), 

	;
	
	private final String jpql;
	private final NumeroMovimentoType numeroMovimentoType;
	private final NumeroSubmovimentoType numeroSubmovimentoType;
	
	
	private MovimentoJpqlEnum(String jpql, NumeroMovimentoType numeroMovimentoType, NumeroSubmovimentoType numeroSubmovimentoType) {
		this.jpql = jpql;
		this.numeroMovimentoType = numeroMovimentoType;
		this.numeroSubmovimentoType = numeroSubmovimentoType;
	}


	/**
	 * @return the jpql
	 */
	public String getJpql() {
		return jpql;
	}
	
	/**
	 * @return the numeroMovimentoType se la query filtra per il numero del movimento, null altrimenti
	 */
	public NumeroMovimentoType getNumeroMovimentoType() {
		return numeroMovimentoType;
	}

	/**
	 * @return the numeroSubmovimentoType se la query filtra per il numero del submovimento, null altrimenti
	 */
	public NumeroSubmovimentoType getNumeroSubmovimentoType() {
		return numeroSubmovimentoType;
	}


	/**
	 * Converte il tipo del numeroMovimento da String al tipo previsto per la query jpql.
	 * 
	 * @param numeroMovimento
	 * @return il numeroMovimento convertito
	 * @throws UnsupportedOperationException se la query non prevede il numeroMovimento
	 */
	public Object toNumeroMovimentoType(String numeroMovimento){
		if(numeroMovimentoType==null){
			throw new UnsupportedOperationException("Non esiste il numeroMovimento per la query jpql MovimentoJpqlEnum." + this.name());
		} 
		
		return numeroMovimentoType.performConversion(numeroMovimento);
	}
	
	/**
	 * Converte il tipo del numeroMovimento da String al tipo previsto per la query jpql.
	 * 
	 * @param numeroSubmovimento
	 * @return il numeroSubmovimento convertito
	 * @throws UnsupportedOperationException se la query non prevede il numeroSubmovimento
	 */
	public Object toNumeroSubmovimentoType(Integer numeroSubmovimento){
		if(numeroSubmovimentoType==null){
			throw new UnsupportedOperationException("Non esiste il numeroSubmovimentoType per la query jpql MovimentoJpqlEnum." + this.name());
		} 
		
		return numeroSubmovimentoType.performConversion(numeroSubmovimento);
	}
	
	/**
	 * Converter il tipo del NumeroMovimento da String al tipo impostato per la query JPQL.
	 * @author Domenico
	 *
	 */
	private static enum NumeroMovimentoType {
		BIG_DECIMAL(new ConversionPerformer() {
			private static final long serialVersionUID = 2610093571151165532L;
			@Override
			public Object perform(String numeroMovimento) {
				if(numeroMovimento==null){
					return null;
				}
				try {
					return new BigDecimal(numeroMovimento);
				} catch (RuntimeException re){
					throw new IllegalArgumentException("Il numero del movimento deve essere di tipo numerico per l'evento selezionato.");
				}
			}
		}),
		
		INTEGER(new ConversionPerformer() {
			private static final long serialVersionUID = -1131223236489032775L;
			@Override
			public Object perform(String numeroMovimento) {
				if(numeroMovimento==null){
					return null;
				}
				try {
					return Integer.valueOf(numeroMovimento);
				} catch (RuntimeException re){
					throw new IllegalArgumentException("Il numero del movimento deve essere di tipo numerico per l'evento selezionato");
				}
			}
		}),
		
		STRING(new ConversionPerformer() {
			private static final long serialVersionUID = -536565899683652275L;
			@Override
			public Object perform(String numeroMovimento) {
				return numeroMovimento;
			}
		}), 
		
		;
		
		private ConversionPerformer conversionPerformer;
		private NumeroMovimentoType(ConversionPerformer conversionPerformer) {
			this.conversionPerformer = conversionPerformer;
		}
		
		public Object performConversion(String numeroMovimento) {
			return conversionPerformer.perform(numeroMovimento);
		}
		
		private abstract static class ConversionPerformer implements Serializable {
			private static final long serialVersionUID = -5519706256278341081L;
			public abstract Object perform(String numeroMovimento);
		}
	}
	
	/**
	 * Converter il tipo del NumeroSubmovimento da Integer al tipo impostato per la query JPQL.
	 * 
	 * @author Domenico
	 *
	 */
	private static enum NumeroSubmovimentoType {
		INTEGER(new ConversionPerformer() {
			private static final long serialVersionUID = -6326723710612125963L;
			@Override
			public Object perform(Integer numeroSubmovimento) {
				return numeroSubmovimento;
			}
		}), 
		
		STRING(new ConversionPerformer() {
			private static final long serialVersionUID = 7987401691144919237L;
			@Override
			public Object perform(Integer numeroSubmovimento) {
				return numeroSubmovimento==null?null:numeroSubmovimento.toString();
			}
		}), 
		
		;
		
		private ConversionPerformer conversionPerformer;
		private NumeroSubmovimentoType(ConversionPerformer conversionPerformer) {
			this.conversionPerformer = conversionPerformer;
		}
		
		public Object performConversion(Integer numeroSubmovimento) {
			return conversionPerformer.perform(numeroSubmovimento);
		}
		
		private abstract static class ConversionPerformer implements Serializable {
			private static final long serialVersionUID = 4551799700919915337L;
			public abstract Object perform(Integer numeroSubmovimento);
		}
	}
}
