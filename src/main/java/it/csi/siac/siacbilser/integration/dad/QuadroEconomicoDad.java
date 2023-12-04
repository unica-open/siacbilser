/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.QuadroEconomicoDao;
import it.csi.siac.siacbilser.integration.dao.SiacTQuadroEconomicoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDQuadroEconomicoParte;
import it.csi.siac.siacbilser.integration.entity.SiacDQuadroEconomicoStato;
import it.csi.siac.siacbilser.integration.entity.SiacRQuadroEconomicoStato;
import it.csi.siac.siacbilser.integration.entity.SiacTQuadroEconomico;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDQuadroEconomicoParteEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDQuadroEconomicoStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.ParteQuadroEconomico;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siacbilser.model.QuadroEconomicoModelDetail;
import it.csi.siac.siacbilser.model.StatoOperativoQuadroEconomico;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
/**
 * Data access delegate di un QuadroEconomico.
 * 
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class QuadroEconomicoDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private QuadroEconomicoDao quadroEconomicoDao;
	@Autowired 
	private SiacTQuadroEconomicoRepository siacTQuadroEconomicoRepository;
	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Inserisci conto.
	 *
	 * @param quadroEconomico the conto
	 */
	public void inserisciQuadroEconomico(QuadroEconomico quadroEconomico) {
		SiacTQuadroEconomico siacTQuadroEconomico = buildSiacTQuadroEconomico(quadroEconomico);
		siacTQuadroEconomico.setLoginCreazione(loginOperazione);
		quadroEconomicoDao.create(siacTQuadroEconomico);
		quadroEconomico.setUid(siacTQuadroEconomico.getUid());
	}
	

	/**
	 * Aggiorna conto.
	 *
	 * @param conto the conto
	 */
	public void aggiornaQuadroEconomico(QuadroEconomico conto) {
		SiacTQuadroEconomico siacTQuadroEconomico = buildSiacTQuadroEconomico(conto);
		siacTQuadroEconomico.setLoginModifica(loginOperazione);
		quadroEconomicoDao.update(siacTQuadroEconomico);
		conto.setUid(siacTQuadroEconomico.getUid());
	}
	
	/**
	 * Find conto by id.
	 *
	 * @param quadro economico the conto
	 * @return the conto
	 */
	public List<QuadroEconomico> findQuadroEconomicoValidoByCodice(QuadroEconomico quadroEconomico) {
		List<SiacTQuadroEconomico> listasiacTQuadroEconomico = siacTQuadroEconomicoRepository.findSiacTQuadroEconomicoValidoByCode(quadroEconomico.getCodice(), ente.getUid());		
		return convertiLista(listasiacTQuadroEconomico,QuadroEconomico.class,BilMapId.SiacTQuadroEconomico_QuadroEconomico_ModelDetail);

	}
	
	/**
	 * Find conto by id.
	 *
	 * @param quadro economico the conto
	 * @return the conto
	 */
	public List<QuadroEconomico> findQuadroEconomicoValidoByCodiceAndParte(QuadroEconomico quadroEconomico, QuadroEconomicoModelDetail...quadroEconomicoModelDetails ) {
		
		//int uidPadre = quadroEconomico.getQuadroEconomicoPadre().getUid();
		
		List<SiacTQuadroEconomico> listasiacTQuadroEconomico = quadroEconomicoDao.findSiacTQuadroEconomicoValidoByCodeAndParte(
				quadroEconomico.getCodice()
				,quadroEconomico.getParteQuadroEconomico().getCodice()
				,quadroEconomico.getQuadroEconomicoPadre()!= null?quadroEconomico.getQuadroEconomicoPadre().getUid():0
				, ente.getUid()
				);	
		
		return convertiLista(listasiacTQuadroEconomico,QuadroEconomico.class,BilMapId.SiacTQuadroEconomico_QuadroEconomico, quadroEconomicoModelDetails);
	}

	
	/**
	 * Find conto by id.
	 *
	 * @param quadro economico the quadro economico gsa
	 * @return the conto
	 */
	public QuadroEconomico findQuadroEconomicoById(QuadroEconomico quadroEconomico) {
		SiacTQuadroEconomico siacTQuadroEconomico = siacTQuadroEconomicoRepository.findSiacTQuadroEconomicoValidoById(quadroEconomico.getUid(), ente.getUid());
		return mapNotNull(siacTQuadroEconomico,QuadroEconomico.class,BilMapId.SiacTQuadroEconomico_QuadroEconomico_ModelDetail);
	}
	
	/**
	 * Findquadro economicoby id.
	 *
	 * @param quadroEconomico the classificatore GSA
	 * @param quadroEconomicoModelDetails the classificatore GSA model details
	 * @return the classificatore GSA
	 */
	public QuadroEconomico findQuadroEconomicoById(QuadroEconomico quadroEconomico, QuadroEconomicoModelDetail... quadroEconomicoModelDetails) {
		SiacTQuadroEconomico siacTQuadroEconomico = siacTQuadroEconomicoRepository.findSiacTQuadroEconomicoValidoById(quadroEconomico.getUid(), ente.getUid());
		if(quadroEconomicoModelDetails != null) {
			
			return mapNotNull(siacTQuadroEconomico,QuadroEconomico.class,BilMapId.SiacTQuadroEconomico_QuadroEconomico_ModelDetail, Converters.byModelDetails(quadroEconomicoModelDetails));
		}
		
		return mapNotNull(siacTQuadroEconomico,QuadroEconomico.class,BilMapId.SiacTQuadroEconomico_QuadroEconomico_ModelDetail);
	}
	
	/**
	 * Controlla se tutti i figli di un QuadroEconomico sono annullati.
	 *
	 * @param quadroEconomico the classificatore GSA
	 * @return true se tutti i figli del conto passato come parametro non hanno figli.
	 */
	public Boolean isQuadroEconomicoSenzaFigliValidi(QuadroEconomico quadroEconomico) {
		Long figliValidi = siacTQuadroEconomicoRepository.countSiacTQuadroEconomicoFigliValidiByIdPadre(quadroEconomico.getUid());
		return figliValidi.equals(Long.valueOf(0));
	}
	
	/**
	 * Annulla stato Quadro Economico.
	 *
	 * @param quadroEconomico the Quadro Economico
	 */
	public void annullaStatoQuadroEconomico(QuadroEconomico quadroEconomico) {
		SiacTQuadroEconomico siacTQuadroEconomico = siacTQuadroEconomicoRepository.findOne(quadroEconomico.getUid());
		aggiornaStatoQuadroEconomico(siacTQuadroEconomico, StatoOperativoQuadroEconomico.ANNULLATO);
	}
	

	private void aggiornaStatoQuadroEconomico(SiacTQuadroEconomico siacTQuadroEconomico,StatoOperativoQuadroEconomico statoOperativoQuadroEconomico) {
		Date dataCancellazione = new Date();
		if(siacTQuadroEconomico.getSiacRQuadroEconomicoStatos()==null){
			siacTQuadroEconomico.setSiacRQuadroEconomicoStatos(new ArrayList<SiacRQuadroEconomicoStato>());
		}
		for(SiacRQuadroEconomicoStato r : siacTQuadroEconomico.getSiacRQuadroEconomicoStatos()){
			r.setDataCancellazioneIfNotSet(dataCancellazione);					
		}
		Date now = new Date();
		SiacRQuadroEconomicoStato siacRQuadroEconomicoStato = new SiacRQuadroEconomicoStato();
		SiacDQuadroEconomicoStato siacDQuadroEconomicoStato = eef.getEntity(SiacDQuadroEconomicoStatoEnum.byStatoOperativo(statoOperativoQuadroEconomico), siacTQuadroEconomico.getSiacTEnteProprietario().getUid());
		siacRQuadroEconomicoStato.setSiacDQuadroEconomicoStato(siacDQuadroEconomicoStato);		
		siacRQuadroEconomicoStato.setSiacTQuadroEconomico(siacTQuadroEconomico);			
		siacRQuadroEconomicoStato.setSiacTEnteProprietario(siacTQuadroEconomico.getSiacTEnteProprietario());
		siacRQuadroEconomicoStato.setDataModificaInserimento(now);
		siacRQuadroEconomicoStato.setLoginOperazione(loginOperazione);		
		
		siacTQuadroEconomico.addSiacRQuadroEconomicoStato(siacRQuadroEconomicoStato);
		
	}

	public void aggiornaParteQuadroEconomico(SiacTQuadroEconomico siacTQuadroEconomico,ParteQuadroEconomico parteQuadroEconomico) {
		if(siacTQuadroEconomico.getSiacDQuadroEconomicoParte()==null){
			siacTQuadroEconomico.setSiacDQuadroEconomicoParte(new SiacDQuadroEconomicoParte());
		}
		Date now = new Date();
		SiacDQuadroEconomicoParte siacDQuadroEconomicoParte = eef.getEntity(SiacDQuadroEconomicoParteEnum.byParte(parteQuadroEconomico), siacTQuadroEconomico.getSiacTEnteProprietario().getUid());
			
		siacTQuadroEconomico.setSiacTEnteProprietario(siacTQuadroEconomico.getSiacTEnteProprietario());
		siacTQuadroEconomico.setDataModificaInserimento(now);
		siacTQuadroEconomico.setLoginOperazione(loginOperazione);		
		siacTQuadroEconomico.setSiacDQuadroEconomicoParte(siacDQuadroEconomicoParte);	
		
	}

	/**
	 * Builds the siac t pdce conto.
	 *
	 * @param quadroEconomico the conto
	 * @return the siac t pdce conto
	 */
	private SiacTQuadroEconomico buildSiacTQuadroEconomico(QuadroEconomico quadroEconomico) {
		SiacTQuadroEconomico siacTQuadroEconomico = new SiacTQuadroEconomico();
		quadroEconomico.setLoginOperazione(loginOperazione);
		siacTQuadroEconomico.setLoginOperazione(loginOperazione);
		quadroEconomico.setEnte(ente);
		map(quadroEconomico,siacTQuadroEconomico, BilMapId.SiacTQuadroEconomico_QuadroEconomico);
		return siacTQuadroEconomico;
	}
	
	
	
	/**
	 * Ricerca sintetica conto.
	 *
	 * @param qe the conto
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<QuadroEconomico> ricercaSinteticaQuadroEconomico(QuadroEconomico qe, ParametriPaginazione parametriPaginazione) {
		final String methodName = "ricercaSinteticaQuadroEconomico";
		log.info(methodName, "BEGIN");
		Page<SiacTQuadroEconomico> lista = quadroEconomicoDao.ricercaSinteticaQuadroEconomico(
				ente.getUid(),
				StringUtils.trimToNull(qe.getCodice()),
				qe.getDescrizione(),
				qe.getStatoOperativoQuadroEconomico() != null? SiacDQuadroEconomicoStatoEnum.byStatoOperativo(qe.getStatoOperativoQuadroEconomico()) : null,
				qe.getParteQuadroEconomico() != null? SiacDQuadroEconomicoParteEnum.byParte(qe.getParteQuadroEconomico()) : null,
						
				toPageable(parametriPaginazione));

		log.info(methodName, "lista size " + lista.getSize());

		return toListaPaginata(lista, QuadroEconomico.class, BilMapId.SiacTQuadroEconomico_QuadroEconomico_All);
		
	}
	
	/**
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public List<QuadroEconomico> ricercaQuadroEconomicoValidi() {
		List<SiacTQuadroEconomico> lista = quadroEconomicoDao.ricercaQuadroEconomico(
				ente.getUid(),
				null,
				null,
				SiacDQuadroEconomicoStatoEnum.VALIDO,
				null
				);
		
		return convertiLista(lista, QuadroEconomico.class, BilMapId.SiacTQuadroEconomico_QuadroEconomico_AllValidi);
	}

}
