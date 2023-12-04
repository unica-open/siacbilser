/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.mutuo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.business.service.stampa.allegatoatto.model.StampaAllegatoAttoImpegno;
import it.csi.siac.siacbilser.business.utility.mutuo.MutuoUtil;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaRipartizioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaRipartizioneMutuoResponse;
import it.csi.siac.siacbilser.integration.dad.CapitoloDad;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.mutuo.RipartizioneMutuo;
import it.csi.siac.siacbilser.model.mutuo.TipoRipartizioneMutuo;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siaccommon.util.collections.Function;
import it.csi.siac.siaccommon.util.collections.Reductor;
import it.csi.siac.siaccommon.util.collections.filter.NotNullFilter;
import it.csi.siac.siaccommon.util.number.BigDecimalUtil;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AggiornaRipartizioneMutuoService extends BaseAggiornaAnnullaRipartizioneMutuoService<AggiornaRipartizioneMutuo, AggiornaRipartizioneMutuoResponse> {
	
	@Autowired CapitoloDad capitoloDad;
	
	@Override
	@Transactional
	public AggiornaRipartizioneMutuoResponse executeService(AggiornaRipartizioneMutuo serviceRequest) {
		return super.executeService(serviceRequest);
	}
	
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		super.checkServiceParam();
		checkNotNull(mutuo.getElencoRipartizioneMutuo(), ErroreCore.ENTITA_NON_COMPLETA.getErrore("mutuo", "elenco ripartizione null"));
		checkCondition(!mutuo.getElencoRipartizioneMutuo().isEmpty(), ErroreCore.ENTITA_NON_COMPLETA.getErrore("mutuo", "elenco ripartizione vuoto"));
	}

	@Override
	protected void execute() {
		
		super.execute();

		checkRipartizioneCapitale();
		checkRipartizioneInteressi();
		
		mutuoDad.salvaRipartizioneMutuo(mutuo);

	}
	
	private void checkRipartizioneCapitale() {
		
		List<RipartizioneMutuo> ripartizioneMutuoCapitaleList = getRipartizioneMutuoListFiltered(TipoRipartizioneMutuo.Capitale);
		
		checkBusinessCondition(!ripartizioneMutuoCapitaleList.isEmpty() && ripartizioneMutuoCapitaleList.size()==1, ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("e' ammessa una e una sola ripartizione di tipo Capitale"));
		checkBusinessCondition(ripartizioneMutuoCapitaleList.get(0).getRipartizioneImporto().compareTo(mutuoCorrente.getSommaMutuataIniziale())==0, ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'importo della ripartizione di tipo Capitale deve coincidere con l'importo del mutuo"));
		checkBusinessCondition(ripartizioneMutuoCapitaleList.get(0).getRipartizionePercentuale().compareTo(new BigDecimal(100))==0, ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("l'importo % della ripartizione di tipo Capitale deve essere pari al 100% "));
	}
	private void checkRipartizioneInteressi() {
		
		List<RipartizioneMutuo> ripartizioneMutuoInteressiList = getRipartizioneMutuoListFiltered(TipoRipartizioneMutuo.Interessi);
		
		BigDecimal totaleRipartizioneImporto = CollectionUtil.reduce(ripartizioneMutuoInteressiList, new Reductor<RipartizioneMutuo, BigDecimal>() {
			@Override
			public BigDecimal reduce(BigDecimal accumulator, RipartizioneMutuo current, int index) {
				return accumulator.add(BigDecimalUtil.getDefaultZero(current.getRipartizioneImporto()));
			}
		}, BigDecimal.ZERO);
		
		BigDecimal totaleRipartizionePercentuale = CollectionUtil.reduce(ripartizioneMutuoInteressiList, new Reductor<RipartizioneMutuo, BigDecimal>() {
			@Override
			public BigDecimal reduce(BigDecimal accumulator, RipartizioneMutuo current, int index) {
				return accumulator.add(BigDecimalUtil.getDefaultZero(current.getRipartizionePercentuale()));
			}
		}, BigDecimal.ZERO);
		
		checkBusinessCondition(totaleRipartizioneImporto.compareTo(mutuoCorrente.getSommaMutuataIniziale())==0, ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("la somma degli importi delle ripartizioni di tipo Interessi deve coincidere con l'importo del mutuo"));
		checkBusinessCondition(totaleRipartizionePercentuale.compareTo(new BigDecimal(100))==0, ErroreCore.AGGIORNAMENTO_NON_POSSIBILE.getErrore("la somma delgli importi % delle ripartizioni di tipo Interesse deve essere pari al 100% "));
		
		checkBusinessCondition(
				CollectionUtil.getSize(
					CollectionUtil.distinct(	
							ripartizioneMutuoInteressiList,
							new Function<RipartizioneMutuo, String>() {
								@Override
								public String map(RipartizioneMutuo source) {
									return String.valueOf(source.getCapitolo().getUid());
								}
							}
						)) == CollectionUtil.getSize(ripartizioneMutuoInteressiList) , ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("non e' possibile inserire piu' ripartizioni di tipo Interesse sullo stesso capitolo "));
	}
	
	private List<RipartizioneMutuo> getRipartizioneMutuoListFiltered (TipoRipartizioneMutuo tipoRipartizioneMutuo){
		return MutuoUtil.getRipartizioneMutuoListFiltered(mutuo.getElencoRipartizioneMutuo(), tipoRipartizioneMutuo);
	}
	
	
	/*
	private void checkCapitolo() {
		for(RipartizioneMutuo ripartizioneMutuo: mutuo.getElencoRipartizioneMutuo()) {
			if (ripartizioneMutuo.getCapitolo().getUid() == 0) {
				
				capitoloDad.ricercaIdCapitoliPerChiave(TipoCapitolo.CAPITOLO_USCITA_GESTIONE, ripartizioneMutuo.getCapitolo().getAnnoCapitolo(),
						ripartizioneMutuo.getCapitolo().getNumeroCapitolo(), ripartizioneMutuo.getCapitolo().getNumeroArticolo(), 1);
				
				AttoAmministrativo attoAmministrativo =  attoAmministrativoDad.findAttoAmministrativoByLogicKey(mutuo.getAttoAmministrativo());
				checkBusinessCondition(attoAmministrativo != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("attoAmministrativo", mutuo.getAttoAmministrativo().getDescrizioneCompleta()) );
				checkBusinessCondition(attoAmministrativo.isEntitaValida(), ErroreCore.VALORE_NON_VALIDO.getErrore("attoAmministrativo", attoAmministrativo.getDescrizioneCompleta()));
				mutuo.setAttoAmministrativo(attoAmministrativo);
			}
			
		}
	}	*/
}
