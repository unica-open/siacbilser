/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cassaeconomale;

import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.CheckedAccountBaseService;
import it.csi.siac.siacbilser.integration.dad.CassaEconomaleDad;
import it.csi.siac.siacbilser.model.ImportiCassaEconomaleEnum;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaCassaEconomaleResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.StatoOperativoCassaEconomale;
import it.csi.siac.siaccecser.model.errore.ErroreCEC;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.Bilancio;


@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnnullaCassaEconomaleService extends CheckedAccountBaseService<AnnullaCassaEconomale, AnnullaCassaEconomaleResponse> {
	
	@Autowired 
	private CassaEconomaleDad cassaEconomaleDad;
	
	private CassaEconomale cassaEconomale;
	
	
	
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#checkServiceParam()
	 */
	@Override
	protected void checkServiceParam() throws ServiceParamError {
		checkEntita(req.getCassaEconomale(), "cassa economale");
		cassaEconomale = req.getCassaEconomale();
	}
	
	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#init()
	 */
	@Override
	protected void init() {
		cassaEconomaleDad.setEnte(ente);
		cassaEconomaleDad.setLoginOperazione(loginOperazione);
	}
	
	

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#executeService(it.csi.siac.siaccorser.model.ServiceRequest)
	 */
	@Override
	@Transactional
	public AnnullaCassaEconomaleResponse executeService(AnnullaCassaEconomale serviceRequest) {
		return super.executeService(serviceRequest);
	}

	/* (non-Javadoc)
	 * @see it.csi.siac.siaccommonser.business.service.base.BaseService#execute()
	 */
	@Override
	protected void execute() {
		checkImporti();
		cassaEconomaleDad.annullaCassaEconomale(cassaEconomale);
		cassaEconomale.setStatoOperativoCassaEconomale(StatoOperativoCassaEconomale.ANNULLATA);
		res.setCassaEconomale(cassaEconomale);
	}

	private void checkImporti() {
		
		List<Bilancio> bilanciAssociati = cassaEconomaleDad.findBilanciAssociati(cassaEconomale);
		
		for(Bilancio bilancio : bilanciAssociati){
			cassaEconomaleDad.calcolaImportiDerivatiCassaEconomale(cassaEconomale, bilancio, EnumSet.allOf(ImportiCassaEconomaleEnum.class));
			
			if(cassaEconomale.getDisponibilitaCassaContantiNotNull().signum() != 0 || cassaEconomale.getDisponibilitaCassaContoCorrenteNotNull().signum() != 0) {
				throw new BusinessException(ErroreCEC.CEC_ERR_0010.getErrore());
			}
		}
		
		
	}

	
	/*
	 * Per procedere con l’annullamento occorre verificare che gli importi conto corrente e contanti siano a 0; 
		n questo caso procedere con l’annullo e quindi aggiornare lo stato dell’elemento ad ‘Annullato’.
		Nel caso in cui gli importi siamo significativi visualizzare il messaggio <CEC_ERR_0010, Cassa  che non si può annullare.
		 > e non procedere con l’annullo dell’elemento.
	 */
}
