package com.jitkasem.bitcoin;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import com.jitkasem.bitcoin.model.Etherum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.core.GcpProjectIdProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class GreetingController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private GcpProjectIdProvider projectIdProvider;

    @Autowired
    private EthbookDataRepository ethbookDataRepository;

    private static final Logger log = LoggerFactory.getLogger(GreetingController.class);
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private final AtomicLong fSeq = new AtomicLong();

    @RequestMapping("/greeting")
    public Etherum /*Greeting*/ greeting(@RequestParam(value="name", defaultValue="World") String name) throws IOException {


        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Etherum> response = restTemplate.exchange(
                "https://min-api.cryptocompare.com/data/pricehistorical?fsym=ETH&tsyms=BTC,USD,EUR",
                HttpMethod.GET, entity, Etherum.class);


        Etherum et = response.getBody();

        EthbookData ethbookData = new EthbookData();
        ethbookData.setEthPri((float)et.getCrypto().getUSD());
        ethbookData.setTimeRecord("No time");

        Stream.of(ethbookData).forEach(ethbookDataRepository::save);

        log.info("Number of eth data is " + ethbookDataRepository.count());

        return et;

    }
}