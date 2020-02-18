/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti.jpql;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccespser.model.AnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.model.PrimaNota;

public enum EntitaCollegatePrimaNotaInventarioJpqlEnum {
	Cespite(Cespite.class, "SiacRCespitiPrimaNota", "siacTCespiti", "cesId", "", "siacTPrimaNota"),
	VariazioneCespite(VariazioneCespite.class, "SiacRCespitiVariazionePrimaNota", "siacTCespitiVariazione", "cesVarId", "", "siacTPrimaNota"),
	DismissioneCespite(DismissioneCespite.class, "SiacRCespitiDismissioniPrimaNota", "siacTCespitiDismissioni", "cesDismissioniId", "siacTCespitiAmmortamentoDett.siacTCespitiAmmortamento.siacTCespiti", "siacTPrimaNota"),
	DettaglioAmmortamentoAnnuoCespite(DettaglioAmmortamentoAnnuoCespite.class, "SiacTCespitiAmmortamentoDett", "", "cesAmmDettId","", "siacTPrimaNota"),
	AnteprimaAmmortamentoAnnuoCespite(AnteprimaAmmortamentoAnnuoCespite.class, "SiacRCespitiCespitiElabAmmortamentiDett", "siacTCespitiElabAmmortamentiDettDare.siacTCespitiElabAmmortamenti", "elabId","", "siacTPrimaNota"),
	PrimaNota(PrimaNota.class, "SiacRCespitiMovEpDet", "siacTMovEpDet.siacTMovEp.siacTPrimaNota", "pnotaId", "siacTCespiti.cesId", "siacTPrimaNotaAlienazione"),
	;

	private Class<?> entitaGenerantePrimaNotaInventarioClazz;
	private String nomeTabellaCollegamentoEntitaGeneranteEPrimaNota;
	private String percorsoDaTabellaCollegamentoAEntitaGenerante;
	private String aliasCampoUidEntita;
	private String percorsoDiCollegamentoConSiacTCespiti;
	private String nomeCampoPrimaNotaSuTabellaCollegamentoPrimaNotaEntitaGenerante;	

	
	/**
	 * Instantiates a new entita collegate prima nota inventario jpql enum.
	 *
	 * @param clazz the clazz
	 * @param aliasRPrimeNote the alias R prime note
	 * @param aliasCampoUidEntita the alias campo uid entita
	 * @param aliasRUlteriori the alias R ulteriori
	 * @param aliasCondizioniUlteriori the alias condizioni ulteriori
	 */
	private EntitaCollegatePrimaNotaInventarioJpqlEnum(Class<?> entitaGenerantePrimaNotaInventarioClazz, String nomeTabellaCollegamentoEntitaGeneranteEPrimaNota, 
			String percorsoDaTabellaCollegamentoAEntitaGenerante, String aliasCampoUidEntita, 
			String percorsoDiCollegamentoConSiacTCespiti, String nomeCampoPrimaNotaSuTabellaCollegamentoPrimaNotaEntitaGenerante) {
		this.entitaGenerantePrimaNotaInventarioClazz = entitaGenerantePrimaNotaInventarioClazz;     
		this.nomeTabellaCollegamentoEntitaGeneranteEPrimaNota = nomeTabellaCollegamentoEntitaGeneranteEPrimaNota;
		this.percorsoDaTabellaCollegamentoAEntitaGenerante = percorsoDaTabellaCollegamentoAEntitaGenerante;
		this.aliasCampoUidEntita = aliasCampoUidEntita;
		this.percorsoDiCollegamentoConSiacTCespiti = percorsoDiCollegamentoConSiacTCespiti;
		this.nomeCampoPrimaNotaSuTabellaCollegamentoPrimaNotaEntitaGenerante = nomeCampoPrimaNotaSuTabellaCollegamentoPrimaNotaEntitaGenerante;
		
	}
	
	/**
	 * Ottiene la classe dell'oggetto di modello da cui e' partita la creazione della prima nota di inventario.
	 * Le entita generanti sono:
	 *  <ul>
	 *  	<li> <strong> CESPITE</strong> per le prime note inventario generate in seguito ad operazioni su alle donazioni </li>
	 *  	<li> <strong> VARIAZIONECESPITE </strong> per le prime note inventario generate in seguito ad operazioni su alle variazioni cespite</li>
	 *  	<li> <strong> DISMISSIONECESPITE </strong>  per le prime note inventario generate in seguito ad operazioni su alle dismissioni cespite </li>
	 *  	<li> <strong> DETTAGLIOAMMORTAMENTOANNUOCESPITE </strong> per le prime note inventario generate in seguito ad operazioni sull ammortamento per un certo anno </li>
	 *  	<li> <strong> ANTEPRIMAAMMORTAMENTOANNUOCESPITE</strong> per le prime note inventario generate in seguito ad operazioni sull'anteprima di un ammortamento annuo</li>
	 *  	<li><strong>  PRIMANOTA</strong> per le prime note inventario generate in seguito ad operazioni su sulle prime note di contabilita' generale integrate su registro A</li>
	 *  </ul>
	 *
	 * @return the entita generante prima nota inventario clazz
	 */
	public Class<?> getEntitaGenerantePrimaNotaInventarioClazz() {
		return this.entitaGenerantePrimaNotaInventarioClazz;
	}
	
	/**
	 *  Ottiene l'enum a partire dall'oggetto di modello da cui e' partita la creazione della prima nota di inventario.
	 *
	 * @param entitaClazz l'oggetto di modello da cui e' partita la creazione della prima nota di inventario
	 * @return the pql enum
	 */
	public static EntitaCollegatePrimaNotaInventarioJpqlEnum byEntitaCollegataClass(Class<?> entitaClazz) {
		for (EntitaCollegatePrimaNotaInventarioJpqlEnum jpqlEnum : EntitaCollegatePrimaNotaInventarioJpqlEnum.values()) {
			if(jpqlEnum.getEntitaGenerantePrimaNotaInventarioClazz().equals(entitaClazz)) {
				return jpqlEnum;
			}
		}
		throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Non e' gestito il collegamento con la prima nota inventario per la classe " 
				+ (entitaClazz != null? entitaClazz.getSimpleName() : "null")));
	}

	/**
	 * Ottiene il nome della tabella di collegamento tra la entity che ha creato la prima nota (esempio: SIacTCespiti.java, SiacTCespitiDismissioni.java...) e la entity SiacTPrimaNota.java
	 *
	 * @return il nomeTabellaCollegamentoEntitaGeneranteEPrimaNota
	 */
	public String getNomeTabellaCollegamentoEntitaGeneranteEPrimaNota() {
		return nomeTabellaCollegamentoEntitaGeneranteEPrimaNota;
	}

	/**
	 * @return the aliasCampoUidEntita
	 */
	public String getPatternUidEntita() {
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isNotBlank(getPercorsoDaTabellaCollegamentoAEntitaGenerante())) {
			sb.append(getPercorsoDaTabellaCollegamentoAEntitaGenerante()).append(".");
		}
		return sb.append(aliasCampoUidEntita).toString();
	}
	
	/**
	 * Ottiene il collegamento tra le entity generante la prima nota ed il cepsite ad essa collegato
	 *
	 * @return il collegamento con la tabella SIacTCespiti.java
	 */
	public String getCollegamentoSiacTCespiti() {
		if(StringUtils.isBlank(this.percorsoDiCollegamentoConSiacTCespiti)) {
			throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("impossibile operare un filtro sul cespite, non esiste un campo corrispondente su EntitaCollegatePrimaNotaInventarioJpqlEnum."));
		}
		return this.percorsoDiCollegamentoConSiacTCespiti;
	}

	/**
	 * Ottiene la sequenza di entity necessarie ad arrivare dalla entity di collegamento entit&agrave; - prima nota inventario all'entit&agrave; generante stessa.
	 *
	 *@return the pathCollegamentoSiacRSiacT
	 */
	public String getPercorsoDaTabellaCollegamentoAEntitaGenerante() {
		return this.percorsoDaTabellaCollegamentoAEntitaGenerante;
	}
	
	public String getNomeCampoPrimaNotaSuTabellaCollegamentoPrimaNotaEntitaGenerante() {
		return this.nomeCampoPrimaNotaSuTabellaCollegamentoPrimaNotaEntitaGenerante;
	}

	
}
