/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.cespite;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.cespiti.utility.AmmortamentoAnnuoCespiteFactory;
import it.csi.siac.siacbilser.business.utility.Utility;
import it.csi.siac.siacbilser.integration.dad.AmmortamentoAnnuoCespiteDad;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccespser.model.AmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siacgenser.model.PrimaNota;

public class AmmortamentoAnnuoCespiteMainTest extends BaseJunit4TestCase {


	@Autowired private AmmortamentoAnnuoCespiteDad ammortamentoAnnuoCespiteDad;
	
	@Test
	public void test() {
		ammortamentoAnnuoCespiteDad.caricaDatiAmmortamentoCespite(Arrays.asList(24), new Date());
	}
	
	public static void main(String[] args) {
		BigDecimal aliquotaAnnua = new BigDecimal("25");
		BigDecimal valoreAttualeBene = new BigDecimal("500");
		String codiceTipoCalcolo = "50";		
		Date dataIngressoInventario = parseDate("23/04/2018");
		Date dataAmmortamento = null;
		Date dataVariazione = null;
		
		AmmortamentoAnnuoCespite ammortamentoFinoAl2019;
		AmmortamentoAnnuoCespite ammortamentoFinoAl2022;
		
		loggaCespite(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario);
		
		System.out.println("*****************************************************************");
		System.out.println("CASO 1 ammortamento che ammorta tutto l'importo:");
		loggaCespite(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario);
		
		dataAmmortamento = parseDate("06/06/2018");
		effettuaAmmortamento(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario,
				new AmmortamentoAnnuoCespite(), dataAmmortamento, null);
		
		
		System.out.println("*****************************************************************");
		System.out.println("CASO 2: cespite ammortato in due tranches per errore dell'utente");
		loggaCespite(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario);
		dataAmmortamento = parseDate("06/06/2018");
		ammortamentoFinoAl2019 = effettuaAmmortamento(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario,
				new AmmortamentoAnnuoCespite(), dataAmmortamento, 2019);
		
		dataAmmortamento = parseDate("06/06/2019");
		ammortamentoFinoAl2022 = effettuaAmmortamento(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario,
				ammortamentoFinoAl2019, dataAmmortamento, 2022);
		
		System.out.println("*****************************************************************");
		System.out.println("CASO 3: cespite ammortato in due tranches per rivalutazione");
		loggaCespite(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario);
		dataAmmortamento = parseDate("06/06/2018");
		ammortamentoFinoAl2019 = effettuaAmmortamento(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario,
				new AmmortamentoAnnuoCespite(), dataAmmortamento, 2019);
		
		valoreAttualeBene = valoreAttualeBene.add(new BigDecimal("15"));
		dataVariazione = parseDate("23/04/2019");
		System.out.println("L'operatore effettua una rivalutazione in data" + formattaData(dataVariazione) + ". Nuovo importo attuale: " + valoreAttualeBene);
		dataAmmortamento = parseDate("06/06/2019");
		ammortamentoFinoAl2022 = effettuaAmmortamento(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario,
				ammortamentoFinoAl2019, parseDate("06/06/2019"), 2024, dataVariazione);
		
		System.out.println("*****************************************************************");
		System.out.println("CASO 4: cespite ammortato in due tranches con prime note per rivalutazione");
		loggaCespite(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario);
		dataAmmortamento = parseDate("06/06/2018");
		ammortamentoFinoAl2019 = effettuaAmmortamento(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario,
				new AmmortamentoAnnuoCespite(), dataAmmortamento, null);
		valoreAttualeBene = valoreAttualeBene.add(new BigDecimal("15"));
		
		effettuaScrittureAmmortamento(ammortamentoFinoAl2019,null);
		dataVariazione = parseDate("23/04/2019");
		System.out.println("L'operatore effettua una rivalutazione in data" + formattaData(dataVariazione) + ". Nuovo importo attuale: " + valoreAttualeBene);
		dataAmmortamento = parseDate("06/06/2019");
		
		ammortamentoFinoAl2022 = effettuaAmmortamento(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario,
				ammortamentoFinoAl2019, dataAmmortamento, 2024, dataVariazione);
		
		System.out.println("*****************************************************************");
		System.out.println("CASO 5: cespite ammortato in due tranches con prime note miste");
		loggaCespite(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario);
		dataAmmortamento = parseDate("06/06/2018");
		ammortamentoFinoAl2019 = effettuaAmmortamento(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario,
				new AmmortamentoAnnuoCespite(), dataAmmortamento, null);
		valoreAttualeBene = valoreAttualeBene.add(new BigDecimal("15"));
		
		effettuaScrittureAmmortamento(ammortamentoFinoAl2019,2);
		System.out.println("L 'operatore effettua una rivalutazione. Nuovo importo attuale: " + valoreAttualeBene);
		dataAmmortamento = parseDate("06/06/2019");
		ammortamentoFinoAl2022 = effettuaAmmortamento(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario,
				ammortamentoFinoAl2019, dataAmmortamento, 2024);
		
		System.out.println("*****************************************************************");
		System.out.println("CASO 6: cespite ammortato in due tranches per decisione dell'operatore");
		loggaCespite(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario);
		dataAmmortamento = parseDate("06/06/2018");
		Integer ultimoAnnoAmmortamento = Integer.valueOf(2020);
		ammortamentoFinoAl2019 = effettuaAmmortamento(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario,
				new AmmortamentoAnnuoCespite(), dataAmmortamento, null);
		dataAmmortamento = parseDate("06/06/2019");
		ammortamentoFinoAl2019 = effettuaAmmortamento(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario,
				ammortamentoFinoAl2019, dataAmmortamento, null);
		
	}

	/**
	 * @param ammortamentoConScritture
	 */
	private static void effettuaScrittureAmmortamento(AmmortamentoAnnuoCespite ammortamentoConScritture, Integer scrittureDaEffettuare) {
		System.out.println("Vengono create tutte le prime nota sull'ammortamento appena effettuato");
		if(ammortamentoConScritture == null || ammortamentoConScritture.getDettagliAmmortamentoAnnuoCespite() == null) {
			return;
		}
		List<DettaglioAmmortamentoAnnuoCespite> detts = ammortamentoConScritture.getDettagliAmmortamentoAnnuoCespite();
		int maxVal = scrittureDaEffettuare == null? detts.size() : scrittureDaEffettuare;
		for(int i = 0; i<maxVal; i++) {
			PrimaNota pn = new PrimaNota();
			pn.setUid(33);
			detts.get(i).setPrimaNota(pn);
		}
		
		loggaAmmortamentoCreato(ammortamentoConScritture);
	}

	private static AmmortamentoAnnuoCespite modificaAmmortamentoPrecedente(AmmortamentoAnnuoCespite ammortamentoPrecedente, Date dataAmmortamento) {
		if(ammortamentoPrecedente == null || ammortamentoPrecedente.getDettagliAmmortamentoAnnuoCespite() == null || ammortamentoPrecedente.getDettagliAmmortamentoAnnuoCespite().isEmpty()) {
			System.out.println("Non esiste nessun ammortamento precedente.");
			return null;
		}
		List<DettaglioAmmortamentoAnnuoCespite> dettaglio = new ArrayList<DettaglioAmmortamentoAnnuoCespite>();
		Integer ultimoAnnoAmmortamento = Utility.getAnno(dataAmmortamento);
		BigDecimal importoAmmortato = ammortamentoPrecedente.getImportoTotaleAmmortato();
		for(DettaglioAmmortamentoAnnuoCespite amm : ammortamentoPrecedente.getDettagliAmmortamentoAnnuoCespite()) {
			if(amm.getAnno().compareTo(ultimoAnnoAmmortamento)>=0 && amm.getPrimaNota() == null) {
				amm.setDataCancellazione(dataAmmortamento);
//				importoAmmortato.subtract(amm.getQuotaAnnuale(),MathContext.BigDec)
			}
			dettaglio.add(amm);
		}
		ammortamentoPrecedente.setDettagliAmmortamentoAnnuoCespite(dettaglio);
		return ammortamentoPrecedente;
	}

	/**
	 * @param aliquotaAnnua
	 * @param valoreAttualeBene
	 * @param codiceTipoCalcolo
	 * @param dataIngressoInventario
	 */
	private static void loggaCespite(BigDecimal aliquotaAnnua, BigDecimal valoreAttualeBene, String codiceTipoCalcolo,
			Date dataIngressoInventario) {
		System.out.println("Cespite inserito il " + 
				formattaData(dataIngressoInventario) + 
				" con valore attuale " + 
				valoreAttualeBene.toPlainString() +
				" aliquota al " + 
				aliquotaAnnua.toPlainString() + 
				" tipo calcolo " +
				codiceTipoCalcolo
		);
	}
	
	private static AmmortamentoAnnuoCespite effettuaAmmortamento(BigDecimal aliquotaAnnua, BigDecimal valoreAttualeBene,
			String codiceTipoCalcolo, Date dataIngressoInventario, AmmortamentoAnnuoCespite ammortamentoPrecedente,
			Date dataAmmortamento, Integer annoFineAmmortamento) {
		return effettuaAmmortamento(aliquotaAnnua, valoreAttualeBene,
				codiceTipoCalcolo, dataIngressoInventario, ammortamentoPrecedente,
				dataAmmortamento, annoFineAmmortamento, null);
	}

	/**
	 * @param aliquotaAnnua
	 * @param valoreAttualeBene
	 * @param codiceTipoCalcolo
	 * @param dataIngressoInventario
	 * @param ammortamentoPrecedente
	 * @param dataAmmortamento
	 * @param annoFineAmmortamento
	 */
	private static AmmortamentoAnnuoCespite effettuaAmmortamento(BigDecimal aliquotaAnnua, BigDecimal valoreAttualeBene,
			String codiceTipoCalcolo, Date dataIngressoInventario, AmmortamentoAnnuoCespite ammortamentoPrecedente,
			Date dataAmmortamento, Integer annoFineAmmortamento, Date dataVariazione) {
		System.out.println("\nL'operatore lancia un ammortamento in data: " + 
				formattaData(dataAmmortamento) + 
				" e con anno di fine: " + 
				(annoFineAmmortamento != null? annoFineAmmortamento.toString() : "null")
				);
		return creaAmmortamento(aliquotaAnnua, valoreAttualeBene, codiceTipoCalcolo, dataIngressoInventario,
				dataAmmortamento, annoFineAmmortamento, ammortamentoPrecedente, dataVariazione);
	}

	/**
	 * @param aliquotaAnnua
	 * @param valoreAttualeBene
	 * @param codiceTipoCalcolo
	 * @param dataIngressoInventario
	 * @param factory
	 * @param dataAmmortamento
	 * @param annoFineAmmortamento
	 */
	private static AmmortamentoAnnuoCespite creaAmmortamento(BigDecimal aliquotaAnnua, BigDecimal valoreAttualeBene, String codiceTipoCalcolo, Date dataIngressoInventario,
			Date dataAmmortamento, Integer annoFineAmmortamento,AmmortamentoAnnuoCespite ammortamentoPrecedente, Date dataVariazione) {
		
		if(ammortamentoPrecedente != null && ammortamentoPrecedente.getUltimoAnnoAmmortato() != null && valoreAttualeBene.compareTo(ammortamentoPrecedente.getImportoTotaleAmmortato())!=0) {
			modificaAmmortamentoPrecedente(ammortamentoPrecedente, dataAmmortamento);
		}
		AmmortamentoAnnuoCespiteFactory factory = new AmmortamentoAnnuoCespiteFactory(codiceTipoCalcolo,aliquotaAnnua,115,  dataIngressoInventario, valoreAttualeBene,  annoFineAmmortamento, dataAmmortamento);
		factory.elaboraAmmortamento( ammortamentoPrecedente);
		AmmortamentoAnnuoCespite ammortamentoCreato = factory.getTestataAmmortamento();
		List<DettaglioAmmortamentoAnnuoCespite> ll = ammortamentoCreato.getDettagliAmmortamentoAnnuoCespite() != null?  ammortamentoCreato.getDettagliAmmortamentoAnnuoCespite() 
				: new ArrayList<DettaglioAmmortamentoAnnuoCespite>();
		ll.addAll(factory.getDettagliAmmortamentoElaborati());
		ammortamentoCreato.setDettagliAmmortamentoAnnuoCespite(ll);
		ammortamentoCreato.setUid(18);
		
		loggaAmmortamentoCreato(ammortamentoCreato);
		
		return ammortamentoCreato;
	}

	private static void loggaAmmortamentoCreato(AmmortamentoAnnuoCespite ammortamentoCreato) {
		StringBuilder sb = new StringBuilder();
		if(ammortamentoCreato == null) {
			sb.append("Nessun ammortamento creato.");
			System.out.println(sb.toString());
			return;
		}
		sb.append("Ammortamento effettuato con successo.\n\tData ammortamento:")
		.append("\n\timporto totale ammortato: ")
		.append(ammortamentoCreato.getImportoTotaleAmmortato())
		.append("\n\tultimo anno: ")
		.append(ammortamentoCreato.getUltimoAnnoAmmortato())
		.append("\n\tdettaglio ammortamento: \n");
		
		sb.append("\t\t")
		.append("Anno")
		.append("\t")
		.append("quota")
		.append("\t")
		.append("prima nota ")
		.append("\t")
		.append(" data ammortamento")
		.append("\t")
		.append(" data cancellazione")
		.append("\n");
		
		for (DettaglioAmmortamentoAnnuoCespite dett : ammortamentoCreato.getDettagliAmmortamentoAnnuoCespite()) {
			sb.append("\t\t")
			.append(dett.getAnno().toString())
			.append("\t")
			.append(dett.getQuotaAnnuale().toString())
			.append("\t\t")
			.append(dett.getPrimaNota() != null? " XXXX" : " ")
			.append("\t\t")
			.append(dett.getDataAmmortamento() != null? formattaData(dett.getDataAmmortamento()) : "null")
			.append("\t\t")
			.append(dett.getDataCancellazione() != null? formattaData(dett.getDataCancellazione()) : "null")
			.append("\n");
		}
		
		System.out.println(sb.toString());
		
	}
	
	private static String  formattaData(Date data) {
		return new SimpleDateFormat("dd-MM-yyyy", Locale.ITALY).format(data);
	}
	

}
