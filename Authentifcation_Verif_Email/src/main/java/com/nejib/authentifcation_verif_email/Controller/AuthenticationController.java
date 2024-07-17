package com.nejib.authentifcation_verif_email.Controller;

import com.nejib.authentifcation_verif_email.Entites.AuthenticationResponse;
import com.nejib.authentifcation_verif_email.Entites.Role;
import com.nejib.authentifcation_verif_email.Entites.User;
import com.nejib.authentifcation_verif_email.Repository.IUserRepository;
import com.nejib.authentifcation_verif_email.Services.IServices.IAuthenticationServices;
import com.nejib.authentifcation_verif_email.Services.ServiceImpl.SendEmailServiceImp;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    public static String uploadDirectory = System.getProperty("user.dir") + "/uploadUser";

    private final IAuthenticationServices authenticationServices;
    private final SendEmailServiceImp sendEmailService;
    private final IUserRepository userRepository;
    @Value("${app.frontend.url}")
    private String frontendUrl;
    @PostMapping("/registerUser")
        public ResponseEntity<User> registerUser(@RequestParam("nom") String nom,
                                              @RequestParam("prenom") String prenom,
                                              @RequestParam("email") String email,
                                              @RequestParam("password") String password,
                                              @RequestParam("numeroTelephone") String numeroTelephone,

                                              @RequestParam("dateNaissance") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateNaissance,
                                              @RequestParam("image") MultipartFile file) throws IOException {
        User user = new User();
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setEmail(email);
        user.setPassword(password);

        user.setNumeroTelephone(numeroTelephone);
        user.setDateNaissance(dateNaissance);
 ;
        Random random = new Random();
        int verificationCode = 1000 + random.nextInt(9000); // Génère un nombre entre 100000 et 999999
        user.setVerificationCode(String.valueOf(verificationCode));

        // Construire le lien de vérification
        String verificationLink = frontendUrl + "?email=" + email ;

        // Envoyer l'email de vérification
        String emailBody = "Votre code de confirmation est : " + verificationCode + "\n\nVeuillez cliquer sur le lien suivant pour activer votre compte : " + verificationLink;
        sendEmailService.sendEmail(email, emailBody, "Confirmation de votre compte");

        user.setRole(Role.USER);
        String originalFilename = file.getOriginalFilename();
        String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
        Path fileNameAndPath = Paths.get(uploadDirectory, uniqueFilename);
        if (!Files.exists(fileNameAndPath.getParent())) {
            Files.createDirectories(fileNameAndPath.getParent());
        }
        Files.write(fileNameAndPath, file.getBytes());
        user.setImage(uniqueFilename);
        User savedAgent = authenticationServices.RegisterUser(user);
        return ResponseEntity.ok(savedAgent);
    }

    @PostMapping("/verifyUser")
    public ResponseEntity<String> verifyEtudiant(@RequestParam("email") String email,
                                                 @RequestParam("code") String code) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
        if (user != null && user.getVerificationCode().equals(code)) {
            user.setVerified(true);
            userRepository.save(user);

            return ResponseEntity.ok("Compte vérifié avec succès.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Code de vérification invalide.");
    }
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody User user) {
        return authenticationServices.login(user.getEmail(), user.getPassword());
    }
}
