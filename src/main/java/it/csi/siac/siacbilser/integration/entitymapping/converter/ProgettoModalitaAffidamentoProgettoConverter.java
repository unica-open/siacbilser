/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacDProgrammaAffidamentoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDProgrammaAffidamento;
import it.csi.siac.siacbilser.integration.entity.SiacTProgramma;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.ModalitaAffidamentoProgetto;
import it.csi.siac.siacbilser.model.Progetto;

// TODO: Auto-generated Javadoc
/**
 * Converter per gli Attributi tra Progetto e SiacTProgramma.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/02/2014
 *
 */
@Component
public class ProgettoModalitaAffidamentoProgettoConverter extends ExtendedDozerConverter<Progetto, SiacTProgramma> {
	
	@Autowired 
	private SiacDProgrammaAffidamentoRepository siacDProgrammaAffidamentoRepository;
	
	/**
	 * Instantiates a new progetto attr converter.
	 */
	public ProgettoModalitaAffidamentoProgettoConverter() {
		super(Progetto.class, SiacTProgramma.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Progetto convertFrom(SiacTProgramma src, Progetto dest) {
		
		dest.setModalitaAffidamentoProgetto(mapNotNull(src.getSiacDProgrammaAffidamento(), ModalitaAffidamentoProgetto.class, BilMapId.SiacDProgrammaAffidamento_ModalitaAffidamentoProgetto));
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTProgramma convertTo(Progetto src, SiacTProgramma dest) {	
		if(src.getModalitaAffidamentoProgetto() == null) {
			return dest;
		}
		SiacDProgrammaAffidamento siacDProgrammaAffidamento = null;
		if(src.getModalitaAffidamentoProgetto().getUid() != 0) {
			siacDProgrammaAffidamento = siacDProgrammaAffidamentoRepository.findOne(src.getModalitaAffidamentoProgetto().getUid());
		} else if(StringUtils.isNotBlank(src.getModalitaAffidamentoProgetto().getCodice()) && src.getEnte() != null && src.getEnte().getUid() != 0) {
			siacDProgrammaAffidamento = siacDProgrammaAffidamentoRepository.findByCode(src.getModalitaAffidamentoProgetto().getCodice(), src.getEnte().getUid());
		}
		dest.setSiacDProgrammaAffidamento(siacDProgrammaAffidamento);
		return dest;
	}
	
	

}
