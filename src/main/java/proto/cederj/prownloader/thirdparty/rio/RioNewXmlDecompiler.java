/*
 * Copyright 2015 Felipe Santos <live.proto at hotmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package proto.cederj.prownloader.thirdparty.rio;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import proto.cederj.prownloader.mvp.model.Video;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class RioNewXmlDecompiler implements RioXmlDecompiler {

    private final Document document;
    private final XPath xpath;

    public RioNewXmlDecompiler(InputStream xml) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        this.document = db.parse(xml);
        document.getDocumentElement().normalize();

        XPathFactory xpathFactory = XPathFactory.newInstance();
        this.xpath = xpathFactory.newXPath();
    }

    @Override
    public String getTitle() throws XPathExpressionException {
        //<OBAA_Videoaula><general><title><string>
        XPathExpression expr = xpath.compile("/OBAA_Videoaula/general/title/string/text()");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        if (nodes.item(0) != null) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

    @Override
    public String getDescription() throws XPathExpressionException {
        //<OBAA_Videoaula><general><description><string>
        XPathExpression expr = xpath.compile("/OBAA_Videoaula/general/description/string/text()");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        if (nodes.item(0) != null) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

    @Override
    public String getKeywords() throws XPathExpressionException {
        //<OBAA_Videoaula><general><keyword><string>
        XPathExpression expr = xpath.compile("/OBAA_Videoaula/general/keyword/string/text()");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        if (nodes.item(0) != null) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

    @Override
    public List<String> getAuthor() throws XPathExpressionException {
        //<OBAA_Videoaula><lifecycle><contribute><role>author</role><entity>
        XPathExpression expr = xpath.compile("/OBAA_Videoaula/lifecycle/contribute");
        XPathExpression roleexpr = xpath.compile("role");
        XPathExpression entityexpr = xpath.compile("entity");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        List<String> professors = new ArrayList<>();

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            NodeList ns = (NodeList) roleexpr.evaluate(n, XPathConstants.NODESET);
            String role = ns.item(0).getTextContent();
            if ("author".equalsIgnoreCase(role)) {
                ns = (NodeList) entityexpr.evaluate(n, XPathConstants.NODESET);
                String entity = ns.item(0).getTextContent();
                if (entity != null && !entity.toLowerCase().contains("cederj")) {
                    entity = entity.replace("BEGIN:VCARD\\nFN:", "").replace("END:VCARD\\n", "");
                    professors.add(entity);
                }
            }
        }
        return professors;
    }

    @Override
    public String getDuration() throws XPathExpressionException {
        //<OBAA_Videoaula><technical><duration>
        XPathExpression expr = xpath.compile("/OBAA_Videoaula/technical/duration/text()");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        if (nodes.item(0) != null) {
            String durationString = nodes.item(0).getTextContent();
            try {
                
                Duration duration = DatatypeFactory.newInstance().newDuration(durationString);
               
                
                return ""+String.format("%02d", duration.getHours())+":"+String.format("%02d", duration.getMinutes())+":"+String.format("%02d", duration.getSeconds());
            } catch (DatatypeConfigurationException ex) {
                Logger.getLogger(RioNewXmlDecompiler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    public String getThumbnailFilename() throws XPathExpressionException {
        //<OBAA_Videoaula><videoaula><technical><thumbnail><entry> 
        XPathExpression expr = xpath.compile("/OBAA_Videoaula/videoaula/technical/thumbnail/entry/text()");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        if (nodes.item(0) != null) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

    @Override
    public String getSyncFilename() throws XPathExpressionException {
        //<OBAA_Videoaula><videoaula><technical><relatedmedia><catalog>sync</catalog><entry>  
        XPathExpression expr = xpath.compile("/OBAA_Videoaula/videoaula/technical/relatedmedia");
        XPathExpression catalogexpr = xpath.compile("catalog");
        XPathExpression entityexpr = xpath.compile("entry");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            NodeList ns = (NodeList) catalogexpr.evaluate(n, XPathConstants.NODESET);
            String catalog = ns.item(0).getTextContent();
            if ("sync".equalsIgnoreCase(catalog)) {
                ns = (NodeList) entityexpr.evaluate(n, XPathConstants.NODESET);
                return ns.item(0).getTextContent();
            }
        }
        return null;
    }

    @Override
    public String getIndexFilename() throws XPathExpressionException {
        //<OBAA_Videoaula><videoaula><technical><relatedmedia><catalog>index</catalog><entry>
        XPathExpression expr = xpath.compile("/OBAA_Videoaula/videoaula/technical/relatedmedia");
        XPathExpression catalogexpr = xpath.compile("catalog");
        XPathExpression entityexpr = xpath.compile("entry");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            NodeList ns = (NodeList) catalogexpr.evaluate(n, XPathConstants.NODESET);
            String catalog = ns.item(0).getTextContent();
            if ("index".equalsIgnoreCase(catalog)) {
                ns = (NodeList) entityexpr.evaluate(n, XPathConstants.NODESET);
                return ns.item(0).getTextContent();
            }
        }
        return null;
    }

    @Override
    public List<Video> getRelatedVideos() throws XPathExpressionException {
        //<OBAA_Videoaula><videoaula><technical><relatedmedia><catalog>video</catalog>
//        <catalog>video</catalog>
//        <entry>Aula_001.mp4</entry>
//        <resolution>320x240</resolution>
//        <aspectratio>4:3</aspectratio>
//        <bitrate>70.00</bitrate>        

        XPathExpression expr = xpath.compile("/OBAA_Videoaula/videoaula/technical/relatedmedia");
        XPathExpression catalogexpr = xpath.compile("catalog");
        XPathExpression entityexpr = xpath.compile("entry");
        XPathExpression resolutionExp = xpath.compile("resolution");
        XPathExpression aspectratioExp = xpath.compile("aspectratio");
        XPathExpression bitrateExp = xpath.compile("bitrate");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        List<Video> videos = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            NodeList ns = (NodeList) catalogexpr.evaluate(n, XPathConstants.NODESET);
            String catalog = ns.item(0).getTextContent();

            if (!"video".equalsIgnoreCase(catalog)) {
                continue;
            }

            ns = (NodeList) entityexpr.evaluate(n, XPathConstants.NODESET);
            String filename = ns.item(0).getTextContent();
            ns = (NodeList) resolutionExp.evaluate(n, XPathConstants.NODESET);
            String resolution = ns.item(0).getTextContent();
            ns = (NodeList) aspectratioExp.evaluate(n, XPathConstants.NODESET);
            String aspectRatio = ns.item(0).getTextContent();
            ns = (NodeList) bitrateExp.evaluate(n, XPathConstants.NODESET);
            String bitrate = ns.item(0).getTextContent();
            Video video = new Video(filename, resolution, aspectRatio, bitrate);
            videos.add(video);

        }
        return videos;
    }

    @Override
    public int getSourceVersion() {
        return 1;
    }
}
