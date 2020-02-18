/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.dad;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.integration.dao.SiacDContotesoreriaRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDContotesoreria;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;

// TODO: Auto-generated Javadoc
/**
 * The Class ContoTesoreriaDad.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class ContoTesoreriaDad extends BaseDadImpl {

	/** The siac d contotesoreria repository. */
	@Autowired
	private SiacDContotesoreriaRepository siacDContotesoreriaRepository;

	/**
	 * Effettua la ricerca dei conti tesoreria per un Ente (entrata o uscita).
	 *
	 * @param ente the ente
	 * @return List&lt;TipoCausale&gt;
	 */
	public List<ContoTesoreria> ricercaContiTesoreriaByEnte(Ente ente) {

		List<SiacDContotesoreria> elencoSiacDContotesoreria = siacDContotesoreriaRepository.findContitesoreriaByEnte(ente.getUid());
		if(elencoSiacDContotesoreria == null) {
			return new ArrayList<ContoTesoreria>();
		}

		List<ContoTesoreria> elencoContiTesoreria = convertiLista(elencoSiacDContotesoreria, ContoTesoreria.class, BilMapId.SiacDContotesoreria_ContoTesoreria);
		return elencoContiTesoreria;
	}

	public it.csi.siac.siacfinser.model.ContoTesoreria findByUid(int uid) {
		SiacDContotesoreria siacDContotesoreria = siacDContotesoreriaRepository.findOne(uid);
		it.csi.siac.siacfinser.model.ContoTesoreria contoTesoreria = mapNotNull(siacDContotesoreria, it.csi.siac.siacfinser.model.ContoTesoreria.class, BilMapId.SiacDContotesoreria_ContoTesoreria);
		return contoTesoreria;
	}
}
