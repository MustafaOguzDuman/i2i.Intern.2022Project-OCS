package com.i2i;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ClientActor  extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    public static final Logger logger = LogManager.getLogger(Client.class);

    private ActorSelection selection = getContext().actorSelection("akka.tcp://AkkaRemoteServer@127.0.0.1:2552/user/Listener");
    LocationGenerator locationGenerator = new LocationGenerator();
    ServiceGenerator serviceGenerator = new ServiceGenerator();
    AmountGenerator amountGenerator = new AmountGenerator();
    OPNumberGenerator opNumberGenerator = new OPNumberGenerator();

    @Override
    public void onReceive(Object message) throws Exception {
        if (message.equals("DoCalcs")) {

            log.info("Got a calc job, send it to the remote calculator");
            selection.tell(new Message.Sum(2, 2), getSelf());

        } else if (message instanceof Message.Result) {
            Message.Result result = (Message.Result) message;
            log.info("Got result back from calculator: {}", result.getResult());
        }
        else{
            String messageStr = message.toString();
            String[] twoMSISDNs = messageStr.split("x");
            String preMSISDN = twoMSISDNs[0];
            String preOPNumber = twoMSISDNs[1];


            String location = locationGenerator.getLocation();
            String service = serviceGenerator.getService();
            int amount = amountGenerator.getAmount(service);

            String opnumber = opNumberGenerator.getOPNumber(service, preOPNumber);

            logger.info("MSISDN: " + preMSISDN);
            logger.info("OPNumber " + opnumber);
            logger.warn("Location: " + location);
            logger.error("Service: " + service);
            logger.fatal("Amount: " + amount);
            logger.fatal("");
            selection.tell(new Message.Usage(preMSISDN, opnumber, location, service, amount), getSelf());
        }


    }
}
