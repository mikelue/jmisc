package guru.mikelue.misc.springframework.data.web;

import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

public interface MockServerWebExchangeUtils {
	public final static MultiValueMap<String, String> EmptyMultiValueMap = new LinkedMultiValueMap<>();
	public final static ServerWebExchange EmptyWebExchange = build(EmptyMultiValueMap, EmptyMultiValueMap);

	public static ServerWebExchange build(
		MultiValueMap<String, String> httpHeaders,
		MultiValueMap<String, String> httpQueryString
	) {
		return MockServerWebExchange.from(
			MockServerHttpRequest.get("/mock-srv")
				.headers(httpHeaders)
				.queryParams(httpQueryString)
				.build()
		);
	}
}
