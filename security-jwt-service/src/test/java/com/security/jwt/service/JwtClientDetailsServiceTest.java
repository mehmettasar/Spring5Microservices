package com.security.jwt.service;

import com.security.jwt.configuration.cache.CacheConfiguration;
import com.security.jwt.exception.ClientNotFoundException;
import com.security.jwt.model.JwtClientDetails;
import com.security.jwt.repository.JwtClientDetailsRepository;
import com.spring5microservices.common.service.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class JwtClientDetailsServiceTest {

    @Mock
    private CacheConfiguration mockCacheConfiguration;

    @Mock
    private CacheService mockCacheService;

    @Mock
    private JwtClientDetailsRepository mockJwtClientDetailsRepository;

    private JwtClientDetailsService jwtClientDetailsService;

    @BeforeEach
    public void init() {
        jwtClientDetailsService = new JwtClientDetailsService(mockCacheConfiguration, mockCacheService, mockJwtClientDetailsRepository);
    }


    static Stream<Arguments> findByClientIdTestCases() {
        JwtClientDetails jwtClientDetails = JwtClientDetails.builder().clientId("ItDoesNotCare").build();
        return Stream.of(
                //@formatter:off
                //            clientId,                repositoryResult,       cacheServiceResult,   expectedException,               expectedResult
                Arguments.of( null,                    empty(),                null,                 ClientNotFoundException.class,   null ),
                Arguments.of( "NotFound",              empty(),                null,                 ClientNotFoundException.class,   null ),
                Arguments.of( "FoundOnlyInDatabase",   of(jwtClientDetails),   null,                 null,                            jwtClientDetails ),
                Arguments.of( "FoundInCache",          empty(),                jwtClientDetails,     null,                            jwtClientDetails )
        ); //@formatter:on
    }

    @ParameterizedTest
    @MethodSource("findByClientIdTestCases")
    @DisplayName("findByClientId: test cases")
    public void findByClientId_testCases(String clientId, Optional<JwtClientDetails> repositoryResult, JwtClientDetails cacheServiceResult,
                                         Class<? extends Exception> expectedException, JwtClientDetails expectedResult) {

        when(mockJwtClientDetailsRepository.findByClientId(clientId)).thenReturn(repositoryResult);
        when(mockCacheService.get(any(), eq(clientId))).thenReturn(ofNullable(cacheServiceResult));
        if (null != expectedException) {
            assertThrows(expectedException, () -> jwtClientDetailsService.findByClientId(clientId));
        }
        else {
            assertEquals(expectedResult, jwtClientDetailsService.findByClientId(clientId));
        }
        findByClientId_verifyInvocations(clientId, repositoryResult, cacheServiceResult);
    }

    private void findByClientId_verifyInvocations(String clientId, Optional<JwtClientDetails> repositoryResult, JwtClientDetails cacheServiceResult) {
        // Found jwtClientDetails only in database
        if (repositoryResult.isPresent() && null == cacheServiceResult) {
            verify(mockJwtClientDetailsRepository, times(1)).findByClientId(eq(clientId));
            verify(mockCacheService, times(1)).get(any(), eq(clientId));
            verify(mockCacheService, times(1)).put(any(), eq(clientId), eq(repositoryResult.get()));
        }
        // Found jwtClientDetails in cache
        if (null != cacheServiceResult) {
            verify(mockJwtClientDetailsRepository, times(0)).findByClientId(eq(clientId));
            verify(mockCacheService, times(1)).get(any(), eq(clientId));
            verify(mockCacheService, times(0)).put(any(), any(), any());
        }
        // Not found jwtClientDetails neither in cache nor database
        if (!repositoryResult.isPresent() && null == cacheServiceResult) {
            int expectedInvocations = null == clientId ? 0 : 1;
            verify(mockJwtClientDetailsRepository, times(expectedInvocations)).findByClientId(eq(clientId));
            verify(mockCacheService, times(expectedInvocations)).get(any(), eq(clientId));
            verify(mockCacheService, times(0)).put(any(), any(), any());
        }
    }

}