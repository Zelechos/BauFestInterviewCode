package com.eldarinterview.ejercicio2.services;

import com.eldarinterview.ejercicio2.models.User;
import com.eldarinterview.ejercicio2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Servicio para gestionar las operaciones relacionadas con los usuarios.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param user El objeto User que se desea registrar.
     * @return El usuario registrado después de ser guardado en la base de datos.
     */
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Busca un usuario en la base de datos por su DNI.
     *
     * @param dni El DNI del usuario a buscar.
     * @return Un Optional que contiene el usuario encontrado, o vacío si no se encuentra.
     */
    public Optional<User> findUserByDni(String dni) {
        return userRepository.findByDni(dni);
    }

    /**
     * Actualiza la información de un usuario existente.
     *
     * @param dni El DNI del usuario que se desea actualizar.
     * @param user Un objeto User que contiene los nuevos datos del usuario.
     * @return El usuario actualizado después de ser guardado en la base de datos.
     * @throws RuntimeException si el usuario no se encuentra.
     */
    public User updateUser(String dni, User user) {
        User existingUser = userRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Actualizar solo si el nuevo valor no es nulo
        if (user.getFirstName() != null) {
            existingUser.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            existingUser.setLastName(user.getLastName());
        }
        if (user.getDni() != null) {
            existingUser.setDni(user.getDni());
        }
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getBirthDate() != null) {
            existingUser.setBirthDate(user.getBirthDate());
        }

        return userRepository.save(existingUser);
    }

    /**
     * Elimina un usuario de la base de datos por su DNI.
     *
     * @param dni El DNI del usuario que se desea eliminar.
     * @throws RuntimeException si el usuario no se encuentra.
     */
    public void deleteUser(String dni) {
        User existingUser = userRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(existingUser);
    }
}
