/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.consultazioneentita;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaCapitoloEntrataConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaCapitoloSpesaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaProvvedimentoConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.ParametriRicercaSoggettoConsultabile;
import it.csi.siac.siacconsultazioneentitaser.model.TipoEntitaConsultabile;

/**
 * Enumera le function di ricerca delle entita consultabili.
 * 
 * @author Domenico Lisi
 *
 */
public enum FunctionEntitaConsultabile {
	
	CAPITOLOSPESA_FROM_NULL(null, TipoEntitaConsultabile.CAPITOLOSPESA, "fnc_siac_cons_entita_capitolospesa", false, ParametriRicercaCapitoloSpesaConsultabile.class),
	CAPITOLOENTRATA_FROM_NULL(null, TipoEntitaConsultabile.CAPITOLOENTRATA, "fnc_siac_cons_entita_capitoloentrata", false, ParametriRicercaCapitoloEntrataConsultabile.class),
	PROVVEDIMENTO_FROM_NULL(null, TipoEntitaConsultabile.PROVVEDIMENTO, "fnc_siac_cons_entita_provvedimento", false, ParametriRicercaProvvedimentoConsultabile.class),
	SOGGETTO_FROM_NULL(null, TipoEntitaConsultabile.SOGGETTO, "fnc_siac_cons_entita_soggetto", false, ParametriRicercaSoggettoConsultabile.class),
	
	IMPEGNO_FROM_CAPITOLOSPESA(TipoEntitaConsultabile.CAPITOLOSPESA, TipoEntitaConsultabile.IMPEGNO, true, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE, FunctionEntitaConsultabileParam.ANNO_ESERCIZIO, FunctionEntitaConsultabileParam.FILTRO_GENERICO_0)),
	IMPEGNO_FROM_PROVVEDIMENTO(TipoEntitaConsultabile.PROVVEDIMENTO, TipoEntitaConsultabile.IMPEGNO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE, FunctionEntitaConsultabileParam.ANNO_ESERCIZIO)),
	IMPEGNO_FROM_SOGGETTO(TipoEntitaConsultabile.SOGGETTO, TipoEntitaConsultabile.IMPEGNO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE, FunctionEntitaConsultabileParam.ANNO_ESERCIZIO)),
	
	LIQUIDAZIONE_FROM_CAPITOLOSPESA(TipoEntitaConsultabile.CAPITOLOSPESA, TipoEntitaConsultabile.LIQUIDAZIONE, true, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE, FunctionEntitaConsultabileParam.ANNO_ESERCIZIO, FunctionEntitaConsultabileParam.FILTRO_GENERICO_0)),
	LIQUIDAZIONE_FROM_IMPEGNO(TipoEntitaConsultabile.IMPEGNO, TipoEntitaConsultabile.LIQUIDAZIONE, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE, FunctionEntitaConsultabileParam.ANNO_ESERCIZIO)),
	LIQUIDAZIONE_FROM_PROVVEDIMENTO(TipoEntitaConsultabile.PROVVEDIMENTO, TipoEntitaConsultabile.LIQUIDAZIONE, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE, FunctionEntitaConsultabileParam.ANNO_ESERCIZIO)),
	LIQUIDAZIONE_FROM_SOGGETTO(TipoEntitaConsultabile.SOGGETTO, TipoEntitaConsultabile.LIQUIDAZIONE, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE, FunctionEntitaConsultabileParam.ANNO_ESERCIZIO)),
	
	MANDATO_FROM_CAPITOLOSPESA(TipoEntitaConsultabile.CAPITOLOSPESA, TipoEntitaConsultabile.MANDATO, true, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE, FunctionEntitaConsultabileParam.FILTRO_GENERICO_0)),
	MANDATO_FROM_IMPEGNO(TipoEntitaConsultabile.IMPEGNO, TipoEntitaConsultabile.MANDATO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	MANDATO_FROM_LIQUIDAZIONE(TipoEntitaConsultabile.LIQUIDAZIONE, TipoEntitaConsultabile.MANDATO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	MANDATO_FROM_PROVVEDIMENTO(TipoEntitaConsultabile.PROVVEDIMENTO, TipoEntitaConsultabile.MANDATO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	// SIAC-5919
	MANDATO_FROM_SOGGETTO(TipoEntitaConsultabile.SOGGETTO, TipoEntitaConsultabile.MANDATO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE,FunctionEntitaConsultabileParam.ANNO_ESERCIZIO)),
	
	VARIAZIONE_FROM_CAPITOLOSPESA(TipoEntitaConsultabile.CAPITOLOSPESA, TipoEntitaConsultabile.VARIAZIONE, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	VARIAZIONE_FROM_CAPITOLOENTRATA(TipoEntitaConsultabile.CAPITOLOENTRATA, TipoEntitaConsultabile.VARIAZIONE, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	VARIAZIONE_FROM_PROVVEDIMENTO(TipoEntitaConsultabile.PROVVEDIMENTO, TipoEntitaConsultabile.VARIAZIONE, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	VARIAZIONE_FROM_SOGGETTO(TipoEntitaConsultabile.SOGGETTO, TipoEntitaConsultabile.VARIAZIONE, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	
	ACCERTAMENTO_FROM_CAPITOLOENTRATA(TipoEntitaConsultabile.CAPITOLOENTRATA, TipoEntitaConsultabile.ACCERTAMENTO, true, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE, FunctionEntitaConsultabileParam.ANNO_ESERCIZIO, FunctionEntitaConsultabileParam.FILTRO_GENERICO_0)),
	
	ACCERTAMENTO_FROM_SOGGETTO(TipoEntitaConsultabile.SOGGETTO, TipoEntitaConsultabile.ACCERTAMENTO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE, FunctionEntitaConsultabileParam.ANNO_ESERCIZIO)),
	// SIAC-4588
	ACCERTAMENTO_FROM_PROVVEDIMENTO(TipoEntitaConsultabile.PROVVEDIMENTO, TipoEntitaConsultabile.ACCERTAMENTO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE, FunctionEntitaConsultabileParam.ANNO_ESERCIZIO)),
	
	REVERSALE_FROM_CAPITOLOENTRATA(TipoEntitaConsultabile.CAPITOLOENTRATA, TipoEntitaConsultabile.REVERSALE, true, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE, FunctionEntitaConsultabileParam.FILTRO_GENERICO_0)),
	REVERSALE_FROM_ACCERTAMENTO(TipoEntitaConsultabile.ACCERTAMENTO, TipoEntitaConsultabile.REVERSALE, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	// SIAC-5919
	REVERSALE_FROM_SOGGETTO(TipoEntitaConsultabile.SOGGETTO, TipoEntitaConsultabile.REVERSALE, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE,FunctionEntitaConsultabileParam.ANNO_ESERCIZIO)),
	// SIAC-4588
	REVERSALE_FROM_PROVVEDIMENTO(TipoEntitaConsultabile.PROVVEDIMENTO, TipoEntitaConsultabile.REVERSALE, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	
	ALLEGATO_FROM_PROVVEDIMENTO(TipoEntitaConsultabile.PROVVEDIMENTO, TipoEntitaConsultabile.ALLEGATO,false,  Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	
	ELENCO_FROM_PROVVEDIMENTO(TipoEntitaConsultabile.PROVVEDIMENTO, TipoEntitaConsultabile.ELENCO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	ELENCO_FROM_ALLEGATO(TipoEntitaConsultabile.ALLEGATO, TipoEntitaConsultabile.ELENCO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	
	DOCUMENTO_FROM_PROVVEDIMENTO(TipoEntitaConsultabile.PROVVEDIMENTO, TipoEntitaConsultabile.DOCUMENTO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	DOCUMENTO_FROM_ELENCO(TipoEntitaConsultabile.ELENCO, TipoEntitaConsultabile.DOCUMENTO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	DOCUMENTO_FROM_ALLEGATO(TipoEntitaConsultabile.ALLEGATO, TipoEntitaConsultabile.DOCUMENTO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	DOCUMENTO_FROM_CAPITOLOSPESA(TipoEntitaConsultabile.CAPITOLOSPESA, TipoEntitaConsultabile.DOCUMENTO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	DOCUMENTO_FROM_CAPITOLOENTRATA(TipoEntitaConsultabile.CAPITOLOENTRATA, TipoEntitaConsultabile.DOCUMENTO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	DOCUMENTO_FROM_SOGGETTO(TipoEntitaConsultabile.SOGGETTO, TipoEntitaConsultabile.DOCUMENTO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),

	// SIAC-4589
	MMGSIMP_FROM_PROVVEDIMENTO(TipoEntitaConsultabile.PROVVEDIMENTO, TipoEntitaConsultabile.MODIFICA_IMPORTO_MOVIMENTO_GESTIONE_SPESA, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE, FunctionEntitaConsultabileParam.ANNO_ESERCIZIO)),
	MMGEIMP_FROM_PROVVEDIMENTO(TipoEntitaConsultabile.PROVVEDIMENTO, TipoEntitaConsultabile.MODIFICA_IMPORTO_MOVIMENTO_GESTIONE_ENTRATA, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE, FunctionEntitaConsultabileParam.ANNO_ESERCIZIO)),
	
	// SIAC-5279
	INDIRIZZO_FROM_SOGGETTO(TipoEntitaConsultabile.SOGGETTO, TipoEntitaConsultabile.INDIRIZZO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	SEDE_SECONDARIA_FROM_SOGGETTO(TipoEntitaConsultabile.SOGGETTO, TipoEntitaConsultabile.SEDE_SECONDARIA_SOGGETTO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	MODALITA_PAGAMENTO_FROM_SOGGETTO(TipoEntitaConsultabile.SOGGETTO, TipoEntitaConsultabile.MODALITA_PAGAMENTO_SOGGETTO, false, Arrays.asList(FunctionEntitaConsultabileParam.UID_PADRE)),
	;
	
	
	private final TipoEntitaConsultabile tipoEntitaConsultabileDiPartenza;
	private final TipoEntitaConsultabile tipoEntitaConsultabileDaCercare;
	private final String functionName;
	private final Class<? extends ParametriRicercaEntitaConsultabile> parametriRicercaEntitaConsultabileClass;
	private final Iterable<FunctionEntitaConsultabileParam> params;
	// SIAC-6193
	private final boolean totaleImporti;


	private FunctionEntitaConsultabile(TipoEntitaConsultabile tipoEntitaConsultabileDiPartenza, TipoEntitaConsultabile tipoEntiataConsultabileDaCercare,
			String functionName, boolean totaleImporti, Class<? extends ParametriRicercaEntitaConsultabile> parametriRicercaEntitaConsultabileClass) {
		this(tipoEntitaConsultabileDiPartenza, tipoEntiataConsultabileDaCercare, functionName, totaleImporti, parametriRicercaEntitaConsultabileClass, null);
	}
	
	private FunctionEntitaConsultabile(TipoEntitaConsultabile tipoEntitaConsultabileDiPartenza, TipoEntitaConsultabile tipoEntiataConsultabileDaCercare, boolean totaleImporti,
			Iterable<FunctionEntitaConsultabileParam> params) {
		this(tipoEntitaConsultabileDiPartenza, tipoEntiataConsultabileDaCercare, null, totaleImporti, null, params);
	}
	
	private FunctionEntitaConsultabile(TipoEntitaConsultabile tipoEntitaConsultabileDiPartenza, TipoEntitaConsultabile tipoEntiataConsultabileDaCercare,
			String functionName, boolean totaleImporti, Class<? extends ParametriRicercaEntitaConsultabile> parametriRicercaEntitaConsultabileClass,
			Iterable<FunctionEntitaConsultabileParam> params) {
		
		this.tipoEntitaConsultabileDiPartenza = tipoEntitaConsultabileDiPartenza;
		this.tipoEntitaConsultabileDaCercare = tipoEntiataConsultabileDaCercare;
		this.functionName = functionName;
		this.totaleImporti = totaleImporti;
		this.parametriRicercaEntitaConsultabileClass = parametriRicercaEntitaConsultabileClass;
		this.params = params;
		
	}

	/**
	 * @return the tipoEntitaConsultabile
	 */
	public TipoEntitaConsultabile getTipoEntitaConsultabileDiPartenza() {
		return tipoEntitaConsultabileDiPartenza;
	}
	/**
	 * @return the tipoEntitaConsultabileDaCercare
	 */
	public TipoEntitaConsultabile getTipoEntitaConsultabileDaCercare() {
		return tipoEntitaConsultabileDaCercare;
	}
	/**
	 * @return the totaleImporti
	 */
	public boolean isTotaleImporti() {
		return this.totaleImporti;
	}

	/**
	 * @return the params
	 */
	public Iterable<FunctionEntitaConsultabileParam> getParams() {
		return params;
	}

	/**
	 * Nome della function per la ricerca a partire da un entita consultabile padre.
	 * 
	 * @return the functionName
	 */
	public String getFunctionName() {
		if(this.functionName!=null){
			//se e' stato specificato un nome lo restituisco
			return this.functionName;
		}
		//altrimenti restituisco il nome secondo la regola:
		return "fnc_siac_cons_entita_"+name().toLowerCase();
	}
	
	/**
	 * Nome della function che ottiene il totale degli elementi restituiti
	 * dalle function {@link #getFunctionName()} e {@link #getFunctionNameRic()}
	 * @return the functionName desc
	 */
	public String getFunctionNameTotal() {
		return getFunctionName()+"_total";
	}
	
	/**
	 * Nome della function che ottiene l'importo
	 * dalle function {@link #getFunctionName()} e {@link #getFunctionNameRic()}
	 * @return the functionName desc
	 */
	public String getFunctionNameImporto() {
		return getFunctionName()+"_importo";
	}

	/**
	 * @return the parametriRicercaEntitaConsultabileClass
	 */
	public Class<? extends ParametriRicercaEntitaConsultabile> getParametriRicercaEntitaConsultabileClass() {
		return parametriRicercaEntitaConsultabileClass;
	}

	public Object[] getParams(Map<FunctionEntitaConsultabileParam, Object> values) {
		List<Object> res = new ArrayList<Object>();
		for(FunctionEntitaConsultabileParam param : this.params) {
			if(values.containsKey(param)) {
				res.add(values.get(param));
			}
		}
		return res.toArray(new Object[res.size()]);
	}


	public static FunctionEntitaConsultabile byParametriRicercaEntitaConsultabileClass(Class<? extends ParametriRicercaEntitaConsultabile> c) {
		for(FunctionEntitaConsultabile tecf : FunctionEntitaConsultabile.values()){
			if(tecf.getParametriRicercaEntitaConsultabileClass()!=null 
					&& tecf.getParametriRicercaEntitaConsultabileClass().equals(c)){
				return tecf;
			}
		}
		throw new IllegalArgumentException("Nessun mapping in TipoEntitaConsultabileFunctions associato ai ParametriRicercaEntitaConsultabile di tipo : "+ c);
	}

	
	public static FunctionEntitaConsultabile byTipoEntitaConsultabileDiPartenzaEDaCercare(TipoEntitaConsultabile tipoEntitaConsultabileDiPartenza, TipoEntitaConsultabile tipoEntiataConsultabileDaCercare) {
		for(FunctionEntitaConsultabile tecf : FunctionEntitaConsultabile.values()){
			if(((tecf.getTipoEntitaConsultabileDiPartenza() == null && tipoEntitaConsultabileDiPartenza == null) 
					|| tecf.getTipoEntitaConsultabileDiPartenza()!=null && tecf.getTipoEntitaConsultabileDiPartenza().equals(tipoEntitaConsultabileDiPartenza))
					
					&&
					
					((tecf.getTipoEntitaConsultabileDaCercare() == null && tipoEntiataConsultabileDaCercare == null) 
							|| tecf.getTipoEntitaConsultabileDaCercare()!=null && tecf.getTipoEntitaConsultabileDaCercare().equals(tipoEntiataConsultabileDaCercare))
					){
				return tecf;
			}
		}
		throw new IllegalArgumentException("Nessun mapping in TipoEntitaConsultabileFunctions associato al tipoEntiataConsultabile: "+ tipoEntiataConsultabileDaCercare);
	}

}
