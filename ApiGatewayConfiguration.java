package com.gateway.configuration;

import java.util.function.Function;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

	@Bean
	public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {

		Function<PredicateSpec, Buildable<Route>> routeFunction = p -> p.path("/get")
				.filters(f -> f.addRequestHeader("MyUrl", "Uri").addRequestParameter("param", "test"))
				.uri("http://httpbin.org:80");
		return builder.routes().route(routeFunction)
				.route(p -> p.path("/currencyconversion-feign/**").uri("lb://currency-conversion-service"))
				.route(p -> p.path("/currencyconversion/**").uri("lb://currency-conversion-service"))
				.route(p -> p.path("/currency-exchange/**").uri("lb://currency-exchange-service"))
				.route(p -> p.path("/currency-exchange-new/**")
						.filters(f -> f.rewritePath("/currency-exchange-new/(?<segment>.*)",
								"/currency-exchange/${segment}")).uri("lb://currency-exchange-service"))

				.build();
	}

}
