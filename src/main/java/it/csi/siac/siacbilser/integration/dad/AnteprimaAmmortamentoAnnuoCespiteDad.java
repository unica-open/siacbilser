/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.cespiti.AnteprimaAmmortamentoAnnuoCespiteDao;
import it.csi.siac.siacbilser.integration.dao.cespiti.SiacRCespitiCespitiElabAmmortamentiDettRepository;
import it.csi.siac.siacbilser.integration.dao.cespiti.SiacTCespitiAmmortamentoDettRepository;
import it.csi.siac.siacbilser.integration.dao.cespiti.SiacTCespitiElabAmmortamentiRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRCespitiCespitiElabAmmortamentiDett;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiAmmortamentoDett;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiElabAmmortamenti;
import it.csi.siac.siacbilser.integration.entity.SiacTCespitiElabAmmortamentiDett;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDOperazioneEpEnum;
import it.csi.siac.siacbilser.integration.entitymapping.CespMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.ModelDetail;
import it.csi.siac.siaccespser.model.AnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di DAD per il Tipo Bene.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class AnteprimaAmmortamentoAnnuoCespiteDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private AnteprimaAmmortamentoAnnuoCespiteDao anteprimaAmmortamentoAnnuoCespiteDao;
	
	@Autowired
	private SiacTCespitiElabAmmortamentiRepository siacTCespitiElabAmmortamentiRepository;
	
	@Autowired
	private SiacRCespitiCespitiElabAmmortamentiDettRepository siacRCespitiCespitiElabAmmortamentiDettRepository;
	
	@Autowired
	private SiacTCespitiAmmortamentoDettRepository siacTCespitiAmmortamentoDettRepository;

	/**
	 * Ricerca sintetica dettaglio anteprima ammortamento annuo cespite.
	 *
	 * @param anteprimaAmmortamentoAnnuoCespite the anteprima ammortamento annuo cespite
	 * @param modelDetails the model details
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<DettaglioAnteprimaAmmortamentoAnnuoCespite> ricercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite(AnteprimaAmmortamentoAnnuoCespite anteprimaAmmortamentoAnnuoCespite,
			DettaglioAnteprimaAmmortamentoAnnuoCespiteModelDetail[] modelDetails,
			ParametriPaginazione parametriPaginazione) {
		Page<SiacTCespitiElabAmmortamentiDett> siacTCespitiElabAmmortamentiDetts = anteprimaAmmortamentoAnnuoCespiteDao.ricercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite(
				ente.getUid(),
				anteprimaAmmortamentoAnnuoCespite != null? anteprimaAmmortamentoAnnuoCespite.getAnno() : null,				
				toPageable(parametriPaginazione)
				);	
		return toListaPaginata(siacTCespitiElabAmmortamentiDetts, DettaglioAnteprimaAmmortamentoAnnuoCespite.class, CespMapId.SiacTCespitiElabAmmortamentiDett_DettaglioAnteprimaAmmortamentoAnnuoCespite_ModelDetail, modelDetails);
	}
	
	/**
	 * Inserisci nuova anteprima ammortamento annuo cespite.
	 *
	 * @param anno the anno
	 * @return the integer
	 */
	public Integer inserisciNuovaAnteprimaAmmortamentoAnnuoCespite(Integer anno) {
		List<Object[]> result = anteprimaAmmortamentoAnnuoCespiteDao.inserisciAnteprimaAmmortamentoAnnuoCespite(ente.getUid(), anno, loginOperazione);
		if(result == null) {
			return null;
		}
		int numeroCespiti = 0 ;
		for (Object[] objects : result) {
			String resultMessage = (String) objects[1];
			
			if(resultMessage == null || StringUtils.startsWith(resultMessage, "KO")) {
				String errorMessage = new StringBuilder().append("Errore durante l'elaborazione dell'anteprima: ").append(resultMessage!= null? resultMessage : "null").toString();
				throw new BusinessException(ErroreCore.ERRORE_DI_SISTEMA.getErrore(errorMessage));
			}
			Integer numeroCespitiParziale = (Integer) objects[0];
			if(numeroCespitiParziale != null) {
				numeroCespiti = numeroCespiti + numeroCespitiParziale.intValue();
			}
		}	
		return Integer.valueOf(numeroCespiti);
	}
	
	/**
	 * Carica lista dettagli anteprima segno.
	 *
	 * @param anteprimaAmmortamentoAnnuoCespite the anteprima ammortamento annuo cespite
	 * @param operazione the operazione
	 * @param modelDetails the model details
	 * @return the list
	 */
	public List<DettaglioAnteprimaAmmortamentoAnnuoCespite> caricaListaDettagliAnteprimaSegno(AnteprimaAmmortamentoAnnuoCespite anteprimaAmmortamentoAnnuoCespite, OperazioneSegnoConto operazione, ModelDetail... modelDetails){
		if(anteprimaAmmortamentoAnnuoCespite == null) {
			return null;
		}
				List<SiacTCespitiElabAmmortamentiDett> siacTCespitiElabAmmortamentiDetts = siacTCespitiElabAmmortamentiRepository.findDettagliByIdAnteprimaAndSegno(anteprimaAmmortamentoAnnuoCespite.getUid(), SiacDOperazioneEpEnum.byOperazione(operazione).getDescrizione(), ente.getUid());
		return convertiLista(siacTCespitiElabAmmortamentiDetts, DettaglioAnteprimaAmmortamentoAnnuoCespite.class, CespMapId.SiacTCespitiElabAmmortamentiDett_DettaglioAnteprimaAmmortamentoAnnuoCespite_ModelDetail, modelDetails);
	}
	
	/**
	 * Find dettaglio ammortamento avere collegato.
	 *
	 * @param dettaglioDare the dettaglio dare
	 * @param modelDetails the model details
	 * @return the dettaglio anteprima ammortamento annuo cespite
	 */
	public DettaglioAnteprimaAmmortamentoAnnuoCespite caricaDettaglioAmmortamentoAvereCollegato(DettaglioAnteprimaAmmortamentoAnnuoCespite dettaglioDare, ModelDetail... modelDetails) {
		SiacTCespitiElabAmmortamentiDett siacTCespitiElabAmmortamentiDett = siacRCespitiCespitiElabAmmortamentiDettRepository.findElabDettCollegatoAvere(dettaglioDare.getUid(), ente.getUid());
		DettaglioAnteprimaAmmortamentoAnnuoCespite a=  mapNotNull(siacTCespitiElabAmmortamentiDett, DettaglioAnteprimaAmmortamentoAnnuoCespite.class, CespMapId.SiacTCespitiElabAmmortamentiDett_DettaglioAnteprimaAmmortamentoAnnuoCespite_ModelDetail, Converters.byModelDetails(modelDetails));
		return a;
	}
	
	/**
	 * Carica anteprima ammprtamento annuo.
	 *
	 * @param anno the anno
	 * @return the anteprima ammortamento annuo cespite
	 */
	public AnteprimaAmmortamentoAnnuoCespite caricaAnteprimaAmmprtamentoAnnuo(Integer anno) {
		SiacTCespitiElabAmmortamenti siacTCespitiElabAmmortamenti = siacTCespitiElabAmmortamentiRepository.findAnteprimaByAnno(anno,ente.getUid());
		return mapNotNull(siacTCespitiElabAmmortamenti, AnteprimaAmmortamentoAnnuoCespite.class, CespMapId.SiacTCespitiElabAmmortamenti_AnteprimaAmmortamentoAnnuoCespite_ModelDetail);
	}

	public void collegaDettagliPrimaNota(DettaglioAnteprimaAmmortamentoAnnuoCespite dettaglioDare, DettaglioAnteprimaAmmortamentoAnnuoCespite dettaglioAvere, PrimaNota pnotaInserita) {
		//TODO: valutare se effettivamente sia ancora necessario scrivere la prima nota sull atabella
		List<SiacRCespitiCespitiElabAmmortamentiDett> siacRCespiti = siacRCespitiCespitiElabAmmortamentiDettRepository.findByDettagli(dettaglioDare.getUid(), dettaglioAvere.getUid(), ente.getUid());
		SiacTPrimaNota siacTPrimaNota = new SiacTPrimaNota();
		siacTPrimaNota.setUid(pnotaInserita.getUid());
		for (SiacRCespitiCespitiElabAmmortamentiDett siacRCespitiCespitiElabAmmortamentiDett : siacRCespiti) {
			siacRCespitiCespitiElabAmmortamentiDett.setSiacTPrimaNota(siacTPrimaNota);
			siacRCespitiCespitiElabAmmortamentiDettRepository.saveAndFlush(siacRCespitiCespitiElabAmmortamentiDett);
			collegaDettaglioAmmortamentoAllaPrimaNota(siacRCespitiCespitiElabAmmortamentiDett.getSiacTCespitiAmmortamentoDett(), pnotaInserita.getUid());
		}
		
	}

	private void collegaDettaglioAmmortamentoAllaPrimaNota(SiacTCespitiAmmortamentoDett siacTCespitiAmmortamentoDett, int uidPrimaNota ) {
		if(siacTCespitiAmmortamentoDett == null || siacTCespitiAmmortamentoDett.getUid() == null || siacTCespitiAmmortamentoDett.getUid().intValue() ==0) {
			return;
		}
		SiacTCespitiAmmortamentoDett found = siacTCespitiAmmortamentoDettRepository.findOne(siacTCespitiAmmortamentoDett.getUid());
		SiacTPrimaNota siacTPrimaNota = new SiacTPrimaNota();
		siacTPrimaNota.setUid(uidPrimaNota);
		found.setSiacTPrimaNota(siacTPrimaNota);
		siacTCespitiAmmortamentoDettRepository.saveAndFlush(found);
	}
	
}
