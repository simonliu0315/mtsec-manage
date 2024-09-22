package com.cht.network.monitoring.util;

import com.cht.network.monitoring.service.PingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.List;
import java.util.LinkedList;


public class ProcessUtil {

    private static final Logger log = LoggerFactory.getLogger(ProcessUtil.class);
    /**
     * Executes the given command
     * Returns the output of the process as a List of strings
     * @param command
     * @param charsetName
     * @return List<String>
     */
    public static List<String> executeProcessAndGetOutputAsStringList (
            final String command,
            final String charsetName) {

        // handle exceptions
        try {

            // delegate
            final Runtime runtime = Runtime.getRuntime();
            final Process process = runtime.exec (command);

            // extract output
            final InputStream inputStream = process.getInputStream ();
            final InputStreamReader inputStreamReader = charsetName != null ?
                    new InputStreamReader (inputStream, charsetName) :
                    new InputStreamReader (inputStream);
            final BufferedReader bufferedReader = new BufferedReader (inputStreamReader);
            final List<String> stringList = new LinkedList<String> ();
            while (true) {

                // next line
                final String string = bufferedReader.readLine ();
                if (string == null) {
                    break;
                }

                log.info("{}", string);
                // track
                stringList.add (string);
            }

            // done
            return stringList;
        }
        catch (final Exception e) {

            // propagate
            throw new RuntimeException (e);
        }
    }
}
