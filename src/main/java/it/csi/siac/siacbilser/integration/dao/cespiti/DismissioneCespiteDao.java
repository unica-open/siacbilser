/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiDismissioni;
import it.csi.siac.siacbilser.integration.entity.SiacTPrimaNota;
import it.csi.siac.siaccommonser.integration.dao.base.Dao;


/**
 * The Interface CespitiDismissioniDao.
 *
 * @author Antonino
 */
public interface DismissioneCespiteDao extends Dao<SiacTCespitiDismissioni, Integer> {
	
	public SiacTCespitiDismissioni create(SiacTCespitiDismissioni r);

	public SiacTCespitiDismissioni update(SiacTCespitiDismissioni r);

	public void delete(SiacTCespitiDismissioni r);
	
	public Page<SiacTCespitiDismissioni>ricercaSinteticaCespite(Integer enteProprietarioId, Integer annoElenco, Integer numeroElenco, String descrizione, Integer uidAttoAmministrativo, Date dataCessazione, Integer uidEvento, Integer uidCausaleEP, String descrizioneStatoCessazione, Integer uidCespite, Pageable pageable);

	public void aggiornaStato(SiacTCespitiDismissioni siacTDismissioneCespite);

	public List<SiacTPrimaNota> ricercaPrimeNoteGenerateDaDismissione(Integer uidDismissione, Integer uidCespite, String statoCode);
}
