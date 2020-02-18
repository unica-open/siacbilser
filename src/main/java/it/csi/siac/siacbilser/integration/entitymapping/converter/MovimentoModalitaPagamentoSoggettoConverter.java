/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRMovimentoModpag;
import it.csi.siac.siacbilser.integration.entity.SiacTModpag;
import it.csi.siac.siacbilser.integration.entity.SiacTMovimento;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siacfinser.integration.helper.ModalitaPagamentoSoggettoHelper;
import it.csi.siac.siacfinser.model.soggetto.modpag.DescrizioneInfoModPagSog;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * The Class MovimentoModalitaPagamentoConverter.
 */
@Component
public class MovimentoModalitaPagamentoSoggettoConverter extends ExtendedDozerConverter<Movimento, SiacTMovimento> {
	
	@Autowired
	private ApplicationContext applicationContext;
	
	private ModalitaPagamentoSoggettoHelper mpsHelper;
	
	/**
	 * Instantiates a new subdocumento Entrata subdocumento iva converter.
	 */
	public MovimentoModalitaPagamentoSoggettoConverter() {
		super(Movimento.class, SiacTMovimento.class);
	}

	@Override
	public Movimento convertFrom(SiacTMovimento src, Movimento dest) {
		if(src.getSiacRMovimentoModpags() == null || src.getSiacRMovimentoModpags().isEmpty()){
			return dest;
		}
		for(SiacRMovimentoModpag siacRMovimentoModpag : src.getSiacRMovimentoModpags()){
			if(siacRMovimentoModpag.getDataCancellazione() == null){
				ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = convertiModalitaPagamento(siacRMovimentoModpag.getSiacTModpag());
				dest.setModalitaPagamentoSoggetto(modalitaPagamentoSoggetto );
				break;
			}
		}
		return dest;
	}

	private ModalitaPagamentoSoggetto convertiModalitaPagamento(SiacTModpag siacTModpag) {
		ModalitaPagamentoSoggetto modalitaPagamentoSoggetto = map(siacTModpag, ModalitaPagamentoSoggetto.class, BilMapId.SiacTModpag_ModalitaPagamentoSoggetto);
		
		// SIAC-5156: completo i dati della MPS
		// Inizializzazione lazy: se non ho i dati della MPS non inizializzo alcunche'; riutilizzo inoltre la stessa istanza
		if(mpsHelper == null) {
			mpsHelper = new ModalitaPagamentoSoggettoHelper(applicationContext);
			mpsHelper.init();
		}
		
		DescrizioneInfoModPagSog descrizioneInfoModPagSog = mpsHelper.componiDescrizioneArricchitaModalitaPagamento(modalitaPagamentoSoggetto, null, siacTModpag.getSiacTEnteProprietario().getEnteProprietarioId());
		modalitaPagamentoSoggetto.setDescrizioneInfo(descrizioneInfoModPagSog);
		
		return modalitaPagamentoSoggetto;
	}

	@Override
	public SiacTMovimento convertTo(Movimento src, SiacTMovimento dest) {
		if(src.getModalitaPagamentoSoggetto() == null || src.getModalitaPagamentoSoggetto().getUid()==0){
			return dest;
		}
		
		SiacRMovimentoModpag siacRMovimentoModpag = new SiacRMovimentoModpag();
		SiacTModpag siacTModpag = map(src.getModalitaPagamentoSoggetto(), SiacTModpag.class, BilMapId.SiacTModpag_ModalitaPagamentoSoggetto);
		
		siacRMovimentoModpag.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRMovimentoModpag.setSiacTMovimento(dest);
		siacRMovimentoModpag.setSiacTModpag(siacTModpag);
		siacRMovimentoModpag.setLoginOperazione(src.getLoginOperazione());
		
		List<SiacRMovimentoModpag> siacRMovimentoModpags = new ArrayList<SiacRMovimentoModpag>();
		siacRMovimentoModpags.add(siacRMovimentoModpag);
		dest.setSiacRMovimentoModpags(siacRMovimentoModpags);
		return dest;
	}



	

}
