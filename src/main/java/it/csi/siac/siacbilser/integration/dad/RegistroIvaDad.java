/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.EnumEntityFactory;
import it.csi.siac.siacbilser.integration.dao.RegistroIvaDao;
import it.csi.siac.siacbilser.integration.dao.SiacTIvaAliquotaRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTIvaRegistroRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTSubdocIvaRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDIvaRegistroTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaAliquota;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaRegistro;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDIvaRegistroTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.AliquotaIva;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

/**
 * Data access delegate di un RegistroIva.
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class RegistroIvaDad extends ExtendedBaseDadImpl {
	
	/** The registro iva dao. */
	@Autowired
	private RegistroIvaDao registroIvaDao;
	
	
	/** The siac t iva registro repository. */
	@Autowired
	private SiacTIvaRegistroRepository siacTIvaRegistroRepository;
	
	@Autowired
	private SiacTIvaAliquotaRepository siacTIvaAliquotaRepository;
	
	@Autowired
	private SiacTSubdocIvaRepository siacTSubdocIvaRepository;
	
	/** The eef. */
	@Autowired
	private EnumEntityFactory eef;

	
	/**
	 * Find registro iva by id.
	 *
	 * @param uid the uid
	 * @return the registro iva
	 */
	public RegistroIva findRegistroIvaByIdMinimal(Integer uid) {
		final String methodName = "findRegistroIvaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTIvaRegistro siacTIvaRegistro = registroIvaDao.findById(uid);
		if(siacTIvaRegistro == null) {
			log.debug(methodName, "Impossibile trovare il Registro Iva con id: " + uid);
		}
		return mapNotNull(siacTIvaRegistro, RegistroIva.class, BilMapId.SiacTIvaRegistro_RegistroIva_Minimal);
	}
	
	/**
	 * Find registro iva by id.
	 *
	 * @param uid the uid
	 * @return the registro iva
	 */
	public RegistroIva findRegistroIvaById(Integer uid) {
		final String methodName = "findRegistroIvaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTIvaRegistro siacTIvaRegistro = registroIvaDao.findById(uid);

		if(siacTIvaRegistro == null) {
			log.debug(methodName, "Impossibile trovare il Registro Iva con id: " + uid);
		}
		return mapNotNull(siacTIvaRegistro, RegistroIva.class, BilMapId.SiacTIvaRegistro_RegistroIva);
	}
	
	public RegistroIva findRegistroIvaByIdGruppoBase(Integer uid) {
		final String methodName = "findRegistroIvaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTIvaRegistro siacTIvaRegistro = registroIvaDao.findById(uid);
		if(siacTIvaRegistro == null) {
			log.debug(methodName, "Impossibile trovare il Registro Iva con id: " + uid);
		}
		return mapNotNull(siacTIvaRegistro, RegistroIva.class, BilMapId.SiacTIvaRegistro_RegistroIva_GruppoBase);
	}
	
	public RegistroIva findRegistroIvaByIdGruppoMinimal(Integer uid) {
		final String methodName = "findRegistroIvaById";		
		log.debug(methodName, "uid: "+ uid);
		SiacTIvaRegistro siacTIvaRegistro = registroIvaDao.findById(uid);
		if(siacTIvaRegistro == null) {
			log.debug(methodName, "Impossibile trovare il Registro Iva con id: " + uid);
		}
		return mapNotNull(siacTIvaRegistro, RegistroIva.class, BilMapId.SiacTIvaRegistro_RegistroIva_GruppoMinimal);
	}


	/**
	 * Inserisci registro iva.
	 *
	 * @param registroIva the registro iva
	 */
	public void inserisciRegistroIva(RegistroIva registroIva) {
		// Forzo il flag bloccato a FALSE
		registroIva.setFlagBloccato(Boolean.FALSE);
		SiacTIvaRegistro siacTIvaRegistro = buildSiacTIvaRegistro(registroIva);
		registroIvaDao.create(siacTIvaRegistro);
		registroIva.setUid(siacTIvaRegistro.getUid());
	}
	
	/**
	 * Aggiorna registro iva.
	 *
	 * @param registroIva the registro iva
	 */
	public void aggiornaRegistroIva(RegistroIva registroIva) {
		// Recupero il flag bloccato dal db
		SiacTIvaRegistro stir = registroIvaDao.findById(registroIva.getUid());
		if(stir == null) {
			throw new IllegalArgumentException("Nessun registro IVA corrispondente all'id " + registroIva.getUid());
		}
		registroIva.setFlagBloccato(stir.getIvaregFlagbloccato());
		
		SiacTIvaRegistro siacTIvaRegistro = buildSiacTIvaRegistro(registroIva);	
		registroIvaDao.update(siacTIvaRegistro);
		registroIva.setUid(siacTIvaRegistro.getUid());
	}
	
	
	/**
	 * Ricerca sintetica registro iva.
	 *
	 * @param registroIva the registro iva
	 * @param parametriPaginazione the parametri paginazione
	 * @return the lista paginata
	 */
	public ListaPaginata<RegistroIva> ricercaSinteticaRegistroIva(RegistroIva registroIva, ParametriPaginazione parametriPaginazione) {
		
		Page<SiacTIvaRegistro> lista = registroIvaDao.ricercaSinteticaRegistroIva(
				registroIva.getEnte().getUid(),
				registroIva.getGruppoAttivitaIva()!=null?registroIva.getGruppoAttivitaIva().getUid():null,
				registroIva.getTipoRegistroIva()!=null?eef.getEntity(SiacDIvaRegistroTipoEnum.byTipoRegistroIva(registroIva.getTipoRegistroIva()),registroIva.getEnte().getUid(), SiacDIvaRegistroTipo.class).getUid():null,
				registroIva.getCodice(),
				registroIva.getDescrizione(),
				toPageable(parametriPaginazione));
		
		return toListaPaginata(lista, RegistroIva.class, BilMapId.SiacTIvaRegistro_RegistroIva);
		
	}
	
	/**
	 * Ricerca registro iva.
	 *
	 * @param registroIva the registro iva
	 * @return the list
	 */
	public List<RegistroIva> ricercaRegistroIva(RegistroIva registroIva) {
		
		List<Integer> listaUidAttivitaIva = new ArrayList<Integer>();
		if(registroIva.getGruppoAttivitaIva() != null && registroIva.getGruppoAttivitaIva().getListaAttivitaIva() != null) {
			for(AttivitaIva ai : registroIva.getGruppoAttivitaIva().getListaAttivitaIva()) {
				if(ai != null && ai.getUid() != 0) {
					listaUidAttivitaIva.add(ai.getUid());
				}
			}
		}
		
		List<SiacTIvaRegistro> lista = registroIvaDao.findByEnteProprietarioEGruppoETipo(
				registroIva.getEnte().getUid(),
				registroIva.getGruppoAttivitaIva() == null || registroIva.getGruppoAttivitaIva().getUid() == 0 ? null : registroIva.getGruppoAttivitaIva().getUid(),
				registroIva.getTipoRegistroIva() == null ? null : eef.getEntity(SiacDIvaRegistroTipoEnum.byTipoRegistroIva(registroIva.getTipoRegistroIva()),registroIva.getEnte().getUid(), SiacDIvaRegistroTipo.class).getUid(),
				listaUidAttivitaIva);
		
		return convertiLista(lista, RegistroIva.class, BilMapId.SiacTIvaRegistro_RegistroIva_Base);
	}
	
	/**
	 * Elimina registro iva.
	 *
	 * @param registro the registro
	 */
	public void eliminaRegistroIva(RegistroIva registro) {
		SiacTIvaRegistro siacTIvaRegistro = new SiacTIvaRegistro();
		siacTIvaRegistro.setUid(registro.getUid());
		registroIvaDao.delete(siacTIvaRegistro);
	}
	
	/**
	 * Builds the siac t iva registro.
	 *
	 * @param registroIva the registro iva
	 * @return the siac t iva registro
	 */
	private SiacTIvaRegistro buildSiacTIvaRegistro(RegistroIva registroIva) {
		SiacTIvaRegistro siacTIvaRegistro = new SiacTIvaRegistro();
		siacTIvaRegistro.setLoginOperazione(loginOperazione);
		registroIva.setLoginOperazione(loginOperazione);
		map(registroIva, siacTIvaRegistro, BilMapId.SiacTIvaRegistro_RegistroIva);		
		return siacTIvaRegistro;
	}
	
	/**
	 * Ottiene il registro con un dato codice e afferente ad un dato gruppoAttivitaIva.
	 * 
	 * @param codice codice del registro iva
	 * @param ente ente proprietario
	 * @param gruppo gruppo iva del registro
	 * 
	 * @return il registro iva
	 */
	public RegistroIva findRegistroIvaByCodice(String codice, Integer ente, Integer gruppo) {
		final String methodName = "findRegistroIvaByCodice";		
		log.debug(methodName, "codice: "+ codice);
		SiacTIvaRegistro siacTIvaRegistro = siacTIvaRegistroRepository.findByCodice(codice, ente, gruppo);
		if(siacTIvaRegistro == null) {
			log.debug(methodName, "Impossibile trovare il RegistroIva con codice: " + codice);
		}
		return  mapNotNull(siacTIvaRegistro, RegistroIva.class, BilMapId.SiacTIvaRegistro_RegistroIva_Base);
	}

	/**
	 * Ottiene i registri afferenti ad un dato gruppoAttivitaIva e relativi ad un determinato tipoRegistroIva.
	 * 
	 * @param ente              l'ente dei registri
	 * @param gruppoAttivitaIva il gruppo dei registri
	 * @param tipoRegistroIva   il tipo dei registri
	 * 
	 * @return i registri di dati gruppo e tipo
	 */
	public List<RegistroIva> findRegistriByGruppoAttivitaIvaAndTipoRegistroIva(Ente ente, GruppoAttivitaIva gruppoAttivitaIva, TipoRegistroIva tipoRegistroIva) {
		List<SiacTIvaRegistro> siacTIvaRegistros = siacTIvaRegistroRepository.findBySiacTEnteProprietarioAndSiacTIvaGruppoAndSiacDIvaTipo(
				ente.getUid(), gruppoAttivitaIva.getUid(), tipoRegistroIva.getCodice());
		return convertiLista(siacTIvaRegistros, RegistroIva.class, BilMapId.SiacTIvaRegistro_RegistroIva);
	}
	
	
	/**
	 * Ottiene i registri afferenti ad un dato gruppoAttivitaIva.
	 * 
	 * @param ente              l'ente dei registri
	 * @param gruppoAttivitaIva il gruppo dei registri
	 * 
	 * @return i registri di dati gruppo e tipo
	 */
	public List<RegistroIva> findRegistriByGruppoAttivitaIvaBase(Ente ente, GruppoAttivitaIva gruppoAttivitaIva) {
		List<SiacTIvaRegistro> siacTIvaRegistros = siacTIvaRegistroRepository.findBySiacTEnteProprietarioAndSiacTIvaGruppo(
				ente.getUid(), gruppoAttivitaIva.getUid());
		return convertiLista(siacTIvaRegistros, RegistroIva.class, BilMapId.SiacTIvaRegistro_RegistroIva_Base);
	}
	
	
	/**
	 * Ottiene i registri afferenti ad un dato gruppoAttivitaIva.
	 * 
	 * @param ente              l'ente dei registri
	 * @param gruppoAttivitaIva il gruppo dei registri
	 * 
	 * @return i registri di dati gruppo e tipo
	 */
	public List<RegistroIva> findRegistriByGruppoAttivitaIva(GruppoAttivitaIva gruppoAttivitaIva) {
		List<SiacTIvaRegistro> siacTIvaRegistros = siacTIvaRegistroRepository.findBySiacTEnteProprietarioAndSiacTIvaGruppo(
				ente.getUid(), gruppoAttivitaIva.getUid());
		return convertiLista(siacTIvaRegistros, RegistroIva.class, BilMapId.SiacTIvaRegistro_RegistroIva);
	}
	
		
//	/**
//	 * Ottiene le aliquote afferenti ad un dato gruppoAttivitaIva.
//	 * 
//	 * @param ente              l'ente dei registri
//	 * @param gruppoAttivitaIva il gruppo dei registri
//	 * 
//	 * @return i registri di dati gruppo e tipo
//	 */
//	public List<AliquotaIva> findAliquoteByGruppoAttivitaIvaAndTipoRegistroIva(Ente ente, GruppoAttivitaIva gruppoAttivitaIva, TipoRegistroIva tipoRegistroIva) {
//		List<SiacTIvaAliquota> siacTIvaAliquota = siacTIvaAliquotaRepository.findByIvaGruppoAndIvaTipo(ente.getUid(), gruppoAttivitaIva.getUid(), tipoRegistroIva.getCodice());		
//		return convertiLista(siacTIvaAliquota, AliquotaIva.class, BilMapId.SiacTIvaAliquota_AliquotaIva);
//	}
	
	/**
	 * Ottiene le aliquote afferenti ad un dato gruppoAttivitaIva.
	 * 
	 * @param ente              l'ente dei registri
	 * @param gruppoAttivitaIva il gruppo dei registri
	 * @param anno anno di riferimento per i subdocumentiIVA
	 * 
	 * @return i registri di dati gruppo e tipo
	 */
	public List<AliquotaIva> findAliquoteByGruppoAttivitaIvaAndTipoRegistroIvaAndSubDocIvaAnno(Ente ente, GruppoAttivitaIva gruppoAttivitaIva, TipoRegistroIva tipoRegistroIva, Integer annoSubDocIva, boolean definitivo) {
		Date inizioAnno = Periodo.ANNO.getInizioPeriodo(annoSubDocIva);
		Date fineAnno = Periodo.ANNO.getFinePeriodo(annoSubDocIva);
		
		List<SiacTIvaAliquota> siacTIvaAliquota;
		
		if(definitivo) {
			siacTIvaAliquota = siacTIvaAliquotaRepository.findByIvaGruppoAndIvaTipoAndDateProtocolloDefDaA(ente.getUid(), gruppoAttivitaIva.getUid(), tipoRegistroIva.getCodice(), inizioAnno, fineAnno);
		} else {
			siacTIvaAliquota = siacTIvaAliquotaRepository.findByIvaGruppoAndIvaTipoAndDateProtocolloProvDaA(ente.getUid(), gruppoAttivitaIva.getUid(), tipoRegistroIva.getCodice(), inizioAnno, fineAnno);
		}
		return convertiLista(siacTIvaAliquota, AliquotaIva.class, BilMapId.SiacTIvaAliquota_AliquotaIva);
	}
	
	/**
	 * Aggiornamento del flag bloccato del registro iva
	 * @param registroIva il registro da aggiornare
	 */
	public void aggiornaFlagBloccato(RegistroIva registroIva) {
		SiacTIvaRegistro stir = siacTIvaRegistroRepository.findOne(registroIva.getUid());
		stir.setIvaregFlagbloccato(registroIva.getFlagBloccato());
		stir.setDataModificaAggiornamento(new Date());
	}

	/**
	 * Incrementa il numero di protocollo provvisorio per tutti i subdocumenti iva di dato registro e con numero protocollo provvisorio
	 * non inferiore al numero fornito.
	 * 
	 * @param registroIva il registro per cui effettuare la ricerca
	 * @param numeroProtocolloProvvisorio il numero di protocollo da recuperare
	 * @param annoDataProtocollo anno della data di protocollo
	 */
	public Integer incrementaProtocolloProvvisorio(RegistroIva registroIva, Integer numeroProtocolloProvvisorio, Integer annoDataProtocollo) {
		String methodName = "incrementaProtocolloProvvisorio";
		Date inizioPeriodo = Periodo.ANNO.getInizioPeriodo(annoDataProtocollo);
		Date finePeriodo = Periodo.ANNO.getFinePeriodo(annoDataProtocollo);
		List<SiacTSubdocIva> siacTSubdocIvas = siacTSubdocIvaRepository.findByIvaRegistroAndNumProtocolloProvIvaGreaterThan(registroIva.getUid(), numeroProtocolloProvvisorio, inizioPeriodo, finePeriodo);
		Integer ultimoProtProv = null;
		Date now = new Date();
		for(SiacTSubdocIva stsi : siacTSubdocIvas) {
			int subdocivaProtProv = Integer.parseInt(stsi.getSubdocivaProtProv());
			log.debug(methodName, "trovato subdoc iva con uid: " + stsi.getUid() + " e prot provv : " + subdocivaProtProv + ", lo aggiorno con protocollo " + (subdocivaProtProv+1));
			stsi.setSubdocivaProtProv(Integer.toString(subdocivaProtProv + 1));
			stsi.setDataModificaAggiornamento(now);
			if(ultimoProtProv == null || Integer.parseInt(stsi.getSubdocivaProtProv()) > ultimoProtProv.intValue()){
				ultimoProtProv = Integer.parseInt(stsi.getSubdocivaProtProv());
			}
		}
		log.debug(methodName, "returning " + ultimoProtProv);
		return ultimoProtProv;
	}
	
	/**
	 * Incrementa il numero di protocollo definitivo per tutti i subdocumenti iva di dato registro e con numero protocollo definitivo
	 * non inferiore al numero fornito.
	 * 
	 * @param registroIva il registro per cui effettuare la ricerca
	 * @param numeroProtocolloDefinitivo il numero di protocollo da recuperare
	 * @param annoDataProtocollo anno della data di protocollo
	 */
	public Integer incrementaProtocolloDefinitivo(RegistroIva registroIva, Integer numeroProtocolloDefinitivo, Integer annoDataProtocollo) {
		String methodName = "incrementaProtocolloDefinitivo";
		Date inizioPeriodo = Periodo.ANNO.getInizioPeriodo(annoDataProtocollo);
		Date finePeriodo = Periodo.ANNO.getFinePeriodo(annoDataProtocollo);
		List<SiacTSubdocIva> siacTSubdocIvas = siacTSubdocIvaRepository.findByIvaRegistroAndNumProtocolloDefIvaGreaterThan(registroIva.getUid(), numeroProtocolloDefinitivo, inizioPeriodo, finePeriodo);
		Date now = new Date();
		Integer ultimoProtDef = null;
		for(SiacTSubdocIva stsi : siacTSubdocIvas) {
			int subdocivaProtDef = Integer.parseInt(stsi.getSubdocivaProtDef());
			log.debug(methodName, "trovato subdoc iva con uid: " + stsi.getUid() + " e prot def : " + subdocivaProtDef + ", lo aggiorno con protocollo " + (subdocivaProtDef+1));
			stsi.setSubdocivaProtDef(Integer.toString(subdocivaProtDef + 1));
			stsi.setDataModificaAggiornamento(now);
			if(ultimoProtDef == null || Integer.parseInt(stsi.getSubdocivaProtDef()) > ultimoProtDef.intValue()){
				ultimoProtDef = Integer.parseInt(stsi.getSubdocivaProtDef());
			}
		}
		log.debug(methodName, "returning " + ultimoProtDef);
		return ultimoProtDef;
	}
	
	/**
	 * Ricerca il subdocumento iva corrispondente al registro, al numero di registrazione fornito e all'anno indiccato
	 * @param registroIva il registro iva per cui cercare
	 * @param numeroProtocolloDefinitivo il numero di protocollo definitivo per cui cercare
	 * @param annoDataProtocollo anno della data di protocollo
	 * @return il subdocumento iva popolato con i dati minimi
	 */
	public SubdocumentoIva<?, ?, ?> findSubdocumentoIvaMinimalByRegistroAndNumeroProtocolloDef(RegistroIva registroIva, Integer numeroProtocolloDefinitivo, Integer annoDataProtocollo) {
		Date inizioPeriodo = Periodo.ANNO.getInizioPeriodo(annoDataProtocollo);
		Date finePeriodo = Periodo.ANNO.getFinePeriodo(annoDataProtocollo);
		List<SiacTSubdocIva> siacTSubdocIvas = siacTSubdocIvaRepository.findByIvaRegistroAndNumProtocolloDef(registroIva.getUid(),numeroProtocolloDefinitivo, inizioPeriodo, finePeriodo);
		if(siacTSubdocIvas == null || siacTSubdocIvas.isEmpty()) {
			return null;
		}
		return mapNotNull(siacTSubdocIvas.get(0), SubdocumentoIva.class, BilMapId.SiacTSubdocIva_SubdocumentoIva_Minimal);
	}
	
	/**
	 * Ricerca il subdocumento iva corrispondente al registro, al numero di registrazione fornito e all'anno indiccato
	 * @param registroIva il registro iva per cui cercare
	 * @param numeroProtocolloProvvisorio il numero di protocollo provvisorio per cui cercare
	 * @param annoDataProtocollo anno della data di protocollo
	 * @return il subdocumento iva popolato con i dati minimi
	 */
	public SubdocumentoIva<?, ?, ?> findSubdocumentoIvaMinimalByRegistroAndNumeroProtocolloProv(RegistroIva registroIva, Integer numeroProtocolloProvvisorio, Integer annoDataProtocollo) {
		Date inizioPeriodo = Periodo.ANNO.getInizioPeriodo(annoDataProtocollo);
		Date finePeriodo = Periodo.ANNO.getFinePeriodo(annoDataProtocollo);
		List<SiacTSubdocIva> siacTSubdocIvas = siacTSubdocIvaRepository.findByIvaRegistroAndNumProtocolloProv(registroIva.getUid(),numeroProtocolloProvvisorio, inizioPeriodo, finePeriodo);
		if(siacTSubdocIvas == null || siacTSubdocIvas.isEmpty()) {
			return null;
		}
		return mapNotNull(siacTSubdocIvas.get(0), SubdocumentoIva.class, BilMapId.SiacTSubdocIva_SubdocumentoIva_Minimal);
	}
	
	/**
	 * Ricerca i subdocumenti iva corrispondenti al registro e con data  di protocollo provvisorio nell'anno indicato
	 * @param registroIva il registro iva per cui cercare
	 * @param annoDataProtocollo anno della data di protocollo
	 * @return i subdocumenti iva popolati con i dati minimi
	 */
	@SuppressWarnings("unchecked")
	public List<SubdocumentoIva<?, ?, ?>> findSubdocumentiIvaMinimalByRegistroAndAnnoProtProv(RegistroIva registroIva, Integer annoDataProtocollo) {
		Date inizioPeriodo = Periodo.ANNO.getInizioPeriodo(annoDataProtocollo);
		Date finePeriodo = Periodo.ANNO.getFinePeriodo(annoDataProtocollo);
		List<SiacTSubdocIva> siacTSubdocIvas = siacTSubdocIvaRepository.findByIvaRegistroAndAnnoProtProv(registroIva.getUid(), inizioPeriodo, finePeriodo);
		if(siacTSubdocIvas == null || siacTSubdocIvas.isEmpty()) {
			return new ArrayList<SubdocumentoIva<?, ?, ?>>();
		}
		return convertiLista(siacTSubdocIvas, (Class<SubdocumentoIva<?, ?, ?>>)(Class<?>)SubdocumentoIva.class, BilMapId.SiacTSubdocIva_SubdocumentoIva_Minimal);
	}
	
	/**
	 * Ricerca i subdocumenti iva corrispondenti al registro e con data  di protocollo definitivo nell'anno indicato
	 * @param registroIva il registro iva per cui cercare
	 * @param annoDataProtocollo anno della data di protocollo
	 * @return i subdocumenti iva popolati con i dati minimi
	 */
	@SuppressWarnings("unchecked")
	public List<SubdocumentoIva<?, ?, ?>> findSubdocumentiIvaMinimalByRegistroAndAnnoProtDef(RegistroIva registroIva, Integer annoDataProtocollo) {
		Date inizioPeriodo = Periodo.ANNO.getInizioPeriodo(annoDataProtocollo);
		Date finePeriodo = Periodo.ANNO.getFinePeriodo(annoDataProtocollo);
		List<SiacTSubdocIva> siacTSubdocIvas = siacTSubdocIvaRepository.findByIvaRegistroAndAnnoProtDef(registroIva.getUid(), inizioPeriodo, finePeriodo);
		if(siacTSubdocIvas == null || siacTSubdocIvas.isEmpty()) {
			return new ArrayList<SubdocumentoIva<?, ?, ?>>();
		}
		return convertiLista(siacTSubdocIvas, (Class<SubdocumentoIva<?, ?, ?>>)(Class<?>)SubdocumentoIva.class, BilMapId.SiacTSubdocIva_SubdocumentoIva_Minimal);
	}
	
	/**
	 * Aggiornamento dei numeri di protocollo
	 * @param subdocIvas i subdoc da aggiornare
	 */
	public void aggiornaNumeroProtocolloDefinitivo(List<SubdocumentoIva<?, ?, ?>> subdocIvas) {
		Map<Integer, SubdocumentoIva<?, ?, ?>> ids = new HashMap<Integer, SubdocumentoIva<?, ?, ?>>();
		for(SubdocumentoIva<?, ?, ?> si : subdocIvas) {
			ids.put(si.getUid(), si);
		}
		
		Iterable<SiacTSubdocIva> siacTSubdocIvas = siacTSubdocIvaRepository.findAll(ids.keySet());
		Date now = new Date();
		for(SiacTSubdocIva stsi : siacTSubdocIvas) {
			Integer subdocivaProtDef = ids.get(stsi.getUid()).getNumeroProtocolloDefinitivo();
			
			stsi.setSubdocivaProtDef(subdocivaProtDef != null ? subdocivaProtDef.toString() : null);
			stsi.setDataModificaAggiornamento(now);
		}
	}
	
	/**
	 * Aggiornamento dei numeri di protocollo
	 * @param subdocIvas i subdoc da aggiornare
	 */
	public void aggiornaNumeroProtocolloProvvisorio(List<SubdocumentoIva<?, ?, ?>> subdocIvas) {
		Map<Integer, SubdocumentoIva<?, ?, ?>> ids = new HashMap<Integer, SubdocumentoIva<?, ?, ?>>();
		for(SubdocumentoIva<?, ?, ?> si : subdocIvas) {
			ids.put(si.getUid(), si);
		}
		
		Iterable<SiacTSubdocIva> siacTSubdocIvas = siacTSubdocIvaRepository.findAll(ids.keySet());
		Date now = new Date();
		for(SiacTSubdocIva stsi : siacTSubdocIvas) {
			Integer subdocivaProtProv = ids.get(stsi.getUid()).getNumeroProtocolloProvvisorio();
			
			stsi.setSubdocivaProtProv(subdocivaProtProv != null ? subdocivaProtProv.toString() : null);
			stsi.setDataModificaAggiornamento(now);
		}
	}

	public Integer[] ultimoNumeroProtocolloDefinitivoProvvisorioStampatoDefinitivo(RegistroIva registroIva, int annoBilancio) {
		Object[] importi = siacTIvaRegistroRepository.findSiacTIvaStampaValoreByIvaRegistro(registroIva.getUid(), ""+annoBilancio);
		Integer[] res = new Integer[2];
		if(importi == null) {
			return res;
		}
		for(int i = 0; i < importi.length; i++) {
			res[i] = Integer.valueOf(importi[i] == null ? "0" : importi[i].toString());
		}
		
		return res;
	}

	public <S extends Subdocumento<?, ?>> Long countRegistriIvaBloccatiBySubdocumenti(Iterable<S> subdocumenti) {
		Collection<Integer> ids = new HashSet<Integer>();
		for(S s : subdocumenti) {
			ids.add(s.getUid());
		}
		
		return siacTIvaRegistroRepository.oountBySubdocIdsAndIvaregFlagbloccato(ids, Boolean.TRUE);
	}

	public Long countRegistriIvaBloccatiByElenchi(Iterable<ElencoDocumentiAllegato> elenchi) {
		Collection<Integer> ids = new HashSet<Integer>();
		for(ElencoDocumentiAllegato eda : elenchi) {
			ids.add(eda.getUid());
		}
		return siacTIvaRegistroRepository.oountByEldocIdsAndIvaregFlagbloccato(ids, Boolean.TRUE);
	}

	public Long countRegistriIvaBloccatiByAllegatiAtto(Iterable<AllegatoAtto> allegatoAtto) {
		Collection<Integer> ids = new HashSet<Integer>();
		for(AllegatoAtto aa : allegatoAtto) {
			ids.add(aa.getUid());
		}
		return siacTIvaRegistroRepository.oountByAttoalIdsAndIvaregFlagbloccato(ids, Boolean.TRUE);
	}


}
