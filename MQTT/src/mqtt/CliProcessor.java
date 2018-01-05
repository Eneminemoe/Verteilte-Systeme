/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.cli.*;
/**
 *
 * @author Jens
 */
public class CliProcessor {
    
     /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CliProcessor.class);

    /** The help message. */
    private static final String HELP_MSG = "mqtt-publisher [OPTIONS] [MESSAGE]";

    /** The one and only instance of CLI processor. */
    private static CliProcessor instance;

    /** The CLI parameters store object. */
    private CliParameters cliParameters;

    /**
     * The static getter for the CLI processor instance.
     *
     * @return The CLI processor instance.
     */
    public static CliProcessor getInstance() {
        if (instance == null)
            instance = new CliProcessor();
        return instance;
    }

    public void parseCliOptions(String[] args) {
        // Command line options.
        Options options = createCliOptions();
        // Command line parser.
        CommandLineParser parser = new DefaultParser();

        try {
            // Parse the command line arguments.
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("h")) {
                printHelp(options);
                System.exit(constants.Constants.EXIT_CODE_SUCCESS);
            }
            if (line.hasOption("b")) {
                this.cliParameters.setBrokerAddress(line.getOptionValue('b'));
            }
            if (line.hasOption("p")) {
                this.cliParameters.setBrokerPort(line.getOptionValue('p'));
            }
            if (line.hasOption("P")) {
                this.cliParameters.setBrokerProtocol(line.getOptionValue('P'));
            }
            if (line.hasOption("t")) {
                this.cliParameters.setTopic(line.getOptionValue('t'));
            }
            if (line.hasOption("x")) {
                this.cliParameters.setProducer(line.getOptionValue('x'));
            }
             if (line.hasOption("s")) {
                this.cliParameters.setStore(line.getOptionValue('s'));
            }
            // Get whatever ist left, after the options have been processed.
            if (line.getArgList() == null || line.getArgList().isEmpty()) {
                LOGGER.info("No message given; using the default message.");
            } else {
                this.cliParameters.setMessage(line.getArgList());
            }

        } catch (MissingOptionException | MissingArgumentException e) {
            LOGGER.error("ERROR: " + e.getMessage() + "\n");
            printHelp(options);
            System.exit(constants.Constants.EXIT_CODE_ERROR);
        } catch (ParseException e) {
            // Oops, something went totally wrong.
            LOGGER.error("ERROR: Parsing failed. Reason: " + e.getMessage());
        }
    }

    /**
     * Creates the command line options for the
     * program.
     *
     * @return An Options object containing all the command line options of the program.
     */
    private Options createCliOptions() {
        // A helper option.
        Option help = Option.builder("h")
                .longOpt("help")
                .desc("Give this help list.")
                .build();
        // The broker address option.
        Option broker = Option.builder("b")
                .longOpt("broker")
                .desc("The broker address.")
                .hasArg()
                .argName("ADDRESS")
                .build();
        // The broker address option.
        Option port = Option.builder("p")
                .longOpt("port")
                .desc("The broker port.")
                .hasArg()
                .argName("PORT")
                .build();
        // The broker address option.
        Option protocol = Option.builder("P")
                .longOpt("protocol")
                .desc("The broker protocol.")
                .hasArg()
                .argName("PROTO")
                .build();
        // The topic option.
        Option topic = Option.builder("t")
                .longOpt("topic")
                .desc("The topic to listen to.")
                .hasArg()
                .argName("TOPIC")
                .build();
        // The producer option.
        Option producer = Option.builder("x")
                .longOpt("Producer")
                .desc("The Name of the Producer.")
                .hasArg()
                .argName("PRODUCER")
                .build();
        // The producer option.
        Option store = Option.builder("s")
                .longOpt("Store")
                .desc("The Name of the Store.")
                .hasArg()
                .argName("STORE")
                .build();

        // Create and add options.
        Options options = new Options();
        options.addOption(help);
        options.addOption(broker);
        options.addOption(port);
        options.addOption(protocol);
        options.addOption(topic);
        options.addOption(producer);
        options.addOption(store);

        // Return options.
        return options;
    }

    /**
     * Prints the help of the command.
     *
     * @param options The command's options.
     */
    private void printHelp(Options options) {
        // A help formatter.
        HelpFormatter formatter = new HelpFormatter();
        // Print help.
        formatter.printHelp(HELP_MSG, options);
    }

    /**
     * A private constructor to avoid
     * instantiation.
     */
    private CliProcessor() {
        this.cliParameters = CliParameters.getInstance();
    }
}
