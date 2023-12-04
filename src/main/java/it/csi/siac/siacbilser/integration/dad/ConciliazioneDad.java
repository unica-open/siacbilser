/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.ConciliazionePerBeneficiarioDao;
import it.csi.siac.siacbilser.integration.dao.ConciliazionePerCapitoloDao;
import it.csi.siac.siacbilser.integration.dao.ConciliazionePerTitoloDao;
import it.csi.siac.siacbilser.integration.dao.SiacRConciliazioneBeneficiarioRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRConciliazioneCapitoloRepository;
import it.csi.siac.siacbilser.integration.dao.SiacRConciliazioneTitoloRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneBeneficiario;
import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneCapitolo;
import it.csi.siac.siacbilser.integration.entity.SiacRConciliazioneTitolo;
import it.csi.siac.siacbilser.integration.entity.SiacTPdceConto;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDConciliazioneClasseEnum;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.ConciliazionePerBeneficiario;
import it.csi.siac.siacgenser.model.ConciliazionePerCapitolo;
import it.csi.siac.siacgenser.model.ConciliazionePerTitolo;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Data access delegate delle classi di conciliazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ConciliazioneDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private SiacRConciliazioneTitoloRepository siacRConciliazioneTitoloRepository;
	@Autowired
	private SiacRConciliazioneCapitoloRepository siacRConciliazioneCapitoloRepository;
	@Autowired
	private SiacRConciliazioneBeneficiarioRepository siacRConciliazioneBeneficiarioRepository;
	
	@Autowired
	private ConciliazionePerTitoloDao conciliazionePerTitoloDao;
	@Autowired
	private ConciliazionePerCapitoloDao conciliazionePerCapitoloDao;
	@Autowired
	private ConciliazionePerBeneficiarioDao conciliazionePerBeneficiarioDao;
	
	public Long countConciliazioniTitoloByContoAndClassificatoreAndClasse(Conto conto, ClassificatoreGerarchico classificatoreGerarchico, ClasseDiConciliazione classeDiConciliazione, Integer uid) {
		SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum = SiacDConciliazioneClasseEnum.byClasseDiConciliazione(classeDiConciliazione);
		return siacRConciliazioneTitoloRepository.countByEnteProprietarioIdAndPdceContoIdAndClassifIdAndConcclaCodeAndNotConctitId(ente.getUid(), conto.getUid(),
				classificatoreGerarchico.getUid(), siacDConciliazioneClasseEnum.getCodice(), uid);
	}

	public Long countConciliazioniCapitoloByContoAndCapitolo(Conto conto, Capitolo<?, ?> capitolo, Integer uid) {
		return siacRConciliazioneCapitoloRepository.countByEnteProprietarioIdAndPdceContoIdAndElemIdAndNotConccapId(ente.getUid(), conto.getUid(), capitolo.getUid(), uid);
	}
	
	public Long countConciliazioniBeneficiarioByContoAndCapitoloAndSoggetto(Conto conto, Capitolo<?, ?> capitolo, Soggetto soggetto, Integer uid) {
		return siacRConciliazioneBeneficiarioRepository.countByEnteProprietarioIdAndPdceContoIdAndElemIdAndSoggettoIdAndNotConcbenId(ente.getUid(), conto.getUid(), capitolo.getUid(), soggetto.getUid(), uid);
	}

	public void inserisciConciliazionePerTitolo(ConciliazionePerTitolo conciliazionePerTitolo) {
		SiacRConciliazioneTitolo siacRConciliazioneTitolo = buildSiacRConciliazioneTitolo(conciliazionePerTitolo);
		conciliazionePerTitoloDao.create(siacRConciliazioneTitolo);
		conciliazionePerTitolo.setUid(siacRConciliazioneTitolo.getUid());
	}

	private SiacRConciliazioneTitolo buildSiacRConciliazioneTitolo(ConciliazionePerTitolo conciliazionePerTitolo) {
		SiacRConciliazioneTitolo siacRConciliazioneTitolo = new SiacRConciliazioneTitolo();
		conciliazionePerTitolo.setLoginOperazione(loginOperazione);
		siacRConciliazioneTitolo.setLoginOperazione(loginOperazione);
		conciliazionePerTitolo.setEnte(ente);
		map(conciliazionePerTitolo,siacRConciliazioneTitolo, GenMapId.SiacRConciliazioneTitolo_ConciliazionePerTitolo);
		return siacRConciliazioneTitolo;
	}

	public void aggiornaConciliazionePerTitolo(ConciliazionePerTitolo conciliazionePerTitolo) {
		SiacRConciliazioneTitolo siacRConciliazioneTitolo = buildSiacRConciliazioneTitolo(conciliazionePerTitolo);
		conciliazionePerTitoloDao.update(siacRConciliazioneTitolo);
		conciliazionePerTitolo.setUid(siacRConciliazioneTitolo.getUid());
	}

	public void eliminaConciliazionePerTitolo(ConciliazionePerTitolo conciliazionePerTitolo) {
		final String methodName = "eliminaConciliazionePerTitolo";
		SiacRConciliazioneTitolo siacRConciliazioneTitolo = new SiacRConciliazioneTitolo();
		siacRConciliazioneTitolo.setUid(conciliazionePerTitolo.getUid());
		conciliazionePerTitoloDao.elimina(siacRConciliazioneTitolo);
		log.debug(methodName, "Eliminata conciliazione per titolo con uid:" + conciliazionePerTitolo.getUid());
	}

	public ConciliazionePerTitolo findConciliazionePerTitoloByUid(Integer uid) {
		SiacRConciliazioneTitolo siacRConciliazioneTitolo = conciliazionePerTitoloDao.findById(uid);
		return mapNotNull(siacRConciliazioneTitolo, ConciliazionePerTitolo.class, GenMapId.SiacRConciliazioneTitolo_ConciliazionePerTitolo);
	}

	public void inserisciConciliazionePerCapitolo(ConciliazionePerCapitolo conciliazionePerCapitolo) {
		SiacRConciliazioneCapitolo siacRConciliazioneCapitolo = buildSiacRConciliazioneCapitolo(conciliazionePerCapitolo);
		conciliazionePerCapitoloDao.create(siacRConciliazioneCapitolo);
		conciliazionePerCapitolo.setUid(siacRConciliazioneCapitolo.getUid());
	}

	public void aggiornaConciliazionePerCapitolo(ConciliazionePerCapitolo conciliazionePerCapitolo) {
		SiacRConciliazioneCapitolo siacRConciliazioneCapitolo = buildSiacRConciliazioneCapitolo(conciliazionePerCapitolo);
		conciliazionePerCapitoloDao.update(siacRConciliazioneCapitolo);
		conciliazionePerCapitolo.setUid(siacRConciliazioneCapitolo.getUid());
	}

	private SiacRConciliazioneCapitolo buildSiacRConciliazioneCapitolo(ConciliazionePerCapitolo conciliazionePerCapitolo) {
		SiacRConciliazioneCapitolo siacRConciliazioneCapitolo = new SiacRConciliazioneCapitolo();
		conciliazionePerCapitolo.setLoginOperazione(loginOperazione);
		siacRConciliazioneCapitolo.setLoginOperazione(loginOperazione);
		conciliazionePerCapitolo.setEnte(ente);
		map(conciliazionePerCapitolo,siacRConciliazioneCapitolo, GenMapId.SiacRConciliazioneCapitolo_ConciliazionePerCapitolo);
		return siacRConciliazioneCapitolo;
	}

	public void eliminaConciliazionePerCapitolo(ConciliazionePerCapitolo conciliazionePerCapitolo) {
		final String methodName = "eliminaConciliazionePerCapitolo";
		SiacRConciliazioneCapitolo siacRConciliazioneCapitolo = new SiacRConciliazioneCapitolo();
		siacRConciliazioneCapitolo.setUid(conciliazionePerCapitolo.getUid());
		conciliazionePerCapitoloDao.elimina(siacRConciliazioneCapitolo);
		log.debug(methodName, "Eliminata conciliazione per capitolo con uid:" + conciliazionePerCapitolo.getUid());
	}

	public ConciliazionePerCapitolo findConciliazionePerCapitoloByUid(Integer uid) {
		SiacRConciliazioneCapitolo siacRConciliazioneCapitolo = conciliazionePerCapitoloDao.findById(uid);
		return mapNotNull(siacRConciliazioneCapitolo, ConciliazionePerCapitolo.class, GenMapId.SiacRConciliazioneCapitolo_ConciliazionePerCapitolo);
	}

	public void inserisciConciliazionePerBeneficiario(ConciliazionePerBeneficiario conciliazionePerBeneficiario) {
		SiacRConciliazioneBeneficiario siacRConciliazioneBeneficiario = buildSiacRConciliazioneBeneficiario(conciliazionePerBeneficiario);
		conciliazionePerBeneficiarioDao.create(siacRConciliazioneBeneficiario);
		conciliazionePerBeneficiario.setUid(siacRConciliazioneBeneficiario.getUid());
	}

	public void aggiornaConciliazionePerBeneficiario(ConciliazionePerBeneficiario conciliazionePerBeneficiario) {
		SiacRConciliazioneBeneficiario siacRConciliazioneBeneficiario = buildSiacRConciliazioneBeneficiario(conciliazionePerBeneficiario);
		conciliazionePerBeneficiarioDao.update(siacRConciliazioneBeneficiario);
		conciliazionePerBeneficiario.setUid(siacRConciliazioneBeneficiario.getUid());
	}

	private SiacRConciliazioneBeneficiario buildSiacRConciliazioneBeneficiario(ConciliazionePerBeneficiario conciliazionePerBeneficiario) {
		SiacRConciliazioneBeneficiario siacRConciliazioneBeneficiario = new SiacRConciliazioneBeneficiario();
		conciliazionePerBeneficiario.setLoginOperazione(loginOperazione);
		siacRConciliazioneBeneficiario.setLoginOperazione(loginOperazione);
		conciliazionePerBeneficiario.setEnte(ente);
		map(conciliazionePerBeneficiario,siacRConciliazioneBeneficiario, GenMapId.SiacRConciliazioneBeneficiario_ConciliazionePerBeneficiario);
		return siacRConciliazioneBeneficiario;
	}
	
	public void eliminaConciliazionePerBeneficiario(ConciliazionePerBeneficiario conciliazionePerBeneficiario) {
		final String methodName = "eliminaConciliazionePerBeneficiario";
		SiacRConciliazioneBeneficiario siacRConciliazioneBeneficiario = new SiacRConciliazioneBeneficiario();
		siacRConciliazioneBeneficiario.setUid(conciliazionePerBeneficiario.getUid());
		conciliazionePerBeneficiarioDao.elimina(siacRConciliazioneBeneficiario);
		log.debug(methodName, "Eliminata conciliazione per beneficiario con uid:" + conciliazionePerBeneficiario.getUid());
	}

	public ConciliazionePerBeneficiario findConciliazionePerBeneficiarioByUid(Integer uid) {
		SiacRConciliazioneBeneficiario siacRConciliazioneBeneficiario = conciliazionePerBeneficiarioDao.findById(uid);
		return mapNotNull(siacRConciliazioneBeneficiario, ConciliazionePerBeneficiario.class, GenMapId.SiacRConciliazioneBeneficiario_ConciliazionePerBeneficiario);
	}

	public ListaPaginata<ConciliazionePerTitolo> ricercaSinteticaConciliazioniPerTitolo(ConciliazionePerTitolo conciliazionePerTitolo, ClassificatoreGerarchico titolo, ClassificatoreGerarchico tipologia, ParametriPaginazione parametriPaginazione) {
		
		SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum = SiacDConciliazioneClasseEnum.byClasseDiConciliazioneEvenNull(conciliazionePerTitolo.getClasseDiConciliazione());
		
		Page<SiacRConciliazioneTitolo> siacRConciliazioneTitolos = conciliazionePerTitoloDao.ricercaSinteticaConciliazioniPerTitolo(
				ente.getUid(),
				siacDConciliazioneClasseEnum,
				conciliazionePerTitolo.getClassificatoreGerarchico() != null ? conciliazionePerTitolo.getClassificatoreGerarchico().getUid() : null,
				titolo != null ? titolo.getUid() : null,
				tipologia != null ? tipologia.getUid() : null,
				toPageable(parametriPaginazione));
		return toListaPaginata(siacRConciliazioneTitolos, ConciliazionePerTitolo.class, GenMapId.SiacRConciliazioneTitolo_ConciliazionePerTitolo);
	}

	public ListaPaginata<ConciliazionePerCapitolo> ricercaSinteticaConciliazioniPerCapitolo(ConciliazionePerCapitolo conciliazionePerCapitolo, ParametriPaginazione parametriPaginazione) {
		Page<SiacRConciliazioneCapitolo> siacRConciliazioneCapitolos = conciliazionePerCapitoloDao.ricercaSinteticaConciliazioniPerCapitolo(
				ente.getUid(),
				conciliazionePerCapitolo.getCapitolo() != null ? conciliazionePerCapitolo.getCapitolo().getUid() : null,
				toPageable(parametriPaginazione));
		return toListaPaginata(siacRConciliazioneCapitolos, ConciliazionePerCapitolo.class, GenMapId.SiacRConciliazioneCapitolo_ConciliazionePerCapitolo);
	}

	public ListaPaginata<ConciliazionePerBeneficiario> ricercaSinteticaConciliazioniPerBeneficiario(ConciliazionePerBeneficiario conciliazionePerBeneficiario, ParametriPaginazione parametriPaginazione) {
		
		SiacDBilElemTipoEnum siacDBilElemTipoEnum = null;
		if(conciliazionePerBeneficiario.getCapitolo() != null) {
		siacDBilElemTipoEnum = SiacDBilElemTipoEnum.byTipoCapitoloEvenNull(conciliazionePerBeneficiario.getCapitolo().getTipoCapitolo());
		}
		
		Page<SiacRConciliazioneBeneficiario> siacRConciliazioneBeneficiarios = conciliazionePerBeneficiarioDao.ricercaSinteticaConciliazioniPerBeneficiario(
				ente.getUid(),
				conciliazionePerBeneficiario.getSoggetto() != null ? conciliazionePerBeneficiario.getSoggetto().getUid() : null,
				siacDBilElemTipoEnum != null ? siacDBilElemTipoEnum.getCodice() : null,
				conciliazionePerBeneficiario.getCapitolo() != null ? mapToString(conciliazionePerBeneficiario.getCapitolo().getAnnoCapitolo()) : null,
				conciliazionePerBeneficiario.getCapitolo() != null ? mapToString(conciliazionePerBeneficiario.getCapitolo().getNumeroCapitolo()) : null,
				conciliazionePerBeneficiario.getCapitolo() != null ? mapToString(conciliazionePerBeneficiario.getCapitolo().getNumeroArticolo()) : null,
				conciliazionePerBeneficiario.getCapitolo() != null ? mapToString(conciliazionePerBeneficiario.getCapitolo().getNumeroUEB()) : null,
				toPageable(parametriPaginazione));
		return toListaPaginata(siacRConciliazioneBeneficiarios, ConciliazionePerBeneficiario.class, GenMapId.SiacRConciliazioneBeneficiario_ConciliazionePerBeneficiario);
	}

	public List<Conto> ricercaContiConciliazionePerTitolo(ClasseDiConciliazione classe, ClassificatoreGerarchico classificatore) {
		SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum = SiacDConciliazioneClasseEnum.byClasseDiConciliazioneEvenNull(classe);
		List<SiacTPdceConto> siacTPdceContos = siacRConciliazioneTitoloRepository.findSiacTPdceContoByEnteProprietarioIdAndClassifIdAndClasseConciliazione(ente.getUid(), classificatore.getUid(), siacDConciliazioneClasseEnum.getCodice());
		return convertiLista(siacTPdceContos, Conto.class, GenMapId.SiacTPdceConto_Conto_Minimal);
	}

	public List<Conto> ricercaContiConciliazionePerCapitolo(Capitolo<?,?> capitolo) {
		List<SiacTPdceConto> siacTPdceContos = siacRConciliazioneCapitoloRepository.findSiacTPdceContoByBilElem(capitolo.getUid());
		return convertiLista(siacTPdceContos, Conto.class, GenMapId.SiacTPdceConto_Conto_Minimal);
	}
	
	public List<Conto> ricercaContiConciliazionePerCapitoloEClasse(Capitolo<?,?> capitolo, ClasseDiConciliazione classeDiConciliazione) {
		SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum = SiacDConciliazioneClasseEnum.byClasseDiConciliazione(classeDiConciliazione);
		List<SiacTPdceConto> siacTPdceContos = siacRConciliazioneCapitoloRepository.findSiacTPdceContoByBilElemAndClasseConciliazione(capitolo.getUid(),siacDConciliazioneClasseEnum.getCodice());
		return convertiLista(siacTPdceContos, Conto.class, GenMapId.SiacTPdceConto_Conto_Minimal);
	}

	public List<Conto> ricercaContiConciliazionePerBeneficiario(Soggetto beneficiario, Capitolo<?,?> capitolo) {
		List<SiacTPdceConto> siacTPdceContos = siacRConciliazioneBeneficiarioRepository.findSiacTPdceContoByBilElemESoggetto(beneficiario.getUid(), capitolo.getUid());
		return convertiLista(siacTPdceContos, Conto.class, GenMapId.SiacTPdceConto_Conto_Minimal);
	}
	
	public List<Conto> ricercaContiConciliazionePerBeneficiarioEClasse(Soggetto beneficiario, Capitolo<?,?> capitolo, ClasseDiConciliazione classeDiConciliazione) {
		SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum = SiacDConciliazioneClasseEnum.byClasseDiConciliazione(classeDiConciliazione);
		List<SiacTPdceConto> siacTPdceContos = siacRConciliazioneBeneficiarioRepository.findSiacTPdceContoByBilElemESoggettoEClasseConciliazione(beneficiario.getUid(), capitolo.getUid(), siacDConciliazioneClasseEnum.getCodice());
		return convertiLista(siacTPdceContos, Conto.class, GenMapId.SiacTPdceConto_Conto_Minimal);
	}

	public List<Conto> ricercaContiConciliazioniPerTitolo(ConciliazionePerTitolo conciliazionePerTitolo) {
		SiacDConciliazioneClasseEnum siacDConciliazioneClasseEnum = SiacDConciliazioneClasseEnum.byClasseDiConciliazione(conciliazionePerTitolo.getClasseDiConciliazione());
		List<SiacTPdceConto> siacTPdceContos = siacRConciliazioneTitoloRepository.findSiacTPdceContoByEnteProprietarioIdAndClassifIdAndClasseConciliazione(ente.getUid(),
				conciliazionePerTitolo.getClassificatoreGerarchico().getUid(), siacDConciliazioneClasseEnum.getCodice());
		return convertiLista(siacTPdceContos, Conto.class, GenMapId.SiacTPdceConto_Conto_Minimal);
	}

}
