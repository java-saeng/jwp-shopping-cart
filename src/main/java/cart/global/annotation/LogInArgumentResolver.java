package cart.global.annotation;

import cart.auth.AuthAccount;
import cart.global.exception.auth.CanNotFoundHeaderException;
import cart.global.exception.auth.InvalidAuthorizationTypeException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class LogInArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String BASIC_TYPE = "Basic";
    private static final String DELIM = ":";

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LogIn.class) &&
                AuthAccount.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) throws Exception {

        final String header = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (hasNotHeader(header)) {
            throw new CanNotFoundHeaderException();
        }

        if (isBasicAuthFrom(header)) {
            final String decodedString = decodingByBase64(header);
            final String[] credentials = decodedString.split(DELIM);

            final String email = credentials[0];
            final String password = credentials[1];

            return new AuthAccount(email, password);
        }

        throw new InvalidAuthorizationTypeException();
    }

    private String decodingByBase64(final String header) {
        final String authHeaderValue = header.substring(BASIC_TYPE.length()).trim();
        final byte[] decodeBytes = Base64.getDecoder().decode(authHeaderValue);

        return new String(decodeBytes, StandardCharsets.UTF_8);
    }

    private boolean hasNotHeader(final String header) {
        return header == null;
    }

    private boolean isBasicAuthFrom(final String header) {
        return header.toLowerCase().startsWith((BASIC_TYPE.toLowerCase()));
    }
}
