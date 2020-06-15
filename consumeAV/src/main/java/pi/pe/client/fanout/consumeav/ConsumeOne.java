/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pi.pe.client.fanout.consumeav;

import com.phono.srtplight.Log;
import pe.pi.client.small.App;
import pe.pi.client.small.screen.SmallScreen;
import pe.pi.sctp4j.sctp.SCTPStreamListener;
import java.util.function.BiFunction;
import pe.pi.client.base.certHolders.JksCertMaker;
import pe.pi.client.av.ConsumerAVMux;
import pe.pi.client.endpoints.rtmedia.VideoRelayMux;
import pe.pi.sctp4j.sctp.Association;
import pe.pi.sctp4j.sctp.AssociationListener;
import pe.pi.sctp4j.sctp.SCTPStream;

/**
 *
 * @author thp
 *
 *
 *
 *
 *
 *
 */
public class ConsumeOne {

    class LogScreen implements SmallScreen {

        @Override
        public void init() throws UnsupportedOperationException {
        }

        @Override
        public void clearScreen() {
        }

        @Override
        public void drawQr(String finger) {
            Log.info("My id is " + finger);
        }

        @Override
        public void showMessage(String message) {
            Log.info("Message:" + message);
        }

        @Override
        public void setStatus(String s, String string) {
            Log.info("Status [" + s + "]=" + string);
        }

    };

    ConsumerAVMux consMux;
    SmallScreen screen;
    AssociationListener assocListener;
    BiFunction<String, SCTPStream, SCTPStreamListener> mapper;

    ConsumeOne() {
        screen = new LogScreen();
        assocListener = new AssociationListener() {
            public void onAssociated(Association a) {
                Log.info("Associated with ice");
                makeStream(a);
            }

            public void onDisAssociated(Association a) {

            }

            public void onDCEPStream(SCTPStream s, String label, int type) throws Exception {
                // never called - we want the mapper to do this

            }

            public void onRawStream(SCTPStream s) {
                // absolutely never called - we want the stack to do this

            }
        };
        mapper = (l, s) -> {

            Log.info("mapping Datachannel label");
            SCTPStreamListener ret = null;
            try {
                switch (l) {
                    case "birdsysub":
                        Log.debug("Creating consumer" + l);
                        consMux = new ConsumerAVMux(s, l, screen);
                        ret = consMux.getListener();
                        break;
                    case "videorelay":
                        Log.debug("Creating " + l);
                        ret = new VideoRelayMux(s, l, screen, consMux);
                        break;
                }
            } catch (Exception x) {
                Log.error("Cant map label because.... " + x.getMessage());
            }
            return ret;
        };
    }

    public void makeStream(Association asso) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                if (asso != null) {
                    Log.info(" now create stream");
                    asso.mkStream("birdsysub");
                }
            } catch (Exception x) {
                Log.error("Error dealing creating stream " + x.getMessage());
            }
        }).start();
    }

    public void start(String arg) {
        Log.info(" arg " + arg);
        try {
            String tempDir = System.getProperty("java.io.tmpdir");

            JksCertMaker c = new JksCertMaker(tempDir);
            if (c.hasMaster()) {
                var master = c.getMasterCert();
                c.removeFriendCert("master", master);
                Log.info("removed master");

            }

            App.connectToPair(screen, mapper, tempDir, assocListener, arg);
        } catch (Exception x) {
            Log.error("Error deleting  master " + x.getMessage());

        }
    }

    public static void main(String args[]){
        Log.setLevel(Log.INFO);
        ConsumeOne me = new ConsumeOne();
       
        me.start("5A41E5BE51A7C00435485FBCC176D1B1FD81F2E5759A3993D39EAA6F26A775A2:EE4AAC99F3086D727533B820C4BD8D2F");
    }
}

