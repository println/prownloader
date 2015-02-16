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
package proto.cederj.prownloader.thirdparty.pseudohttpclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class GenericPseudoHttpClient implements PseudoHttpClient {

    private boolean isDownloading;
    private String userAgent;
    private CloseableHttpClient client;

    public GenericPseudoHttpClient(String userAgent) {
        this.userAgent = userAgent;
        this.client = getClient();
    }

    public CloseableHttpClient getClient() {
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setUserAgent(userAgent);
        return builder.build();
    }

    @Override
    public InputStream doGet(String url) throws IOException {
        //CloseableHttpClient client = getClient();
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);

        HttpEntity entity = null;

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IOException("Unexpected status code: " + response.getStatusLine().getStatusCode());
        }
        entity = response.getEntity();
        InputStream inputStream = entity.getContent();
        return inputStream;
    }

    @Override
    public void doDownload(String url, File output, DownloadMonitor monitor) throws IOException {
        isDownloading = true;
        CloseableHttpClient client = getClient();
        monitor = new DownloadMonitorWrapper(monitor);

        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = client.execute(get);

        HttpEntity entity = null;

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IOException("Unexpected status code: " + response.getStatusLine().getStatusCode());
        }
        entity = response.getEntity();

        Header[] headers = response.getHeaders("Content-Length");

        String fileSize = null;
        if (headers.length > 0) {
            fileSize = headers[0].getValue();
        }

        int size = -1;
        if (fileSize != null) {
            size = Integer.parseInt(fileSize);
        }

        if (output.exists()) {
            //do not remove/redownload file if it is correct
            if (output.length() == size) {
                monitor.status(size, size, 100, url);
                return;
            }
            output.delete();
        }

        OutputStream outputStream = new FileOutputStream(output);
        int read = 0;
        byte[] bytes = new byte[1024];
        CountingInputStream count = new CountingInputStream(entity.getContent());

        int percent = 0;
        while ((read = count.read(bytes)) != -1 && isDownloading) {
            outputStream.write(bytes, 0, read);
            int current = count.getCount();

            if (size > -1) {
                int temp = (current / (size / 100));
                if (temp > percent) {
                    percent = temp;
                    monitor.status(size, current, percent, url);
                }
            } else {
                monitor.status(size, current, percent, url);
            }
        }
        outputStream.close();
        response.close();
        
        count.close();
        client.close();
        if(!isDownloading){
            FileDeleteStrategy.FORCE.delete(output);
        }        
        isDownloading = false;
    }

    @Override
    public void stopDownload() {
        isDownloading = false;
    }
}
