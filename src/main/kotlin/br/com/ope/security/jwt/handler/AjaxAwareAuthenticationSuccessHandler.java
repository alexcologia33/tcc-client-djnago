package br.com.ope.security.jwt.handler;

import br.com.ope.model.Usuario;
import br.com.ope.security.jwt.model.token.JwtToken;
import br.com.ope.security.jwt.model.token.JwtTokenFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AjaxAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private final ObjectMapper mapper;
	private final JwtTokenFactory tokenFactory;

	@Autowired
	public AjaxAwareAuthenticationSuccessHandler(final ObjectMapper mapper, final JwtTokenFactory tokenFactory) {
		this.mapper = mapper;
		this.tokenFactory = tokenFactory;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException {
		Usuario userContext = (Usuario) authentication.getPrincipal();
		
		JwtToken accessToken = tokenFactory.createAccessJwtToken(userContext);
		
		Map<String, String> tokenMap = new HashMap<String, String>();
		tokenMap.put("token", accessToken.getToken());

		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		mapper.writeValue(response.getWriter(), tokenMap);

		clearAuthenticationAttributes(request);
	}

	
	protected final void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session == null) {
			return;
		}

		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}
