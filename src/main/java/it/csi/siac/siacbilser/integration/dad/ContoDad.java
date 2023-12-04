/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.ContoDao;
import it.csi.siac.siacbilser.integration.dao.SiacTPdceContoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDPdceFam;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoModelDetail;

/**
 * Data access delegate di un Conto.
 * 
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ContoDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private ContoDao contoDao;
	
	@Autowired
	private SiacTPdceContoRepository siacTPdceContoRepository;
	
	/**
	 * Inserisci conto.
	 *
	 * @param conto the conto
	 */
	public void inserisciConto(Conto conto) {
		SiacTPdceConto siacTPdceConto = buildSiacTPdceConto(conto);
		siacTPdceConto.setLoginCreazione(loginOperazione);
		contoDao.create(siacTPdceConto);
		conto.setUid(siacTPdceConto.getUid());
	}
	

	/**
	 * Aggiorna conto.
	 *
	 * @param conto the conto
	 */
	public void aggiornaConto(Conto conto) {
		SiacTPdceConto siacTPdceConto = buildSiacTPdceConto(conto);
		siacTPdceConto.setLoginModifica(loginOperazione);
		contoDao.update(siacTPdceConto);
		conto.setUid(siacTPdceConto.getUid());
	}
	
	/**
	 * Annulla un Conto e tutti i suoi figli.
	 *
	 * @param conto the conto
	 */
	public void annullaConto(Conto conto) {
		final String methodName = "annullaConto";
		
		SiacTPdceConto siacTPdceConto = new SiacTPdceConto();
		siacTPdceConto.setUid(conto.getUid());
		contoDao.annulla(siacTPdceConto);
		log.debug(methodName, "annullato conto con uid:"+siacTPdceConto.getUid());
		
		List<SiacTPdceConto> siacTPdceContos = contoDao.ricercaFigliRicorsiva(conto.getUid());
		for (SiacTPdceConto siacTPdceContoFiglio : siacTPdceContos) {
			contoDao.annulla(siacTPdceContoFiglio);
			log.debug(methodName, "annullato conto figlio (diretto o indiretto) con uid:"+siacTPdceContoFiglio.getUid());
		}
		
	}
	
	
	/**
	 * Elimina un Conto e tutti i suoi figli.
	 *
	 * @param conto the conto
	 */
	public void eliminaConto(Conto conto) {
		final String methodName = "eliminaConto";
		
		SiacTPdceConto siacTPdceConto = new SiacTPdceConto();
		siacTPdceConto.setUid(conto.getUid());
		contoDao.elimina(siacTPdceConto);
		log.debug(methodName, "eliminato conto con uid:"+siacTPdceConto.getUid());
		
		List<SiacTPdceConto> siacTPdceContos = contoDao.ricercaFigliRicorsiva(conto.getUid());
		for (SiacTPdceConto siacTPdceContoFiglio : siacTPdceContos) {
			contoDao.elimina(siacTPdceContoFiglio);
			log.debug(methodName, "eliminato conto figlio (diretto o indiretto) con uid:"+siacTPdceContoFiglio.getUid());
		}
	}

	
	
	/**
	 * Find conto by id.
	 *
	 * @param conto the conto
	 * @return the conto
	 */
	public Conto findContoById(Conto conto, Date inizioAnno) {
		conto.setDataInizioValiditaFiltro(inizioAnno);
		SiacTPdceConto siacTPdceConto = contoDao.findById(conto.getUid());
		mapNotNull(siacTPdceConto,conto,GenMapId.SiacTPdceConto_Conto);
		return conto;
	}
	
	/**
	 * Find conto by id.
	 *
	 * @param conto the conto
	 * @return the conto
	 */
	public Conto findContoById(Conto conto, Date inizioAnno, ContoModelDetail... contoModelDetails) {
		String methodName = "findContoById";
		conto.setDataInizioValiditaFiltro(inizioAnno);
		SiacTPdceConto siacTPdceConto = contoDao.findById(conto.getUid());
		if(siacTPdceConto == null) {
			log.warn(methodName, "Impossibile trovare Conto con uid: " + conto.getUid());
		}
		if(contoModelDetails==null){
			mapNotNull(siacTPdceConto,conto,GenMapId.SiacTPdceConto_Conto);
			return conto;
		}
		mapNotNull(siacTPdceConto,conto,GenMapId.SiacTPdceConto_Conto_Base, Converters.byModelDetails(contoModelDetails));
		return conto;
	}
	
	
	/**
	 * Builds the siac t pdce conto.
	 *
	 * @param conto the conto
	 * @return the siac t pdce conto
	 */
	private SiacTPdceConto buildSiacTPdceConto(Conto conto) {
		SiacTPdceConto siacTPdceConto = new SiacTPdceConto();
		conto.setLoginOperazione(loginOperazione);
		siacTPdceConto.setLoginOperazione(loginOperazione);
		conto.setEnte(ente);
		map(conto,siacTPdceConto, GenMapId.SiacTPdceConto_Conto);
		return siacTPdceConto;
	}
	
	public ListaPaginata<Conto> ricercaSinteticaConto(Conto conto, ParametriPaginazione parametriPaginazione) {
		ContoModelDetail[] contoModelDetails = null;
		return ricercaSinteticaConto(conto, parametriPaginazione, contoModelDetails);
	}
	
	/**
	 * Ricerca sintetica conto.
	 *
	 * @param conto the conto
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<Conto> ricercaSinteticaConto(Conto conto, ParametriPaginazione parametriPaginazione, ContoModelDetail... modelDetails) {
		
		boolean uidClassePianoValorizzato = conto.getPianoDeiConti()!=null && conto.getPianoDeiConti().getClassePiano()!=null && conto.getPianoDeiConti().getClassePiano().getUid()!=0;
		boolean uidContoPadreValorizzato = conto.getContoPadre()!=null && conto.getContoPadre().getUid()!=0;
		boolean tipoContoValorizzato = conto.getTipoConto()!=null;
		
		SiacDAmbitoEnum siacDAmbitoEnum = SiacDAmbitoEnum.byAmbito(conto.getAmbito());
		
		Page<SiacTPdceConto> lista = contoDao.ricercaSinteticaConto(
				conto.getEnte().getUid(),
				siacDAmbitoEnum,
				mapToAnno(conto.getDataInizioValiditaFiltro()),
				uidClassePianoValorizzato? conto.getPianoDeiConti().getClassePiano().getUid():null,
				conto.getCodiceInterno(),
				StringUtils.trimToNull(conto.getCodice()),
				uidContoPadreValorizzato? conto.getContoPadre().getUid():null,
				conto.getLivello(),
				conto.getElementoPianoDeiConti() != null ? conto.getElementoPianoDeiConti().getUid() : null,
				conto.getAmmortamento(),
				conto.getContoFoglia(),
				conto.getAttivo(),
				tipoContoValorizzato && conto.getTipoConto().getUid() != 0 ?   conto.getTipoConto().getUid()  : null,
				tipoContoValorizzato && StringUtils.isNotBlank(conto.getTipoConto().getCodice()) ? conto.getTipoConto().getCodice()  : null,
				toPageable(parametriPaginazione));
		
		Conto c = new Conto();
		c.setDataInizioValiditaFiltro(conto.getDataInizioValiditaFiltro());
		if(modelDetails == null) {
			return toListaPaginata(lista, c, GenMapId.SiacTPdceConto_Conto);
		}
		return toListaPaginata(lista, c, GenMapId.SiacTPdceConto_Conto, modelDetails );
		
	}
	
	/**
	 * Ricerca conto by codice.
	 *
	 * @param codiceConto the codcie conto
	 * @return the conto
	 */
	public Conto ricercaContoByCodice(String codiceConto, Integer anno, Ambito ambito) {
		final String methodName = "ricercaContoByCodice";
		 
		SiacDAmbitoEnum siacDAmbitoEnum = SiacDAmbitoEnum.byAmbito(ambito);
		
		Page<SiacTPdceConto> page = contoDao.ricercaSinteticaConto(
				ente.getUid(),
				siacDAmbitoEnum,
				anno,
				null, //
				null, //
				codiceConto, //
				null, //
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				new PageRequest(0, 10)
				);
		
		if(!page.hasContent()){
			return null;
		}
		
		int size = page.getContent().size();
		if(size>0){
			log.warn(methodName, "Trovati "+size+" Conti con codice: "+codiceConto + " Verra' restituito solo il primo.");
		}
		
		SiacTPdceConto siacTPdceConto =  page.getContent().get(0);
		Conto conto = new Conto();
		conto.setUid(siacTPdceConto.getUid());
		return conto;
		
	}
	
	/**
	 * Ricerca sintetica conto.
	 *
	 * @param conto the conto
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<Conto> ricercaSinteticaContoFigli(Conto conto, ParametriPaginazione parametriPaginazione) {
		
		SiacDAmbitoEnum siacDAmbitoEnum = SiacDAmbitoEnum.byAmbito(conto.getAmbito());
			
		Page<SiacTPdceConto> lista = contoDao.ricercaSinteticaConto(
				conto.getEnte().getUid(),
				siacDAmbitoEnum,
				mapToAnno(conto.getDataInizioValiditaFiltro()),
				null, //uidClassePianoValorizzato? conto.getPianoDeiConti().getClassePiano().getUid():null,
				null, //conto.getCodiceInterno(),
				null, //conto.getCodice(),
				conto.getUid(),
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				toPageable(parametriPaginazione));
		
		Conto c = new Conto();
		c.setDataInizioValiditaFiltro(conto.getDataInizioValiditaFiltro());
		return toListaPaginata(lista, c, GenMapId.SiacTPdceConto_Conto);
		
	}
	
	

	/**
	 * Controlla se tutti i figli di un Conto sono senza figli.
	 * 
	 * @param conto
	 * @return true se tutti i figli del conto passato come parametro non hanno figli.
	 */
	public Boolean isContiFiglioSenzaFigli(Conto conto) {
		String methodName = "isContiFiglioSenzaFigli";
		Long contiFiglioConFigli = siacTPdceContoRepository.countContiFiglioConFigli(conto.getUid(), ente.getUid());
		log.debug(methodName, "contiFiglioConFigli: "+ contiFiglioConFigli);
		return contiFiglioConFigli.equals(0L);
	}
	
	/**
	 * Controlla se un conto e' conto foglia.
	 * 
	 * @param conto
	 * @return true se il conto passato come parametro e' conto foglia.
	 */
	public boolean isContoFoglia(Conto conto) {
		String contoFoglia = siacTPdceContoRepository.findAttrContoFoglia(conto.getUid());
		return "S".equals(contoFoglia);
	}
	
	
	/**
	 * Numero di causali non annullate associate ad un conto e a tutti i suoi figli.
	 *
	 * @param conto the conto
	 * @return the long
	 */
	public Long countCausaliNonAnnullateAssociateAlConto(Conto conto) {
		String methodName = "countCausaliNonAnnullateAssociateAlConto";
		
		List<SiacTPdceConto> siacTPdceContos = contoDao.ricercaFigliRicorsiva(conto.getUid());
		
		SiacTPdceConto siacTPdceConto = new SiacTPdceConto();
		siacTPdceConto.setUid(conto.getUid());
		siacTPdceContos.add(siacTPdceConto);
		
		Long count = siacTPdceContoRepository.countCausaliNonAnnullateAssociateAiConti(siacTPdceContos);
		
		log.debug(methodName, "count: "+ count);
		return count;
		
	}
	
	
	/**
	 * Numero di causali non annullate associate ad un conto e a tutti i suoi figli.
	 *
	 * @param conto the conto
	 * @return the long
	 */
	public Long countPrimeNoteNonAnnullateAssociateAlConto(Conto conto) {
		String methodName = "countPrimeNoteNonAnnullateAssociateAlConto";
		
		List<SiacTPdceConto> siacTPdceContos = contoDao.ricercaFigliRicorsiva(conto.getUid());
		
		SiacTPdceConto siacTPdceConto = new SiacTPdceConto();
		siacTPdceConto.setUid(conto.getUid());
		siacTPdceContos.add(siacTPdceConto);
		
		Long count = siacTPdceContoRepository.countPrimeNoteNonAnnullateAssociateAiConti(siacTPdceContos);
		
		log.debug(methodName, "count: "+ count);
		return count;
		
	}

	public ClassePiano getClassePianoAssociatoAlConto(Conto conto) {
		SiacTPdceConto siacTPdceConto = contoDao.findById(conto.getUid());
		
		if(siacTPdceConto==null){
			throw new IllegalArgumentException("Conto con uid "+ conto.getUid() +" non trovato in archivio.");
		}
		
		SiacDPdceFam siacDPdceFam = siacTPdceConto.getSiacTPdceFamTree().getSiacDPdceFam();
		
		return mapNotNull(siacDPdceFam, ClassePiano.class, GenMapId.SiacDPdceFam_ClassePiano);
		
	}

	public List<ClassePiano> findClassiPianoAmmortamento() {
		List<SiacDPdceFam> siacDPdceFams = siacTPdceContoRepository.findClassiPianoAmmortamento(ente.getUid());
		return convertiLista(siacDPdceFams, ClassePiano.class, GenMapId.SiacDPdceFam_ClassePiano);
	}

}
