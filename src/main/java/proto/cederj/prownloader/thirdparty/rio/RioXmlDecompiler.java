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

import java.util.List;
import javax.xml.xpath.XPathExpressionException;
import proto.cederj.prownloader.model.Video;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public interface RioXmlDecompiler {

    String getAuthor() throws XPathExpressionException;

    String getDescription() throws XPathExpressionException;

    String getDuration() throws XPathExpressionException;

    String getIndexFilename() throws XPathExpressionException;

    String getKeywords() throws XPathExpressionException;

    List<Video> getRelatedVideos() throws XPathExpressionException;

    String getSyncFilename() throws XPathExpressionException;

    String getThumbnailFilename() throws XPathExpressionException;

    String getTitle() throws XPathExpressionException;
    
}