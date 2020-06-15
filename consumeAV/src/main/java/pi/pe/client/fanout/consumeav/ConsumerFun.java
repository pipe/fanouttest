/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pi.pe.client.fanout.consumeav;

/**
 *
 * @author thp
 */
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.phono.srtplight.Log;
import com.phono.srtplight.LogFace;
import java.io.IOException;

public class ConsumerFun implements HttpFunction {
static {
            System.setProperty("java.net.preferIPv4Stack" , "true");
}
    @Override
    public void service(HttpRequest request, HttpResponse response)
            throws IOException {
        Log.setLevel(Log.INFO);
        var argO = request.getQuery();
        var writer = response.getWriter();
        if (argO.isPresent()) {
            ConsumeOne cons = new ConsumeOne();
            Log.setLogger(new LogFace() {
                public void e(String string) {
                    logWrite("Error:" + string);
                }

                public void d(String string) {
                    logWrite("Debug:" + string);

                }

                public void w(String string) {
                    logWrite("Warn:" + string);

                }

                public void v(String string) {
                    logWrite("Verb:" + string);

                }

                public void i(String string) {
                    logWrite("Error:" + string);
                }

                void logWrite(String s) {
                    try {
                        writer.write("Error:" + s);
                        writer.newLine();
                    } catch (Exception x) {
;
                    }

                }
            });
            cons.start(argO.get());
            try {Thread.sleep(100000);} catch (Exception x) {}
            writer.write("Hello cons");
        } else {
            writer.write("No cons");
        }

    }
}
