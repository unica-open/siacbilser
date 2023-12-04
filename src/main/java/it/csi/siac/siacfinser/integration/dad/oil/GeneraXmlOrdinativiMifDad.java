/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.dad.oil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siaccommonser.integration.dad.base.BaseDadImpl;
import it.csi.siac.siacfinser.integration.dao.oil.OrdinativoMifDao;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class GeneraXmlOrdinativiMifDad extends BaseDadImpl
{
	@Autowired
	private OrdinativoMifDao ordinativoMifDao;
	
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n";
	private static final String INDENT_CHAR = " ";
	private static final int INDENT_SIZE = 4;

	public String createXml(Integer idEnte, Integer idElaborazione, Integer annoEsercizio, String codiceIstat, Integer limitOrdinativi,
			Integer offsetOrdinativi)
	{
		Map<String, Object> dataMap = new HashMap<String, Object>();

		dataMap.put("ente_proprietario_id", idEnte);
		dataMap.put("limitOrdinativi", limitOrdinativi);
		dataMap.put("offsetOrdinativi", offsetOrdinativi);
		dataMap.put("mif_ord_flusso_elab_mif_id", idElaborazione);
		
		if (annoEsercizio != null) {
			dataMap.put("mif_ord_anno_esercizio", annoEsercizio);
		}
		
		if (codiceIstat != null) {
			dataMap.put("mif_ord_codice_ente_istat", codiceIstat);
		}
		
		List<Node> nodes = readXmlNodes(idElaborazione);

		StringBuilder sb = new StringBuilder(XML_HEADER);

		sb.append(nodesToXml(nodes, dataMap, 0));

		return sb.toString();
	}

	private List<Node> readXmlNodes(int idElaborazione)
	{
		final String ROOT_KEY = "";

		List<Map<String, Object>> strutturaXml = ordinativoMifDao.leggiStrutturaXml(idElaborazione);

		Map<String, Node> tmpStruttura = new HashMap<String, Node>();
		tmpStruttura.put(ROOT_KEY, new Node(ROOT_KEY));

		for (Map<String, Object> map : strutturaXml)
		{
			Node node = new Node();

			node.setTag((String) map.get("flusso_elab_mif_code"));
			node.setQuery((String) map.get("flusso_elab_mif_query"));
			node.setField((String) map.get("flusso_elab_mif_campo"));
			node.setConstant((String) map.get("flusso_elab_mif_default"));
			node.setVisible((Boolean) map.get("flusso_elab_mif_xml_out"));

			String codePadre = (String) map.get("flusso_elab_mif_code_padre");
			String key = (codePadre == null ? "" : codePadre + ".") + node.getTag();
			tmpStruttura.put(key, node);

			Node parentNode = tmpStruttura.get(StringUtils.defaultString(codePadre, ROOT_KEY));

			if (parentNode == null)
				throw new IllegalStateException(String.format("Nodo padre %s non definito", codePadre));

			parentNode.addNode(node);
		}

		return tmpStruttura.get(ROOT_KEY).getNodes();
	}

	private StringBuilder nodesToXml(List<Node> nodes, Map<String, Object> dataMap, int level)
	{
		StringBuilder sb = new StringBuilder();

		String indent = getIndent(level);

		for (Node node : nodes)
		{
			log.debug("NODE", node.getTag());

			String content = getContent(dataMap, node, level);

			// logDebug(" content", content);

			if (StringUtils.isNotBlank(content))
			{
				appendIfNodeVisible(node, sb, indent);
				appendIfNodeVisible(node, sb, node.openString());
				sb.append(content);
				appendIfNodeVisible(node, sb, node.closeString());
				appendIfNodeVisible(node, sb, "\n");
			}
		}

		return sb;
	}

	private void appendIfNodeVisible(Node node, StringBuilder sb, String str)
	{
		if (node.isVisible())
			sb.append(str);
	}

	private String getContent(Map<String, Object> dataMap, Node node, int level)
	{
		if (node.isVisible() && node.isLeaf())
			return getLeafContent(dataMap, node);

		if (!node.isVisible())
			level--;

		if (StringUtils.isNotBlank(node.getQuery()))
			return getQueryContent(dataMap, node, level);

		return getNodesContent(dataMap, node, level);
	}

	private String getLeafContent(Map<String, Object> dataMap, Node node)
	{
		if (StringUtils.isNotBlank(node.getField()))
			return getFieldContent(dataMap, node.getField());

		if (StringUtils.isNotBlank(node.getConstant()))
			return getConstantContent(node.getConstant());

		if (StringUtils.isNotBlank(node.getQuery()))
			return getSingleFieldContent(execQuery(node.getQuery(), dataMap));

		return null;
	}

	private String getNodesContent(Map<String, Object> dataMap, Node node, int level)
	{
		StringBuilder sb = new StringBuilder();

		sb.append("\n");
		sb.append(nodesToXml(node.getNodes(), dataMap, level + 1));
		sb.append(getIndent(level));

		return sb.toString();
	}

	private String getIndent(int level)
	{
		return StringUtils.repeat(INDENT_CHAR, level * INDENT_SIZE);
	}

	private String getQueryContent(Map<String, Object> dataMap, Node node, int level)
	{
		StringBuilder sb = new StringBuilder();

		List<Map<String, Object>> resultSet = execQuery(node.getQuery(), dataMap);

		if (node.isVisible() && !resultSet.isEmpty())
			sb.append("\n");

		for (Map<String, Object> data : resultSet)
		{
			if (dataMap != null)
				putAllPreserve(data, dataMap);
			sb.append(nodesToXml(node.getNodes(), data, level + 1));
		}

		sb.append(getIndent(level));

		return sb.toString();
	}

	private String getSingleFieldContent(List<Map<String, Object>> resultSet)
	{
		if (resultSet.isEmpty())
			return null;

		Map<String, Object> dataMap = resultSet.get(0);

		String field = dataMap.keySet().iterator().next();

		return getFieldContent(dataMap, field);
	}

	private String getFieldContent(Map<String, Object> dataMap, String field)
	{
		String content = StringEscapeUtils.escapeXml(String.valueOf(ObjectUtils.defaultIfNull(dataMap.get(field), "")));

		log.debug("		fieldContent", content);

		return content;
	}

	private String getConstantContent(String constant)
	{
		return StringEscapeUtils.escapeXml(StringUtils.defaultIfEmpty(constant, ""));
	}

	private List<Map<String, Object>> execQuery(String query, Map<String, Object> dataMap)
	{
		log.debug("QUERY", query);
		log.debug("PARAMS", dataMap.toString());

		if (query == null)
			return null;

		List<Map<String, Object>> results = ordinativoMifDao.queryForList(query, dataMap);

		log.debug("RESULTS", results.toString());

		return results;
	}

	private void putAllPreserve(Map<String, Object> data, Map<String, Object> dataMap)
	{
		if (dataMap.isEmpty())
			return;

		for (Map.Entry<String, Object> entry : dataMap.entrySet())
			if (!data.containsKey(entry.getKey()))
				data.put(entry.getKey(), entry.getValue());
	}
}
