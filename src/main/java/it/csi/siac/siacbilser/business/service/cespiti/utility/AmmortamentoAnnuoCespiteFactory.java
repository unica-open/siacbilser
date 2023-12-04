/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti.utility;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * A factory for creating AmmortamentoAnnuoCespite objects.
 */
public final class AmmortamentoAnnuoCespiteFactory {
	
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
	/**
	 * Instantiates a new ammortamento annuo cespite factory.
	 */
	//DATI DI INPUT
	private Integer annoFineAmmortamento;
	private Date dataAmmortamento;
	private int uidCespite;
	private Date dataIngressoInventario;
	private Date dataVariazione;
	
	//DATI RICAVATI A PARTIRE DAI DATI DI INPUT
	private BigDecimal quotaAnnua = BigDecimal.ZERO;
	private BigDecimal importoDaAmmortare = BigDecimal.ZERO;
	private Integer annoInizioAmmortamento;
	private BigDecimal importoAmmortato = BigDecimal.ZERO;
	private TipoCalcoloQuotaPrimoAnnoEnum tipoCalcoloEnum;
	private Integer ultimoAnnoAmmortamentoPrecedente;
	//RISULTATO
	private AmmortamentoAnnuoCespite testataAmmortamentoElaborato = new AmmortamentoAnnuoCespite();
	private List<DettaglioAmmortamentoAnnuoCespite> dettagliAmmortamentoDaInserire = new ArrayList<DettaglioAmmortamentoAnnuoCespite>();
	
	
	/**
	 * Inizializza l'istanza della factroy. Se si sta facendo l'ammortamento annuo del CESPITE 001, i parametri necessari saranno:
	 *
	 * @param codiceTipoCalcolo  il codice della CategoriaCalcoloTipoCespite (pu&ograve; essere quota intera, 365-esimi, 12-esimi, 50) legata alla categoria cespite del tipo bene del cespite
	 * @param aliquotaAnnua l'aliquota annua della categoria legata al tipo bene del cespite
	 * @param uidCespite the uid cespite
	 * @param dataIngressoInventario la data di ingresso in inventario del cespite
	 * @param valoreAttualeBene the valore attuale bene il valore attuale del cespite
	 * @param annoFineAmmortamento l'anno fino al quale devo calcolare l'ammortamento
	 * @param dataAmmortamento the data ammortamento
	 */
	public AmmortamentoAnnuoCespiteFactory(String codiceTipoCalcolo, BigDecimal aliquotaAnnua, int uidCespite, Date dataIngressoInventario,  BigDecimal valoreAttualeBene, Integer annoFineAmmortamento, Date dataAmmortamento) {
		
		if(!isCampiInputCorretti(codiceTipoCalcolo, aliquotaAnnua, uidCespite, dataIngressoInventario, valoreAttualeBene, dataAmmortamento)) {
			//non ho i dati per calcolare l'ammortamento
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile elaborare l'ammortamento, non tutti i dati sono stati forniti."));
		}
		
		initCampi(codiceTipoCalcolo, uidCespite,dataIngressoInventario, valoreAttualeBene, aliquotaAnnua, annoFineAmmortamento, dataAmmortamento, null);
	}

	/**
	 * Inits the.
	 *
	 * @param cespite the cespite
	 * @param categoriaCespiti the categoria cespiti
	 * @param annoFineAmmortamento the anno fine ammortamento
	 * @param dataAmmortamento the data ammortamento
	 * @param dataVariazione the anno variazione
	 */
	public AmmortamentoAnnuoCespiteFactory(Cespite cespite, CategoriaCespiti categoriaCespiti, Integer annoFineAmmortamento, Date dataAmmortamento, Date dataVariazione) {
		if(categoriaCespiti == null || categoriaCespiti.getCategoriaCalcoloTipoCespite() == null || cespite == null || !isCampiInputCorretti(categoriaCespiti.getCategoriaCalcoloTipoCespite().getCodice(), categoriaCespiti.getAliquotaAnnua(), cespite.getUid(), cespite.getDataAccessoInventario(), cespite.getValoreAttuale(), dataAmmortamento)) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile elaborare l'ammortamento, non tutti i dati sono stati forniti."));
		}
		initCampi(categoriaCespiti.getCategoriaCalcoloTipoCespite().getCodice(), cespite.getUid(), cespite.getDataAccessoInventario(),cespite.getValoreAttuale(), categoriaCespiti.getAliquotaAnnua(), annoFineAmmortamento, dataAmmortamento, dataVariazione);
	}
	
	/**
	 * Inits the.
	 *
	 * @param cespite the cespite
	 * @param categoriaCespiti the categoria cespiti
	 * @param annoFineAmmortamento the anno fine ammortamento
	 * @param dataAmmortamento the data ammortamento
	 */
	public AmmortamentoAnnuoCespiteFactory(Cespite cespite, CategoriaCespiti categoriaCespiti, Integer annoFineAmmortamento, Date dataAmmortamento) {
		
		if(categoriaCespiti == null || categoriaCespiti.getCategoriaCalcoloTipoCespite() == null || cespite == null || !isCampiInputCorretti(categoriaCespiti.getCategoriaCalcoloTipoCespite().getCodice(), categoriaCespiti.getAliquotaAnnua(), cespite.getUid(), cespite.getDataAccessoInventario(), cespite.getValoreAttuale(), dataAmmortamento)) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile elaborare l'ammortamento, non tutti i dati sono stati forniti."));

		}
		initCampi(categoriaCespiti.getCategoriaCalcoloTipoCespite().getCodice(),cespite.getUid(), cespite.getDataAccessoInventario(),cespite.getValoreAttuale(), categoriaCespiti.getAliquotaAnnua(), annoFineAmmortamento, dataAmmortamento, null);
	}

	/**
	 * @param codiceTipoCalcolo
	 * @param dataIngressoInventario
	 * @param valoreAttualeBene
	 * @param aliquotaAnnua
	 * @param dataAmmortamento
	 * @return
	 */
	private boolean isCampiInputCorretti(String codiceTipoCalcolo, BigDecimal aliquotaAnnua,int uidCespite, Date dataIngressoInventario, BigDecimal valoreAttualeBene,  Date dataAmmortamento) {
		return StringUtils.isNotBlank(codiceTipoCalcolo) && dataIngressoInventario != null && uidCespite != 0 && valoreAttualeBene != null && aliquotaAnnua != null && dataAmmortamento != null;
	}
	

	/**
	 * @param codiceTipoCalcolo
	 * @param dataIngressoInventario
	 * @param valoreAttualeBene
	 * @param aliquotaAnnua
	 * @param annoFineAmmortamento
	 * @param dataAmmortamento
	 * @param dataVariazione
	 */
	private void initCampi(String codiceTipoCalcolo, int uidCespite, Date dataIngressoInventario, BigDecimal valoreAttualeBene, BigDecimal aliquotaAnnua, Integer annoFineAmmortamento, Date dataAmmortamento, Date dataVariazione) {
		
		//inizializzazione dei campi di default
		this.annoFineAmmortamento = annoFineAmmortamento;
		this.dataAmmortamento = dataAmmortamento;
		this.importoDaAmmortare = valoreAttualeBene; 
		this.dataVariazione = dataVariazione;
		this.annoInizioAmmortamento = Integer.valueOf(Utility.getAnno(dataIngressoInventario));
		this.dataIngressoInventario = dataIngressoInventario;
		this.uidCespite = uidCespite;
		
		//inizializzazione dei campi derivati
		this.quotaAnnua = valoreAttualeBene.multiply(aliquotaAnnua).divide(BilUtilities.BIG_DECIMAL_ONE_HUNDRED,MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_DOWN);
		this.tipoCalcoloEnum = TipoCalcoloQuotaPrimoAnnoEnum.byCodiceTipoCalcolo(codiceTipoCalcolo);
	}
	

	/**
	 * 
	 */
	private void impostaDatiDaAmmortamentoPrecedente(AmmortamentoAnnuoCespite ammortamentoCespite) {
		final String methodName = "elaboraAmmortamentoEsistente";
		if(ammortamentoCespite == null ||  ammortamentoCespite.getUid() == 0 || (ammortamentoCespite.getUltimoAnnoAmmortato() == null && ammortamentoCespite.getImportoTotaleAmmortato() == null)) {
			log.debug(methodName, "Nessun ammortamento precedente legato al cespite."); 
			return;
		}
		this.ultimoAnnoAmmortamentoPrecedente = ammortamentoCespite.getUltimoAnnoAmmortato();
		if(ammortamentoCespite.getImportoTotaleAmmortato() != null && !BigDecimal.ZERO.equals(ammortamentoCespite.getImportoTotaleAmmortato())) {
			this.importoAmmortato =  ammortamentoCespite.getImportoTotaleAmmortato();
		}
		if(this.ultimoAnnoAmmortamentoPrecedente != null && this.ultimoAnnoAmmortamentoPrecedente != 0) {
			this.annoInizioAmmortamento = this.dataVariazione != null ? 
					Utility.getAnno(this.dataVariazione) : 
						this.ultimoAnnoAmmortamentoPrecedente.intValue() +1; 
		}
		
		//LASCIO QUI LA VERSIONE CHE CICLA, SE SI VEDESSE CHE CE N'E' BISOGNO.
//		if(ammortamentoCespite == null || ammortamentoCespite.getUid() == 0 || ammortamentoCespite.getUltimoAnnoAmmortato() == null) {
//			return;	
//		}
//		List<DettaglioAmmortamentoAnnuoCespite> dettagliAmmortamentoAnnuoCespite = ammortamentoCespite.getDettagliAmmortamentoAnnuoCespite();
//		if(dettagliAmmortamentoAnnuoCespite == null || dettagliAmmortamentoAnnuoCespite.isEmpty() ) {
//			log.warn(methodName, "E' presente una testata ammortamento senza che siano presenti dettagli ammortamento. Si prega di controllare su db il record [ uid: "+ ammortamentoCespite.getUid() + " ].");;
//			return;
//		}
//		Integer ultimoAnnoRegistrato = null;
//		BigDecimal importoTotaleConPrimaNota = BigDecimal.ZERO;
		
//		for (DettaglioAmmortamentoAnnuoCespite dett : ammortamentoCespite.getDettagliAmmortamentoAnnuoCespite()) {
//			if(dett.getPrimaNota() == null || dett.getPrimaNota().getUid() == 0) {
//				//non ho la prima nota. vado avanti
//				continue;
//			}
//			importoTotaleConPrimaNota = importoTotaleConPrimaNota.add(dett.getQuotaAnnuale());
//			if(dett.getAnno()!= null && (ultimoAnnoRegistrato == null ||  dett.getAnno().compareTo(ultimoAnnoRegistrato)>0)){
//				ultimoAnnoRegistrato = dett.getAnno();
//			}
//		}
//		this.importoAmmortato = BigDecimal.ZERO.equals(importoTotaleConPrimaNota)? ammortamentoCespite.getImportoTotaleAmmortato() : importoTotaleConPrimaNota;
//		if(ultimoAnnoRegistrato != null && ultimoAnnoRegistrato.intValue() != 0) {
//			this.annoInizioAmmortamento = ultimoAnnoRegistrato.equals(this.ultimoAnnoAmmortamentoPrecedente) && this.dataVariazione != null?
//					Utility.getAnno(this.dataVariazione) : (ultimoAnnoRegistrato.intValue() + 1);
//		}
		
		
		
	}
	
	

	/**
	 * Crea ammortamenti by cespite.
	 *
	 * @param ammortamentoAnnuoPrecedente the ammortamento annuo precedente
	 * @return the list
	 */
	public void elaboraAmmortamento(AmmortamentoAnnuoCespite ammortamentoAnnuoPrecedente){
		
		checkCoerenzaUidCespite(ammortamentoAnnuoPrecedente);
		impostaDatiDaAmmortamentoPrecedente(ammortamentoAnnuoPrecedente);
		
				
				
		Integer ultimoAnnoAmmortato = null; //testataAmmortamentoElaborato.getUltimoAnnoAmmortato();
		
		BigDecimal quotaAnnuale = null;
		int annoCiclo = annoInizioAmmortamento.intValue();
		
		while(this.annoFineAmmortamento == null || annoCiclo<=this.annoFineAmmortamento.intValue()) {
			DettaglioAmmortamentoAnnuoCespite dettaglioAmmortamento = new DettaglioAmmortamentoAnnuoCespite();
			dettaglioAmmortamento.setAnno(annoCiclo);
			
			
			quotaAnnuale = calcolaQuotaAnnuale(this.importoAmmortato, annoCiclo);
			if(BigDecimal.ZERO.equals(quotaAnnuale)) {
				break;
			}
			
			dettaglioAmmortamento.setQuotaAnnuale(quotaAnnuale);			
			dettaglioAmmortamento.setDataAmmortamento(dataAmmortamento);
			
			
			this.dettagliAmmortamentoDaInserire.add(dettaglioAmmortamento);
			
			this.importoAmmortato = this.importoAmmortato.add(quotaAnnuale);
			ultimoAnnoAmmortato = Integer.valueOf(annoCiclo);
			annoCiclo++;
		}
		
		//SIAC-6447
		if(dettagliAmmortamentoDaInserire == null || this.dettagliAmmortamentoDaInserire.isEmpty()) {
			throw new BusinessException(ErroreCore.ENTITA_NON_COMPLETA.getErrore("ammortamento cespite", " non sono stati creati nuovi dettagli ammortamento."));
		}
		
		
//		//modificata quando l'importo ammortato sulla testata è stato l'importo nuovo
//		testataAmmortamentoElaborato.setImportoTotaleAmmortato(this.importoAmmortato);
//		boolean variazioneSuPianoAmmortamentoCompleto = this.dataVariazione != null && this.ultimoAnnoAmmortamentoPrecedente != null && this.ultimoAnnoAmmortamentoPrecedente.compareTo(ultimoAnnoAmmortato)>0;
//		testataAmmortamentoElaborato.setUltimoAnnoAmmortato(variazioneSuPianoAmmortamentoCompleto ?  : ultimoAnnoAmmortato);
		
		
	}

	private void checkCoerenzaUidCespite(AmmortamentoAnnuoCespite ammortamentoAnnuoPrecedente) {
		if(ammortamentoAnnuoPrecedente != null && ammortamentoAnnuoPrecedente.getCespite() != null && ammortamentoAnnuoPrecedente.getCespite().getUid() != 0 && ammortamentoAnnuoPrecedente.getCespite().getUid() != this.uidCespite) {
			throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("impossibile modificare il cespite legato all'ammortamento: " + testataAmmortamentoElaborato.getUid()));
		}
	}

	/**
	 * Gets the testata ammortamento.
	 *
	 * @return the testata ammortamento
	 */
	public AmmortamentoAnnuoCespite getTestataAmmortamento() {
		testataAmmortamentoElaborato.setImportoTotaleAmmortato(BigDecimal.ZERO);
		Cespite cespite = new Cespite();
		cespite.setUid(uidCespite);
		testataAmmortamentoElaborato.setCespite(cespite);
		return this.testataAmmortamentoElaborato;
	}
	
	/**
	 * Gets the dettatgli ammortamento elaborati.
	 *
	 * @return the dettatgli ammortamento elaborati
	 */
	public List<DettaglioAmmortamentoAnnuoCespite> getDettagliAmmortamentoElaborati(){
		return this.dettagliAmmortamentoDaInserire;
	}
	

	/**
	 * Calcola  la quota di ammortamento.
	 *
	 * @param sum la somma parziale delle quota gia' ammortate
	 * @param anno the anno
	 * @return la quota calcolata
	 */
	private BigDecimal calcolaQuotaAnnuale(BigDecimal sum, int anno) {
		//controllo se io stia o meno sforando il valore del bene
		if(sum.add(quotaAnnua).compareTo(this.importoDaAmmortare)<0) {
			
			return isPrimaQuotaDaProporzionare(anno)?
					//la quota annua va proporzionata in base al tipo calcolo della categoria collegata
					quotaAnnua.multiply(tipoCalcoloEnum.getFattoreProporzionamentoDaDataAFineAnno(dataIngressoInventario)).setScale(2, RoundingMode.HALF_DOWN)
					//la quota annua non deve essere calibrata
					: this.quotaAnnua;
		}
		//se facessi il calcolo precedente, la somma degli importi dell'ammortamento supererebbe il valore attuale del bene: prendo la differenza tra quanto precedentemente ammortato e 
		BigDecimal ultimaQuota = this.importoDaAmmortare.subtract(sum, MathContext.DECIMAL128);
		
		//se la differenza e' negativa, ho superato il bene, ritorno zero
		return BigDecimal.ZERO.compareTo(ultimaQuota) < 0? ultimaQuota : BigDecimal.ZERO;	 
	}
	
	/**
	 * Decide se sia o meno necessario proporzionare la quota annua per l'anno passato in input.
	 *
	 * @param anno the anno a cui afferisce la quota
	 * 
	 * @return true, if successful
	 */
	private boolean isPrimaQuotaDaProporzionare(int anno) {
		return anno == Utility.getAnno(this.dataIngressoInventario);
	}
		

}
