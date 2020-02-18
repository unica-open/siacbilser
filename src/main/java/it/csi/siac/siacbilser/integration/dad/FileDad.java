/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.FileDao;
import it.csi.siac.siacbilser.integration.dao.SiacDFileTipoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDFileTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTEnteProprietario;
import it.csi.siac.siacbilser.integration.entity.SiacTFile;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siaccorser.model.file.TipoFile;

/**
 * Data access delegate di un File .
 *
 * @author Domenico
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class FileDad extends ExtendedBaseDadImpl {
	
	@Autowired
	private FileDao fileDao;
	@Autowired
	private SiacDFileTipoRepository siacDFileTipoRepository;
	
	
	/**
	 * Inserisci file
	 * @param file il file
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void inserisciFile(File file) {
		SiacTFile siacTFile = buildSiacTFile(file);
		
		fileDao.create(siacTFile);
		file.setUid(siacTFile.getUid());
	}
	
	/**
	 * Ricerca del tipo file per codice
	 * @param codice il codice del tipo
	 * @return il tipo file
	 */
	public TipoFile ricercaTipoFileByCodice(String codice) {
		SiacDFileTipo siacDFileTipo = siacDFileTipoRepository.findByFileTipoCode(codice, ente.getUid());
		return mapNotNull(siacDFileTipo, TipoFile.class, BilMapId.SiacDFileTipo_TipoFile);
	}
	
	/**
	 * Costruzione del SiacTFile
	 * @param file il file
	 * @return la entity
	 */
	private SiacTFile buildSiacTFile(File file) {
		SiacTFile siacTFile = new SiacTFile();
		siacTFile.setLoginOperazione(loginOperazione);
		siacTFile.setSiacTEnteProprietario(createSiacTEnteProprietario(ente));
		map(file, siacTFile, BilMapId.SiacTFile_File);
		siacTFile.setLoginOperazione(loginOperazione);
		return siacTFile;
	}
	
	protected SiacTEnteProprietario createSiacTEnteProprietario(Ente ente) {
		SiacTEnteProprietario siacTEnteProprietario = new SiacTEnteProprietario();
		siacTEnteProprietario.setUid(ente.getUid());
		return siacTEnteProprietario;
	}
	
}
