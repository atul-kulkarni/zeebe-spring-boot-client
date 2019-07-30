package com.example.zeebe;

import java.time.Instant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.spring.client.EnableZeebeClient;
import io.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableZeebeClient
@Slf4j
public class ZeebeClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZeebeClientApplication.class, args);
	}

	private static void logJob(final ActivatedJob job) {
		log.info(
				"complete job\n>>> [type: {}, key: {}, element: {}, workflow instance: {}]\n{deadline; {}]\n[headers: {}]\n[variables: {}]",
				job.getType(), job.getKey(), job.getElementId(), job.getWorkflowInstanceKey(),
				Instant.ofEpochMilli(job.getDeadline()), job.getCustomHeaders(), job.getVariables());
	}

	@ZeebeWorker(type = "initiate-payment", name = "initiate-payment")
	public void handleInitiatePaymentJob(final JobClient client, final ActivatedJob job) {
		logJob(job);
		client.newCompleteCommand(job.getKey()).send().join();
	}

	@ZeebeWorker(type = "ship-with-insurance", name = "ship-with-insurance")
	public void handleShipWithInsuranceJob(final JobClient client, final ActivatedJob job) {
		logJob(job);
		client.newCompleteCommand(job.getKey()).send().join();
	}

	@ZeebeWorker(type = "ship-without-insurance", name = "ship-without-insurance")
	public void handleShipWithoutInsuranceJob(final JobClient client, final ActivatedJob job) {
		logJob(job);
		client.newCompleteCommand(job.getKey()).send().join();
	}
}
