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

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class DownloadMonitorWrapper implements DownloadMonitor {

    private DownloadMonitor monitor;

    public DownloadMonitorWrapper(DownloadMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void status(long fileSize, long downloaded, int percent, String url) {
        if (monitor != null) {
            monitor.status(fileSize, downloaded, percent, url);
        }
    }
}
