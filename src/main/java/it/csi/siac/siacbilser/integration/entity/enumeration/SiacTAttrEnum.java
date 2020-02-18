/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entity.enumeration;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;

import it.csi.siac.siacbilser.integration.entitymapping.converter.handler.CigAttrHandler;
import it.csi.siac.siacbilser.integration.entitymapping.converter.handler.CupAttrHandler;
import it.csi.siac.siacbilser.integration.entitymapping.converter.handler.SiacTAttrHandler;
import it.csi.siac.siacbilser.model.TipoMediaPrescelta;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siaccommon.util.log.LogUtil;


/**
 * The Enum SiacTAttrEnum.
 */
@EnumEntity(entityName="SiacTAttr", idPropertyName="attrId", codePropertyName="attrCode")
public enum SiacTAttrEnum {	
	
	//Attributi trasversali
	Note("Note", String.class, "note", TipologiaAttributo.NOTE, SiacDAttrTipoEnum.Testo),
	
	//Attributi legati a Capitolo
	FlagAssegnabile("FlagAssegnabile", Boolean.class,"flagAssegnabile", TipologiaAttributo.FLAG_ASSEGNABILE, SiacDAttrTipoEnum.Boolean),
	FlagFondoSvalutazioneCrediti("FlagFondoSvalutazioneCrediti", Boolean.class, "flagFondoSvalutazioneCred", TipologiaAttributo.FLAG_FONDO_SVALUTAZIONE_CREDITI, SiacDAttrTipoEnum.Boolean),
	FlagFunzioniDelegate("FlagFunzioniDelegate", Boolean.class, "funzDelegateRegione", TipologiaAttributo.FLAG_FUNZIONI_DELEGATE, SiacDAttrTipoEnum.Boolean),
	FlagPerMemoria("FlagPerMemoria", Boolean.class, "flagPerMemoria", TipologiaAttributo.FLAG_PER_MEMORIA, SiacDAttrTipoEnum.Boolean),
	FlagRilevanteIva("FlagRilevanteIva", Boolean.class, "flagRilevanteIva", TipologiaAttributo.FLAG_RILEVANTE_IVA, SiacDAttrTipoEnum.Boolean),
	FlagTrasferimentoOrganiComunitari("FlagTrasferimentoOrganiComunitari", Boolean.class, "flagTrasferimentiOrgComunitari", TipologiaAttributo.FLAG_TRASFERIMENTO_ORGANI_COMUNITARI, SiacDAttrTipoEnum.Boolean),
	FlagEntrateRicorrenti("FlagEntrateRicorrenti", Boolean.class, "flagEntrateRicorrenti", TipologiaAttributo.FLAG_ENTRATE_RICORRENTI, SiacDAttrTipoEnum.Boolean),
	@Deprecated FlagFondoPluriennaleVinc("FlagFondoPluriennaleVinc", Boolean.class,"flagFondoPluriennaleVinc", TipologiaAttributo.FLAG_FONDO_PLURIENNALE_VINC, SiacDAttrTipoEnum.Boolean),
	FlagImpegnabile("FlagImpegnabile", Boolean.class,"flagImpegnabile", TipologiaAttributo.FLAG_IMPEGNABILE, SiacDAttrTipoEnum.Numerico),
	FlagTrasferimentiVincolati("FlagTrasferimentiVincolati", Boolean.class, "flagTrasferimentiVincolati", TipologiaAttributo.FLAG_TRASFERIMENTI_VINCOLATI, SiacDAttrTipoEnum.Boolean),
	// SIAC-5582
	FlagAccertatoPerCassa("FlagAccertatoPerCassa", Boolean.class, "flagAccertatoPerCassa", TipologiaAttributo.FLAG_ACCERTATO_PER_CASSA, SiacDAttrTipoEnum.Boolean),

	//Attributi legati a Progetto e Cronoprogramma
	FlagRilevanteFondoPluriennaleVincolato("FlagRilevanteFPV", Boolean.class, "rilevanteFPV", TipologiaAttributo.FLAG_RILEVANTE_FPV, SiacDAttrTipoEnum.Boolean),
	ValoreComplessivoProgetto("ValoreComplessivoProgramma", BigDecimal.class, "valoreComplessivo", TipologiaAttributo.VALORE_COMPLESSIVO_PROGETTO, SiacDAttrTipoEnum.Numerico),
	FlagCronoprogrammaDaDefinire("FlagCronoprogrammaDaDefinire", Boolean.class, "cronoprogrammaDaDefinire", TipologiaAttributo.FLAG_CRONOPROGRAMMA_DA_DEFINIRE, SiacDAttrTipoEnum.Boolean),

	
	//Attributi legati a Documento
	NumeroRepertorio("num_repertorio", String.class, "numeroRepertorio", TipologiaAttributo.NUMERO_REPERTORIO, SiacDAttrTipoEnum.Testo),
	DataRepertorio("data_repertorio", Date.class, "dataRepertorio", TipologiaAttributo.DATA_REPERTORIO, SiacDAttrTipoEnum.Testo),
	AnnoRepertorio("anno_repertorio", Integer.class, "annoRepertorio", TipologiaAttributo.ANNO_REPERTORIO, SiacDAttrTipoEnum.Testo),
	RegistroRepertorio("registro_repertorio", String.class, "registroRepertorio", TipologiaAttributo.REGISTRO_REPERTORIO, SiacDAttrTipoEnum.Testo),
	Arrotondamento("arrotondamento", BigDecimal.class, "arrotondamento", TipologiaAttributo.ARROTONDAMENTO, SiacDAttrTipoEnum.Numerico),
	TerminePagamento("terminepagamento", Integer.class, "terminePagamento", TipologiaAttributo.TERMINE_PAGAMENTO, SiacDAttrTipoEnum.Numerico),
	@Deprecated
	CausaleSospensione("causale_sospensione", String.class, "causaleSospensione", TipologiaAttributo.CAUSALE_SOSPENSIONE, SiacDAttrTipoEnum.Testo),
	@Deprecated
	DataSospensione("data_sospensione", Date.class, "dataSospensione", TipologiaAttributo.DATA_SOSPENSIONE, SiacDAttrTipoEnum.Testo),
	@Deprecated
	DataRiattivazione("data_riattivazione",  Date.class, "dataRiattivazione", TipologiaAttributo.DATA_RIATTIVAZIONE, SiacDAttrTipoEnum.Testo),
	CodiceFiscalePignorato("codiceFiscalePignorato",  String.class, "codiceFiscalePignorato", TipologiaAttributo.CODICE_FISCALE_PIGNORATO, SiacDAttrTipoEnum.Testo),
	DataRicezionePortale("dataRicezionePortale", Date.class, "dataRicezionePortale", TipologiaAttributo.DATA_RICEZIONE_PORTALE, SiacDAttrTipoEnum.Testo),
	DataScadenzaDopoSospensione("dataScadenzaDopoSospensione", Date.class, "dataScadenzaDopoSospensione", TipologiaAttributo.DATA_SCADENZA_DOPO_SOSPENSIONE, SiacDAttrTipoEnum.Testo),
	FlagAggiornaQuoteDaElenco("flagAggiornaQuoteDaElenco", Boolean.class, "flagAggiornaQuoteDaElenco", TipologiaAttributo.FLAG_AGGIORNA_QUOTE_DA_ELENCO, SiacDAttrTipoEnum.Boolean),
	FlagSenzaNumero("flagSenzaNumero", Boolean.class, "flagSenzaNumero", TipologiaAttributo.FLAG_SENZA_NUMERO, SiacDAttrTipoEnum.Boolean),
	FlagDisabilitaRegistrazioneResidui("flagDisabilitaRegistrazioneResidui", Boolean.class, "flagDisabilitaRegistrazioneResidui", TipologiaAttributo.FLAG_DISABILITA_REGISTRAZIONE_RESIDUI, SiacDAttrTipoEnum.Boolean),
	// SIAC-4749
	FlagPagataIncassata("flagPagataIncassata", Boolean.class, "datiFatturaPagataIncassata.flagPagataIncassata", TipologiaAttributo.FLAG_PAGATA_INCASSATA, SiacDAttrTipoEnum.Boolean),
	NotePagamentoIncasso("notePagamentoIncasso", String.class, "datiFatturaPagataIncassata.notePagamentoIncasso", TipologiaAttributo.NOTE_PAGAMENTO_INCASSO, SiacDAttrTipoEnum.Testo),
	DataOperazione("dataOperazionePagamentoIncasso", Date.class, "datiFatturaPagataIncassata.dataOperazione", TipologiaAttributo.DATA_OPERAZIONE_PAGAMENTO_INCASSO, SiacDAttrTipoEnum.Testo),
	
	//Attributi legati a Subdocumento
	FlagAvviso("flagAvviso",  Boolean.class , "flagAvviso" , TipologiaAttributo.FLAG_AVVISO, SiacDAttrTipoEnum.Boolean),
	FlagEsproprio("flagEsproprio",  Boolean.class , "flagEsproprio" , TipologiaAttributo.FLAG_ESPROPRIO, SiacDAttrTipoEnum.Boolean),
	FlagOrdinativoManuale("flagOrdinativoManuale",  Boolean.class , "flagOrdinativoManuale" , TipologiaAttributo.FLAG_ORDINATIVO_MANUALE, SiacDAttrTipoEnum.Boolean),
	FlagOrdinativoSingolo("flagOrdinativoSingolo",  Boolean.class , "flagOrdinativoSingolo" , TipologiaAttributo.FLAG_ORDINATIVO_SINGOLO, SiacDAttrTipoEnum.Boolean),
	FlagRilevanteIVA("flagRilevanteIVA",  Boolean.class , "flagRilevanteIVA" , TipologiaAttributo.FLAG_RILEVANTE_IVA, SiacDAttrTipoEnum.Boolean), //verificare sovrapposizione con FlagRilevanteIva (va minuscole)
	CausaleOrdinativo("causaleOrdinativo",  String.class , "causaleOrdinativo" , TipologiaAttributo.FLAG_CAUSALE_ORDINATIVO, SiacDAttrTipoEnum.Testo),
	Cig("cig",  String.class , "cig" , TipologiaAttributo.CIG, SiacDAttrTipoEnum.Testo, CigAttrHandler.class),
	Cup("cup",  String.class , "cup" , TipologiaAttributo.CUP, SiacDAttrTipoEnum.Testo, CupAttrHandler.class),
	/** @deprecated Lasciato per retrocompatibilità. Ora viene utilizzata l'Entita {@link it.csi.siac.siacfinser.model.mutuo.VoceMutuo}. */
	@Deprecated NumeroMutuo("numeroMutuo",  String.class , "numeroMutuo" , TipologiaAttributo.NUMERO_MUTUO, SiacDAttrTipoEnum.Numerico),
	DataEsecuzionePagamento("dataEsecuzionePagamento",  Date.class , "dataEsecuzionePagamento" , TipologiaAttributo.DATA_ESECUZIONE_PAGAMENTO, SiacDAttrTipoEnum.Testo),
	
	//Attributi legati a Subdocumento <>---- Dati Certificazione Crediti
	Annotazione("annotazione", String.class, "annotazione", TipologiaAttributo.ANNOTAZIONE, SiacDAttrTipoEnum.Testo),
	NoteCertificazione("noteCertificazione", String.class, "noteCertificazione", TipologiaAttributo.NOTE_CERTIFICAZIONE, SiacDAttrTipoEnum.Testo),
	NumeroCertificazione("numeroCertificazione", String.class, "numeroCertificazione", TipologiaAttributo.NUMERO_CERTIFICAZIONE, SiacDAttrTipoEnum.Testo),
	DataCertificazione("dataCertificazione", Date.class, "dataCertificazione", TipologiaAttributo.DATA_CERTIFICAZIONE, SiacDAttrTipoEnum.Testo),
	FlagCertificazione("flagCertificazione", Boolean.class, "flagCertificazione", TipologiaAttributo.FLAG_CERTIFICAZIONE, SiacDAttrTipoEnum.Boolean),

	//RitenuteDocumento
	ImportoCassaPensioni("importoCassaPensioni", BigDecimal.class, "importoCassaPensioni", TipologiaAttributo.IMPORTO_CASSA_PENSIONI, SiacDAttrTipoEnum.Numerico),
	ImportoEsente("importoEsente", BigDecimal.class, "importoEsente", TipologiaAttributo.IMPORTO_ESENTE, SiacDAttrTipoEnum.Numerico),
	ImportoIVA("importoIVA", BigDecimal.class, "importoIVA", TipologiaAttributo.IMPORTO_IVA, SiacDAttrTipoEnum.Numerico),
	ImportoRivalsa("importoRivalsa", BigDecimal.class, "importoRivalsa", TipologiaAttributo.IMPORTO_RIVALSA, SiacDAttrTipoEnum.Numerico),
	
	//Attributi legati a Documento Tipo
	FlagRitenute("flagRitenute", Boolean.class,"flagRitenute", TipologiaAttributo.FLAG_RITENUTE, SiacDAttrTipoEnum.Boolean),
	FlagNotaCredito("flagNotaCredito", Boolean.class,"flagNotaCredito", TipologiaAttributo.FLAG_NOTA_CREDITO, SiacDAttrTipoEnum.Boolean),
	FlagPenaleAltro("flagPenaleAltro", Boolean.class,"flagPenaleAltro", TipologiaAttributo.FLAG_PENALE_ALTRO, SiacDAttrTipoEnum.Boolean),
	FlagSpesaCollegata("flagSpesaCollegata", Boolean.class,"flagSpesaCollegata", TipologiaAttributo.FLAG_SPESA_COLLEGATA, SiacDAttrTipoEnum.Boolean),
	FlagIVA("flagIVA", Boolean.class,"flagIVA", TipologiaAttributo.FLAG_IVA, SiacDAttrTipoEnum.Boolean),
	FlagSubordinato("flagSubordinato", Boolean.class,"flagSubordinato", TipologiaAttributo.FLAG_SUBORDINATO, SiacDAttrTipoEnum.Boolean),
	FlagRegolarizzazione("flagRegolarizzazione", Boolean.class,"flagRegolarizzazione", TipologiaAttributo.FLAG_REGOLARIZZAZIONE, SiacDAttrTipoEnum.Boolean),
	FlagAttivaGEN("flagAttivaGEN", Boolean.class,"flagAttivaGEN", TipologiaAttributo.FLAG_ATTIVA_GEN, SiacDAttrTipoEnum.Boolean),
	FlagComunicaPCC("flagComunicaPCC", Boolean.class,"flagComunicaPCC", TipologiaAttributo.FLAG_COMUNICA_PCC, SiacDAttrTipoEnum.Boolean),
	// SIAC-4748
	FlagRegistroUnico("flagRegistroUnico", Boolean.class, "flagRegistroUnico", TipologiaAttributo.FLAG_REGISTRO_UNICO, SiacDAttrTipoEnum.Boolean),
	
	// SIAC-6677
	FlagNumerazioneAutomaticaDaIVA("flagNumerazioneAutomaticaDaIVA", Boolean.class, "flagNumerazioneAutomaticaDaIVA", TipologiaAttributo.FLAG_NUMERAZIONE_AUTOMATICA_DA_IVA, SiacDAttrTipoEnum.Boolean),
	
	//Attributi legati a TipoOnere
	AliquotaCaricoEnte("ALIQUOTA_ENTE", BigDecimal.class, "aliquotaCaricoEnte", TipologiaAttributo.ALIQUOTA_CARICO_ENTE, SiacDAttrTipoEnum.Numerico),
	AliquotaCaricoSogg("ALIQUOTA_SOGG", BigDecimal.class, "aliquotaCaricoSoggetto", TipologiaAttributo.ALIQUOTA_CARICO_SOGGETTO, SiacDAttrTipoEnum.Numerico),
	Quadro770("QUADRO_770", String.class,"quadro770", TipologiaAttributo.QUADRO770, SiacDAttrTipoEnum.Testo),
	
	//Attivita IVA
	FlagRilevanteIRAP("flagRilevanteIRAP", Boolean.class, "flagRilevanteIRAP", TipologiaAttributo.FLAG_RILEVANTE_IRAP, SiacDAttrTipoEnum.Boolean),
	FlagRegistrazioneIva("flagRegistrazioneIva", Boolean.class, "flagRegistrazioneIva", TipologiaAttributo.FLAG_REGISTRAZIONE_IVA, SiacDAttrTipoEnum.Boolean),
	FlagIntracomunitario("flagIntracomunitario", Boolean.class, "flagIntracomunitario", TipologiaAttributo.FLAG_INTRACOMUNITARIO, SiacDAttrTipoEnum.Boolean),
	
	AnnoCapitoloOrigine("annoCapitoloOrigine", String.class,"annoCapitoloOrigine", TipologiaAttributo.ANNO_CAPITOLO_ORIGINE, SiacDAttrTipoEnum.Testo),
	FlagAttivaGsa("FlagAttivaGsa", Boolean.class, "flagAttivaGsa", TipologiaAttributo.FLAG_ATTIVA_GSA, SiacDAttrTipoEnum.Boolean),
	
	
	IntestazioneDirezione("intestazioneDirezione", String.class, "intestazioneDirezione", TipologiaAttributo.INTESTAZIONE_DIREZIONE, SiacDAttrTipoEnum.Testo),
	IntestazioneSettore("intestazioneSettore", String.class, "intestazioneSettore", TipologiaAttributo.INTESTAZIONE_SETTORE, SiacDAttrTipoEnum.Testo),
	IntestazioneUfficio("intestazioneUfficio", String.class, "intestazioneUfficio", TipologiaAttributo.INTESTAZIONE_UFFICIO, SiacDAttrTipoEnum.Testo),
	IntestazioneCitta("intestazioneCitta", String.class, "intestazioneCitta", TipologiaAttributo.INTESTAZIONE_CITTA, SiacDAttrTipoEnum.Testo),
	
	//Attributi legati a Conto (siac_t_pdce) //bisogna aggiungere le tipologie?
	PdceContoAttivo("pdce_conto_attivo", Boolean.class, "attivo", null, SiacDAttrTipoEnum.Boolean), 
	PdceContoDiLegge("pdce_conto_di_legge", Boolean.class, "contoDiLegge", null, SiacDAttrTipoEnum.Boolean), 
	PdceContoFoglia("pdce_conto_foglia", Boolean.class, "contoFoglia", null, SiacDAttrTipoEnum.Boolean), 
	PdceContoCodificaInterna("pdce_conto_codifica_interna", String.class, "codiceInterno", null, SiacDAttrTipoEnum.Testo),
	PdceAmmortamento("pdce_ammortamento", Boolean.class, "ammortamento", null, SiacDAttrTipoEnum.Boolean),
	
	Matricola("Matricola",String.class,"matricola",null, SiacDAttrTipoEnum.Testo),
	
	
	AccertamentoAutomatico("ACC_AUTO",Boolean.class,"automatico",null, SiacDAttrTipoEnum.Boolean),
	
	// AttributiBilancio
	FlagReimputaSpese("flagReimputaSpese", Boolean.class, "flagReimputaSpese", TipologiaAttributo.FLAG_REIMPUTA_SPESE, SiacDAttrTipoEnum.Boolean),
	FlagReimputaEntrate("flagReimputaEntrate", Boolean.class, "flagReimputaEntrate", TipologiaAttributo.FLAG_REIMPUTA_ENTRATE, SiacDAttrTipoEnum.Boolean),
	AccantonamentoGraduale("accantonamentoGraduale", Boolean.class, "accantonamentoGraduale", TipologiaAttributo.ACCANTONAMENTO_GRADUALE, SiacDAttrTipoEnum.Boolean),
	RiscossioneVirtuosa("riscossioneVirtuosa", Boolean.class, "riscossioneVirtuosa", TipologiaAttributo.RISCOSSIONE_VIRTUOSA, SiacDAttrTipoEnum.Boolean),
	MediaApplicata("mediaApplicata", TipoMediaPrescelta.class, "mediaApplicata", TipologiaAttributo.MEDIA_APPLICATA, SiacDAttrTipoEnum.Testo),
	PercentualeAccantonamentoAnno("percentualeAccantonamentoAnno", BigDecimal.class, "percentualeAccantonamentoAnno", TipologiaAttributo.PERCENTUALE_ACCANTONAMENTO_ANNO, SiacDAttrTipoEnum.Numerico),
	PercentualeAccantonamentoAnno1("percentualeAccantonamentoAnno1", BigDecimal.class, "percentualeAccantonamentoAnno1", TipologiaAttributo.PERCENTUALE_ACCANTONAMENTO_ANNO_1, SiacDAttrTipoEnum.Numerico),
	PercentualeAccantonamentoAnno2("percentualeAccantonamentoAnno2", BigDecimal.class, "percentualeAccantonamentoAnno2", TipologiaAttributo.PERCENTUALE_ACCANTONAMENTO_ANNO_2, SiacDAttrTipoEnum.Numerico),
	UltimoAnnoApprovato("ultimoAnnoApprovato", Integer.class, "ultimoAnnoApprovato", TipologiaAttributo.ULTIMO_ANNO_APPROVATO, SiacDAttrTipoEnum.Numerico),
	
	//Attributi movimenti gestione
	FlagDaRiaccertamento("flagDaRiaccertamento", Boolean.class, null, null, null),
	FlagPrenotazione("flagPrenotazione", Boolean.class, null, null, null),
	//SIAC-6261
	FlagSoggettoDurc("flagSoggettoDurc", Boolean.class,null, null, null),
	;
	
	
	public static final String FlagRilevanteIVA_Codice = "flagRilevanteIVA"; //FlagRilevanteIVA.getCodice();
	private static LogUtil log = new LogUtil(SiacTAttrEnum.class);
	private final String codice;
	private final String modelFieldName;
	
	private final Class<?> fieldType;
	
	private final TipologiaAttributo tipologiaAttributo;
	private final SiacDAttrTipoEnum siacDAttrTipoEnum;
	private final Class<? extends SiacTAttrHandler> siacTAttrHandlerType;
	
	

	/**
	 * Instantiates a new siac t attr enum.
	 *
	 * @param codice the codice
	 * @param fieldType the field type
	 * @param modelFieldName the model field name
	 * @param tipologiaAttributo the tipologia attributo
	 */
	SiacTAttrEnum(String codice, Class<?> fieldType, String modelFieldName, TipologiaAttributo tipologiaAttributo, SiacDAttrTipoEnum siacDAttrTipoEnum) {
		this(codice, fieldType, modelFieldName, tipologiaAttributo, siacDAttrTipoEnum, null);
	}


	/**
	 * Instantiates a new siac t attr enum.
	 *
	 * @param codice the codice
	 * @param fieldType the field type
	 * @param modelFieldName the model field name
	 * @param tipologiaAttributo the tipologia attributo
	 */
	SiacTAttrEnum(String codice, Class<?> fieldType, String modelFieldName, TipologiaAttributo tipologiaAttributo, 
			SiacDAttrTipoEnum siacDAttrTipoEnum, Class<? extends SiacTAttrHandler> siacTAttrHandlerType ) {
		this.codice = codice;
		this.fieldType = fieldType;
		this.modelFieldName = modelFieldName;
		this.tipologiaAttributo = tipologiaAttributo;
		this.siacDAttrTipoEnum = siacDAttrTipoEnum;
		this.siacTAttrHandlerType = siacTAttrHandlerType;
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
	 * Gets the model field name.
	 *
	 * @return the model field name
	 */
	public String getModelFieldName() {
		return modelFieldName;
	}

	/**
	 * Gets the tipologia attributo.
	 *
	 * @return the tipologia attributo
	 */
	public TipologiaAttributo getTipologiaAttributo() {
		return tipologiaAttributo;
	}

	/**
	 * @return the siacDAttrTipoEnum
	 */
	public SiacDAttrTipoEnum getSiacDAttrTipoEnum() {
		return siacDAttrTipoEnum;
	}

	/**
	 * By codice.
	 *
	 * @param codice the codice
	 * @return the siac t attr enum
	 */
	public static SiacTAttrEnum byCodice(String codice){
		for(SiacTAttrEnum e : SiacTAttrEnum.values()){
			if(e.getCodice().equals(codice)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il codice "+ codice + " non ha un mapping corrispondente in SiacTAttrEnum");
	}
	
	/**
	 * By tipologia attributo.
	 *
	 * @param tipologiaAttributo the tipologia attributo
	 * @return the siac t attr enum
	 */
	public static SiacTAttrEnum byTipologiaAttributo(TipologiaAttributo tipologiaAttributo){
		for(SiacTAttrEnum e : SiacTAttrEnum.values()){
			if(tipologiaAttributo.equals(e.getTipologiaAttributo())){
				return e;
			}
		}
		
		throw new IllegalArgumentException("Il tipo attributo "+ tipologiaAttributo + " non ha un mapping corrispondente in SiacTAttrEnum");
	}
	
	/**
	 * By tipologia attributo even null.
	 *
	 * @param tipologiaAttributo the tipologia attributo
	 * @return the siac t attr enum
	 */
	public static SiacTAttrEnum byTipologiaAttributoEvenNull(TipologiaAttributo tipologiaAttributo){
		if(tipologiaAttributo==null){
			return null;
		}
		return byTipologiaAttributo(tipologiaAttributo);
		
	}
	
	/**
	 * By capitolo field name.
	 *
	 * @param fieldName the field name
	 * @return the siac t attr enum
	 */
	public static SiacTAttrEnum byCapitoloFieldName(String fieldName){
		for(SiacTAttrEnum e : SiacTAttrEnum.values()){
			if(e.getModelFieldName().equals(fieldName)){
				return e;
			}
		}
		throw new IllegalArgumentException("Il field name "+ fieldName + " non ha un mapping corrispondente in SiacTAttrEnum");
	}

//	public SiacTAttr getEntity(){
//		SiacTAttr result = new SiacTAttr();
//		result.setAttrId(getId());
//		result.setAttrCode(getCodice());
//		return result;
//	}

	/**
	 * Gets the field type.
	 *
	 * @return the field type
	 */
	public Class<?> getFieldType() {
		return fieldType;
	}

	/**
	 * Ottiene una mappa con chiave il nome del field rimapabile nella SiacTAttr 
	 * e valore il valore della poperty nell'oggetto passato come parametro.
	 *
	 * @param obj the obj
	 * @return the field attr name value map by type
	 */
	public static Map<String, Object> getFieldAttrNameValueMapByType(Object obj) {
		final String methodName ="getFieldAttrNameValueMapByType";
		Map<String, Object> result = new HashMap<String, Object>();
		 
		BeanWrapper wObj = PropertyAccessorFactory.forBeanPropertyAccess(obj);
		for (SiacTAttrEnum siacTAttr : SiacTAttrEnum.values()){
			String fieldName = siacTAttr.getModelFieldName();
			if(fieldName!=null && wObj.isReadableProperty(fieldName) && wObj.isWritableProperty(fieldName)){
				try{
					Object fieldValue = wObj.getPropertyValue(fieldName);
					result.put(fieldName,fieldValue);
				} catch(NotReadablePropertyException nrpe){
					log.warn(methodName, "No mappable fieldName: "+fieldName + " ["+ nrpe.getMessage()+"]");
				}
			}
		}
		
		
		log.debug(methodName, "returning: "+ result);
		return result;
		
	}
	
	/**
	 * Ottiene una mappa con chiave l'enum TipologiaAttributo rappresentante il tipo di attributo  
	 * e valore il valore della poperty nell'oggetto passato come parametro.
	 *
	 * @param obj the obj
	 * @return the tipologia attributo value map by type
	 */
	public static Map<TipologiaAttributo, Object> getTipologiaAttributoValueMapByType(Object obj) {
		return getTipologiaAttributoValueMapByType(obj, false);
	}
	
	
	/**
	 * Ottiene una mappa con chiave l'enum TipologiaAttributo rappresentante il tipo di attributo  
	 * e valore il valore della poperty nell'oggetto passato come parametro.
	 *
	 * @param obj the obj
	 * @param booleanTypeNullValueIsEqualsToFalse se true indica che i tipi booleani impostati a null verranno restituiti con valore false (anzichè null)
	 * @return the tipologia attributo value map by type
	 */
	public static Map<TipologiaAttributo, Object> getTipologiaAttributoValueMapByType(Object obj, boolean booleanTypeNullValueIsEqualsToFalse) {
		final String methodName ="getTipologiaAttributoValueMapByType";
		Map<TipologiaAttributo, Object> result = new HashMap<TipologiaAttributo, Object>();
		 
		BeanWrapper wObj = PropertyAccessorFactory.forBeanPropertyAccess(obj);
		for (SiacTAttrEnum siacTAttr : SiacTAttrEnum.values()){
			String fieldName = siacTAttr.getModelFieldName();
			if(fieldName!=null){
				try{
					Object fieldValue = wObj.getPropertyValue(fieldName);
					
					if(booleanTypeNullValueIsEqualsToFalse){
						fieldValue = setBooleanTypeNullValueToFalse(wObj, fieldName, fieldValue);
					}
					
					result.put(siacTAttr.getTipologiaAttributo(),fieldValue);
				} catch(NotReadablePropertyException nrpe){
					log.warn("getTipologiaAttributoValueMapByType", "No mappable fieldName: "+fieldName+ " ["+ nrpe.getMessage()+"]");					
				}
			}
		}
		
		
		log.debug(methodName, "returning: "+ result);
		return result;
		
	}

	/**
	 * Sets the boolean type null value to false.
	 *
	 * @param wObj the w obj
	 * @param fieldName the field name
	 * @param fieldValue the field value
	 * @return the object
	 */
	private static Object setBooleanTypeNullValueToFalse(BeanWrapper wObj, String fieldName, Object fieldValue) {
		Class<?> fieldType = wObj.getPropertyType(fieldName);				
		if(Boolean.class.equals(fieldType) && fieldValue == null){
			fieldValue = Boolean.FALSE;
		}
		return fieldValue;
	}


	/**
	 * Gets the siac T attr handler type.
	 *
	 * @return the siac T attr handler type
	 */
	public Class<? extends SiacTAttrHandler> getSiacTAttrHandlerType(){
		return siacTAttrHandlerType;
	}


}
