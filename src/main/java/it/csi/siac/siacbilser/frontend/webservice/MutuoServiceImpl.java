/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.frontend.webservice;

import javax.jws.WebService;

import it.csi.siac.siacbilser.business.service.base.BaseServiceExecutor;
import it.csi.siac.siacbilser.business.service.mutuo.AggiornaMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.AggiornaPianoAmmortamentoMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.AggiornaRipartizioneMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.AnnullaMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.AnnullaPianoAmmortamentoMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.AnnullaRipartizioneMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.EliminaAssociazioneMovimentiGestioneMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.EliminaAssociazioneProgettiMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.InserisciAssociazioneMovimentiGestioneMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.InserisciAssociazioneProgettiMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.InserisciMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.InserisciVariazioneMassivaTassoMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.InserisciVariazioneMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.LeggiPeriodiRimborsoMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.RicercaAccertamentiAssociabiliMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.RicercaAccertamentiAssociatiMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.RicercaDettaglioMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.RicercaImpegniAssociabiliMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.RicercaImpegniAssociatiMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.RicercaProgettiAssociabiliMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.RicercaSinteticaMutuoService;
import it.csi.siac.siacbilser.business.service.mutuo.report.excel.movimentogestione.MovimentiGestioneAssociatiMutuoExcelReportService;
import it.csi.siac.siacbilser.business.service.mutuo.report.excel.pianoammortamento.PianoAmmortamentoMutuoExcelReportService;
import it.csi.siac.siacbilser.business.service.mutuo.report.excel.progetto.ProgettiAssociatiMutuoExcelReportService;
import it.csi.siac.siacbilser.business.service.mutuo.report.excel.ripartizione.RipartizioneMutuoExcelReportService;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaPianoAmmortamentoMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaPianoAmmortamentoMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaRipartizioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AggiornaRipartizioneMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaPianoAmmortamentoMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaPianoAmmortamentoMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaRipartizioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.AnnullaRipartizioneMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.EliminaAssociazioneMovimentiGestioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.EliminaAssociazioneMovimentiGestioneMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.EliminaAssociazioneProgettiMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.EliminaAssociazioneProgettiMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciAssociazioneMovimentiGestioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciAssociazioneMovimentiGestioneMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciAssociazioneProgettiMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciAssociazioneProgettiMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciVariazioneMassivaTassoMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciVariazioneMassivaTassoMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciVariazioneMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.InserisciVariazioneMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.LeggiPeriodiRimborsoMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.LeggiPeriodiRimborsoMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.MovimentiGestioneAssociatiMutuoExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.MovimentiGestioneAssociatiMutuoExcelReportResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.PianoAmmortamentoMutuoExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.PianoAmmortamentoMutuoExcelReportResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.ProgettiAssociatiMutuoExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.ProgettiAssociatiMutuoExcelReportResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaAccertamentiAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaAccertamentiAssociabiliMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaAccertamentiAssociatiMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaAccertamentiAssociatiMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaDettaglioMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaImpegniAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaImpegniAssociabiliMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaImpegniAssociatiMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaImpegniAssociatiMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaProgettiAssociabiliMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaProgettiAssociabiliMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaSinteticaMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaSinteticaMutuoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RipartizioneMutuoExcelReport;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RipartizioneMutuoExcelReportResponse;
import it.csi.siac.siaccommonser.webservice.BaseWebServiceImpl;

@WebService(serviceName = "MutuoService",
portName = "MutuoServicePort",
targetNamespace = BILSvcDictionary.NAMESPACE,
endpointInterface = "it.csi.siac.siacbilser.frontend.webservice.MutuoService")
public class MutuoServiceImpl extends BaseWebServiceImpl implements MutuoService {

	@Override
	public InserisciMutuoResponse inserisciMutuo(InserisciMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciMutuoService.class, parameters);
	}

	@Override
	public AggiornaMutuoResponse aggiornaMutuo(AggiornaMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaMutuoService.class, parameters);
	}

	@Override
	public AnnullaMutuoResponse annullaMutuo(AnnullaMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaMutuoService.class, parameters);
	}

	@Override
	public RicercaSinteticaMutuoResponse ricercaSinteticaMutuo(RicercaSinteticaMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaSinteticaMutuoService.class, parameters);
	}

	@Override
	public RicercaDettaglioMutuoResponse ricercaDettaglioMutuo(RicercaDettaglioMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaDettaglioMutuoService.class, parameters);
	}
	
	@Override
	public LeggiPeriodiRimborsoMutuoResponse leggiPeriodiRimborsoMutuo(LeggiPeriodiRimborsoMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, LeggiPeriodiRimborsoMutuoService.class, parameters);
	}

	@Override
	public AggiornaPianoAmmortamentoMutuoResponse aggiornaPianoAmmortamentoMutuo(AggiornaPianoAmmortamentoMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaPianoAmmortamentoMutuoService.class, parameters);
	}

	@Override
	public InserisciVariazioneMutuoResponse inserisciVariazioneMutuo(InserisciVariazioneMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciVariazioneMutuoService.class, parameters);
	}

	@Override
	public InserisciVariazioneMassivaTassoMutuoResponse inserisciVariazioneMassivaTassoMutuo(
			InserisciVariazioneMassivaTassoMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciVariazioneMassivaTassoMutuoService.class, parameters);
	}

	@Override
	public AnnullaPianoAmmortamentoMutuoResponse annullaPianoAmmortamentoMutuo(
			AnnullaPianoAmmortamentoMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaPianoAmmortamentoMutuoService.class, parameters);
	}

	@Override
	public RicercaImpegniAssociabiliMutuoResponse ricercaImpegniAssociabiliMutuo(RicercaImpegniAssociabiliMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaImpegniAssociabiliMutuoService.class, parameters);
	}

	@Override
	public RicercaAccertamentiAssociabiliMutuoResponse ricercaAccertamentiAssociabiliMutuo(RicercaAccertamentiAssociabiliMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaAccertamentiAssociabiliMutuoService.class, parameters);
	}

	@Override
	public InserisciAssociazioneMovimentiGestioneMutuoResponse inserisciAssociazioneMovimentiGestioneMutuo(
			InserisciAssociazioneMovimentiGestioneMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciAssociazioneMovimentiGestioneMutuoService.class, parameters);

	}

	@Override
	public EliminaAssociazioneMovimentiGestioneMutuoResponse eliminaAssociazioneMovimentiGestioneMutuo(
			EliminaAssociazioneMovimentiGestioneMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaAssociazioneMovimentiGestioneMutuoService.class, parameters);
	}

	@Override
	public RicercaImpegniAssociatiMutuoResponse ricercaImpegniAssociatiMutuo(RicercaImpegniAssociatiMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaImpegniAssociatiMutuoService.class, parameters);
	}

	@Override
	public RicercaAccertamentiAssociatiMutuoResponse ricercaAccertamentiAssociatiMutuo(
			RicercaAccertamentiAssociatiMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaAccertamentiAssociatiMutuoService.class, parameters);
	}
	
	@Override
	public PianoAmmortamentoMutuoExcelReportResponse pianoAmmortamentoMutuoExcelReport(
			PianoAmmortamentoMutuoExcelReport parameters){
		return BaseServiceExecutor.execute(appCtx, PianoAmmortamentoMutuoExcelReportService.class, parameters);
	}

	@Override
	public AggiornaRipartizioneMutuoResponse aggiornaRipartizioneMutuo(AggiornaRipartizioneMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, AggiornaRipartizioneMutuoService.class, parameters);
	}

	@Override
	public AnnullaRipartizioneMutuoResponse annullaRipartizioneMutuo(AnnullaRipartizioneMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, AnnullaRipartizioneMutuoService.class, parameters);
	}
	
	@Override
	public MovimentiGestioneAssociatiMutuoExcelReportResponse movimentiGestioneAssociatiMutuoExcelReport(
			MovimentiGestioneAssociatiMutuoExcelReport parameters){
		return BaseServiceExecutor.execute(appCtx, MovimentiGestioneAssociatiMutuoExcelReportService.class, parameters);
	}

	@Override
	public RipartizioneMutuoExcelReportResponse ripartizioneMutuoExcelReport(RipartizioneMutuoExcelReport parameters) {
		return BaseServiceExecutor.execute(appCtx, RipartizioneMutuoExcelReportService.class, parameters);
	}

	@Override
	public EliminaAssociazioneProgettiMutuoResponse eliminaAssociazioneProgettiMutuo(
			EliminaAssociazioneProgettiMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, EliminaAssociazioneProgettiMutuoService.class, parameters);
	}

	@Override
	public RicercaProgettiAssociabiliMutuoResponse ricercaProgettiAssociabiliMutuo(
			RicercaProgettiAssociabiliMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, RicercaProgettiAssociabiliMutuoService.class, parameters);
	}

	@Override
	public InserisciAssociazioneProgettiMutuoResponse inserisciAssociazioneProgettiMutuo(
			InserisciAssociazioneProgettiMutuo parameters) {
		return BaseServiceExecutor.execute(appCtx, InserisciAssociazioneProgettiMutuoService.class, parameters);
	}

	@Override
	public ProgettiAssociatiMutuoExcelReportResponse progettiAssociatiMutuoExcelReport(
			ProgettiAssociatiMutuoExcelReport parameters) {
		return BaseServiceExecutor.execute(appCtx, ProgettiAssociatiMutuoExcelReportService.class, parameters);
	}
}
