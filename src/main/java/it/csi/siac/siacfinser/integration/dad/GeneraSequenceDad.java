/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siacfinser.integration.dao.movgest.SiacTProgressivoRepository;
import it.csi.siac.siacfinser.integration.entity.SiacTEnteProprietarioFin;
import it.csi.siac.siacfinser.integration.entity.SiacTProgressivoFin;


@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class GeneraSequenceDad extends BaseDadImpl {
	
	@Autowired
	SiacTProgressivoRepository siacTProgressivoRepository;
	
	/**
	 * Genera un progressivo in una transazione separata in base al contesto
	 * formato dai parametri indicati <br/>
	 * 
	 * @param type
	 *            tipo del progressivo da restituire (cui vengono concatenati
	 *            gli eventuali id, se presenti)
	 * @param idEnte
	 * @param idAmbito
	 * @param loginOperazione
	 * @param ids: id per contestualizzare in modo piu ristretto il progressivo restituito
	 * @return il progressivo già incrementato
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Long getMaxCode(ProgressivoType type, Number idEnte, Number idAmbito, String loginOperazione, Number... ids) {
		
		// formo la chiave di ricerca del progressivo
		StringBuilder keyBuilder = new StringBuilder(type.getVal());
		if (ids != null) {
			for (Number id : ids) {
				keyBuilder.append('_').append(id);
			}
		}
		String key = keyBuilder.toString();

		Long l = null;
		SiacTProgressivoFin prog = null;
		// faccio la select for update per fermare le altre sessioni
		List<SiacTProgressivoFin> lstp = siacTProgressivoRepository.findByKey(
				key, idAmbito.intValue(), idEnte.intValue());
		Date now = new Timestamp(System.currentTimeMillis());
		if (lstp == null || lstp.isEmpty()) {
			// se la select non ritorna dati, � il primo progressivo per il
			// contesto passato e quindi inserisco una nuova riga
			l = 1L;
			prog = new SiacTProgressivoFin();
			prog.setProgKey(key);
			prog.setProgValue(l);
			prog.setDataCreazione(now);
			prog.setDataInizioValidita(now);
			prog.setAmbitoId(idAmbito.intValue());
			prog.setLoginOperazione(loginOperazione);
			SiacTEnteProprietarioFin step = new SiacTEnteProprietarioFin();
			step.setUid(idEnte.intValue());
			prog.setSiacTEnteProprietario(step);
		} else {
			// se la select ritorna dati, incremento il progressivo
			prog = lstp.get(0);
			l = prog.getProgValue() + 1;
			prog.setProgValue(l);
		}
		prog.setDataModifica(now);
		// salvo i dati sul db
		siacTProgressivoRepository.saveAndFlush(prog);
		// Termino restituendo l'oggetto di ritorno:
		return l;
	}

}
