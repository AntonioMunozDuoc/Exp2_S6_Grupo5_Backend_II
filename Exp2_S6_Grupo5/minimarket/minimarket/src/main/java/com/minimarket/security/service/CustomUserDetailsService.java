package com.minimarket.security.service;

import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.security.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//esta clase busca a los usuarios en la BD y los carga para la autenticacion
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    private static final Logger logger =
        LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

                Usuario usuario = usuarioRepository.findByUsername(username)
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Intento login fallido para usuario: {}",
                                    username
                            );

                            return new UsernameNotFoundException(
                                    "Credenciales inválidas"
                            );
                        });

                return new CustomUserDetails(usuario);
            }
    }
