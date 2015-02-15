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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import proto.cederj.prownloader.mvp.model.Video;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class RioOldXmlDecompiler implements RioXmlDecompiler {

    private final Document document;
    private final XPath xpath;

    public RioOldXmlDecompiler(InputStream xml) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        db.setEntityResolver(new BlankingResolver());
        this.document = db.parse(xml);
        document.getDocumentElement().normalize();

        XPathFactory xpathFactory = XPathFactory.newInstance();
        this.xpath = xpathFactory.newXPath();
    }

    @Override
    public String getTitle() throws XPathExpressionException {
        //<rio_object><obj_title>
        XPathExpression expr = xpath.compile("/rio_object/obj_title/text()");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        if (nodes.item(0) != null) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

    @Override
    public String getDescription() throws XPathExpressionException {
        //<rio_object><obj_title>
        XPathExpression expr = xpath.compile("/rio_object/obj_title/text()");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        if (nodes.item(0) != null) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

    @Override
    public String getKeywords() throws XPathExpressionException {
        //<rio_object><obj_title>
        XPathExpression expr = xpath.compile("/rio_object/obj_title/text()");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        if (nodes.item(0) != null) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

    @Override
    public  List<String> getAuthor() throws XPathExpressionException {
        //<rio_object><professor>
        XPathExpression expr = xpath.compile("/rio_object/professor/text()");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        List<String> professors = new ArrayList<>();

        if (nodes.item(0) != null) {
            professors.add(nodes.item(0).getTextContent());
        }

        return professors;
    }

    @Override
    public String getDuration() throws XPathExpressionException {
        //<rio_object><duration>
        XPathExpression expr = xpath.compile("/rio_object/duration/text()");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        if (nodes.item(0) != null) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

    @Override
    public String getThumbnailFilename() throws XPathExpressionException {
        return null;
    }

    @Override
    public String getSyncFilename() throws XPathExpressionException {
        //<rio_object><related_media><rm_item>
//            <rm_filename>Aula_013.sync</rm_filename>
//            <rm_type>sync</rm_type>

        XPathExpression expr = xpath.compile("/rio_object/related_media/rm_item");
        XPathExpression typeexpr = xpath.compile("rm_type");
        XPathExpression filenameexpr = xpath.compile("rm_filename");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            NodeList ns = (NodeList) typeexpr.evaluate(n, XPathConstants.NODESET);
            String catalog = ns.item(0).getTextContent();
            if ("sync".equalsIgnoreCase(catalog)) {
                ns = (NodeList) filenameexpr.evaluate(n, XPathConstants.NODESET);
                return ns.item(0).getTextContent();
            }
        }
        return null;
    }

    @Override
    public String getIndexFilename() throws XPathExpressionException {
        //<rio_object><related_media><rm_item><rm_item>
//            <rm_filename>Aula_013.index</rm_filename>
//            <rm_type>index</rm_type>

        XPathExpression expr = xpath.compile("/rio_object/related_media/rm_item");
        XPathExpression typeexpr = xpath.compile("rm_type");
        XPathExpression filenameexpr = xpath.compile("rm_filename");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            NodeList ns = (NodeList) typeexpr.evaluate(n, XPathConstants.NODESET);
            String catalog = ns.item(0).getTextContent();
            if ("index".equalsIgnoreCase(catalog)) {
                ns = (NodeList) filenameexpr.evaluate(n, XPathConstants.NODESET);
                return ns.item(0).getTextContent();
            }
        }
        return null;
    }

    @Override
    public List<Video> getRelatedVideos() throws XPathExpressionException {
        //<rio_object><related_media><rm_item>
//            <rm_filename>Aula_013.flv</rm_filename>
//            <rm_type>video</rm_type>

        XPathExpression expr = xpath.compile("/rio_object/related_media/rm_item");
        XPathExpression typeexpr = xpath.compile("rm_type");
        XPathExpression filenameexpr = xpath.compile("rm_filename");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

        List<Video> videos = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node n = nodes.item(i);
            NodeList ns = (NodeList) typeexpr.evaluate(n, XPathConstants.NODESET);
            String catalog = ns.item(0).getTextContent();

            if (!"video".equalsIgnoreCase(catalog)) {
                continue;
            }

            ns = (NodeList) filenameexpr.evaluate(n, XPathConstants.NODESET);
            String filename = ns.item(0).getTextContent();
            String resolution = getResolution();
            String aspectRatio = getAspectRatio();
            String bitrate = getBitrate();
            Video video = new Video(filename, resolution, aspectRatio, bitrate);
            videos.add(video);

        }
        return videos;
    }

    @Override
    public int getSourceVersion() {
        return 0;
    }

    private String getBitrate() throws XPathExpressionException {
        //<rio_object><bitrate>
        XPathExpression expr = xpath.compile("/rio_object/bitrate/text()");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        if (nodes.item(0) != null) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

    private String getResolution() throws XPathExpressionException {
        //<rio_object><resolution>
//        <r_x>320</r_x>
//        <r_y>240</r_y>
        XPathExpression expr = xpath.compile("/rio_object/resolution");
        XPathExpression rxexpr = xpath.compile("r_x");
        XPathExpression ryexpr = xpath.compile("r_y");
        NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
        Node n = nodes.item(0);

        NodeList ns = (NodeList) rxexpr.evaluate(n, XPathConstants.NODESET);
        String rx = ns.item(0).getTextContent();
        ns = (NodeList) ryexpr.evaluate(n, XPathConstants.NODESET);
        String ry = ns.item(0).getTextContent();

        return rx + "x" + ry;

    }

    private String getAspectRatio() {
        return "4:3";
    }

    public class BlankingResolver implements EntityResolver {

        public InputSource resolveEntity(String arg0, String arg1) throws SAXException, IOException {
            return new InputSource(new ByteArrayInputStream("".getBytes()));
        }
    }
}
