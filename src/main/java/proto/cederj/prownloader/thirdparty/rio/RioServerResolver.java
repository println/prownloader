/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proto.cederj.prownloader.thirdparty.rio;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import proto.cederj.prownloader.thirdparty.pseudohttpclient.PseudoHttpClient;

/**
 *
 * @author Proto
 */
public class RioServerResolver {

    private final PseudoHttpClient client;
    private final String redirector;
    private final String transfer;
    private final String xmlTagName;

    public RioServerResolver(PseudoHttpClient client, String redirector, String transfer, String xmlTagName) {
        this.client = client;
        this.redirector = redirector;
        this.transfer = transfer;
        this.xmlTagName = xmlTagName;
    }

    public String getDefaultXml(String link) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        URL url = new URL(link);
        String host = resolveHost(url);
        String mirror = resolveMirror(host);
        String xmlUrl = mirror + resolveData(url);
        return xmlUrl;
    }

    public String getFile(String link, String filename) throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
        URL url = new URL(link);
        String host = resolveHost(url);
        String mirror = resolveMirror(host);
        String xmlUrl = mirror + resolveData(url, filename);
        return xmlUrl;
    }

    private String resolveHost(URL url) {
        String link = url.toString();
        String host = link.split(url.getPath())[0];
        return host;
    }

    private String resolveData(URL url) {
        String data = url.getQuery().split("=")[1];
        return data;
    }

    private String resolveData(URL url, String filename) {
        String data = resolveData(url);
        int index = data.lastIndexOf("/");
        String filedata = data.substring(0, index + 1) + filename;
        return filedata;
    }

    private String resolveMirror(String host) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputStream inputStream = client.doGet(host + redirector);
        Document doc = db.parse(inputStream);
        doc.getDocumentElement().normalize();
        NodeList nodeLst = doc.getElementsByTagName(xmlTagName);
        Node node = nodeLst.item(0);
        String server = node.getTextContent() + transfer;
        return server;
    }
}
