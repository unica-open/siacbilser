/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business;

import java.math.BigDecimal;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BigDecimalAdapter extends XmlAdapter<String, BigDecimal> {

	@Override
    public String marshal(BigDecimal value) throws Exception  {
        if (value != null) {
            return value.toString() + " euro";
        }
        return null;
    }

	@Override
	public BigDecimal unmarshal(String s) throws Exception {
		return new BigDecimal(s);
	}
	
}
