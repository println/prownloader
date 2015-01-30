/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proto.cederj.prownloader.thirdparty.pseudohttpclient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Your Proto <live.proto at hotmail.com>
 */
public interface PseudoHttpClient {

    InputStream doGet(String url) throws IOException;

    void doDownload(String url, File output, DownloadMonitor monitor) throws IOException;
}
