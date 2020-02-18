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

import it.csi.siac.siacbilser.integration.dao.ClassificatoreGsaDao;
import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.SiacTGsaClassifRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDGsaClassifStato;
import it.csi.siac.siacbilser.integration.entity.SiacRGsaClassifStato;
import it.csi.siac.siacbilser.integration.entity.SiacTGsaClassif;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDAmbitoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDGsaClassifStatoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.GenMapId;
import it.csi.siac.siacbilser.integration.entitymapping.converter.base.Converters;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;
import it.csi.siac.siacgenser.model.ClassificatoreGSAModelDetail;
import it.csi.siac.siacgenser.model.StatoOperativoClassificatoreGSA;
/**
 * Data access delegate di un ClassificatoreGSA.
 * 
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ClassificatoreGSADad extends ExtendedBaseDadImpl {
	
	@Autowired
	private ClassificatoreGsaDao classificatoreGsaDao;
	@Autowired 
	private SiacTGsaClassifRepository siacTGsaClassifRepository;
	@Autowired
	private EnumEntityFactory eef;
	
	/**
	 * Inserisci conto.
	 *
	 * @param classificatoreGSA the conto
	 */
	public void inserisciClassificatoreGSA(ClassificatoreGSA classificatoreGSA) {
		SiacTGsaClassif siacTGsaClassif = buildSiacTGsaClassif(classificatoreGSA);
		siacTGsaClassif.setLoginCreazione(loginOperazione);
		classificatoreGsaDao.create(siacTGsaClassif);
		classificatoreGSA.setUid(siacTGsaClassif.getUid());
	}
	

	/**
	 * Aggiorna conto.
	 *
	 * @param conto the conto
	 */
	public void aggiornaClassificatoreGSA(ClassificatoreGSA conto) {
		SiacTGsaClassif siacTGsaClassif = buildSiacTGsaClassif(conto);
		siacTGsaClassif.setLoginModifica(loginOperazione);
		classificatoreGsaDao.update(siacTGsaClassif);
		conto.setUid(siacTGsaClassif.getUid());
	}
	
	/**
	 * Find conto by id.
	 *
	 * @param classificatoreGsa the conto
	 * @return the conto
	 */
	public ClassificatoreGSA findClassificatoreGSAValidoByCodice(ClassificatoreGSA classificatoreGsa) {
		SiacTGsaClassif siacTGsaClassif = siacTGsaClassifRepository.findSiacTGsaClassifValidoByCode(classificatoreGsa.getCodice(), ente.getUid());
		return mapNotNull(siacTGsaClassif,ClassificatoreGSA.class,GenMapId.SiacTGsaClassif_ClassificatoreGSA_ModelDetail);
	}
	
	
	/**
	 * Find conto by id.
	 *
	 * @param classificatoreGsa the classificatore gsa
	 * @return the conto
	 */
	public ClassificatoreGSA findClassificatoreGSAById(ClassificatoreGSA classificatoreGsa) {
		SiacTGsaClassif siacTGsaClassif = siacTGsaClassifRepository.findSiacTGsaClassifValidoById(classificatoreGsa.getUid(), ente.getUid());
		return mapNotNull(siacTGsaClassif,ClassificatoreGSA.class,GenMapId.SiacTGsaClassif_ClassificatoreGSA_ModelDetail);
	}
	
	/**
	 * Find classificatore GSA by id.
	 *
	 * @param classificatoreGSA the classificatore GSA
	 * @param classificatoreGSAModelDetails the classificatore GSA model details
	 * @return the classificatore GSA
	 */
	public ClassificatoreGSA findClassificatoreGSAById(ClassificatoreGSA classificatoreGSA, ClassificatoreGSAModelDetail... classificatoreGSAModelDetails) {
		SiacTGsaClassif siacTGsaClassif = siacTGsaClassifRepository.findSiacTGsaClassifValidoById(classificatoreGSA.getUid(), ente.getUid());
		if(classificatoreGSAModelDetails != null) {
			
			return mapNotNull(siacTGsaClassif,ClassificatoreGSA.class,GenMapId.SiacTGsaClassif_ClassificatoreGSA_ModelDetail, Converters.byModelDetails(classificatoreGSAModelDetails));
		}
		
		return mapNotNull(siacTGsaClassif,ClassificatoreGSA.class,GenMapId.SiacTGsaClassif_ClassificatoreGSA_ModelDetail);
	}
	
	/**
	 * Controlla se tutti i figli di un ClassificatoreGSA sono annullati.
	 *
	 * @param classificatoreGSA the classificatore GSA
	 * @return true se tutti i figli del conto passato come parametro non hanno figli.
	 */
	public Boolean isClassificatoreGSASenzaFigliValidi(ClassificatoreGSA classificatoreGSA) {
		Long figliValidi = siacTGsaClassifRepository.countSiacTGsaClassifFigliValidiByIdPadre(classificatoreGSA.getUid());
		return figliValidi.equals(Long.valueOf(0));
	}
	
	/**
	 * Annulla stato classificatore GSA.
	 *
	 * @param classificatoreGSA the classificatore GSA
	 */
	public void annullaStatoClassificatoreGSA(ClassificatoreGSA classificatoreGSA) {
		SiacTGsaClassif siacTGsaClassif = siacTGsaClassifRepository.findOne(classificatoreGSA.getUid());
		aggiornaStatoClassificatoreGSA(siacTGsaClassif, StatoOperativoClassificatoreGSA.ANNULLATO);
	}
	
	private void aggiornaStatoClassificatoreGSA(SiacTGsaClassif siacTGsaClassif,StatoOperativoClassificatoreGSA statoOperativoClassificatoreGSA) {
		Date dataCancellazione = new Date();
		if(siacTGsaClassif.getSiacRGsaClassifStatos()==null){
			siacTGsaClassif.setSiacRGsaClassifStatos(new ArrayList<SiacRGsaClassifStato>());
		}
		for(SiacRGsaClassifStato r : siacTGsaClassif.getSiacRGsaClassifStatos()){
			r.setDataCancellazioneIfNotSet(dataCancellazione);					
		}
		Date now = new Date();
		SiacRGsaClassifStato siacRGsaClassifStato = new SiacRGsaClassifStato();
		SiacDGsaClassifStato siacDGsaClassifStato = eef.getEntity(SiacDGsaClassifStatoEnum.byStatoOperativo(statoOperativoClassificatoreGSA), siacTGsaClassif.getSiacTEnteProprietario().getUid());
		siacRGsaClassifStato.setSiacDGsaClassifStato(siacDGsaClassifStato);		
		siacRGsaClassifStato.setSiacTGsaClassif(siacTGsaClassif);			
		siacRGsaClassifStato.setSiacTEnteProprietario(siacTGsaClassif.getSiacTEnteProprietario());
		siacRGsaClassifStato.setDataModificaInserimento(now);
		siacRGsaClassifStato.setLoginOperazione(loginOperazione);		
		
		siacTGsaClassif.addSiacRGsaClassifStato(siacRGsaClassifStato);
		
	}


	/**
	 * Builds the siac t pdce conto.
	 *
	 * @param classificatoreGSA the conto
	 * @return the siac t pdce conto
	 */
	private SiacTGsaClassif buildSiacTGsaClassif(ClassificatoreGSA classificatoreGSA) {
		SiacTGsaClassif siacTGsaClassif = new SiacTGsaClassif();
		classificatoreGSA.setLoginOperazione(loginOperazione);
		siacTGsaClassif.setLoginOperazione(loginOperazione);
		classificatoreGSA.setEnte(ente);
		map(classificatoreGSA,siacTGsaClassif, GenMapId.SiacTGsaClassif_ClassificatoreGSA);
		return siacTGsaClassif;
	}
	
	
	
	/**
	 * Ricerca sintetica conto.
	 *
	 * @param classifGSA the conto
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<ClassificatoreGSA> ricercaSinteticaClassificatoreGSA(ClassificatoreGSA classifGSA, ParametriPaginazione parametriPaginazione) {
		
		
		SiacDAmbitoEnum siacDAmbitoEnum = SiacDAmbitoEnum.byAmbito(classifGSA.getAmbito());
		Page<SiacTGsaClassif> lista = classificatoreGsaDao.ricercaSinteticaGsaClassif(
				ente.getUid(),
				siacDAmbitoEnum,
				StringUtils.trimToNull(classifGSA.getCodice()),
				classifGSA.getDescrizione(),
//				mapToAnno(classifGSA.getDataInizioValidita()),
				classifGSA.getStatoOperativoClassificatoreGSA() != null? SiacDGsaClassifStatoEnum.byStatoOperativo(classifGSA.getStatoOperativoClassificatoreGSA()) : null,
				toPageable(parametriPaginazione));
		
		//c.setDataInizioValiditaFiltro(conto.getDataInizioValidita());
		return toListaPaginata(lista, ClassificatoreGSA.class, GenMapId.SiacTGsaClassif_ClassificatoreGSA_All);
		
	}
	
	/**
	 * Ricerca dei classificatori GSA.
	 *
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public List<ClassificatoreGSA> ricercaClassificatoreGSAValidi(Ambito ambito) {
		List<SiacTGsaClassif> lista = classificatoreGsaDao.ricercaGsaClassif(
				ente.getUid(),
				SiacDAmbitoEnum.byAmbito(ambito),
				null,
				null,
				SiacDGsaClassifStatoEnum.VALIDO);
		
		return convertiLista(lista, ClassificatoreGSA.class, GenMapId.SiacTGsaClassif_ClassificatoreGSA_AllValidi);
	}

}
