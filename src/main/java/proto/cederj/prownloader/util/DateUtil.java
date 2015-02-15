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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author Felipe Santos <live.proto at hotmail.com>
 */
public class DateUtil {

    public Calendar parse(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getFormat().parse(date));
        return calendar;
    }

    public String generate(Calendar calendar) {
        return getFormat().format(calendar.getTime());
    }

   private SimpleDateFormat getFormat() {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", new Locale("pt", "BR"));
        format.setTimeZone(TimeZone.getDefault());
        return format;
    }
}

