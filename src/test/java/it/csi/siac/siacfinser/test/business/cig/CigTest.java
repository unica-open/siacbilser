/**
 * SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
 * SPDX-License-Identifier: EUPL-1.2
 */
package it.csi.siac.siacfinser.test.business.cig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;

import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfinser.cig.exception.CigException;
import it.csi.siac.siacfinser.cig.helper.CigHelper;
import it.csi.siac.siacfinser.cig.utility.CigUtil;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;

/**
 * SIAC-8208
 * 
 * <p>CLasse di test per i contorlli del CIG.</p>
 * 
 * @author todescoa
 *
 */
public class CigTest extends BaseJunit4TestCase {

	@Test
	public void testCig() {
		String cig = "1545849ABF";
		
		assertTrue(cig.length() == 10);
		
		assertTrue(CigUtil.controlloCIGValido(cig));
	}

	@Test
	public void testCigErrati() {
		List<String> errati = Arrays.asList(
				"abc0123456",
				"ABC012345$",
				"02ABDFF820549825",
				"989()HCKA-"
			);
		
		for (String cig : errati) {
			assertFalse(CigUtil.controlloCIGValido(cig));
		}
	}
	
	@Test
	public void testSmartCig() {
		String cig = "ZAF520D809";
		
		assertTrue(cig.length() == 10);
		
		assertTrue(CigUtil.controlloSmartCIGValido(cig));
	}
	@Test
	public void testSmartCigErrati() {
		List<String> errati = Arrays.asList(
				"A0BH12G34K6",
				"0ABC860123",
				"02ABDFF8200000011000000",
				"V85jk21aBH"
			);
		
		for (String cig : errati) {
			assertFalse(CigUtil.controlloSmartCIGValido(cig));
		}
	}

	@Test
	public void testCigUnico() {
		String cig = "GD5241CA23";
		
		assertTrue(cig.length() == 10);
	
		assertTrue(CigUtil.controlloCIGUnicoValido(cig));
	}

	@Test
	public void testCigUniciErrati() {
		List<String> errati = Arrays.asList(
				"abc0123456",
				"ABC0123456F45SF2",
				"02ABDFF820",
				"989ù§HCKA-"
			);
		
		for (String cig : errati) {
			assertFalse(CigUtil.controlloCIGUnicoValido(cig));
		}
	}
	
	@Test
	public void testCigHelperImpegno() {
		String cig = "V00FBCA891";
		
		assertTrue(cig.length() == 10);
		
		Impegno impegno = new Impegno();
		impegno.setCig(cig);
		
		assertTrue(CigHelper.controlloCIGSuImpegno(impegno));
	}

	@Test
	public void testCigHelperLiquidazione() {
		String cig = "2015645F8A";
		
		assertTrue(cig.length() == 10);
		
		Liquidazione liquidazione = new Liquidazione();
		liquidazione.setCig(cig);
		
		assertTrue(CigHelper.controlloCIGSuLiquidazione(liquidazione));
	}

	@Test
	public void testCigHelperDocumento() {
		String cig = "U452F8810D";
		
		assertTrue(cig.length() == 10);
		
		DocumentoSpesa documento = new DocumentoSpesa();
		documento.setCig(cig);
		
		assertTrue(CigHelper.controlloCIGSuDocumento(documento));
	}

	@Test
	public void testCigHelperOrdinativo() {
		String cig = "V45D1F02AB";
		
		assertTrue(cig.length() == 10);
		
		OrdinativoPagamento ordinativo = new OrdinativoPagamento();
		ordinativo.setCig(cig);
		
		assertTrue(CigHelper.controlloCIGSuOrdinativoDiPagamento(ordinativo));
	}
	
	@Test
	public void testCigHelperThrowErroreLunghezza() {
		List<Errore> errori = new ArrayList<Errore>();

		String cig = "V45D1";
		OrdinativoPagamento ordinativo = new OrdinativoPagamento();
		ordinativo.setCig(cig);
		
		try {
			
			CigHelper.controlloCIGSuOrdinativoDiPagamento(ordinativo, true);
			
		} catch (CigException ce) {
			errori.add(ce.getErrore());
		}
		
		assertTrue(CollectionUtils.isNotEmpty(errori));
		
	}

	@Test
	public void testCigHelperThrowErroreFormato() {
		List<Errore> errori = new ArrayList<Errore>();

		String cig = "uik0123456";
		OrdinativoPagamento ordinativo = new OrdinativoPagamento();
		ordinativo.setCig(cig);
		
		try {
			
			CigHelper.controlloCIGSuOrdinativoDiPagamento(ordinativo, true);
			
		} catch (CigException ce) {
			errori.add(ce.getErrore());
		}
		
		assertTrue(CollectionUtils.isNotEmpty(errori));
	}

	@Test
	public void testCigHelperThrowErroreTipo() {
		List<Errore> errori = new ArrayList<Errore>();
		
		String cig = "0123456uhjasd";
		
		try {
			
			CigHelper.getTipoCig(cig, true);
			
		} catch (CigException ce) {
			errori.add(ce.getErrore());
		}
		
		assertTrue(CollectionUtils.isNotEmpty(errori));
	}

	@Test
	public void testCigHelperThrowErroreTipoName() {
		List<Errore> errori = new ArrayList<Errore>();
		
		String cig = "tu851a3658a";
		
		try {
			
			CigHelper.getNameTipoCig(cig, true);
			
		} catch (CigException ce) {
			errori.add(ce.getErrore());
		}
		
		assertTrue(CollectionUtils.isNotEmpty(errori));
	}
	
}
