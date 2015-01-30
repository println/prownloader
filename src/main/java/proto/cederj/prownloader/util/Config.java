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
package proto.cederj.prownloader.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class Config {

    private final String CONFIG_FILE = "config.xml";
    private final String PROPERTY_USERAGENT = "app.useragent";
    private final String PROPERTY_TRACKER = "cederj.tracker";
    private final String PROPERTY_TRACKER_ELEMENT = "cederj.tracker.document.source.element";
    private final String PROPERTY_RIO_REDIRECT = "rio.resource.redirect";
    private final String PROPERTY_RIO_TRANSFER = "rio.resource.transfer";
    private final String PROPERTY_RIO_XML_TAG = "rio.server.download.mirror.xml.tagname";

    private String userAgent;
    private String tracker;
    private String trackerElement;
    private String rioRedirect;
    private String rioTransfer;
    private String rioServerMirrorXmlTagname;

    public Config() {
        initialize();
    }

    private void initialize() {
        Properties properties = null;
        try {
            properties = readFile();
        } catch (Exception e) {
            properties = createProperties();
            try {
                writeFile(properties);
            } catch (Exception ex) {
            }
        }
        loadProperties(properties);
    }

    private Properties createProperties() {
        String chrome = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.91 Safari/537.36";
        String cederjTrackerHtmlAddress = "http://www.cederj.edu.br/videoaulas/";
        String cederjTrackerHtmlElement = "#browser>ul>li:last-child>ul>li";
        String rioRedirectResource = "/serverredirect.rio";
        String rioTransferResource = "/riotransfer";
        String rioXmlTagname = "rioserverredirect";

        Properties properties = new Properties();
        properties.setProperty(PROPERTY_USERAGENT, chrome);
        properties.setProperty(PROPERTY_TRACKER, cederjTrackerHtmlAddress);
        properties.setProperty(PROPERTY_TRACKER_ELEMENT, cederjTrackerHtmlElement);
        properties.setProperty(PROPERTY_RIO_TRANSFER, rioTransferResource);
        properties.setProperty(PROPERTY_RIO_REDIRECT, rioRedirectResource);
        properties.setProperty(PROPERTY_RIO_XML_TAG, rioXmlTagname);
        return properties;
    }

    private void loadProperties(Properties properties) {
        this.userAgent = properties.getProperty(PROPERTY_USERAGENT);
        this.tracker = properties.getProperty(PROPERTY_TRACKER);
        this.trackerElement = properties.getProperty(PROPERTY_TRACKER_ELEMENT);
        this.rioRedirect = properties.getProperty(PROPERTY_RIO_REDIRECT);
        this.rioTransfer = properties.getProperty(PROPERTY_RIO_TRANSFER);
        this.rioServerMirrorXmlTagname = properties.getProperty(PROPERTY_RIO_XML_TAG);
    }

    private Properties readFile() throws Exception {
        try {
            File configFile = getConfigFile();
            if (configFile.exists()) {
                FileInputStream fileInput = new FileInputStream(configFile);
                Properties properties = new Properties();
                properties.loadFromXML(fileInput);
                fileInput.close();
                return properties;
            }
            throw new RuntimeException();
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void writeFile(Properties properties) throws Exception {
        try {
            File configFile = getConfigFile();
            if (!configFile.exists()) {
                FileOutputStream fileOut = new FileOutputStream(configFile);
                Properties tmp = new Properties() {
                    @Override
                    public synchronized Enumeration<Object> keys() {
                        return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                    }
                };
                tmp.putAll(properties);
                tmp.storeToXML(fileOut, "Proto's video-lesson downloader");
                fileOut.close();
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    private File getConfigFile() throws URISyntaxException {
        String name = CONFIG_FILE;
        File base = new File(Config.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
        File configFile = new File(base, name);
        return configFile;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getTracker() {
        return tracker;
    }

    public String getTrackerElement() {
        return trackerElement;
    }

    public String getRioRedirect() {
        return rioRedirect;
    }

    public String getRioTransfer() {
        return rioTransfer;
    }

    public String getRioServerMirrorXmlTagname() {
        return rioServerMirrorXmlTagname;
    }

}
