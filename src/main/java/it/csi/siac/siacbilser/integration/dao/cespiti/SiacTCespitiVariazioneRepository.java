/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dao.cespiti;


import org.springframework.data.jpa.repository.JpaRepository;

import it.csi.siac.siacbilser.integration.entity.SiacTCespitiVariazione;

/**
 * The Interface SiacDOnereRepository.
 */
public interface SiacTCespitiVariazioneRepository extends JpaRepository<SiacTCespitiVariazione, Integer> {

}
