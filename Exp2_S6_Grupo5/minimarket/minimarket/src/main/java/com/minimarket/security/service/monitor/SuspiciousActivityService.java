package com.minimarket.security.service.monitor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
    public class SuspiciousActivityService {

    private static final Logger logger =
            LoggerFactory.getLogger(
                    SuspiciousActivityService.class
            );

    // Configuración

    private static final int MAX_LOGIN_ATTEMPTS = 5;

    private static final int MAX_REQUESTS_PER_IP = 100;

    // username: intentos de login

    private final Map<String, Integer>
            failedLogins =
            new ConcurrentHashMap<>();

    // ip requests

    private final Map<String, Integer>
            ipRequests =
            new ConcurrentHashMap<>();

    // username: fecha último fallo

    private final Map<String, LocalDateTime>
            lastFailedLogin =
            new ConcurrentHashMap<>();


    public void registerFailedLogin(
            String username
    ) {

        int attempts =
                failedLogins.merge(
                        username,
                        1,
                        Integer::sum
                );

        lastFailedLogin.put(
                username,
                LocalDateTime.now()
        );

        if (
                attempts
                >=
                MAX_LOGIN_ATTEMPTS
        ) {

            logger.warn(

                "Actividad sospechosa: {} intentos fallidos para usuario {}",

                attempts,

                username

            );

        }

    }


    public void registerSuccessfulLogin(
            String username
    ) {

        failedLogins.remove(
                username
        );

        lastFailedLogin.remove(
                username
        );

    }


    public void registerRequest(
            String ip
    ) {

        int requests =

                ipRequests.merge(

                        ip,

                        1,

                        Integer::sum

                );

        if (

                requests
                >=
                MAX_REQUESTS_PER_IP

        ) {

            logger.warn(

                    "Actividad sospechosa: exceso de solicitudes desde IP {}",

                    ip

            );

        }

    }


    public void registerExpiredToken(
            String username
    ) {

        logger.warn(

                "Token expirado detectado para usuario {}",

                username

        );

    }


    public void registerUnauthorizedAccess(
            String endpoint
    ) {

        logger.warn(

                "Acceso no autorizado al endpoint {}",

                endpoint

        );

    }


    public void registerCrudFailure(
            String operation
    ) {

        logger.warn(

                "Error repetido en operación {}",

                operation

        );

    }

}

