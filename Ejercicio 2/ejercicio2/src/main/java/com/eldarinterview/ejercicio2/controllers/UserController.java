package com.eldarinterview.ejercicio2.controllers;

import com.eldarinterview.ejercicio2.models.User;
import com.eldarinterview.ejercicio2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador REST para gestionar las operaciones relacionadas con los usuarios.
 *
 * Este controlador proporciona endpoints para registrar, obtener, actualizar y
 * eliminar usuarios en el sistema. Los datos de los usuarios son manejados
 * a través del servicio UserService.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param user El objeto User que contiene la información del nuevo usuario.
     * @return ResponseEntity con el usuario creado y un código de estado 201 (CREATED).
     */
    @PostMapping("/alta")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /**
     * Obtiene un usuario basado en su DNI.
     *
     * @param dni El DNI del usuario a buscar.
     * @return ResponseEntity con el usuario encontrado y un código de estado 200 (OK),
     *         o un código de estado 404 (NOT FOUND) si el usuario no se encuentra.
     */
    @GetMapping("/{dni}")
    public ResponseEntity<User> getUserByDni(@PathVariable String dni) {
        Optional<User> user = userService.findUserByDni(dni);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Actualiza la información de un usuario existente.
     *
     * @param dni El DNI del usuario a actualizar.
     * @param user El objeto User que contiene los nuevos datos del usuario.
     * @return ResponseEntity con el usuario actualizado y un código de estado 200 (OK).
     */
    @PutMapping("/{dni}")
    public ResponseEntity<User> updateUser(@PathVariable String dni, @RequestBody User user) {
        User updatedUser = userService.updateUser(dni, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Elimina un usuario basado en su DNI.
     *
     * @param dni El DNI del usuario a eliminar.
     * @return ResponseEntity con un código de estado 204 (NO CONTENT) si se elimina con éxito.
     */
    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> deleteUser(@PathVariable String dni) {
        userService.deleteUser(dni);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Método de prueba que devuelve un saludo.
     *
     * @return Un saludo como una cadena.
     */
    @GetMapping("/manu")
    public String getHello() {
        return "Hello Manu|!!!";
    }
}
