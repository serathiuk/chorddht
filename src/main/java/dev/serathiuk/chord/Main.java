package dev.serathiuk.chord;

import dev.serathiuk.chord.client.ChordClient;
import dev.serathiuk.chord.server.ChordServer;
import dev.serathiuk.chord.server.ChordServerConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(Main.class);

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "display a help message")
    private boolean helpRequested = false;

    @CommandLine.Option(names = {"-s", "--server"}, description = "Execute server")
    private boolean server = false;

    @CommandLine.Option(names = {"-c", "--client"}, description = "Execute client")
    private boolean client = false;

    @CommandLine.Option(names = {"-rh", "--remoteHost"}, description = "Remote host address")
    private String remoteHost = "";

    @CommandLine.Option(names = {"-rp", "--remotePort"}, description = "Remote port")
    private int remotePort = 0;

    @CommandLine.Option(names = {"-lh", "--localHost"}, description = "Local host")
    private String localHost = "localhost";

    @CommandLine.Option(names = {"-lp", "--localPort"}, description = "Local port")
    private int localPort = 8000;

    @CommandLine.Option(names = {"-bk", "--bitsKey"}, description = "Number of Bits of Key")
    private int bitsKey= 160;

    private ExecutorService executor;

    public static void main(String[] args) {
        System.exit(new CommandLine(new Main()).execute(args));
    }

    @Override
    public void run() {
        if (server && client) {
            executor  = Executors.newFixedThreadPool(1);
            executor.execute(() -> {
                logger.info("Executing server at {}:{}", localHost, localPort);

                var chordServer = new ChordServer(ChordServerConfigBuilder.aChordServerConfig()
                        .withLocalHost(localHost)
                        .withLocalPort(localPort)
                        .withRemoteHost(remoteHost)
                        .withRemotePort(remotePort)
                        .withNumberOfBitsKey(bitsKey)
                        .build());

                chordServer.run();
            });

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) { }

            new ChordClient(localHost, localPort).run();
        } else if (server) {
            var chordServer = new ChordServer(ChordServerConfigBuilder.aChordServerConfig()
                    .withLocalHost(localHost)
                    .withLocalPort(localPort)
                    .withRemoteHost(remoteHost)
                    .withRemotePort(remotePort)
                    .withNumberOfBitsKey(bitsKey)
                    .build());

            chordServer.run();
        } else if(client) {
            new ChordClient(remoteHost, remotePort).run();
        } else {
            logger.error("The execution needs to be as client or server");
        }
    }

}
