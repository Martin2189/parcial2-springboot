
package com.parcial2.demo.Controllers;

import com.parcial2.demo.dto.LoginRequest;
import com.parcial2.demo.dto.RegistroRequest;
import com.parcial2.demo.models.Usuario;
import com.parcial2.demo.security.JwtService;
import com.parcial2.demo.Service.usuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import com.parcial2.demo.security.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final usuarioService usuarioService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(
            @Valid @RequestBody RegistroRequest request) {

        try {
            Usuario usuario = new Usuario();
            usuario.setUsername(request.getUsername());
            usuario.setPassword(request.getPassword());

            Usuario guardado = usuarioService.guardar(usuario);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "mensaje", "Usuario registrado correctamente",
                            "username", guardado.getUsername()
                    ));


        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "error", "Error al guardar el usuario",
                            "detalle", e.getMostSpecificCause().getMessage()
                    ));
        }

    }


    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(
                        request.getUsername()
                );

        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "tipo", "Bearer",
                "expiraEnMilisegundos", 3600000
        ));
    }

}
