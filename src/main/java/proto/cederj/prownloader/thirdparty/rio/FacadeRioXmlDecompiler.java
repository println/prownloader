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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;
import proto.cederj.prownloader.model.Video;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class FacadeRioXmlDecompiler implements RioXmlDecompiler {

    private RioXmlDecompiler decompiler;

    public FacadeRioXmlDecompiler(InputStream xmlInputStream) throws ParserConfigurationException, SAXException, IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(xmlInputStream);
        try {
            bufferedInputStream.mark(0);
            this.decompiler = new RioNewXmlDecompiler(bufferedInputStream);
        } catch (IOException ex) {
            bufferedInputStream.reset();
            this.decompiler = new RioOldXmlDecompiler(bufferedInputStream);
        }
    }

    @Override
    public String getTitle() throws XPathExpressionException {
        return decompiler.getTitle();
    }

    @Override
    public String getDescription() throws XPathExpressionException {
        return decompiler.getDescription();
    }

    @Override
    public String getKeywords() throws XPathExpressionException {
        return decompiler.getKeywords();
    }

    @Override
    public String getAuthor() throws XPathExpressionException {
        return decompiler.getTitle();
    }

    @Override
    public String getDuration() throws XPathExpressionException {
        return decompiler.getDuration();
    }

    @Override
    public String getThumbnailFilename() throws XPathExpressionException {
        return decompiler.getThumbnailFilename();
    }

    @Override
    public String getSyncFilename() throws XPathExpressionException {
        return decompiler.getTitle();
    }

    @Override
    public List<Video> getRelatedVideos() throws XPathExpressionException {
        return decompiler.getRelatedVideos();
    }

    @Override
    public String getIndexFilename() throws XPathExpressionException {
        return decompiler.getIndexFilename();
    }
}
