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
import org.apache.commons.io.input.CountingInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class GenericPseudoHttpClient implements PseudoHttpClient {

    private HttpClient client;

    public GenericPseudoHttpClient(String userAgent) {
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setUserAgent(userAgent);
        this.client = builder.build();
    }

    @Override
    public InputStream doGet(String url) throws IOException {
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
        HttpGet get = new HttpGet(url);
        HttpResponse response = client.execute(get);

        HttpEntity entity = null;

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IOException("Unexpected status code: " + response.getStatusLine().getStatusCode());
        }
        entity = response.getEntity();

        Header[] headers = response.getHeaders("Content-Length");

        String s = null;
        if (headers.length > 0) {
            s = headers[0].getValue();
        }

        if (output.exists()) {
            output.delete();
        }

        int size = -1;
        if (s != null) {
            size = Integer.parseInt(s);
        }

        OutputStream outputStream = new FileOutputStream(output);
        int read = 0;
        byte[] bytes = new byte[1024];
        CountingInputStream count = new CountingInputStream(entity.getContent());

        int percent = 0;
        while ((read = count.read(bytes)) != -1) {
            outputStream.write(bytes, 0, read);
            int current = count.getCount();

            if (size > -1) {
                int temp = (current / (size / 100));
                if (temp > percent) {
                    percent = temp;
                    monitor.status(size, current, percent, url);
                }
            }else{                
                monitor.status(size, current, percent, url);
            }
        }
    }
}
